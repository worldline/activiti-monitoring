package org.activiti.monitor.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.monitor.data.HistoryInstance;
import org.springframework.stereotype.Component;

@Component
public class HistoryDAO {
	@Inject
	HistoryService historyService;

	public List<HistoryInstance> getHistories(String parentProcessId) {
		List<HistoryInstance> histories = new ArrayList<HistoryInstance>();
		List<HistoricActivityInstance> list = historyService
				.createHistoricActivityInstanceQuery()
				.processInstanceId(parentProcessId).list();
		for (HistoricActivityInstance historicProcessInstance : list) {
			HistoryInstance dao = new HistoryInstance();
			dao.setName(historicProcessInstance.getActivityName());
			dao.setStartDate(historicProcessInstance.getStartTime());
			dao.setEndDate(historicProcessInstance.getEndTime());
			histories.add(dao);
		}
		return histories;
	}

	public String getProcessDefinitionId(String processId) {
		return historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processId).singleResult()
				.getProcessDefinitionId();
	}
}
