package org.activiti.monitor.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

public class ProcessInstanceDataSource implements GridDataSource {
	private final RepositoryService repositoryService;
	private final HistoryService historyService;
	private final String processDefinitionId;
	private final ProcessPath parentProcess;
	Map<String, String> processDefinitionNames;

	private List<Instance> pdfDaoList;
	private int startIndex;
	private final SearchParameters searchParameters;

	public ProcessInstanceDataSource(RepositoryService repositoryService,
			HistoryService historyService, ProcessPath parentProcess,
			String processDefinitionId, SearchParameters searchParameters) {
		this.repositoryService = repositoryService;
		this.historyService = historyService;
		this.parentProcess = parentProcess;
		this.processDefinitionId = processDefinitionId;
		this.searchParameters = searchParameters;
	}

	@Override
	public int getAvailableRows() {
		System.out.println(parentProcess);
		int count = parentProcess == null ? (int) buildQuery().count()
				: listProcessesFromParent(null).size();
		System.out.println("count : " + count);
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

			Collections.sort(subprocesses,
					new Comparator<Instance>() {
						@Override
						public int compare(Instance o1,
								Instance o2) {
							switch (sortColumn) {
							case BK:
								return o1.businessKey.compareTo(o2.businessKey);
							case ENDDATE:
								return o1.getEndDate().compareTo(
										o2.getEndDate());
							case ENDSTATUS:
								return o1.getEndStatus().compareTo(
										o2.getEndStatus());
							case NAME:
								return o1.getName().compareTo(o2.getName());
							case STARTDATE:
								return o1.getStartDate().compareTo(
										o2.getStartDate());
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
		HistoricProcessInstanceQuery query = buildQuery();
		if (!sortConstraints.isEmpty()) {
			SortConstraint sortConstraint = sortConstraints.get(0);
			Column sortColumn = Column.from(sortConstraint.getPropertyModel()
					.getPropertyName());
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

	private HistoricProcessInstanceQuery buildQuery() {
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

	private List<Instance> recursiveSubprocesses(
			String parentProcessId) {
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

	Instance copyInstanceDAO(HistoricProcessInstance h) {
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
}
