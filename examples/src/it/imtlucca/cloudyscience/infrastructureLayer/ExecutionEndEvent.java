package it.imtlucca.cloudyscience.infrastructureLayer;

import java.util.Properties;

import it.imtlucca.cloudyscience.applicationLayer.AppAgent;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

/**
 * General structure for the 'execution end' of an Application.
 * It is associated with the IaaS Agent that has executed the application.
 * 
 * @author Stefano Sebastio
 *
 */
public class ExecutionEndEvent extends Event {

	private IaasAgent iaasAgent = null;
	private AppAgent appAgent = null; // the reference to the executing app
	
	public ExecutionEndEvent(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
	}


	public IaasAgent getIaasAgent() {
		return iaasAgent;
	}


	public void setIaasAgent(IaasAgent iaasAgent) {
		this.iaasAgent = iaasAgent;
	}

	public AppAgent getAppAgent() {
		return appAgent;
	}


	public void setAppAgent(AppAgent appAgent) {
		this.appAgent = appAgent;
	}


	public void run() throws RunException {

		this.iaasAgent.getBehavior().appExecutionEnded(this.appAgent);
		
	}

}
