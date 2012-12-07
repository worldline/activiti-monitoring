package org.activiti.monitor.dao;

import org.apache.tapestry5.beaneditor.NonVisual;

public class ProcessInstanceDAO {
	
	String name;
	
		
	@NonVisual
	String id;

	String businessKey;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	
	
	

}
