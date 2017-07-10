package it.imtlucca.cloudyscience.infrastructureLayer;

import java.util.Properties;

import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;

/**
 * The event that throw a death action of a IaaS on the Network
 * 
 * @author Stefano Sebastio
 *
 */
public class IaasNodeDeathEvent extends AbstractIaasNodeDeathEvent {

	
	public IaasNodeDeathEvent(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
		onlyForAssociatedNode = true;
	}

}
