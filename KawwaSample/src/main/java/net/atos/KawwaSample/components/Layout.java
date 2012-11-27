package net.atos.KawwaSample.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Import;

@Import(stylesheet = {"context:static/css/k-structure.css", "context:static/css/k-theme1.css"})
public class Layout {
	
	@Parameter(defaultPrefix=BindingConstants.LITERAL)
	private String title;
	
	public String getTitle(){
		return title;
	}
}
