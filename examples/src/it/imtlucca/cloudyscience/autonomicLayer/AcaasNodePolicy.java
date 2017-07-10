package it.imtlucca.cloudyscience.autonomicLayer;

/**
 * Defines the policy used in the partial volunteer studies.
 * Following the PDP'13 paper
 * 
 * @author Stefano Sebastio
 *
 */
public class AcaasNodePolicy extends AbstractAcaasNodePolicy {

	
	private double missRateTolerance;
	private boolean askToVolunteer;
	private int maxNumOfAttempt;

	public AcaasNodePolicy(double missRateTolerance, boolean askToVolunteer, int maxNumOfAttempt) {
		super();
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
	
	public boolean askingToVolunteer(){
		return askToVolunteer;
	}
	
	
	
}
