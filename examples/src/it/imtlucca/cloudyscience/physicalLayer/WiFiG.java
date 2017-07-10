package it.imtlucca.cloudyscience.physicalLayer;

/**
 * WiFi G connection
 * 
 * @author Stefano Sebastio
 *
 */
public class WiFiG extends AbstractWiFi {

	private static float rangeMin = 35;
	private static float rangeMax = 100;
	
	private static float dataRate = 24.7f;


	public float getRangeMax() {
		return rangeMax;
	}

	public float getRangeMin() {
		return rangeMin;
	}

	public float getDataRate() {
		return dataRate;
	}
	
	
//	public WiFiG() {
//		super(35, 100, 24.7f);
//		/*this.rangeMin = 35;
//		this.rangeMax = 100;
//		this.dataRate = 24.7f;*/
//	}

	
	
}
