package it.imtlucca.aco;

import java.util.ArrayList;

import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;

/**
 * Define a colored ant that periodically search for a node according a color criteria.
 * It will release pheromone according the goodness of the nest found
 * 
 * @author Stefano Sebastio
 *
 */
public class ColoredAnt {

	private final int antColor;
	//possibile eliminare il best colorFound
	//private double bestColorFound;
	//private final double worstColor;
	
	private ArrayList<IaasAgent> path;
	private ArrayList<Double> resources;
	private int ttlValue;
	private IaasAgent source;
	
	//total number of hops crossed by the ant
	private int nHop;
	
	public ColoredAnt(IaasAgent source, int color, /*double worstColor,*/ int maxTtl) {
		super();
		
		this.path = new ArrayList<IaasAgent>(maxTtl);
		this.resources = new ArrayList<Double>(maxTtl);
		
		this.ttlValue = 0;
		this.source = source;
		this.antColor = color;
		
		//this.bestColorFound = worstColor;
		//this.worstColor = worstColor;
		
		this.nHop = 0;
	}


	public ArrayList<IaasAgent> getPath() {
		return path;
	}


	public ArrayList<Double> getResources() {
		return resources;
	}


	public int getnHop() {
		return nHop;
	}

	public int getTtlValue() {
		return ttlValue;
	}


	public IaasAgent getSource() {
		return source;
	}

	public int getAntColor() {
		return antColor;
	}
	/*
	public double getBestColorFound() {
		return bestColorFound;
	}*/


	public ColoredAnt addStep(IaasAgent a, double colorFound){
		//check if the new value is better than the previous found
		/*if (Math.abs(this.worstColor-colorFound) > Math.abs(this.worstColor-this.bestColorFound)){
			this.bestColorFound = colorFound;
		}*/
		this.resources.add(colorFound);
		
		this.path.add(a);
		this.nHop++;
		this.ttlValue--;
		return this;
	}


	/**
	 * Initialize the ant
	 */
	public void initAnt(int ttl){
		this.path.clear();
		this.resources.clear();
		this.ttlValue = ttl;
		//this.bestColorFound = this.worstColor;
	}
	
}
