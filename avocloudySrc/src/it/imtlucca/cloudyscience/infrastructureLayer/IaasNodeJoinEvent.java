package it.imtlucca.cloudyscience.infrastructureLayer;

import java.util.ArrayList;
import java.util.Properties;

import it.imtlucca.cloudyscience.VolunteerNode;
import it.imtlucca.cloudyscience.util.NodeList;
import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.NodeEvent;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

/**
 * The event that throw a join action of a IaaS on the Network
 * 
 * @author Stefano Sebastio
 *
 */
public class IaasNodeJoinEvent extends NodeEvent {

	
	public IaasNodeJoinEvent(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
	
	}

	public void run() throws RunException {
		if (getParentProcess() == null)
			throw new RunException("A parent process must be set in order to run " + getClass().getCanonicalName());
		
		
		if (getAssociatedNode() == null){
			//take a new node
			NodeList nodeList = new NodeList();
			ArrayList<VolunteerNode> nodes = nodeList.getOnlineVolunteerNodes();
		
			VolunteerNode node = nodes.get(Engine.getDefault().getSimulationRandom().nextInt(nodes.size()));
			((IaasNodeBehavior)node.getIaasAgent().getBehavior()).join();
			//System.out.println("randomly selected for join " + node + " at " + Engine.getDefault().getVirtualTime());
		}
		//FIXME: il join dev'essere fatto dal nodo IaaS non dal Volunteer
		else if (getAssociatedNode() instanceof VolunteerNode){
			//System.out.println("....");
			VolunteerNode node = (VolunteerNode) getAssociatedNode();
			((IaasNodeBehavior)node.getIaasAgent().getBehavior()).join();
		}
		
		//TODO: eseguire l'azione di join
	//	System.out.println("join Event called----");

	}

}
