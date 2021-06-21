package org.cytoscape.sample.internal.Listensers;

import org.cytoscape.sample.internal.LegendPanel;
import org.cytoscape.sample.internal.cellnoptr.tasks.TxtFileImport;
import org.cytoscape.service.util.CyServiceRegistrar;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TxtFileImportButtonActionListener implements ActionListener {
    private LegendPanel controlPanel;
    JFileChooser fc;
    private CyServiceRegistrar cyServiceRegistrar;
    public TxtFileImportButtonActionListener(LegendPanel controlPanel, CyServiceRegistrar cyServiceRegistrar)
    {
        this.controlPanel = controlPanel;
        this.cyServiceRegistrar = cyServiceRegistrar;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String path="";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("Parameters Text Document (*.txt)", "txt");
        fileChooser.setFileFilter(xmlFilter);
        int result = fileChooser.showOpenDialog(controlPanel);
        File selectedFile = null;
        if (result == JFileChooser.APPROVE_OPTION) {

            selectedFile = fileChooser.getSelectedFile();
            path = selectedFile.getAbsolutePath();
            System.out.println("selected file path->"+path);

        }
        try {
             TxtFileImport importer = new TxtFileImport(selectedFile,path, cyServiceRegistrar);
        }  catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FormatFileImportActionListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
