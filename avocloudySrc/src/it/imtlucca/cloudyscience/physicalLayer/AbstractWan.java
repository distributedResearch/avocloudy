package it.imtlucca.cloudyscience.physicalLayer;

/**
 * Generic WAN connection
 * 
 * @author Stefano Sebastio
 * 
 */
public abstract class AbstractWan extends AbstractConnection {

	
	private static float rangeMin = 3000;
	private static float rangeMax = 60000;
	
	public float getRangeMin() {
		return rangeMin;
	}
	public float getRangeMax() {
		return rangeMax;
	}
	
	
}
