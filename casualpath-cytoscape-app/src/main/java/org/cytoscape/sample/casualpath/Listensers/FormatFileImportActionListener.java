package org.cytoscape.sample.casualpath.Listensers;

import org.cytoscape.sample.casualpath.Panel.LegendPanel;
import org.cytoscape.sample.casualpath.ImportandExecutor.tasks.FormatFileImport;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FormatFileImportActionListener implements ActionListener {
    private LegendPanel controlPanel;
    JFileChooser fc;
    FormatFileImport formatFileImport;
    private CyServiceRegistrar cyServiceRegistrar;
    public FormatFileImportActionListener(LegendPanel controlPanel, CyServiceRegistrar cyServiceRegistrar)
    {
        this.controlPanel = controlPanel;
        this.cyServiceRegistrar = cyServiceRegistrar;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String path="";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("Format file (*.format)", "format");
        fileChooser.setFileFilter(xmlFilter);
        int result = fileChooser.showOpenDialog(controlPanel);
        File selectedFile = null;
        if (result == JFileChooser.APPROVE_OPTION) {

            selectedFile = fileChooser.getSelectedFile();
            path = selectedFile.getAbsolutePath();
            //System.out.println("selected file path->"+path);

        }
        try {
           formatFileImport = new FormatFileImport(selectedFile,path, cyServiceRegistrar,controlPanel);
        }  catch (IOException ex) {
            Logger.getLogger(FormatFileImportActionListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException | ParserConfigurationException | TransformerException saxException) {
            saxException.printStackTrace();
        }
    }

    public FormatFileImport getFormatFileImport() {
        return formatFileImport;
    }
}
