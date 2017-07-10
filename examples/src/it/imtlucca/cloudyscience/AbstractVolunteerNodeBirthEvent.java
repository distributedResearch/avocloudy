package it.imtlucca.cloudyscience;

import java.util.Properties;

import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;
import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.NodeEvent;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

/**
 * Abstract class for the birth of a new Physical Voluneer Node
 * 
 * It has an optional method to specify the ACaaS model that must be used
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractVolunteerNodeBirthEvent extends NodeEvent {

	
	public AbstractVolunteerNodeBirthEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
		
	}

	public void run() throws RunException {
		
		
		if(getParentProcess() == null)
			throw new RunException("A parent process must be set in order to run " + getClass().getCanonicalName());
		
		//System.out.println("getParentProcess().getReferencedNodes().size " + getParentProcess().getReferencedNodes().size());
		//System.out.println("getParentProcess().getReferencedNodes() " + getParentProcess().getReferencedNodes().get(0));
		//to handle the case where there is more than one nodes type associated to the event. It is chosen uniformly random
		Node n = (Node) getParentProcess().getReferencedNodes().get(Engine.getDefault().getSimulationRandom().nextInt(getParentProcess().getReferencedNodes().size())).createInstance(Engine.getDefault().generateKey());
		//System.out.println("Node " + n.getClass());
		
		Engine.getDefault().getNodes().add(n);
		associatedNode = n;
		VolunteerNode v = (VolunteerNode) n;
		
		
		v.getIaasAgent().setAcaasAgentTemplate(this.specifyAcaasNode(v));
		//call the connect action
		v.getIaasAgent().getBehavior().connect();
		
		this.postConnectionActions(v);

	}

	/**
	 * The function to define the associated ACaaS layer 
	 * 
	 * @param v
	 * @return
	 */
	protected AcaasAgent specifyAcaasNode(VolunteerNode v){
		return null;
	}
	
	/**
	 * Actions to be performed after a new Iaas node connect to the network
	 * 
	 * @param v
	 */
	protected void postConnectionActions(VolunteerNode v){
	}
	
}
