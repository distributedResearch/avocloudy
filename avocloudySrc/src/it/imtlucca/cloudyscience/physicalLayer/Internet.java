package it.imtlucca.cloudyscience.physicalLayer;

/**
 * 
 * Internet connection
 * 
 * @author Stefano Sebastio
 *
 */
public class Internet extends AbstractConnection {

//	public static final float INTERNET_AVERAGE = 64;
//	public static final float INTERNET_STDDEV = 44;
	
	private static float rangeMin = 60000;
	private static float rangeMax = Float.MAX_VALUE;
	
	private static float dataRate = 64;


	public float getRangeMax() {
		return rangeMax;
	}

	public float getRangeMin() {
		return rangeMin;
	}

	public float getDataRate() {
		return dataRate;
	}

}
