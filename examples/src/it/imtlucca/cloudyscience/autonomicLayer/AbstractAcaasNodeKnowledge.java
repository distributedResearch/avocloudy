package it.imtlucca.cloudyscience.autonomicLayer;

import java.util.Hashtable;

/**
 * Basic Knowledge of the ACaaS node constituted by the world history 
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractAcaasNodeKnowledge {

	// the key is formed by "sourceId->destinationId"
	private Hashtable<String, AbstractWorldHistory> worldHistory = null;
	
	private AbstractAcaasNodeFeature feature = null;
	

	public AbstractAcaasNodeKnowledge(AbstractAcaasNodeFeature feature) {
		super();
		this.worldHistory = new Hashtable<String, AbstractWorldHistory>();
		this.feature = feature;
	}

	public void addAckRecord(String id){

	}

	public void addNackRecord(String id){
		
	}

	public AbstractAcaasNodeFeature getFeature() {
		return feature;
	}

	public Hashtable<String, AbstractWorldHistory> getWorldHistory() {
		return worldHistory;
	}
	
	
	
	
}
