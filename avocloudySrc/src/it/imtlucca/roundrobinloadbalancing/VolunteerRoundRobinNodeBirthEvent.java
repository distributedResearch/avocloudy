package it.imtlucca.roundrobinloadbalancing;

import java.util.Properties;

import it.imtlucca.cloudyscience.AbstractVolunteerNodeBirthEvent;
import it.imtlucca.cloudyscience.VolunteerNode;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeBehavior;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeFeature;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeKnowledge;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodePolicy;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodeFeature;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodeKnowledge;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodePolicy;
import it.imtlucca.roundrobinloadbalancing.autonomicLayer.AcaasRoundRobinNodeBehavior;

import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;

/**
 * The birth of a new Volunteer Node that acts according the Round Robin model
 * 
 * @author Stefano Sebastio
 *
 */
public class VolunteerRoundRobinNodeBirthEvent extends AbstractVolunteerNodeBirthEvent {

	
	public VolunteerRoundRobinNodeBirthEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
	}


	
	/**
	 * Provides the templates needed by the ACaaS layer node according the Round Robin model
	 * 
	 * @param v
	 * @return
	 */
	protected AcaasAgent specifyAcaasNode(VolunteerNode v){

		//System.out.println("RoundRobin model...");
		AbstractAcaasNodeFeature feature = new AcaasNodeFeature();
		//System.out.println("getMissRateTolerance " + v.getIaasAgent().getAppCriteria().getMissRateTolerance());
		AbstractAcaasNodePolicy policy = new AcaasNodePolicy(v.getIaasAgent().getAppCriteria().getMissRateTolerance(),v.getIaasAgent().getAppCriteria().isAskingToVolunteer(), v.getIaasAgent().getAppCriteria().getMaxNumOfAttempt());
		AbstractAcaasNodeKnowledge knowledge = new AcaasNodeKnowledge(feature);
		AbstractAcaasNodeBehavior behavior = new AcaasRoundRobinNodeBehavior(knowledge, policy);
		
		AcaasAgent acAgent = new AcaasAgent(behavior, knowledge, policy, feature, v.getIaasAgent());
		behavior.setReferringAgent(acAgent);
		
		//v.getIaasAgent().setAcAgent(acAgent);
		
		return acAgent;
	}
}
