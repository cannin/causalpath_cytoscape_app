package org.cytoscape.sample.casualpath.Listensers;

import org.cytoscape.sample.casualpath.Panel.LegendPanel;
import org.cytoscape.service.util.CyServiceRegistrar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubmitButtonActionListener implements ActionListener {
    private LegendPanel controlPanel;
    JFileChooser fc;
    private CyServiceRegistrar cyServiceRegistrar;
    public SubmitButtonActionListener(LegendPanel controlPanel, CyServiceRegistrar cyServiceRegistrar)
    {
        this.controlPanel = controlPanel;
        this.cyServiceRegistrar = cyServiceRegistrar;
    }
    @Override
    public void actionPerformed(ActionEvent e) {


    }
}
