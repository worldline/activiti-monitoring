package net.atos.KawwaSample;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ComponentResources;

import net.atos.kawwaportal.components.services.breadcrumb.BreadcrumbListProvider;

public class MyBreadcrumbListProvider implements BreadcrumbListProvider  {

	@Override
	public List<String[]> getBreadcrumbList(String home, ComponentResources resources) {
		ArrayList<String[]> list = new ArrayList<String[]>();
		list.add(new String[]{"Timer", "Definition 1.1 (id 454545)"});
		list.add(new String[]{"Timer", "Definition 1 (id 1231245)"});
		list.add(new String[]{"Tree", "Process Definition 1"});
		list.add(new String[]{"Timer", "All Definitions"});
		return list;
	}

}
