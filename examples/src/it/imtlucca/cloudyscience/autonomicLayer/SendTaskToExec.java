package it.imtlucca.cloudyscience.autonomicLayer;

import java.util.Properties;

import it.imtlucca.cloudyscience.applicationLayer.AppAgent;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

/**
 * 
 * Send a task to be executed by a remote node. It is just accepted by the remote node.
 * The event model the network overhead for task transmission
 * 
 * @author Stefano Sebastio
 *
 */
public class SendTaskToExec extends Event {

	
	private AcaasAgent acaasAgent = null;
	private AppAgent appAgent = null;
	
	public SendTaskToExec(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);

	}

	
	
	public AcaasAgent getAcaasAgent() {
		return acaasAgent;
	}



	public AppAgent getAppAgent() {
		return appAgent;
	}



	public void setAcaasAgent(AcaasAgent acaasAgent) {
		this.acaasAgent = acaasAgent;
	}



	public void setAppAgent(AppAgent appAgent) {
		this.appAgent = appAgent;
	}



	public void run() throws RunException {
		((AcaasNodeBehavior)this.acaasAgent.getBehavior()).notifyNewTask(this.appAgent);

	}

}
