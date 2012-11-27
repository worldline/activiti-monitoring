package net.atos.KawwaSample;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ComponentResources;

import net.atos.kawwaportal.components.services.breadcrumb.BreadcrumbListProvider;

public class MyBreadcrumbListProvider implements BreadcrumbListProvider  {

	@Override
	public List<String[]> getBreadcrumbList(String home, ComponentResources resources) {
		ArrayList<String[]> list = new ArrayList<String[]>();
		list.add(new String[]{"Timer", "Timer"});
		list.add(new String[]{"Tree", "Tree"});
		return list;
	}

}
