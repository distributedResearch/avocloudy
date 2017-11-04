package it.imtlucca.cloudyscience.infrastructureLayer;

import it.imtlucca.cloudyscience.infrastructureLayer.dc.IaasDCAgent;

/**
 * Data Center version of the IaaS agent. 
 * It has a link with the underlying IaaS DC Agent
 * 
 * @author Stefano Sebastio
 *
 */
public class IaasAgentDataCenter extends IaasAgent {

	
	private IaasDCAgent dcAgent;
	
	public IaasAgentDataCenter(AbstractIaasNodeBehavior behavior,
			AbstractIaasNodeKnowledge knowledge, AbstractIaasNodePolicy policy,
			AbstractIaasNodeFeature feature, IaasDCAgent dc) {
		super(behavior, knowledge, policy, feature);
		
		this.dcAgent = dc;
	}

	public IaasDCAgent getDcAgent() {
		return dcAgent;
	}

	
}
