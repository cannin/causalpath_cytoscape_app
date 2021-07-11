package org.cytoscape.sample.internal;

import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;
import org.cytoscape.task.read.LoadVizmapFileTaskFactory;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;

import org.cytoscape.task.visualize.ApplyVisualStyleTaskFactory;
import org.cytoscape.work.TaskIterator;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

public class ImportVisualStyleTask extends AbstractTask  {

	@Tunable(description = "Network file", params = "fileCategory=Network;input=true")
	public File f;
     public  TaskMonitor taskMonitor;

	private  final LoadNetworkFileTaskFactory loadNetworkFileTaskFactory;
	private CyNetworkView view;

	private final ApplyVisualStyleTaskFactory applyVisualStyleTaskFactory;

	public ImportVisualStyleTask(LoadNetworkFileTaskFactory loadNetworkFileTaskFactory, CyNetworkView view,
								 ApplyVisualStyleTaskFactory applyVisualStyleTaskFactory) {

		this.loadNetworkFileTaskFactory = loadNetworkFileTaskFactory;
		this.view = view;
		this.applyVisualStyleTaskFactory = applyVisualStyleTaskFactory;

	}

	@Override
	public void run(TaskMonitor taskMonitor) throws Exception {
		this.taskMonitor = taskMonitor;
		System.out.println("file loading ");
		TaskIterator ti=loadNetworkFileTaskFactory.createTaskIterator(f);
		System.out.println("file loaded");
		System.out.println(f);
		try
		{


			FileInputStream fis=new FileInputStream(f);     //opens a connection to an actual file
			System.out.println("file content: ");
			int r=0;
			while((r=fis.read())!=-1)
			{
				System.out.print((char)r);      //prints the content of the file
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		this.insertTasksAfterCurrentTask(ti);
		System.out.println("After the the current task ");
	}

	public TaskMonitor getTaskMonitor() {
		return taskMonitor;
	}
}
