package org.cytoscape.sample.casualpath.ImportandExecutor.tasks;


import org.cytoscape.sample.casualpath.Panel.LegendPanel;
import org.cytoscape.sample.casualpath.creatystyle.RGBValue;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;

import java.awt.*;
import java.io.*;
import java.util.*;


public class FormatFileImport {
    public static CyServiceRegistrar cyServiceRegistrar;
    public String formatFile;
    public File Formatfile;
    LegendPanel legendPanel;
    public RGBValue AllNodesColor,EdgeColor,AllNodesBorderColor;
    public int AllNodesBorderWidth,AllEdgeBorderWidth;
    public Map<Object, HashMap<String,String>> Rppasite = new HashMap<>();
    public Map<Object , HashMap <String,Double>> Rppasitetooltip = new HashMap<>();
    public  Map<Object , HashMap <String,RGBValue>> NodesitergbValueHashMap = new HashMap<>();
    public  Map<Object , HashMap <String,RGBValue>> NodesiteBorderrgbValueHashMap = new HashMap<>();
    public HashMap<String,Integer > RppasiteCount = new HashMap<>();
    public  HashMap<String,RGBValue> NodeSpecificBorderColor = new HashMap<>();
    public HashMap<String,RGBValue> NodeSpecificColor = new HashMap<>();
    public HashMap<String,Double> NodeSpecifictooltipinfo = new HashMap<>();

    public HashMap<String,RGBValue> NodespecificBordercolor = new HashMap<>();
    public FormatFileImport(File Formatfile, String formatFile, CyServiceRegistrar cyServiceRegistrar,LegendPanel legendPanel) throws IOException, ParserConfigurationException, SAXException, TransformerException {

        this.cyServiceRegistrar = cyServiceRegistrar;
        this.formatFile = formatFile;
        this.Formatfile = Formatfile;
        this.legendPanel=legendPanel;
        writeFormatandLoadVisualStyle(Formatfile,formatFile,legendPanel);
    }

    public RGBValue getAllNodesBorderColor() {
        return AllNodesBorderColor;
    }

    public void writeFormatandLoadVisualStyle(File formatfile, String fXmlFile, LegendPanel legendPanel) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        try {


            FileInputStream fis = new FileInputStream(formatfile);
            Scanner sc = new Scanner(fis);    //file to be scanned
            //returns true if there is another line to read
            while (sc.hasNextLine()) {
                String str = sc.nextLine();
               // System.out.println(str);      //returns the line that was skipped
                //System.out.println("NEXT LINE");

                String[] splited = str.split("[\\s|]+");
                //System.out.println(splited.length);

                if (splited.length >= 3) {
                    if (splited.length == 4) {
                        if (splited[1] == "all-nodes" && splited[2] == "borderwidth") {
                            AllNodesBorderWidth = Integer.parseInt(splited[3]);
                        } else if (splited[1] == "all-edges" && splited[2] == "width") {
                            AllEdgeBorderWidth = Integer.parseInt(splited[3]);
                        }
                    } else if (splited.length == 12 || splited.length == 11) {
                        if (!Rppasite.containsKey(splited[1])) {
                            HashMap<String,String> siteinfo = new HashMap<>();
                            siteinfo.put(splited[3],splited[4]);
                            Rppasite.put(splited[1], siteinfo);
                            RppasiteCount.put(splited[1], 1);
                            HashMap<String,RGBValue> sitecolor = new HashMap<>();
                            HashMap<String,RGBValue> sitebordercolor = new HashMap<>();
                            sitebordercolor.put(splited[3],new RGBValue(Integer.parseInt(splited[8]),
                                    Integer.parseInt(splited[9]), Integer.parseInt(splited[10])));
                            NodesiteBorderrgbValueHashMap.put(splited[1],sitebordercolor);
                            sitecolor.put(splited[3],new RGBValue(Integer.parseInt(splited[5]),
                                    Integer.parseInt(splited[6]), Integer.parseInt(splited[7])));
                            NodesitergbValueHashMap.put(splited[1],sitecolor);
                            HashMap<String,Double> sitetooltipinfo = new HashMap<>();
                            try {
                                sitetooltipinfo.put(splited[3], Double.parseDouble(splited[11]));
                                Rppasitetooltip.put(splited[1],sitetooltipinfo);

                            }
                            catch (Exception e){
                                System.out.println("Format does not matched");
                                System.out.println(e);
                            }

                        } else {
                            Rppasite.get(splited[1]).put(splited[3],splited[4]);
                            int val = RppasiteCount.get(splited[1]);
                            val++;
                            RppasiteCount.replace(splited[1], val);
                            NodesitergbValueHashMap.get(splited[1]).put(splited[3],new RGBValue(Integer.parseInt(splited[5]),
                                    Integer.parseInt(splited[6]), Integer.parseInt(splited[7])));
                           NodesiteBorderrgbValueHashMap.get(splited[1]).put(splited[3],new RGBValue(Integer.parseInt(splited[8]),
                                   Integer.parseInt(splited[9]), Integer.parseInt(splited[10])));
                            try {
                                Rppasitetooltip.get(splited[1]).put(splited[3], Double.parseDouble(splited[11]));
                            }
                            catch (Exception e){
                                System.out.println(
                                        "Format does not mathched"
                                );
                                System.out.println(e);
                            }
                        }



                    } else if (splited.length == 6) {
                        if (Objects.equals(splited[1], "all-nodes") && Objects.equals(splited[2] ,"bordercolor")) {
                            AllNodesBorderColor = new RGBValue(Integer.parseInt(splited[3]),
                                    Integer.parseInt(splited[4]), Integer.parseInt(splited[5]));
                        } else if (Objects.equals(splited[1], "all-nodes")  && Objects.equals(splited[2], "color")) {
                            //System.out.println("Entered Here");
                            AllNodesColor = new RGBValue(Integer.parseInt(splited[3]),
                                    Integer.parseInt(splited[4]), Integer.parseInt(splited[5]));
                        } else if (Objects.equals(splited[2] ,"bordercolor")) {
                            NodespecificBordercolor.put(splited[1], new RGBValue(Integer.parseInt(splited[3]),
                                    Integer.parseInt(splited[4]), Integer.parseInt(splited[5])));

                        }
                        else if (Objects.equals(splited[2] ,"color")) {
                            NodeSpecificColor.put(splited[1], new RGBValue(Integer.parseInt(splited[3]),
                                    Integer.parseInt(splited[4]), Integer.parseInt(splited[5])));

                        }
                    }
                    else if(splited.length ==5){
                        if (Objects.equals(splited[2] ,"tooltip")) {
                            //System.out.println("tooltipinfo->"+splited[3]+" "+splited[4]);
                            NodeSpecifictooltipinfo.put(splited[1],Double.parseDouble(splited[4]));

                        }
                    }
//                    else {
//                        System.out.println("check->"+splited.length);
//                        System.out.println("output->"+splited[0]+ " "+ splited[1]+" "
//                         + splited[2]+ " "+splited[3]+ splited[4]);
//                    }
                }
            }
            sc.close();
            JButton submitbutton = legendPanel.getSubmitbutton();
            submitbutton.setEnabled(true);
            JProgressBar progressBar = legendPanel.getStatusBar();
            progressBar.setValue(66);
            legendPanel.getStatusLabel().setText("uploaded...");
            legendPanel.getStatusLabel().setForeground(new Color(5,102,8));
            JOptionPane.showMessageDialog(null, Formatfile);
            System.out.println("color->"+AllNodesColor.getR()+AllNodesColor.getG()+AllNodesColor.getB());

        }
        catch (NullPointerException ignored){
            JOptionPane optionPane = new JOptionPane("File Open Failed", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Failure");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            ignored.printStackTrace();
            legendPanel.getStatusLabel().setText("Failed");
            legendPanel.getStatusLabel().setForeground(Color.RED);
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

    public Map<Object, HashMap<String, String>> getRppasite() {
        return Rppasite;
    }

    public Map<Object, HashMap<String, RGBValue>> getNodesitergbValueHashMap() {
        return NodesitergbValueHashMap;
    }

    public Map<Object, HashMap<String, RGBValue>> getNodesiteBorderrgbValueHashMap() {
        return NodesiteBorderrgbValueHashMap;
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

    public HashMap<String, RGBValue> getNodeSpecificColor() {
        return NodeSpecificColor;
    }

    public Map<Object, HashMap<String, Double>> getRppasitetooltip() {
        return Rppasitetooltip;
    }

    public HashMap<String, Double> getNodeSpecifictooltipinfo() {
        return NodeSpecifictooltipinfo;
    }

    public HashMap<String, RGBValue> getNodespecificBordercolor() {
        return NodespecificBordercolor;
    }
}

