package org.activiti.monitor.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProcessInstanceHistoryDAO {

	private String name;
	Date startDate;
	Date endDate;

	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartDate() {
		return startDate == null ? "" : dateFormat.format(startDate);
	}

	public String getEndDate() {
		return endDate == null ? "" : dateFormat.format(endDate);
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
