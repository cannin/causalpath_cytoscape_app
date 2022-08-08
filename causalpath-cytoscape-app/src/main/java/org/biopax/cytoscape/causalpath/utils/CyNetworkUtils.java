package org.biopax.cytoscape.causalpath.utils;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;


import org.apache.poi.ss.formula.functions.T;
import org.biopax.cytoscape.causalpath.ImportandExecutor.tasks.FormatFileImport;
import org.biopax.cytoscape.causalpath.ImportandExecutor.utils.CommandExecutor;
import org.biopax.cytoscape.causalpath.Panel.LegendPanel;
import org.biopax.cytoscape.causalpath.creatystyle.RGBValue;
import org.cytoscape.io.read.CyNetworkReader;
import org.cytoscape.io.read.CyNetworkReaderManager;
import org.cytoscape.model.*;

import org.biopax.cytoscape.causalpath.creatystyle.StyleCreate;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.session.CyNetworkNaming;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.SynchronousTaskManager;
import org.cytoscape.work.TaskIterator;

import org.cytoscape.work.Tunable;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import static org.biopax.cytoscape.causalpath.Panel.LegendPanel.LOGGER;

public class CyNetworkUtils {

    public static CyNetwork getCyNetwork(CyServiceRegistrar cyServiceRegistrar, String networkName) {
        Set<CyNetwork> networks = cyServiceRegistrar.getService(CyNetworkManager.class).getNetworkSet();

        for (CyNetwork network : networks)
            if (network.getRow(network).get(CyNetwork.NAME, String.class).equals(networkName))
                return network;

        return null;
    }

    public static CyNode getCyNode(CyNetwork cyNetwork, String nodeName) {
        for (CyNode node : cyNetwork.getNodeList())
            if (cyNetwork.getRow(node).get(CyNetwork.NAME, String.class).equals(nodeName))
                return node;

        return null;
    }

    public static CyEdge getCyEdge(CyNetwork cyNetwork, String edgeName) {
        for (CyEdge edge : cyNetwork.getEdgeList())
            if (cyNetwork.getRow(edge).get(CyNetwork.NAME, String.class).equals(edgeName))
                return edge;

        return null;
    }

    public static List<CyEdge> getCyEdges(CyServiceRegistrar cyServiceRegistrar, String cyNetworkName) {
        CyNetwork cyNetwork = getCyNetwork(cyServiceRegistrar, cyNetworkName);
        return cyNetwork.getEdgeList();
    }

    public static String getUniqueNetworkName(CyServiceRegistrar cyServiceRegistrar, String name) {
        String uniqueName = name;

        for (int i = 1; getCyNetwork(cyServiceRegistrar, uniqueName) != null; i++) {
            uniqueName = name + "_" + i;
        }

        return uniqueName;
    }

    public static void createViewAndRegister(CyServiceRegistrar cyServiceRegistrar, CyNetwork cyNetwork, FormatFileImport formatFileImport, LegendPanel legendPanel) throws IOException, TransformerException, ParserConfigurationException, SAXException {
        String networkName = cyNetwork.getRow(cyNetwork).get(CyNetwork.NAME, String.class);
        CyNetworkNaming cyNetworkNaming = cyServiceRegistrar.getService(CyNetworkNaming.class);
        cyNetwork.getDefaultNetworkTable().getRow(cyNetwork.getSUID())
                .set("name", cyNetworkNaming.getSuggestedNetworkTitle(networkName));
        // Register network

        cyServiceRegistrar.getService(CyNetworkManager.class).addNetwork(cyNetwork);


        // Create network view
        final Collection<CyNetworkView> views = cyServiceRegistrar.getService(CyNetworkViewManager.class).getNetworkViews(cyNetwork);

        CyNetworkView myView = null;
        if (views.size() != 0)
            myView = views.stream().iterator().next();

        if (myView == null) {
            // create a new view for my network

            myView = cyServiceRegistrar.getService(CyNetworkViewFactory.class).createNetworkView(cyNetwork);

            cyServiceRegistrar.getService(CyNetworkViewManager.class).addNetworkView(myView, true);


        } else {
            myView = cyServiceRegistrar.getService(CyNetworkViewFactory.class).createNetworkView(cyNetwork);
            cyServiceRegistrar.getService(CyNetworkViewManager.class).addNetworkView(myView, true);
            System.out.println("networkView already existed.");
        }

        // Apply visual style

        StyleCreate styleCreate = new StyleCreate(cyServiceRegistrar, formatFileImport, cyNetwork);
        styleCreate.createStyle(myView);

        networkName = cyNetwork.getRow(cyNetwork).get(CyNetwork.NAME, String.class);

        // Apply layout
        CommandExecutor.execute("layout force-directed network=\"" + networkName + "\"", cyServiceRegistrar, legendPanel);

    }

    public static void createViewAndRegister(CyServiceRegistrar cyServiceRegistrar, CyNetwork cyNetwork, LegendPanel legendPanel) throws IOException, TransformerException, ParserConfigurationException, SAXException {
        String networkName = cyNetwork.getRow(cyNetwork).get(CyNetwork.NAME, String.class);
        CyNetworkNaming cyNetworkNaming = cyServiceRegistrar.getService(CyNetworkNaming.class);
        cyNetwork.getDefaultNetworkTable().getRow(cyNetwork.getSUID())
                .set("name", cyNetworkNaming.getSuggestedNetworkTitle(networkName));
        // Register network
        cyServiceRegistrar.getService(CyNetworkManager.class).addNetwork(cyNetwork);

        // Create network view
        final Collection<CyNetworkView> views = cyServiceRegistrar.getService(CyNetworkViewManager.class).getNetworkViews(cyNetwork);

        CyNetworkView myView = null;
        if (views.size() != 0)
            myView = views.stream().iterator().next();

        if (myView == null) {
            // create a new view for my network

            myView = cyServiceRegistrar.getService(CyNetworkViewFactory.class).createNetworkView(cyNetwork);

            cyServiceRegistrar.getService(CyNetworkViewManager.class).addNetworkView(myView, true);


        } else {
            LOGGER.log(Level.INFO, "networkView already existed.");
        }


        StyleCreate styleCreate = new StyleCreate(cyServiceRegistrar, new RGBValue(255, 255, 255), cyNetwork);
        styleCreate.createStyle(myView);


        // Apply layout
        networkName = cyNetwork.getRow(cyNetwork).get(CyNetwork.NAME, String.class);

        CommandExecutor.execute("layout force-directed network=\"" + networkName + "\"", cyServiceRegistrar, legendPanel);

    }


    public static CyNetwork readCyNetworkFromFile(CyServiceRegistrar cyServiceRegistrar, File cyNetworkFile, SynchronousTaskManager synchronousTaskManager,CyNetwork cyNetwork) {
        CyNetworkReader networkReader = cyServiceRegistrar.getService(CyNetworkReaderManager.class).getReader(cyNetworkFile.toURI(), cyNetworkFile.getName());
        CommandExecutor.execute(new TaskIterator(networkReader), cyServiceRegistrar,synchronousTaskManager);
        CyNetworkFactory cyNetworkFactory = cyServiceRegistrar.getService(CyNetworkFactory.class);
        CyNetwork newNetwork;

        newNetwork =  networkReader.getNetworks()[0];
        List<CyNode> CyNodes = newNetwork.getNodeList();
        List<CyEdge> CyEdges = newNetwork.getEdgeList();
        HashMap<Long,Long> nodetonode = new HashMap<>();
        HashMap<Long,Long> edgetoedge = new HashMap<>();
        for(CyNode  cyNode: CyNodes) {
            CyNode node = cyNetwork.addNode();
//
//            System.out.println("new nodes"+node);
//
//            System.out.println(cyNode);

            nodetonode.put(cyNode.getSUID(),node.getSUID());
            //cyNetwork.getRow(node).set(CyNetwork.NAME,newNetwork.getRow(cyNode).get(CyNetwork.NAME,String.class),String.class);
           // cyNetwork.getDefaultNodeTable().getRow(node.getSUID()).set(CyNode.SUID,cyNode.getSUID());
            cyNetwork.getDefaultNodeTable().getRow(node.getSUID()).set("name",newNetwork.getRow(cyNode).get(CyNetwork.NAME, String.class));
//            System.out.println("New node"+cyNetwork.getRow(node).get(CyNetwork.NAME, String.class));
//            System.out.println("node"+newNetwork.getRow(cyNode).get(CyNetwork.NAME, String.class));
        }


        cyNetwork.getDefaultNodeTable();
        for(CyEdge cyEdge : CyEdges) {
            System.out.println(cyEdge);

            System.out.println("source " + cyNetwork.getNode(nodetonode.get(cyEdge.getSource().getSUID())));
            System.out.println("dest" + cyNetwork.getNode(nodetonode.get(cyEdge.getTarget().getSUID())));

            CyEdge edge = cyNetwork.addEdge(cyNetwork.getNode(nodetonode.get(cyEdge.getSource().getSUID())), cyNetwork.getNode(nodetonode.get(cyEdge.getTarget().getSUID())), false);
            System.out.println(edge.getSource().getSUID() + " " + edge.getTarget().getSUID());
            System.out.println("NewNode  Interaction " + cyNetwork.getDefaultEdgeTable().getRow(edge.getSUID()).get(CyEdge.INTERACTION, String.class));

            System.out.println("OLd Node  Interaction " + newNetwork.getDefaultEdgeTable().getRow(cyEdge.getSUID()).get(CyEdge.INTERACTION, String.class));

            System.out.println("OLd Node  Name " + newNetwork.getDefaultEdgeTable().getRow(cyEdge.getSUID()).get(CyNetwork.NAME, String.class));
            edgetoedge.put(edge.getSUID(),cyEdge.getSUID());
        }
        CyTable edgetable = cyNetwork.getDefaultEdgeTable();
        Collection<CyEdge> edgelist = cyNetwork.getEdgeList();
        if(edgetable.getColumns().contains("name"))
            System.out.println("name columns exist");
        if(edgetable.getColumns().contains(CyEdge.INTERACTION))
            System.out.println("Interaction columns exist");
        for (CyEdge edge : edgelist) {
            cyNetwork.getDefaultEdgeTable().getRow(edge.getSUID()).set(CyEdge.INTERACTION, newNetwork.getDefaultEdgeTable().getRow(edgetoedge.get(edge.getSUID())).get(CyEdge.INTERACTION, String.class));
            cyNetwork.getDefaultEdgeTable().getRow(edge.getSUID()).set("name", newNetwork.getDefaultEdgeTable().getRow(edgetoedge.get(edge.getSUID())).get("name", String.class));
        }

            System.out.println(networkReader.getNetworks().length);
         return cyNetwork;
    }


}



