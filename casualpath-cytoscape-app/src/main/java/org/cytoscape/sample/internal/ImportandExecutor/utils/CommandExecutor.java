package org.cytoscape.sample.internal.ImportandExecutor.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cytoscape.command.CommandExecutorTaskFactory;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.SynchronousTaskManager;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;

public class CommandExecutor
{

	public static void execute(String command, CyServiceRegistrar cyServiceRegistrar)
	{
		CommandExecutorTaskFactory executor = cyServiceRegistrar.getService(CommandExecutorTaskFactory.class);

		List<String> commands = new ArrayList<String>();
		commands.add(command);
		System.out.println("commads->"+command);
		TaskIterator task = executor.createTaskIterator(commands,null);
		SynchronousTaskManager manager = cyServiceRegistrar.getService(SynchronousTaskManager.class);
		manager.execute(task);
	}

	public static void execute(TaskIterator commandTask, CyServiceRegistrar cyServiceRegistrar)
	{
		cyServiceRegistrar.getService(SynchronousTaskManager.class).execute(commandTask);
	}
}
