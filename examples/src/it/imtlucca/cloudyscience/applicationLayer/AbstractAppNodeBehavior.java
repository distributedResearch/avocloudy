package it.imtlucca.cloudyscience.applicationLayer;

/**
 * Main Application Behavior
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractAppNodeBehavior {

	private AbstractAppNodeKnowledge knowledge;
	private AbstractAppNodePolicy policy;
	
	private AppAgent referringAgent;

	public AbstractAppNodeBehavior(AbstractAppNodeKnowledge knowledge,
			AbstractAppNodePolicy policy) {
		super();
		this.knowledge = knowledge;
		this.policy = policy;
	}

	public void setReferringAgent(AppAgent referringAgent) {
		this.referringAgent = referringAgent;
	}

	public AbstractAppNodeKnowledge getKnowledge() {
		return knowledge;
	}

	public AbstractAppNodePolicy getPolicy() {
		return policy;
	}

	public AppAgent getReferringAgent() {
		return referringAgent;
	}
	
	
	
	
}
