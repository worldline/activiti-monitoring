package org.activiti.monitor.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.monitor.dao.ProcessDefinitionDAO;
import org.activiti.monitor.dao.ProcessInstanceDAO;
import org.activiti.monitor.dao.ProcessInstanceHistoryDAO;
import org.activiti.monitor.dao.VariableDAO;
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
	VariableDAO variable;

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

	ProcessInstanceDAO copyInstanceDAO(HistoricProcessInstance h) {
		ProcessInstanceDAO p = new ProcessInstanceDAO();
		p.setId(h.getId());
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
			
			subprocesses.add(copyInstanceDAO(historicProcessInstance) );
			subprocesses.addAll(recursiveSubprocesses(historicProcessInstance.getId()));
		}
		return subprocesses;
	}
	
	public List<ProcessInstanceHistoryDAO> getHistories() {
		ArrayList<ProcessInstanceHistoryDAO> histories = new ArrayList<ProcessInstanceHistoryDAO>();
		if (parentProcess != null) {
			for (HistoricActivityInstance historicProcessInstance : historyService
					.createHistoricActivityInstanceQuery()
					.processInstanceId(parentProcess).list()) {
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
		if (parentProcess != null) {
			 Map<String, Object> vars = 
					 runtimeService.getVariables(parentProcess);
					 
			 for (Map.Entry<String, Object> entry :vars.entrySet() ) {
				 VariableDAO varDAO = new VariableDAO();
				 varDAO.setName(entry.getKey());
				 varDAO.setValue((String) entry.getValue());
				 varList.add(varDAO);
			 }
		} 
		} catch (Exception e) {
			
		}
		 return varList;
		
	}

	public List<Object> getProcessInstances() {
		List<Object> pdfDaoList = new ArrayList<Object>();
		
		List<HistoricProcessInstance> historyProcessInstanceList = historyService
				.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).list();
		
		if (parentProcess == null) {
			
			for (HistoricProcessInstance processHistoryInstance : historyProcessInstanceList) {
				
				pdfDaoList.add(copyInstanceDAO(processHistoryInstance));

			}

		} else {
			List<ProcessInstanceDAO> subprocesses = recursiveSubprocesses(parentProcess);
			pdfDaoList.addAll(subprocesses);

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
