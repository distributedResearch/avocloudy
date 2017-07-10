package it.imtlucca.cloudyscience.infrastructureLayer;

/**
 * Main policy for the IaaS node.
 * These define the structure of the overlay network among IaaS nodes through the definitions of the allowed connections 
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractIaasNodePolicy {

	private boolean randomZoneAssignemnt;
	private int nSupernodeAtInit;
	private boolean randomConnectionInit;
	private int nConnectionInit;
	private int nConnectionOngoing;
	private boolean connectionAmongZonesInit;
	private boolean connectionAmongZonesOngoing;
	
	private int zoneAssignement = -1;
	
	
	public AbstractIaasNodePolicy(boolean randomZoneAssignemnt,
			int nSupernodeAtInit, boolean randomConnectionInit,
			int nConnectionInit, int nConnectionOngoing,
			boolean connectionAmongZonesInit,
			boolean connectionAmongZonesOngoing) {
		super();
		this.randomZoneAssignemnt = randomZoneAssignemnt;
		this.nSupernodeAtInit = nSupernodeAtInit;
		this.randomConnectionInit = randomConnectionInit;
		this.nConnectionInit = nConnectionInit;
		this.nConnectionOngoing = nConnectionOngoing;
		this.connectionAmongZonesInit = connectionAmongZonesInit;
		this.connectionAmongZonesOngoing = connectionAmongZonesOngoing;
	}

	
	

	public int getZoneAssignement() {
		return zoneAssignement;
	}

	public void setZoneAssignement(int zoneAssignement) {
		this.zoneAssignement = zoneAssignement;
	}



	public boolean isRandomZoneAssignemnt() {
		return randomZoneAssignemnt;
	}


	public int getnSupernodeAtInit() {
		return nSupernodeAtInit;
	}


	public boolean isRandomConnectionInit() {
		return randomConnectionInit;
	}


	public int getnConnectionInit() {
		return nConnectionInit;
	}


	public int getnConnectionOngoing() {
		return nConnectionOngoing;
	}


	public boolean isConnectionAmongZonesInit() {
		return connectionAmongZonesInit;
	}


	public boolean isConnectionAmongZonesOngoing() {
		return connectionAmongZonesOngoing;
	}


	public void setRandomZoneAssignemnt(boolean randomZoneAssignemnt) {
		this.randomZoneAssignemnt = randomZoneAssignemnt;
	}


	public void setnSupernodeAtInit(int nSupernodeAtInit) {
		this.nSupernodeAtInit = nSupernodeAtInit;
	}


	public void setRandomConnectionInit(boolean randomConnectionInit) {
		this.randomConnectionInit = randomConnectionInit;
	}


	public void setnConnectionInit(int nConnectionInit) {
		this.nConnectionInit = nConnectionInit;
	}


	public void setnConnectionOngoing(int nConnectionOngoing) {
		this.nConnectionOngoing = nConnectionOngoing;
	}


	public void setConnectionAmongZonesInit(boolean connectionAmongZonesInit) {
		this.connectionAmongZonesInit = connectionAmongZonesInit;
	}


	public void setConnectionAmongZonesOngoing(boolean connectionAmongZonesOngoing) {
		this.connectionAmongZonesOngoing = connectionAmongZonesOngoing;
	}

	
	
	
	
}
