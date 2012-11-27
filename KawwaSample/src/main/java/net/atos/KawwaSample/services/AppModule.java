package net.atos.KawwaSample.services;

import net.atos.KawwaSample.MyBreadcrumbListProvider;
import net.atos.kawwaportal.components.services.breadcrumb.BreadcrumbListProvider;
import net.atos.kawwaportal.components.services.breadcrumb.BreadcrumbListProviderSource;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;


public class AppModule {
	public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration) {
		configuration.add(SymbolConstants.START_PAGE_NAME, "Index");
		configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
	}
	
	public static void contributeIgnoredPathsFilter(Configuration<String> configuration) {
	}
	
	@Contribute(BreadcrumbListProviderSource.class)
	public static void addingYourkageBasedBreadcrumbListProvider(MappedConfiguration<String, BreadcrumbListProvider> configuration)
	{
		configuration.addInstance("MyBreadcrumbListProvider", MyBreadcrumbListProvider.class);
	}
}
