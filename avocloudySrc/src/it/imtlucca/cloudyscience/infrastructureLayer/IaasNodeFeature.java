package it.imtlucca.cloudyscience.infrastructureLayer;

/**
 * Simple implementation of IaaS Feature
 * 
 * @author Stefano Sebastio
 *
 */
public class IaasNodeFeature extends AbstractIaasNodeFeature {

	public IaasNodeFeature(int coreNumber, float coreFreq, int mainMemory,
			int nodeId) {
		super(coreNumber, coreFreq, mainMemory, nodeId);
	}

}
