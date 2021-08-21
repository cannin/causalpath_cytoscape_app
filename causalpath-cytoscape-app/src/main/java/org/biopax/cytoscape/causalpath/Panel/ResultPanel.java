package org.biopax.cytoscape.causalpath.Panel;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanel;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.application.swing.CytoPanelState;
import org.cytoscape.service.util.CyServiceRegistrar;


@SuppressWarnings("serial")
public class ResultPanel extends JPanel implements CytoPanelComponent {

    private CyServiceRegistrar cyServiceRegistrar;

    private JPanel toolBarPanel;


    private JPanel plotPanel;
    private JLabel nodename, nodeheading, heading, siteinformation;
    JTextArea jTextArea;


    public ResultPanel(CyServiceRegistrar cyServiceRegistrar) {
        this.cyServiceRegistrar = cyServiceRegistrar;


        // Define Panel properties
        setLayout(new BorderLayout());
        setSize(new Dimension(400, 400));
        setPreferredSize(new Dimension(400, 400));

        // Create Panels
        createNorthPanel();
        createCentrePanel();


    }

    private void createNorthPanel() {

        toolBarPanel = new JPanel(new GridBagLayout());
        heading = new JLabel("CausalPath");
        heading.setFont(new java.awt.Font("Tahoma", 1, 18));
        nodeheading = new JLabel("Node Name");
        nodeheading.setFont(new java.awt.Font("Tahoma", 1, 12));
        nodename = new JLabel("");
        siteinformation = new JLabel("Node Information");
        siteinformation.setFont(new java.awt.Font("Tahoma", 1, 12));
        jTextArea = new JTextArea("All info is given here", 2, 9);
        jTextArea.setBounds(10, 30, 300, 200);
        int top = 3, left = 3, bottom = 3, right = 3;
        Insets i = new Insets(top, left, bottom, right);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.insets = i;

        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridy = 1;
        toolBarPanel.add(nodeheading, c);
        c.gridy = 2;
        toolBarPanel.add(nodename, c);
        c.gridy = 3;
        toolBarPanel.add(siteinformation, c);
        c.gridy = 4;
        toolBarPanel.add(jTextArea, c);
        jTextArea.setVisible(false);
        siteinformation.setVisible(false);
        nodename.setVisible(false);
        nodeheading.setVisible(false);


        add(toolBarPanel, BorderLayout.NORTH);

    }

    private void createCentrePanel() {
        plotPanel = new JPanel(new BorderLayout());
        add(plotPanel, BorderLayout.CENTER);
    }


    public JLabel getLabel() {
        return nodename;
    }

    public void updatetext(String name, String Siteinfo, int flag) {
        showPanel();
        jTextArea.setVisible(true);

        siteinformation.setVisible(true);
        nodename.setVisible(true);
        nodeheading.setVisible(true);
        nodename.setText(name);
        if (flag == 1) {
            nodeheading.setText("Edge Name");
            siteinformation.setText("Edge Information");
        } else {
            nodeheading.setText("Node Name");
            siteinformation.setText("Node Information");
        }
        jTextArea.setText(Siteinfo);

    }

    public void clearPlotPanel() {
        plotPanel.removeAll();
    }

    /**
     * Checks if Result Panel, CytoPanelName.EAST, is visible if not display it and focus on Cytocopter result pane.
     */
    public void showPanel() {
        CytoPanel cytoPanel = cyServiceRegistrar.getService(CySwingApplication.class).getCytoPanel(CytoPanelName.EAST);

        if (cytoPanel.getState() == CytoPanelState.HIDE)
            cytoPanel.setState(CytoPanelState.DOCK);

        int index = cytoPanel.indexOfComponent(this);

        if (index != -1)
            cytoPanel.setSelectedIndex(index);
    }

    public void HidePanel() {
        CytoPanel cytoPanel = cyServiceRegistrar.getService(CySwingApplication.class).getCytoPanel(CytoPanelName.EAST);

        if (cytoPanel.getState() == CytoPanelState.DOCK)
            cytoPanel.setState(CytoPanelState.HIDE);

        int index = cytoPanel.indexOfComponent(this);

        if (index != -1)
            cytoPanel.setSelectedIndex(index);
    }


    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public CytoPanelName getCytoPanelName() {
        return CytoPanelName.EAST;
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public String getTitle() {
        return "CausalPath ";
    }

}
