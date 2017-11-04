package it.imtlucca.cloudyscience.log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;
import it.imtlucca.cloudyscience.util.NodeList;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

/**
 * This event print the network topology in <p>Pajek</p> format
 * 
 * @author Stefano Sebastio
 *
 */

public class LogCloudyScienceForPajek extends Event {

	public LogCloudyScienceForPajek(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);

	} 
	
	
	public void run() throws RunException {
		System.out.println("Logging for Pajek...");
		getLogger().info("##### Network links:");
		//Collections.sort(Engine.getDefault().getNodes());
		//getLogger().info("*Vertices " + Engine.getDefault().getNodes().size());
		
		//ArrayList<IaasAgent> nodes = this.getIaasAgentList();
		NodeList nodesList = new NodeList();
		ArrayList<IaasAgent> nodes = nodesList.getIaasAgentList();
		
		
		getLogger().info("*Vertices " + nodes.size());
		ArrayList<Integer> peer = new ArrayList<Integer>();
		int k=1;
		//for(Iterator<Node> it = Engine.getDefault().getNodes().iterator(); it.hasNext(); k++){
		for(Iterator<IaasAgent> it = nodes.iterator(); it.hasNext(); k++){
			
			IaasAgent node = it.next();
				if (node.getFeature().isSupernode()){
					getLogger().info(k + " \"" + node.getFeature().getNodeId() + "\" ic Green bc Black");
					//System.out.println("Found a Supernode");
				} else{
					getLogger().info(k + " \"" + node.getFeature().getNodeId() + "\" ic Yellow bc Black");
				}
				
				peer.add(node.getFeature().getNodeId());	
		}
		
		
		//FIXME: correggere i link dei datacenter 
		
		getLogger().info("*Edges");
		//getLogger().info("*Arcs");
		
		for(Iterator<IaasAgent> it = nodes.iterator(); it.hasNext(); k++){
			
			IaasAgent node = it.next();
			
			//Print supernodes
			for (int i=0; i<node.getKnowledge().getSupernodes().size(); i++){
				if (node.getKnowledge().getFeature().isOnline() && node.getKnowledge().getSupernodes().get(i).getKnowledge().getFeature().isOnline()){
					//getLogger().info(q + " " + node.nodeId + " " + node.getNodeKnwoledge().getSupernodes().get(i).nodeId + " c Black");
					//getLogger().info(q + " " +node.nodeId+ "-" +(nodes.indexOf(node)+1) + " " +node.getNodeKnwoledge().getSupernodes().get(i).nodeId +"-"+(nodes.indexOf(node.getNodeKnwoledge().getSupernodes().get(i))+1) + " c Black");
					//getLogger().info(q + " " + (findIndexFromId(nodes, node.nodeId)+1) + " " + (findIndexFromId(nodes, node.getNodeKnwoledge().getSupernodes().get(i).nodeId)+1) + " c Black");
					getLogger().info((findIndexFromId(nodes, node.getFeature().getNodeId())+1) + " " + (findIndexFromId(nodes, node.getKnowledge().getSupernodes().get(i).getFeature().getNodeId())+1) + " 1 c Black");
					
					//q++;
				}
			}
			
			if (node.getKnowledge() != null){
				//Print all neighbors
				for (int i=0; i<node.getKnowledge().getNeighbors().size(); i++){
					//System.out.println("node " + node + " with id " + node.getFeature().getNodeId() + " nodeKnowledge " + node.getKnowledge().getNeighbors());
					if (node.getKnowledge().getFeature().isOnline() && node.getKnowledge().getNeighbors().get(i).getKnowledge().getFeature().isOnline()){
						
						if (node.getFeature().getZoneOfBelonging() == node.getKnowledge().getNeighbors().get(i).getFeature().getZoneOfBelonging())
							getLogger().info((findIndexFromId(nodes, node.getFeature().getNodeId())+1) + " " + (findIndexFromId(nodes, node.getKnowledge().getNeighbors().get(i).getFeature().getNodeId())+1) + " 1 c Red");
						else
							getLogger().info((findIndexFromId(nodes, node.getFeature().getNodeId())+1) + " " + (findIndexFromId(nodes, node.getKnowledge().getNeighbors().get(i).getFeature().getNodeId())+1) + " 1 c Brown");
						//q++;
					}
				}
		//}
			}
			
		}	

		
		System.out.println("Logging for Pajek completed.");
	}
	
	
	/**
	 * Obtains the index used in the Pajek node from Node id 
	 * 
	 * @param nodes
	 * @param id
	 * @return
	 */
	//FIXME: correggere perche' indexOf sull'array restituisce sempre lo stesso oggetto???
	private int findIndexFromId(ArrayList<IaasAgent> nodes, int id){
		int index = -1;
		for (int i=0; i<nodes.size();i++){
			if(nodes.get(i).getFeature().getNodeId()==id){
				index=i;
				break;
			}
		}
		if(index == -1){
			System.out.println(id + " not found on ");
		}
			
		return index;
	}
	
	
}
