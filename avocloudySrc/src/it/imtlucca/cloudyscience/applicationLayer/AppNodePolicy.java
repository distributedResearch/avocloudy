package it.imtlucca.cloudyscience.applicationLayer;

/**
 * Add restriction policy to nodes on which the application can performs
 * 
 * @author Stefano Sebastio
 *
 */
public class AppNodePolicy extends AbstractAppNodePolicy {

	// -1 (minus one) stands for NO Zone Restriction 
	private int zoneRestriction = -1;

	public AppNodePolicy(int zoneRestriction) {
		super();
		this.zoneRestriction = zoneRestriction;
	}
	
	//Constructor without zone restriction policy
	public AppNodePolicy(){
		super();
		
	}

	public int getZoneRestriction() {
		return zoneRestriction;
	}
	
	
	
}
