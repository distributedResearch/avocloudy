package it.imtlucca.cloudyscience.infrastructureLayer.dc;

/**
 * Minimum implementation of a Data Center level implementation
 * 
 * @author Stefano Sebastio
 *
 */
public class IaasDCNodeKnowledge extends AbstractIaasDCNodeKnowledge {

	public IaasDCNodeKnowledge(){
		super();
	}
	
	public void addRecord(AbstractLoadRecord record, int id){
		String identifier = new Integer(id).toString(); 
		super.addRecord(record, identifier);
	}
	
}
