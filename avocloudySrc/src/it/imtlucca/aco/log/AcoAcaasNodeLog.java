package it.imtlucca.aco.log;

import java.util.ArrayList;
import java.util.Properties;

import it.imtlucca.aco.ColoredAnt;
import it.imtlucca.aco.autonomicLayer.AcaasAcoNodeBehavior;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;
import it.imtlucca.cloudyscience.log.AcaasNodeLog;
import it.imtlucca.cloudyscience.util.NodeList;
import it.unipr.ce.dsg.deus.automator.AutomatorLogger;
import it.unipr.ce.dsg.deus.automator.LoggerObject;
import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

/**
 * Log the unique characteristics of ant approach model
 * 
 * 
 * @author Stefano Sebastio
 *
 */
public class AcoAcaasNodeLog extends AcaasNodeLog {

	public AcoAcaasNodeLog(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);		
	}
	
	
	public void run() throws RunException {
		
		super.run();
		
		AutomatorLogger al = new AutomatorLogger();
		ArrayList<LoggerObject> fileValue = new ArrayList<LoggerObject>();
		
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
		fileValue.add(new LoggerObject("colored_steps_per_node", (overallColoredSteps/acaasAgents.size())));
		al.write(Engine.getDefault().getVirtualTime(), fileValue);
	}
	

}
