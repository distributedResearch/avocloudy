package it.imtlucca.cloudyscience.infrastructureLayer.dc;

import java.util.ArrayList;
import java.util.Properties;

import it.imtlucca.cloudyscience.DataCenterNode;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.NodeEvent;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

/**
 * Abstract class to define the check of startup another IaaS node.
 * It is possible to assign a customized ACaaS behavior
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractDataCenterNodeStartupCheckEvent extends NodeEvent {

	public AbstractDataCenterNodeStartupCheckEvent(String id,
			Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
	}
	
	public void run() throws RunException {
		
		if (getParentProcess() == null)
			throw new RunException("A parent process must be set in order to run " + getClass().getCanonicalName());
		
		if (getAssociatedNode() != null){
			DataCenterNode dcNode = (DataCenterNode) getAssociatedNode();
			//System.out.println("call " + getAssociatedNode());
			dcNode.getIaasDCAgent().getBehavior().setAcaasAgentTemplate(this.specifyAcaasNode(dcNode));
			//System.out.println("dcNode " + dcNode);
			//System.out.println("dcNode AgentDC " + dcNode.getIaasDCAgent());
			ArrayList<IaasAgent> managedAgents = dcNode.getIaasDCAgent().getIaasAgents();
			dcNode.getIaasDCAgent().getBehavior().manageStartup(managedAgents);
			
			this.postDcStartup(dcNode);
			//System.out.println("Start check process Called...");
		} else {
			//FIXME: gestire il caso in cui si ha un nodo associato
			//System.out.println("Check startup event associated to a DataCenter node");
		}
	}

	
	/**
	 * Provides the templates needed by the ACaaS layer node
	 * 
	 * @param dc
	 * @return
	 */
	protected AcaasAgent specifyAcaasNode(DataCenterNode dc){
		return null;
	}
	
	/**
	 * Actions to be performed after a DataCenter starts a new VM
	 * 
	 * @param d
	 */
	protected void postDcStartup(DataCenterNode d){
	}

}
