package it.imtlucca.aco.autonomicLayer;

//import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodePolicy;
import java.util.ArrayList;

import it.imtlucca.aco.AntColor;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodePolicy;
import it.imtlucca.cloudyscience.util.IFunction;
import it.unipr.ce.dsg.deus.core.Engine;


/**
 * Defines the rules according to which the ants move in the network
 * 
 * @author Stefano Sebastio
 *
 */
//FIXME: extends abstract e no super !!!
public class AcaasAcoNodePolicy extends AcaasNodePolicy {//extends AbstractAcaasNodePolicy {
	
	// Time to Live for the ant (i.e. the number of hop that performs each ant)
	private int ttl;
	private float timeOut;
	//number of ant throw for each investigation
	private int kAnt;
	
	private double evaporation;
	private double initPheromone;

	//private double pheromoneToDeposit;
	
	private IFunction agingFunction;
	private IFunction temperatureFunction;
	
	//Colored ants's policies
	private ArrayList<ColoredAntPolicy> coloredAntPolicy;
	private ArrayList<Double> coloredWeight;
	
	private double resourceWeight = 0;
	private double finishingTimeWeight = 0;
	private double pheromoneWeight = 0;
	private double linkQtWeight = 0;
	

	public AcaasAcoNodePolicy(int ttl, float timeOut, int kAnt,
			double evaporation, double initP, /*double pToDeposit,*/ IFunction func, IFunction temperature) {
		super(1.0, true, Integer.MAX_VALUE); //so to overwrite every constraint and limiting ant attempts only on their life
		this.ttl = ttl;
		this.timeOut = timeOut;
		this.kAnt = kAnt;
		this.evaporation = evaporation;
		this.initPheromone = initP;
		//this.pheromoneToDeposit = pToDeposit;
		
		this.agingFunction = func;
		this.temperatureFunction = temperature;
		
		this.coloredAntPolicy = new ArrayList<ColoredAntPolicy>(AntColor.N_OF_COLOR);
		this.coloredWeight = new ArrayList<Double>(AntColor.N_OF_COLOR);
		for (int i=0; i<AntColor.N_OF_COLOR; i++){
			this.coloredAntPolicy.add(null);
			this.coloredWeight.add(0.0);
		}

		
	}
	
	public void setAntPolicy(ColoredAntPolicy policy, int color){
		this.coloredAntPolicy.set(color, policy);		
	}
	
	/**
	 * Set the weight for the colored pheromone.
	 * 
	 * @param weight
	 * @param color
	 */
	public void setAntWeight(double weight, int color){
		this.coloredWeight.set(color, weight);
	}
	
	public double getAntWeight(int color){
		return this.coloredWeight.get(color);
	}
	
	public void setWeights(double resource, double finishingTime, double pheromone, double link){
		this.resourceWeight = resource;
		this.finishingTimeWeight = finishingTime;
		this.pheromoneWeight = pheromone;
		this.linkQtWeight = link;
	}
	
	public int getTtl() {
		return ttl;
	}

	public float getTimeOut() {
		return timeOut;
	}

	public int getkAnt() {
		return kAnt;
	}

	public double getEvaporation() {
		return evaporation;
	}

	public double getInitPheromone() {
		return initPheromone;
	}

	/**
	 * 	The amount of pheromone that the ant must release on each hop of the path.
	 * It receives the pheromone gathered and the corresponding step.
	 * The Pheromone level is magnified or reduced by the aging function
	 * 
	 * @return
	 */
	public double getPheromoneToDeposit(int step, double p) {
		return p*this.agingFunction.getValue(step);
		
		//return pheromoneToDeposit;
	}
	
	
	//Colored Ant
	/**
	 * Return the initial colored pheromone
	 * 
	 * @param color
	 * @return
	 */
	public double getInitColoredPheromone(int color){
		return this.coloredAntPolicy.get(color).getInitPheromone();
	}
	
	/**
	 * Return the colored ttl
	 * 
	 * @param color
	 * @return
	 */
	public int getTtlColored(int color){
		return this.coloredAntPolicy.get(color).getTtl();
	}

	/**
	 * Get the pheromone of the specified color according the best resource value found
	 * 
	 * @param resourceValue
	 * @param color
	 * @return
	 */
	public double getColoredPheromone(double resourceValue, int color){
		
		//System.out.println("VT " + Engine.getDefault().getVirtualTime() + " color " + color + " - " + this.coloredAntPolicy.get(color));
		if (this.coloredAntPolicy.get(color) != null)
			return Math.max(this.coloredAntPolicy.get(color).getReleaseFunction().getValue(resourceValue),0);
		else
			return 0;
	}

	
	/**
	 * Check if the colored pheromone is set or not.
	 * 
	 * @param color
	 * @return true if the colored pheromone is used
	 */
	public boolean isColoredReleaseFunction(int color){
		if ( (color < this.coloredAntPolicy.size()) && (this.coloredAntPolicy.get(color) != null) && (this.coloredAntPolicy.get(color).getReleaseFunction() != null) )
			return true;
		else
			return false;
	}


	public double getColoredEvaporation(int color) {
		return this.coloredAntPolicy.get(color).getEvaporation();
	}
	
	/**
	 * 	The amount of pheromone that the ant must release on each hop of the path for ants that take into account aging.
	 * 
	 * @param step is the steps among the best color and the current node
	 * @return
	 */
	public double getColoredPheromoneToDepositWithAging(int step, double p, int color){
		if ( (this.coloredAntPolicy.get(color) == null) || (this.coloredAntPolicy.get(color).getAgingFunction() == null) )
			return p;
		else 
			return p*this.coloredAntPolicy.get(color).getAgingFunction().getValue(step);
	}
	
	/**
	 * The temperature is define relative to the time elapsed from the initial cloud  
	 * 
	 * @param p
	 * @param color
	 * @return
	 */
	public double getColoredPheromoneForBoltzmann(double p, int color){
//		if(this.coloredAntPolicy.get(color) == null)
//			return 0.0;
		//System.out.println("this " + this);
		if (this.coloredAntPolicy.get(color).getTemperatureFunction() == null)
			return p;
		else
			return (Math.exp(p)/this.coloredAntPolicy.get(color).getTemperatureFunction().getValue(Engine.getDefault().getVirtualTime()));
		
	}
	
	public double getPheromoneForBoltzmann(double p){
		if(this.getTemperatureFunction() == null)
			return p;
		else
			return Math.exp(p)/this.getTemperatureFunction().getValue(Engine.getDefault().getVirtualTime());
	}

	public ArrayList<ColoredAntPolicy> getColoredAntPolicy() {
		return coloredAntPolicy;
	}


	public double getResourceWeight() {
		return resourceWeight;
	}

	public double getFinishingTimeWeight() {
		return finishingTimeWeight;
	}

	public double getPheromoneWeight() {
		return pheromoneWeight;
	}

	public double getLinkQtWeight() {
		return linkQtWeight;
	}

	public IFunction getTemperatureFunction() {
		return temperatureFunction;
	}

	public void setTemperatureFunction(IFunction temperatureFunction) {
		this.temperatureFunction = temperatureFunction;
	}

}
