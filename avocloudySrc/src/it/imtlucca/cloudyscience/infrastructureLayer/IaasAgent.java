package it.imtlucca.cloudyscience.infrastructureLayer;

import it.imtlucca.cloudyscience.AbstractCloudyNode;
import it.imtlucca.cloudyscience.DataCenterNode;
import it.imtlucca.cloudyscience.VolunteerNode;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;
import it.imtlucca.cloudyscience.autonomicLayer.AppCriteria;
import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.Node;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The IaaS Agent constitute link with the external world for the IaaS node
 * 
 * @author Stefano Sebastio
 *
 */
public class IaasAgent {


	private AbstractIaasNodeBehavior behavior = null;
	private AbstractIaasNodeKnowledge knowledge = null;
	private AbstractIaasNodePolicy policy = null;
	private AbstractIaasNodeFeature feature = null;
	
	//private boolean online;
	
	private AppCriteria appCriteria = null;
	
	private AcaasAgent acAgent = null;
	
	//it is an optional parameter to define the ACaasAgent to be used
	//TODO: non completamente modulare
	private AcaasAgent acaasAgentTemplate = null;
	
	
	public IaasAgent(AbstractIaasNodeBehavior behavior,
			AbstractIaasNodeKnowledge knowledge, AbstractIaasNodePolicy policy,
			AbstractIaasNodeFeature feature) {
		super();
		this.behavior = behavior;
		this.knowledge = knowledge;
		this.policy = policy;
		this.feature = feature;
		
		//this.online = true;
	}

	
	
	/**
	 * If it returns null no ACaaS template is defined and the default one is used 
	 * 
	 * @return
	 */
	public AcaasAgent getAcaasTemplate() {
		return acaasAgentTemplate;
	}

	public void setAcaasAgentTemplate(AcaasAgent acaas) {
		this.acaasAgentTemplate = acaas;
	}




	public AbstractIaasNodeBehavior getBehavior() {
		return behavior;
	}


	public AbstractIaasNodeKnowledge getKnowledge() {
		return knowledge;
	}


	public AbstractIaasNodePolicy getPolicy() {
		return policy;
	}


	public AbstractIaasNodeFeature getFeature() {
		return feature;
	}
	/*
	public void setOnline(){
		this.online = true;
	}
	
	public boolean isOnline(){
		return this.online;
	}
	
	public void setOffline(){
		this.online = false;
	}
	*/
	
	//TODO: da spostare anche queste in NodeList. Forse no dato che fornisce soltanto quelli online
	/**
	 * Retrieve a list with all "online" IaaS nodes that are in the network
	 * (since it gives only the online nodes a check is performed prior to complete the list)
	 */
	public ArrayList<IaasAgent> getAllIaasNodes(){
		ArrayList<IaasAgent> nodes = new ArrayList<IaasAgent>();
		for (Iterator<Node> it = Engine.getDefault().getNodes().iterator(); it.hasNext();){
			Object elem = it.next();
			
			if (elem instanceof AbstractCloudyNode) {
				
				if (elem instanceof VolunteerNode){
					IaasAgent vn = ((VolunteerNode)elem).getIaasAgent();
					//if (vn.isOnline())
					if (vn.getKnowledge().getFeature().isOnline())
						nodes.add(vn);
				
				} else if (elem instanceof DataCenterNode) {
					ArrayList<IaasAgent> al = ((DataCenterNode)elem).getIaasDCAgent().getIaasAgents();
					
					for (IaasAgent a : al){
						//if (a.isOnline())
						if (a.getKnowledge().getFeature().isOnline())
							nodes.add(a);
					}
					
				}
			}
		}
		
		return nodes;
	}
	
	/**
	 * Gives a list with all "online" IaaS supernodes
	 * (since it gives only the online nodes a check is performed prior to complete the list)
	 * 
	 * @return
	 */
	public ArrayList<IaasAgent> getIaasSuperNodes(){
		ArrayList<IaasAgent> as = new ArrayList<IaasAgent>();
		
		for (IaasAgent a : this.getAllIaasNodes()){
			if (a.getFeature().isSupernode())
				as.add(a);
		}
		
		return as;
	}
	
	
	/**
	 * Gives a list of all "online" IaaS nodes in a given zoneId 
	 * (since it gives only the online nodes a check is performed prior to complete the list)
	 * 
	 * @param zoneId the zone identifier used as a filter over all Iaas nodes 
	 */
	public ArrayList<IaasAgent> getAllIaasNodesInZone(int zoneId){
		ArrayList<IaasAgent> az = new ArrayList<IaasAgent>();
		
		for (IaasAgent a : this.getAllIaasNodes()){
			if (a.getFeature().getZoneOfBelonging() == zoneId)
				az.add(a);
		}
		
		return az;
	}
	
	/**
	 * Gives a list with all "online" supernodes in a given zone.
	 * (since it gives only the online nodes a check is performed prior to complete the list)
	 * 
	 * @param zoneId
	 * @return
	 */
	public ArrayList<IaasAgent> getAllSuperNodesInZone( int zoneId){
		ArrayList<IaasAgent> sz = new ArrayList<IaasAgent>();
		
		for (IaasAgent a : this.getAllIaasNodesInZone(zoneId)){
			if (a.getFeature().isSupernode()){
				sz.add(a);
			}
		}
		
		return sz;
	}


	
	
	
	public AppCriteria getAppCriteria() {
		return appCriteria;
	}


	public void setAppCriteria(AppCriteria appCriteria) {
		this.appCriteria = appCriteria;
	}


	public AcaasAgent getAcAgent() {
		return acAgent;
	}


	public void setAcAgent(AcaasAgent acAgent) {
		this.acAgent = acAgent;
	}

	
	/**
	 * Receive a notification of a new neighbor
	 */
	public void newNeighborNotify(IaasAgent a){
		if (this.getAcAgent() != null){
			this.getAcAgent().overlayAddNeighborNotification(a);
		}
	}
	//For a node of another zone
	public void newNodeNotify(IaasAgent a){
		if (this.getAcAgent() != null){
			this.getAcAgent().overlayAddNodeNotification(a);
		}
	}
	
}
