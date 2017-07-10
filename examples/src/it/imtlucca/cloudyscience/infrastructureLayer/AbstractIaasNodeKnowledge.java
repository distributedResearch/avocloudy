package it.imtlucca.cloudyscience.infrastructureLayer;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Defines the structure of the Knowledge for the IaaS node.
 * It is mainly constituted by the informations about the overlay network
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractIaasNodeKnowledge {
	
	private int numberOfRegionalZones;
	private ArrayList<IaasAgent> neighbors;
	private ArrayList<IaasAgent> supernodes;
	private IaasAgent supernodeResponsible;
	
	private AbstractIaasNodeFeature feature;
	
	private IaasAgent referringAgent;
	
	public AbstractIaasNodeKnowledge(int numberOfRegionalZones, AbstractIaasNodeFeature feature) {
		super();
		this.numberOfRegionalZones = numberOfRegionalZones;
		this.feature = feature;
		this.referringAgent = null;
		this.neighbors = new ArrayList<IaasAgent>();
		this.supernodes = new ArrayList<IaasAgent>();
		this.supernodeResponsible = null;
		
	}

	public void clearOverlayKnowledge(){
		this.supernodeResponsible = null;
		this.neighbors = new ArrayList<IaasAgent>();
		this.supernodes = new ArrayList<IaasAgent>();
	}
	
	public IaasAgent getReferringAgent() {
		return referringAgent;
	}

	public void setReferringAgent(IaasAgent referringAgent) {
		this.referringAgent = referringAgent;
	}

	public int getNumberOfRegionalZones() {
		return numberOfRegionalZones;
	}
	
	public AbstractIaasNodeFeature getFeature() {
		return this.feature;
	}

	public ArrayList<IaasAgent> getNeighbors() {
		return neighbors;
	}
	
	public ArrayList<IaasAgent> getSupernodes() {
		return supernodes;
	}
	
	public boolean addNeighbor(IaasAgent neighbor){
		/*if ( ((this.referringAgent instanceof IaasAgentVolunteer) && this.referringAgent.getFeature().isSupernode()) && (!this.neighbors.contains(neighbor) && (neighbor != this.referringAgent))){
			System.out.print("supernode is volunteer " + this.referringAgent);
			System.out.println("    newNieghbor " + neighbor);
		}*/
		
		if ( !this.neighbors.contains(neighbor) && (neighbor != this.referringAgent) ) {
			//System.out.println("newNieghbor " + neighbor + " referringOfThis " + this.referringAgent);

			this.getReferringAgent().newNeighborNotify(neighbor);
			
			
			return this.neighbors.add(neighbor);
		}
		else {
		//	System.out.print("addNeighbor false case");
		//	System.out.println("      newNieghbor " + neighbor + " referringOfThis " + this.referringAgent);
			return false;
		}
	}
	
	public boolean removeNeighbor(IaasAgent neighbor){
		return this.neighbors.remove(neighbor);
	}
	
	public boolean addSupernode(IaasAgent supernode){
		if (!this.supernodes.contains(supernode)){ // && (supernode != this.referringAgent)) leave itself in the list of all supernodes
			this.getReferringAgent().newNodeNotify(supernode);
			return this.supernodes.add(supernode);
		}
		else
			return false;
	}
	
	public boolean removeSupernode(IaasAgent supernode) {
		return this.supernodes.remove(supernode);
	}

	public IaasAgent getSupernodeResponsible() {
		return supernodeResponsible;
	}

	public void setSupernodeResponsible(IaasAgent supernodeResponsible) {
		this.supernodeResponsible = supernodeResponsible;
	}

	/**
	 * Add supernodes to the list if these do not already belong
	 * 
	 * @param supernodes
	 */
	public void addSupernodes(ArrayList<IaasAgent> supernodes){
		if (supernodes == null)
			return;
		//TODO: controllare il caso in cui si passa null come argomento
		for (IaasAgent s : supernodes){
			this.addSupernode(s);
			//TODO: rimuovere supernodi se si e' sopra il limite specificato dalla policy? (considerare sempre lo stesso parametro nConnectionAmongZones)  
		}
	}
	
	public void addUpToNSupernodes(ArrayList<IaasAgent> supernodes, int n){
		Collections.shuffle(supernodes);
		if (n == -1) //add all supernodes
			this.addSupernodes(supernodes);
		else
			this.addSupernodes((ArrayList<IaasAgent>)supernodes.subList(0, n));
		
	}
	
	/**
	 * Add neighbors to the list if these do not already belong
	 * @param neighbors
	 */
	public void addNeighbors(ArrayList<IaasAgent> neighbors){
		//TODO: controllare il caso in cui si passa null come argomento
		for (IaasAgent n : neighbors){
			this.addNeighbor(n);
			
			//TODO: rimuovere vicini se si e' sopra il limite specificato dalla policy
		}
	}

	public void setNeighbors(ArrayList<IaasAgent> neighbors) {
		this.neighbors = neighbors;
	}

	public void setSupernodes(ArrayList<IaasAgent> supernodes) {
		this.supernodes = supernodes;
	}
	
	
}
