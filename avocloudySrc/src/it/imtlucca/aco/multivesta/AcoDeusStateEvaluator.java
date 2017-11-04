package it.imtlucca.aco.multivesta;

import java.util.ArrayList;

import vesta.mc.NewState;

import it.imtlucca.aco.ColoredAnt;
import it.imtlucca.aco.autonomicLayer.AcaasAcoNodeBehavior;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;
import it.imtlucca.cloudyscience.multivesta.CloudyScienceDeusStateEvaluator;
import it.imtlucca.cloudyscience.util.NodeList;

/**
 * Provide evaluation of colored ant steps
 * 
 * @author Stefano Sebastio
 *
 */
public class AcoDeusStateEvaluator extends CloudyScienceDeusStateEvaluator {

	public double getVal(int which, NewState engine) {
		
		if (which == 30){
			NodeList nodes = new NodeList();
			
			ArrayList<AcaasAgent> acaasAgents = nodes.getAcaasAgentList();
	
			//Log the steps done by the 
			ArrayList<Integer> coloredAntSteps = new ArrayList<Integer>();
			
			for (AcaasAgent a : acaasAgents){
				ArrayList<ColoredAnt> ants = ((AcaasAcoNodeBehavior)a.getBehavior()).getColoredAnts();
				for (int i = 0; i < ants.size(); i++){
					if (i < coloredAntSteps.size())
						coloredAntSteps.set(i, coloredAntSteps.get(i)+ants.get(i).getnHop());
					else
						coloredAntSteps.add(ants.get(i).getnHop());
				}
			}
			
			int overallColoredSteps = 0;
			
			for (int s : coloredAntSteps){
				overallColoredSteps += s;
				System.out.println("Colored steps of different colours " + s);
			}
		
			System.out.println("colored_steps_per_node " + (overallColoredSteps/acaasAgents.size()));
			return (overallColoredSteps/acaasAgents.size());
		}
		return super.getVal(which, engine);
	}
	
}
