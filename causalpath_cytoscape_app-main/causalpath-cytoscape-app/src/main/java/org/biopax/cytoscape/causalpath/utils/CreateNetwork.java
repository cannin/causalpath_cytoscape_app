package org.biopax.cytoscape.causalpath.utils;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class CreateNetwork extends AbstractTask {
    private  final CyNetworkFactory cyNetworkFactory;
    public CreateNetwork(CyNetworkFactory cyNetworkFactory){
     this.cyNetworkFactory = cyNetworkFactory;
    }
    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {
        CyNetwork myNet = this.cyNetworkFactory.createNetwork();

    }
}
