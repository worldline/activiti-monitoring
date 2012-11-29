package org.activiti.monitor.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.monitor.graphics.ProcessDefinitionImageStreamResourceBuilder;


public class ProcessImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setContentType("image/png");
		String processInstanceId = request.getParameter("instanceId");
		System.out.println(processInstanceId);
		
		RuntimeService runtimeService = (RuntimeService) getServletContext().getAttribute("runtimeService");
		RepositoryService repositoryService = (RepositoryService) getServletContext().getAttribute("repositoryService");
		System.out.println(runtimeService);
		System.out.println(repositoryService);
		if (repositoryService == null || runtimeService == null)
			return;

		System.out.println("Show " + processInstanceId);
		ProcessInstance pi =runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		ProcessDefinitionImageStreamResourceBuilder imageBuilder = new ProcessDefinitionImageStreamResourceBuilder();

		InputStream is = imageBuilder.buildStreamResource(pi, repositoryService, runtimeService);

		
		BufferedImage bi = ImageIO.read(is);
		OutputStream out = response.getOutputStream();
		ImageIO.write(bi, "png", out);
		out.close();

	}

}