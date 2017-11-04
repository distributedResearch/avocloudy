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
 * Logging of the main features for Application nodes in the network
 * 
 * @author Stefano Sebastio
 *
 */
public class AppNodeLog extends Event {

	public AppNodeLog(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
	}

	public void run() throws RunException {
		
		AutomatorLogger al = new AutomatorLogger();
		ArrayList<LoggerObject> fileValue = new ArrayList<LoggerObject>();
		
		NodeList nodes = new NodeList();
		System.out.println("totAppNodes: " + nodes.getAppAgentList().size());
		fileValue.add(new LoggerObject("totAppNodes", nodes.getAppAgentList().size()));
		System.out.println("totAppAgents: " + nodes.getAppAgentList().size());
		fileValue.add(new LoggerObject("totAppAgents", nodes.getAppAgentList().size()));
		
		al.write(Engine.getDefault().getVirtualTime(), fileValue);

	}

}
