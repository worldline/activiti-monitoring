package org.activiti.monitor.pages;

import java.net.MalformedURLException;

public class Index {
	

	Object onActivate() throws MalformedURLException {
		return ProcessList.class;
	}
}
