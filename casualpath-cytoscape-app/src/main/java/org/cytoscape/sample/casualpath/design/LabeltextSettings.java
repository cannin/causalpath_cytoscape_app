package org.cytoscape.sample.casualpath.design;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LabeltextSettings implements Serializable {


    public  static  final String LABEL_TEXT_LABEL_PROPERTY="label";
     public static final String LABEL_WIDTH_PROPERTY= "labelsize";
     public static  final String LABEL_POSTION_PROPERTY="position";
     public static final String LABEL_PADDING_PROPERTY = "padding";
     public static final String LABEL_ALIGNMENT_PROPERTY= "labelAlignment";
     public static final String LABEL_BG_PROPERTY="bgColor";
     public static final String BACKGROUND_BOOL = "background";
     public static final String OUTLINE_PROPERTY  = "outline";
    public static final String OUTLINE_COLOR_PROPERTY= "outlineColor";
    public static final String OUTLINE_WIDTH_PROPERTY="outlineWidth";

    public  static  final String LABEL_TEXT_LABEL_DEFAULT="p";
    public static final String LABEL_SIZE_DEFAULT= "10";
    public static  final String LABEL_POSTION_DEFAULT="northwest";
    public static final String LABEL_PADDING_DEFAULT = "0.05";
    public static final String LABEL_ALIGNMENT_DEFAULT= "left";
    public static final String BACKGROUND_BOOL_DEAFULT= "true";
    String hex = String.format("#%02X%02X%02X", 44, 162, 44);
    public static final String LABEL_BG_DEFAULT="black";
    public static final String OUTLINE_PROPERTY_DEFAULT= "true";
    public static final String OUTLINE_COLOR_DEFAULT= "#EC1920";
    public static final String OUTLINE_WIDTH_DEFAULT = "4.0";
    private Map<String, String> settings;
    public LabeltextSettings (){
        this.settings = new HashMap<>();
        this.settings.put(BACKGROUND_BOOL,BACKGROUND_BOOL_DEAFULT);
        this.settings.put(LABEL_BG_PROPERTY,hex);
        this.settings.put(LABEL_TEXT_LABEL_PROPERTY,LABEL_TEXT_LABEL_DEFAULT);
        //this.settings.put(LABEL_ALIGNMENT_PROPERTY,LABEL_ALIGNMENT_DEFAULT);
        this.settings.put(LABEL_PADDING_PROPERTY,LABEL_PADDING_DEFAULT);
        this.settings.put(LABEL_WIDTH_PROPERTY,LABEL_SIZE_DEFAULT);
       this.settings.put(LABEL_POSTION_PROPERTY,LABEL_POSTION_DEFAULT);
       this.settings.put(OUTLINE_PROPERTY,OUTLINE_PROPERTY_DEFAULT);
       this.settings.put(OUTLINE_COLOR_PROPERTY,"#00FFFFFF");
       this.settings.put(OUTLINE_WIDTH_PROPERTY,OUTLINE_WIDTH_DEFAULT);


    }
    public void set(String setting, String value) {
        this.settings.put(setting, value);
    }

    public String get(String setting) {
        return this.settings.get(setting);
    }


    public Set<String> getKeys() {
        return this.settings.keySet();
    }

}
