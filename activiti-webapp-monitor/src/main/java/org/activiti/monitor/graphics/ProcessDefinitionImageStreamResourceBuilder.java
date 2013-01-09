package org.activiti.monitor.graphics;

import java.io.InputStream;
import java.util.logging.Logger;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * Builder that is capable of creating a {@link StreamResource} for a given
 * process-definition, containing the diagram image, if available.
 * 
 * @author Frederik Heremans
 */
public class ProcessDefinitionImageStreamResourceBuilder {

	protected static final Logger LOGGER = Logger
			.getLogger(ProcessDefinitionImageStreamResourceBuilder.class
					.getName());
	private static final String DEFAULT_DIAGRAM_IMAGE_EXTENSION = "png";

	public InputStream buildStreamResource(ProcessInstance processInstance,
			RepositoryService repositoryService, RuntimeService runtimeService) {

		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processInstance
						.getProcessDefinitionId());

		if (processDefinition != null) {
			try {
				InputStream definitionImageStream = ProcessDiagramGenerator
						.generateDiagram(processDefinition,
								DEFAULT_DIAGRAM_IMAGE_EXTENSION, runtimeService
										.getActiveActivityIds(processInstance
												.getId()));
				return definitionImageStream;

			} catch (Throwable t) {
				// Image can't be generated, ignore this
				LOGGER.warning("Process image cannot be generated due to exception: "
						+ t.getClass().getName() + " - " + t.getMessage());
			}
		}
		return null;
	}

	protected String extractImageExtension(String diagramResourceName) {
		String[] parts = diagramResourceName.split(".");
		if (parts.length > 1) {
			return parts[parts.length - 1];
		}
		return DEFAULT_DIAGRAM_IMAGE_EXTENSION;
	}
}