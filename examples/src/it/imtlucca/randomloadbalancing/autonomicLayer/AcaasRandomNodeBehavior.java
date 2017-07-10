package it.imtlucca.randomloadbalancing.autonomicLayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.imtlucca.cloudyscience.applicationLayer.AppAgent;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeKnowledge;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodePolicy;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodeBehavior;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;
import it.unipr.ce.dsg.deus.core.Engine;

/**
 * 
 * ACaaS layer according the Random model load balancing
 * 
 * @author Stefano Sebastio
 *
 */
public class AcaasRandomNodeBehavior extends AcaasNodeBehavior {

	//private ArrayList<IaasAgent> availableNodes;
	private List<IaasAgent> availableNodes;
	
	public AcaasRandomNodeBehavior(AbstractAcaasNodeKnowledge knwoledge,
			AbstractAcaasNodePolicy policy) {
		super(knwoledge, policy);
		
		this.availableNodes = null;
	}


	
	/**
	 * Manage application according the Random model.
	 * 
	 */
	public void manageApplication(AppAgent app){
		//System.out.println("currentTime " + Engine.getDefault().getVirtualTime());
		this.updateRequestCounter(app);
		
		if (this.searchExecutingNode(app))
			return;
		else{
			this.updateMissCounter(app);
		}
	}
	
	/**
	 * Shuffle the list of available nodes (neighbors plus supernode and itself) at each request
	 */
	public boolean searchExecutingNode(AppAgent a){
		
		boolean nodeFound = false;
		
		if (this.availableNodes == null){
			//Construct the list of available nodes:
			//1) get the list of neighbors
			this.availableNodes = new ArrayList<IaasAgent>(getReferringAgent().getReferringAgent().getKnowledge().getNeighbors());
			
			//2) add the supernode
			this.availableNodes.add(this.getReferringAgent().getReferringAgent().getKnowledge().getSupernodeResponsible());
			//3) add itself
			this.availableNodes.add(this.getReferringAgent().getReferringAgent());
		
			int nOfNodeToContact = this.getReferringAgent().getReferringAgent().getPolicy().getnConnectionInit();
			if ( (nOfNodeToContact != -1) && (nOfNodeToContact < this.availableNodes.size()) ){
				this.availableNodes = new ArrayList<IaasAgent>(this.availableNodes.subList(0, nOfNodeToContact)); 
					//	(ArrayList<IaasAgent>) this.availableNodes.subList(0, nOfNodeToContact);
			}
			
		}
		//FIXME: numero di tentativi forzato !!!
		int numberOfAttempts = 9;
		//while (!nodeFound && numberOfAttempts > 0){
			//System.out.println("shuffle the list");
			//System.out.println("before " + this.availableNodes);
			Collections.shuffle(this.availableNodes, Engine.getDefault().getSimulationRandom());
			//System.out.println("first " + this.availableNodes);
			if (this.availableNodes.size() > (numberOfAttempts-1))
				nodeFound = this.askExecutionToNodeList(this.availableNodes.subList(0, (numberOfAttempts-1)), a);
			else 
				nodeFound = this.askExecutionToNodeList(this.availableNodes, a);
			//numberOfAttempts--;
		//}
		return nodeFound;
	}
	
}
