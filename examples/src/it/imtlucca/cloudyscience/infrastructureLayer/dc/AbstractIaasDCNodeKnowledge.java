package it.imtlucca.cloudyscience.infrastructureLayer.dc;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Skeleton for the Data Center Knowledge. It is mainly constituted by the log usage record 
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractIaasDCNodeKnowledge {

	//ArrayList<AbstractLoadRecord> logUsage = null;
	Hashtable<String, ArrayList<AbstractLoadRecord>> logUsage;// = null;

	public AbstractIaasDCNodeKnowledge() {
		super();
		this.logUsage = new Hashtable<String,ArrayList<AbstractLoadRecord>>();
	}
	
	/**
	 * Add a new record for the node 'id'
	 * 
	 * @param record
	 * @param id identifier of the record node owner
	 */
	public void addRecord(AbstractLoadRecord record, String id){
		//this.logUsage.add(record);
		if (!this.logUsage.containsKey(id)) {
			ArrayList<AbstractLoadRecord> trace = new ArrayList<AbstractLoadRecord>();
			trace.add(record);
			this.logUsage.put(id, trace);
		}
		else {
			this.logUsage.get(id).add(record);
		}
			
		
	}
	
	
}
