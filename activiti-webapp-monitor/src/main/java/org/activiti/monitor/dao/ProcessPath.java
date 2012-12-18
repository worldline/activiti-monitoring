package org.activiti.monitor.dao;

import java.io.Serializable;

public class ProcessPath implements Serializable {
	private final String id;
	private final String display;

	public ProcessPath(String id, String display) {
		this.id = id;
		this.display = display;
	}
	
	public String getId() {
		return id;
	}

	public String getDisplay() {
		return display;
	}
	
	@Override
	public String toString() {
		return display;
	}
}
