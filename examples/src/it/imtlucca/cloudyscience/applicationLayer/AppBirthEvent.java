package it.imtlucca.cloudyscience.applicationLayer;

import java.util.ArrayList;
import java.util.Properties;

import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;
import it.imtlucca.cloudyscience.util.NodeList;
import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

/**
 * Generation of a new Application.
 * The supporting ACaaS node is chosen randomly
 * 
 * @author Stefano Sebastio
 *
 */
public class AppBirthEvent extends Event {

	public AppBirthEvent(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
		
	}

	public void run() throws RunException {

		if (getParentProcess() == null)
			throw new RunException("A parent process must be set in order to run "
					+ getClass().getCanonicalName());
		
		
		Node n = (Node) getParentProcess().getReferencedNodes().get(
				Engine.getDefault().getSimulationRandom().nextInt(
						getParentProcess().getReferencedNodes().size()))
				.createInstance(Engine.getDefault().generateKey());
		
		Application app = (Application) n;
		((AppNodeFeature)(app.getAppAgent().getFeature())).setTaskType(app.getId());
		
		AcaasAgent a = getRandomAcaasNode();
		if (a != null)
			a.setNewApp(app.getAppAgent());
		else
			System.out.println("AppBirth Aborted...");
	}

	
	/**
	 * Search for ACaaS nodes candidates as Application source.
	 * 
	 * @return the ACaaS Node used as contact
	 */
	public AcaasAgent getRandomAcaasNode(){

		NodeList nodeList = new NodeList();
		ArrayList<AcaasAgent> nodes = nodeList.getOnlineAcaasAgentList();
		
		if (nodes.size() == 0){
			System.out.println("NO ACaaS Node available");
			return null;
		}
		
		//int i = (int) (Engine.getDefault().getSimulationRandom().nextDouble()*nodes.size());
		int i = Engine.getDefault().getSimulationRandom().nextInt(nodes.size());

		return nodes.get(i);
	}
}
