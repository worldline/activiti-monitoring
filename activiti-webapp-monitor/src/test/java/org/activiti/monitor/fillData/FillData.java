package org.activiti.monitor.fillData;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public class FillData {
	 final private int N_PROCESSES = 1 ;
	 final private int N_INSTANCES = 50 ;
	 final private static int level1Percent = 80;
	 final private static int level2Percent = 60;
	 final private static int level3Percent = 60;
	 final private static int level4Percent = 60;
	 
	 
	 ProcessEngine processEngine;
	 RuntimeService runtimeService;
	 RepositoryService repositoryService;
	 TaskService taskService;
	 ProcessInstance instances[] = new ProcessInstance[N_PROCESSES * N_INSTANCES];
	 int processInstanceIndex = 0;
	

	public static void main(String[] args) throws IOException {
		FillData fd = new FillData();
		fd.inits();
		fd.deplyProcesses();
		
		fd.startInstances();
		fd.completeTasks("User Task 1", level1Percent);
		fd.completeTasks("User Task 2", level2Percent);
		fd.completeTasks("User Task 3", level3Percent);
		fd.completeTasks("User Task 4", level4Percent);
		
//		fd.showResults();

	}

	private void completeTasks(String taskname, int percentafe) {
		System.out.println("Completing tasks "+ taskname+" started");
		
		Random randomGenerator = new Random();

		List<Task> taskList1 = taskService.createTaskQuery().taskName(taskname).list();
		Task[] tasks1  = taskList1.toArray(new Task[taskList1.size()]);
		
		int secondLevel = tasks1.length * percentafe / 100;
		
		Set<String> completedTasks = new HashSet<String>();
		
		for (int i=0; i<secondLevel; i++) {
			Task task = tasks1[randomGenerator.nextInt(secondLevel)];
			if (completedTasks.contains(task.getId())) {
				i--;
				continue;
			}

			taskService.complete(task.getId());
			completedTasks.add(task.getId());
		}
		System.out.println("Completing tasks "+ taskname+" Finished");
		
		
		
	}

	private void showResults() {
		List<Execution> execs = runtimeService.createExecutionQuery().list();
		
		for (Execution exec:execs) {
			System.out.println(exec.getId());
		}
		
		
	}

	private void inits() {
		processEngine = ProcessEngines.getDefaultProcessEngine();
        repositoryService = processEngine.getRepositoryService();		
		runtimeService = processEngine.getRuntimeService();
		taskService = processEngine.getTaskService();
		
	}

	private void startInstances() {
		System.out.println("starting instances");
		List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
		
		
		for (ProcessDefinition pd:processDefinitionList) {
			if (pd.getName().contains("_"))
				continue;
			for (int i=0; i<N_INSTANCES; i++) {				
				ProcessInstance processInstance = runtimeService.startProcessInstanceById(pd.getId());
				instances[processInstanceIndex++] =  processInstance;
			}
		}

		System.out.println("starting instances Finished");
		
	}
	public static final String[] processFileNames = {
		"org/activiti/monitor/fillData/process1.bpmn", 
		"org/activiti/monitor/fillData/process1_2.bpmn",
		"org/activiti/monitor/fillData/process1_3.bpmn",
		"org/activiti/monitor/fillData/process1_4.bpmn"};

	void deplyProcesses() throws IOException {
		
		DeploymentBuilder deployment = repositoryService.createDeployment();

		
		int j=0;
		for (String processFileName:processFileNames) {
			String processStr = readFile(processFileName);
			for (int i = 1; i <= N_PROCESSES; i++) {
				String newProcName = processStr.replaceAll("\"process1\"",
						"\"process" + i + "\"");
				newProcName = processStr;
				deployment = deployment.addInputStream("process" + + j + "_" + i + ".bpmn20.xml",
						new ByteArrayInputStream(newProcName.getBytes()));
			}
			j++;
		}

		System.out.println("Start deploying");
		deployment.deploy();
		System.out.println("End deploying");

	}

	String readFile(String path) throws IOException {

		BufferedReader br = new BufferedReader(
				new InputStreamReader(
						ClassLoader
								.getSystemResourceAsStream(path),
						"UTF-8"));

		StringBuffer sb = new StringBuffer();
		for (int c = br.read(); c != -1; c = br.read())
			sb.append((char) c);
		return sb.toString();
	}
}
