package org.cytoscape.sample.internal.Listensers;

import org.cytoscape.sample.internal.LegendPanel;
import org.cytoscape.sample.internal.cellnoptr.tasks.JsonImport;
import org.cytoscape.service.util.CyServiceRegistrar;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonFileImportActionListener implements ActionListener {
    private LegendPanel controlPanel;
    JFileChooser fc;
    private CyServiceRegistrar cyServiceRegistrar;
    public JsonFileImportActionListener(LegendPanel controlPanel, CyServiceRegistrar cyServiceRegistrar)
    {
        this.controlPanel = controlPanel;
        this.cyServiceRegistrar = cyServiceRegistrar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String path="";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter("JSON Source File (*.json)", "json");
        fileChooser.setFileFilter(jsonFilter);
        int result = fileChooser.showOpenDialog(controlPanel);
        File selectedFile = null;
        if (result == JFileChooser.APPROVE_OPTION) {

            selectedFile = fileChooser.getSelectedFile();
            path = selectedFile.getAbsolutePath();
            System.out.println("selected file path->"+path);

        }

        try {
            JsonImport importer = new JsonImport(selectedFile,path, cyServiceRegistrar);
        }
        catch (IOException ex) {
            Logger.getLogger(JsonFileImportActionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
