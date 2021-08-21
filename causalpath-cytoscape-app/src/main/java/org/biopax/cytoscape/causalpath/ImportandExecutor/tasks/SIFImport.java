/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.biopax.cytoscape.causalpath.ImportandExecutor.tasks;

import java.awt.*;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.biopax.cytoscape.causalpath.Panel.LegendPanel;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyTable;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.xml.sax.SAXException;
import org.biopax.cytoscape.causalpath.utils.CyNetworkUtils;


public class SIFImport {

    /**
     * @param args the command line arguments
     */

    private static CyServiceRegistrar cyServiceRegistrar;
    private String fXmlFile;
    private File Siffile;
    private LegendPanel legendPanel;
    public CyNetwork SIFCyNetwork;
    public static String Edge_Info_col_Name = "CausalPath_Edge_Information";
    public File temp = File.createTempFile("tempSIF", ".sif");
    public HashMap<String, String> edgeinformation = new HashMap<>();

    public SIFImport(File siffile, String fXmlFile, CyServiceRegistrar cyServiceRegistrar, LegendPanel legendPanel) throws ParserConfigurationException, SAXException, IOException, TransformerException {

        this.cyServiceRegistrar = cyServiceRegistrar;
        this.fXmlFile = fXmlFile;
        this.Siffile = siffile;
        this.legendPanel = legendPanel;
        writeSIF(siffile, fXmlFile);
    }

    public void writeSIF(File siffile, String fXmlFile) throws ParserConfigurationException, SAXException, IOException, TransformerException {


        try {

            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
            FileInputStream fis = new FileInputStream(siffile);
            Scanner sc = new Scanner(fis);    //file to be scanned
            //returns true if there is another line to read
            while (sc.hasNextLine()) {


                String str = sc.nextLine();
                String[] splited = str.split("\\t");

                if (splited.length >= 3) {

                    bw.write(splited[0] + "\t" + splited[1] + "\t" + splited[2]);
                    bw.write("\n");

                    if (splited.length == 5) {
                        edgeinformation.put(splited[0] + " (" + splited[1] + ") " + splited[2], splited[4]);
                    }

                }
            }
            sc.close();     //closes the scanner
            bw.close();
            String OS = System.getProperty("os.name").toLowerCase();
            String name = "";
            if (OS.indexOf("win") >= 0) {
                name = fXmlFile.substring(fXmlFile.lastIndexOf("\\") + 1, fXmlFile.length());
            } else name = fXmlFile.substring(fXmlFile.lastIndexOf("/") + 1, fXmlFile.length());
            String[] arr;

            String modelID = name;

            SIFCyNetwork = CyNetworkUtils.readCyNetworkFromFile(cyServiceRegistrar, temp);
            SIFCyNetwork.getRow(SIFCyNetwork).set(CyNetwork.NAME, modelID);
            CyTable edgetable = SIFCyNetwork.getDefaultEdgeTable();
            Collection<CyEdge> edgelist = SIFCyNetwork.getEdgeList();
            edgetable.createColumn(Edge_Info_col_Name, String.class, false);
            for (CyEdge edge : edgelist) {

                String temp = edgetable.getRow(edge.getSUID()).get("name", String.class);

                if (edgeinformation.containsKey(temp)) {

                    edgetable.getRow(edge.getSUID()).set(Edge_Info_col_Name, edgeinformation.get(temp));
                }
            }

            JOptionPane.showMessageDialog(null, SIFCyNetwork);

            JProgressBar progressBar = legendPanel.getStatusBar();

            progressBar.setValue(33);
            legendPanel.getStatusLabel().setText("uploaded ...");
            legendPanel.getStatusLabel().setForeground(new Color(5, 102, 8));
            JButton formatflleuploadbuttoon = legendPanel.getFormatfileuploadbutton();
            formatflleuploadbuttoon.setEnabled(true);
            JButton submitbutton = legendPanel.getSubmitbutton();
            submitbutton.setEnabled(false);
        } catch (NullPointerException ignored) {
            JOptionPane optionPane = new JOptionPane("File Open Failed", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Failure");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            legendPanel.getStatusLabel().setText("Failed");
            legendPanel.getStatusLabel().setForeground(Color.RED);

        } catch (FileNotFoundException e) {
            JOptionPane optionPane = new JOptionPane("File Not Found", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Failure");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane optionPane = new JOptionPane("File IO Failed", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Failure");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
        }


    }

    public HashMap<String, String> getEdgeinformation() {
        return edgeinformation;
    }

    public File getTemp() {
        return temp;
    }

    public CyNetwork getSIFCyNetwork() {
        return SIFCyNetwork;
    }
}
