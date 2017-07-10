package it.imtlucca.cloudyscience.infrastructureLayer.dc;

import java.util.Properties;

import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;

/**
 * The event that throw a check for the need of startup another IaaS Node
 * 
 * @author Stefano Sebastio
 *
 */
public class DataCenterNodeStartupCheckEvent extends AbstractDataCenterNodeStartupCheckEvent {

	public DataCenterNodeStartupCheckEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);

	}


}
