/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cytoscape.sample.internal.ImportandExecutor.tasks;

import java.io.*;
import java.util.Scanner;

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
       /* try
        {


            FileInputStream fis=new FileInputStream(siffile);     //opens a connection to an actual file
            System.out.println("file content: ");
            int r=0;
            while((r=fis.read())!=-1)
            {
                System.out.println((char)r);      //prints the content of the file

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        */
        File temp = File.createTempFile("tempSIF", ".sif");
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
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

       /* BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
        for (int i = 0; i < allTransitions.size(); i++){
            for (int j = 0; j < allTransitions.get(i).getInputs().size();j++){
                bw.write(allTransitions.get(i).getInputs().get(j)+"\t"+allTransitions.get(i).getInteractions().get(j)+"\t"+allTransitions.get(i).getOutput());
                bw.write("\n");
            }
        }
        bw.close();
        */

        String OS = System.getProperty("os.name").toLowerCase();
        String name = "";
        if (OS.indexOf("win") >= 0){
            name = fXmlFile.substring(fXmlFile.lastIndexOf("\\")+1, fXmlFile.length());
        } else name = fXmlFile.substring(fXmlFile.lastIndexOf("/")+1, fXmlFile.length());
        
        String modelID = name + ".SBMLQual";
        System.out.println("After file print");
        CyNetwork SIFCyNetwork = CyNetworkUtils.readCyNetworkFromFile(cyServiceRegistrar, temp);
	   SIFCyNetwork.getRow(SIFCyNetwork).set(CyNetwork.NAME, modelID);
        JOptionPane.showMessageDialog(null, SIFCyNetwork);
        CyNetworkUtils.createViewAndRegister(cyServiceRegistrar, SIFCyNetwork);
    }

    
    
}
