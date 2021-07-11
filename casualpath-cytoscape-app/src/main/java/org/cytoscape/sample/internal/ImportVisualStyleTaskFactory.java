package org.cytoscape.sample.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.task.read.LoadVizmapFileTaskFactory;
import org.cytoscape.task.visualize.ApplyVisualStyleTaskFactory;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.Task;
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

	public LoadNetworkFileTaskFactory getLoadNetworkFileTaskFactory() {
		return loadNetworkFileTaskFactory;
	}

	public CyApplicationManager getCyApplicationManagerServiceRef() {
		return cyApplicationManagerServiceRef;
	}

	public ApplyVisualStyleTaskFactory getApplyVisualStyleTaskFactory() {
		return applyVisualStyleTaskFactory;
	}

	@Override
	public TaskIterator createTaskIterator() {
		LoadNetworkFileTaskFactory loadNetworkFileTaskFactory;
		System.out.println("entered into task iterator");
		return new TaskIterator( new ImportVisualStyleTask(this.loadNetworkFileTaskFactory, this.cyApplicationManagerServiceRef.getCurrentNetworkView(),
				this.applyVisualStyleTaskFactory));
	}
}
