package it.imtlucca.aco;

import java.util.Properties;

import it.imtlucca.aco.autonomicLayer.AcaasAcoNodeBehavior;
import it.imtlucca.aco.autonomicLayer.AcaasAcoNodeKnowledge;
import it.imtlucca.aco.autonomicLayer.AcaasAcoNodePolicy;
import it.imtlucca.cloudyscience.AbstractVolunteerNodeBirthEvent;
import it.imtlucca.cloudyscience.VolunteerNode;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeBehavior;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeFeature;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeKnowledge;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodePolicy;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodeFeature;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;

/**
 * The birth of a new Volunteer Node that acts according the ACO model
 * 
 * @author Stefano Sebastio
 *
 */
public class VolunteerAcoNodeBirthEvent extends AbstractVolunteerNodeBirthEvent {

	public VolunteerAcoNodeBirthEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
		
	}

	/**
	 * Provides the templates needed by the ACaaS layer node with the ACO model
	 * 
	 * @param v
	 * @return
	 */
	protected AcaasAgent specifyAcaasNode(VolunteerNode v){

		//System.out.println("call the trust..");
		AbstractAcaasNodeFeature feature = new AcaasNodeFeature();

		AcoVolunteerNode av = (AcoVolunteerNode) v;
		
		//System.out.println("getMissRateTolerance " + v.getIaasAgent().getAppCriteria().getMissRateTolerance());
		AbstractAcaasNodePolicy policy = new AcaasAcoNodePolicy( av.ttl, av.timeout, av.numOfAnts, av.evaporation, av.initPheromone, av.agingFunction, av.temperatureFunction );
		((AcaasAcoNodePolicy)policy).setAntWeight(av.weightCore, AntColor.CPU_CORE);
		((AcaasAcoNodePolicy)policy).setAntWeight(av.weightFreq, AntColor.CPU_FREQ);
		((AcaasAcoNodePolicy)policy).setAntWeight(av.weightRam, AntColor.MAIN_MEMORY);
		((AcaasAcoNodePolicy)policy).setWeights(av.resourceWeight, av.finishingTimeWeight, av.pheromoneWeight, av.linkQtWeight);

		AbstractAcaasNodeKnowledge knowledge = new AcaasAcoNodeKnowledge(feature);
		AbstractAcaasNodeBehavior behavior = new AcaasAcoNodeBehavior(knowledge, policy);
		
		AcaasAgent acAgent = new AcaasAgent(behavior, knowledge, policy, feature, v.getIaasAgent());
		behavior.setReferringAgent(acAgent);
		
		//v.getIaasAgent().setAcAgent(acAgent);
			
		return acAgent;
	}	
	
	/**
	 * Initialize the pheromone path for the ACO strategies
	 * 
	 */
	protected void postConnectionActions(VolunteerNode v){
		((AcaasAcoNodeBehavior) v.getIaasAgent().getAcAgent().getBehavior()).initializePheromonePath();
		
	}
}
