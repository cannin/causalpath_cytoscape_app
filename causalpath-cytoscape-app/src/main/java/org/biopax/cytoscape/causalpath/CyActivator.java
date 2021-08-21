package org.biopax.cytoscape.causalpath;

import org.biopax.cytoscape.causalpath.Panel.LegendPanel;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CyAction;

import org.biopax.cytoscape.causalpath.Panel.CreateLegendAction;
import org.biopax.cytoscape.causalpath.Panel.ResultPanel;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.osgi.framework.BundleContext;

import org.cytoscape.service.util.AbstractCyActivator;

import java.util.Properties;


public class CyActivator extends AbstractCyActivator {

    public static CyServiceRegistrar cyServiceRegistrar;

    public static ResultPanel resultPanel;

    public CyActivator() {
        super();
    }


    public void start(BundleContext bc) {
        CySwingApplication cytoscapeDesktopService = getService(bc, CySwingApplication.class);


        cyServiceRegistrar = getService(bc, CyServiceRegistrar.class);
        resultPanel = new ResultPanel(cyServiceRegistrar);
        registerService(bc, resultPanel, CytoPanelComponent.class, new Properties());
        LegendPanel controlpanel = new LegendPanel(cyServiceRegistrar, resultPanel);
        CreateLegendAction controlPanelAction = new CreateLegendAction(cytoscapeDesktopService, controlpanel, cyServiceRegistrar);
        registerService(bc, controlpanel, CytoPanelComponent.class, new Properties());

        registerService(bc, controlPanelAction, CyAction.class, new Properties());


        System.out.println("CausalPath APP started");

    }


    public static ResultPanel getResultPanel() {
        return resultPanel;
    }


}

