package org.activiti.monitor.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.monitor.data.Definition;
import org.apache.tapestry5.annotations.Persist;
import org.springframework.stereotype.Component;

@Component
public class DefinitionDAO {
	@Persist
	Map<String, String> processDefinitionNames;

	@Inject
	RepositoryService repositoryService;

	public String getProcessDefinitionName(String processDefinitionId) {
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

	public List<Definition> getProcessDefinitions() {
		List<ProcessDefinition> processDefinitionList = repositoryService
				.createProcessDefinitionQuery().list();
		List<Definition> pdfDaoList = new ArrayList<Definition>();
		for (ProcessDefinition dp : processDefinitionList) {
			Definition p = new Definition();
			p.setProcessDefinitionName(dp.getName());
			p.setId(dp.getId());
			p.setVersion(dp.getVersion());
			pdfDaoList.add(p);
		}
		return pdfDaoList;
	}

}
