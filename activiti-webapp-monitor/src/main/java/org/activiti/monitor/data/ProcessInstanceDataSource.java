package org.activiti.monitor.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.query.Query;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

public class ProcessInstanceDataSource implements GridDataSource {
	private final String processDefinitionId;
	private final ProcessPath parentProcess;
	Map<String, String> processDefinitionNames;

	private List<Instance> pdfDaoList;
	private int startIndex;
	private final SearchParameters searchParameters;
	private final ProcessEngine processEngine;
	private final RepositoryService repositoryService;
	private final HistoryService historyService;
	private final RuntimeService runtimeService;

	public ProcessInstanceDataSource(ProcessEngine processEngine,
			ProcessPath parentProcess, String processDefinitionId,
			SearchParameters searchParameters) {
		this.processEngine = processEngine;
		repositoryService = processEngine.getRepositoryService();
		historyService = processEngine.getHistoryService();
		runtimeService = processEngine.getRuntimeService();
		this.parentProcess = parentProcess;
		this.processDefinitionId = processDefinitionId;
		this.searchParameters = searchParameters;
	}

	@Override
	public int getAvailableRows() {
		int count = parentProcess == null ? (int) buildQuery().count()
				: listProcessesFromParent(null).size();
		return count;
	}

	@Override
	public void prepare(int startIndex, int endIndex,
			List<SortConstraint> sortConstraints) {
		this.startIndex = startIndex;

		pdfDaoList = new ArrayList<Instance>();

		if (parentProcess == null) {
			listRootProcesses(startIndex, endIndex, sortConstraints);
		} else {
			listProcessesFromParent(sortConstraints);
		}

		System.out.println("prepared " + pdfDaoList.size());
	}

	private List<Instance> listProcessesFromParent(
			List<SortConstraint> sortConstraints) {
		List<Instance> subprocesses = recursiveSubprocesses(parentProcess
				.getId());
		if (sortConstraints != null && !sortConstraints.isEmpty()) {
			SortConstraint sortConstraint = sortConstraints.get(0);
			final Column sortColumn = Column.from(sortConstraint
					.getPropertyModel().getPropertyName());

			Collections.sort(subprocesses, new Comparator<Instance>() {
				@Override
				public int compare(Instance o1, Instance o2) {
					switch (sortColumn) {
					case BK:
						return o1.businessKey.compareTo(o2.businessKey);
					case ENDDATE:
						return o1.getEndDate().compareTo(o2.getEndDate());
					case ENDSTATUS:
						return o1.getEndStatus().compareTo(o2.getEndStatus());
					case NAME:
						return o1.getName().compareTo(o2.getName());
					case STARTDATE:
						return o1.getStartDate().compareTo(o2.getStartDate());
					default:
						return 0;
					}
				}
			});
			if (sortConstraint.getColumnSort() == ColumnSort.DESCENDING)
				Collections.reverse(subprocesses);
		}
		pdfDaoList = subprocesses;
		return subprocesses;
	}

	private void listRootProcesses(int startIndex, int endIndex,
			List<SortConstraint> sortConstraints) {
		if (isDefined(searchParameters.getActivityId())) {
			ExecutionQuery query = buildExecutionQuery();
			List<Execution> list = query.listPage(startIndex, endIndex
					- startIndex + 1);
			for (Execution execution : list) {
				pdfDaoList.add(copyInstanceDAO(execution));
			}
		} else {
			HistoricProcessInstanceQuery query = buildHistoryQuery();
			if (!sortConstraints.isEmpty()) {
				SortConstraint sortConstraint = sortConstraints.get(0);
				Column sortColumn = Column.from(sortConstraint
						.getPropertyModel().getPropertyName());
				System.out.println(sortColumn);
				switch (sortColumn) {
				case BK:
					query = query.orderByProcessInstanceBusinessKey();
					break;
				case ENDDATE:
				case ENDSTATUS: // NOT really implemented here
					query = query.orderByProcessInstanceEndTime();
					break;
				case NAME:
					query = query.orderByProcessInstanceId();
					break;
				case STARTDATE:
					query = query.orderByProcessInstanceStartTime();
					break;
				}
				switch (sortConstraint.getColumnSort()) {
				case ASCENDING:
					query = query.asc();
					break;
				case DESCENDING:
					query = query.desc();
					break;
				}
			}

			List<HistoricProcessInstance> historyProcessInstanceList = query
					.listPage(startIndex, endIndex - startIndex + 1);

			for (HistoricProcessInstance processHistoryInstance : historyProcessInstanceList) {
				pdfDaoList.add(copyInstanceDAO(processHistoryInstance));
			}
		}
	}

	private Query<?, ?> buildQuery() {
		return isDefined(searchParameters.getActivityId()) ? buildExecutionQuery()
				: buildHistoryQuery();
	}

	private HistoricProcessInstanceQuery buildHistoryQuery() {
		HistoricProcessInstanceQuery query = historyService
				.createHistoricProcessInstanceQuery();
		query = query.processDefinitionId(processDefinitionId);
		String businessKey = searchParameters.getBusinessKey();
		if (isDefined(businessKey))
			query = query.processInstanceBusinessKey(businessKey);
		Date start = searchParameters.getStart();
		if (start != null)
			query = query.startDateOn(start);
		Date end = searchParameters.getEnd();
		if (end != null)
			query = query.finishDateOn(end);
		return query;
	}

	private ExecutionQuery buildExecutionQuery() {
		ExecutionQuery query = runtimeService.createExecutionQuery();
		query = query.processDefinitionId(processDefinitionId);
		String businessKey = searchParameters.getBusinessKey();
		if (isDefined(businessKey))
			query = query.processInstanceBusinessKey(businessKey);
		String activityId = searchParameters.getActivityId();
		if (isDefined(activityId))
			query = query.activityId(activityId);
		return query;
	}

	private List<Instance> recursiveSubprocesses(String parentProcessId) {
		List<Instance> subprocesses = new ArrayList<Instance>();
		for (HistoricProcessInstance historicProcessInstance : historyService
				.createHistoricProcessInstanceQuery()
				.superProcessInstanceId(parentProcessId).list()) {

			subprocesses.add(copyInstanceDAO(historicProcessInstance));
			subprocesses.addAll(recursiveSubprocesses(historicProcessInstance
					.getId()));
		}
		return subprocesses;
	}

	@Override
	public Object getRowValue(int index) {
		System.out.println(index);
		Instance res = pdfDaoList.get(index - startIndex);
		return res;
	}

	@Override
	public Class getRowType() {
		return Instance.class;
	}

	private Instance copyInstanceDAO(HistoricProcessInstance h) {
		Instance p = new Instance();
		p.setId(h.getId());
		p.setName(getProcessDefinitionName(h.getProcessDefinitionId()) + " - "
				+ h.getId());
		p.setStartDate(h.getStartTime());
		p.setEndDate(h.getEndTime());
		p.setBusinessKey(h.getBusinessKey());
		p.setEndStatus(h.getEndActivityId());
		return p;
	}

	private Instance copyInstanceDAO(Execution h) {
		return copyInstanceDAO(getHistory(h.getProcessInstanceId()));
	}

	private String getProcessDefinitionName(String processDefinitionId) {
		if (processDefinitionNames == null)
			processDefinitionNames = new HashMap<String, String>();
		if (!processDefinitionNames.containsKey(processDefinitionId)) {
			processDefinitionNames.put(processDefinitionId,
					repositoryService.createProcessDefinitionQuery()
							.processDefinitionId(processDefinitionId)
							.singleResult().getName());
		}
		return processDefinitionNames.get(processDefinitionId);
	}

	static enum Column {
		NAME("name"), BK("businessKey"), STARTDATE("startDate"), ENDDATE(
				"endDate"), ENDSTATUS("endStatus");

		private final String name;

		private Column(String name) {
			this.name = name;
		}

		static Column from(String col) {
			for (Column column : values()) {
				if (column.name.equals(col))
					return column;
			}
			throw new IllegalArgumentException(col);
		}
	}

	private static boolean isDefined(String value) {
		return value != null && value.trim().length() > 0;
	}

	private HistoricProcessInstance getHistory(String processInstanceId) {
		return historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
	}
}
