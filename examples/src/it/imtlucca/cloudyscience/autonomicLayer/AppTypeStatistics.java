package it.imtlucca.cloudyscience.autonomicLayer;

/**
 * 
 * Collects all the statistics referred to a particular type of Applications
 * 
 * @author Stefano Sebastio
 *
 */
public class AppTypeStatistics {

	private int reqReceived;
	private int reqUnmet;
	private int reqSatisfied;
	
	//from App creation to start execution -> update when App starts its execution
	private double waitingTime;
	private int waitingTimeCounter;
	//(waitingTime + serviceTime) from App creation to execution App completion -> update at App completion
	private double sojournTime;
	private int sojournTimeCounter;
	
	private String appType;
	
	
	public AppTypeStatistics(String appType){
		super();
		
		this.appType = appType;
		
		this.reqReceived = 0;
		this.reqUnmet = 0;
		this.reqSatisfied = 0;
		
		this.waitingTime = 0;
		this.sojournTime = 0;
		this.waitingTimeCounter = 0;
		this.sojournTimeCounter = 0;
	}


	public int getReqReceived() {
		return reqReceived;
	}


	public int getReqUnmet() {
		return reqUnmet;
	}


	public int getReqSatisfied() {
		return reqSatisfied;
	}
	
	public void incReqReceived() {
		this.reqReceived++;
	}

	public void incReqUnmet() {
		this.reqUnmet++;
	}


	public void incReqSatisfied() {
		this.reqSatisfied++;
	}
	
	public void addReqSatisfiedSet(int reqSatisfied){
		this.reqSatisfied += reqSatisfied;
	}
	
	public void addReqReceivedSet(int reqReceived){
		this.reqReceived += reqReceived;
	}
	
	public void addReqUnmetSet (int reqUnmet){
		this.reqUnmet += reqUnmet;
	}


	public double getWaitingTime() {
		return waitingTime;
	}


	public double getSojournTime() {
		return sojournTime;
	}
	

	public int getWaitingTimeCounter() {
		return waitingTimeCounter;
	}
	
	public void addWaitingTimeCounter(int addition) {
		this.waitingTimeCounter += addition;
	}


	public int getSojournTimeCounter() {
		return sojournTimeCounter;
	}

	public void addSojournTimeCounter(int addition) {
		this.sojournTimeCounter += addition;
	}

	
	public void addWaitingTime(double wait){
		this.waitingTime += wait;
		this.waitingTimeCounter++;
	}
	
	public void addSojournTime(double sojourn){
		this.sojournTime += sojourn;
		this.sojournTimeCounter++;
	}


	public String getAppType() {
		return appType;
	}
	
	
	
	
}
