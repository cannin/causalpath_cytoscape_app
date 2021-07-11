package org.cytoscape.sample.internal.cellnoptr.tasks;

import org.cytoscape.service.util.CyServiceRegistrar;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FormatFileImport {
    private static CyServiceRegistrar cyServiceRegistrar;
    private String formatFile;
    private File Formatfile;
    public FormatFileImport(File Formatfile, String formatFile, CyServiceRegistrar cyServiceRegistrar) throws  IOException {

        this.cyServiceRegistrar = cyServiceRegistrar;
        this.formatFile = formatFile;
        this.Formatfile = Formatfile;
        writeFormat(Formatfile,formatFile);
    }

    private void writeFormat(File siffile, String fXmlFile) {
        try
        {


            FileInputStream fis=new FileInputStream(siffile);     //opens a connection to an actual file
            System.out.println("file content: ");
            int r=0;
            while((r=fis.read())!=-1)
            {
                System.out.print((char)r);      //prints the content of the file
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,"Loaded succesfully","Format File Upload",JOptionPane.INFORMATION_MESSAGE);
    }
}
