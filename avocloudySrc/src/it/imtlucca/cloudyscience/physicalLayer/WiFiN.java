package it.imtlucca.cloudyscience.physicalLayer;

/**
 * WiFi N connection
 * 
 * @author Stefano Sebastio
 *
 */
public class WiFiN extends AbstractWiFi {

	private static float rangeMin = 65;
	private static float rangeMax = 200;
	
	private static float dataRate = 52.4f;
	
//	public WiFiN() {
//		super(65, 200, 52.4f);
//		
//	}
	
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
