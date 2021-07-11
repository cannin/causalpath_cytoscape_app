/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cytoscape.sample.casualpath.ImportandExecutor.tasks;

import java.io.*;
import java.util.Scanner;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.sample.casualpath.LegendPanel;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.xml.sax.SAXException;
import org.cytoscape.sample.casualpath.utils.CyNetworkUtils;


public class SIFImport {

    /**
     * @param args the command line arguments
     */
    
    private static CyServiceRegistrar cyServiceRegistrar;
    private String fXmlFile;
    private File Siffile;
    private  LegendPanel legendPanel;
    public CyNetwork SIFCyNetwork;
    public File temp = File.createTempFile("tempSIF", ".sif");
    public SIFImport(File siffile, String fXmlFile, CyServiceRegistrar cyServiceRegistrar, LegendPanel legendPanel) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        
        this.cyServiceRegistrar = cyServiceRegistrar;
        this.fXmlFile = fXmlFile;
        this.Siffile=siffile;
        this.legendPanel = legendPanel;
        writeSIF(siffile,fXmlFile);
    }
        
    public void writeSIF(File siffile,String fXmlFile) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        


        try
        {

            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
            FileInputStream fis=new FileInputStream(siffile);
            Scanner sc=new Scanner(fis);    //file to be scanned
            //returns true if there is another line to read
            while(sc.hasNextLine())
            {
                //System.out.println(sc.nextLine());      //returns the line that was skipped
                //System.out.println("NEXT LINE");
                String str=sc.nextLine();
                String[] splited = str.split("\\s+");
                //System.out.println(splited.length);
                if(splited.length>=3){
                    //for(int i=0;i<3;i++)
                      //  System.out.print(splited[i]);
                    bw.write(splited[0]+"\t"+splited[1]+"\t"+splited[2]);
                    bw.write("\n");
                    //System.out.println(" ");
                }
            }
            sc.close();     //closes the scanner
            bw.close();
            String OS = System.getProperty("os.name").toLowerCase();
            String name = "";
            if (OS.indexOf("win") >= 0){
                name = fXmlFile.substring(fXmlFile.lastIndexOf("\\")+1, fXmlFile.length());
            } else name = fXmlFile.substring(fXmlFile.lastIndexOf("/")+1, fXmlFile.length());

            String modelID = name ;
            //System.out.println("After file print");
            SIFCyNetwork = CyNetworkUtils.readCyNetworkFromFile(cyServiceRegistrar, temp);
            SIFCyNetwork.getRow(SIFCyNetwork).set(CyNetwork.NAME, modelID);
            JOptionPane.showMessageDialog(null, SIFCyNetwork);
           // CyNetworkUtils.createViewAndRegister(cyServiceRegistrar, SIFCyNetwork);
            JProgressBar progressBar = legendPanel.getStatusBar();
            progressBar.setValue(50);
            JButton formatflleuploadbuttoon = legendPanel.getFormatfileuploadbutton();
            formatflleuploadbuttoon.setEnabled(true);
            JButton submitbutton = legendPanel.getSubmitbutton();
            submitbutton.setEnabled(false);
        }
        catch (NullPointerException ignored){
            JOptionPane optionPane = new JOptionPane("File Open Failed", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Failure");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
           // ignored.printStackTrace();
        }
        catch (FileNotFoundException e) {
            JOptionPane optionPane = new JOptionPane("File Not Found", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Failure");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            //System.out.println("entered here");
           // e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            JOptionPane optionPane = new JOptionPane("File IO Failed", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Failure");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
        }




    }

    public File getTemp() {
        return temp;
    }

    public CyNetwork getSIFCyNetwork() {
        return SIFCyNetwork;
    }
}
