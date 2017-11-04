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
 * Logging of the main features for the physical nodes in the network
 * 
 * @author Stefano Sebastio
 *
 */
public class PhysicalNodeLog extends Event {

	public PhysicalNodeLog(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
	}


	public void run() throws RunException {

		AutomatorLogger al = new AutomatorLogger();
		ArrayList<LoggerObject> fileValue = new ArrayList<LoggerObject>();
		
		NodeList nodes = new NodeList();
		
		//ArrayList<AbstractCloudyNode> physNodes = nodes.getCloudyNodes();
		System.out.println("totPhysNodes: " + nodes.getCloudyNodes().size());
		fileValue.add(new LoggerObject("totPhysNodes", nodes.getCloudyNodes().size()));
		System.out.println("totPhysDataCenter: " + nodes.getDataCenterNodes().size());
		fileValue.add(new LoggerObject("totPhysDataCenter", nodes.getDataCenterNodes().size()));
		System.out.println("totPhysVolunteer: " + nodes.getVolunteerNodes().size());
		fileValue.add(new LoggerObject("totPhysVolunteer", nodes.getVolunteerNodes().size()));
		
		
		
		
		
		
		al.write(Engine.getDefault().getVirtualTime(), fileValue);
		
	}
	
}
