package it.imtlucca.cloudyscience.infrastructureLayer;

import it.imtlucca.cloudyscience.physicalLayer.Hardware;

/**
 * Volunteer version of the IaaS agent.
 * It has direct access to underlying Hardware resources
 * 
 * @author Stefano Sebastio
 *
 */
public class IaasAgentVolunteer extends IaasAgent {

	private Hardware hardwareResources;
	
	public IaasAgentVolunteer(AbstractIaasNodeBehavior behavior,
			AbstractIaasNodeKnowledge knowledge, AbstractIaasNodePolicy policy,
			AbstractIaasNodeFeature feature, Hardware hw) {
		super(behavior, knowledge, policy, feature);
		
		this.hardwareResources = hw;
	}


	public Hardware getHardwareResources() {
		return hardwareResources;
	}
	
	
}
