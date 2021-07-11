/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cytoscape.sample.internal.cellnoptr.tasks;

import java.io.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.xml.sax.SAXException;
import org.cytoscape.sample.internal.utils.CyNetworkUtils;

/**
 *
 * @author francescoceccarelli
 */
public class SIFImport {

    /**
     * @param args the command line arguments
     */
    
    private static CyServiceRegistrar cyServiceRegistrar;
    private String fXmlFile;
    private File Siffile;
    public SIFImport(File siffile, String fXmlFile, CyServiceRegistrar cyServiceRegistrar) throws ParserConfigurationException, SAXException, IOException  {
        
        this.cyServiceRegistrar = cyServiceRegistrar;
        this.fXmlFile = fXmlFile;
        this.Siffile=siffile;
        writeSIF(siffile,fXmlFile);
    }
        
    public static void writeSIF(File siffile,String fXmlFile)throws ParserConfigurationException, SAXException, IOException{
        
    /*
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("qual:transition");
        
        ArrayList<Transition> allTransitions;
        allTransitions = new ArrayList<>();
        
        for (int i = 0; i < nList.getLength(); i++){
            
            Node nNode = nList.item(i);
            
            Transition trans = new Transition();
            ArrayList<String> inputs = new ArrayList<>();
            ArrayList<Integer> interactions = new ArrayList<>();
            
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;
                        
                        //Assuming for time being all OR transitions 
                        
			NodeList inputNodeList =  eElement.getElementsByTagName("qual:input");
                        
                        for (int j = 0; j < inputNodeList.getLength(); j++){
                            
                            Element iElement = (Element) inputNodeList.item(j);

                            inputs.add(iElement.getAttribute("qual:qualitativeSpecies"));
                            int sing = (iElement.getAttribute("qual:sign").equals("positive")) ? 1 : -1 ;
                            interactions.add(sing);
                        
                        }
                        
                        Node OutputNode =  eElement.getElementsByTagName("qual:output").item(0);
                        Element oElement = (Element) OutputNode;
                        trans.setOutput(oElement.getAttribute("qual:qualitativeSpecies"));
                        trans.setInputs(inputs);
                        trans.setInteractions(interactions);
                        allTransitions.add(trans);

		}
        }
        */
      //  File temp = File.createTempFile("tempSIF", ".sif");
       /* BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
        for (int i = 0; i < allTransitions.size(); i++){
            for (int j = 0; j < allTransitions.get(i).getInputs().size();j++){
                bw.write(allTransitions.get(i).getInputs().get(j)+"\t"+allTransitions.get(i).getInteractions().get(j)+"\t"+allTransitions.get(i).getOutput());
                bw.write("\n");
            }
        }
        bw.close();
        */
        try
        {


            FileInputStream fis=new FileInputStream(siffile);     //opens a connection to an actual file
            System.out.println("file content: ");
            int r=0;
            while((r=fis.read())!=-1)
            {
                System.out.print((char)r);      //prints the content of the file
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        String OS = System.getProperty("os.name").toLowerCase();
        String name = "";
        if (OS.indexOf("win") >= 0){
            name = fXmlFile.substring(fXmlFile.lastIndexOf("\\")+1, fXmlFile.length());
        } else name = fXmlFile.substring(fXmlFile.lastIndexOf("/")+1, fXmlFile.length());
        
        String modelID = name + ".SBMLQual";
        System.out.println("After file print");
        CyNetwork SIFCyNetwork = CyNetworkUtils.readCyNetworkFromFile(cyServiceRegistrar, siffile);
	   SIFCyNetwork.getRow(SIFCyNetwork).set(CyNetwork.NAME, modelID);
        JOptionPane.showMessageDialog(null, SIFCyNetwork);
//	CyNetworkUtils.createViewAndRegister(cyServiceRegistrar, SIFCyNetwork);
    }

    
    
}
