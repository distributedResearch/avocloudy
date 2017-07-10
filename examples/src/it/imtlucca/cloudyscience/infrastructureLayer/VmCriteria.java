package it.imtlucca.cloudyscience.infrastructureLayer;

/**
 * Criteria under which IaaS Node creates VM
 * 
 * @author Stefano Sebastio
 *
 */
public class VmCriteria {

	private double quoteOfPhysToVm;
	private boolean vmResizable = false;
	
	private int stabilityFactor = -1;
	
	public VmCriteria(double quoteOfPhysToVm){//, boolean vmResizable) {
		super();
		this.quoteOfPhysToVm = quoteOfPhysToVm;
		//this.vmResizable = vmResizable;
	}

	public double getQuoteOfPhysToVm() {
		return quoteOfPhysToVm;
	}

	public boolean isVmResizable() {
		return vmResizable;
	}
	
	public void setAsVmResizable(){
		this.vmResizable = true;
	}

	public int getStabilityFactor() {
		return stabilityFactor;
	}

	public void setStabilityFactor(int stabilityFactor) {
		this.stabilityFactor = stabilityFactor;
	}
	
	
	
}
