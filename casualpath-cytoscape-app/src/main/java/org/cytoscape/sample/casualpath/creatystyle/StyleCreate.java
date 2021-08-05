package org.cytoscape.sample.casualpath.creatystyle;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.sample.casualpath.ImportandExecutor.tasks.FormatFileImport;
import org.cytoscape.sample.casualpath.design.ApplyEnhancedGraphics;
import org.cytoscape.sample.casualpath.design.LabeltextSettings;
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
import org.cytoscape.work.SynchronousTaskManager;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.cytoscape.sample.casualpath.design.LabeltextSettings.*;

public class StyleCreate {
     SynchronousTaskManager synchronousTaskManager;
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
    public StyleCreate(SynchronousTaskManager synchronousTaskManager, CyServiceRegistrar cyServiceRegistrar, FormatFileImport formatFileImport, CyNetwork cyNetwork){
        this.synchronousTaskManager = synchronousTaskManager;
        this.cyServiceRegistrar = cyServiceRegistrar;
        this.formatFileImport = formatFileImport;
        this.Allnoodescolor = formatFileImport.getAllNodesColor();
        this.AllnodesBorderColor = formatFileImport.getAllNodesBorderColor();
        System.out.println("all node color->"+formatFileImport.getAllNodesColor());
        System.out.println("all node border color"+formatFileImport.getAllNodesBorderColor());
        this.cyNetwork = cyNetwork;

    }
    public StyleCreate(SynchronousTaskManager synchronousTaskManager, CyServiceRegistrar cyServiceRegistrar, RGBValue AllNodesColor, CyNetwork cyNetwork){
        this.synchronousTaskManager = synchronousTaskManager;
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
            System.out.println(dep.getDisplayName());
            if(dep.getDisplayName().equals("Fit Custom Graphics to node")){
                //System.out.println("entered here");
                dep.setDependency(false);
            }
            System.out.println(dep.isDependencyEnabled());
            names.put(dep.getIdString(), dep);
        }

        System.out.println("color->"+Allnoodescolor.getR()+Allnoodescolor.getG()+Allnoodescolor.getB());
        VisualMappingFunctionFactory continuousFactory =
                cyServiceRegistrar.getService(VisualMappingFunctionFactory.class, "(mapping.type=continuous)");
        VisualMappingFunctionFactory passthroughFactory =
                cyServiceRegistrar.getService(VisualMappingFunctionFactory.class, "(mapping.type=passthrough)");
        VisualMappingFunctionFactory discreteFactory =
                cyServiceRegistrar.getService(VisualMappingFunctionFactory.class, "(mapping.type=discrete)");

        // Set the node label to the name column
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


        VisualMappingFunction<String, ArrowShape> edgetargetshape = discreteFactory.createVisualMappingFunction("DELTA", String.class,BasicVisualLexicon.EDGE_TARGET_ARROW_SHAPE);

        vs.setDefaultValue(BasicVisualLexicon.EDGE_TARGET_ARROW_SHAPE, ArrowShapeVisualProperty.DELTA);
        vs.setDefaultValue(BasicVisualLexicon.EDGE_BEND, EdgeBendVisualProperty.DEFAULT_EDGE_BEND);


        vs.setDefaultValue(BasicVisualLexicon.EDGE_PAINT,Greencolor);


        vs.setDefaultValue(BasicVisualLexicon.EDGE_UNSELECTED_PAINT,Greencolor);

        vs.setDefaultValue(BasicVisualLexicon.EDGE_STROKE_UNSELECTED_PAINT,Greencolor);

        VisualLexicon lex = cyServiceRegistrar.getService(RenderingEngineManager.class).getDefaultVisualLexicon();
        VisualProperty targetedgecolor = lex.lookup(CyEdge.class,"EDGE_TARGET_ARROW_UNSELECTED_PAINT");

        vs.setDefaultValue(targetedgecolor,Greencolor);

        //VisualProperty customgraphicssize = lex.lookup(CyNode.class,"nodeCustomGraphicsSizeSync");
        //vs.setDefaultValue(customgraphicssize,false);
        //VisualPropertyDependency
        LabeltextSettings labeltextSettings1 = new LabeltextSettings();
        CyTable nodeTable =cyNetwork.getDefaultNodeTable();
        for(int i=1;i<=9;i++){
            String temp =vizCol +i;
            if(nodeTable.getColumn(temp) == null){
                //System.out.println("Entered first time");
                nodeTable.createColumn(temp, String.class, false);
            }
        }
        nodeTable.createColumn(Heading,String.class,false);
//        for(CyEdge edge : cyNetwork.getEdgeList()) {
//            String [] edgeproperty = cyNetwork.getRow(edge).get(CyNetwork.NAME,String.class).split("\\s+");
//
//            String edgepropertymiddle = edgeproperty[1].substring(1,edgeproperty[1].length()-1);
//            System.out.println(edgepropertymiddle);
//        }

        ApplyEnhancedGraphics applyEnhancedGraphics = new ApplyEnhancedGraphics(cyServiceRegistrar,cyNetwork,vs,formatFileImport,Heading);
//        vs = applyEnhancedGraphics.perform("NODE_CUSTOMGRAPHICS_1",labeltextSettings1,0,vizCol+0);
//        LabeltextSettings labeltextSettings2 = new LabeltextSettings();
//        String hex = String.format("#%02X%02X%02X", 44, 162, 44);
//        labeltextSettings2.set(OUTLINE_COLOR_PROPERTY, "#EC1920");
//        labeltextSettings2.set(LABEL_POSTION_PROPERTY,"northeast");
//        labeltextSettings2.set(LABEL_BG_PROPERTY,"#EC1920");
        vs = applyEnhancedGraphics.perform(vizCol);


        //perform(cyServiceRegistrar,vs,cyNetwork);
        List<CyNode> nodes = cyNetwork.getNodeList();
        vmm.addVisualStyle(vs);
        vmm.setCurrentVisualStyle(vs);
        vmm.setVisualStyle(vs, netView);
        vs.apply(netView);

        //updateNodeStyle(cyServiceRegistrar,netView,nodes,cyNetwork);

    }





}
