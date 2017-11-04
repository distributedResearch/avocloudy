package it.imtlucca.aco.autonomicLayer;

import java.util.HashMap;

import it.imtlucca.aco.Pheromone;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeFeature;
//import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeKnowledge;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodeKnowledge;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;

/**
 * Keeps a trace of pheromone widespread in the network
 * 
 * @author Stefano Sebastio
 *
 */
public class AcaasAcoNodeKnowledge extends AcaasNodeKnowledge {//extends AbstractAcaasNodeKnowledge {

	private HashMap<IaasAgent, Pheromone> pheromonePath;
	
	public AcaasAcoNodeKnowledge(AbstractAcaasNodeFeature feature) {
		super(feature);
		
		this.pheromonePath = new HashMap<IaasAgent, Pheromone>();
	}
	
	
	public void addPheromone(Pheromone p){
		this.pheromonePath.put(p.getNeighbor(), p);
	}


	public HashMap<IaasAgent, Pheromone> getPheromonePath() {
		return this.pheromonePath;
	}

	public Pheromone getPheromone(IaasAgent a){
		return this.pheromonePath.get(a);
	}

	
}
