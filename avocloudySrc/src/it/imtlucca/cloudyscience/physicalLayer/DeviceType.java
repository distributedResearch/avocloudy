package it.imtlucca.cloudyscience.physicalLayer;

/**
 * Identifier for the Type of Device.
 * 
 * The DataCenter devices main aim is the execution of requests from other nodes
 * The Volunteer devices can submit but also accept requests from other 
 * The Mobile devices are low powered equipment that require only 
 * 
 * @author Stefano Sebastio
 *
 */

public class DeviceType {

	//TODO: andrebbe trasformata in un enum
	
	public static final int UNKNOW = -1;
	
	public static final int DATACENTER = 0;
	public static final int VOLUNTEER = 1;
	public static final int MOBILE = 2; //low power device
	
}
