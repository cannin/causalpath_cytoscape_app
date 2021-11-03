package org.biopax.cytoscape.causalpath.Panel;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


import org.apache.poi.ss.formula.functions.T;
import org.cytoscape.application.events.*;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.*;
import org.cytoscape.model.events.SelectedNodesAndEdgesEvent;
import org.cytoscape.model.events.SelectedNodesAndEdgesListener;
import org.biopax.cytoscape.causalpath.CyActivator;
import org.biopax.cytoscape.causalpath.ImportandExecutor.tasks.FormatFileImport;
import org.biopax.cytoscape.causalpath.ImportandExecutor.tasks.JsonImport;
import org.biopax.cytoscape.causalpath.ImportandExecutor.tasks.SIFImport;
//import org.cytoscape.sample.casualpath.Listensers.*;
import org.biopax.cytoscape.causalpath.utils.CyNetworkUtils;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.task.visualize.ApplyVisualStyleTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.SynchronousTaskManager;
import org.cytoscape.work.Tunable;
import org.xml.sax.SAXException;

import static org.biopax.cytoscape.causalpath.ImportandExecutor.tasks.SIFImport.Edge_Info_col_Name;
import static org.biopax.cytoscape.causalpath.creatystyle.StyleCreate.Heading;

public class LegendPanel extends JPanel implements CytoPanelComponent, SelectedNodesAndEdgesListener, SetSelectedNetworksListener {

    private static final long serialVersionUID = 8292806967891823933L;
    public final static Logger LOGGER =
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static final String URLCODE = "https://github.com/cannin/causalpath_cytoscape_app";
    public static final String URLAPP = "https://apps.cytoscape.org/apps/causalpathcytoscapeapp";
    public static final String URLMethod = "https://pubmed.ncbi.nlm.nih.gov/34179843/";
    private static final String URLTutorial = "https://pubmed.ncbi.nlm.nih.gov/34179843/";
    public final String NodeUrl = "https://www.genecards.org/cgi-bin/carddisp.pl?gene=";
    String  name="";
    public FormatFileImport formatFileImport;
    public CyServiceRegistrar cyServiceRegistrar;
    public CyNetwork cyNetwork;
    JButton networkfileuploadbutton = new JButton("SIF File");
    JButton formatfileuploadbutton = new JButton("Format File");
    JButton Jsonfileuploadbutton = new JButton("Load Json File");
    JButton submitbutton = new JButton("Submit");
    JButton apphelp = new JButton();
    JButton methodhelp = new JButton();
    JButton tutorialhelp = new JButton();
    JButton codehelp = new JButton();
    JButton exitButton = new JButton();
    Checkbox Siftype = new Checkbox();
    Checkbox Jsontype = new Checkbox();
    JLabel siflabel = new JLabel("Check to load file in SIF format");
    JLabel Jsonlabel = new JLabel("Check to load file in Json format");
    JPanel jPanel3 = new JPanel();
    JPanel jPanel4 = new JPanel();
    JPanel jPanel6 = new JPanel();
    JPanel jPanel5 = new JPanel();
    JPanel jPanel7 = new JPanel();
    JPanel jpanel8 = new JPanel();
//    java.net.URL imgurl = new java.net.URL("UrlPath");
//    BufferedImage img = ImageIO.read(imgurl);
    ImagePanel imagepanel = new ImagePanel(new ImageIcon(Objects.requireNonNull(getClass().getResource("/legend.png"))).getImage());
    public LegendPanel legendPanel;
    Boolean SifButtonFlag = false;
    Boolean JsonbuttonFlag = false;
    Boolean Networkflag = false;
    Boolean NetworksetFlag=false;
    JProgressBar statusBar = new javax.swing.JProgressBar();
    JLabel statusLabel = new javax.swing.JLabel();

    ButtonGroup buttonGroup1 = new ButtonGroup();
    JScrollPane jScrollPane = new JScrollPane();
    public ResultPanel resultPanel;

    public CyActivator cyActivator;
    public  SynchronousTaskManager synchronousTaskManager;
    @Tunable(description = "networkName", context = "nogui")
    public String networkName ;
    public LegendPanel(CyServiceRegistrar cyServiceRegistrar, ResultPanel resultPanel, SynchronousTaskManager synchronousTaskManager) {
        this.cyServiceRegistrar = cyServiceRegistrar;
        this.synchronousTaskManager = synchronousTaskManager;
        this.resultPanel = resultPanel;


        this.legendPanel = this;


        cyServiceRegistrar.registerService(this, SelectedNodesAndEdgesListener.class, new Properties());

        cyServiceRegistrar.registerService(this, SetSelectedNetworksListener.class, new Properties());

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        JLabel headingLabel = new JLabel();
        headingLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        headingLabel.setForeground(new java.awt.Color(169, 29, 29));
        headingLabel.setText("     CausalPath");
        jPanel6.setVisible(false);
        jPanel4.setVisible(true);

//        networkfileuploadbutton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
//        formatfileuploadbutton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Jsonfileuploadbutton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
       // submitbutton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        submitbutton.setToolTipText("Please load the files correctly before submitting");


        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));


        formatfileuploadbutton.setEnabled(false);

        submitbutton.setEnabled(false);

        networkfileuploadbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Networkflag){

                    JOptionPane optionPane = new JOptionPane("Please Unselect the current network to proceed from the seleted Networks", JOptionPane.INFORMATION_MESSAGE);
                    JDialog dialog = optionPane.createDialog("Information");
                    dialog.setAlwaysOnTop(true);
                    dialog.setVisible(true);
                    return ;
                }
               // Networkflag=true;
                if(!NetworksetFlag){
                SifUploadAction(legendPanel,synchronousTaskManager,true);
                NetworksetFlag=true;

                }
                else
                    SifUploadAction(legendPanel,synchronousTaskManager,false);
            }
        });
        formatfileuploadbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SifButtonFlag = true;
                JsonbuttonFlag = false;

                formatfileuploadaction(legendPanel);
            }
        });
        Jsonfileuploadbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SifButtonFlag = false;
                JsonbuttonFlag = true;
                JsonfileuploadAction(legendPanel);
            }
        });
        submitbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitaction(legendPanel, cyNetwork);
            }
        });
        Siftype.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                jPanel4.setVisible(true);
                statusBar.setValue(0);
                submitbutton.setEnabled(false);
                formatfileuploadbutton.setEnabled(false);
                if (Jsontype.getState()) {
                    jPanel6.setVisible(false);
                    Jsontype.setState(false);
                }
            }
        });
        Jsontype.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                statusBar.setValue(0);
                submitbutton.setEnabled(false);
                jPanel6.setVisible(true);
                if (Siftype.getState()) {
                    jPanel4.setVisible(false);
                    Siftype.setState(false);
                }
            }
        });
        jPanel3.setVisible(false);
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Select files for the Visualization "));
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel3Layout.createSequentialGroup()
                                                        .addComponent(siflabel)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(Siftype, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
//										.addGroup(jPanel3Layout.createSequentialGroup()
//												.addComponent(Jsonlabel)
//												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//												.addComponent(Jsontype, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                )
                                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(siflabel)
                                        .addComponent(Siftype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//								.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//										.addComponent(Jsonlabel)
//										.addComponent(Jsontype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
//												javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Select SIF and .format File"));

        buttonGroup1.add(networkfileuploadbutton);


        buttonGroup1.add(formatfileuploadbutton);


        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(networkfileuploadbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(formatfileuploadbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(

                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)

                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(formatfileuploadbutton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(networkfileuploadbutton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Help"));

        GridLayout jpanel5layout = new GridLayout(2,2);

        jPanel5.setLayout(jpanel5layout);
        jPanel5.add(methodhelp);
        jPanel5.add(apphelp);
        jPanel5.add(tutorialhelp);

        jPanel5.add(codehelp);

//        GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
//        jPanel5.setLayout(jPanel5Layout);
//        jPanel5Layout.setHorizontalGroup(
//                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                        .addGroup(jPanel5Layout.createSequentialGroup()
//                                .addContainerGap(15, 38)
//                                .addComponent(helpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                .addComponent(helpbuttonnew1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                .addComponent(helpbuttonnew2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
//                                .addContainerGap(15, 38)
//
//                        )
//        );
//        jPanel5Layout.setVerticalGroup(
//                jPanel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                        .addGroup(jPanel5Layout.createSequentialGroup()
//                                .addContainerGap()
//                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                                .addComponent(helpButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                .addComponent(helpbuttonnew1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                .addComponent(helpbuttonnew2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                )
//                                .addContainerGap())
//        );


        jpanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jpanel8);
        jpanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap(29, 68)
                                .addComponent(submitbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        )
        );
        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(submitbutton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );

        //imagepanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        GroupLayout imagepanelLayout = new javax.swing.GroupLayout(imagepanel);
        imagepanel.setLayout(imagepanelLayout);
        System.out.println(imagepanel.getHeight()+" "+imagepanel.getWidth());

        methodhelp.setText("Method");
        apphelp.setText("App");
        codehelp.setText("Code");
        tutorialhelp.setText("Tutorial");
        methodhelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                methodhelpActionPerformed(e);
            }
        });
        apphelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apphelpButtonActionPerformed(e);
            }
        });
        codehelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codehelpButtonActionPerformed(e);
            }
        });
        tutorialhelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tutorialhelpButtonActionPerformed(e);
            }
        });
        

        exitButton.setForeground(new java.awt.Color(200, 0, 0));
        exitButton.setText("Legend");
        exitButton.setVisible(false);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        jPanel6.setBorder(BorderFactory.createTitledBorder("Select the JSON File"));


        GroupLayout jPanel6Layout = new GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(Jsonfileuploadbutton, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGap(0, 11, Short.MAX_VALUE)
                                .addComponent(Jsonfileuploadbutton, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Status Bar"));


        statusLabel.setText("Load Status");

        GroupLayout jPanel7Layout = new GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(statusBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(statusLabel, GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE))
                                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(statusBar, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                                //.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(statusLabel, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                        )
        );
        GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addComponent(headingLabel)
                                                .addGap(75, 75, 75))
                                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)

                                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jPanel4, GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                //.addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jpanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)

                                                //.addComponent(submitbutton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(imagepanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)

                                        ))
                        )
        );
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(headingLabel)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)

                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//								.addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jpanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                               // .addComponent(submitbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)

                                        .addComponent(imagepanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)

                                .addContainerGap(90, Short.MAX_VALUE)

                        )
        );

        jScrollPane.setViewportView(mainPanel);


        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
                                .addContainerGap())
        );
        this.add(headingLabel);
        this.add(networkfileuploadbutton);
        this.add(formatfileuploadbutton);
        this.add(submitbutton);
        this.setVisible(true);
    }
    private void methodhelpActionPerformed(ActionEvent e) {//GEN-FIRST:event_helpButtonActionPerformed
        try {
            Desktop.getDesktop().browse(new URI(URLMethod));
        } catch (IOException | URISyntaxException ioException) {
            ioException.printStackTrace();
        }
        //cyServiceRegistrar.getService(OpenBrowser.class).openURL(url);


    }
    private void apphelpButtonActionPerformed(ActionEvent e) {
        try {
            Desktop.getDesktop().browse(new URI(URLAPP));
        } catch (IOException | URISyntaxException ioException) {
            ioException.printStackTrace();
        }
    }

    private void codehelpButtonActionPerformed(ActionEvent e) {
        try {
            Desktop.getDesktop().browse(new URI(URLCODE));
        } catch (IOException | URISyntaxException ioException) {
            ioException.printStackTrace();
        }
    }

    private void tutorialhelpButtonActionPerformed(ActionEvent e) {
        try {
            Desktop.getDesktop().browse(new URI(URLTutorial));
        } catch (IOException | URISyntaxException ioException) {
            ioException.printStackTrace();
        }
    }

    private void JsonfileuploadAction(LegendPanel legendPanel) {
        String path = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter("JSON Source File (*.json)", "json");
        fileChooser.setFileFilter(jsonFilter);
        int result = fileChooser.showOpenDialog(this.legendPanel);
        File selectedFile = null;
        if (result == JFileChooser.APPROVE_OPTION) {

            selectedFile = fileChooser.getSelectedFile();
            path = selectedFile.getAbsolutePath();


        }

        try {
            JsonImport importer = new JsonImport(selectedFile, path, cyServiceRegistrar, this.legendPanel);
            cyNetwork = importer.getCyNetwork();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void submitaction(LegendPanel legendPanel, CyNetwork cyNetwork) {
        try {
            Networkflag = true;
            if (SifButtonFlag && !JsonbuttonFlag) {
                CyNetworkUtils.createViewAndRegister(cyServiceRegistrar, this.cyNetwork, formatFileImport, this.legendPanel);
            } else if (!SifButtonFlag && JsonbuttonFlag) {

                CyNetworkUtils.createViewAndRegister(cyServiceRegistrar, this.cyNetwork, this.legendPanel);
            }
        } catch (IOException | TransformerException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void formatfileuploadaction(LegendPanel legendPanel) {
        String path = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("Format file (*.format)", "format");
        fileChooser.setFileFilter(xmlFilter);
        int result = fileChooser.showOpenDialog(this.legendPanel);
        File selectedFile = null;
        if (result == JFileChooser.APPROVE_OPTION) {

            selectedFile = fileChooser.getSelectedFile();
            path = selectedFile.getAbsolutePath();
            //System.out.println("selected file path->"+path);

        }
        try {
            formatFileImport = new FormatFileImport(selectedFile, path, cyServiceRegistrar, this.legendPanel);

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SAXException | ParserConfigurationException | TransformerException saxException) {
            saxException.printStackTrace();
        }
    }

    private void SifUploadAction(LegendPanel legendPanel,SynchronousTaskManager synchronousTaskManager,boolean flag) {
        String path = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("SIF file (*.sif)", "sif");
        fileChooser.setFileFilter(xmlFilter);
        int result = fileChooser.showOpenDialog(this.legendPanel);
        File selectedFile = null;
        if (result == JFileChooser.APPROVE_OPTION) {

            selectedFile = fileChooser.getSelectedFile();
            path = selectedFile.getAbsolutePath();


        }
        System.out.println("path" + path);
        try {
            SIFImport sifImport = new SIFImport(selectedFile, path, cyServiceRegistrar, this.legendPanel,synchronousTaskManager,flag);
            cyNetwork = sifImport.getSIFCyNetwork();


        } catch (ParserConfigurationException | SAXException | IOException ex) {
            JOptionPane optionPane = new JOptionPane("File Import Failed", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Failure");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            ex.printStackTrace();
        } catch (TransformerException transformerConfigurationException) {
            JOptionPane optionPane = new JOptionPane("File Import Failed", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Failure");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            JOptionPane.showMessageDialog(null, "Upload Failed", "Fail Message", 1);
            transformerConfigurationException.printStackTrace();
        }
    }


    public Component getComponent() {
        return this;
    }


    public CytoPanelName getCytoPanelName() {
        return CytoPanelName.WEST;
    }


    public String getTitle() {
        return "CausalPath";
    }


    public Icon getIcon() {
        return null;
    }

    public JButton getNetworkbutton() {
        return networkfileuploadbutton;
    }



    public JButton getFormatfileuploadbutton() {
        return formatfileuploadbutton;
    }

    public void setFormatfileuploadbutton(JButton formatfileuploadbutton) {
        this.formatfileuploadbutton = formatfileuploadbutton;
    }

    public JButton getSubmitbutton() {
        return submitbutton;
    }

    public JProgressBar getStatusBar() {
        return statusBar;
    }

    public JLabel getStatusLabel() {

        return statusLabel;
    }

    public Checkbox getSiftype() {
        return Siftype;
    }

    public Checkbox getJsontype() {
        return Jsontype;
    }


    @Override
    public void handleEvent(SelectedNodesAndEdgesEvent selectedNodesAndEdgesEvent) {


        Collection<CyNode> selectednode = selectedNodesAndEdgesEvent.getSelectedNodes();
        Collection<CyEdge> selectedEdges = selectedNodesAndEdgesEvent.getSelectedEdges();
        CyTable nodetable = cyNetwork.getDefaultNodeTable();
        CyTable edgetable = cyNetwork.getDefaultEdgeTable();

        if (selectednode.size() > 0) {
            for (CyNode node : selectednode) {

                name = cyNetwork.getRow(node).get(CyNetwork.NAME, String.class);

                String Totaltext = "";
                JFrame frame = new JFrame();
                DefaultTableModel tableModel = new DefaultTableModel();
                JTable jTable = new JTable(tableModel);
                String temp = nodetable.getRow(node.getSUID()).get(Heading, String.class);
                if (temp.length() > 0) {
                    String[] Siteinfo = temp.split("\\|");

                    int elementLength = 0;


                    tableModel.addColumn("Name");
                    tableModel.addColumn("Value");
                    Vector<String> Sitename = new Vector<>();
                    Vector<Double> Sitevalue = new Vector<>();
                    for(int i=0; i< Siteinfo.length; i++) {
                        String[] var = Siteinfo[i].split("\\s+");
                        Sitename.add(var[0]);
                        Double v = Double.parseDouble(var[1]);
                        v = Math.round(v * 100.0) / 100.0;
                        Sitevalue.add(v);
                        if(var[0].length() > elementLength) {
                            elementLength = var[0].length();
                        }
                        tableModel.insertRow(0, new Object[] { var[0],v });
                    }

//                    frame.setSize(550, 350);
//                    frame.add(new JScrollPane(jTable));
//                    frame.setVisible(true);

//                    for (int i = 0; i < Sitename.size(); i++) {
//                        String var = Sitename.get(i);
//                        String insert = var;
//                        StringBuffer sbSpace = new StringBuffer();
//                        for(int j= var.length();j<=elementLength;j++)
//                            sbSpace.append(" ");
//                        insert+=sbSpace;
//                        insert+="| ";
//                        insert += Sitevalue.get(i);
//
//                        Totaltext += (insert + "\n");
//
//                    }
               }

                resultPanel.updatetext(name, Totaltext, 0,jTable);
            }
            JLabel hyperlink = resultPanel.getNodelink();
            final String finalName = NodeUrl+name;
            System.out.println(finalName);
            hyperlink.setText("GeneCards");
            resultPanel.setFinalName(finalName);
            hyperlink.setVisible(true);
            hyperlink.setForeground(Color.BLUE);
            hyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


//            hyperlink.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    try {
//                        Desktop.getDesktop().browse(new URI(finalName));
//                    } catch (IOException | URISyntaxException ioException) {
//                        ioException.printStackTrace();
//                    }
//                }
//                @Override
//                public void mouseReleased(MouseEvent e) {
//                    hyperlink.setForeground(Color.RED);
//                }
//
//                @Override
//                public void mouseEntered(MouseEvent e) {
//                    hyperlink.setForeground(Color.YELLOW);
//                }
//
//                @Override
//                public void mouseExited(MouseEvent e) {
//                    hyperlink.setForeground(Color.BLUE);
//                }
//            });

        }
        else if (selectedEdges.size() > 0) {
            JFrame jFrame = new JFrame();
            JTable jTable = new JTable();
            for (CyEdge edge : selectedEdges) {

                String temp = edgetable.getRow(edge.getSUID()).get("name", String.class);
                String Edgeinfo = edgetable.getRow(edge.getSUID()).get(Edge_Info_col_Name, String.class);
                String[] splited = temp.split("\\s+");
                String TotalText = "";
                TotalText += "Staring Node: " + splited[0] + "\n";
                TotalText += "Ending Node: " + splited[2] + "\n";
                TotalText += "Type: " + splited[1].substring(1, splited[1].length() - 1) + "\n";
                TotalText += "Site(s):\n";
                if (Edgeinfo != null) {
                    String[] Edgeinfoarr = Edgeinfo.split("\\;");


                    int rem = (Edgeinfoarr.length) % 3;
                    int size = Edgeinfoarr.length / 3;

                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < 3; j++) {
                            TotalText += Edgeinfoarr[i * 3 + j] + " ";
                        }
                        TotalText += "\n";
                    }
                    if (rem != 0) {
                        for (int j = (size) * 3; j < size * 3 + rem; j++) {
                            TotalText += Edgeinfoarr[j] + " ";
                        }
                        TotalText += "\n";
                    }

                }
                resultPanel.updatetext(splited[1].substring(1, splited[1].length() - 1), TotalText, 1,jTable);
            }
                JLabel hyperlink = resultPanel.getNodelink();
                hyperlink.setVisible(false);



        } else {
            resultPanel.HidePanel();
        }


    }


    @Override
    public void handleEvent(SetSelectedNetworksEvent setSelectedNetworksEvent) {
        Collection<CyNetwork> selectednetwork = setSelectedNetworksEvent.getNetworks();
        try {
            for (CyNetwork cyNetwork1 : selectednetwork) {


                cyNetwork = cyNetwork1;
            }
            if (selectednetwork.size() == 0 && Networkflag){
                JOptionPane.showMessageDialog(null, "Select or Create a network");
                Networkflag=false;
            }
            else if(selectednetwork.size()>1){

                JOptionPane optionPane = new JOptionPane("Please Unselect the current network to proceed from the seleted Networks", JOptionPane.INFORMATION_MESSAGE);
                JDialog dialog = optionPane.createDialog("Information");
                dialog.setAlwaysOnTop(true);
                dialog.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


}
