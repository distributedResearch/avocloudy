package it.imtlucca.cloudyscience;

import java.util.Properties;

import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.NodeEvent;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

/**
 * 
 * The birth of a new Physical DataCenter Node
 * 
 * @author Stefano Sebastio
 *
 */
public class DataCenterNodeBirthEvent extends NodeEvent {

	
	public DataCenterNodeBirthEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
	}

	
	public void run() throws RunException {

		if(getParentProcess() == null)
			throw new RunException("A parent process must be set in order to run " + getClass().getCanonicalName());
		
		//System.out.println("getParentProcess().getReferencedNodes().size " + getParentProcess().getReferencedNodes().size());
		//System.out.println("getParentProcess().getReferencedNodes() " + getParentProcess().getReferencedNodes().get(0));
		Node n = (Node) getParentProcess().getReferencedNodes().get(Engine.getDefault().getSimulationRandom().nextInt(getParentProcess().getReferencedNodes().size())).createInstance(Engine.getDefault().generateKey());
		//System.out.println("Node " + n);
		
		//if (n instanceof DataCenterNode)
		//	System.out.println("AAAAAA");
		
		Engine.getDefault().getNodes().add(n);
		this.setAssociatedNode(n);
		//associatedNode = n;
		//System.out.println("associated by birth " + associatedNode);
		//System.out.println("n " + n);
		//System.out.println("...DataCenter was born at " + Engine.getDefault().getVirtualTime());
		
		//System.out.println("DataCenterNode called...");
		//AbstractCloudyNode node = (AbstractCloudyNode) n;
		//System.out.println("type - " + node.physicalFeature.getTypeOfDevice());
	}

}
