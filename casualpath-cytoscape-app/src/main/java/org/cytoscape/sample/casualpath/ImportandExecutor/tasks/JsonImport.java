package org.cytoscape.sample.casualpath.ImportandExecutor.tasks;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.sample.casualpath.Panel.LegendPanel;
import org.cytoscape.sample.casualpath.utils.CyNetworkUtils;
import org.cytoscape.service.util.CyServiceRegistrar;

import javax.swing.*;
import java.io.*;

public class JsonImport {
    private static CyServiceRegistrar cyServiceRegistrar;
    private String jsonpath;
    private File Formatfile;
    public  CyNetwork cyNetwork;
    public  LegendPanel legendPanel;
    public File temp = File.createTempFile("tempSIF", ".sif");
    public JsonImport(File selectedFile, String jsonpath, CyServiceRegistrar cyServiceRegistrar, LegendPanel legendPanel) throws IOException {
        this.cyServiceRegistrar = cyServiceRegistrar;
        this.jsonpath = jsonpath;
        this.Formatfile = Formatfile;
        this.legendPanel = legendPanel;
        writeJSON(Formatfile,jsonpath,legendPanel);
    }
    public void writeJSON(File Formatfile,String jsonpath,LegendPanel legendPanel)throws IOException

    {
        JsonParser jsonParser = new JsonParser();

        try (FileReader reader = new FileReader(jsonpath))
        {
            //Read JSON file
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
            JsonObject jObject = jsonParser.parse(reader).getAsJsonObject();
          //  System.out.println(jObject.get("nodes").isJsonArray());
            JsonArray jsonnodeArray = jObject.get("nodes").getAsJsonArray();
            //System.out.println(jsonElements);
            for(int i = 0; i < jsonnodeArray.size(); i++)
            {
                JsonElement element = jsonnodeArray.get(i);
                //System.out.println(element.isJsonObject());
                JsonObject jsoneachnodeObject = element.getAsJsonObject();
                //System.out.println(jsonObject.get("data").isJsonArray());
                JsonObject childjsonObject = jsoneachnodeObject.get("data").getAsJsonObject();
                JsonArray childjsonArray=childjsonObject.get("sites").getAsJsonArray();
                //System.out.println(childjsonArray);
                for (int j=0;j<childjsonArray.size();j++){
                    JsonElement element1 = childjsonArray.get(j);
                    JsonObject newobject = element1.getAsJsonObject();
                    //System.out.println(newobject);
                   // System.out.println(newobject.get("siteBackgroundColor"));
                    //System.out.println(newobject.get("siteText"));
                    //System.out.println(newobject.get("siteInfo"));
                    //System.out.println(newobject.get("siteBorderColor"));
                }
               // System.out.println(childjsonObject.get("id"));
                //System.out.println(childjsonObject.get("text"));

            }
            JsonArray jsonedgearray = jObject.get("edges").getAsJsonArray();
            for (int i=0;i< jsonedgearray.size();i++){
                //System.out.println(jsonedgearray.get(i).isJsonObject());
                JsonObject eachedgeobject = jsonedgearray.get(i).getAsJsonObject();
                //System.out.println(eachedgeobject.get("data").getAsJsonObject());
                JsonObject eachchildobject= eachedgeobject.get("data").getAsJsonObject();
                String source = eachchildobject.get("source").toString();
                String edgetype= eachchildobject.get("edgeType").toString();
                String target = eachchildobject.get("target").toString();
                bw.write(source+"\t"+edgetype+"\t"+target);
                bw.write("\n");
                //System.out.println(eachchildobject.get("tooltipText"));
                //System.out.println(eachchildobject.get("edgeType"));
                //System.out.println(eachchildobject.get("source"));
                //System.out.println(eachchildobject.get("pcLinks")); // It is an json array again have to run loop to get this item
                //System.out.println(eachchildobject.get("target"));
            }


            bw.close();
            String OS = System.getProperty("os.name").toLowerCase();
            String name = "";
            if (OS.indexOf("win") >= 0){
                name = jsonpath.substring(jsonpath.lastIndexOf("\\")+1, jsonpath.length());
            } else name = jsonpath.substring(jsonpath.lastIndexOf("/")+1, jsonpath.length());
            String modelID = name;
            //System.out.println("After file print");
            cyNetwork = CyNetworkUtils.readCyNetworkFromFile(cyServiceRegistrar, temp);
            cyNetwork.getRow(cyNetwork).set(CyNetwork.NAME, modelID);
            JProgressBar jProgressBar = legendPanel.getStatusBar();
            jProgressBar.setValue(100);
            JButton jButton = legendPanel.getSubmitbutton();
            jButton.setEnabled(true);
            //CyNetworkUtils.createViewAndRegister(cyServiceRegistrar, cyNetwork);

        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,"Loaded succesfully","Format File Upload",JOptionPane.INFORMATION_MESSAGE);
    }

    public CyNetwork getCyNetwork() {
        return cyNetwork;
    }
}
