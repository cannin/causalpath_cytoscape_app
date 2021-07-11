package org.cytoscape.sample.internal.cellnoptr.tasks;

import org.cytoscape.service.util.CyServiceRegistrar;

import java.io.File;
import java.io.IOException;
import org.panda.causalpath.run.CausalPath;
public class TxtFileImport {
    private static CyServiceRegistrar cyServiceRegistrar;
    private String pathtofile;
    private File Txtfile;
    public TxtFileImport(File Txtfile, String pathtofile, CyServiceRegistrar cyServiceRegistrar) throws IOException, ClassNotFoundException {

        this.cyServiceRegistrar = cyServiceRegistrar;
        this.Txtfile = Txtfile;
        this.pathtofile = pathtofile;
        writeTxt(Txtfile,pathtofile);
    }
    public void writeTxt(File txtfile, String pathtofile) throws IOException{
        System.out.println(pathtofile);


            CausalPath causalPath = new CausalPath(pathtofile);
        try {
            causalPath.run();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
