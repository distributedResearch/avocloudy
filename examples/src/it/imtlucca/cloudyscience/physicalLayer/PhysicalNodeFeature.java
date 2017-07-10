package it.imtlucca.cloudyscience.physicalLayer;

import java.util.ArrayList;

import it.imtlucca.cloudyscience.AbstractCloudyNode;
import it.imtlucca.cloudyscience.util.NodeList;
import it.unipr.ce.dsg.deus.core.Engine;

/**
 * General Feature of a Physical Node
 * 
 * @author Stefano Sebastio
 *
 */
public class PhysicalNodeFeature extends AbstractPysicalNodeFeature {

	private int lanId = -1;
	
	//geo Location Information
	//int geoLocation; 
	
	/**
	 * For small differences in latitude and longitude, if you are willing to assume a round earth, rather than elliptical (errors around 0.5%), 
	 * a degree of latitude is about 111 km, and a degree of longitude depends on latitude and is about 111 km * cos(Lat). 
	 * These approximations are reasonable for differences in latitude and/or longitude which are smaller than 5 degrees. 
	 * 
	 * http://forum.onlineconversion.com/showthread.php?t=15713
	 * http://www.ga.gov.au/geodesy/datums/redfearn_grid_to_geo.jsp
	 */
	private double xLocation = 0;
	private double yLocation = 0;
	
	//private int connectionType;
	private AbstractConnection connection = null;
	//private float connectionDataRate;
	
	/**
	 * Polar coordinate are used. The conversion in meter respect to (0,0) is done assuming a earth as spherical, through a conversion factor
	 * 
	 X Min Minimum x coordinate value. 
	 Y Min Minimum y coordinate value. 
	 X Max Maximum x coordinate value. 
	 Y Max Maximum y coordinate value.
	 
	 XYDomain "-180 -90 180 90"
	 */
	private static final double LONGITUDE_MIN = -180; //West
	private static final double LATITUDE_MIN = -90; //South
	private static final double LONGITUDE_MAX = 180; //East
	private static final double LATITUDE_MAX = 90; //North
	
	private static final double CONVERSION_FACTOR = 111;

	
	public PhysicalNodeFeature(int coreNumber, int coreFreq, int machineId,
			int lanId) {
		this(coreNumber, coreFreq, machineId);
		
		this.lanId = lanId;
	}
	
	/*public PhysicalNodeFeature(int coreNumber, int coreFreq, int machineId,
			int typeOfDevice, int lanId){
		this(coreNumber, coreFreq, machineId, typeOfDevice);
		
		this.lanId = lanId;
		
		this.associateConnectionType();
		
		
		this.createNodePhysicalPositionAndRate();
	}*/


	public PhysicalNodeFeature(int coreNumber, int coreFreq, int machineId) {
		super(coreNumber, coreFreq, machineId);
		
	}
	
	public void setTypeOfDevice(int typeOfDevice, AbstractConnection connection) {
		
		super.setTypeOfDevice(typeOfDevice);
		
		//this.associateConnectionType();
		this.connection = connection;
		
		this.createNodePhysicalPositionAndRate();
	}

	public int getLanId() {
		return lanId;
	}

	public double getxLocation() {
		return xLocation;
	}

	public double getyLocation() {
		return yLocation;
	}

	
	private void createNodePhysicalPositionAndRate(){
		NodeList nodeList = new NodeList();
		ArrayList<AbstractCloudyNode> nodes = nodeList.getNodeInLanId(this.lanId);
		
		// if it is the first node in the lan -> random
		if (nodes.size() == 0){
			double longitude = LONGITUDE_MIN + (double)(Engine.getDefault().getSimulationRandom().nextDouble()*(LONGITUDE_MAX-LONGITUDE_MIN));
			double latitude = LATITUDE_MIN + (double)(Engine.getDefault().getSimulationRandom().nextDouble()*(LATITUDE_MAX-LATITUDE_MIN));
			
			//a degree of latitude is about 111 km, and a degree of longitude depends on latitude and is about 111 km * cos(Lat).
			//related to latitude
			this.yLocation = latitude*CONVERSION_FACTOR;
			//related to longitude
			this.xLocation = longitude*CONVERSION_FACTOR*Math.cos(longitude);			
			
		} else { //near centroid with range that depends on connection type
			double x_mean = 0;
			double y_mean = 0;
			
			for (AbstractCloudyNode n : nodes){
				if (n.getHw().getHwFeature() instanceof PhysicalNodeFeature){
					PhysicalNodeFeature nodeFeature = (PhysicalNodeFeature) n.getHw().getHwFeature();
					x_mean += nodeFeature.getxLocation();
					y_mean += nodeFeature.getyLocation();
				}
			}
			
			this.xLocation = x_mean/nodes.size();
			this.yLocation = y_mean/nodes.size();
//			double max = connection.getRangeMax();
//			double min = connection.getRangeMin();
			//choice among the outdoor or indoor connection range
			float range; 
			int extraction = Engine.getDefault().getSimulationRandom().nextInt(2);
			if (extraction == 0) //outdoor
				range = connection.getRangeMin();
			else //indoor
				range = connection.getRangeMax();
//			
//			this.xLocation += min + (double)(Engine.getDefault().getSimulationRandom().nextDouble()*(max-min));  
//			this.yLocation += min + (double)(Engine.getDefault().getSimulationRandom().nextDouble()*(max-min));
			
			this.xLocation += (double)(Engine.getDefault().getSimulationRandom().nextDouble()*(range));
			this.yLocation += (double)(Engine.getDefault().getSimulationRandom().nextDouble()*(range));
		}
		//System.out.println("connectionDR " + this.connectionDataRate);
	}

	public float getConnectionDataRate() {
		return this.connection.getDataRate();
	}

	public AbstractConnection getConnection() {
		return connection;
	}
	
	
	
}
