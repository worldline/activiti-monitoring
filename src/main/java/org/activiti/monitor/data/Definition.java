package org.activiti.monitor.data;

import org.apache.tapestry5.beaneditor.NonVisual;

public class Definition {

	String processDefinitionName;
	int version;
	long instanceCount;
	long historyInstanceCount;

	@NonVisual
	String id;

	public long getInstanceCount() {
		return instanceCount;
	}

	public void setInstanceCount(long instanceCount) {
		this.instanceCount = instanceCount;
	}

	public long getHistoryInstanceCount() {
		return historyInstanceCount;
	}

	public void setHistoryInstanceCount(long historyInstanceCount) {
		this.historyInstanceCount = historyInstanceCount;
	}

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
