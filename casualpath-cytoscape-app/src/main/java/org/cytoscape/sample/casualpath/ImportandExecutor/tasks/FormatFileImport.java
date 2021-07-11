package org.cytoscape.sample.casualpath.ImportandExecutor.tasks;


import org.cytoscape.sample.casualpath.LegendPanel;
import org.cytoscape.sample.casualpath.creatystyle.RGBValue;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;


public class FormatFileImport {
    public static CyServiceRegistrar cyServiceRegistrar;
    public String formatFile;
    public File Formatfile;
    LegendPanel legendPanel;
    public RGBValue AllNodesColor,EdgeColor,AllNodesBorderColor;
    public int AllNodesBorderWidth,AllEdgeBorderWidth;
    public HashMap<String,String> Rppasite = new HashMap<>();
    public  HashMap<String,RGBValue> NodergbValueHashMap = new HashMap<>();
    public  HashMap<String,RGBValue> NodeBorderrgbValueHashMap = new HashMap<>();
    public HashMap<String,Integer > RppasiteCount = new HashMap<>();
    public  HashMap<String,RGBValue> NodeSpecificBorderColor = new HashMap<>();
    public FormatFileImport(File Formatfile, String formatFile, CyServiceRegistrar cyServiceRegistrar,LegendPanel legendPanel) throws IOException, ParserConfigurationException, SAXException, TransformerException {

        this.cyServiceRegistrar = cyServiceRegistrar;
        this.formatFile = formatFile;
        this.Formatfile = Formatfile;
        this.legendPanel=legendPanel;
        writeFormatandLoadVisualStyle(Formatfile,formatFile,legendPanel);
    }

    public void writeFormatandLoadVisualStyle(File formatfile, String fXmlFile, LegendPanel legendPanel) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        try {


            FileInputStream fis = new FileInputStream(formatfile);
            Scanner sc = new Scanner(fis);    //file to be scanned
            //returns true if there is another line to read
            while (sc.hasNextLine()) {
                String str = sc.nextLine();
                System.out.println(str);      //returns the line that was skipped
                //System.out.println("NEXT LINE");

                String[] splited = str.split("[\\s|]+");
                System.out.println(splited.length);

                if (splited.length >= 3) {
                    if (splited.length == 4) {
                        if (splited[1] == "all-nodes" && splited[2] == "borderwidth") {
                            AllNodesBorderWidth = Integer.parseInt(splited[3]);
                        } else if (splited[1] == "all-edges" && splited[2] == "width") {
                            AllEdgeBorderWidth = Integer.parseInt(splited[3]);
                        }
                    } else if (splited.length == 11) {
                        if (!Rppasite.containsKey(splited[1])) {
                            Rppasite.put(splited[1], splited[4]);
                            RppasiteCount.put(splited[1], 1);
                        } else {
                            int val = RppasiteCount.get(splited[1]);
                            val++;
                            RppasiteCount.replace(splited[1], val);
                        }

                        NodergbValueHashMap.put(splited[1], new RGBValue(Integer.parseInt(splited[5]),
                                Integer.parseInt(splited[6]), Integer.parseInt(splited[7])));
                        NodeBorderrgbValueHashMap.put(splited[1], new RGBValue(Integer.parseInt(splited[8]),
                                Integer.parseInt(splited[9]), Integer.parseInt(splited[10])));

                    } else if (splited.length == 6) {
                        if (Objects.equals(splited[1], "all-nodes") && Objects.equals(splited[2] ,"bordercolor")) {
                            AllNodesBorderColor = new RGBValue(Integer.parseInt(splited[3]),
                                    Integer.parseInt(splited[4]), Integer.parseInt(splited[5]));
                        } else if (Objects.equals(splited[1], "all-nodes")  && Objects.equals(splited[2], "color")) {
                            System.out.println("Entered Here");
                            AllNodesColor = new RGBValue(Integer.parseInt(splited[3]),
                                    Integer.parseInt(splited[4]), Integer.parseInt(splited[5]));
                        } else if (Objects.equals(splited[2] ,"bordercolor")) {
                            NodeBorderrgbValueHashMap.put(splited[1], new RGBValue(Integer.parseInt(splited[3]),
                                    Integer.parseInt(splited[4]), Integer.parseInt(splited[5])));

                        }
                    }
                }
            }
            sc.close();
            JButton submitbutton = legendPanel.getSubmitbutton();
            submitbutton.setEnabled(true);
            JProgressBar progressBar = legendPanel.getStatusBar();
            progressBar.setValue(100);
            System.out.println("color->"+AllNodesColor.getR()+AllNodesColor.getG()+AllNodesColor.getB());

        }
        catch (NullPointerException ignored){
            JOptionPane optionPane = new JOptionPane("File Open Failed", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Failure");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            ignored.printStackTrace();
        }
        catch (FileNotFoundException e) {
            JOptionPane optionPane = new JOptionPane("File Not Found", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Failure");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


    }

    public static CyServiceRegistrar getCyServiceRegistrar() {
        return cyServiceRegistrar;
    }

    public String getFormatFile() {
        return formatFile;
    }

    public File getFormatfile() {
        return Formatfile;
    }

    public RGBValue getAllNodesColor() {
        return AllNodesColor;
    }

    public RGBValue getEdgeColor() {
        return EdgeColor;
    }

    public HashMap<String, String> getRppasite() {
        return Rppasite;
    }

    public HashMap<String, RGBValue> getNodergbValueHashMap() {
        return NodergbValueHashMap;
    }

    public HashMap<String, RGBValue> getNodeBorderrgbValueHashMap() {
        return NodeBorderrgbValueHashMap;
    }

    public HashMap<String, RGBValue> getNodeSpecificBorderColor() {
        return NodeSpecificBorderColor;
    }

    public int getAllNodesBorderWidth() {
        return AllNodesBorderWidth;
    }

    public HashMap<String, Integer> getRppasiteCount() {
        return RppasiteCount;
    }

    public int getAllEdgeBorderWidth() {
        return AllEdgeBorderWidth;
    }
}

