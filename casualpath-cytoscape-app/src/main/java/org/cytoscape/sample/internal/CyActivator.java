package org.cytoscape.sample.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CyAction;

import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.task.read.LoadVizmapFileTaskFactory;
import org.cytoscape.task.visualize.ApplyVisualStyleTaskFactory;
import org.cytoscape.work.SynchronousTaskManager;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.swing.DialogTaskManager;
import org.osgi.framework.BundleContext;

import org.cytoscape.service.util.AbstractCyActivator;

import java.io.InputStream;
import java.util.Properties;


public class CyActivator extends AbstractCyActivator {
	public static String visualStyleFile =  "/CytocopterVisualStyle.xml";
	public  static CyServiceRegistrar cyServiceRegistrar;
	public static final String visualStyleName = "Cytocopter";

	public CyActivator() {
		super();
	}


	public void start(BundleContext bc) {
		CySwingApplication cytoscapeDesktopService = getService(bc,CySwingApplication.class);
		CyApplicationManager cyApplicationManagerServiceRef = getService(bc,CyApplicationManager.class);
		//ApplyVisualStyleTaskFactory applyVisualStyleTaskFactory = getService(bc,ApplyVisualStyleTaskFactory.class);
		//LoadNetworkFileTaskFactory loadNetworkFileTaskFactory= getService(bc,LoadNetworkFileTaskFactory.class);
		//SynchronousTaskManager synchronousTaskManager = getService(bc,SynchronousTaskManager.class);
		//importVisualStyleTaskFactory= new ImportVisualStyleTaskFactory(
		//		loadNetworkFileTaskFactory,cyApplicationManagerServiceRef, applyVisualStyleTaskFactory);
		cyServiceRegistrar = getService(bc, CyServiceRegistrar.class);
		System.out.println("in the cyativator file before register service");
        LegendPanel controlpanel = new LegendPanel(cyServiceRegistrar);
		CreateLegendAction controlPanelAction = new CreateLegendAction(cytoscapeDesktopService,controlpanel,cyServiceRegistrar);
		registerService(bc, controlpanel, CytoPanelComponent.class, new Properties());

		registerService(bc,controlPanelAction,CyAction.class, new Properties());

		//registerService(bc,importVisualStyleTaskFactory, TaskFactory.class, new Properties());
		//registerService(bc,importVisualStyleTask, Task.class,new Properties());

		System.out.println("After Registering Service");
		//loadVisualStyle();
	}

	private void loadVisualStyle () {
		InputStream in = getClass().getResourceAsStream(visualStyleFile);
		LoadVizmapFileTaskFactory loadVizmapFileTaskFactory =  cyServiceRegistrar.getService(LoadVizmapFileTaskFactory.class);
		loadVizmapFileTaskFactory.loadStyles(in);
	}
}

