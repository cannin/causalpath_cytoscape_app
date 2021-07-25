package org.cytoscape.sample.casualpath.design;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * enhancedGraphics settings
 */
public class EGSettings implements Serializable {
    private static final long serialVersionUID = -7356849626176436599L;

    /** enhancedGraphics setting name. In charge of the width of the border. */
    public static final String BORDER_WIDTH = "borderwidth";
    /** enhancedGraphics setting name. In charge of the color of the border. */
    public static final String BORDER_COLOR = "bordercolor";
    /** enhancedGraphics setting name. In charge of the font family of the label. */
    public static final String LABEL_FONT = "labelfont";
    /** enhancedGraphics setting name. In charge of the color of the label. */
    public static final String LABEL_COLOR = "labelcolor";
    /** enhancedGraphics setting name. In charge of the font size of the label. */
    public static final String LABEL_SIZE = "labelsize";
    /** enhancedGraphics setting name. In charge of the angle of the first arc.
     * @see ArcStartValues */
    public static final String ARC_START = "arcstart";
    /** enhancedGraphics setting name. In charge of the width of the arcs. */
    public static final String ARC_WIDTH = "arcwidth";
    public static final String ARC_RANGE = "range";
    /** enhancedGraphics setting name. In charge of the direction of the arcs.
     * @see ArcDirectionValues */
    public static final String ARC_DIRECTION = "arcdirection";
    public static  final String FIRST_ARC_WIDTH = "firstarcwidth";

     public  static  final String CIRCLE_LABELS = "circlelabels";
    /** Default enhancedGraphics setting value.
     * @see  EGSettings#BORDER_WIDTH */
    public static final String BORDER_WIDTH_DEFAULT = "0.1";
    /** Default enhancedGraphics setting value.
     * @see  EGSettings#BORDER_COLOR */
    public static final String BORDER_COLOR_DEFAULT = "black";
    /** Default enhancedGraphics setting value.
     * @see  EGSettings#LABEL_FONT */
    public static final String LABEL_FONT_DEFAULT = "SansSerif";
    /** Default enhancedGraphics setting value.
     * @see  EGSettings#LABEL_COLOR */
    public static final String LABEL_COLOR_DEFAULT = "black";
    /** Default enhancedGraphics setting value.
     * @see  EGSettings#LABEL_SIZE */
    public static final String LABEL_SIZE_DEFAULT = "15";
    /** Default enhancedGraphics setting value.
     * @see EGSettings#ARC_START
     * @see ArcStartValues
     * @see ArcStartValues#TWELVE */
    public static final String ARC_START_DEFAULT = ArcStartValues.TWELVE.toEnhancedGraphics();
    /** Default enhancedGraphics setting value.
     * @see  EGSettings#ARC_WIDTH */
    public static final String ARC_WIDTH_DEFAULT = "1.0";



    public static final String  CIRCLE_LABELS_DEFAULT="p";
    /** Default enhancedGraphics setting value.
     * @see EGSettings#ARC_DIRECTION
     * @see ArcDirectionValues
     * @see ArcDirectionValues#CLOCKWISE */
    public static final String ARC_DIRECTION_DEFAULT = ArcDirectionValues.CLOCKWISE.toString();

    private Map<String, String> settings;

    /**
     * Create a set of enhancedGraphics settings with default values for each setting.
     */
    public EGSettings() {
        this.settings = new HashMap<>();

        // Set the default values
        this.settings.put(BORDER_WIDTH, BORDER_WIDTH_DEFAULT);
        this.settings.put(BORDER_COLOR, BORDER_COLOR_DEFAULT);
        this.settings.put(LABEL_FONT, LABEL_FONT_DEFAULT);
        String hex = String.format("#%02X%02X%02X", 44, 162, 44);
        this.settings.put(LABEL_COLOR, LABEL_COLOR_DEFAULT);
        this.settings.put(LABEL_SIZE, LABEL_SIZE_DEFAULT);
        this.settings.put(ARC_START, ARC_START_DEFAULT);
        this.settings.put(ARC_WIDTH, ARC_WIDTH_DEFAULT);
        this.settings.put(ARC_DIRECTION, ARC_DIRECTION_DEFAULT);
        this.settings.put(CIRCLE_LABELS,CIRCLE_LABELS_DEFAULT);



    }

    /**
     * Add or set an enhancedGraphics setting value.
     * @param setting Name of the enhancedGraphics setting.
     * @param value Value of the enhancedGraphics setting.
     */
    public void set(String setting, String value) {
        this.settings.put(setting, value);
    }

    /**
     * Returns the value of the specific enhancedGraphics setting.
     * @param setting Name of the wanted enhancedGraphics setting.
     * @return The value of the setting, or <code>null</code> if the setting is not set.
     */
    public String get(String setting) {
        return this.settings.get(setting);
    }

    /**
     * Returns the set of enhancedGraphics settings defined.
     * @return the set of enhancedGraphics setting names.
     */
    public Set<String> getKeys() {
        return this.settings.keySet();
    }

    /**
     * Defines the different values of the 'arcdirection' enhancedGraphics setting.
     * @see EGSettings#ARC_DIRECTION
     */
    public enum ArcDirectionValues {
        /** The direction of the arc is clockwise.  */
        CLOCKWISE("clockwise"),
        /** The direction of the arc is counterclockwise.  */
        COUNTERCLOCKWISE("counterclockwise");

        private String str;

        private ArcDirectionValues(String str) {
            this.str=str;
        }

        /**
         * Returns the enhancedGraphics value corresponding to the enum value.
         * @return The enhancedGraphics value.
         */
        public String toString() {
            return this.str;
        }

        /**
         * Creates an {@link ArcDirectionValues} corresponding to the enhancedGraphics value.
         * @param str The enhancedGraphics value to transform into a ArcDirectionValues.
         * @return The corresponding ArcDirectionValues or {@link #CLOCKWISE} if nothing corresponds.
         */
        public static ArcDirectionValues valueOfStr(String str) {
            for(ArcDirectionValues adv : ArcDirectionValues.values()) {
                if(adv.str.equals(str)) {
                    return adv;
                }
            }

            return ArcDirectionValues.CLOCKWISE;
        }
    }

    /**
     * Defines the different values of the 'arcstart' enhancedGraphics setting.
     * @see EGSettings#ARC_START
     */
    public enum ArcStartValues {
        /** The first arc will start at 90 degrees, corresponding to 12 o'clock. */
        TWELVE("12 o'clock", "90"),
        /** The first arc will start at 0 degree, corresponding to 3 o'clock. */
        THREE("3 o'clock", "0"),
        /** The first arc will start at 270 degrees, corresponding to 6 o'clock. */
        SIX("6 o'clock", "270"),
        /** The first arc will start at 180 degrees, corresponding to 9 o'clock. */
        NINE("9 o'clock", "180");

        private String displayValue;
        private String egValue;

        private ArcStartValues(String displayValue, String egValue) {
            this.displayValue=displayValue;
            this.egValue=egValue;
        }

        /**
         * Returns the human readable name of the enum that can be used in forms element.
         * @return the display name.
         */
        public String toString() {
            return this.displayValue;
        }

        /**
         * Returns the enhancedGraphics setting value corresponding.
         * @return the enhancedGraphics value.
         */
        public String toEnhancedGraphics() {
            return this.egValue;
        }

        /**
         * Returns the {@link ArcStartValues} corresponding to the given enhancedGraphics value.
         * @param egValue value to parse into {@link ArcStartValues}
         * @return the {@link ArcStartValues} corresponding, {@link #TWELVE} if the value cannot be parsed.
         */
        public static ArcStartValues valueOfEG(String egValue) {
            for(ArcStartValues asv : ArcStartValues.values()) {
                if(asv.egValue.equals(egValue)) {
                    return asv;
                }
            }

            return TWELVE;
        }
    }
}
