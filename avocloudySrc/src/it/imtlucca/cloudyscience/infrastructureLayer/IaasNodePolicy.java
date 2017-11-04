package it.imtlucca.cloudyscience.infrastructureLayer;

/**
 * Simple implementation of IaaS Policy
 * 
 * @author Stefano Sebastio
 *
 */
public class IaasNodePolicy extends AbstractIaasNodePolicy {

	public IaasNodePolicy(boolean randomZoneAssignemnt, int nSupernodeAtInit,
			boolean randomConnectionInit, int nConnectionInit,
			int nConnectionOngoing, boolean connectionAmongZonesInit,
			boolean connectionAmongZonesOngoing) {
		super(randomZoneAssignemnt, nSupernodeAtInit, randomConnectionInit,
				nConnectionInit, nConnectionOngoing, connectionAmongZonesInit,
				connectionAmongZonesOngoing);
	}

	
	
}
