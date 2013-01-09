package org.activiti.monitor.dao;

public class VariableDAO {
	private final String name;
	private final String value;

	public VariableDAO(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
