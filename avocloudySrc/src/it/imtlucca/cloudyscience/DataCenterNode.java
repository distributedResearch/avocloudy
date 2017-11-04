package it.imtlucca.cloudyscience;

import it.imtlucca.cloudyscience.infrastructureLayer.dc.IaasDCAgent;
import it.imtlucca.cloudyscience.infrastructureLayer.dc.IaasDCNodeBehavior;
import it.imtlucca.cloudyscience.infrastructureLayer.dc.AbstractIaasDCNodeBehavior;
import it.imtlucca.cloudyscience.infrastructureLayer.dc.IaasDCNodeKnowledge;
import it.imtlucca.cloudyscience.infrastructureLayer.dc.AbstractIaasDCNodeKnowledge;
import it.imtlucca.cloudyscience.infrastructureLayer.dc.IaasDCNodePolicy;
import it.imtlucca.cloudyscience.infrastructureLayer.dc.AbstractIaasDCNodePolicy;
import it.imtlucca.cloudyscience.physicalLayer.DeviceType;
import it.imtlucca.cloudyscience.physicalLayer.PhysicalNodeFeature;
import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Resource;

import java.util.ArrayList;
import java.util.Properties;


/**
 * It represents a DataCenter node whose main aim is the execution works of other
 * 
 * @author Stefano Sebastio
 *
 */
public class DataCenterNode extends AbstractCloudyNode {

	
	static int minPhysRAM;
	private static final String MIN_PHYS_RAM = "minPhysRAM";
	static int maxPhysRAM;
	private static final String MAX_PHYS_RAM = "maxPhysRAM";
	
	//static int typeOfDevice;
	private static final String TYPE_OF_DEVICE = "typeOfDevice";
	private static final String DATACENTER_NODE = "datacenter"; 
	
	
	/*** IaaS Layer DataCenter ***/
	// It is the agent available on DC machines at Virtual layer
	IaasDCAgent iaasDCAgent = null;
	
	static double rateResourceToVMStartup;
	private static final String RATE_RESOURCE_TO_VM_STARTUP = "rateResourceToVMStartup";
	
	static double rateResourceToVMResize;
	private static final String RATE_RESOURCE_TO_VM_RESIZE = "rateResourceToVMResize";
	
	static boolean createResizableVM;
	private static final String CREATE_RESIZABLE_VM = "createResizableVM";
	
	static double triggerUtilToStart;
	private static final String TRIGGER_UTILIZATION_TO_START = "triggerUtilizationToStart";
	
	static double triggerUtilToShutdown;
	private static final String TRIGGER_UTILIZATION_TO_SHUTDOWN = "triggerUtilizationToShutdown";
	
	static double triggerUtilToResize;
	private static final String TRIGGER_UTILIZATION_TO_RESIZE = "triggerUtilizationToResize";
	
	
	
	/*** Virtual Layer ***/
/*	
	
	private static final String RANDOM = "random";
	
	
	static int resourceToVM;
	private static final String RESOURCE_TO_VM = "resourceToVM";
	
	static boolean resizable = false;
	private static final String VM_RESIZABLE = "vmResizable";
	
	static int nOfRegionalZone;
	private static final String N_REGIONAL_ZONE = "nOfRegionalZone";
	
	
	static boolean randomZoneOfBelonging = false;
	private static final String ZONE_OF_BELONGING = "zoneOfBelonging";
	*/
	
	
	
	
	public DataCenterNode(String id, Properties params,
			ArrayList<Resource> resources) throws InvalidParamsException {
		super(id, params, resources);
		System.out.println("DataCenter Node constructor called...");
		if (params.getProperty(TYPE_OF_DEVICE) == null)
			throw new InvalidParamsException(TYPE_OF_DEVICE + " param is expected.");
		else {
			if (! params.getProperty(TYPE_OF_DEVICE).equals(DATACENTER_NODE))
				//typeOfDevice = DeviceType.DATACENTER;
			//else
				throw new InvalidParamsException(TYPE_OF_DEVICE + " must be a 'datacenter'.");
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
		
		/*** IaaS Layer DataCenter ***/
		if (params.getProperty(RATE_RESOURCE_TO_VM_STARTUP) == null)
			throw new InvalidParamsException(RATE_RESOURCE_TO_VM_STARTUP + " param is expected.");
		try{
			rateResourceToVMStartup = Double.parseDouble(params.getProperty(RATE_RESOURCE_TO_VM_STARTUP));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(RATE_RESOURCE_TO_VM_STARTUP + " must be a double value.");
		}
		
		if (params.getProperty(RATE_RESOURCE_TO_VM_RESIZE) == null)
			throw new InvalidParamsException(RATE_RESOURCE_TO_VM_RESIZE + " param is expected.");
		try {
			rateResourceToVMResize = Double.parseDouble(params.getProperty(RATE_RESOURCE_TO_VM_RESIZE));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(RATE_RESOURCE_TO_VM_RESIZE + " must be a double.");
		}
		
		if (params.getProperty(CREATE_RESIZABLE_VM) == null)
			throw new InvalidParamsException(CREATE_RESIZABLE_VM + " param is expected");
		try {
			createResizableVM = Boolean.parseBoolean(params.getProperty(CREATE_RESIZABLE_VM));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(CREATE_RESIZABLE_VM + " must be a boolean.");
		}
		
		if (params.getProperty(TRIGGER_UTILIZATION_TO_START) == null)
			throw new InvalidParamsException(TRIGGER_UTILIZATION_TO_START + " param is expected");
		try {
			triggerUtilToStart = Double.parseDouble(params.getProperty(TRIGGER_UTILIZATION_TO_START));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(TRIGGER_UTILIZATION_TO_START + " must be a double.");
		}
		
		if (params.getProperty(TRIGGER_UTILIZATION_TO_SHUTDOWN) == null)
			throw new InvalidParamsException(TRIGGER_UTILIZATION_TO_SHUTDOWN + " param is expecgted");
		try {
			triggerUtilToShutdown = Double.parseDouble(params.getProperty(TRIGGER_UTILIZATION_TO_SHUTDOWN));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(TRIGGER_UTILIZATION_TO_SHUTDOWN + " must be a double.");
		}
		
		if (params.getProperty(TRIGGER_UTILIZATION_TO_RESIZE) == null)
			throw new InvalidParamsException(TRIGGER_UTILIZATION_TO_RESIZE + " param is expected");
		try {
			triggerUtilToResize = Double.parseDouble(params.getProperty(TRIGGER_UTILIZATION_TO_RESIZE));
		} catch (NumberFormatException ex) {
			throw  new InvalidParamsException(TRIGGER_UTILIZATION_TO_RESIZE + " must be a double.");
		}
		
		
		/*
		if (params.getProperty(RESOURCE_TO_VM) == null)
			throw new InvalidParamsException(RESOURCE_TO_VM + " param is expected.");
		try {
			resourceToVM = Integer.parseInt(params.getProperty(RESOURCE_TO_VM));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(RESOURCE_TO_VM + " must ba a valid int value.");
		}
		
		if (params.getProperty(VM_RESIZABLE) != null) {
			resizable = Boolean.parseBoolean(params.getProperty(VM_RESIZABLE));
		}
		
		
		
		if (params.getProperty(N_REGIONAL_ZONE) == null)
			throw new InvalidParamsException(N_REGIONAL_ZONE + " param is expected.");
		try {
			nOfRegionalZone = Integer.parseInt(params.getProperty(N_REGIONAL_ZONE));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(N_REGIONAL_ZONE + " must be a valid int value.");
		}
		
		if (params.getProperty(ZONE_OF_BELONGING) == null)
			throw new InvalidParamsException(ZONE_OF_BELONGING + " param is expected.");
		if (! params.getProperty(ZONE_OF_BELONGING).equals(RANDOM))
			randomZoneOfBelonging = true;
		*/
	}

	
	public Object clone() {
		DataCenterNode clone = (DataCenterNode) super.clone();
		//System.out.println("want to set " + typeOfDevice);
		((PhysicalNodeFeature)clone.getHw().getHwFeature()).setTypeOfDevice(DeviceType.DATACENTER, connection);
		int ram = minPhysRAM + (int)(Engine.getDefault().getSimulationRandom().nextDouble()*(maxPhysRAM - minPhysRAM +1 ));
		clone.getHw().getHwFeature().setMainMemory(ram);
		
		/*** IaaS Layer DataCenter ***/
		AbstractIaasDCNodePolicy policy = new IaasDCNodePolicy(rateResourceToVMStartup, rateResourceToVMResize, createResizableVM, triggerUtilToStart, triggerUtilToShutdown, triggerUtilToResize);
		AbstractIaasDCNodeKnowledge knowledge = new IaasDCNodeKnowledge();
		AbstractIaasDCNodeBehavior behavior = new IaasDCNodeBehavior(knowledge, policy);
	
		
		
		clone.iaasDCAgent = new IaasDCAgent(behavior, knowledge, policy);
		clone.iaasDCAgent.addHws(clone.getHw());
		
		behavior.setAgent(clone.iaasDCAgent);
		
		clone.iaasDCAgent.setNetCriteria(clone.getNetCriteria());
		clone.iaasDCAgent.setVmCriteria(clone.getVmCriteria());
		
		clone.iaasDCAgent.setAppCriteria(clone.getAppCriteria());
		
		/*
		clone.vmCreationPolicy = new VirtualMachCreationPolicy(resourceToVM, resizable);
		
		clone.vKnowledge = new VirtualKnowledge(nOfRegionalZone);
		*/
		//System.out.println("Created a " + clone.getHw().getHwFeature().getTypeOfDevice() + " with id " + clone.getHw().getHwFeature().getMachineId());
		
		return clone;
	}


	public IaasDCAgent getIaasDCAgent() {
		return iaasDCAgent;
	}
	
	
	
}
