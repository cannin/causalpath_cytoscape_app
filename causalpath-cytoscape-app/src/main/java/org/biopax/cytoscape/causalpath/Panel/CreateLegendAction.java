package org.biopax.cytoscape.causalpath.Panel;

import java.awt.event.ActionEvent;

import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;
import org.cytoscape.service.util.CyServiceRegistrar;


public class CreateLegendAction extends AbstractCyAction {

    private static final long serialVersionUID = 1L;
    private CySwingApplication desktopApp;
    private final CytoPanel cytoPanelWest;
    private LegendPanel myControlPanel;
    private CyServiceRegistrar cyServiceRegistrar;

    public CreateLegendAction(CySwingApplication desktopApp,
                              LegendPanel myCytoPanel, CyServiceRegistrar cyServiceRegistrar) {

        super("CausalPath App");
        setPreferredMenu("Apps");

        this.desktopApp = desktopApp;
        this.cyServiceRegistrar = cyServiceRegistrar;
        this.cytoPanelWest = this.desktopApp.getCytoPanel(CytoPanelName.WEST);
        this.myControlPanel = myCytoPanel;

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // If the state of the cytoPanelWest is HIDE, show it
        if (cytoPanelWest.getState() == CytoPanelState.HIDE) {
            cytoPanelWest.setState(CytoPanelState.DOCK);
        }

        // Select my panel
        int index = cytoPanelWest.indexOfComponent(myControlPanel);
        if (index == -1) {
            return;
        }
        cytoPanelWest.setSelectedIndex(index);
        // Define a task and set the progress in the run() method
    }
}
