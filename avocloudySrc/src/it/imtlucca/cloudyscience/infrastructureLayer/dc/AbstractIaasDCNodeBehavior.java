package it.imtlucca.cloudyscience.infrastructureLayer.dc;

import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;
import it.imtlucca.cloudyscience.infrastructureLayer.AbstractIaasNodeBehavior;
import it.imtlucca.cloudyscience.infrastructureLayer.AbstractIaasNodeFeature;
import it.imtlucca.cloudyscience.infrastructureLayer.AbstractIaasNodeKnowledge;
import it.imtlucca.cloudyscience.infrastructureLayer.AbstractIaasNodePolicy;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgentDataCenter;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasNodeBehavior;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasNodeFeature;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasNodeKnowledge;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasNodePolicy;
import it.unipr.ce.dsg.deus.core.Engine;

import java.util.ArrayList;

/**
 * The behavior that takes decisions according to Knowledge and Policies at Data Center level.
 * The communication with the external world to update knowledge or act on a IaaS Node managed
 * by a IaaS DC is done through the IaasDC Agent.
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractIaasDCNodeBehavior {

	private AbstractIaasDCNodeKnowledge knwoledge;
	private AbstractIaasDCNodePolicy policy;
	
	private IaasDCAgent referringAgent;
	
	private AcaasAgent acaasAgentTemplate = null;
	
	/**
	 * Reasons about the Startup of another IaaS Node on top of the current DC
	 * @param nodes
	 */
	//FIXME: aggiungere altri criteri secondo cui lanciare altri IaasS Node (adesso ne viene creato max uno per nodo dc)
	public void manageStartup(ArrayList<IaasAgent> nodes){
		//start a IaaS Node if there are no Node on top of a DC
		if (nodes == null || nodes.size() == 0){
			
			// Create the structures needed to an IaaS Agent
			AbstractIaasNodePolicy policy = new IaasNodePolicy(referringAgent.getNetCriteria().isRandomZoneAssignement(), referringAgent.getNetCriteria().getnOfSupernodeToConnect(), referringAgent.getNetCriteria().isRandomConnectionInit(), 
					referringAgent.getNetCriteria().getnOfConnectionInit(), referringAgent.getNetCriteria().getnOfConnectionOngoing(), referringAgent.getNetCriteria().isConnectionAmongZonesInit(), 
					referringAgent.getNetCriteria().isConnectionAmongZonesOngoing());
			
			if (!referringAgent.getNetCriteria().isRandomZoneAssignement())
				policy.setZoneAssignement(referringAgent.getNetCriteria().getZoneAssigned());
			
			
			
			int coreNumber = (int)Math.ceil(referringAgent.getHws().get(0).getHwFeature().getCoreNumber()*referringAgent.getVmCriteria().getQuoteOfPhysToVm()/100);
			float coreFreq = (float)(referringAgent.getHws().get(0).getHwFeature().getCoreFreq()*referringAgent.getVmCriteria().getQuoteOfPhysToVm()/100);
			int mainMemory = (int)(referringAgent.getHws().get(0).getHwFeature().getMainMemory()*referringAgent.getVmCriteria().getQuoteOfPhysToVm()/100);
			int nodeId = Engine.getDefault().generateResourceKey();
			AbstractIaasNodeFeature feature = new IaasNodeFeature(coreNumber, coreFreq, mainMemory, nodeId);
			feature.setStabilityFactor(referringAgent.getVmCriteria().getStabilityFactor());
			AbstractIaasNodeKnowledge knowledge = new IaasNodeKnowledge(referringAgent.getNetCriteria().getnOfRegionalZone(), feature);
			((IaasNodeKnowledge) knowledge).setOverheadForRemoteSite(referringAgent.getNetCriteria().getOverheadForRemoteSite());
			AbstractIaasNodeBehavior behavior = new IaasNodeBehavior(knowledge, policy);
			
			//IaasAgentDataCenter a = new IaasAgentDataCenter(behavior, knowledge, policy, feature, referringAgent);
			IaasAgent a = new IaasAgentDataCenter(behavior, knowledge, policy, feature, referringAgent);
			behavior.setReferringAgent(a);
			knowledge.setReferringAgent(a);
			
			if (this.acaasAgentTemplate != null){
				//System.out.println("set acaas as template");
				this.acaasAgentTemplate.setReferringAgent(a);
				a.setAcaasAgentTemplate(this.acaasAgentTemplate);
			}
			
			a.setAppCriteria(referringAgent.getAppCriteria());
			
			// add it to the list of IaaS nodes on top of a IaaS DC node
			referringAgent.addIaasAgent(a);
			// make it online
			referringAgent.getIaasAgents().get(0).getBehavior().connect();
			
			//System.out.println("ManageStartup on DC");
		}
	}
	

	public AcaasAgent getAcaasAgentTemplate() {
		return acaasAgentTemplate;
	}


	public void setAcaasAgentTemplate(AcaasAgent acaasAgentTemplate) {
		this.acaasAgentTemplate = acaasAgentTemplate;
	}






	/**
	 * Reasons about the Shutdown of a IaaS Node on top of the current DC
	 * @param nodes
	 */
	public void manageShutdown(ArrayList<IaasAgent> nodes){
		
	}
	
	/**
	 * Reasons about the Resize of a IaaS Node on top of the current DC
	 * @param nodes
	 */
	public void manageResize(ArrayList<IaasAgent> nodes){
		
	}

	
	public AbstractIaasDCNodeKnowledge getKnwoledge() {
		return knwoledge;
	}

	public AbstractIaasDCNodePolicy getPolicy() {
		return policy;
	}
	
	public IaasDCAgent getAgent(){
		return referringAgent;
	}

	public AbstractIaasDCNodeBehavior(AbstractIaasDCNodeKnowledge knwoledge,
			AbstractIaasDCNodePolicy policy, IaasDCAgent agent) {
		super();
		this.knwoledge = knwoledge;
		this.policy = policy;
		this.referringAgent = agent;
	}
	
	public AbstractIaasDCNodeBehavior(AbstractIaasDCNodeKnowledge knwoledge,
			AbstractIaasDCNodePolicy policy) {
		super();
		this.knwoledge = knwoledge;
		this.policy = policy;
	}

	public void setKnwoledge(AbstractIaasDCNodeKnowledge knwoledge) {
		this.knwoledge = knwoledge;
	}

	public void setPolicy(AbstractIaasDCNodePolicy policy) {
		this.policy = policy;
	}

	public void setAgent(IaasDCAgent agent){
		this.referringAgent = agent;
	}
	
	
}
