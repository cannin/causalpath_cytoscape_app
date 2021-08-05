package org.cytoscape.sample.casualpath.design;

import org.apache.poi.common.usermodel.LineStyle;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.command.AvailableCommands;
import org.cytoscape.model.*;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.sample.casualpath.CyActivator;
import org.cytoscape.sample.casualpath.ImportandExecutor.tasks.FormatFileImport;
import org.cytoscape.sample.casualpath.creatystyle.Algorithm.FillAnnotation;
import org.cytoscape.sample.casualpath.creatystyle.RGBValue;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.VisualLexicon;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.LineTypeVisualProperty;
import org.cytoscape.view.presentation.property.values.LineType;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;
import org.cytoscape.view.vizmap.mappings.PassthroughMapping;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ApplyEnhancedGraphics {
      CyServiceRegistrar cyServiceRegistrar;
    CyNetwork cyNetwork;
    VisualStyle style;
    FormatFileImport formatFileImport;

    public static final String CP_COLUMN_NAMESPACE = "CausalPath Visualizer";

    public ApplyEnhancedGraphics(CyServiceRegistrar cyServiceRegistrar, CyNetwork cyNetwork, VisualStyle style, FormatFileImport formatFileImport) {
        this.cyServiceRegistrar=cyServiceRegistrar;
        this.style=style;
        this.cyNetwork=cyNetwork;
        this.formatFileImport = formatFileImport;
        AvailableCommands availableCommands = cyServiceRegistrar.getService(AvailableCommands.class);
        if (!availableCommands.getNamespaces().contains("enhancedGraphics")) {
            JOptionPane.showMessageDialog(cyServiceRegistrar.getService(CySwingApplication.class).getJFrame(),
                    "You need to install enhancedGraphics from the App Manager or Cytoscape App Store.",
                    "Dependency error", JOptionPane.ERROR_MESSAGE);
            return;
        }

    }
    public VisualStyle perform(String vizCol){
        //System.out.println("entered here");
        CyTable networktable = cyNetwork.getDefaultNetworkTable();

        CyTable nodeTable = cyNetwork.getDefaultNodeTable();
        AvailableCommands availableCommands = cyServiceRegistrar.getService(AvailableCommands.class);
        if (!availableCommands.getNamespaces().contains("enhancedGraphics")) {
            JOptionPane.showMessageDialog(cyServiceRegistrar.getService(CySwingApplication.class).getJFrame(),
                    "You need to install enhancedGraphics from the App Manager or Cytoscape App Store.",
                    "Dependency error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        vizCol = vizCol;

//        if(nodeTable.getColumn(vizCol) == null){
//            System.out.println("Entered first time");
//            nodeTable.createColumn(vizCol, String.class, false);
//        }
//        else {
//            System.out.println("entered in the deleted section");
//            nodeTable.deleteColumn(vizCol);
//            nodeTable.createColumn(vizCol, String.class, false);
//        }


//        for(CyNode node : cyNetwork.getNodeList()) {
//
//
//            String nodeStyle = toEnhancedGraphics(labeltextSettings);
////            EGSettings egSettings = new EGSettings();
////            String nodeStyle = toEnhancedGraphics(egSettings);
////            System.out.println("intital Nodestyle->" + nodeStyle);
////
////
////            System.out.println("Middle Nodestyle->" + nodeStyle);
////            String hex = String.format("#%02X%02X%02X", 44, 162, 44);
////            nodeStyle += "colorlist=\"" +hex +"\" valuelist=\"1\"";
////
////            String nodeLabels = "labellist=\"" + "p" + "\" showlabels=\"true\"";
////            nodeStyle += " " + nodeLabels;
//            //System.out.println("Final Nodestyle->" + nodeStyle);
//
//            nodeTable.getRow(node.getSUID()).set(vizCol, nodeStyle);
//        }
//        VisualMappingFunctionFactory passthroughFactory = cyServiceRegistrar.getService(VisualMappingFunctionFactory.class, "(mapping.type=passthrough)");
//        VisualLexicon lex = cyServiceRegistrar.getService(RenderingEngineManager.class).getDefaultVisualLexicon();
//        VisualProperty<?> customGraphics;
//        PassthroughMapping<?,?> pMapping;
//        customGraphics= lex.lookup(CyNode.class, graphphicsnumber);
//        pMapping = (PassthroughMapping<?,?>) passthroughFactory.createVisualMappingFunction(vizCol, String.class, customGraphics);
//        style.addVisualMappingFunction(pMapping);
        style = ApplyEdgeStyle(cyServiceRegistrar,style);
        style = ApplyNodeStyle(cyServiceRegistrar,style,formatFileImport);
        FillAnnotation fillAnnotation = new FillAnnotation(style,formatFileImport,vizCol,cyNetwork,cyServiceRegistrar);
        style = fillAnnotation.RunAlgorithm(style);
//        VisualProperty customGraphicsP = lex.lookup(CyNode.class, "NODE_CUSTOMGRAPHICS_POSITION_1");
//        Object upperLeft = customGraphicsP.parseSerializableString("NE,SE,c,0.00,0.00");
//        style.setDefaultValue(customGraphicsP, upperLeft);

//        VisualProperty customegraphicssize = lex.lookup(CyNode.class,"NODE_CUSTOMGRAPHICS_SIZE_1");
//        style.setDefaultValue(customegraphicssize,20.0);
        return style;
    }
    public static String toEnhancedGraphics(LabeltextSettings labeltextSettings) {
        String style = "label:";
        //System.out.println("style ->"+style);
        for(String key : labeltextSettings.getKeys()) {
            // if(this.type.equals(ChartType.CIRCOS) || !key.equals(EGSettings.ARC_WIDTH)) {
            // ARC_WIDTH is only for CIRCOS

            style += " ";
            style += key;
            style += "=\"" + labeltextSettings.get(key) + "\"";
            //}
        }

        style += " ";
        // style += this.colors.toEnhancedGraphics(values, this.type);

        return style;
    }
    public VisualStyle ApplyEdgeStyle(CyServiceRegistrar cyServiceRegistrar,VisualStyle style){
        //System.out.println("entered into applyedge styles");
        VisualMappingManager vmm = cyServiceRegistrar.getService(VisualMappingManager.class);
        VisualMappingFunctionFactory discreteFactory = cyServiceRegistrar.getService(VisualMappingFunctionFactory.class, "(mapping.type=discrete)");
         DiscreteMapping edgetypeMapping = (DiscreteMapping) discreteFactory
               .createVisualMappingFunction(CyNetwork.NAME, String.class,
                     BasicVisualLexicon.EDGE_LINE_TYPE);
        DiscreteMapping edgeunselectedMapping = (DiscreteMapping) discreteFactory
                .createVisualMappingFunction(CyNetwork.NAME, String.class,
                        BasicVisualLexicon.EDGE_UNSELECTED_PAINT);

        DiscreteMapping edgestrokeunselectedMapping = (DiscreteMapping) discreteFactory
                .createVisualMappingFunction(CyNetwork.NAME, String.class,
                        BasicVisualLexicon.EDGE_STROKE_UNSELECTED_PAINT);
        DiscreteMapping edgecolor = (DiscreteMapping) discreteFactory
                .createVisualMappingFunction(CyNetwork.NAME, String.class,
                        BasicVisualLexicon.EDGE_PAINT);
        VisualLexicon lex = cyServiceRegistrar.getService(RenderingEngineManager.class).getDefaultVisualLexicon();
        VisualProperty targetedgecolor = lex.lookup(CyEdge.class,"EDGE_TARGET_ARROW_UNSELECTED_PAINT");
        DiscreteMapping edgetargetarrowcolor = (DiscreteMapping) discreteFactory
                .createVisualMappingFunction(CyNetwork.NAME, String.class,
                        targetedgecolor);

      CausalPathedgestyles causalPathedgestyles = new CausalPathedgestyles();
        HashMap<String, Color> edgecolormap = causalPathedgestyles.getEdgeColor();
        HashMap<String,String> edgetypemap = causalPathedgestyles.getEdgeType();
        for(CyEdge edge : cyNetwork.getEdgeList()) {
            String edgename = cyNetwork.getRow(edge).get(CyNetwork.NAME,String.class);
            String [] edgeproperty = cyNetwork.getRow(edge).get(CyNetwork.NAME,String.class).split("\\s+");

            String edgepropertymiddle = edgeproperty[1].substring(1,edgeproperty[1].length()-1);
            //System.out.println(edgepropertymiddle);
            if(Objects.equals(edgetypemap.get(edgepropertymiddle),"solid"))
              edgetypeMapping.putMapValue(edgename, LineTypeVisualProperty.SOLID);
            else if (Objects.equals(edgetypemap.get(edgepropertymiddle),"dotted"))
                edgetypeMapping.putMapValue(edgename, LineTypeVisualProperty.EQUAL_DASH);
            edgeunselectedMapping.putMapValue(edgename,edgecolormap.get(edgepropertymiddle));
            edgestrokeunselectedMapping.putMapValue(edgename,edgecolormap.get(edgepropertymiddle));
            edgecolor.putMapValue(edgename,edgecolormap.get(edgepropertymiddle));
            edgetargetarrowcolor.putMapValue(edgename,edgecolormap.get(edgepropertymiddle));
         }
        style.addVisualMappingFunction(edgetypeMapping);
        style.addVisualMappingFunction(edgecolor);
        style.addVisualMappingFunction(edgestrokeunselectedMapping);
        style.addVisualMappingFunction(edgeunselectedMapping);
        style.addVisualMappingFunction(edgetargetarrowcolor);
        return style;

    }
    public VisualStyle ApplyNodeStyle(CyServiceRegistrar cyServiceRegistrar,VisualStyle style,FormatFileImport formatFileImport){
        //System.out.println("entered into applynode styles");
        VisualMappingManager vmm = cyServiceRegistrar.getService(VisualMappingManager.class);
        VisualMappingFunctionFactory discreteFactory = cyServiceRegistrar.getService(VisualMappingFunctionFactory.class, "(mapping.type=discrete)");
        DiscreteMapping nodecolorMapping = (DiscreteMapping) discreteFactory
                .createVisualMappingFunction(CyNetwork.NAME, String.class,
                        BasicVisualLexicon.NODE_FILL_COLOR);
        DiscreteMapping nodebordercolorMapping = (DiscreteMapping) discreteFactory
                .createVisualMappingFunction(CyNetwork.NAME, String.class,
                        BasicVisualLexicon.NODE_BORDER_PAINT);
        HashMap<String,RGBValue> nodespcificcolor = formatFileImport.getNodeSpecificColor();
        HashMap<String,RGBValue> nodespcificbordercolor = formatFileImport.getNodespecificBordercolor();
        for (Map.Entry mapElement : nodespcificcolor.entrySet()) {

           // System.out.println(mapElement.getKey() +" "+ nodespcificcolor.get(mapElement.getKey()) );
        }
        for (CyNode node : cyNetwork.getNodeList()){

            String nodename = cyNetwork.getRow(node).get(CyNetwork.NAME,String.class);
            //System.out.println("nodename-> "+nodename);
            if  (nodespcificcolor.containsKey(nodename))
                nodecolorMapping.putMapValue(nodename,new Color(nodespcificcolor.get(nodename).getR(),nodespcificcolor.get(nodename).getG(),
                    nodespcificcolor.get(nodename).getB()));
            if(nodespcificbordercolor.containsKey(nodename))
                nodebordercolorMapping.putMapValue(nodename,new Color(nodespcificbordercolor.get(nodename).getR(),nodespcificbordercolor.get(nodename).getG(),
                nodespcificbordercolor.get(nodename).getB()));

        }
        style.addVisualMappingFunction(nodecolorMapping);
        style.addVisualMappingFunction(nodebordercolorMapping);

        return style;

    }
    public String toEnhancedGraphics(EGSettings egSettings) {
        String style = ChartType.CIRCOS.getStyle();
        System.out.println("style ->"+style);
        for(String key : egSettings.getKeys()) {
           // if(this.type.equals(ChartType.CIRCOS) || !key.equals(EGSettings.ARC_WIDTH)) {
                // ARC_WIDTH is only for CIRCOS

                style += " ";
                style += key;
                style += "=\"" + egSettings.get(key) + "\"";
            //}
        }

        style += " ";
       // style += this.colors.toEnhancedGraphics(values, this.type);

        return style;
    }
    public static void updateNodeStyle(CyServiceRegistrar cyServiceRegistrar,
                                       CyNetworkView view, List<CyNode> nodes, CyNetwork cyNetwork) {
        // manager.flushEvents();
        VisualMappingManager vmm = cyServiceRegistrar.getService(VisualMappingManager.class);
        VisualMappingFunctionFactory discreteFactory = cyServiceRegistrar.getService(VisualMappingFunctionFactory.class, "(mapping.type=discrete)");
        if(discreteFactory == null) {
            return ;
        }

        // DiscreteMapping dMapping = (DiscreteMapping) discreteFactory
        //       .createVisualMappingFunction(CyNetwork.NAME, String.class,
        //             BasicVisualLexicon.NODE_FILL_COLOR);

        VisualStyle style = vmm.getVisualStyle(view);
        for (CyNode node: nodes) {

            String name = cyNetwork.getRow(node).get(CyNetwork.NAME, String.class);
            System.out.println(name);

            // if (view.getNodeView(node) != null)
            //   style.apply(view.getModel().getRow(node), view.getNodeView(node));
        }
        // style.apply(view);
    }
    public enum ChartType implements Serializable {
        /** Donut chart. */
        CIRCOS("Donut Chart", "circoschart: firstarc=1.0"),
        /** Pie chart. */
        PIE("Pie Chart", "piechart:");

        String name;
        String style;
        ChartType(String name, String style) {
            this.name = name;
            this.style = style;
        }

        @Override
        public String toString() {
            return this.name;
        }

        /**
         * Returns the name of the type.
         * @return The name of the type.
         */
        public String getName() {
            return this.name;
        }

        /**
         * Returns the beginning of the enhancedGraphics String corresponding to the chart.
         * @return The enhancedGraphics String.
         */
        public String getStyle() {
            return this.style;
        }
    }
}
