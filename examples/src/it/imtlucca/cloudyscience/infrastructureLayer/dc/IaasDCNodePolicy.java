package it.imtlucca.cloudyscience.infrastructureLayer.dc;

/**
 * Minimum implementation of a Data Center level policy
 * 
 * @author Stefano Sebastio
 *
 */
public class IaasDCNodePolicy extends AbstractIaasDCNodePolicy {

	public IaasDCNodePolicy(double rateResourceToVMStartup,
			double rateResourceToVMResize, boolean createResizableVM,
			double triggerUtilToStart, double triggerUtilToShutdown,
			double triggerUtilToResize) {
		super(rateResourceToVMStartup, rateResourceToVMResize, createResizableVM,
				triggerUtilToStart, triggerUtilToShutdown, triggerUtilToResize);

	}

	
}
