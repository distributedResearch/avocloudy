package it.imtlucca.spatialaco.autonomicLayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import it.imtlucca.aco.AntColor;
import it.imtlucca.aco.ColoredAnt;
import it.imtlucca.aco.autonomicLayer.AcaasAcoNodeBehavior;
import it.imtlucca.aco.autonomicLayer.AcaasAcoNodeKnowledge;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeKnowledge;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodePolicy;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;
import it.unipr.ce.dsg.deus.core.Engine;

/**
 * ACaaS layer according the Spatial-ACO model load balancing
 * 
 * @author Stefano Sebastio
 *
 */
public class AcaasSpatialAcoNodeBehavior extends AcaasAcoNodeBehavior {
	
	public AcaasSpatialAcoNodeBehavior(AbstractAcaasNodeKnowledge knwoledge,
			AbstractAcaasNodePolicy policy) {
		super(knwoledge, policy);
	}

	//TODO: e' la funzione che si occupa di distribuire periodicamente le info
	public void initPeriodicPulse(){
		Set<IaasAgent> neighbors = ((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromonePath().keySet();
		ArrayList<IaasAgent> neighborsList = new ArrayList<IaasAgent>(neighbors);
		//FIXME: fare la richiesta ad uno solo dei vicini !!!
		
		//for (IaasAgent n : neighbors){
			Collections.shuffle(neighborsList, Engine.getDefault().getSimulationRandom());
			IaasAgent n = neighborsList.get(0);
			
			ArrayList<Double> neighborPheromoneCpu = new ArrayList<Double>();
			ArrayList<Double> neighborPheromoneFreq = new ArrayList<Double>();
			ArrayList<Double> neighborPheromoneMem = new ArrayList<Double>();
			ArrayList<Double> neighborPheromoneFT = new ArrayList<Double>();
			
			for ( IaasAgent p : neighbors){
				if (p!= n){ 
					//double pheronome = ((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromonePath().get(p).getPheromone();
					double pheronomeCpu = ((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromone(p).getColoredPheromone(this.getPolicy(), AntColor.CPU_CORE);
					neighborPheromoneCpu.add(pheronomeCpu);
					
					double pheronomeFreq = ((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromone(p).getColoredPheromone(this.getPolicy(), AntColor.CPU_FREQ);
					neighborPheromoneFreq.add(pheronomeFreq);

					double pheronomeMem = ((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromone(p).getColoredPheromone(this.getPolicy(), AntColor.MAIN_MEMORY);
					neighborPheromoneMem.add(pheronomeMem);

					double pheronomeFT = ((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromone(p).getColoredPheromone(this.getPolicy(), AntColor.FINISHING_TIME);
					neighborPheromoneFT.add(pheronomeFT);					
					//double cpuPheromone = (((AcaasAcoNodeKnowledge) p.getAcAgent().getKnowledge()).getPheromone(n).getColoredPheromone(this.getPolicy(), AntColor.CPU_CORE));
				}
			}
			//FIXME: le info di se stesso devono considerare il fatto che i vicini deono risultare con uno step di distanza (discount dato dallo step). Altrimenti tutti convergono allo stesso livello del feronome massimo
			//TODO: aggiungere le info di se stesso !!!
			//ant.addStep(this.getReferringAgent().getReferringAgent(), this.getRequestedInfo(ant.getAntColor()));
			double thisCpu = this.getRequestedInfo(AntColor.CPU_CORE);
			neighborPheromoneCpu.add(thisCpu);
			double thisFreq = this.getRequestedInfo(AntColor.CPU_FREQ);
			neighborPheromoneFreq.add(thisFreq);
			double thisMem = this.getRequestedInfo(AntColor.MAIN_MEMORY);
			neighborPheromoneMem.add(thisMem);
			double thisFT = this.getRequestedInfo(AntColor.FINISHING_TIME);
			neighborPheromoneFreq.add(thisFT);
			
			double maxPheronomeCpu = Collections.max(neighborPheromoneCpu);
			ColoredAnt ant = this.getColoredAnt(AntColor.CPU_CORE);
			ant.addStep(this.getReferringAgent().getReferringAgent(), maxPheronomeCpu);
			((AcaasAcoNodeBehavior)n.getAcAgent().getBehavior()).depositColoredPheromone(ant, this.getReferringAgent().getReferringAgent(), 1);
			//TODO: forse e' necessario resettare tutta la memoria del path della formica
			double maxPheronomeFreq = Collections.max(neighborPheromoneFreq);
			ant = this.getColoredAnt(AntColor.CPU_FREQ);
			ant.addStep(this.getReferringAgent().getReferringAgent(), maxPheronomeFreq);
			((AcaasAcoNodeBehavior)n.getAcAgent().getBehavior()).depositColoredPheromone(ant, this.getReferringAgent().getReferringAgent(), 1);
			double maxPheronomeMem = Collections.max(neighborPheromoneMem);
			ant = this.getColoredAnt(AntColor.MAIN_MEMORY);
			ant.addStep(this.getReferringAgent().getReferringAgent(), maxPheronomeMem);
			((AcaasAcoNodeBehavior)n.getAcAgent().getBehavior()).depositColoredPheromone(ant, this.getReferringAgent().getReferringAgent(), 1);
			double maxPheronomeFT = Collections.max(neighborPheromoneFT);
			ant = this.getColoredAnt(AntColor.FINISHING_TIME);
			ant.addStep(this.getReferringAgent().getReferringAgent(), maxPheronomeFT);
			((AcaasAcoNodeBehavior)n.getAcAgent().getBehavior()).depositColoredPheromone(ant, this.getReferringAgent().getReferringAgent(), 1);
		//}

	}
}
