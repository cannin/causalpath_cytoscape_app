package org.cytoscape.sample.internal.cellnoptr.tasks;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.cytoscape.service.util.CyServiceRegistrar;

import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.text.ParseException;
import java.util.Iterator;

public class JsonImport {
    private static CyServiceRegistrar cyServiceRegistrar;
    private String jsonpath;
    private File Formatfile;
    public JsonImport(File selectedFile, String jsonpath, CyServiceRegistrar cyServiceRegistrar) throws IOException {
        this.cyServiceRegistrar = cyServiceRegistrar;
        this.jsonpath = jsonpath;
        this.Formatfile = Formatfile;
        writeJSON(Formatfile,jsonpath);
    }
    public void writeJSON(File Formatfile,String jsonpath)throws IOException

    {
        JsonParser jsonParser = new JsonParser();

        try (FileReader reader = new FileReader(jsonpath))
        {
            //Read JSON file
            JsonObject jObject = jsonParser.parse(reader).getAsJsonObject();
            System.out.println(jObject.get("nodes").isJsonArray());
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
                    System.out.println(newobject.get("siteBackgroundColor"));
                    System.out.println(newobject.get("siteText"));
                    System.out.println(newobject.get("siteInfo"));
                    System.out.println(newobject.get("siteBorderColor"));
                }
                System.out.println(childjsonObject.get("id"));
                System.out.println(childjsonObject.get("text"));

            }
            JsonArray jsonedgearray = jObject.get("edges").getAsJsonArray();
            for (int i=0;i< jsonedgearray.size();i++){
                //System.out.println(jsonedgearray.get(i).isJsonObject());
                JsonObject eachedgeobject = jsonedgearray.get(i).getAsJsonObject();
                //System.out.println(eachedgeobject.get("data").getAsJsonObject());
                JsonObject eachchildobject= eachedgeobject.get("data").getAsJsonObject();
                System.out.println(eachchildobject.get("tooltipText"));
                System.out.println(eachchildobject.get("edgeType"));
                System.out.println(eachchildobject.get("source"));
                System.out.println(eachchildobject.get("pcLinks")); // It is an json array again have to run loop to get this item
                System.out.println(eachchildobject.get("target"));
            }




        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,"Loaded succesfully","Format File Upload",JOptionPane.INFORMATION_MESSAGE);
    }

}
