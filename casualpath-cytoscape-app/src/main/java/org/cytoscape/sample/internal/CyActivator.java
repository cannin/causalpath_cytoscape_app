package org.cytoscape.sample.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CyAction;

import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.task.visualize.ApplyVisualStyleTaskFactory;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;
import org.osgi.framework.BundleContext;

import org.cytoscape.service.util.AbstractCyActivator;

import java.util.Properties;


public class CyActivator extends AbstractCyActivator {
	public CyActivator() {
		super();
	}

    public ImportVisualStyleTaskFactory importVisualStyleTaskFactory ;
	public void start(BundleContext bc) {
		CySwingApplication cytoscapeDesktopService = getService(bc,CySwingApplication.class);
		CyApplicationManager cyApplicationManagerServiceRef = getService(bc,CyApplicationManager.class);
		ApplyVisualStyleTaskFactory applyVisualStyleTaskFactory = getService(bc,ApplyVisualStyleTaskFactory.class);
		LoadNetworkFileTaskFactory loadNetworkFileTaskFactory= getService(bc,LoadNetworkFileTaskFactory.class);
		//importVisualStyleTaskFactory= new ImportVisualStyleTaskFactory(
		//		loadNetworkFileTaskFactory,cyApplicationManagerServiceRef, applyVisualStyleTaskFactory);
		
		LegendPanel myControlPanel = new LegendPanel(loadNetworkFileTaskFactory,cyApplicationManagerServiceRef.getCurrentNetworkView(),
				applyVisualStyleTaskFactory);


		CreateLegendAction controlPanelAction = new CreateLegendAction(cytoscapeDesktopService,myControlPanel,loadNetworkFileTaskFactory,cyApplicationManagerServiceRef,
				applyVisualStyleTaskFactory);
		//ImportVisualStyleTask importVisualStyleTask = new ImportVisualStyleTask(loadNetworkFileTaskFactory,cyApplicationManagerServiceRef.getCurrentNetworkView(),
		//		applyVisualStyleTaskFactory);
		registerService(bc,myControlPanel,CytoPanelComponent.class, new Properties());
		registerService(bc,controlPanelAction,CyAction.class, new Properties());
		System.out.println("in the cyativator file before register service");
		//registerService(bc,importVisualStyleTaskFactory, TaskFactory.class, new Properties());
		//registerService(bc,importVisualStyleTask, Task.class,new Properties());
		////registerService(bc,importfilethread,Thread.class,new Properties());
		System.out.println("After Registering Service");
	}
	public ImportVisualStyleTaskFactory getImportVisualStyleTaskFactory() {
		return importVisualStyleTaskFactory;

	}

}

