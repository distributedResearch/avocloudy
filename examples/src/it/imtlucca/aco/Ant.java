package it.imtlucca.aco;

import java.util.ArrayList;

import it.imtlucca.cloudyscience.applicationLayer.AppAgent;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;

/**
 * Define an ant that is searching for a node able to execute a task.
 * It will release pheromone if it will found a nest (the node that that the task in charge)
 * 
 * @author Stefano Sebastio
 *
 */
public class Ant {

	private AppAgent app;
	private ArrayList<IaasAgent> path;
	private int ttlValue;
	private IaasAgent source;
	
	//total number of hops crossed by the ant
	private int nHop;
	
	//the number of App that an Ant has served
	private int servedApp;
	
	
	
	public Ant(IaasAgent source) {
		super();
		
		this.app = null;
		this.path = new ArrayList<IaasAgent>();
		this.ttlValue = 0;
		this.source = source;
		
		this.nHop = 0;
		this.servedApp = 0;
		
	}


	public AppAgent getApp() {
		return app;
	}


	public ArrayList<IaasAgent> getPath() {
		return path;
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


	public Ant setApp(AppAgent app) {
		
		this.app = app;
		//this.path.add(this.source);
		
		if (app != null)
			this.servedApp++;
		
		return this;
	}
	
	public Ant addStep(IaasAgent a){
		this.path.add(a);
		this.nHop++;
		this.ttlValue--;
		return this;
	}


	public int getServedApp() {
		return servedApp;
	}

	/**
	 * Initialize the ant
	 */
	public void initAnt(int ttl){
		this.app = null;
		this.path.clear();
		this.ttlValue = ttl;
	}
	
	//required by partitioned ants to do not waste a move walking over nodes in a different set
	public void bonusTtl(){
		this.ttlValue++;
	}
}
