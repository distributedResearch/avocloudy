package it.imtlucca.cloudyscience;

import java.util.ArrayList;
import java.util.Properties;

import it.imtlucca.cloudyscience.autonomicLayer.AppCriteria;
import it.imtlucca.cloudyscience.infrastructureLayer.NetworkCriteria;
import it.imtlucca.cloudyscience.infrastructureLayer.VmCriteria;
import it.imtlucca.cloudyscience.physicalLayer.AbstractConnection;
import it.imtlucca.cloudyscience.physicalLayer.Glan;
import it.imtlucca.cloudyscience.physicalLayer.Hardware;
import it.imtlucca.cloudyscience.physicalLayer.Lan;
import it.imtlucca.cloudyscience.physicalLayer.PhysicalNodeFeature;
import it.imtlucca.cloudyscience.physicalLayer.AbstractPysicalNodeFeature;
import it.imtlucca.cloudyscience.physicalLayer.TenGlan;
import it.imtlucca.cloudyscience.physicalLayer.WiFiG;
import it.imtlucca.cloudyscience.physicalLayer.WiFiN;
import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Resource;
import it.unipr.ce.dsg.deus.p2p.node.Peer;

/**
 * Minimum requirements for the specification of any type of 'Working' Node
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractCloudyNode extends Peer {

	// Standard Version for the description of the Physical Feature of a Node
	//AbstractPysicalNodeFeature physicalFeature = null;
	private Hardware hw = null;
	

	final int minPhysCore;
	private static final String MIN_PHYS_CORE = "minPhysCore";
	final int maxPhysCore;
	private static final String MAX_PHYS_CORE = "maxPhysCore";
	final int minPhysCoreFreq;
	private static final String MIN_PHYS_CORE_FREQ = "minPhysCoreFreq";
	final int maxPhysCoreFreq;
	private static final String MAX_PHYS_CORE_FREQ = "maxPhysCoreFreq";
	
	private static final String LAN_NAME = "lanName";
	private static final String LAN_NAME_AS_ZONE = "asZone";
	private int lanId = -1;
	private boolean lanNameAsZone = false;

	private static final String CONNECTION = "connection";
	
	private static final String WIFI_G = "WiFiG";
	private static final String WIFI_N = "WiFiN";
	private static final String LAN = "Lan";
	private static final String G_LAN = "GLan";
	private static final String TEN_G_LAN = "10GLan";
	
	public static int unit = 100; 
	private static final String UNIT = "unit";
	private static final String SEC = "sec";
	private static final String M_SEC = "msec";
	private static final String U_SEC = "usec";
	private static final String N_SEC = "nsec";
	
	//private float dataRate = -1;
	protected AbstractConnection connection = null;
	
	
	/** IaaS Layer Node**/
/*	final boolean volunteer;
	private static final String VOLUNTEER_APPROACH = "volunteerApproach";
	*/
	final double resourceToVM;
	private static final String RESOURCE_TO_VM = "resourceToVM";
	
/*	final boolean vmResizable;
	private static final String VM_RESIZABLE = "vmResizable";
*/	
	private static final String RANDOM = "random";
	private static final String ALL = "all";
	
	private boolean randomZoneAssignment;
	private static final String ZONE_ASSIGNMENT = "zoneAssignment";
	
	final int nOfRegionalZone;
	private static final String N_OF_REGIONAL_ZONE = "nOfRegionalZone";
	
	final int nOfSupernodeToConnect;
	private static final String CONNECT_TO_N_SUPERNODE = "connectToNSupernode";
	
	final boolean randomInitConnection;
	private static final String CONNECTION_CRITERIA_INIT = "connectionCriteriaInit";
	
	final int nOfConnectionInit;
	private static final String N_CONNECTION_INIT = "nConnectionInit";
	
	final int nOfConnectionOngoing;
	private static final String N_CONNECTION_ONGOING = "nConnectionOngoing";
	
	final boolean connectionAmongZonesInit;
	private static final String CONNECTION_AMONG_ZONES_INIT = "connectionAmongZonesInit";
	
	final boolean connectionAmongZonesOngoing;
	private static final String CONNECTION_AMONG_ZONES_ONGOING = "connectionAmongZonesOngoing";
	
	private VmCriteria vmCriteria;
	private NetworkCriteria netCriteria;
	
	final int overheadForRemoteSite;
	private static final String OVERHEAD_FOR_REMOTE_SITE = "overheadForRemoteSite";
	
	/*
	 * Optional parameter to specify the resilience to death and disconnection actions.
	 * If set to 100 (max value) death and disconnection doesn't affect the node.
	 * The default value is 0, then all death and disconnection will succeed
	 */
	int stabilityFactor = 0;
	private static final String STABILITY_FACTOR = "stabilityFactor";
	
	/** Autonomic Computing Layer  **/
	final boolean partialVolunteer;
	private static final String PARTIAL_VOLUNTEER = "partialVolunteer";
	
	final double missRateTolerance;
	private static final String MISS_RATE_TOLERANCE = "missRateTolerance";
	
	final boolean askToVolunteer;
	private static final String ASK_TO_VOLUNTEER = "askToVolunteer";
	
	final int maxNumOfAttempts;
	private static final String MAX_NUM_OF_ATTEMPTS = "maxNumOfAttempts";
	
	private static final String MAX = "max";
	
	private AppCriteria appCriteria;
	
	

	
	
	
	public AbstractCloudyNode(String id, Properties params,
			ArrayList<Resource> resources) throws InvalidParamsException {
		super(id, params, resources);

		// Physical parameter
		if (params.getProperty(MIN_PHYS_CORE) == null)
			throw new InvalidParamsException(MIN_PHYS_CORE + " param is expected.");
		try{
			minPhysCore = Integer.parseInt(params.getProperty(MIN_PHYS_CORE));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(MIN_PHYS_CORE + " must be a valid int value.");
		}
		if (params.getProperty(MAX_PHYS_CORE) == null)
			throw new InvalidParamsException(MAX_PHYS_CORE + " param is expected.");
		try{
			maxPhysCore = Integer.parseInt(params.getProperty(MAX_PHYS_CORE));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(MAX_PHYS_CORE + " must be a valid int value.");
		}
		
		if (params.getProperty(MIN_PHYS_CORE_FREQ) == null)
			throw new InvalidParamsException(MIN_PHYS_CORE_FREQ + " param is expected.");
		try{
			minPhysCoreFreq = Integer.parseInt(params.getProperty(MIN_PHYS_CORE_FREQ));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(MIN_PHYS_CORE_FREQ + " must be a valid int value.");
		}
		if (params.getProperty(MAX_PHYS_CORE_FREQ) == null)
			throw new InvalidParamsException(MAX_PHYS_CORE_FREQ + " param is expected.");
		try{
			maxPhysCoreFreq = Integer.parseInt(params.getProperty(MAX_PHYS_CORE_FREQ));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(MAX_PHYS_CORE_FREQ + " must be a valid int value.");
		}
		if (params.getProperty(LAN_NAME) != null) {
			try {
				if (params.getProperty(LAN_NAME).equals(LAN_NAME_AS_ZONE))
					lanNameAsZone = true;
				else {
					lanId = Integer.parseInt(params.getProperty(LAN_NAME));
				}
			} catch (NumberFormatException ex){
				throw new InvalidParamsException(LAN_NAME + " must be a valid int or " + LAN_NAME_AS_ZONE);
			}
		}
		
		if (params.getProperty(CONNECTION) != null){
			
			if (params.getProperty(CONNECTION).equals(WIFI_G)){
				//dataRate = (new WiFiG()).getDataRate();
				connection = new WiFiG();
			}
			else if (params.getProperty(CONNECTION).equals(WIFI_N)){
				//dataRate = (new WiFiN()).getDataRate();
				connection = new WiFiN();
			}
			else if (params.getProperty(CONNECTION).equals(LAN)){
				//dataRate = (new Lan()).getDataRate();
				connection = new Lan();
			}
			else if (params.getProperty(CONNECTION).equals(G_LAN)){
				//dataRate = (new Glan()).getDataRate();
				connection = new Glan();
			}
			else if (params.getProperty(CONNECTION).equals(TEN_G_LAN)){
				//dataRate = (new TenGlan()).getDataRate();
				connection = new TenGlan();
			}
			
		}
		
		if (params.getProperty(UNIT) != null){
			if (params.getProperty(UNIT).equals(SEC)){
				unit = 1;
			}
			else if (params.getProperty(UNIT).equals(M_SEC)){
				unit = 1000;
			}
			else if (params.getProperty(UNIT).equals(U_SEC)){
				unit = 1000000;
			}
			else if (params.getProperty(UNIT).equals(N_SEC)){
				unit = 1000000000;
			}
		}
		
		/** IaaS Node Layer**/
		/*if (params.getProperty(VOLUNTEER_APPROACH) == null)
			throw new InvalidParamsException(VOLUNTEER_APPROACH + " param is expected.");
		try{
			volunteer = Boolean.parseBoolean(params.getProperty(VOLUNTEER_APPROACH));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(VOLUNTEER_APPROACH + " must be a valid boolean value.");
		}*/
		if (params.getProperty(RESOURCE_TO_VM) == null)
			throw new InvalidParamsException(RESOURCE_TO_VM + " param is expected.");
		try{
			resourceToVM = Double.parseDouble(params.getProperty(RESOURCE_TO_VM));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(RESOURCE_TO_VM + " must be a valid double value."); 
		}
/*		if (params.getProperty(VM_RESIZABLE) == null)
			throw new InvalidParamsException(VM_RESIZABLE + " param is expected.");
		try{
			vmResizable = Boolean.parseBoolean(params.getProperty(VM_RESIZABLE));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(VM_RESIZABLE + " must be a valid boolean value.");
		}
	*/	
	
		if (params.getProperty(ZONE_ASSIGNMENT) == null)
			throw new InvalidParamsException(ZONE_ASSIGNMENT + " param is expected.");
		try{
			if (params.getProperty(ZONE_ASSIGNMENT).equals(RANDOM))
				randomZoneAssignment = true;
			else
				randomZoneAssignment = false;
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(ZONE_ASSIGNMENT + " must be a valid criteria (random)");
		}
		if (params.getProperty(N_OF_REGIONAL_ZONE) == null)
			throw new InvalidParamsException(N_OF_REGIONAL_ZONE + " param is expected.");
		try{
			nOfRegionalZone = Integer.parseInt(params.getProperty(N_OF_REGIONAL_ZONE));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(N_OF_REGIONAL_ZONE + " must be a valid integer value.");
		}
		if (params.getProperty(CONNECT_TO_N_SUPERNODE) == null)
			throw new InvalidParamsException(CONNECT_TO_N_SUPERNODE + " param is expected.");
		try{
			if (params.getProperty(CONNECT_TO_N_SUPERNODE).equals(ALL)){
				nOfSupernodeToConnect = -1;
			}
			else
				nOfSupernodeToConnect = Integer.parseInt(params.getProperty(CONNECT_TO_N_SUPERNODE));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(CONNECT_TO_N_SUPERNODE + " must be a valid integer value (or the 'all' keyword).");
		}
		if (params.getProperty(CONNECTION_CRITERIA_INIT) == null)
			throw new InvalidParamsException(CONNECTION_CRITERIA_INIT + " param is expected.");
		try {
			if (params.getProperty(CONNECTION_CRITERIA_INIT).equals(RANDOM))
				randomInitConnection = true;
			else
				randomInitConnection = false;
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(CONNECTION_CRITERIA_INIT + " must be a valid criteria (random).");
		}
		if (params.getProperty(N_CONNECTION_INIT) == null)
			throw new InvalidParamsException(N_CONNECTION_INIT + " param is expected.");
		try {
			if (params.getProperty(N_CONNECTION_INIT).equals(ALL)){
				nOfConnectionInit = -1;
			}
			else
				nOfConnectionInit = Integer.parseInt(params.getProperty(N_CONNECTION_INIT));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(N_CONNECTION_INIT + " must be a valid integer value.");
		}
		if (params.getProperty(N_CONNECTION_ONGOING) == null)
			throw new InvalidParamsException(N_CONNECTION_ONGOING + " param is expected.");
		try {
			if (params.getProperty(N_CONNECTION_ONGOING).equals(ALL)){
				nOfConnectionOngoing = -1;
			}
			else
				nOfConnectionOngoing = Integer.parseInt(params.getProperty(N_CONNECTION_ONGOING));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(N_CONNECTION_ONGOING + " must be a valid integer value.");
		}
		if (params.getProperty(CONNECTION_AMONG_ZONES_INIT) == null)
			throw new InvalidParamsException(CONNECTION_AMONG_ZONES_INIT + " param is expected.");
		try {
			connectionAmongZonesInit = Boolean.parseBoolean(params.getProperty(CONNECTION_AMONG_ZONES_INIT));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(CONNECTION_AMONG_ZONES_INIT + " must be a valid boolean value.");
		}
		if (params.getProperty(CONNECTION_AMONG_ZONES_ONGOING) == null)
			throw new InvalidParamsException(CONNECTION_AMONG_ZONES_ONGOING + " param is expected.");
		try {
			connectionAmongZonesOngoing = Boolean.parseBoolean(params.getProperty(CONNECTION_AMONG_ZONES_ONGOING));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(CONNECTION_AMONG_ZONES_ONGOING + " must be a valid boolean value."); 
		}
		if (params.getProperty(OVERHEAD_FOR_REMOTE_SITE) == null)
			throw new InvalidParamsException(OVERHEAD_FOR_REMOTE_SITE + " param is expected.");
		try {
			overheadForRemoteSite = Integer.parseInt(params.getProperty(OVERHEAD_FOR_REMOTE_SITE));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(OVERHEAD_FOR_REMOTE_SITE + " must be a valid int value.");
		}
		if (params.getProperty(STABILITY_FACTOR) != null){
			try{
				stabilityFactor = Integer.parseInt(params.getProperty(STABILITY_FACTOR));
			} catch (NumberFormatException ex) {
				throw new InvalidParamsException(STABILITY_FACTOR + " must be a valid integer value.");
			}
		}
		
		
		/** Autonomic Computing Layer **/
		if (params.getProperty(PARTIAL_VOLUNTEER) == null)
			throw new InvalidParamsException(PARTIAL_VOLUNTEER + " param is expected.");
		try{
			partialVolunteer = Boolean.parseBoolean(params.getProperty(PARTIAL_VOLUNTEER));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(PARTIAL_VOLUNTEER + " must be a valid boolean value.");
		}
		
		if (partialVolunteer){
			if (params.getProperty(MISS_RATE_TOLERANCE) == null)
				throw new InvalidParamsException(MISS_RATE_TOLERANCE + " param is expected.");
			try{
				missRateTolerance = Double.parseDouble(params.getProperty(MISS_RATE_TOLERANCE));
			} catch (NumberFormatException ex){
				throw new InvalidParamsException(MISS_RATE_TOLERANCE + " must be a valid double value.");
			}
			
			if (params.getProperty(ASK_TO_VOLUNTEER) == null)
				throw new InvalidParamsException(ASK_TO_VOLUNTEER + " param is expected.");
			try {
				askToVolunteer = Boolean.parseBoolean(params.getProperty(ASK_TO_VOLUNTEER));
			} catch (NumberFormatException ex){
				throw new InvalidParamsException(ASK_TO_VOLUNTEER + " must be a valid boolean.");
			}
			
			if (params.getProperty(MAX_NUM_OF_ATTEMPTS) != null){
				if (params.getProperty(MAX_NUM_OF_ATTEMPTS).equals(MAX))
					maxNumOfAttempts = Integer.MAX_VALUE;
				else {
					try{
						maxNumOfAttempts = Integer.parseInt(params.getProperty(MAX_NUM_OF_ATTEMPTS));
					} catch (NumberFormatException ex){
						throw new InvalidParamsException(MAX_NUM_OF_ATTEMPTS + " must be a valid integer or \"max\"");
					}
				}
			} else{
				maxNumOfAttempts = Integer.MAX_VALUE;
			}
			
			
		}
		else{
			missRateTolerance = -1;
			askToVolunteer = false;
			maxNumOfAttempts = Integer.MAX_VALUE;
		}

	}

	
	public Object clone(){
		AbstractCloudyNode clone = (AbstractCloudyNode) super.clone();
		
		int coreNumber = minPhysCore + (int)(Engine.getDefault().getSimulationRandom().nextDouble()*(maxPhysCore-minPhysCore+1));
		int coreFreq = minPhysCoreFreq + (int)(Engine.getDefault().getSimulationRandom().nextDouble()*(maxPhysCoreFreq-minPhysCoreFreq+1));
		
		int nodeId = Engine.getDefault().generateResourceKey();
		
		AbstractPysicalNodeFeature physicalFeature;
		//int lanNameIdentifier = lanId;
		if ( (lanId != -1) || (lanNameAsZone)){
			if (lanNameAsZone){
				lanId = (int) (Engine.getDefault().getSimulationRandom().nextDouble()*(nOfRegionalZone));
				randomZoneAssignment = false;
				
			}
			physicalFeature = new PhysicalNodeFeature(coreNumber, coreFreq, nodeId, lanId);	
		}else {
			physicalFeature = new PhysicalNodeFeature(coreNumber, coreFreq, nodeId);
		}
		
		
		clone.hw = new Hardware(physicalFeature); 
		
		/** IaaS Node - Virtual Layer **/
		clone.vmCriteria = new VmCriteria(resourceToVM);//, vmResizable);
		clone.vmCriteria.setStabilityFactor(stabilityFactor);
		
		//If the lanNameAsZone is assigned the randomZoneAssignment is overridden by the 
		
		clone.netCriteria = new NetworkCriteria(randomZoneAssignment, nOfRegionalZone, nOfSupernodeToConnect, randomInitConnection, nOfConnectionInit, nOfConnectionOngoing, connectionAmongZonesInit, connectionAmongZonesOngoing);
		clone.netCriteria.setOverheadForRemoteSite(overheadForRemoteSite);
		clone.netCriteria.setZoneAssigned(lanId);
		
		/** AcaaS Layer **/
		clone.appCriteria = new AppCriteria(partialVolunteer, missRateTolerance, askToVolunteer, maxNumOfAttempts);
		
		

		//System.out.println("Created a " + clone.hw.getHwFeature().getTypeOfDevice() + " with id " + clone.hw.getHwFeature().getMachineId());
		
		return clone;
		
	}


	public Hardware getHw() {
		return hw;
	}


	public VmCriteria getVmCriteria() {
		return vmCriteria;
	}


	public NetworkCriteria getNetCriteria() {
		return netCriteria;
	}


	public AppCriteria getAppCriteria() {
		return appCriteria;
	}
	
	
	
}
