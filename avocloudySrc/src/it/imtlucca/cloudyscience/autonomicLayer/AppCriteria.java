package it.imtlucca.cloudyscience.autonomicLayer;

/**
 * Criteria for managing the application at ACaaS Layer (according PDP'13 simulator)
 * 
 * @author Stefano Sebastio
 *
 */
public class AppCriteria {

	private boolean partialVolunteer;
	private double missRateTolerance;
	private boolean askToVolunteer;
	private int maxNumOfAttempt;

	public AppCriteria(boolean partialVolunteer, double missRateTolerance, boolean askToVolunteer, int maxNumOfAttempt) {
		super();
		this.partialVolunteer = partialVolunteer;
		this.missRateTolerance = missRateTolerance;
		this.askToVolunteer = askToVolunteer;
		this.maxNumOfAttempt = maxNumOfAttempt;
	}

	
	
	public int getMaxNumOfAttempt() {
		return maxNumOfAttempt;
	}

	public double getMissRateTolerance() {
		return missRateTolerance;
	}
	
	public boolean isPartialVolunteer(){
		return this.partialVolunteer;
	}
	
	public boolean isAskingToVolunteer(){
		return this.askToVolunteer;
	}
	
	
}
