package org.cytoscape.sample.casualpath.creatystyle.Algorithm;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.sample.casualpath.ImportandExecutor.tasks.FormatFileImport;
import org.cytoscape.sample.casualpath.creatystyle.RGBValue;
import org.cytoscape.sample.casualpath.design.ApplyEnhancedGraphics;
import org.cytoscape.sample.casualpath.design.LabeltextSettings;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.view.model.VisualLexicon;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.mappings.PassthroughMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.cytoscape.sample.casualpath.design.LabeltextSettings.*;

public class FillAnnotation {
    public VisualStyle style;
    public FormatFileImport formatFileImport;
    public String vizcol;
    public  CyNetwork cyNetwork;
    public Map<Object, HashMap<String, RGBValue>> NodesiteBorderrgbValueHashMap;
    public Map<Object, HashMap<String, RGBValue>> NodesitergbValueHashMap;
    public  Map<Object, HashMap<String, String>> Rppasite;
    public  HashMap<String, Integer> RppasiteCount;
    public  String Graphicsname = "NODE_CUSTOMGRAPHICS_";
    CyServiceRegistrar cyServiceRegistrar;
    String ToolTipheading;
    public HashMap<Integer,String> AlgoWisePosition = new HashMap<>();
    public FillAnnotation(VisualStyle style, FormatFileImport formatFileImport, String vizCol, CyNetwork cyNetwork, CyServiceRegistrar cyServiceRegistrar,String ToolTipColumn){
      this.style= style;
      this.cyServiceRegistrar = cyServiceRegistrar;
      this.formatFileImport = formatFileImport;
      this.vizcol = vizCol;
      this.ToolTipheading = ToolTipColumn;
      this.cyNetwork = cyNetwork;
        NodesiteBorderrgbValueHashMap =  formatFileImport.getNodesiteBorderrgbValueHashMap();
         NodesitergbValueHashMap = formatFileImport.getNodesitergbValueHashMap();
         Rppasite =  formatFileImport.getRppasite();
         RppasiteCount =  formatFileImport.getRppasiteCount();
        AlgoWisePosition.put(1,"northwest");
        AlgoWisePosition.put(2,"northeast");
        AlgoWisePosition.put(3,"southeast");
        AlgoWisePosition.put(4,"southwest");
        AlgoWisePosition.put(5,"north");
        AlgoWisePosition.put(6,"east");
        AlgoWisePosition.put(7,"south");
        AlgoWisePosition.put(8,"west");
        AlgoWisePosition.put(9,"center");
    }



    public  VisualStyle RunAlgorithm(VisualStyle style){
        HashMap<String, RGBValue> info1;
        HashMap<String,String> info2;
        HashMap<String, RGBValue> info3;
        CyTable networktable = cyNetwork.getDefaultNetworkTable();


        CyTable nodeTable = cyNetwork.getDefaultNodeTable();
        VisualMappingFunctionFactory passthroughFactory = cyServiceRegistrar.getService(VisualMappingFunctionFactory.class, "(mapping.type=passthrough)");
        VisualLexicon lex = cyServiceRegistrar.getService(RenderingEngineManager.class).getDefaultVisualLexicon();
        VisualProperty<?> [] customGraphics = new VisualProperty<?>[9];
        PassthroughMapping<?,?> [] pMapping = new PassthroughMapping<?,?>[9];
        for (int i=0;i<9;i++){
            String newGraphicsname= Graphicsname+(i+1);
            customGraphics[i] = lex.lookup(CyNode.class, newGraphicsname);
        }



        for(CyNode node : cyNetwork.getNodeList()) {
            String name = cyNetwork.getRow(node).get(CyNetwork.NAME, String.class);
            String Totaltext = "";
            try {
                String nodetooltipinfo = formatFileImport.getNodeSpecifictooltipinfo().get(name).toString();
                Totaltext +=  name + " " + nodetooltipinfo +"|";
            }
            catch (Exception e){
                System.out.println("No tool tip information for the node");
            }


            HashMap<String, Double> sitetooltipinfo = formatFileImport.getRppasitetooltip().get(name);
            if(sitetooltipinfo != null) {
                for (Map.Entry mapelement : sitetooltipinfo.entrySet()) {
                    Totaltext += mapelement.getKey() + " " + sitetooltipinfo.get(mapelement.getKey()) + "|";
                }
            }
            nodeTable.getRow(node.getSUID()).set(ToolTipheading,Totaltext);

            try {

                //System.out.println("size->" + RppasiteCount.get(name));
                int size = RppasiteCount.get(name);
                if(size >= 8){
                    info1 = NodesiteBorderrgbValueHashMap.get(name);

                    info2 = Rppasite.get(name);

                    info3 = NodesitergbValueHashMap.get(name);
                    int s=0;
                    for(Map.Entry mapElement : info1.entrySet()){
                        if(s==8) break;
                        s++;
                        //System.out.println("s -> "+s);
                        String key = mapElement.getKey().toString();
                        String newGraphicsname= Graphicsname+s;
                        LabeltextSettings labeltextSettings = new LabeltextSettings();
                        String hex = String.format("#%02x%02x%02x",info1.get(key).getR(),info1.get(key).getG(),
                                info1.get(key).getB() );
                        if(Objects.equals(hex,"#323232")){
                            labeltextSettings.set(OUTLINE_PROPERTY,"false");
                        }
                        labeltextSettings.set(OUTLINE_COLOR_PROPERTY, hex);

                        String hex1 = String.format("#%02x%02x%02x",info3.get(key).getR(),info3.get(key).getG(),
                                info3.get(key).getB() );
                        labeltextSettings.set(LABEL_BG_PROPERTY,hex1);
                        labeltextSettings.set(LABEL_TEXT_LABEL_PROPERTY,info2.get(key));
                        labeltextSettings.set(LABEL_POSTION_PROPERTY,AlgoWisePosition.get(s));


                        String nodeStyle = ApplyEnhancedGraphics.toEnhancedGraphics(labeltextSettings);
                        //System.out.println("Final Nodestyle->" + nodeStyle);

                        String newcol = vizcol +s;

                        //System.out.println("Column name : "+ newcol);
                        nodeTable.getRow(node.getSUID()).set(newcol, nodeStyle);
//                        VisualProperty<?> customGraphics;
//                        PassthroughMapping<?,?> pMapping;
//                        customGraphics= lex.lookup(CyNode.class, newGraphicsname);



                    }
                    if(s==8){
                        LabeltextSettings labeltextSettings = new LabeltextSettings();
                        labeltextSettings.set(OUTLINE_PROPERTY,"false");



                    String hex1 = "#3B3B3B";
                    labeltextSettings.set(LABEL_BG_PROPERTY,hex1);
                    labeltextSettings.set(LABEL_POSTION_PROPERTY,AlgoWisePosition.get(s));


                    String nodeStyle = ApplyEnhancedGraphics.toEnhancedGraphics(labeltextSettings);
                   // System.out.println("Final Nodestyle->" + nodeStyle);

                    String newcol = vizcol +s;

                   // System.out.println("Column name : "+ newcol);
                    nodeTable.getRow(node.getSUID()).set(newcol, nodeStyle);
                    }


                }
                else {
                    info1 = NodesiteBorderrgbValueHashMap.get(name);
                    for (Map.Entry mapElement : info1.entrySet()) {
                        //System.out.println(mapElement.getKey() + " " + info1.get(mapElement.getKey()));
                    }

                    info2 = Rppasite.get(name);
                    for (Map.Entry mapElement : info2.entrySet()) {
                        //System.out.println("sitename->" + mapElement.getKey()+" "+info2.get(mapElement.getKey()));
                    }
                    info3 = NodesitergbValueHashMap.get(name);
                    for (Map.Entry mapElement : info3.entrySet()) {
                        //System.out.println(mapElement.getKey() + " " + info3.get(mapElement.getKey()));
                    }
                    int s=0;
                    for(Map.Entry mapElement : info1.entrySet()){
                        s++;
                       // System.out.println("s -> "+s);
                        String key = mapElement.getKey().toString();
                        String newGraphicsname= Graphicsname+s;
                        LabeltextSettings labeltextSettings = new LabeltextSettings();
                        String hex = String.format("#%02x%02x%02x",info1.get(key).getR(),info1.get(key).getG(),
                                info1.get(key).getB() );
                        if(Objects.equals(hex,"#323232")){
                            labeltextSettings.set(OUTLINE_PROPERTY,"false");
                        }
                        labeltextSettings.set(OUTLINE_COLOR_PROPERTY, hex);

                        String hex1 = String.format("#%02x%02x%02x",info3.get(key).getR(),info3.get(key).getG(),
                                info3.get(key).getB() );
                        labeltextSettings.set(LABEL_BG_PROPERTY,hex1);
                        labeltextSettings.set(LABEL_TEXT_LABEL_PROPERTY,info2.get(key));
                        labeltextSettings.set(LABEL_POSTION_PROPERTY,AlgoWisePosition.get(s));


                        String nodeStyle = ApplyEnhancedGraphics.toEnhancedGraphics(labeltextSettings);
                        //System.out.println("Final Nodestyle->" + nodeStyle);

                        String newcol = vizcol +s;

                        //System.out.println("Column name : "+ newcol);
                       nodeTable.getRow(node.getSUID()).set(newcol, nodeStyle);
//                        VisualProperty<?> customGraphics;
//                        PassthroughMapping<?,?> pMapping;
//                        customGraphics= lex.lookup(CyNode.class, newGraphicsname);



                    }

                }


            }
            catch (Exception e){
                System.out.println("Node ->" + name + " does not have any site");
            }
        }
        System.out.println("after creating the styles");
        for(int i=1;i<=9;i++) {
            pMapping[i-1] = (PassthroughMapping<?, ?>) passthroughFactory.createVisualMappingFunction(vizcol+i, String.class, customGraphics[i-1]);
            style.addVisualMappingFunction(pMapping[i-1]);
        }

        return style;

    }
}
