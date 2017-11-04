package it.imtlucca.cloudyscience.autonomicLayer;

/**
 * Update the World History when happens a new interaction among a pair of node 
 * 
 * @author Stefano Sebastio
 *
 */
public class AcaasNodeKnowledge extends AbstractAcaasNodeKnowledge {
	
	public AcaasNodeKnowledge(AbstractAcaasNodeFeature feature){
		super(feature);
		
	}
	
	
	/**
	 * 'idS' adds to record list the received Ack form 'idD'
	 * 
	 * 
	 * @param idS source request identifier
	 * @param idD idD destination request identifier
	 */
	public void addAckRecord(int idS, int idD){
		//it is not used on PDP'13
		
//	TODO: fare gli identificatori come string per essere piu' flessibili
//TODO: mettere queste due costruzioni della chiave in un unico punto
		//String identifier = new Integer(id).toString();
		
		String identifier = new Integer(idS).toString() + "->" + new Integer(idD).toString();
		
		if (!this.getWorldHistory().containsKey(identifier)){
			AbstractWorldHistory trace = new WorldHistory(idS, idD);
			trace.ackReceived();
			this.getWorldHistory().put(identifier, trace);
		} else {
			this.getWorldHistory().get(identifier).ackReceived();
		}
		
	}

	/**
	 * 'idS' adds to record list the received NAck form 'idD'
	 * 
	 * 
	 * @param idS source request identifier
	 * @param idD idD destination request identifier
	 */
	public void addNackRecord(int idS, int idD){
		
		//TODO: dev'essere chiamata anche quando un nodo muore/disconnette e chiama AstractIaasNodeBehavior.eraseAcceptedApp()
		//String identifier = new Integer(id).toString();
		//TODO: costruzione chiave centralizzata
		String identifier = new Integer(idS).toString() + "->" + new Integer(idD).toString();
		
		if (!this.getWorldHistory().containsKey(identifier)){
			AbstractWorldHistory trace = new WorldHistory(idS, idD);
			trace.nackReceived();
			this.getWorldHistory().put(identifier, trace);
		} else {
			this.getWorldHistory().get(identifier).nackReceived();
		}
	}
	
	/**
	 * Update the counter of requests done by 'idS' to 'idD'
	 * 
	 * @param idS source request identifier
	 * @param idD destination request identifier
	 */
	public void addReqRecord(int idS, int idD){
		//TODO: costruzione chiave centralizzata
		String identifier = new Integer(idS).toString() + "->" + new Integer(idD).toString();
		
		if (!this.getWorldHistory().containsKey(identifier)){
			AbstractWorldHistory trace = new WorldHistory(idS, idD);
			trace.reqReceived();
			this.getWorldHistory().put(identifier, trace);
		} else {
			this.getWorldHistory().get(identifier).reqReceived();
		}
		
	}

	
}
