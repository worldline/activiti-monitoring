package org.activiti.monitor.graphics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.explorer.Constants;


/**
 * Builder that is capable of creating a {@link StreamResource} for a given
 * process-definition, containing the diagram image, if available.
 * 
 * @author Frederik Heremans
 */
public class ProcessDefinitionImageStreamResourceBuilder {
  
  protected static final Logger LOGGER = Logger.getLogger(ProcessDefinitionImageStreamResourceBuilder.class.getName());
  
//  public InputStream buildStreamResource(ProcessDefinition processDefinition, RepositoryService repositoryService) {
//    
//    StreamResource imageResource = null;
//    
//    if(processDefinition.getDiagramResourceName() != null) {
//      final InputStream definitionImageStream = repositoryService.getResourceAsStream(
//        processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());
//      
//      StreamSource streamSource = new InputStreamStreamSource(definitionImageStream);
//      
//      // Creating image name based on process-definition ID is fine, since the diagram image cannot
//      // be altered once deployed.
//      String imageExtension = extractImageExtension(processDefinition.getDiagramResourceName());
//      String fileName = processDefinition.getId() + "." + imageExtension;
//      
//      imageResource = new StreamResource(streamSource, fileName, ExplorerApp.get());
//    }
//    
//    return imageResource;
//  }

  public InputStream buildStreamResource(ProcessInstance processInstance, RepositoryService repositoryService, RuntimeService runtimeService) {

    
    ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processInstance
            .getProcessDefinitionId());

    if (processDefinition != null && processDefinition.isGraphicalNotationDefined()) {
      try {
        InputStream definitionImageStream = ProcessDiagramGenerator.generateDiagram(processDefinition, "png", 
                runtimeService.getActiveActivityIds(processInstance.getId()));
        return definitionImageStream;
              
      } catch(Throwable t) {
        // Image can't be generated, ignore this
        LOGGER.warning("Process image cannot be generated due to exception: " + t.getClass().getName() + " - " + t.getMessage());
      }
    }
    return null;
  }

  protected String extractImageExtension(String diagramResourceName) {
    String[] parts = diagramResourceName.split(".");
    if(parts.length > 1) {
      return parts[parts.length - 1];
    }
    return Constants.DEFAULT_DIAGRAM_IMAGE_EXTENSION;
  }
}