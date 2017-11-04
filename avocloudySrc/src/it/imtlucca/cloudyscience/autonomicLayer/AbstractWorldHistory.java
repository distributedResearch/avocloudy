package it.imtlucca.cloudyscience.autonomicLayer;

/**
 * The World History contains the history information of interaction among a pair of ACaaS nodes 
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractWorldHistory {

	private int ackCounter;
	private int nackCounter;
	private int reqCounter;
	
	// Source and Destination of the request
	private int source;
	private int dest;
	
	public AbstractWorldHistory(int s, int d) {
		super();
		this.ackCounter = 0;
		this.nackCounter = 0;
		this.reqCounter = 0;
		
		this.source = s;
		this.dest = d;
	}


	public int getAckCounter() {
		return ackCounter;
	}


	public int getNackCounter() {
		return nackCounter;
	}
	
	
	public int getReqCounter() {
		return reqCounter;
	}


	public int ackReceived(){
		return this.ackCounter++;
	}
	
	public int nackReceived(){
		return this.nackCounter++;
	}
	
	public int reqReceived(){
		return this.reqCounter++;
	}
	
	public double ackNackRatio(){
		return (this.ackCounter/this.nackCounter);
	}


	public int getSource() {
		return source;
	}


	public int getDest() {
		return dest;
	}
	
	
	
}
