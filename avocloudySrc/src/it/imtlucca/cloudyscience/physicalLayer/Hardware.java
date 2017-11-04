package it.imtlucca.cloudyscience.physicalLayer;

import it.imtlucca.cloudyscience.AbstractCloudyNode;
import it.imtlucca.cloudyscience.applicationLayer.AppAgent;

/**
 * It defines the building blocks elements related to Physical resources.
 * It is not defined like an Agent since it is a passive element 
 * 
 * @author Stefano Sebastio
 *
 */
public class Hardware {

	AbstractPysicalNodeFeature hwFeature = null;
	private static final float internetDataRate = (new Internet()).getDataRate();
	//Earth radius in Kms
	//private static final double EARTH_RADIUS = 6371;
	
	//Distance is: Distance = Time * Velocity 
	//Propagation delay on fiber is 2/3 of speed of light -> 2*10^8 m/s
	private static final float FIBER_VELOCITY = 200000000;
	//Wave propagation on vacuum -> 3*10^8 m/s
	private static final float VACUUM_VELOCITY = 300000000;
	
	public Hardware(AbstractPysicalNodeFeature hwFeature) {
		super();
		this.hwFeature = hwFeature;
		
	}

	public AbstractPysicalNodeFeature getHwFeature() {
		return hwFeature;
	}
	
	/**
	 * Evaluate the time needed to transmit the application among two different nodes in the network, according the physical network.
	 * 
	 * Each physical node has an associated lan_id. If two nodes need to transfer an Application, they need to take into account the
	 * network overhead.
	 * Overhead is composed by the network delay (usually it is under threshold in the same lan) plus the transferring time from the
	 * two endpoints (the minimum data rate among them). 
	 * 
	 * @return the returned delay is rescaled according the specified unit of measure
	 */
	public float evaluateOverheadTime(Hardware otherPoint, AppAgent app){

		//There is a fixed overhead due to delay
		float delay = this.evaluateDelayOverheadTime(otherPoint);
		//another overhead is done when the application as an associated size parameter-> app.size()/min(path-dataRate)
		if (app.getFeature().getSize() > 0)
			delay += this.evaluateTaskTransmissionOverhead(otherPoint, app);
		//System.out.println("unit " + AbstractCloudyNode.unit);
		return (delay/AbstractCloudyNode.unit);
	}
	
	/**
	 * Evaluate the delay associated to the delay among the other nodes.
	 * 
	 * It is an overestimation since it consider always the worst case: OPTICAL_FIBER with
	 * a delay of 2/3 of speed of light (the 2/3 are related to the fiber refraction index)
	 * 
	 * Geographical delay is calculate following the code:
	 * http://fnss.github.io/
	 * [Saino:2013] "A Toolchain for Simplifying Network Simulation Setup"
	 * [cit.]:"Assign a delay to all selected links equal to the product of link length
	 * and specific delay. To use this function, all nodes must have a 'latitude'
	 * and a 'longitude' attribute."
	 * 
	 * @param otherPoint
	 * @return delay is provided in seconds
	 */
	private float evaluateDelayOverheadTime(Hardware otherPoint) {
		if (this.hwFeature instanceof PhysicalNodeFeature){
	
			double a_x = ((PhysicalNodeFeature)this.hwFeature).getxLocation();
			double a_y = ((PhysicalNodeFeature)this.hwFeature).getyLocation();
			
			double b_x = ((PhysicalNodeFeature)otherPoint.getHwFeature()).getxLocation();
			double b_y = ((PhysicalNodeFeature)otherPoint.getHwFeature()).getyLocation();
			
			
			//float(sqrt((x_v - x_u)**2 + (y_v - y_u)**2))
			double length = Math.sqrt(Math.pow(a_x-b_x,2)+ Math.pow(a_y-b_y,2));
			
			//If the nodes are in the same network the delay could be due to wireless or wired connection
			if (((PhysicalNodeFeature)this.hwFeature).getLanId() == ((PhysicalNodeFeature)otherPoint.getHwFeature()).getLanId()){
				
				if (((PhysicalNodeFeature)this.hwFeature).getConnection() instanceof AbstractWiFi) // the connection is a wireless one
					return ((float)( (float)length / VACUUM_VELOCITY));
				else
					return ((float)( (float)length / FIBER_VELOCITY));
			}
			else {

				//if the nodes position is expressed in latitude and longitude
				/**
				 * lon_u = (pi/180) * topology.node[u]['longitude']  
				 * lat_u = (pi/180) * topology.node[u]['latitude']  
				 * lon_v = (pi/180) * topology.node[v]['longitude']  
				 * lat_v = (pi/180) * topology.node[v]['latitude']  
				 * length = EARTH_RADIUS * acos(cos(lon_v - lon_u) * cos(lat_v - lat_u))  
				 **/
					
				return ((float)( (float)length / FIBER_VELOCITY));
			
			}			
		}
		return 0;
	}
	
	/**
	 * The task transmission overhead is the affected by the minimum data rate available among the tow endpoints.
	 * 
	 * Task size is divided by network data rate.
	 * 
	 * @param hw
	 * @param app
	 * @return delay is provided in seconds
	 */
	private float evaluateTaskTransmissionOverhead(Hardware hw, AppAgent app){
		
		float appSize = app.getFeature().getSize();
		float dataRate = this.dataRate(hw);
		//float dataRate = 0;
		
		/*if (this.hwFeature instanceof PhysicalNodeFeature){
			
			float destDr = ((PhysicalNodeFeature)this.hwFeature).getConnectionDataRate();
			float sourceDr = ((PhysicalNodeFeature)hw.getHwFeature()).getConnectionDataRate();
			
			
			dataRate = Math.min(destDr, sourceDr);
			
			if (((PhysicalNodeFeature)this.hwFeature).getLanId() != ((PhysicalNodeFeature)hw.getHwFeature()).getLanId()){
				dataRate = Math.min(dataRate, Hardware.internetDataRate);
			}
		}*/
		
		return ((float) (appSize/dataRate));
	}
	
	/**
	 * Evaluate the bandwidth transmitting to the other point 
	 */
	private float dataRate(Hardware otherPoint){
		float dataRate = 0;
		float destDr = ((PhysicalNodeFeature)this.hwFeature).getConnectionDataRate();
		float sourceDr = ((PhysicalNodeFeature)otherPoint.getHwFeature()).getConnectionDataRate();
		
		
		dataRate = Math.min(destDr, sourceDr);
		
		if (((PhysicalNodeFeature)this.hwFeature).getLanId() != ((PhysicalNodeFeature)otherPoint.getHwFeature()).getLanId()){
			dataRate = Math.min(dataRate, Hardware.internetDataRate);
		}
		
		return dataRate;
	}
}
