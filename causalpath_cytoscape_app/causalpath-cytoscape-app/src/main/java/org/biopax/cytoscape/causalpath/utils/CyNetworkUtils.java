package org.biopax.cytoscape.causalpath.utils;

import java.io.*;
import java.util.Collection;
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
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;

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


    public static CyNetwork readCyNetworkFromFile(CyServiceRegistrar cyServiceRegistrar, File cyNetworkFile, SynchronousTaskManager synchronousTaskManager) {
        CyNetworkReader networkReader = cyServiceRegistrar.getService(CyNetworkReaderManager.class).getReader(cyNetworkFile.toURI(), cyNetworkFile.getName());
        CommandExecutor.execute(new TaskIterator(networkReader), cyServiceRegistrar,synchronousTaskManager);

        System.out.println(networkReader.getNetworks().length);
        return networkReader.getNetworks()[0];
    }


}



