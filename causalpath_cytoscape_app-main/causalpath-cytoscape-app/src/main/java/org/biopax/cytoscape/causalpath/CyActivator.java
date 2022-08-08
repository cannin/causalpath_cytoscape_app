package org.biopax.cytoscape.causalpath;

import org.biopax.cytoscape.causalpath.Panel.LegendPanel;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CyAction;

import org.biopax.cytoscape.causalpath.Panel.CreateLegendAction;
import org.biopax.cytoscape.causalpath.Panel.ResultPanel;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.work.SynchronousTaskManager;
import org.cytoscape.work.Tunable;
import org.osgi.framework.BundleContext;

import org.cytoscape.service.util.AbstractCyActivator;

import java.util.Properties;



public class CyActivator extends AbstractCyActivator {

    public static CyServiceRegistrar cyServiceRegistrar;
    SynchronousTaskManager synchronousTaskManager;
    public static ResultPanel resultPanel;

    public CyActivator() {
        super();
    }


    public void start(BundleContext bc) {
        CySwingApplication cytoscapeDesktopService = getService(bc, CySwingApplication.class);
       // synchronousTaskManager = getService(bc,SynchronousTaskManager.class);

        cyServiceRegistrar = getService(bc, CyServiceRegistrar.class);
        resultPanel = new ResultPanel(cyServiceRegistrar);
        registerService(bc, resultPanel, CytoPanelComponent.class, new Properties());
        synchronousTaskManager  = getService(bc,SynchronousTaskManager.class);
        LegendPanel controlpanel = new LegendPanel(cyServiceRegistrar, resultPanel,synchronousTaskManager);
        CreateLegendAction controlPanelAction = new CreateLegendAction(cytoscapeDesktopService, controlpanel, cyServiceRegistrar);
        registerService(bc, controlpanel, CytoPanelComponent.class, new Properties());

        registerService(bc, controlPanelAction, CyAction.class, new Properties());
       // registerService(bc,synchronousTaskManager,SynchronousTaskManager.class,new Properties());

        System.out.println("CausalPath started");

    }
    public SynchronousTaskManager getSynchronousTaskManager(){
        return synchronousTaskManager;
    }


    public static ResultPanel getResultPanel() {
        return resultPanel;
    }


}

