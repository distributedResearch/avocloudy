package it.imtlucca.cloudyscience.infrastructureLayer;

/**
 * Criteria under which IaaS Node creates and manages virtual network
 * 
 * @author Stefano Sebastio
 *
 */
public class NetworkCriteria {

	private boolean randomZoneAssignement;
	private int nOfRegionalZone;
	private int nOfSupernodeToConnect; //if set to -1 connect to the maximum number of supernodes
	private boolean randomConnectionInit;
	private int nOfConnectionInit; //if set to -1 connect  to the maximum number
	private int nOfConnectionOngoing; //if set to -1 connect  to the maximum number
	private boolean connectionAmongZonesInit;
	private boolean connectionAmongZonesOngoing;
	
	private int overheadForRemoteSite;
	private int zoneAssigned = -1;
	
	
	public NetworkCriteria(boolean randomZoneAssignement, int nOfRegionalZone,
			int nOfSupernodeToConnect, boolean randomConnectionInit,
			int nOfConnectionInit, int nOfConnectionOngoing,
			boolean connectionAmongZonesInit,
			boolean connectionAmongZonesOngoing) {
		super();
		this.randomZoneAssignement = randomZoneAssignement;
		this.nOfRegionalZone = nOfRegionalZone;
		this.nOfSupernodeToConnect = nOfSupernodeToConnect;
		this.randomConnectionInit = randomConnectionInit;
		this.nOfConnectionInit = nOfConnectionInit;
		this.nOfConnectionOngoing = nOfConnectionOngoing;
		this.connectionAmongZonesInit = connectionAmongZonesInit;
		this.connectionAmongZonesOngoing = connectionAmongZonesOngoing;
		
		this.overheadForRemoteSite= 0; 
	}

	

	public int getZoneAssigned() {
		return zoneAssigned;
	}



	public void setZoneAssigned(int zoneAssigned) {
		this.zoneAssigned = zoneAssigned;
	}



	public int getOverheadForRemoteSite() {
		return overheadForRemoteSite;
	}



	public void setOverheadForRemoteSite(int overheadForRemoteSite) {
		this.overheadForRemoteSite = overheadForRemoteSite;
	}



	public boolean isRandomZoneAssignement() {
		return randomZoneAssignement;
	}


	public int getnOfRegionalZone() {
		return nOfRegionalZone;
	}


	public int getnOfSupernodeToConnect() {
		return nOfSupernodeToConnect;
	}


	public boolean isRandomConnectionInit() {
		return randomConnectionInit;
	}


	public int getnOfConnectionInit() {
		return nOfConnectionInit;
	}


	public int getnOfConnectionOngoing() {
		return nOfConnectionOngoing;
	}


	public boolean isConnectionAmongZonesInit() {
		return connectionAmongZonesInit;
	}


	public boolean isConnectionAmongZonesOngoing() {
		return connectionAmongZonesOngoing;
	}
	
	public boolean connectToAllSupernodes(){
		return (nOfSupernodeToConnect == -1);
	}
	
	public boolean connectToAllInit(){
		return (nOfConnectionInit == -1);
	}
	
	public boolean connectToAllOngoing(){
		return (nOfConnectionOngoing == -1);
	}
	
}
