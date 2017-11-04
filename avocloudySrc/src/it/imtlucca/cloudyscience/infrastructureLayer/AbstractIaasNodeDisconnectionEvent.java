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
 * Node disconnection.
 * It also permits to add a customized action on IaaS node, before leaving the network
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractIaasNodeDisconnectionEvent extends NodeEvent {

	
	public AbstractIaasNodeDisconnectionEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
	}

	/**
	 * Define if the event can be executed also for non associated nodes
	 */
	protected boolean onlyForAssociatedNode = false;
	
	public void run() throws RunException {

		if (getParentProcess() == null)
			throw new RunException("A parent process must be set in order to run " + getClass().getCanonicalName());
		
		//System.out.println("getAssociatedNode " + getAssociatedNode());
		//System.out.println("...." + Engine.getDefault().getVirtualTime());
		
		if ( (getAssociatedNode() == null) && (!onlyForAssociatedNode)){
			//take a new node
			NodeList nodeList = new NodeList();
			ArrayList<VolunteerNode> nodes = nodeList.getOnlineVolunteerNodes();
			if (nodes.size() == 0)
				return;
			VolunteerNode node = nodes.get(Engine.getDefault().getSimulationRandom().nextInt(nodes.size()));
			
			if (this.checkNodeResiliency(node)){
				this.preDisconnectionAction(node.getIaasAgent());
				((IaasNodeBehavior)node.getIaasAgent().getBehavior()).disconnect();
				this.postDisconnectionAction(node.getIaasAgent());
				//System.out.println("randomly selected for disconnection " + node + " at " + Engine.getDefault().getVirtualTime());
			}
		}
		else if (getAssociatedNode() instanceof VolunteerNode){
			//System.out.println("getAssociatedNode " + getAssociatedNode());
			VolunteerNode node = (VolunteerNode) getAssociatedNode();
			
			if (this.checkNodeResiliency(node)){
				this.preDisconnectionAction(node.getIaasAgent());
				((IaasNodeBehavior) node.getIaasAgent().getBehavior()).disconnect();
				this.postDisconnectionAction(node.getIaasAgent());
			}
		}

	}
	
	
	/**
	 * Selects according the node resiliency if a node will be shout down or not
	 * @return
	 */
	private boolean checkNodeResiliency(VolunteerNode n){
		
		int stability = n.getIaasAgent().getFeature().getStabilityFactor();
		int disconnection = Engine.getDefault().getSimulationRandom().nextInt(100);
		
		if (disconnection > stability){
			return true;
		}
		else 
			return false;
	}
	
	/**
	 * Action that must be done before start the disconnection event 
	 * @param node
	 */
	protected void preDisconnectionAction(IaasAgent node){
		
	}
	
	/**
	 * Action that must be done after the disconnection event.
	 * Erase all the application that were in charge and are not migrated
	 * @param node
	 */
	protected void postDisconnectionAction(IaasAgent node){
		node.getBehavior().eraseAcceptedApp();
	}

}
