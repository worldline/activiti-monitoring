package org.activiti.monitor.pages;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.monitor.dao.ProcessDefinitionDAO;
import org.activiti.monitor.dao.ProcessInstanceDAO;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public class ProcessList {
	@Property
	ProcessDefinitionDAO processDefinition;

	@Inject
	RepositoryService repositoryService;
	
	@Inject
	RuntimeService runtimeService;

	static int level = 0;
	static String processDefinitionId = null;

	public List<Object> getProcessDefinitions() {


		if (level == 0) {

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
		} else {
			ProcessInstanceDAO pi = new ProcessInstanceDAO();
			List<Object> pdfDaoList = new ArrayList<Object>();

			List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().processDefinitionId(processDefinitionId).list();
			for (ProcessInstance pp: processInstanceList) {
				ProcessInstanceDAO p = new ProcessInstanceDAO();
				p.setId(pp.getId());
			    p.setName(pp.getBusinessKey());
				p.setName("saeid");
				pdfDaoList.add(p);
				
			}
			return pdfDaoList;
			
			
		}
	}

	void onActionFromDelete(String processDefinitionId) {
		level = 1;
		this.processDefinitionId = processDefinitionId;
		System.out.println(processDefinitionId);
	}
}
