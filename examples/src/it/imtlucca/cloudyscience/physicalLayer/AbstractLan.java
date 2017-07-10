package it.imtlucca.cloudyscience.physicalLayer;


/**
 * Generic LAN Connection
 * 
 * The effective data rate (without overhead) is assumed to be the 75% of the declared one
 * 
 * @author Stefano Sebastio
 */
public abstract class AbstractLan extends AbstractConnection {

	private static float rangeMin = 100;
	private static float rangeMax = 3000;
	
	public float getRangeMin() {
		return rangeMin;
	}
	public float getRangeMax() {
		return rangeMax;
	}
	
}
