package org.biopax.cytoscape.causalpath.Panel;

import javax.swing.*;
import java.io.File;

public class FileHistory {
    private static String lastSifDir = null;
    private static String lastFormatDir = null;

    public static JFileChooser getSifFileChooser() {
        if(lastSifDir != null) {
            JFileChooser fc = new JFileChooser(lastSifDir);
            return fc;
        } else {
            JFileChooser fc = new JFileChooser();
            return fc;
        }
    }

    public static void setLastSifDir(File file) {
        lastSifDir = file.getParent();
    }

    public static JFileChooser getFormatFileChooser() {
        if(lastFormatDir != null) {
            JFileChooser fc = new JFileChooser(lastFormatDir);
            return fc;
        } else {
            JFileChooser fc = new JFileChooser();
            return fc;
        }
    }

    public static void setLastFormatDir(File file) {
        lastFormatDir = file.getParent();
    }
}