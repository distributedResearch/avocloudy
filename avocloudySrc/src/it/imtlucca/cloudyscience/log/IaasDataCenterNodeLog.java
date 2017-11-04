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
 * Logging of the main features for DataCenter nodes in the network 
 * 
 * @author Stefano Sebastio
 *
 */
public class IaasDataCenterNodeLog extends Event {

	public IaasDataCenterNodeLog(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
	}


	public void run() throws RunException {

		AutomatorLogger al = new AutomatorLogger();
		ArrayList<LoggerObject> fileValue = new ArrayList<LoggerObject>();
		
		
		//retrieve the list with all DataCenter Nodes
		NodeList nodes = new NodeList();
		System.out.println("totIaasDcNodes: " + nodes.getIaasDcAgentList().size());
		fileValue.add(new LoggerObject("totIaasDcNodes", nodes.getIaasDcAgentList().size()));
		
		
		al.write(Engine.getDefault().getVirtualTime(), fileValue);

	}

}
