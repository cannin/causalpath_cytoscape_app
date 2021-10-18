package org.biopax.cytoscape.causalpath.creatystyle;

import org.biopax.cytoscape.causalpath.ImportandExecutor.tasks.FormatFileImport;
import org.biopax.cytoscape.causalpath.design.ApplyEnhancedGraphics;
import org.biopax.cytoscape.causalpath.design.LabeltextSettings;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.VisualLexicon;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.presentation.RenderingEngineManager;
import org.cytoscape.view.presentation.property.ArrowShapeVisualProperty;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.EdgeBendVisualProperty;
import org.cytoscape.view.presentation.property.values.ArrowShape;
import org.cytoscape.view.vizmap.*;
import org.cytoscape.view.vizmap.mappings.PassthroughMapping;


import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StyleCreate {

     CyServiceRegistrar cyServiceRegistrar;
     FormatFileImport formatFileImport;
    RGBValue Allnoodescolor;
    RGBValue AllnodesBorderColor;
    public CyNetwork cyNetwork;
    public static final String CYNODETABLE_VIZCOL="CausalPath";
    public static final String CYNODETABLE_INNERVIZCOL=CYNODETABLE_VIZCOL+" LineChart";
    String vizCol=CYNODETABLE_INNERVIZCOL;
    public static final String CP_COLUMN_NAMESPACE = "CausalPath Visualizer";
    public  static String Heading = "CausalPath ToolInfo";

    public Color Greencolor = new Color(44, 162, 44);
    public  Color BlackColor = new Color(50,50,50) ;
    public StyleCreate( CyServiceRegistrar cyServiceRegistrar, FormatFileImport formatFileImport, CyNetwork cyNetwork){

        this.cyServiceRegistrar = cyServiceRegistrar;
        this.formatFileImport = formatFileImport;
        this.Allnoodescolor = formatFileImport.getAllNodesColor();
        this.AllnodesBorderColor = formatFileImport.getAllNodesBorderColor();

        this.cyNetwork = cyNetwork;

    }
    public StyleCreate(CyServiceRegistrar cyServiceRegistrar, RGBValue AllNodesColor, CyNetwork cyNetwork){

        this.cyServiceRegistrar = cyServiceRegistrar;
        this.Allnoodescolor = AllNodesColor;
        this.cyNetwork = cyNetwork;
    }
    public void createStyle(CyNetworkView netView
                            ) {
        VisualMappingManager vmm = cyServiceRegistrar.getService(VisualMappingManager.class);


        VisualStyle currentStyle = vmm.getCurrentVisualStyle();
        VisualStyle vs = cyServiceRegistrar.getService(VisualStyleFactory.class).createVisualStyle(currentStyle);
        vs.setTitle("CausalPath Style");

       Set<VisualPropertyDependency<?>> visualPropertyDependency = vs.getAllVisualPropertyDependencies();

        final Map<String, VisualPropertyDependency<?>> names = new HashMap<>();
        for(final VisualPropertyDependency<?> dep:visualPropertyDependency) {

            if(dep.getDisplayName().equals("Fit Custom Graphics to node")){

                dep.setDependency(false);
            }

            names.put(dep.getIdString(), dep);
        }



        VisualMappingFunctionFactory passthroughFactory =
                cyServiceRegistrar.getService(VisualMappingFunctionFactory.class, "(mapping.type=passthrough)");
        VisualMappingFunctionFactory discreteFactory =
                cyServiceRegistrar.getService(VisualMappingFunctionFactory.class, "(mapping.type=discrete)");


        PassthroughMapping<String,String> name = (PassthroughMapping<String,String>)
                passthroughFactory.createVisualMappingFunction(CyNetwork.NAME,
                        String.class,
                        BasicVisualLexicon.NODE_LABEL);
        vs.addVisualMappingFunction(name);


        // Set the label color to black
        vs.setDefaultValue(BasicVisualLexicon.NODE_FILL_COLOR,new Color(Allnoodescolor.getR(),Allnoodescolor.getG(),Allnoodescolor.getB()));
        vs.setDefaultValue(BasicVisualLexicon.NODE_BORDER_PAINT,new Color(AllnodesBorderColor.getR(),AllnodesBorderColor.getG(),AllnodesBorderColor.getB()));
        vs.setDefaultValue(BasicVisualLexicon.NODE_LABEL_COLOR, Color.BLACK);
        vs.setDefaultValue(BasicVisualLexicon.NODE_BORDER_WIDTH,2.0);

        vs.setDefaultValue(BasicVisualLexicon.EDGE_TARGET_ARROW_SHAPE, ArrowShapeVisualProperty.DELTA);
        vs.setDefaultValue(BasicVisualLexicon.EDGE_BEND, EdgeBendVisualProperty.DEFAULT_EDGE_BEND);


        vs.setDefaultValue(BasicVisualLexicon.EDGE_PAINT,Greencolor);


        vs.setDefaultValue(BasicVisualLexicon.EDGE_UNSELECTED_PAINT,Greencolor);
        vs.setDefaultValue(BasicVisualLexicon.EDGE_SELECTED_PAINT,Color.YELLOW);

        vs.setDefaultValue(BasicVisualLexicon.EDGE_STROKE_UNSELECTED_PAINT,Greencolor);
        vs.setDefaultValue(BasicVisualLexicon.EDGE_STROKE_SELECTED_PAINT,Color.YELLOW);

        VisualLexicon lex = cyServiceRegistrar.getService(RenderingEngineManager.class).getDefaultVisualLexicon();
        VisualProperty targetedgecolor = lex.lookup(CyEdge.class,"EDGE_TARGET_ARROW_UNSELECTED_PAINT");
        VisualProperty targetselectededgecolor = lex.lookup(CyEdge.class,"EDGE_TARGET_ARROW_SELECTED_PAINT");

        vs.setDefaultValue(targetedgecolor,Greencolor);
        vs.setDefaultValue(targetselectededgecolor,Color.YELLOW);
        CyTable nodeTable =cyNetwork.getDefaultNodeTable();
        for(int i=1;i<=9;i++){
            String temp =vizCol +i;
            if(nodeTable.getColumn(temp) == null){

                nodeTable.createColumn(temp, String.class, false);
            }
        }
        nodeTable.createColumn(Heading,String.class,false);


        ApplyEnhancedGraphics applyEnhancedGraphics = new ApplyEnhancedGraphics(cyServiceRegistrar,cyNetwork,vs,formatFileImport,Heading);

        vs = applyEnhancedGraphics.perform(vizCol);
        vmm.addVisualStyle(vs);
        vmm.setCurrentVisualStyle(vs);
        vmm.setVisualStyle(vs, netView);
        vs.apply(netView);


    }
}
