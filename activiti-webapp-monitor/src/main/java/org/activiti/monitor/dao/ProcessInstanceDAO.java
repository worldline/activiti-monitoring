package org.activiti.monitor.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.beaneditor.NonVisual;

public class ProcessInstanceDAO {
	
	String name;
	
		
	@NonVisual
	String id;

	String businessKey;
	Date startDate;
	Date endDate;
	
	DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	
	public String getName() {
		return name;
	}
	public String getBusinessKey() {
		return businessKey;
	}

	public String getStartDate() {
		return startDate == null ? "" : dateFormat.format(startDate);
	}

	public String getEndDate() {
		return endDate == null ? "" : dateFormat.format(endDate);
	}
	
	public String getEndStatus() {
		return endStatus==null ? "":endStatus;
	}

	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	String endStatus;
	
	public void setEndStatus(String endStatus) {
		this.endStatus = endStatus;
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
	
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	
	
	

}
