package it.imtlucca.cloudyscience.infrastructureLayer.dc;

/**
 * Skeleton for the Data Center node Policy
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractIaasDCNodePolicy {

	/**
	 * Quotes of resource that must be added (removed) on startup or resize phases  
	 */
	private double rateResourceToVMStartup;
	private double rateResourceToVMResize;
	
	private boolean createResizableVM = false;
	
	/**
	 * Triggering values that must be observed to act a change on IaaS Nodes on top of the DataCenter
	 */
	private double triggerUtilToStart;
	private double triggerUtilToShutdown;
	private double triggerUtilToResize;

	public AbstractIaasDCNodePolicy(double rateResourceToVMStartup,
			double rateResourceToVMResize, boolean createResizableVM,
			double triggerUtilToStart, double triggerUtilToShutdown,
			double triggerUtilToResize) {
		super();
		this.rateResourceToVMStartup = rateResourceToVMStartup;
		this.rateResourceToVMResize = rateResourceToVMResize;
		this.createResizableVM = createResizableVM;
		this.triggerUtilToStart = triggerUtilToStart;
		this.triggerUtilToShutdown = triggerUtilToShutdown;
		this.triggerUtilToResize = triggerUtilToResize;
	}

	public double getRateResourceToVMStartup() {
		return rateResourceToVMStartup;
	}

	public double getRateResourceToVMResize() {
		return rateResourceToVMResize;
	}

	public boolean isCreateResizableVM() {
		return createResizableVM;
	}

	public double getTriggerUtilToStart() {
		return triggerUtilToStart;
	}

	public double getTriggerUtilToShutdown() {
		return triggerUtilToShutdown;
	}

	public double getTriggerUtilToResize() {
		return triggerUtilToResize;
	}

	
	
	public void setRateResourceToVMStartup(double rateResourceToVMStartup) {
		this.rateResourceToVMStartup = rateResourceToVMStartup;
	}

	public void setRateResourceToVMResize(double rateResourceToVMResize) {
		this.rateResourceToVMResize = rateResourceToVMResize;
	}

	public void setCreateResizableVM(boolean createResizableVM) {
		this.createResizableVM = createResizableVM;
	}

	public void setTriggerUtilToStart(double triggerUtilToStart) {
		this.triggerUtilToStart = triggerUtilToStart;
	}

	public void setTriggerUtilToShutdown(double triggerUtilToShutdown) {
		this.triggerUtilToShutdown = triggerUtilToShutdown;
	}

	public void setTriggerUtilToResize(double triggerUtilToResize) {
		this.triggerUtilToResize = triggerUtilToResize;
	}
	
	
	
	
	
}
