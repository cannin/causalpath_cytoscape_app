package org.cytoscape.sample.casualpath;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CyAction;

import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.task.read.LoadVizmapFileTaskFactory;
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

		cyServiceRegistrar = getService(bc, CyServiceRegistrar.class);

        LegendPanel controlpanel = new LegendPanel(cyServiceRegistrar);
		CreateLegendAction controlPanelAction = new CreateLegendAction(cytoscapeDesktopService,controlpanel,cyServiceRegistrar);
		registerService(bc, controlpanel, CytoPanelComponent.class, new Properties());

		registerService(bc,controlPanelAction,CyAction.class, new Properties());


		System.out.println("CausalPath APP started");

	}
	private void loadVisualStyle () {
		InputStream in = getClass().getResourceAsStream(visualStyleFile);
		LoadVizmapFileTaskFactory loadVizmapFileTaskFactory =  cyServiceRegistrar.getService(LoadVizmapFileTaskFactory.class);
		loadVizmapFileTaskFactory.loadStyles(in);
	}

}

