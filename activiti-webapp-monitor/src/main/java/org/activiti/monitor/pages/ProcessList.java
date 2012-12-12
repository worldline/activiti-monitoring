package org.activiti.monitor.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.monitor.dao.ProcessDefinitionDAO;
import org.activiti.monitor.dao.ProcessInstanceDAO;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ProcessList {
	protected static final Logger LOGGER = Logger.getLogger(ProcessList.class
			.getName());

	// TODO: should be deleted, uses processDefinition instead

	@Property
	ProcessDefinitionDAO processDefinition;

	@Property
	static String parentProcess = null;

	@Property
	ProcessInstanceDAO processInstance;

	@Inject
	RepositoryService repositoryService;

	@Inject
	HistoryService historyService;

	@Inject
	RuntimeService runtimeService;

	@Inject
	private ProcessEngine processEngine;


	public List<String> getPath() {
		return path;
	}

	@Property
	private String pathElement;

	@Persist("flash")
	static List<String> path = new ArrayList<String>();

	public boolean getFirstLevel() {
		return processDefinitionId == null;
	}

	public String getParent() {
		return parentProcess;

	}

	public boolean getHasImage() {
		return parentProcess != null;
	}



	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public String getProcessDefinitionName() {
		return processDefinitionName;
	}

	static int level = 0;
	static String processDefinitionId = null;
	static String processDefinitionName = null;

	public List<String> recursiveSubprocesses(String parentProcessId) {
		List<String> subprocesses = new ArrayList<String>();
		for (HistoricProcessInstance historicProcessInstance : historyService
				.createHistoricProcessInstanceQuery()
				.superProcessInstanceId(parentProcessId).list()) {
			String subProcessId = historicProcessInstance.getId();
			subprocesses.add(subProcessId);
			subprocesses.addAll(recursiveSubprocesses(subProcessId));
		}
		return subprocesses;
	}

	public List<Object> getProcessInstances() {
		ProcessInstanceDAO pi = new ProcessInstanceDAO();
		List<Object> pdfDaoList = new ArrayList<Object>();

		ProcessInstanceQuery processInstanceQuery = runtimeService
				.createProcessInstanceQuery();

		if (parentProcess == null) {
			processInstanceQuery = processInstanceQuery
					.processDefinitionId(processDefinitionId);
			List<ProcessInstance> processInstanceList = processInstanceQuery
					.list();
			for (ProcessInstance processInstance : processInstanceList) {
				ProcessInstanceDAO p = new ProcessInstanceDAO();
				p.setId(processInstance.getId());
				p.setBusinessKey(processInstance.getBusinessKey());
				// p.setName(pp.getId() + "12");
				pdfDaoList.add(p);

			}

		} else {
			// processInstanceQuery =
			// processInstanceQuery.superProcessInstanceId(parentProcess);
			List<String> subprocesses = recursiveSubprocesses(parentProcess);
			for (String pp : subprocesses) {
				ProcessInstanceDAO p = new ProcessInstanceDAO();
				p.setId(pp);
				// p.setName(pp.getBusinessKey());
				// p.setName(pp.getId() + "12");
				pdfDaoList.add(p);
			}

		}
		return pdfDaoList;
	}

	public List<Object> getProcessDefinitions() {

		List<ProcessDefinition> processDefinitionList = repositoryService
				.createProcessDefinitionQuery().list();
		List<Object> pdfDaoList = new ArrayList<Object>();

		for (ProcessDefinition dp : processDefinitionList) {
			ProcessDefinitionDAO p = new ProcessDefinitionDAO();
			p.setProcessDefinitionName(dp.getName());
			p.setId(dp.getId());
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

	}

	void onActionFromProcessBranchInstances(String processInstanceId) {

		if (parentProcess != null)
			path.add(parentProcess);
		parentProcess = processInstanceId;

	}

	// TODO: should be changed to eventLink

	void onActionFromGotoPath1(int index) {
		onActionFromGotoPath(index);
	}

	void onActionFromGotoPath(int index) { 

		if (index != -1) {

			parentProcess = (String) path.toArray()[index];
			for (int i = path.size() - 1; i >= index; i--)
				path.remove(i);
		} else {
			path.clear();
			parentProcess = null;

		}

	}

	void onActionFromUp() {
		if (path.size() == 0) {
			if (parentProcess == null)
				processDefinitionId = null;
			else
				parentProcess = null;
		} else {
			parentProcess = path.remove(path.size() - 1);
		}

	}


}
