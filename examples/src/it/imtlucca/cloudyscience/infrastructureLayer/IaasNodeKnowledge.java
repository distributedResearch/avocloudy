package it.imtlucca.cloudyscience.infrastructureLayer;

/**
 * Simple implementation of IaaS Knowledge
 * 
 * @author Stefano Sebastio
 *
 */
public class IaasNodeKnowledge extends AbstractIaasNodeKnowledge {

	private int overheadForRemoteSite;
	
	public IaasNodeKnowledge(int numberOfRegionalZones,
			AbstractIaasNodeFeature feature) {
		super(numberOfRegionalZones, feature);

		this.overheadForRemoteSite = 0;
	}

	public int getOverheadForRemoteSite() {
		return overheadForRemoteSite;
	}

	public void setOverheadForRemoteSite(int overheadForRemoteSite) {
		this.overheadForRemoteSite = overheadForRemoteSite;
	}

	

}
