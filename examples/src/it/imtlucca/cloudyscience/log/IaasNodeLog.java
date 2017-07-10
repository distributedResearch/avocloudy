package it.imtlucca.cloudyscience.log;

import java.util.ArrayList;
import java.util.Properties;

import it.imtlucca.cloudyscience.util.NodeList;
import it.unipr.ce.dsg.deus.automator.AutomatorLogger;
import it.unipr.ce.dsg.deus.automator.LoggerObject;
import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

/**
 * Logging of the main features for IaaS nodes in the network
 * 
 * @author Stefano Sebastio
 *
 */
public class IaasNodeLog extends Event {

	public IaasNodeLog(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
	}


	public void run() throws RunException {

		AutomatorLogger al = new AutomatorLogger();
		ArrayList<LoggerObject> fileValue = new ArrayList<LoggerObject>();
		
		NodeList nodes = new NodeList();
		
		
		System.out.println("totIaasNodes: " + nodes.getIaasAgentList().size());
		fileValue.add(new LoggerObject("totIaasNodes", nodes.getIaasAgentList().size()));
		
		al.write(Engine.getDefault().getVirtualTime(), fileValue);
		
	}

}
