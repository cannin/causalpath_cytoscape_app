package org.biopax.cytoscape.causalpath.design;

import org.biopax.cytoscape.causalpath.creatystyle.RGBValue;

import java.awt.*;
import java.util.HashMap;

public class CausalPathedgestyles {
    public String UpregulatesExpression = "upregulates-expression";
    public String DownregulatesExpression = "downregulates-expression";
    public String Phosphorylates = "phosphorylates";
    public String DePhosphorylates = "dephosphorylates";
    public String TypeProperty;
    public RGBValue colorProperty;
    public HashMap<String, Color> EdgeColor = new HashMap<>();
    public HashMap<String, String> EdgeType = new HashMap<>();

    public CausalPathedgestyles() {
        EdgeColor.put(Phosphorylates, new Color(44, 162, 44));
        EdgeColor.put(UpregulatesExpression, new Color(44, 162, 44));
        EdgeColor.put(DePhosphorylates, new Color(227, 26, 28));
        EdgeColor.put(DownregulatesExpression, new Color(227, 26, 28));

        EdgeType.put(Phosphorylates, "solid");
        EdgeType.put(UpregulatesExpression, "dotted");
        EdgeType.put(DePhosphorylates, "solid");
        EdgeType.put(DownregulatesExpression, "dotted");

    }

    public HashMap<String, Color> getEdgeColor() {
        return EdgeColor;
    }

    public HashMap<String, String> getEdgeType() {
        return EdgeType;
    }

}
