package org.activiti.monitor.dao;

import org.apache.tapestry5.beaneditor.NonVisual;

public class ProcessDefinitionDAO {
	
	String processDefinitionName;
	String version;
	
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
	

}
