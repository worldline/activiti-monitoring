package org.activiti.monitor.data;

import java.io.Serializable;

public class ProcessPath implements Serializable {
	private static final long serialVersionUID = 8094584685989723858L;
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
