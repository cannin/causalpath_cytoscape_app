package org.cytoscape.sample.casualpath;

import javax.swing.*;
import java.awt.*;

public class CausalPathHelp extends javax.swing.JFrame {

    private String helpString;

    public CausalPathHelp() {
        initComponents();
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setForeground(new java.awt.Color(255, 255, 51));
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE).addContainerGap()));
        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
       EventQueue.invokeLater(new Runnable() {

            public void run() {
                new CausalPathHelp().setVisible(true);
            }
        });
    }

    private JScrollPane jScrollPane1;
    private JTextArea jTextArea1;


    public void setText(int buttonNumber) {
      
        helpString =
                "Causal Path Cytoscape App: \n\n"
                + "----\n"
        +"Proteomic analysis (proteomics) refers to the systematic identification and \n"
        +"quantification of the complete complement of proteins (the proteome) of a biological system\n"
        +"(cell, tissue, organ, biological fluid, or organism) at a specific point in time. CausalPath (Causal\n"
         +"       interactions from proteomic profiles: molecular data meets pathway knowledge) is a big\n"
        +"helping hand for the researchers to inspect experimental observations using prior mechanistic\n"
        +"knowledge with an understanding in post-translational modifications. It is a computational method\n"
        +"to generate causal explanations for proteomic profiles using prior mechanistic knowledge in the literature,\n"
         +"       as recorded in cellular pathway maps. It generates casual explanations for the coordinated changes\n"
        +"in proteomic and phosphoproteomic profiles using literature-curated mechanistic pathways.\n"
        +"Protein features found from experimental data get changed in coordination, and CausalPath\n"
        +"automates the search for causal explanations in the literature.\n"

                + "Importing inputs for the Casual Path Algortihm Visualization ::::::: \n"
                + "Import network using \n"
                + "Import style format file using  \n"
                + "----\n"
                + "\n"
                + "----\n"
                + "Running Casual Path Cytoscape app :::::: \n"
                 ;
        
                

        this.setTitle("Causal Path Cytoscpae Help : ");
        
        jTextArea1.setText(helpString);
        jTextArea1.setCaretPosition(0);
    }
}
