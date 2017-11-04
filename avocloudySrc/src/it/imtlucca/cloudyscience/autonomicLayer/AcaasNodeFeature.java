package it.imtlucca.cloudyscience.autonomicLayer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contains some measures about the performance of itself 
 * 
 * @author Stefano Sebastio
 *
 */
public class AcaasNodeFeature extends AbstractAcaasNodeFeature {

	//FIXME: alcuni campi history devono essere spostati in Knowledge? forse no essendo le 'sue' performance (del nodo stesso). CONTROLLARE !!!

	
	// these fields are relative to the PDP'13 implementation
	
	//counter for overall Application execution requests
	private int ownAppReq = 0;
	private int unmetCounter = 0;
	private int hitCounter = 0;
	
	private int remoteReqAccepted = 0; //accepted remote requests
	private int remoteReqRefused = 0; //refused remote requests

	// counter 'for class of Application' (grouped by Type)
	private HashMap<String, AppTypeStatistics> appTypeStatistics = null;
	
	/*
	private HashMap<String, Integer> reqReceived = null;
	private HashMap<String, Integer> reqUnmet = null;
	private HashMap<String, Integer> reqSatisfied = null;
	// counter the 'waiting' and the 'sojourn' times 
	private HashMap<String, Float> waitingTime = null; //from App creation to start execution -> update when App starts its execution
	private HashMap<String, Float> sojournTime = null; //(waitingTime + serviceTime) from App creation to execution App completion -> update at App completion
	*/
	//private int waitingTimeCounter = 0;
	//private int sojournTimeCounter = 0;
	
	private ArrayList<Double> historyMissHit = null;
	
	
	public AcaasNodeFeature() {
		super();
		
	/*	this.reqReceived = new HashMap<String, Integer>();
		this.reqUnmet = new HashMap<String, Integer>();
		this.reqSatisfied = new HashMap<String, Integer>();
		
		this.waitingTime = new HashMap<String, Float>();
		this.sojournTime = new HashMap<String, Float>();
		*/
		this.historyMissHit = new ArrayList<Double>();
		
		
		this.appTypeStatistics = new HashMap<String, AppTypeStatistics>();
	}


	public int getOwnAppReq() {
		return ownAppReq;
	}
	
	
	public void incReq(){
		this.ownAppReq++;
	}

	
	public HashMap<String, AppTypeStatistics> getAppTypeStatistics(){
		return this.appTypeStatistics;
	}

	public int getReqReceived(String appType) {
		if (this.appTypeStatistics.containsKey(appType)){
			return this.appTypeStatistics.get(appType).getReqReceived();
		}
		else 
			return -1;
	}
	
	public void incReqReceived(String appType) {
		if (!this.appTypeStatistics.containsKey(appType))
			this.appTypeStatistics.put(appType, new AppTypeStatistics(appType));
	
		this.appTypeStatistics.get(appType).incReqReceived();
	}

	public void incReqReceivedSet(String appType, int reqReceived) {
		if (!this.appTypeStatistics.containsKey(appType))
			this.appTypeStatistics.put(appType, new AppTypeStatistics(appType));
	
		this.appTypeStatistics.get(appType).addReqReceivedSet(reqReceived);
	}
	
	public int getReqUnmet(String appType) {
		if (this.appTypeStatistics.containsKey(appType)){
			return this.appTypeStatistics.get(appType).getReqUnmet();
		}
		else
			return -1;
	}
	
	public void incReqUnmet(String appType){
		if (!this.appTypeStatistics.containsKey(appType))
			this.appTypeStatistics.put(appType, new AppTypeStatistics(appType));
		
		this.appTypeStatistics.get(appType).incReqUnmet();
	}
	
	public void incReqUnmetSet(String appType, int reqUnmet){
		if (!this.appTypeStatistics.containsKey(appType))
			this.appTypeStatistics.put(appType, new AppTypeStatistics(appType));
		
		this.appTypeStatistics.get(appType).addReqUnmetSet(reqUnmet);
	}


	public int getReqSatisfied(String appType) {
		if (this.appTypeStatistics.containsKey(appType)){
			return this.appTypeStatistics.get(appType).getReqSatisfied();
		}
		else
			return -1;
	}
	
	public void incReqSatisfied(String appType){
		if (!this.appTypeStatistics.containsKey(appType))
			this.appTypeStatistics.put(appType, new AppTypeStatistics(appType));
		
		this.appTypeStatistics.get(appType).incReqSatisfied();
	}
	
	public void incReqSatisfiedSet(String appType, int reqSatisfied){
		if (!this.appTypeStatistics.containsKey(appType))
			this.appTypeStatistics.put(appType, new AppTypeStatistics(appType));
		
		this.appTypeStatistics.get(appType).addReqSatisfiedSet(reqSatisfied);
	}


	public double getWaitingTime(String appType) {
		if (this.appTypeStatistics.containsKey(appType)){
			return this.appTypeStatistics.get(appType).getWaitingTime();
		}
		else
			return -1;
	}
	
	public int getWaitingTimeCounter(String appType) {
		if (this.appTypeStatistics.containsKey(appType)){
			return this.appTypeStatistics.get(appType).getWaitingTimeCounter();
		}
		else
			return 0;
	}
	
	public void addWaitingTime(String appType, float waiting){
		if (!this.appTypeStatistics.containsKey(appType))
			this.appTypeStatistics.put(appType, new AppTypeStatistics(appType));
		
		this.appTypeStatistics.get(appType).addWaitingTime(waiting);
	}
	
	
//	public int getWaitingTimeCounter() {
//		return waitingTimeCounter;
//	}
//	
//	public void incWaitingCounter(){
//		this.waitingTimeCounter++;
//	}


	public double getSojournTime(String appType) {
		if (this.appTypeStatistics.containsKey(appType)){
			return this.appTypeStatistics.get(appType).getSojournTime();
		}
		else
			return -1;
	}
	
	public void addSojournTime(String appType, float sojourn){
		if (!this.appTypeStatistics.containsKey(appType))
			this.appTypeStatistics.put(appType, new AppTypeStatistics(appType));
		
		this.appTypeStatistics.get(appType).addSojournTime(sojourn);
	}
	
	public int getSojournTimeCounter(String appType) {
		if (this.appTypeStatistics.containsKey(appType)){
			return this.appTypeStatistics.get(appType).getSojournTimeCounter();
		}
		else
			return 0;
	}

//	public int getSojournTimeCounter() {
//		return sojournTimeCounter;
//	}
//	
//	public void incSojournCounter(){
//		this.sojournTimeCounter++;
//	}


	public int getUnmetCounter() {
		return unmetCounter;
	}
	
	public void incUnmetCounter(){
		this.unmetCounter++;
	}

	
	public int getHitCounter() {
		return hitCounter;
	}
	
	public void incHitCounter(){
		this.hitCounter++;
	}


	public ArrayList<Double> getHistoryMissHit() {
		return historyMissHit;
	}


	public int getRemoteReqAccepted() {
		return remoteReqAccepted;
	}
	
	public void incRemoteReqAccepted(){
		this.remoteReqAccepted++;
	}
	
	

	public int getRemoteReqRefused() {
		return remoteReqRefused;
	}


	public void incRemoteReqRefused() {
		this.remoteReqRefused++;
	}


	/**
	 * Compute the miss rate from the application executed history
	 * @return
	 */
	public double actualMissRate(){
		
		double missRate = 0;
		if (this.ownAppReq != 0)
			missRate = (((double)this.unmetCounter)/((double)this.ownAppReq));
		
		return missRate;
		
	}
	
}
