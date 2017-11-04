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
 * 
 * Abstract class to kill a node.
 * 
 * It also permits to execute a customized action on IaaS prior to leave the network
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractIaasNodeDeathEvent extends NodeEvent {

	public AbstractIaasNodeDeathEvent(String id, Properties params,
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
		//System.out.println("Death VT " + Engine.getDefault().getVirtualTime());
		if ( (getAssociatedNode() == null) &&  (!onlyForAssociatedNode) ){
			//System.out.println("death no associated node");
			//take a new node
			NodeList nodeList = new NodeList();
			ArrayList<VolunteerNode> nodes = nodeList.getOnlineVolunteerNodes();
			if (nodes.size() == 0)
				return;
			VolunteerNode node = nodes.get(Engine.getDefault().getSimulationRandom().nextInt(nodes.size()));
			
			if (this.checkNodeResiliency(node)){
			
				this.preDeathAction(node.getIaasAgent());
				((IaasNodeBehavior)node.getIaasAgent().getBehavior()).death();
				//System.out.println("randomly selected for death " + node + " at " + Engine.getDefault().getVirtualTime());
			}
		}
		else if (getAssociatedNode() instanceof VolunteerNode){
			//System.out.println("death associated...of " + getAssociatedNode()  + "  AAAAAAAA" + Engine.getDefault().getVirtualTime());
			VolunteerNode node = (VolunteerNode) getAssociatedNode();
			//System.out.println("death associated node " + node + " resiliency " + this.checkNodeResiliency(node));
			
			if (this.checkNodeResiliency(node)){
				this.preDeathAction(node.getIaasAgent());
				((IaasNodeBehavior) node.getIaasAgent().getBehavior()).death();
			}
		}
		

	}
	
	/**
	 * Selects according the node resiliency if a node will be shout down or not
	 * @return
	 */
	private boolean checkNodeResiliency(VolunteerNode n){
		
		int stability = n.getIaasAgent().getFeature().getStabilityFactor();
		int death = Engine.getDefault().getSimulationRandom().nextInt(100);
		
		if (death >= stability){
			return true; //death accepted
		}
		else 
			return false;
	}
	
	/**
	 * Template to execute an action on IaaS prior to leave the network 
	 * @param node
	 */
	protected void preDeathAction(IaasAgent node){
	}

}
