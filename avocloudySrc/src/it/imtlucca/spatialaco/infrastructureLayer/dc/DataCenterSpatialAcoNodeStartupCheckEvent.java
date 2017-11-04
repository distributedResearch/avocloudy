package it.imtlucca.spatialaco.infrastructureLayer.dc;

import java.util.Properties;

import it.imtlucca.aco.AcoDataCenterNode;
import it.imtlucca.aco.AntColor;
import it.imtlucca.aco.autonomicLayer.AcaasAcoNodeBehavior;
import it.imtlucca.aco.autonomicLayer.AcaasAcoNodeKnowledge;
import it.imtlucca.aco.autonomicLayer.AcaasAcoNodePolicy;
import it.imtlucca.cloudyscience.DataCenterNode;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeBehavior;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeFeature;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeKnowledge;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodePolicy;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodeFeature;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;
import it.imtlucca.cloudyscience.infrastructureLayer.dc.AbstractDataCenterNodeStartupCheckEvent;
import it.imtlucca.spatialaco.autonomicLayer.AcaasSpatialAcoNodeBehavior;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;

/**
 * The event that throw a check for the need of startup another Iaas Node with ACO model
 * 
 * @author Stefano Sebastio
 *
 */
public class DataCenterSpatialAcoNodeStartupCheckEvent extends
		AbstractDataCenterNodeStartupCheckEvent {

	public DataCenterSpatialAcoNodeStartupCheckEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
		
	}

	/**
	 * Provides the templates needed by the ACaaS layer node according the ACO model
	 * 
	 * @param v
	 * @return
	 */
	protected AcaasAgent specifyAcaasNode(DataCenterNode dc){

		AcoDataCenterNode adc = (AcoDataCenterNode) dc;
		
		AbstractAcaasNodeFeature feature = new AcaasNodeFeature();
		AbstractAcaasNodePolicy policy = new AcaasAcoNodePolicy( adc.ttl, adc.timeout, adc.numOfAnts, adc.evaporation, adc.initPheromone, adc.agingFunction, adc.temperatureFunction );
		((AcaasAcoNodePolicy)policy).setAntWeight(adc.weightCore, AntColor.CPU_CORE);
		((AcaasAcoNodePolicy)policy).setAntWeight(adc.weightFreq, AntColor.CPU_FREQ);
		((AcaasAcoNodePolicy)policy).setAntWeight(adc.weightRam, AntColor.MAIN_MEMORY);
		((AcaasAcoNodePolicy)policy).setWeights(adc.resourceWeight, adc.finishingTimeWeight, adc.pheromoneWeight, adc.linkQtWeight);

		AbstractAcaasNodeKnowledge knowledge = new AcaasAcoNodeKnowledge(feature);
		AbstractAcaasNodeBehavior behavior = new AcaasSpatialAcoNodeBehavior(knowledge, policy);
		
		AcaasAgent acAgent = new AcaasAgent(behavior, knowledge, policy, feature, null);
		behavior.setReferringAgent(acAgent);
		
		//v.getIaasAgent().setAcAgent(acAgent);
		
		return acAgent;
	}
	
	
	/**
	 * Initialize the pheromone path for every neighbor
	 * 
	 * @param d
	 */
	protected void postDcStartup(DataCenterNode d){
		
		for (IaasAgent i : d.getIaasDCAgent().getIaasAgents()){
			((AcaasAcoNodeBehavior)i.getAcAgent().getBehavior()).initializePheromonePath();
		}
		
	}
}
