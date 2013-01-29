package org.activiti.monitor.pages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.monitor.dao.DefinitionDAO;
import org.activiti.monitor.dao.HistoryDAO;
import org.activiti.monitor.dao.InstanceDAO;
import org.activiti.monitor.dao.VariableDAO;
import org.activiti.monitor.data.Definition;
import org.activiti.monitor.data.HistoryInstance;
import org.activiti.monitor.data.Instance;
import org.activiti.monitor.data.ProcessInstanceDataSource;
import org.activiti.monitor.data.ProcessPath;
import org.activiti.monitor.data.SearchParameters;
import org.activiti.monitor.data.Variable;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.BeanModelSource;

public class Index {
	protected static final Logger LOGGER = Logger.getLogger(Index.class
			.getName());

	@Inject
	InstanceDAO instanceDAO;
	@Inject
	VariableDAO variableDAO;
	@Inject
	DefinitionDAO definitionDAO;
	@Inject
	HistoryDAO historyDAO;
	@Inject
	ProcessEngine processEngine;

	@Inject
	RepositoryService repositoryService;
	@Inject
	HistoryService historyService;
	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources resources;

	@Property
	private Definition processDefinition;

	@Property
	@Persist
	ProcessPath parentProcess;

	@Property
	Variable variable;

	@Property
	@Environmental
	Instance processInstance;

	@Property
	@Persist
	private String businessKeySearch;

	@Property
	@Persist
	private String activitySearch;

	@Persist
	private boolean hasEnded;

	@Property
	private ProcessPath pathElement;

	@Persist
	Stack<ProcessPath> path;

	@Persist
	int level;

	@Persist
	String processDefinitionId;

	@Persist
	String processDefinitionName;

	@Property
	@Persist
	private Date startDateSearch;

	@Property
	@Persist
	private Date endDateSearch;

	void adjustProcess() {
		if (parentProcess == null)
			hasEnded = false;
		else
			hasEnded = instanceDAO.hasEnded(parentProcess.getId());
	}

	public JSONObject getDateOptions() {
		return new JSONObject().put("dateFormat", "dd/mm/yy");
	}

	private String getDisplay(String id) {
		String definitionId = getFirstLevel() ? id : historyDAO
				.getProcessDefinitionId(id);
		String name = definitionDAO.getProcessDefinitionName(definitionId);
		return String.format("%s - %s", name, id);
	}

	public boolean getFirstLevel() {
		return processDefinitionId == null;
	}

	public boolean getHasImage() {
		return (parentProcess != null) && (!hasEnded);
	}

	public List<HistoryInstance> getHistories() {
		ArrayList<HistoryInstance> histories = new ArrayList<HistoryInstance>();
		if (parentProcess != null) {
			for (HistoricActivityInstance historicProcessInstance : historyService
					.createHistoricActivityInstanceQuery()
					.processInstanceId(parentProcess.getId()).list()) {
				HistoryInstance dao = new HistoryInstance();
				dao.setName(historicProcessInstance.getActivityName());
				dao.setStartDate(historicProcessInstance.getStartTime());
				dao.setEndDate(historicProcessInstance.getEndTime());
				histories.add(dao);
			}
		}
		return histories;
	}
	
	public BeanModel<Definition> getProcessDefinitionModel() {
		BeanModel<Definition> model = beanModelSource.createDisplayModel(
				Definition.class, resources.getMessages());
		return model;
		}
	

	public BeanModel<Instance> getInstanceModel() {
		BeanModel<Instance> model = beanModelSource.createDisplayModel(
				Instance.class, resources.getMessages());

		return model;
	}

	public ProcessPath getParent() {
		return parentProcess;
	}

	public List<ProcessPath> getPath() {
		return path;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public List<Definition> getProcessDefinitions() {
		return definitionDAO.getProcessDefinitions();
	}

	public String getProcessDefinitionName() {
		return processDefinitionName;
	}

	public GridDataSource getProcessInstances() {
		SearchParameters searchParameters = new SearchParameters(
				businessKeySearch, startDateSearch, endDateSearch,
				activitySearch);
		return new ProcessInstanceDataSource(processEngine, parentProcess,
				processDefinitionId, searchParameters);
	}

	public List<Variable> getVariables() {
		if (!hasEnded && parentProcess != null)
			return variableDAO.getVariables(parentProcess.getId());
		else
			return Collections.emptyList();
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

	void onActionFromGotoPath1(int index) {
		onActionFromGotoPath(index);
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
}
