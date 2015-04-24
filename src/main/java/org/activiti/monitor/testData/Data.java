package org.activiti.monitor.testData;

import java.io.Serializable;

public class Data implements Serializable {
	private static final long serialVersionUID = 4956246494607830964L;
	private final String val;

	public Data(String val) {
		this.val = val;
	}

	@Override
	public String toString() {
		return "data : " + val;
	}
}