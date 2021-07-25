package org.cytoscape.sample.casualpath.ImportandExecutor.tasks;

import org.cytoscape.service.util.CyServiceRegistrar;
import org.panda.causalpath.run.CausalityAnalysisSingleMethodInterface;

import java.io.File;
import java.io.IOException;

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




    }
}
