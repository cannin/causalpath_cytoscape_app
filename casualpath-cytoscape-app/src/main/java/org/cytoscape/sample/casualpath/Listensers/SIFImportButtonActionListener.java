
package org.cytoscape.sample.casualpath.Listensers;



import org.cytoscape.sample.casualpath.LegendPanel;
import org.cytoscape.sample.casualpath.ImportandExecutor.tasks.SIFImport;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SIFImportButtonActionListener implements ActionListener{
    
    private LegendPanel controlPanel;
    JFileChooser fc;
    private CyServiceRegistrar cyServiceRegistrar;
    public  SIFImport sifImport;

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
                   // System.out.println("selected file path->"+path);
                
                }
        try {
           sifImport = new SIFImport(selectedFile,path, cyServiceRegistrar,controlPanel);

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(SIFImportButtonActionListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(SIFImportButtonActionListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SIFImportButtonActionListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException transformerConfigurationException) {
            transformerConfigurationException.printStackTrace();
        } catch (TransformerException transformerException) {
            transformerException.printStackTrace();
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

    public SIFImport getSifImport() {
        return sifImport;
    }
}
