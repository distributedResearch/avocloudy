package it.imtlucca.roundrobinloadbalancing.autonomicLayer;

import java.util.ArrayList;
import java.util.List;

import it.imtlucca.cloudyscience.applicationLayer.AppAgent;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeKnowledge;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodePolicy;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodeBehavior;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodePolicy;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;

/**
 * 
 * ACaaS layer according the Round Robin model load balancing
 * 
 * @author Stefano Sebastio
 *
 */
public class AcaasRoundRobinNodeBehavior extends AcaasNodeBehavior {

	//private ArrayList<IaasAgent> availableNodes;
	private List<IaasAgent> availableNodes;
	private int round = 0;
	
	public AcaasRoundRobinNodeBehavior(AbstractAcaasNodeKnowledge knwoledge,
			AbstractAcaasNodePolicy policy) {
		super(knwoledge, policy);
		
		this.availableNodes = null;
	}


	
	/**
	 * Manage application according the Round Robin model.
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
			this.availableNodes = new ArrayList<IaasAgent>(this.getReferringAgent().getReferringAgent().getBehavior().getAllKnownNodes());

		}
	
		//int numberOfAttempts = ((AcaasNodePolicy)this.getPolicy()).getMaxNumOfAttempt();
			
		//System.out.println("first " + this.availableNodes);
		/*if (this.availableNodes.size() > numberOfAttempts)
			nodeFound = this.askExecutionToNodeList(this.availableNodes.subList(this.round, (numberOfAttempts-1)), a);
		else*/ 
			nodeFound = this.askExecutionToNodeList(this.availableNodes, a);

		return nodeFound;
	}
	
	/**
	 * Ask to a node list if there is someone that can execute the application 
	 * 
	 * @param nodes
	 * @return
	 */
	protected boolean askExecutionToNodeList(List<IaasAgent> nodes, AppAgent a){
		boolean nodeFound = false;

		//first of all ask to itself
		nodeFound = ((AcaasNodeBehavior)this.getReferringAgent().getReferringAgent().getAcAgent().getBehavior()).appExecReq(a, this.getReferringAgent());
		a.getKnowledge().incReqCounter();
		if (nodeFound)
			return nodeFound;

		//to avoid multiple requests to the same node
		int initialRound = this.round; 
		
		while ( a.getKnowledge().getReqCounter() < ( (AcaasNodePolicy)this.getPolicy()).getMaxNumOfAttempt()) {
			IaasAgent n = nodes.get(this.round);
			this.round = (this.round+1)%nodes.size();
			Float resp = this.requestFinishEstimation(n.getAcAgent());
			if (resp != null) {
				nodeFound = ((AcaasNodeBehavior)n.getAcAgent().getBehavior()).appExecReq(a, this.getReferringAgent());
				
				a.getKnowledge().incReqCounter();
				
				if (nodeFound)
					return nodeFound;
			}
			
			//if a cycle on the list is already performed exit from the requests attempts
			if (this.round == initialRound)
				return false;
			
			
		}
				
		return nodeFound;
	}
	
	
	/**
	 * When a new node is added to the overlay network add also the node to the list of available for remote execution
	 */
	public void newNeighborOnOverlay(IaasAgent a){
		if (this.availableNodes != null)
			this.availableNodes.add(a);
	}
	
	public void newNodeOnOverlay(IaasAgent a){
		if (this.availableNodes != null)
			this.availableNodes.add(a);
	}
	
}
