package it.imtlucca.cloudyscience.autonomicLayer;

import it.imtlucca.cloudyscience.applicationLayer.AppAgent;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;

/**
 * Structure for managing the ACaaS node.
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractAcaasNodeBehavior {

	private AbstractAcaasNodeKnowledge knowledge;
	private AbstractAcaasNodePolicy policy;
	
	private AcaasAgent referringAgent;

	public AbstractAcaasNodeBehavior(AbstractAcaasNodeKnowledge knowledge,
			AbstractAcaasNodePolicy policy) {
		super();
		this.knowledge = knowledge;
		this.policy = policy;
	}

	public AbstractAcaasNodeBehavior(AbstractAcaasNodeKnowledge knwoledge,
			AbstractAcaasNodePolicy policy, AcaasAgent referringAgent) {
		super();
		this.knowledge = knwoledge;
		this.policy = policy;
		this.referringAgent = referringAgent;
	}

	public AbstractAcaasNodeKnowledge getKnowledge() {
		return knowledge;
	}

	public AbstractAcaasNodePolicy getPolicy() {
		return policy;
	}

	public AcaasAgent getReferringAgent() {
		return referringAgent;
	}

	public void setKnowledge(AbstractAcaasNodeKnowledge knwoledge) {
		this.knowledge = knwoledge;
	}

	public void setPolicy(AbstractAcaasNodePolicy policy) {
		this.policy = policy;
	}

	public void setReferringAgent(AcaasAgent referringAgent) {
		this.referringAgent = referringAgent;
	}
	
	
	public void manageApplication(AppAgent app) {
		
	}
	
	public void appExecReq(AppAgent app){
		System.out.println("MANAGE APPLICATION");
		
	}
	
	
	public void newNeighborOnOverlay(IaasAgent a){
		
	}
	
	public void newNodeOnOverlay(IaasAgent a){
		
	}
	
	/**
	 * Notify the result of the application execution end
	 * @param result
	 */
	public void notifyAppExecutionEnd(boolean result){
		
	}
}
