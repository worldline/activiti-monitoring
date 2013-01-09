package org.activiti.monitor.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.monitor.dao.ProcessDefinitionDAO;
import org.activiti.monitor.dao.ProcessInstanceDAO;
import org.activiti.monitor.dao.ProcessInstanceDataSource;
import org.activiti.monitor.dao.ProcessInstanceHistoryDAO;
import org.activiti.monitor.dao.ProcessPath;
import org.activiti.monitor.dao.SearchParameters;
import org.activiti.monitor.dao.VariableDAO;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.BeanModelSource;

public class Index {
	protected static final Logger LOGGER = Logger.getLogger(Index.class
			.getName());

	// TODO: should be deleted, uses processDefinition instead

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources resources;

	@Property
	ProcessDefinitionDAO processDefinition;

	@Property
	@Persist
	ProcessPath parentProcess;

	@Property
	VariableDAO variable;

	@Property
	@Environmental
	ProcessInstanceDAO processInstance;

	@Inject
	RepositoryService repositoryService;

	@Inject
	HistoryService historyService;

	@Inject
	RuntimeService runtimeService;

	public List<ProcessPath> getPath() {
		return path;
	}

	@Property
	@Persist
	private String businessKeySearch;

	@Persist
	private boolean hasEnded;

	@Property
	private ProcessPath pathElement;

	@Persist
	Stack<ProcessPath> path;

	@Persist
	Map<String, String> processDefinitionNames;

	public boolean getFirstLevel() {
		return processDefinitionId == null;
	}

	public ProcessPath getParent() {
		return parentProcess;
	}

	public boolean getHasImage() {
		return (parentProcess != null) && (!hasEnded);
	}

	public BeanModel getInstanceModel() {
		@SuppressWarnings("deprecation")
		BeanModel model = beanModelSource.create(ProcessInstanceDAO.class,
				false, resources.getMessages());

		return model;
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

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public String getProcessDefinitionName() {
		return processDefinitionName;
	}

	@Persist
	int level;

	@Persist
	String processDefinitionId;

	@Persist
	String processDefinitionName;

	private ProcessInstanceDAO copyInstanceDAO(HistoricProcessInstance h) {
		ProcessInstanceDAO p = new ProcessInstanceDAO();
		p.setId(h.getId());
		p.setName(getProcessDefinitionName(h.getProcessDefinitionId()) + " - "
				+ h.getId());
		p.setStartDate(h.getStartTime());
		p.setEndDate(h.getEndTime());
		p.setBusinessKey(h.getBusinessKey());
		p.setEndStatus(h.getEndActivityId());

		return p;

	}

	public List<ProcessInstanceDAO> recursiveSubprocesses(String parentProcessId) {
		List<ProcessInstanceDAO> subprocesses = new ArrayList<ProcessInstanceDAO>();
		for (HistoricProcessInstance historicProcessInstance : historyService
				.createHistoricProcessInstanceQuery()
				.superProcessInstanceId(parentProcessId).list()) {

			subprocesses.add(copyInstanceDAO(historicProcessInstance));
			subprocesses.addAll(recursiveSubprocesses(historicProcessInstance
					.getId()));
		}
		return subprocesses;
	}

	public List<ProcessInstanceHistoryDAO> getHistories() {
		ArrayList<ProcessInstanceHistoryDAO> histories = new ArrayList<ProcessInstanceHistoryDAO>();
		if (parentProcess != null) {
			for (HistoricActivityInstance historicProcessInstance : historyService
					.createHistoricActivityInstanceQuery()
					.processInstanceId(parentProcess.getId()).list()) {
				ProcessInstanceHistoryDAO dao = new ProcessInstanceHistoryDAO();
				dao.setName(historicProcessInstance.getActivityName());
				dao.setStartDate(historicProcessInstance.getStartTime());
				dao.setEndDate(historicProcessInstance.getEndTime());
				histories.add(dao);
			}
		}
		return histories;
	}

	public List<VariableDAO> getVariables() {
		List<VariableDAO> varList = new ArrayList<VariableDAO>();
		try {
			if (!hasEnded) {
				if (parentProcess != null) {
					Map<String, Object> vars = runtimeService
							.getVariables(parentProcess.getId());
					for (Map.Entry<String, Object> entry : vars.entrySet()) {
						varList.add(new VariableDAO(entry.getKey(), entry
								.getValue().toString()));
					}
				}
			}
		} catch (ActivitiException e) {
			varList.add(new VariableDAO("error listing variables", e
					.getMessage()));
		}
		return varList;

	}

	public GridDataSource getProcessInstances() {
		SearchParameters searchParameters = new SearchParameters(
				businessKeySearch, startDateSearch, endDateSearch);
		return new ProcessInstanceDataSource(repositoryService, historyService,
				parentProcess, processDefinitionId, searchParameters);
	}

	public List<Object> getProcessDefinitions() {

		List<ProcessDefinition> processDefinitionList = repositoryService
				.createProcessDefinitionQuery().list();
		List<Object> pdfDaoList = new ArrayList<Object>();

		for (ProcessDefinition dp : processDefinitionList) {
			ProcessDefinitionDAO p = new ProcessDefinitionDAO();
			p.setProcessDefinitionName(dp.getName());
			p.setId(dp.getId());
			p.setVersion(dp.getVersion());
			pdfDaoList.add(p);

		}
		return pdfDaoList;
	}

	void onActionFromlistRootProcesses(String processDefinitionId) {
		level = 1;
		parentProcess = null;
		this.processDefinitionId = processDefinitionId;
		ProcessDefinition processDefinitionList = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		processDefinitionName = processDefinitionList.getName();
		adjustProcess();
		adjustProcess();

	}

	void onActionFromProcessBranchInstances(String processInstanceId) {

		if (parentProcess != null) {
			if (path == null)
				path = new Stack<ProcessPath>();
			path.add(parentProcess);
		}
		parentProcess = new ProcessPath(processInstanceId,
				getDisplay(processInstanceId));
		adjustProcess();

	}

	private String getDisplay(String id) {
		String definitionId = getFirstLevel() ? id : runtimeService
				.createProcessInstanceQuery().processInstanceId(id)
				.singleResult().getProcessDefinitionId();
		String name = getProcessDefinitionName(definitionId);
		return String.format("%s - %s", name, id);
	}

	// TODO: should be changed to eventLink

	void onActionFromGotoPath1(int index) {
		onActionFromGotoPath(index);
	}

	void onActionFromGotoPath(int index) {

		if (index != -1 && path != null) {

			parentProcess = path.pop();
			for (int i = path.size() - 2; i >= index; i--)
				path.pop();
		} else {
			path = null;
			parentProcess = null;

		}
		adjustProcess();

	}

	void onActionFromUp() {
		if (path == null || path.size() == 0) {
			if (parentProcess == null)
				processDefinitionId = null;
			else
				parentProcess = null;
		} else {
			parentProcess = path.pop();
		}
		adjustProcess();

	}

	void adjustProcess() {
		if (parentProcess == null)
			hasEnded = false;
		else {
			ProcessInstance processInstance = runtimeService
					.createProcessInstanceQuery()
					.processInstanceId(parentProcess.getId()).singleResult();

			hasEnded = (processInstance == null) || processInstance.isEnded();
		}

	}

	@Property
	@Persist
	private Date startDateSearch;
	@Property
	@Persist
	private Date endDateSearch;

	public JSONObject getDateOptions() {
		return new JSONObject().put("dateFormat", "dd/mm/yy");
	}

	public void onSubmit() {
		System.out.println(startDateSearch);

	}
}
