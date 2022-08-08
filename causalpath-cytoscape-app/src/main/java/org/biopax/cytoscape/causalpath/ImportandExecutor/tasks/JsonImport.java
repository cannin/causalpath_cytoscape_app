package org.biopax.cytoscape.causalpath.ImportandExecutor.tasks;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.biopax.cytoscape.causalpath.Panel.LegendPanel;
import org.biopax.cytoscape.causalpath.utils.CyNetworkUtils;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.service.util.CyServiceRegistrar;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class JsonImport {
    private static CyServiceRegistrar cyServiceRegistrar;
    private String jsonpath;
    private File Formatfile;
    public CyNetwork cyNetwork;
    public LegendPanel legendPanel;
    public File temp = File.createTempFile("tempSIF", ".sif");

    public JsonImport(File selectedFile, String jsonpath, CyServiceRegistrar cyServiceRegistrar, LegendPanel legendPanel) throws IOException {
        this.cyServiceRegistrar = cyServiceRegistrar;
        this.jsonpath = jsonpath;
        this.Formatfile = Formatfile;
        this.legendPanel = legendPanel;
        writeJSON(Formatfile, jsonpath, legendPanel);
    }

    public void writeJSON(File Jsonfile, String jsonpath, LegendPanel legendPanel) throws IOException {
        JsonParser jsonParser = new JsonParser();

        try (FileReader reader = new FileReader(jsonpath)) {
            //Read JSON file
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
            JsonObject jObject = jsonParser.parse(reader).getAsJsonObject();

            JsonArray jsonnodeArray = jObject.get("nodes").getAsJsonArray();

            for (int i = 0; i < jsonnodeArray.size(); i++) {
                JsonElement element = jsonnodeArray.get(i);

                JsonObject jsoneachnodeObject = element.getAsJsonObject();

                JsonObject childjsonObject = jsoneachnodeObject.get("data").getAsJsonObject();
                JsonArray childjsonArray = childjsonObject.get("sites").getAsJsonArray();

                for (int j = 0; j < childjsonArray.size(); j++) {
                    JsonElement element1 = childjsonArray.get(j);
                    JsonObject newobject = element1.getAsJsonObject();

                }

            }
            JsonArray jsonedgearray = jObject.get("edges").getAsJsonArray();
            for (int i = 0; i < jsonedgearray.size(); i++) {

                JsonObject eachedgeobject = jsonedgearray.get(i).getAsJsonObject();

                JsonObject eachchildobject = eachedgeobject.get("data").getAsJsonObject();
                String source = eachchildobject.get("source").toString();
                String edgetype = eachchildobject.get("edgeType").toString();
                String target = eachchildobject.get("target").toString();
                bw.write(source + "\t" + edgetype + "\t" + target);
                bw.write("\n");

            }


            bw.close();
            String OS = System.getProperty("os.name").toLowerCase();
            String name = "";
            if (OS.indexOf("win") >= 0) {
                name = jsonpath.substring(jsonpath.lastIndexOf("\\") + 1, jsonpath.length());
            } else name = jsonpath.substring(jsonpath.lastIndexOf("/") + 1, jsonpath.length());
            String modelID = name;

            //cyNetwork = CyNetworkUtils.readCyNetworkFromFile(cyServiceRegistrar, temp);
            cyNetwork.getRow(cyNetwork).set(CyNetwork.NAME, modelID);
            JProgressBar jProgressBar = legendPanel.getStatusBar();
            jProgressBar.setValue(50);
            legendPanel.getStatusLabel().setText("uploaded..");
            legendPanel.getStatusLabel().setForeground(new Color(5, 102, 8));
            JOptionPane.showMessageDialog(null, Jsonfile);
            JButton jButton = legendPanel.getSubmitbutton();
            jButton.setEnabled(true);


        } catch (NullPointerException ignored) {
            JOptionPane optionPane = new JOptionPane("File Open Failed", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Failure");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
            legendPanel.getStatusLabel().setText("Failed");
            legendPanel.getStatusLabel().setForeground(Color.RED);


        } catch (FileNotFoundException e) {
            JOptionPane optionPane = new JOptionPane("File Not Found", JOptionPane.ERROR_MESSAGE);
            JDialog dialog = optionPane.createDialog("Failure");
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public CyNetwork getCyNetwork() {
        return cyNetwork;
    }
}
