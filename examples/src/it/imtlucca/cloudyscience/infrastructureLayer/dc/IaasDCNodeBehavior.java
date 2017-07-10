package it.imtlucca.cloudyscience.infrastructureLayer.dc;

import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;

import java.util.ArrayList;

/**
 * Simple implementations of a Data Center Behavior 
 * 
 * @author Stefano Sebastio
 *
 */
public class IaasDCNodeBehavior extends AbstractIaasDCNodeBehavior {

	

	public IaasDCNodeBehavior(AbstractIaasDCNodeKnowledge knwoledge,
			AbstractIaasDCNodePolicy policy, IaasDCAgent agent) {
		super(knwoledge, policy, agent);
		
	}
	
	public IaasDCNodeBehavior(AbstractIaasDCNodeKnowledge knowledge,
			AbstractIaasDCNodePolicy policy){
		super(knowledge, policy);
	}
	
	
	/**
	 * Receives the current set of IaaS Nodes that are currently on execution
	 */
	public void manageStartup(ArrayList<IaasAgent> nodes) {
		super.manageStartup(nodes);
		
		//TODO: ask agent to update the Load Record
		//System.out.println("Manage startup ");
		//TODO: evaluate the need of acts according some criteria
	}


	public void manageShutdown(ArrayList<IaasAgent> nodes) {
		// TODO Auto-generated method stub

	}
	
	public void manageResize(ArrayList<IaasAgent> nodes) {
		// TODO Auto-generated method stub

	}

}
