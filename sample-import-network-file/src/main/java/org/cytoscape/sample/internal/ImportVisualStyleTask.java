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
import java.util.HashSet;
import java.util.Set;

public class ImportVisualStyleTask extends AbstractTask {

	@Tunable(description = "Network file", params = "fileCategory=Network;input=true")
	public File f;


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
	public void run(TaskMonitor taskMonitor) {

		taskMonitor.setStatusMessage("Loading Network from file ...");
		TaskIterator ti=loadNetworkFileTaskFactory.createTaskIterator(f);
		this.insertTasksAfterCurrentTask(ti);
	}
}
