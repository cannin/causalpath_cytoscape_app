package org.cytoscape.sample.casualpath.design;

import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.command.AvailableCommands;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.subnetwork.CyRootNetwork;
import org.cytoscape.sample.casualpath.CyActivator;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.VisualLexicon;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.mappings.PassthroughMapping;

import javax.swing.*;
import java.io.Serializable;
import java.util.List;

public class ApplyEnhancedGraphics {
      CyServiceRegistrar cyServiceRegistrar;
    CyNetwork cyNetwork;
    VisualStyle style;

    public static final String CP_COLUMN_NAMESPACE = "CausalPath Visualizer";

    public ApplyEnhancedGraphics(CyServiceRegistrar cyServiceRegistrar,CyNetwork cyNetwork, VisualStyle style) {
        this.cyServiceRegistrar=cyServiceRegistrar;
        this.style=style;
        this.cyNetwork=cyNetwork;

        AvailableCommands availableCommands = cyServiceRegistrar.getService(AvailableCommands.class);
        if (!availableCommands.getNamespaces().contains("enhancedGraphics")) {
            JOptionPane.showMessageDialog(cyServiceRegistrar.getService(CySwingApplication.class).getJFrame(),
                    "You need to install enhancedGraphics from the App Manager or Cytoscape App Store.",
                    "Dependency error", JOptionPane.ERROR_MESSAGE);
            return;
        }

    }
    public VisualStyle perform(String graphphicsnumber, LabeltextSettings labeltextSettings,int a,String vizCol){
        System.out.println("entered here");
        CyTable nodeTable = cyNetwork.getDefaultNodeTable();
        AvailableCommands availableCommands = cyServiceRegistrar.getService(AvailableCommands.class);
        if (!availableCommands.getNamespaces().contains("enhancedGraphics")) {
            JOptionPane.showMessageDialog(cyServiceRegistrar.getService(CySwingApplication.class).getJFrame(),
                    "You need to install enhancedGraphics from the App Manager or Cytoscape App Store.",
                    "Dependency error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        vizCol = vizCol;
        System.out.println(vizCol);
        nodeTable.createColumn(vizCol, String.class, false);

        for(CyNode node : cyNetwork.getNodeList()) {


            String nodeStyle = toEnhancedGraphics(labeltextSettings);
//            EGSettings egSettings = new EGSettings();
//            String nodeStyle = toEnhancedGraphics(egSettings);
//            System.out.println("intital Nodestyle->" + nodeStyle);
//
//
//            System.out.println("Middle Nodestyle->" + nodeStyle);
//            String hex = String.format("#%02X%02X%02X", 44, 162, 44);
//            nodeStyle += "colorlist=\"" +hex +"\" valuelist=\"1\"";
//
//            String nodeLabels = "labellist=\"" + "p" + "\" showlabels=\"true\"";
//            nodeStyle += " " + nodeLabels;
            System.out.println("Final Nodestyle->" + nodeStyle);

            nodeTable.getRow(node.getSUID()).set(vizCol, nodeStyle);
        }
        VisualMappingFunctionFactory passthroughFactory = cyServiceRegistrar.getService(VisualMappingFunctionFactory.class, "(mapping.type=passthrough)");
        VisualLexicon lex = cyServiceRegistrar.getService(RenderingEngineManager.class).getDefaultVisualLexicon();
        VisualProperty<?> customGraphics;
        PassthroughMapping<?,?> pMapping;
        customGraphics= lex.lookup(CyNode.class, graphphicsnumber);
        pMapping = (PassthroughMapping<?,?>) passthroughFactory.createVisualMappingFunction(vizCol, String.class, customGraphics);
        style.addVisualMappingFunction(pMapping);
//        VisualProperty customGraphicsP = lex.lookup(CyNode.class, "NODE_CUSTOMGRAPHICS_POSITION_1");
//        Object upperLeft = customGraphicsP.parseSerializableString("NE,SE,c,0.00,0.00");
//        style.setDefaultValue(customGraphicsP, upperLeft);

//        VisualProperty customegraphicssize = lex.lookup(CyNode.class,"NODE_CUSTOMGRAPHICS_SIZE_1");
//        style.setDefaultValue(customegraphicssize,20.0);
        return style;
    }
    public String toEnhancedGraphics(LabeltextSettings labeltextSettings) {
        String style = "label:";
        System.out.println("style ->"+style);
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
    public String toEnhancedGraphics(EGSettings egSettings) {
        String style = ChartType.CIRCOS.getStyle();;
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
