package it.imtlucca.cloudyscience.applicationLayer;

import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;

import java.util.ArrayList;

/**
 * Knowledge on nodes that are performing the application (and request counter that must be done prior to obtain an Ack for execution)
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractAppNodeKnowledge {

	private int reqCounter;
	private ArrayList<AcaasAgent> performingNodes;
	
	private AbstractAppNodeFeature feature; 
	
	
	public AbstractAppNodeKnowledge(AbstractAppNodeFeature feature) {
		super();
		this.reqCounter = 0;
		this.feature = feature;
		this.performingNodes = new ArrayList<AcaasAgent>();
	}


	public int getReqCounter() {
		return reqCounter;
	}


	public ArrayList<AcaasAgent> getPerformingNodes() {
		return performingNodes;
	}
	
	
	public void addPerformingNode(AcaasAgent a){
		this.performingNodes.add(a);
	}


	public AbstractAppNodeFeature getFeature() {
		return feature;
	}
	
	// Counter of execution requests (excluding the one done to the contacting ACaaS Node Agent)
	public void incReqCounter(){
		this.reqCounter++;
	}
	
}
