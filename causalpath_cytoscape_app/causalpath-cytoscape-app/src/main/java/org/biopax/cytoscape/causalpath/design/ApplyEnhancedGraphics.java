package org.biopax.cytoscape.causalpath.design;

import org.biopax.cytoscape.causalpath.ImportandExecutor.tasks.FormatFileImport;
import org.biopax.cytoscape.causalpath.creatystyle.Algorithm.FillAnnotation;
import org.biopax.cytoscape.causalpath.creatystyle.RGBValue;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.command.AvailableCommands;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.view.model.VisualLexicon;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.LineTypeVisualProperty;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Objects;

public class ApplyEnhancedGraphics {
    CyServiceRegistrar cyServiceRegistrar;
    CyNetwork cyNetwork;
    VisualStyle style;
    FormatFileImport formatFileImport;
    String ToolTipHeading;

    public static final String CP_COLUMN_NAMESPACE = "CausalPath Visualizer";

    public ApplyEnhancedGraphics(CyServiceRegistrar cyServiceRegistrar, CyNetwork cyNetwork, VisualStyle style, FormatFileImport formatFileImport, String heading) {
        this.cyServiceRegistrar = cyServiceRegistrar;
        this.style = style;
        this.ToolTipHeading = heading;
        this.cyNetwork = cyNetwork;
        this.formatFileImport = formatFileImport;
        AvailableCommands availableCommands = cyServiceRegistrar.getService(AvailableCommands.class);
        if (!availableCommands.getNamespaces().contains("enhancedGraphics")) {
            JOptionPane.showMessageDialog(cyServiceRegistrar.getService(CySwingApplication.class).getJFrame(),
                    "You need to install enhancedGraphics from the App Manager or Cytoscape App Store.",
                    "Dependency error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public VisualStyle perform(String vizCol) {


        AvailableCommands availableCommands = cyServiceRegistrar.getService(AvailableCommands.class);
        if (!availableCommands.getNamespaces().contains("enhancedGraphics")) {
            JOptionPane.showMessageDialog(cyServiceRegistrar.getService(CySwingApplication.class).getJFrame(),
                    "You need to install enhancedGraphics from the App Manager or Cytoscape App Store.",
                    "Dependency error", JOptionPane.ERROR_MESSAGE);
            return null;
        }


        style = ApplyEdgeStyle(cyServiceRegistrar, style);
        style = ApplyNodeStyle(cyServiceRegistrar, style, formatFileImport);
        FillAnnotation fillAnnotation = new FillAnnotation(style, formatFileImport, vizCol, cyNetwork, cyServiceRegistrar, ToolTipHeading);
        style = fillAnnotation.RunAlgorithm(style);

        return style;
    }

    public static String toEnhancedGraphics(LabeltextSettings labeltextSettings) {
        String style = "label:";

        for (String key : labeltextSettings.getKeys()) {

            style += " ";
            style += key;
            style += "=\"" + labeltextSettings.get(key) + "\"";
        }
        style += " ";
        return style;
    }

    public VisualStyle ApplyEdgeStyle(CyServiceRegistrar cyServiceRegistrar, VisualStyle style) {


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
        VisualProperty targetedgecolor = lex.lookup(CyEdge.class, "EDGE_TARGET_ARROW_UNSELECTED_PAINT");
        DiscreteMapping edgetargetarrowcolor = (DiscreteMapping) discreteFactory
                .createVisualMappingFunction(CyNetwork.NAME, String.class,
                        targetedgecolor);

        CausalPathedgestyles causalPathedgestyles = new CausalPathedgestyles();
        HashMap<String, Color> edgecolormap = causalPathedgestyles.getEdgeColor();
        HashMap<String, String> edgetypemap = causalPathedgestyles.getEdgeType();
        for (CyEdge edge : cyNetwork.getEdgeList()) {
            String edgename = cyNetwork.getRow(edge).get(CyNetwork.NAME, String.class);
            String[] edgeproperty = cyNetwork.getRow(edge).get(CyNetwork.NAME, String.class).split("\\s+");

            String edgepropertymiddle = edgeproperty[1].substring(1, edgeproperty[1].length() - 1);

            if (Objects.equals(edgetypemap.get(edgepropertymiddle), "solid"))
                edgetypeMapping.putMapValue(edgename, LineTypeVisualProperty.SOLID);
            else if (Objects.equals(edgetypemap.get(edgepropertymiddle), "dotted"))
                edgetypeMapping.putMapValue(edgename, LineTypeVisualProperty.EQUAL_DASH);
            edgeunselectedMapping.putMapValue(edgename, edgecolormap.get(edgepropertymiddle));
            edgestrokeunselectedMapping.putMapValue(edgename, edgecolormap.get(edgepropertymiddle));
            edgecolor.putMapValue(edgename, edgecolormap.get(edgepropertymiddle));
            edgetargetarrowcolor.putMapValue(edgename, edgecolormap.get(edgepropertymiddle));
        }
        style.addVisualMappingFunction(edgetypeMapping);
        style.addVisualMappingFunction(edgecolor);
        style.addVisualMappingFunction(edgestrokeunselectedMapping);
        style.addVisualMappingFunction(edgeunselectedMapping);
        style.addVisualMappingFunction(edgetargetarrowcolor);
        return style;

    }

    public VisualStyle ApplyNodeStyle(CyServiceRegistrar cyServiceRegistrar, VisualStyle style, FormatFileImport formatFileImport) {


        VisualMappingFunctionFactory discreteFactory = cyServiceRegistrar.getService(VisualMappingFunctionFactory.class, "(mapping.type=discrete)");
        DiscreteMapping nodecolorMapping = (DiscreteMapping) discreteFactory
                .createVisualMappingFunction(CyNetwork.NAME, String.class,
                        BasicVisualLexicon.NODE_FILL_COLOR);
        DiscreteMapping nodebordercolorMapping = (DiscreteMapping) discreteFactory
                .createVisualMappingFunction(CyNetwork.NAME, String.class,
                        BasicVisualLexicon.NODE_BORDER_PAINT);
        HashMap<String, RGBValue> nodespcificcolor = formatFileImport.getNodeSpecificColor();
        HashMap<String, RGBValue> nodespcificbordercolor = formatFileImport.getNodespecificBordercolor();

        for (CyNode node : cyNetwork.getNodeList()) {

            String nodename = cyNetwork.getRow(node).get(CyNetwork.NAME, String.class);

            if (nodespcificcolor.containsKey(nodename))
                nodecolorMapping.putMapValue(nodename, new Color(nodespcificcolor.get(nodename).getR(), nodespcificcolor.get(nodename).getG(),
                        nodespcificcolor.get(nodename).getB()));
            if (nodespcificbordercolor.containsKey(nodename))
                nodebordercolorMapping.putMapValue(nodename, new Color(nodespcificbordercolor.get(nodename).getR(), nodespcificbordercolor.get(nodename).getG(),
                        nodespcificbordercolor.get(nodename).getB()));

        }
        style.addVisualMappingFunction(nodecolorMapping);
        style.addVisualMappingFunction(nodebordercolorMapping);

        return style;

    }

}
