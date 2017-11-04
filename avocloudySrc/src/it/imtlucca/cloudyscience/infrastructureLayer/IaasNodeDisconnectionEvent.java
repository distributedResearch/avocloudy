package it.imtlucca.cloudyscience.infrastructureLayer;

import java.util.Properties;

import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;

/**
 * The event that throw a disconnection action of a IaaS on the Network
 * 
 * @author Stefano Sebastio
 *
 */
public class IaasNodeDisconnectionEvent extends AbstractIaasNodeDisconnectionEvent {

	
	public IaasNodeDisconnectionEvent(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
	
	}


}
