/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cytoscape.sample.internal.Listensers;

/**
 *
 * @author francescoceccarelli
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.cytoscape.sample.internal.LegendPanel;
import org.cytoscape.sample.internal.ImportandExecutor.tasks.SIFImport;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

//import uk.ac.ebi.cytocopter.internal.cellnoptr.tasks.SBMLFileString;

/**
 *
 * @author francescoceccarelli
 */
public class SIFImportButtonActionListener implements ActionListener{
    
    private LegendPanel controlPanel;
    JFileChooser fc;
    private CyServiceRegistrar cyServiceRegistrar;


	public SIFImportButtonActionListener(LegendPanel controlPanel, CyServiceRegistrar cyServiceRegistrar)
	{
		this.controlPanel = controlPanel;
                this.cyServiceRegistrar = cyServiceRegistrar;
	}


    public void actionPerformed(ActionEvent e)
	{
                String path="";
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("SIF file (*.sif)", "sif");
                fileChooser.setFileFilter(xmlFilter);
                int result = fileChooser.showOpenDialog(controlPanel);
                File selectedFile = null;
                if (result == JFileChooser.APPROVE_OPTION) {
                    
                    selectedFile = fileChooser.getSelectedFile();
                    path = selectedFile.getAbsolutePath();
                    System.out.println("selected file path->"+path);
                
                }
        try {
            SIFImport importer = new SIFImport(selectedFile,path, cyServiceRegistrar);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(SIFImportButtonActionListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(SIFImportButtonActionListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SIFImportButtonActionListener.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    private static File getLatestFilefromDir(String dirPath2){
        File dir = new File(dirPath2);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        return lastModifiedFile;
    }
}
