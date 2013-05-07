package org.activiti.monitor.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RuntimeService;
import org.activiti.monitor.data.Variable;
import org.springframework.stereotype.Component;

@Component
public class VariableDAO {
	@Inject
	RuntimeService runtimeService;

	public List<Variable> getVariables(String processId) {
		List<Variable> varList = new ArrayList<Variable>();
		try {
			Map<String, Object> vars = runtimeService.getVariables(processId);
			for (Map.Entry<String, Object> entry : vars.entrySet()) {
				varList.add(new Variable(entry.getKey(), entry.getValue()
						.toString()));
			}
		} catch (ActivitiException e) {
			varList.add(new Variable("error listing variables", e.getMessage()));
		}
		return varList;

	}
}
