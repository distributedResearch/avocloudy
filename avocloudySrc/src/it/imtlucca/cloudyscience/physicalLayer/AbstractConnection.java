package it.imtlucca.cloudyscience.physicalLayer;

/**
 * Define the connection structure
 * 
 * 'Range' are expressed in meter (m)
 * 'Data Rate' are expressed in Mega bits per second (Mbps).
 * 
 * 
 * 
 * The real data rate used by the different type of connection expressed in Mbit/s
 * 
 * For Internet data rate: [Lee:2005] "Measuring Bandwidth Between PlanetLab Nodes"
 * Real datarate are considered as of 50% for wireless and of 75% for wired than the maximal data rate
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractConnection {

	//TODO: utilizzare dei valori di distribuzione nel data rate rispetto ad un valore fissato, soprattuto per internet dato che si dispone anche della stdDev 
	
/*	private static float rangeMax;
	private static float rangeMin;
	
	private static float dataRate;
*/	//private float dataRateMax;
	//private float dataRateMin;
	

/*	public static float getRangeMax() {
		return rangeMax;
	}
	public static float getRangeMin() {
		return rangeMin;
	}
//	public float getDataRateMax() {
//		return dataRateMax;
//	}
//	public float getDataRateMin() {
//		return dataRateMin;
//	}
	public static float getDataRate() {
		return dataRate;
	}
	*/
	
/*	public AbstractConnection(float rangeMin, float rangeMax, float dataRate) {
		super();
		AbstractConnection.rangeMax = rangeMax;
		AbstractConnection.rangeMin = rangeMin;
		AbstractConnection.dataRate = dataRate;
	}*/
	
	public abstract float getRangeMax();
	public abstract float getRangeMin();
	public abstract float getDataRate();
}
