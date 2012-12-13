package org.activiti.monitor.dao;

import org.apache.tapestry5.beaneditor.NonVisual;

public class ProcessDefinitionDAO {
	
	String processDefinitionName;
	int version;
	
	@NonVisual
	String id;

	public String getProcessDefinitionName() {
		return processDefinitionName;
	}

	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	
	

}
