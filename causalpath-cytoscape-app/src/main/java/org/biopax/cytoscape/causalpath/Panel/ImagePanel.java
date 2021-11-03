package org.biopax.cytoscape.causalpath.Panel;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

    private Image img;

    public ImagePanel(String img) {
        this(new ImageIcon(img).getImage());
    }

    public ImagePanel(Image img) {

        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));

        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }

    public void paintComponent(Graphics g) {

        g.drawImage(img, 1, 0, img.getWidth(null),img.getHeight(null),null);
    }
}