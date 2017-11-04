package it.imtlucca.cloudyscience.applicationLayer;

import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;

/**
 * Main agent for the managing of the application.
 * It can be extended for supporting workflow and bargaining with ACaaS nodes 
 * 
 * @author Stefano Sebastio
 *
 */
public class AppAgent {

	private AbstractAppNodeBehavior behavior = null;
	private AbstractAppNodeKnowledge knowledge = null;
	private AbstractAppNodePolicy policy = null;
	private AbstractAppNodeFeature feature = null;
	
	private AcaasAgent referringAgent = null;

	public AppAgent(AbstractAppNodeBehavior behavior,
			AbstractAppNodeKnowledge knowledge, AbstractAppNodePolicy policy,
			AbstractAppNodeFeature feature, AcaasAgent referringAgent) {
		super();
		this.behavior = behavior;
		this.knowledge = knowledge;
		this.policy = policy;
		this.feature = feature;
		this.referringAgent = referringAgent;
	}
	
	public AppAgent(AbstractAppNodeBehavior behavior,
			AbstractAppNodeKnowledge knowledge, AbstractAppNodePolicy policy,
			AbstractAppNodeFeature feature) {
		super();
		this.behavior = behavior;
		this.knowledge = knowledge;
		this.policy = policy;
		this.feature = feature;
	}

	public AbstractAppNodeBehavior getBehavior() {
		return behavior;
	}

	public AbstractAppNodeKnowledge getKnowledge() {
		return knowledge;
	}

	public AbstractAppNodePolicy getPolicy() {
		return policy;
	}

	public AbstractAppNodeFeature getFeature() {
		return feature;
	}

	public AcaasAgent getReferringAgent() {
		return referringAgent;
	}

	// Used when the App is associated to a ACaaS Node
	public void setReferringAgent(AcaasAgent referringAgent) {
		this.referringAgent = referringAgent;
	}
	
}
