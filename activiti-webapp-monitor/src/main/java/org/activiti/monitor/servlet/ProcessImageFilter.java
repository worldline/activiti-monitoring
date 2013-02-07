package org.activiti.monitor.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.monitor.graphics.ProcessDefinitionImageStreamResourceBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

@Component("processImageServlet")
public class ProcessImageFilter implements javax.servlet.Filter {

	@Inject
	RuntimeService runtimeService;
	@Inject
	RepositoryService repositoryService;


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		response.setContentType("image/png");
		String processInstanceId = request.getParameter("instanceId");
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		ProcessDefinitionImageStreamResourceBuilder imageBuilder = new ProcessDefinitionImageStreamResourceBuilder();
		InputStream is = imageBuilder.buildStreamResource(pi,
				repositoryService, runtimeService);
		BufferedImage bi = ImageIO.read(is);
		OutputStream out = response.getOutputStream();
		ImageIO.write(bi, "png", out);
		out.close();
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}