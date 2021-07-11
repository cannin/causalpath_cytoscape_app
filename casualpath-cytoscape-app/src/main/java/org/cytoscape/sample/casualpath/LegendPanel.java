package org.cytoscape.sample.casualpath;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.sample.casualpath.ImportandExecutor.tasks.FormatFileImport;
import org.cytoscape.sample.casualpath.ImportandExecutor.tasks.JsonImport;
import org.cytoscape.sample.casualpath.ImportandExecutor.tasks.SIFImport;
import org.cytoscape.sample.casualpath.Listensers.*;
import org.cytoscape.sample.casualpath.utils.CyNetworkUtils;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.task.visualize.ApplyVisualStyleTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.xml.sax.SAXException;

public class LegendPanel extends JPanel implements CytoPanelComponent {
	
	private static final long serialVersionUID = 8292806967891823933L;
	public  FormatFileImport formatFileImport;
	public CyServiceRegistrar cyServiceRegistrar;
    public LoadNetworkFileTaskFactory loadNetworkFileTaskFactory;
    public CyNetworkView view;
	public  CyNetwork cyNetwork;
	JButton networkfileuploadbutton = new JButton("Upload Sif File");
	JButton formatfileuploadbutton = new JButton("Upload format File");
	JButton Jsonfileuploadbutton= new JButton("Upload Json File");
	JButton submitbutton= new JButton("Submit");
	JButton helpButton= new JButton();
	JButton exitButton = new JButton();
	Checkbox Siftype = new Checkbox();
	Checkbox Jsontype = new Checkbox();
	JLabel siflabel = new JLabel("Check to upload file in Sif format");
	JLabel Jsonlabel = new JLabel("Check to upload file in Json format");
	JPanel jPanel3 = new JPanel();
	JPanel jPanel4= new JPanel();
	JPanel jPanel6 = new JPanel();
	JPanel jPanel5 = new JPanel();
	JPanel jPanel7 = new JPanel();
	public LegendPanel legendPanel;
	Boolean SifButtonFlag = false;
	Boolean JsonbuttonFlag = false;

	JProgressBar statusBar = new javax.swing.JProgressBar();
	JLabel statusLabel = new javax.swing.JLabel();

	ButtonGroup buttonGroup1 = new ButtonGroup();
	JScrollPane jScrollPane = new JScrollPane();
    public ApplyVisualStyleTaskFactory applyVisualStyleTaskFactory;
	public LegendPanel(CyServiceRegistrar cyServiceRegistrar) {
		this.cyServiceRegistrar=cyServiceRegistrar;
		//this.importVisualStyleTaskFactory = importVisualStyleTaskFactory;
		this.loadNetworkFileTaskFactory=loadNetworkFileTaskFactory;
		this.applyVisualStyleTaskFactory=applyVisualStyleTaskFactory;
		this.view=view;
		this.legendPanel = this;
		setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		JLabel headingLabel = new JLabel();
		headingLabel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
		headingLabel.setForeground(new java.awt.Color(169, 29, 29));
		headingLabel.setText("       Casual Path Cytoscape App");
		jPanel6.setVisible(false);
		jPanel4.setVisible(false);

        networkfileuploadbutton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        formatfileuploadbutton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Jsonfileuploadbutton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        submitbutton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        //submitbutton.setOpaque(true);
        submitbutton.setToolTipText("Please upload the files correctly before submitting");
        //submitbutton.setEnabled(false);
		//submitbutton.addActionListener(new SubmitButtonActionListener(this,cyServiceRegistrar));
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        //networkfileuploadbutton.addActionListener(new SIFImportButtonActionListener(this,cyServiceRegistrar));
		formatfileuploadbutton.setEnabled(false);
		//formatfileuploadbutton.addActionListener(new FormatFileImportActionListener(this,cyServiceRegistrar));
		submitbutton.setEnabled(false);
		//Jsonfileuploadbutton.addActionListener(new JsonFileImportActionListener(this,cyServiceRegistrar));
        networkfileuploadbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				SifUploadAction(legendPanel);
			}
		});
        formatfileuploadbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SifButtonFlag =true;
				JsonbuttonFlag = false;

		        formatfileuploadaction(legendPanel);
			}
		});
        Jsonfileuploadbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SifButtonFlag =false;
				JsonbuttonFlag = true;
				JsonfileuploadAction(legendPanel);
			}
		});
        submitbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				submitaction(legendPanel,cyNetwork);
			}
		});
		Siftype.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
               jPanel4.setVisible(true);
               statusBar.setValue(0);
               submitbutton.setEnabled(false);
               formatfileuploadbutton.setEnabled(false);
               if(Jsontype.getState()){
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
				if(Siftype.getState()){
					jPanel4.setVisible(false);
					Siftype.setState(false);
				}
			}
		});

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
										.addGroup(jPanel3Layout.createSequentialGroup()
												.addComponent(Jsonlabel)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(Jsontype, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
								.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(Jsonlabel)
										.addComponent(Jsontype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addContainerGap(12, Short.MAX_VALUE))
		);
		jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Select Sif and Format File"));

		buttonGroup1.add(networkfileuploadbutton);


		buttonGroup1.add(formatfileuploadbutton);


		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(
				jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel4Layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(networkfileuploadbutton,javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(formatfileuploadbutton,javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
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
		jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
		 GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
		jPanel5.setLayout(jPanel5Layout);
		jPanel5Layout.setHorizontalGroup(
				jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel5Layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(helpButton, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap())
		);
		jPanel5Layout.setVerticalGroup(
				jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(exitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(helpButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addContainerGap())
		);
		helpButton.setForeground(new java.awt.Color(0, 200, 0));
		helpButton.setText("Help");
		helpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				helpButtonActionPerformed(e);
			}
		});

		exitButton.setForeground(new java.awt.Color(200, 0, 0));
		exitButton.setText("Exit");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//exitButtonActionPerformed(evt);
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
								.addComponent(Jsonfileuploadbutton,GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
								.addContainerGap())
		);
		jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Status bar"));

		statusLabel.setFont(new Font("Times New Roman", 2, 15));
		statusLabel.setText("Upload status");

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
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(statusLabel, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
								.addContainerGap())
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
														.addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(submitbutton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
								.addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(submitbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)

								.addContainerGap(90, Short.MAX_VALUE))
		);

		jScrollPane.setViewportView(mainPanel);



		GroupLayout layout = new GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
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

	private void JsonfileuploadAction(LegendPanel legendPanel) {
		String path="";
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter("JSON Source File (*.json)", "json");
		fileChooser.setFileFilter(jsonFilter);
		int result = fileChooser.showOpenDialog(this.legendPanel);
		File selectedFile = null;
		if (result == JFileChooser.APPROVE_OPTION) {

			selectedFile = fileChooser.getSelectedFile();
			path = selectedFile.getAbsolutePath();
			//System.out.println("selected file path->"+path);


		}

		try {
			JsonImport importer = new JsonImport(selectedFile,path, cyServiceRegistrar,this.legendPanel);
			cyNetwork = importer.getCyNetwork();
		}
		catch (IOException ex) {
			Logger.getLogger(JsonFileImportActionListener.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void submitaction(LegendPanel legendPanel, CyNetwork cyNetwork)  {
		try {
			if(SifButtonFlag && !JsonbuttonFlag){
			CyNetworkUtils.createViewAndRegister(cyServiceRegistrar, this.cyNetwork,formatFileImport);}
			else if (!SifButtonFlag && JsonbuttonFlag){
				System.out.println("entered into json runner");
				CyNetworkUtils.createViewAndRegister(cyServiceRegistrar, this.cyNetwork);
			}
		} catch (IOException | TransformerException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
	}

	private void formatfileuploadaction(LegendPanel legendPanel) {
		String path="";
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
			formatFileImport = new FormatFileImport(selectedFile,path, cyServiceRegistrar,this.legendPanel);

		}  catch (IOException ex) {
			Logger.getLogger(FormatFileImportActionListener.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SAXException | ParserConfigurationException | TransformerException saxException) {
			saxException.printStackTrace();
		}
	}

	private void SifUploadAction(LegendPanel legendPanel) {
		String path="";
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("SIF file (*.sif)", "sif");
		fileChooser.setFileFilter(xmlFilter);
		int result = fileChooser.showOpenDialog(this.legendPanel);
		File selectedFile = null;
		if (result == JFileChooser.APPROVE_OPTION) {

			selectedFile = fileChooser.getSelectedFile();
			path = selectedFile.getAbsolutePath();
			System.out.println("selected file path->"+path);

		}
		try {
			SIFImport sifImport = new SIFImport(selectedFile,path, cyServiceRegistrar, this.legendPanel);
			cyNetwork = sifImport.getSIFCyNetwork();
			//CyNetworkUtils.createViewAndRegister(cyServiceRegistrar, cyNetwork);

		} catch (ParserConfigurationException | SAXException | IOException ex) {
			JOptionPane optionPane = new JOptionPane("File Import Failed", JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog("Failure");
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
			Logger.getLogger(SIFImportButtonActionListener.class.getName()).log(Level.SEVERE, null, ex);
		} catch (TransformerException transformerConfigurationException) {
			JOptionPane optionPane = new JOptionPane("File Import Failed", JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog("Failure");
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
			JOptionPane.showMessageDialog(null,"Upload Failed","Fail Message",1);
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
		return "Casual Path Cytoscape App";
	}


	public Icon getIcon() {
		return null;
	}
	public JButton getNetworkbutton(){
		return networkfileuploadbutton;
	}
	private void helpButtonActionPerformed(ActionEvent e) {//GEN-FIRST:event_helpButtonActionPerformed
		CasualPathHelp help = new CasualPathHelp();
		help.setText(1);
		help.setVisible(true);


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
}
