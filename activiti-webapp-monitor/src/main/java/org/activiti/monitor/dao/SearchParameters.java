package org.activiti.monitor.dao;

public class SearchParameters {
	private final String businessKey;
	
	public String getBusinessKey() {
		return businessKey;
	}

	public SearchParameters(String businessKey){
		this.businessKey = businessKey;}
}
