package  org.cytoscape.sample.internal.utils;

import java.io.File;
import java.util.List;
import java.util.Set;


import org.cytoscape.io.read.CyNetworkReader;
import org.cytoscape.io.read.CyNetworkReaderManager;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.TaskIterator;

import  org.cytoscape.sample.internal.CyActivator;
import  org.cytoscape.sample.internal.cellnoptr.utils.CommandExecutor;

public class CyNetworkUtils {

	public static CyNetwork getCyNetwork (CyServiceRegistrar cyServiceRegistrar, String networkName) {
		Set<CyNetwork> networks = cyServiceRegistrar.getService(CyNetworkManager.class).getNetworkSet();
		
		for (CyNetwork network : networks)
			if (network.getRow(network).get(CyNetwork.NAME, String.class).equals(networkName))
				return network; 
		
		return null;
	}
	
	public static CyNode getCyNode (CyNetwork cyNetwork, String nodeName) {
		for (CyNode node : cyNetwork.getNodeList())
			if (cyNetwork.getRow(node).get(CyNetwork.NAME, String.class).equals(nodeName))
				return node;
				
		return null;
	}
	
	public static CyEdge getCyEdge (CyNetwork cyNetwork, String edgeName) {
		for (CyEdge edge : cyNetwork.getEdgeList())
			if (cyNetwork.getRow(edge).get(CyNetwork.NAME, String.class).equals(edgeName))
				return edge;
		
		return null;
	}

	public static List<CyEdge> getCyEdges (CyServiceRegistrar cyServiceRegistrar, String cyNetworkName) {
		CyNetwork cyNetwork = getCyNetwork(cyServiceRegistrar, cyNetworkName);
		return cyNetwork.getEdgeList();
	}
	
	public static String getUniqueNetworkName (CyServiceRegistrar cyServiceRegistrar, String name) {
		String uniqueName = name;
		
		for (int i = 1; getCyNetwork(cyServiceRegistrar, uniqueName) != null; i++) {
			uniqueName = name + "_" + i;
		}
		
		return uniqueName;
	}
	
	public static void createViewAndRegister (CyServiceRegistrar cyServiceRegistrar, CyNetwork cyNetwork) {
		String networkName = cyNetwork.getRow(cyNetwork).get(CyNetwork.NAME, String.class);
		
		// Register network
		cyServiceRegistrar.getService(CyNetworkManager.class).addNetwork(cyNetwork);
		
		// Create network view
		CyNetworkView cyNetworkView = cyServiceRegistrar.getService(CyNetworkViewFactory.class).createNetworkView(cyNetwork);
		cyServiceRegistrar.getService(CyNetworkViewManager.class).addNetworkView(cyNetworkView);
		
		// Apply visual style
		CommandExecutor.execute("vizmap apply styles=\"" + CyActivator.visualStyleName + "\"", cyServiceRegistrar);
		
		// Apply layout
		CommandExecutor.execute("layout grid network=\"" + networkName + "\"", cyServiceRegistrar);
	}
	
	public static CyNetwork readCyNetworkFromFile (CyServiceRegistrar cyServiceRegistrar, File cyNetworkFile) {
		CyNetworkReader networkReader = cyServiceRegistrar.getService(CyNetworkReaderManager.class).getReader(cyNetworkFile.toURI(), cyNetworkFile.getName());
		CommandExecutor.execute(new TaskIterator(networkReader), cyServiceRegistrar);
		return networkReader.getNetworks()[0];
	}
	
}
