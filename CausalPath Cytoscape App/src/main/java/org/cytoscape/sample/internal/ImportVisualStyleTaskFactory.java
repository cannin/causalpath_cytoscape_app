package org.cytoscape.sample.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.task.read.LoadVizmapFileTaskFactory;
import org.cytoscape.task.visualize.ApplyVisualStyleTaskFactory;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class ImportVisualStyleTaskFactory extends AbstractTaskFactory {


	private final CyApplicationManager cyApplicationManagerServiceRef;
	private final ApplyVisualStyleTaskFactory applyVisualStyleTaskFactory;
	private  LoadNetworkFileTaskFactory loadNetworkFileTaskFactory;
	
	public ImportVisualStyleTaskFactory(LoadNetworkFileTaskFactory loadNetworkFileTaskFactory,CyApplicationManager cyApplicationManagerServiceRef,
			ApplyVisualStyleTaskFactory applyVisualStyleTaskFactory){

		this.cyApplicationManagerServiceRef = cyApplicationManagerServiceRef;
		this.applyVisualStyleTaskFactory = applyVisualStyleTaskFactory;
		this.loadNetworkFileTaskFactory=loadNetworkFileTaskFactory;
	}
	
	public TaskIterator createTaskIterator(){
		LoadNetworkFileTaskFactory loadNetworkFileTaskFactory;
		return new TaskIterator(new ImportVisualStyleTask(this.loadNetworkFileTaskFactory, this.cyApplicationManagerServiceRef.getCurrentNetworkView(),
				this.applyVisualStyleTaskFactory));
	}
}
