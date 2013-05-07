package org.activiti.monitor.data;

import java.util.Date;

public class SearchParameters {
	private final String businessKey;
	private final Date start;
	private final Date end;
	private final String activityId;

	public String getActivityId() {
		return activityId;
	}

	public Date getStart() {
		return start;
	}

	public Date getEnd() {
		return end;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public SearchParameters(String businessKey, Date start, Date end,
			String activityId) {
		this.businessKey = businessKey;
		this.start = start;
		this.end = end;
		this.activityId = activityId;
	}
}
