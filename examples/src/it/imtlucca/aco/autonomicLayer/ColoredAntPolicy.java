package it.imtlucca.aco.autonomicLayer;

import it.imtlucca.cloudyscience.util.IFunction;

/**
 * Policy to manage the colored pheromone.
 * 
 * Describes how to release pheromone according the resource found.
 * The ant's life duration (ttl). 
 * The memory aging function.
 * The Boltzmann temperature function to weight the pheromone available among the node's choice
 * 
 * An optional parameter can be defined to evaporate the pheromone when time passes 
 * 
 * 
 * @author Stefano Sebastio
 *
 */
public class ColoredAntPolicy {

	private IFunction releaseFunction;
	private double initPheromone = 0;
	private int ttl;
	private IFunction temperatureFunction;
	private double evaporation;
	private IFunction agingFunction;
	
	
	public ColoredAntPolicy(IFunction releaseFunction, double initPheromone,
			int ttl, IFunction temperatureFunction) {
		super();
		this.releaseFunction = releaseFunction;
		this.initPheromone = initPheromone;
		this.ttl = ttl;
		this.temperatureFunction = temperatureFunction;
		
		this.evaporation = 0;
		this.agingFunction = null;
	}


	public ColoredAntPolicy(IFunction releaseFunction, double initPheromone,
			int ttl, IFunction temperatureFunction,
			IFunction agingFunction) {
		this(releaseFunction, initPheromone, ttl, temperatureFunction);

		this.agingFunction = agingFunction;
	}

	
	public ColoredAntPolicy(IFunction releaseFunction, double initPheromone,
			int ttl, IFunction temperatureFunction, double evaporation,
			IFunction agingFunction) {
		this(releaseFunction, initPheromone, ttl, temperatureFunction, agingFunction);

		this.evaporation = evaporation;
		//this.agingFunction = agingFunction;
	}


	public IFunction getReleaseFunction() {
		return releaseFunction;
	}


	public double getInitPheromone() {
		return initPheromone;
	}


	public int getTtl() {
		return ttl;
	}


	public IFunction getTemperatureFunction() {
		return temperatureFunction;
	}


	public double getEvaporation() {
		return evaporation;
	}


	public IFunction getAgingFunction() {
		return agingFunction;
	}

	/**
	 * To change the current temperature function. E.g. for the join of a new neighbor
	 * @param temperatureFunction
	 */
	public void setTemperatureFunction(IFunction temperatureFunction) {
		this.temperatureFunction = temperatureFunction;
	}

	
	
}
