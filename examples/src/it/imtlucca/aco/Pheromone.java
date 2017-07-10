package it.imtlucca.aco;

import java.util.ArrayList;
import java.util.Collections;

import it.imtlucca.aco.autonomicLayer.AcaasAcoNodePolicy;
import it.imtlucca.cloudyscience.applicationLayer.AppAgent;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;
import it.unipr.ce.dsg.deus.core.Engine;

/**
 * Describe the pheromone released in the path with a neighbor
 * 
 * @author Stefano Sebastio
 *
 */
public class Pheromone {

	private double pheromone;
	//Time of the last pheromone update
	private float time;
	private IaasAgent neighbor;
	//private AcaasAcoNodePolicy policy;
	
	private ArrayList<Double> coloredPheromone;
	private float timeForFinishingTime;
	
	private double probability = 0;
	
	private ArrayList<Double> coloredPheromoneProbability;
	/**
	 * Last quality link quality perceived. To a higher value correspond a worst connection. 
	 * Thus in the hunter pheromone evaluation the value are normalized to the worst among neighbor to get a value in the range [0,1]
	 */
	private float lastLinkQtEvaluated = 0;

	
	public Pheromone(double pheromone, float time, IaasAgent neighbor/*, AcaasAcoNodePolicy policy*/) {
		super();
		this.pheromone = pheromone;
		this.time = time;
		this.neighbor = neighbor;
		//this.policy = policy;
	
		this.coloredPheromone = new ArrayList<Double>(AntColor.N_OF_COLOR);
		this.coloredPheromoneProbability = new ArrayList<Double>(AntColor.N_OF_COLOR);
		for (int i=0; i<AntColor.N_OF_COLOR; i++){
			this.coloredPheromone.add(i, 0.0);
			this.coloredPheromoneProbability.add(i,0.0);
		}
	}


	/**
	 * Get the pheromone on the path after have checked the evaporated quantity
	 * 
	 * @return
	 */
	public double getPheromone(AcaasAcoNodePolicy policy) {
		this.checkEvaporation(policy);
		return pheromone;
	}
	
	/**
	 * The currentNodePolicy is used since the entrance of a new node is perceived only by the neighbors nodes and
	 * not from the node where the ant is originated
	 * 
	 * @param policy
	 * @param app
	 * @param currentNodePolicy
	 * @return
	 */
	public double getPheromoneWithTemperature(AcaasAcoNodePolicy policy, AppAgent app, AcaasAcoNodePolicy currentNodePolicy){
		double p = this.overallPheromone(policy, app);
		//return policy.getPheromoneForBoltzmann(p);
		if (currentNodePolicy.getTemperatureFunction() != null)
			return currentNodePolicy.getPheromoneForBoltzmann(p);
		else
			return policy.getPheromoneForBoltzmann(p);
	}
	
	public double getNormalizedPheromoneWithTemperature(AcaasAcoNodePolicy policy, AppAgent app, AcaasAcoNodePolicy currentNodePolicy, ArrayList<Pheromone> neighbors){
		double p = this.normalizedOverallPheromone(policy, app, neighbors);
		//return policy.getPheromoneForBoltzmann(p);
		/*if (currentNodePolicy.getTemperatureFunction() != null)
			return currentNodePolicy.getPheromoneForBoltzmann(p);*/
		if (policy.getTemperatureFunction() != null)
			return policy.getPheromoneForBoltzmann(p);
		else
			//return policy.getPheromoneForBoltzmann(p);
			return p;
	}
	
	public double normalizedOverallPheromone(AcaasAcoNodePolicy policy, AppAgent app, ArrayList<Pheromone> neighbors){
		
		double overallPheromone = Math.pow(this.normalizedWeightedColoredPheromone(policy, app, neighbors), policy.getResourceWeight());
		double factorValue = 0;
		
		factorValue = Math.pow(this.getNormalizedColoredPheromone(policy, AntColor.FINISHING_TIME, neighbors), policy.getFinishingTimeWeight());
		if (factorValue != 0)
			overallPheromone *= factorValue;
		
		factorValue = Math.pow(this.getPheromoneNormalized(neighbors), policy.getPheromoneWeight());
		if (factorValue != 0)
			overallPheromone *= factorValue;
		
		factorValue = Math.pow(this.getLastLinkQtEvaluatedNormalized(app, neighbors), policy.getLinkQtWeight());
		if (factorValue != 0)
			overallPheromone *= factorValue;
		
		return overallPheromone;
	}
	
	/**
	 * Normalizing the colored pheromone to the ones for the other neighbors
	 * 
	 */
	private double normalizedWeightedColoredPheromone(AcaasAcoNodePolicy policy, AppAgent app, ArrayList<Pheromone> neighbors){
		
		//double overallWeight = 0;
		double colorValue = 0;
		
		for (int i=0; i<AntColor.N_OF_COLOR; i++){
			if (this.taskUtilization(policy, app, i) != 0){
			//	overallWeight += policy.getAntWeight(i); 
				colorValue += policy.getAntWeight(i)*this.taskUtilization(policy, app, i);
			}
		}
		double normalizingColorPheromone = 0;
		for (Pheromone p : neighbors){
			for (int i=0; i<AntColor.N_OF_COLOR; i++){
				if (p.taskUtilization(policy, app, i) != 0){
					//overallWeight += policy.getAntWeight(i); 
					normalizingColorPheromone += policy.getAntWeight(i)*p.taskUtilization(policy, app, i);
				}
			}
		}
		//return (colorValue/overallWeight);
		return ( colorValue / normalizingColorPheromone);
	}
	
	/**
	 * 
	 * @param policy
	 * @param color
	 * @return
	 */
	public double getNormalizedColoredPheromone(AcaasAcoNodePolicy policy, int color, ArrayList<Pheromone> neighbors){

		if (color == AntColor.FINISHING_TIME){
			this.checkEvaporationFinishingTime(policy);
			for (Pheromone p : neighbors){
				p.checkEvaporationFinishingTime(policy);
			}
		}
		
		double sumColoredPheromone = 0; 
		for (Pheromone p : neighbors){
			sumColoredPheromone += p.coloredPheromone.get(color);
		}
		
		return (this.coloredPheromone.get(color)/sumColoredPheromone);
	}
	
	public double getPheromoneNormalized(ArrayList<Pheromone> neighbors){
		double otherPheromones = 0; 
		for (Pheromone p : neighbors){
			otherPheromones += p.getPheromone();
		}
		
		return (this.pheromone/otherPheromones);
	}
	
	/**
	 * Normalize the link quality according to: 1-delay/max(delay).
	 * With this normalization a higher (near to 1) obtained value correspond to a better link.
	 *  
	 * 
	 * @param app
	 * @param neighbors
	 * @return
	 */
	public double getLastLinkQtEvaluatedNormalized(AppAgent app, ArrayList<Pheromone> neighbors){
		double worstLinkQt = 0;
		
		for (Pheromone p : neighbors){
			worstLinkQt = Math.max(p.getCurrentLinkQtEvaluation(app, neighbors),worstLinkQt);
		}
		
		return (1-(this.getLastLinkQtEvaluated()/worstLinkQt));
	}
	
	/**
	 * Evaluate the task (under-over) utilization of node resources
	 * 
	 * @param policy
	 * @param app
	 */
	private double taskUtilization(AcaasAcoNodePolicy policy, AppAgent app, int color){ 
		
		double coloredPheromone = this.getColoredPheromone(policy, color);
		if (coloredPheromone == 0) 
			return 0.0;
		double resourceReq = 0;
		switch (color) {
		case AntColor.CPU_CORE:
			resourceReq = policy.getColoredPheromone(app.getFeature().getParallelism(), color);
			//resourceReq = app.getFeature().getParallelism();
			break;
		case AntColor.CPU_FREQ:
			resourceReq = policy.getColoredPheromone(app.getFeature().getDuration(), color);
			//resourceReq = app.getFeature().getDuration();
			break;
		case AntColor.MAIN_MEMORY:
			resourceReq = policy.getColoredPheromone(app.getFeature().getRam(), color);
			//resourceReq = app.getFeature().getRam();
			break;
		}
		resourceReq = policy.getColoredPheromoneToDepositWithAging(0, resourceReq, color);
		return (Math.min(resourceReq, coloredPheromone)/Math.max(resourceReq, coloredPheromone));
		
	}
	
	private double weightedColoredPheromone(AcaasAcoNodePolicy policy, AppAgent app){
		
		double overallWeight = 0;
		double colorValue = 0;
		
		for (int i=0; i<AntColor.N_OF_COLOR; i++){
			if (this.taskUtilization(policy, app, i) != 0){
				overallWeight += policy.getAntWeight(i); 
				colorValue += policy.getAntWeight(i)*this.taskUtilization(policy, app, i);
			}
		}
		
		
		return (colorValue/overallWeight);
	}
	
	/**
	 * Calculate the overall pheromone value according our ACO approach
	 * 
	 * @param policy
	 * @param app
	 * @return
	 */
	public double overallPheromone(AcaasAcoNodePolicy policy, AppAgent app){
		
		double overallPheromone = Math.pow(this.weightedColoredPheromone(policy, app), policy.getResourceWeight());
		double factorValue = 0;
		
		factorValue = Math.pow(this.getColoredPheromone(policy, AntColor.FINISHING_TIME), policy.getFinishingTimeWeight());
		if (factorValue != 0)
			overallPheromone *= factorValue;
		
		factorValue = Math.pow(this.pheromone, policy.getPheromoneWeight());
		if (factorValue != 0)
			overallPheromone *= factorValue;
		
		factorValue = Math.pow(this.lastLinkQtEvaluated, policy.getLinkQtWeight());
		if (factorValue != 0)
			overallPheromone *= factorValue;
		
		return overallPheromone;
	}
	

	/**
	 * Get the time at which the pheromone is updated.
	 * 
	 * Initially it corresponds to the time at which is deposited. 
	 * After is related to the last evaporation action. 
	 * 
	 * @return
	 */
	public float getTime() {
		return time;
	}

	public IaasAgent getNeighbor() {
		return neighbor;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	/**
	 * Deposit pheromone on the path.
	 * 
	 * Every time that a new pheromone is added the previous one is checked for evaporation process
	 * 
	 * @param p
	 */
	public void depositPheromone(AcaasAcoNodePolicy policy, double p){
		this.checkEvaporation(policy);
		
		this.pheromone += Math.max(p, 0);
		
		this.time = Engine.getDefault().getVirtualTime();
		
	}
	
	
	/**
	 * Update the pheromone value according the evaporation
	 * 
	 */
	public void checkEvaporation(AcaasAcoNodePolicy policy){
		
		//if (this.pheromone > this.policy.getInitPheromone()){
		if (this.pheromone > policy.getInitPheromone()){
		
			float elapsedTime = (Engine.getDefault().getVirtualTime() - this.time);
			//this.pheromone = Math.max(this.pheromone - this.pheromone*elapsedTime*this.policy.getEvaporation(), this.policy.getInitPheromone());
			this.pheromone = Math.max(this.pheromone - this.pheromone*elapsedTime*policy.getEvaporation(), policy.getInitPheromone());
			this.time = Engine.getDefault().getVirtualTime();
		}
	}
	

	public double getColoredPheromone(AcaasAcoNodePolicy policy, int color){
		if (color == AntColor.FINISHING_TIME){
			this.checkEvaporationFinishingTime(policy);
		}
		return this.coloredPheromone.get(color);
	}
	
	/**
	 * The currentNodePolicy is used since the entrance of a new node is perceived only by the neighbors nodes and
	 * not from the node where the ant is originated
	 * 
	 * 
	 * @param policy
	 * @param color
	 * @param currentNodePolicy
	 * @return
	 */
	public double getColoredPheromoneWithTemperature(AcaasAcoNodePolicy policy, int color, AcaasAcoNodePolicy currentNodePolicy){
		double p = this.getColoredPheromone(policy, color);
		//System.out.println("policyForT " + policy);
		//return policy.getColoredPheromoneForBoltzmann(p, color);
		//System.out.println("color " + color);
		if (currentNodePolicy.getColoredAntPolicy().get(color) != null)
			return currentNodePolicy.getColoredPheromoneForBoltzmann(p, color);
		else 
			return policy.getColoredPheromoneForBoltzmann(p, color);
	}

	public double getColoredProbability(int color){
		return this.coloredPheromoneProbability.get(color);
	}

	public void setColoredProbability(double coloredProbability, int color){
		this.coloredPheromoneProbability.set(color, coloredProbability);
	}

	
	/**
	 * 
	 * Update the pheromone of the corresponding color taking into account the aging effect
	 * 'Memory Aging' approach
	 * 
	 * @param stepIdStart starting horizon of memory aging
	 * @param color
	 */
	public void depositColoredPheromone(AcaasAcoNodePolicy policy, /*double bestColor,*/ int stepIdStart, int color, ColoredAnt ant){
		if (color == AntColor.FINISHING_TIME)
			this.checkEvaporationFinishingTime(policy);
		double bestColor = Collections.max(ant.getResources().subList(stepIdStart, ant.getResources().size()));
		//ant.getResources().subList(step, ant.getResources().size());
		//The maximum is taken into account to consider the presence of tie best that can bring a negative memory step value
		
		//int memoryStep = Math.max(ant.getResources().indexOf(bestColor) - stepIdStart, 0);
		int memoryStep = ant.getResources().indexOf(bestColor) - stepIdStart;
		
		//double p = this.policy.getColoredPheromone(bestColor, color);
		double p = policy.getColoredPheromone(bestColor, color);
		
		//double pAging = this.policy.getColoredPheromoneToDepositWithAging(step, p, color);
		//double pAging = policy.getColoredPheromoneToDepositWithAging(step, p, color);
		double pAging = policy.getColoredPheromoneToDepositWithAging(memoryStep, p, color);
		double currentPheromone = this.coloredPheromone.get(color);
		
		if (color == AntColor.FINISHING_TIME){ 
			this.coloredPheromone.set(color, currentPheromone+pAging);
			//every time that the finishing time pheromone is updated its deposit time is refreshed (since its depend on evaporation process) 
			this.timeForFinishingTime = Engine.getDefault().getVirtualTime();
		}
		else 
			this.coloredPheromone.set(color, Math.max(pAging, currentPheromone));
		
	}

	
	
	
	/**
	 * Update the finishing time pheromone value according the evaporation
	 * 
	 */
	//TODO: generalizzare l'evaporation per tutti i colori
	public void checkEvaporationFinishingTime(AcaasAcoNodePolicy policy){

		if ( (policy.getColoredAntPolicy().get(AntColor.FINISHING_TIME) != null) &&
				(this.coloredPheromone.get(AntColor.FINISHING_TIME) >
			//this.policy.getInitColoredPheromone(AntColor.FINISHING_TIME)){
			policy.getInitColoredPheromone(AntColor.FINISHING_TIME)) ){
		
			float elapsedTime = (Engine.getDefault().getVirtualTime() - this.timeForFinishingTime);

			//this.coloredPheromone.set(AntColor.FINISHING_TIME, Math.max(this.coloredPheromone.get(AntColor.FINISHING_TIME) - this.coloredPheromone.get(AntColor.FINISHING_TIME)*elapsedTime*this.policy.getColoredEvaporation(AntColor.FINISHING_TIME), this.policy.getInitColoredPheromone(AntColor.FINISHING_TIME)) );
			this.coloredPheromone.set(AntColor.FINISHING_TIME, Math.max(this.coloredPheromone.get(AntColor.FINISHING_TIME) - this.coloredPheromone.get(AntColor.FINISHING_TIME)*elapsedTime*policy.getColoredEvaporation(AntColor.FINISHING_TIME), policy.getInitColoredPheromone(AntColor.FINISHING_TIME)) );
			this.timeForFinishingTime = Engine.getDefault().getVirtualTime();
		}
	}
	
	public void setInitColoredPheromone(double p, int color){
		
		this.coloredPheromone.set(color, p);

	}


	/**
	 * Lower delay value correspond to better link quality.
	 * 
	 * Thus if higher values of lastLinkQtEvaluated is worst
	 * 
	 * @return
	 */
	public double getLastLinkQtEvaluated() {
		return lastLinkQtEvaluated;
	}


	public void setLastLinkQtEvaluated(float lastLinkQtEvaluated) {
		this.lastLinkQtEvaluated = lastLinkQtEvaluated;
	}
	
	public double getCurrentLinkQtEvaluation(AppAgent app, ArrayList<Pheromone> neighbors){
		float lastLinkQtEvaluated = neighbor.getBehavior().evaluateNetOverhead(app, app.getReferringAgent());
		//update the current link quality
		this.setLastLinkQtEvaluated(lastLinkQtEvaluated);
		
		for (Pheromone p : neighbors){
			float d = p.getNeighbor().getBehavior().evaluateNetOverhead(app, app.getReferringAgent());
			p.setLastLinkQtEvaluated(d);
		}
		
		return lastLinkQtEvaluated;
	}

	/**
	 * Retrieve the hunter hunt pheromone
	 * @return
	 */
	public double getPheromone() {
		return pheromone;
	}

	
}
