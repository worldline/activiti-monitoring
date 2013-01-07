package org.activiti.monitor.dao;

import java.util.Date;

public class SearchParameters {
	private final String businessKey;
	private final Date start;
	private final Date end;
	
	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public SearchParameters(String businessKey, Date start, Date end){
		this.businessKey = businessKey;
		this.start = start;
		this.end = end;}
}
