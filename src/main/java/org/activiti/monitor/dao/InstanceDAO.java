package org.activiti.monitor.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.monitor.data.Instance;
import org.springframework.stereotype.Component;

@Component
public class InstanceDAO {
	@Inject
	HistoryService historyService;

	@Inject
	RuntimeService runtimeService;

	@Inject
	DefinitionDAO definitionDAO;

	public boolean hasEnded(String parentId) {
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery().processInstanceId(parentId)
				.singleResult();

		return processInstance == null || processInstance.isEnded();
	}

	private String endStatusName(String processInstanceId) {
		try {
	        String result = null;
	        List<String> types = Arrays.asList("endEvent", "errorEndEvent");
	        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId)
	                .finished();
	        for (HistoricActivityInstance historicActivityInstance : query.list()) {
	            String activityName = historicActivityInstance.getActivityName();
	            if (types.contains(historicActivityInstance.getActivityType())) {
	                if (result == null)
	                    result = activityName;
	                else
	                	// multiple results. show none
	                    return "";
	            }
	        }
	        if (result == null)
	        	// no result
	            return "";
	        else
	            return result;
		} 
		catch (Exception e) {
			return "";
		}
    }
	
	private Instance copyInstanceDAO(HistoricProcessInstance h) {
		Instance p = new Instance();
		p.setId(h.getId());
		p.setName(definitionDAO.getProcessDefinitionName(h
				.getProcessDefinitionId()) + " - " + h.getId());
		p.setStartDate(h.getStartTime());
		p.setEndDate(h.getEndTime());
		p.setBusinessKey(h.getBusinessKey());
		p.setEndStatus(endStatusName(h.getEndActivityId()));

		return p;

	}

	public List<Instance> recursiveSubprocesses(String parentProcessId) {
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

}
