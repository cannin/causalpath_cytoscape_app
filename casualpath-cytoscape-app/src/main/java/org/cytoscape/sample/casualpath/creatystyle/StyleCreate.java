package org.cytoscape.sample.casualpath.creatystyle;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.sample.casualpath.ImportandExecutor.tasks.FormatFileImport;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.ArrowShapeVisualProperty;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.values.ArrowShape;
import org.cytoscape.view.vizmap.*;
import org.cytoscape.view.vizmap.mappings.PassthroughMapping;
import org.cytoscape.work.SynchronousTaskManager;

import java.awt.*;

public class StyleCreate {
     SynchronousTaskManager synchronousTaskManager;
     CyServiceRegistrar cyServiceRegistrar;
     FormatFileImport formatFileImport;
    RGBValue Allnoodescolor;
    public StyleCreate(SynchronousTaskManager synchronousTaskManager, CyServiceRegistrar cyServiceRegistrar, FormatFileImport formatFileImport){
        this.synchronousTaskManager = synchronousTaskManager;
        this.cyServiceRegistrar = cyServiceRegistrar;
        this.formatFileImport = formatFileImport;
        this.Allnoodescolor = formatFileImport.getAllNodesColor();
    }
    public StyleCreate(SynchronousTaskManager synchronousTaskManager, CyServiceRegistrar cyServiceRegistrar,RGBValue AllNodesColor){
        this.synchronousTaskManager = synchronousTaskManager;
        this.cyServiceRegistrar = cyServiceRegistrar;
        this.Allnoodescolor = AllNodesColor;
    }
    public void createStyle(CyNetworkView netView
                            ) {
        VisualMappingManager vmm = cyServiceRegistrar.getService(VisualMappingManager.class);


        VisualStyle currentStyle = vmm.getCurrentVisualStyle();
        VisualStyle vs = cyServiceRegistrar.getService(VisualStyleFactory.class).createVisualStyle(currentStyle);
        vs.setTitle("StEMAP Style");

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
        vs.setDefaultValue(BasicVisualLexicon.NODE_BORDER_PAINT,new Color(50, 50, 50));
        vs.setDefaultValue(BasicVisualLexicon.NODE_LABEL_COLOR, Color.BLACK);
        vs.setDefaultValue(BasicVisualLexicon.NODE_BORDER_WIDTH,2.0);


        VisualMappingFunction<String, ArrowShape> edgetargetshape = discreteFactory.createVisualMappingFunction("DELTA", String.class,BasicVisualLexicon.EDGE_TARGET_ARROW_SHAPE);

        vs.setDefaultValue(BasicVisualLexicon.EDGE_PAINT,new Color(44, 162, 44));


        vs.setDefaultValue(BasicVisualLexicon.EDGE_UNSELECTED_PAINT,new Color(44, 162, 44));

        vs.setDefaultValue(BasicVisualLexicon.EDGE_STROKE_UNSELECTED_PAINT,new Color(44, 162, 44));

        vs.setDefaultValue(BasicVisualLexicon.EDGE_TARGET_ARROW_SHAPE, ArrowShapeVisualProperty.DELTA);



        vmm.addVisualStyle(vs);
        vmm.setCurrentVisualStyle(vs);
        vmm.setVisualStyle(vs, netView);
        vs.apply(netView);
    }
}
