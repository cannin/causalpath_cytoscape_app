package org.cytoscape.sample.internal;

import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.osgi.framework.BundleContext;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.task.read.LoadVizmapFileTaskFactory;
import org.cytoscape.task.visualize.ApplyVisualStyleTaskFactory;
import org.cytoscape.work.TaskFactory;
import java.util.Properties;


public class CyActivator extends AbstractCyActivator {
	public CyActivator() {
		super();
	}


	public void start(BundleContext bc) {

		CyApplicationManager cyApplicationManagerServiceRef = getService(bc,CyApplicationManager.class);

		ApplyVisualStyleTaskFactory applyVisualStyleTaskFactory = getService(bc,ApplyVisualStyleTaskFactory.class);
		LoadNetworkFileTaskFactory loadNetworkFileTaskFactory= getService(bc,LoadNetworkFileTaskFactory.class);
		ImportVisualStyleTaskFactory importVisualStyleTaskFactory= new ImportVisualStyleTaskFactory(
				loadNetworkFileTaskFactory,cyApplicationManagerServiceRef, applyVisualStyleTaskFactory);
		
		Properties importVisualStyleTaskFactoryProps = new Properties();
		importVisualStyleTaskFactoryProps.setProperty("preferredMenu","Apps.Samples");
		importVisualStyleTaskFactoryProps.setProperty("title","Import Network file");
		registerService(bc,importVisualStyleTaskFactory,TaskFactory.class, importVisualStyleTaskFactoryProps);
	}
}

