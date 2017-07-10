package it.imtlucca.cloudyscience;

import it.imtlucca.cloudyscience.infrastructureLayer.AbstractIaasNodeBehavior;
import it.imtlucca.cloudyscience.infrastructureLayer.AbstractIaasNodeFeature;
import it.imtlucca.cloudyscience.infrastructureLayer.AbstractIaasNodeKnowledge;
import it.imtlucca.cloudyscience.infrastructureLayer.AbstractIaasNodePolicy;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgentVolunteer;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasNodeBehavior;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasNodeFeature;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasNodeKnowledge;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasNodePolicy;
import it.imtlucca.cloudyscience.physicalLayer.DeviceType;
import it.imtlucca.cloudyscience.physicalLayer.PhysicalNodeFeature;
import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Resource;

import java.util.ArrayList;
import java.util.Properties;

/**
 * It represents a volunteer node that can require but also execute tasks 
 * 
 * @author Stefano Sebastio
 *
 */
public class VolunteerNode extends AbstractCloudyNode {

	
	final int minPhysRAM;
	private static final String MIN_PHYS_RAM = "minPhysRAM";
	final int maxPhysRAM;
	private static final String MAX_PHYS_RAM = "maxPhysRAM";
	
//	static int typeOfDevice;
	private static final String TYPE_OF_DEVICE = "typeOfDevice";
	private static final String VOLUNTEER_NODE = "volunteer";
	
	/** IaaS Layer Node**/
	//private IaasAgentVolunteer iaasAgent = null;
	private IaasAgent iaasAgent = null;

	
	/*** Virtual Layer ***/
	/*
	private static final String RANDOM = "random";
	
	// For Volunteer Node it is an optional parameter. If not set otherwise it is assumed to be 0 
	static int resourceToVM = -1;
	private static final String RESOURCE_TO_VM = "resourceToVM";
	
	static int nOfRegionalZone = -1;
	private static final String N_REGIONAL_ZONE = "nOfRegionalZone";
	
	
	static boolean randomZoneOfBelonging = false;
	private static final String ZONE_OF_BELONGING = "zoneOfBelonging";
	*/
	
	
	public VolunteerNode(String id, Properties params,
			ArrayList<Resource> resources) throws InvalidParamsException {
		super(id, params, resources);
		System.out.println("Volunteer Node constructor called...");
		if (params.getProperty(TYPE_OF_DEVICE) == null)
			throw new InvalidParamsException(TYPE_OF_DEVICE + " param is expected.");
		else {
			if (! params.getProperty(TYPE_OF_DEVICE).equals(VOLUNTEER_NODE))
				//typeOfDevice = DeviceType.DATACENTER;
			//else
				throw new InvalidParamsException(TYPE_OF_DEVICE + " must be a 'volunteer'.");
		}
		
		if (params.getProperty(MIN_PHYS_RAM) == null)
			throw new InvalidParamsException(MIN_PHYS_RAM + " param is expected.");
		try{
			minPhysRAM = Integer.parseInt(params.getProperty(MIN_PHYS_RAM));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(MIN_PHYS_RAM + " must be a valid int value.");
		}
		if (params.getProperty(MAX_PHYS_RAM) == null)
			throw new InvalidParamsException(MAX_PHYS_RAM + " param is expected.");
		try{
			maxPhysRAM = Integer.parseInt(params.getProperty(MAX_PHYS_RAM));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(MAX_PHYS_RAM + " must be a valid int value.");
		}		
		
		
	/*	if (params.getProperty(RESOURCE_TO_VM) != null){
			try{
				resourceToVM = Integer.parseInt(params.getProperty(RESOURCE_TO_VM));
			} catch (NumberFormatException ex) {
				throw new InvalidParamsException(RESOURCE_TO_VM + " must be a valid int value.");
			}
		}
		
		
		if (params.getProperty(N_REGIONAL_ZONE) != null) {
			try {
				nOfRegionalZone = Integer.parseInt(params.getProperty(N_REGIONAL_ZONE));
			} catch (NumberFormatException ex) {
				throw new InvalidParamsException(N_REGIONAL_ZONE + " must be a valid int value.");
			}
		}
		
		
		if (params.getProperty(ZONE_OF_BELONGING) != null) {
			if (! params.getProperty(ZONE_OF_BELONGING).equals(RANDOM))
				randomZoneOfBelonging = true;
		}*/
	}

	
	public Object clone() {
		VolunteerNode clone = (VolunteerNode) super.clone();
		//System.out.println("want to set " + typeOfDevice);
		((PhysicalNodeFeature)clone.getHw().getHwFeature()).setTypeOfDevice(DeviceType.VOLUNTEER, connection);
		
		if (minPhysRAM != -1 && maxPhysRAM != -1){
			int ram = minPhysRAM + (int)(Engine.getDefault().getSimulationRandom().nextDouble()*(maxPhysRAM - minPhysRAM+1));
			clone.getHw().getHwFeature().setMainMemory(ram);
		}
		
		
		// Create the structures needed to an IaaS Agent
		AbstractIaasNodePolicy policy = new IaasNodePolicy(clone.getNetCriteria().isRandomZoneAssignement(), clone.getNetCriteria().getnOfSupernodeToConnect(), clone.getNetCriteria().isRandomConnectionInit(), 
				clone.getNetCriteria().getnOfConnectionInit(), clone.getNetCriteria().getnOfConnectionOngoing(), clone.getNetCriteria().isConnectionAmongZonesInit(), 
				clone.getNetCriteria().isConnectionAmongZonesOngoing());
		
		if (!clone.getNetCriteria().isRandomZoneAssignement())
			policy.setZoneAssignement(clone.getNetCriteria().getZoneAssigned());
		
		
		
		int coreNumber = (int)(clone.getHw().getHwFeature().getCoreNumber()*clone.getVmCriteria().getQuoteOfPhysToVm()/100);
		int coreFreq = (int)(clone.getHw().getHwFeature().getCoreFreq()*clone.getVmCriteria().getQuoteOfPhysToVm()/100);
		int mainMemory = (int)(clone.getHw().getHwFeature().getMainMemory()*clone.getVmCriteria().getQuoteOfPhysToVm()/100);
		int nodeId = Engine.getDefault().generateResourceKey();
		AbstractIaasNodeFeature feature = new IaasNodeFeature(coreNumber, coreFreq, mainMemory, nodeId);
		feature.setStabilityFactor(clone.getVmCriteria().getStabilityFactor());
		AbstractIaasNodeKnowledge knowledge = new IaasNodeKnowledge(clone.getNetCriteria().getnOfRegionalZone(), feature);
		((IaasNodeKnowledge) knowledge).setOverheadForRemoteSite(clone.getNetCriteria().getOverheadForRemoteSite());
		AbstractIaasNodeBehavior behavior = new IaasNodeBehavior(knowledge, policy);
		
		clone.iaasAgent = new IaasAgentVolunteer(behavior, knowledge, policy, feature, clone.getHw());
		behavior.setReferringAgent(clone.iaasAgent);
		knowledge.setReferringAgent(clone.iaasAgent);
		System.out.println("coreNum " + coreNumber + ", coreFreq " + coreFreq + ", ram " + mainMemory + " for " + clone.iaasAgent.getFeature().getNodeId());
		clone.iaasAgent.setAppCriteria(clone.getAppCriteria());
		
		//clone.physicalFeature.setTypeOfDevice(DeviceType.VOLUNTEER);
	//	System.out.println("Volunteer " + clone.physicalFeature.getTypeOfDevice());
	//	System.out.println("coreNum " + clone.physicalFeature.getCoreNumber() + " at time " + Engine.getDefault().getVirtualTime());
	/*	
		if (resourceToVM != -1){
			clone.vmCreationPolicy = new VirtualMachCreationPolicy(resourceToVM, false);
		}
		
		if (nOfRegionalZone != -1){
			clone.vKnowledge = new VirtualKnowledge(nOfRegionalZone);
		}
		*/
		return clone;
	}


	public IaasAgent getIaasAgent() {
		return iaasAgent;
	}
	
	
	
}
