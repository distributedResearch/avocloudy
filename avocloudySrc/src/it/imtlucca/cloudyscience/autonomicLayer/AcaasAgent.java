package it.imtlucca.cloudyscience.autonomicLayer;

import it.imtlucca.cloudyscience.applicationLayer.AppAgent;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;
import it.unipr.ce.dsg.deus.core.Engine;

/**
 * ACaaS agent for the Autonomic Computing provided as an application execution service
 * 
 * @author Stefano Sebastio
 *
 */
public class AcaasAgent {
	
	private AbstractAcaasNodeBehavior behavior = null;
	private AbstractAcaasNodeKnowledge knowledge = null;
	private AbstractAcaasNodePolicy policy = null;
	private AbstractAcaasNodeFeature feature = null;
	
	private IaasAgent referringAgent = null;
	
	private int acaasId;

	public AcaasAgent(AbstractAcaasNodeBehavior behavior,
			AbstractAcaasNodeKnowledge knowledge,
			AbstractAcaasNodePolicy policy, AbstractAcaasNodeFeature feature,
			IaasAgent referringAgent) {
		super();
		this.behavior = behavior;
		this.knowledge = knowledge;
		this.policy = policy;
		this.feature = feature;
		this.referringAgent = referringAgent;
		
		this.acaasId = Engine.getDefault().generateResourceKey();
	}

	public AbstractAcaasNodeBehavior getBehavior() {
		return behavior;
	}

	public AbstractAcaasNodeKnowledge getKnowledge() {
		return knowledge;
	}

	public AbstractAcaasNodePolicy getPolicy() {
		return policy;
	}

	public AbstractAcaasNodeFeature getFeature() {
		return feature;
	}

	public IaasAgent getReferringAgent() {
		return referringAgent;
	}
	
	
	public void setReferringAgent(IaasAgent referringAgent) {
		this.referringAgent = referringAgent;
	}

	public int getAcaasId() {
		return acaasId;
	}

	/**
	 * Defines an Application that must be supported by the ACaaS node
	 * 
	 * @param app
	 */
	public void setNewApp(AppAgent app){
		app.getFeature().getStatus().taskAdmitted();
		app.setReferringAgent(this);
		this.behavior.manageApplication(app);
	}
	
	
	/**
	 * If it is online it responds with its finishing estimation time (for application that are just in charge). Otherwise (if it is offline) returns a null
	 * 
	 * @return
	 */
	public Float askFinishEstimation(){
		
		if (this.referringAgent.getFeature().isOnline())
			return this.getFeature().getFinishEstimation();
		else
			return null;
	}
	
	/**
	 * Receive the notification from the IaasAgent that a new IaasAgent is added to the overlay network neighborhood 
	 * @param a
	 */
	//TODO: forse conviene separare da nodo aggiunto ad eliminato. o supernode etc.
	public void overlayAddNeighborNotification(IaasAgent a){
		this.behavior.newNeighborOnOverlay(a);
	}
	
	public void overlayAddNodeNotification(IaasAgent a){
		this.behavior.newNodeOnOverlay(a);
	}
}
