package it.imtlucca.cloudyscience.infrastructureLayer;

import java.util.Comparator;

/**
 * Compare two IaaS Agents according their zone of belonging 
 * 
 * @author Stefano Sebastio
 *
 */
public class CloudSiteComparator implements Comparator<IaasAgent> {

	/**
	 * Compare two IaaS Agents according their zone of belonging
	 */
	public int compare(IaasAgent arg0, IaasAgent arg1) {
		
		int a = arg0.getKnowledge().getFeature().getZoneOfBelonging();
		int b = arg1.getKnowledge().getFeature().getZoneOfBelonging();
		int nOfZone = arg0.getKnowledge().getNumberOfRegionalZones();
		
		return Math.abs(a-b)%nOfZone;
	}

	
}
