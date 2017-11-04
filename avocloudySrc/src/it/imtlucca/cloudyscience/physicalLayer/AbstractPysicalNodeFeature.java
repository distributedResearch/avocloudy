package it.imtlucca.cloudyscience.physicalLayer;


/**
 * Skeleton (Abstract) class for the definition of each type of Physical Node Feature
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractPysicalNodeFeature {

	// -1 denotes an undefined value
	private int coreNumber = -1;
	private int coreFreq = -1;
	private int mainMemory = -1; //RAM
	private int localMemory = -1; //
	//private int netBandwidth = -1;
	
	
	
	private int machineId = -1;
	
	public int typeOfDevice = DeviceType.UNKNOW;
	
	
	
	public AbstractPysicalNodeFeature(int coreNumber, int coreFreq,
			int machineId, int typeOfDevice) {
		super();
		this.coreNumber = coreNumber;
		this.coreFreq = coreFreq;
		this.machineId = machineId;
		this.typeOfDevice = typeOfDevice;
	}
	
	/**
	 * Defines a minimum level of Feature that must be presented by a Physical Resource
	 * 
	 * @param coreNumber
	 * @param coreFreq
	 * @param machineId
	 */
	public AbstractPysicalNodeFeature(int coreNumber, int coreFreq,
			int machineId) {
		super();
		this.coreNumber = coreNumber;
		this.coreFreq = coreFreq;
		this.machineId = machineId;
	}
	
	
	// Getter methods
	public int getCoreNumber() {
		return coreNumber;
	}

	public int getCoreFreq() {
		return coreFreq;
	}

	public int getMainMemory() {
		return mainMemory;
	}

	public int getLocalMemory() {
		return localMemory;
	}

//	public int getNetBandwidth() {
//		return netBandwidth;
//	}

	public int getMachineId() {
		return machineId;
	}

	public int getTypeOfDevice() {
		return typeOfDevice;
	}

	

	
	
	// Setter methods
	
	public void setCoreNumber(int coreNumber) {
		this.coreNumber = coreNumber;
	}

	public void setCoreFreq(int coreFreq) {
		this.coreFreq = coreFreq;
	}

	public void setMainMemory(int mainMemory) {
		this.mainMemory = mainMemory;
	}

	public void setLocalMemory(int localMemory) {
		this.localMemory = localMemory;
	}

//	public void setNetBandwidth(int netBandwidth) {
//		this.netBandwidth = netBandwidth;
//	}

	public void setMachineId(int machineId) {
		this.machineId = machineId;
	}

	public void setTypeOfDevice(int typeOfDevice) {
		this.typeOfDevice = typeOfDevice;
	}

}
