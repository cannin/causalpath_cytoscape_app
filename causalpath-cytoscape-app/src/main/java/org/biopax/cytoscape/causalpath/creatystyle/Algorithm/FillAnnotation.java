package org.biopax.cytoscape.causalpath.creatystyle.Algorithm;

import org.biopax.cytoscape.causalpath.ImportandExecutor.tasks.FormatFileImport;
import org.biopax.cytoscape.causalpath.creatystyle.RGBValue;
import org.biopax.cytoscape.causalpath.design.ApplyEnhancedGraphics;
import org.biopax.cytoscape.causalpath.design.LabeltextSettings;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.view.model.VisualLexicon;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.mappings.PassthroughMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

import static org.biopax.cytoscape.causalpath.Panel.LegendPanel.LOGGER;

public class FillAnnotation {
    public VisualStyle style;
    public FormatFileImport formatFileImport;
    public String vizcol;
    public CyNetwork cyNetwork;
    public Map<Object, HashMap<String, RGBValue>> NodesiteBorderrgbValueHashMap;
    public Map<Object, HashMap<String, RGBValue>> NodesitergbValueHashMap;
    public Map<Object, HashMap<String, String>> Rppasite;
    public HashMap<String, Integer> RppasiteCount;
    public String Graphicsname = "NODE_CUSTOMGRAPHICS_";
    CyServiceRegistrar cyServiceRegistrar;
    String ToolTipheading;
    public HashMap<Integer, String> AlgoWisePosition = new HashMap<>();

    public FillAnnotation(VisualStyle style, FormatFileImport formatFileImport, String vizCol, CyNetwork cyNetwork, CyServiceRegistrar cyServiceRegistrar, String ToolTipColumn) {
        this.style = style;
        this.cyServiceRegistrar = cyServiceRegistrar;
        this.formatFileImport = formatFileImport;
        this.vizcol = vizCol;
        this.ToolTipheading = ToolTipColumn;
        this.cyNetwork = cyNetwork;
        NodesiteBorderrgbValueHashMap = formatFileImport.getNodesiteBorderrgbValueHashMap();
        NodesitergbValueHashMap = formatFileImport.getNodesitergbValueHashMap();
        Rppasite = formatFileImport.getRppasite();
        RppasiteCount = formatFileImport.getRppasiteCount();
        AlgoWisePosition.put(1, "northwest");
        AlgoWisePosition.put(2, "northeast");
        AlgoWisePosition.put(3, "southeast");
        AlgoWisePosition.put(4, "southwest");
        AlgoWisePosition.put(5, "north");
        AlgoWisePosition.put(6, "east");
        AlgoWisePosition.put(7, "south");
        AlgoWisePosition.put(8, "west");
        AlgoWisePosition.put(9, "center");
    }


    public VisualStyle RunAlgorithm(VisualStyle style) {
        HashMap<String, RGBValue> info1;
        HashMap<String, String> info2;
        HashMap<String, RGBValue> info3;

        CyTable nodeTable = cyNetwork.getDefaultNodeTable();
        VisualMappingFunctionFactory passthroughFactory = cyServiceRegistrar.getService(VisualMappingFunctionFactory.class, "(mapping.type=passthrough)");
        VisualLexicon lex = cyServiceRegistrar.getService(RenderingEngineManager.class).getDefaultVisualLexicon();
        VisualProperty<?>[] customGraphics = new VisualProperty<?>[9];
        PassthroughMapping<?, ?>[] pMapping = new PassthroughMapping<?, ?>[9];
        for (int i = 0; i < 9; i++) {
            String newGraphicsname = Graphicsname + (i + 1);
            customGraphics[i] = lex.lookup(CyNode.class, newGraphicsname);
        }


        for (CyNode node : cyNetwork.getNodeList()) {
            String name = cyNetwork.getRow(node).get(CyNetwork.NAME, String.class);
            String Totaltext = "";
            try {
                String nodetooltipinfo = formatFileImport.getNodeSpecifictooltipinfo().get(name).toString();
                Totaltext += name + " " + nodetooltipinfo + "|";
            } catch (Exception e) {
                LOGGER.log(Level.INFO, "No tool tip information for the node");
            }


            HashMap<String, Double> sitetooltipinfo = formatFileImport.getRppasitetooltip().get(name);
            if (sitetooltipinfo != null) {
                for (Map.Entry mapelement : sitetooltipinfo.entrySet()) {
                    Totaltext += mapelement.getKey() + " " + sitetooltipinfo.get(mapelement.getKey()) + "|";
                }
            }
            nodeTable.getRow(node.getSUID()).set(ToolTipheading, Totaltext);

            try {
                int size = RppasiteCount.get(name);
                if (size >= 8) {
                    info1 = NodesiteBorderrgbValueHashMap.get(name);

                    info2 = Rppasite.get(name);

                    info3 = NodesitergbValueHashMap.get(name);
                    int s = 0;
                    for (Map.Entry mapElement : info1.entrySet()) {
                        if (s == 8) break;
                        s++;
                        String key = mapElement.getKey().toString();

                        LabeltextSettings labeltextSettings = new LabeltextSettings();
                        String hex = String.format("#%02x%02x%02x", info1.get(key).getR(), info1.get(key).getG(),
                                info1.get(key).getB());
                        if (Objects.equals(hex, "#323232")) {
                            labeltextSettings.set(LabeltextSettings.OUTLINE_PROPERTY, "false");
                        } else {
                            labeltextSettings.set(LabeltextSettings.OUTLINE_PROPERTY, "true");
                            labeltextSettings.set(LabeltextSettings.OUTLINE_COLOR_PROPERTY, "#323232");
                            labeltextSettings.set(LabeltextSettings.LABEL_COLOR_PROPERTY, hex);
                        }
                        String hex1 = String.format("#%02x%02x%02x", info3.get(key).getR(), info3.get(key).getG(),
                                info3.get(key).getB());
                        hex1 += "ff";

                        labeltextSettings.set(LabeltextSettings.LABEL_BG_PROPERTY, hex1);
                        labeltextSettings.set(LabeltextSettings.LABEL_TEXT_LABEL_PROPERTY, info2.get(key));
                        labeltextSettings.set(LabeltextSettings.LABEL_POSTION_PROPERTY, AlgoWisePosition.get(s));


                        String nodeStyle = ApplyEnhancedGraphics.toEnhancedGraphics(labeltextSettings);


                        String newcol = vizcol + s;


                        nodeTable.getRow(node.getSUID()).set(newcol, nodeStyle);


                    }
                    if (s == 8) {
                        LabeltextSettings labeltextSettings = new LabeltextSettings();
                        labeltextSettings.set(LabeltextSettings.OUTLINE_PROPERTY, "false");


                        String hex1 = "#3B3B3B";
                        labeltextSettings.set(LabeltextSettings.LABEL_BG_PROPERTY, hex1);
                        labeltextSettings.set(LabeltextSettings.LABEL_POSTION_PROPERTY, AlgoWisePosition.get(s));


                        String nodeStyle = ApplyEnhancedGraphics.toEnhancedGraphics(labeltextSettings);


                        String newcol = vizcol + s;


                        nodeTable.getRow(node.getSUID()).set(newcol, nodeStyle);
                    }


                } else {
                    info1 = NodesiteBorderrgbValueHashMap.get(name);
                    info2 = Rppasite.get(name);
                    info3 = NodesitergbValueHashMap.get(name);

                    int s = 0;
                    for (Map.Entry mapElement : info1.entrySet()) {
                        s++;

                        String key = mapElement.getKey().toString();

                        LabeltextSettings labeltextSettings = new LabeltextSettings();
                        String hex = String.format("#%02x%02x%02x", info1.get(key).getR(), info1.get(key).getG(),
                                info1.get(key).getB());
                        if (Objects.equals(hex, "#323232")) {
                            labeltextSettings.set(LabeltextSettings.OUTLINE_PROPERTY, "false");
                        } else {
                            labeltextSettings.set(LabeltextSettings.OUTLINE_PROPERTY, "true");
                            labeltextSettings.set(LabeltextSettings.OUTLINE_COLOR_PROPERTY, "#323232");
                            labeltextSettings.set(LabeltextSettings.LABEL_COLOR_PROPERTY, hex);
                        }

                        String hex1 = String.format("#%02x%02x%02x", info3.get(key).getR(), info3.get(key).getG(),
                                info3.get(key).getB());
                        hex1 += "ff";
                        labeltextSettings.set(LabeltextSettings.LABEL_BG_PROPERTY, hex1);
                        labeltextSettings.set(LabeltextSettings.LABEL_TEXT_LABEL_PROPERTY, info2.get(key));
                        labeltextSettings.set(LabeltextSettings.LABEL_POSTION_PROPERTY, AlgoWisePosition.get(s));


                        String nodeStyle = ApplyEnhancedGraphics.toEnhancedGraphics(labeltextSettings);


                        String newcol = vizcol + s;


                        nodeTable.getRow(node.getSUID()).set(newcol, nodeStyle);


                    }

                }


            } catch (Exception e) {
                LOGGER.log(Level.INFO, "Node ->" + name + " does not have any site");
            }
        }

        for (int i = 1; i <= 9; i++) {
            pMapping[i - 1] = (PassthroughMapping<?, ?>) passthroughFactory.createVisualMappingFunction(vizcol + i, String.class, customGraphics[i - 1]);
            style.addVisualMappingFunction(pMapping[i - 1]);
        }

        return style;

    }
}
