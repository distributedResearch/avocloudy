package it.imtlucca.cloudyscience.infrastructureLayer;

import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;

import java.util.Properties;

/**
 * The event that throw a reconnection action of a IaaS on the network
 * 
 * @author Stefano Sebastio
 *
 */
public class IaasNodeReconnectionEvent extends
		AbstractIaasNodeReconnectionEvent {

	public IaasNodeReconnectionEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
		onlyForAssociatedNode = true;
	}

}
