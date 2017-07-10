package it.imtlucca.cloudyscience.infrastructureLayer;

/**
 * Abstract class that defines the main Feature of a IaaS Node
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractIaasNodeFeature {

	// -1 denotes an undefined value
	private int coreNumber = -1;
	private float coreFreq = -1;
	private int mainMemory = -1; //RAM

	
	private int nodeId = -1;
	
	private boolean supernode = false;
	private int zoneOfBelonging = -1;
	private boolean isOnline = false;
	
	private int stabilityFactor = 0;
	
	public AbstractIaasNodeFeature(int coreNumber, float coreFreq,
			int mainMemory, int nodeId) {
		super();
		this.coreNumber = coreNumber;
		this.coreFreq = coreFreq;
		this.mainMemory = mainMemory;
		this.nodeId = nodeId;
	}

	public int getCoreNumber() {
		return coreNumber;
	}

	public float getCoreFreq() {
		return coreFreq;
	}

	public int getMainMemory() {
		return mainMemory;
	}

	public int getNodeId() {
		return nodeId;
	}

	public boolean isSupernode() {
		return supernode;
	}

	public int getZoneOfBelonging() {
		return zoneOfBelonging;
	}

	public void goOnline(){
		this.isOnline = true;
	}
	
	public void goOffline(){
		this.isOnline = false;
	}
	
	public boolean isOnline(){
		return this.isOnline;
	}
	
	
	public void setAsSupernode() {
		this.supernode = true;
	}
	
	public void unsetAsSupernode(){
		this.supernode = false;
	}

	public void setZoneOfBelonging(int zoneOfBelonging) {
		this.zoneOfBelonging = zoneOfBelonging;
	}

	public int getStabilityFactor() {
		return stabilityFactor;
	}

	public void setStabilityFactor(int stabilityFactor) {
		this.stabilityFactor = stabilityFactor;
	}
	
	
	
	
}
