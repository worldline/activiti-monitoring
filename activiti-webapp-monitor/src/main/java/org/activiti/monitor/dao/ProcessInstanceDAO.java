package org.activiti.monitor.dao;

import org.apache.tapestry5.beaneditor.NonVisual;

public class ProcessInstanceDAO {
	
	String name;
	
	@NonVisual	
	String id;
	
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
	
	

}
