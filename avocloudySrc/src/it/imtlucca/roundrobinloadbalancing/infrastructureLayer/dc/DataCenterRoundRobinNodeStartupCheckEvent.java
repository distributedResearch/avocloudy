package it.imtlucca.roundrobinloadbalancing.infrastructureLayer.dc;

import java.util.Properties;

import it.imtlucca.cloudyscience.DataCenterNode;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeBehavior;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeFeature;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeKnowledge;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodePolicy;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodeFeature;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodeKnowledge;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodePolicy;
import it.imtlucca.cloudyscience.infrastructureLayer.dc.AbstractDataCenterNodeStartupCheckEvent;
import it.imtlucca.roundrobinloadbalancing.autonomicLayer.AcaasRoundRobinNodeBehavior;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;

/**
 * The event that throw a check for the need of startup another Iaas Node with Round Robin model
 * 
 * @author Stefano Sebastio
 *
 */
public class DataCenterRoundRobinNodeStartupCheckEvent extends AbstractDataCenterNodeStartupCheckEvent {

	public DataCenterRoundRobinNodeStartupCheckEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
	}

	
	/**
	 * Provides the templates needed by the ACaaS layer node according the Round Robin model
	 * 
	 * @param v
	 * @return
	 */
	protected AcaasAgent specifyAcaasNode(DataCenterNode dc){

		
		AbstractAcaasNodeFeature feature = new AcaasNodeFeature();
		AbstractAcaasNodePolicy policy = new AcaasNodePolicy(dc.getIaasDCAgent().getAppCriteria().getMissRateTolerance(),dc.getIaasDCAgent().getAppCriteria().isAskingToVolunteer(),dc.getIaasDCAgent().getAppCriteria().getMaxNumOfAttempt());
		AbstractAcaasNodeKnowledge knowledge = new AcaasNodeKnowledge(feature);
		AbstractAcaasNodeBehavior behavior = new AcaasRoundRobinNodeBehavior(knowledge, policy);
		
		AcaasAgent acAgent = new AcaasAgent(behavior, knowledge, policy, feature, null);
		behavior.setReferringAgent(acAgent);
		
		//v.getIaasAgent().setAcAgent(acAgent);
		
		return acAgent;
	}

}
