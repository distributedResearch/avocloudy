package it.imtlucca.cloudyscience.infrastructureLayer;

import java.util.ArrayList;
import java.util.Properties;

import it.imtlucca.cloudyscience.VolunteerNode;
import it.imtlucca.cloudyscience.util.NodeList;
import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.NodeEvent;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

/**
 * Abstract class to reconnect a node
 * 
 * The event that throw a join action of a IaaS on the Network
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractIaasNodeReconnectionEvent extends NodeEvent {

	
	public AbstractIaasNodeReconnectionEvent(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
	
	}

	/**
	 * Define if the event can be executed also for non associated nodes
	 */
	protected boolean onlyForAssociatedNode = false;
	
	
	public void run() throws RunException {
		if (getParentProcess() == null)
			throw new RunException("A parent process must be set in order to run " + getClass().getCanonicalName());
		
		//System.out.println("Reconnection VT " + Engine.getDefault().getVirtualTime());
		if ( (getAssociatedNode() == null) && (!onlyForAssociatedNode)){
			//System.out.println("reconnection no associate node");
			//take a new node
			NodeList nodeList = new NodeList();
			ArrayList<VolunteerNode> nodes = nodeList.getOfflineVolunteerNodes();
			if (nodes.size() == 0){
				return;
			}
			VolunteerNode node = nodes.get(Engine.getDefault().getSimulationRandom().nextInt(nodes.size()));
			((IaasNodeBehavior)node.getIaasAgent().getBehavior()).join();
			//System.out.println("randomly selected for join " + node + " at " + Engine.getDefault().getVirtualTime());
		}
		
		else if (getAssociatedNode() instanceof VolunteerNode){
			//System.out.println("reconnection associated....of " + getAssociatedNode() + " BBB  " + Engine.getDefault().getVirtualTime());
			VolunteerNode node = (VolunteerNode) getAssociatedNode();
			if (!node.getIaasAgent().getFeature().isOnline())
				((IaasNodeBehavior)node.getIaasAgent().getBehavior()).join();
		}
		//FIXME: qui come anche per gli eventi Death e Disonnect occorre considerare che possono essere presenti piu' nodi IaaS su uno stesso nodo DEUS (i.e., VolunteerNode)
	//	System.out.println("join Event called----");

	}

}
