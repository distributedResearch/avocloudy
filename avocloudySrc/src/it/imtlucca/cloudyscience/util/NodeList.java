package it.imtlucca.cloudyscience.util;

import it.imtlucca.cloudyscience.AbstractCloudyNode;
import it.imtlucca.cloudyscience.DataCenterNode;
import it.imtlucca.cloudyscience.VolunteerNode;
import it.imtlucca.cloudyscience.applicationLayer.AppAgent;
import it.imtlucca.cloudyscience.applicationLayer.Application;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;
import it.imtlucca.cloudyscience.infrastructureLayer.dc.IaasDCAgent;
import it.imtlucca.cloudyscience.physicalLayer.PhysicalNodeFeature;
import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.Node;

import java.util.ArrayList;

/**
 * Facility class to obtain the informations about Nodes in the simulator. 
 * 
 * @author Stefano Sebastio
 *
 */
public class NodeList {
//TODO: ottimizare con l'uso di cache. Altrimenti si contiunuano ad allocare arraylist facendo esplodere la memoria !!!!
	/**
	 * Time at which the informations are created
	 **/
	private float timeVT;
	

	public NodeList() {
		super();
		this.timeVT = Engine.getDefault().getVirtualTime();
	}
	
	
	
	
	public float getTimeVT() {
		return timeVT;
	}




	/**
	 * Obtain a list with all Cloud Physical Node
	 * 
	 * @return
	 */
	public ArrayList<AbstractCloudyNode> getCloudyNodes(){
		//System.out.println("getNodes called in Log");
		ArrayList<AbstractCloudyNode> node = new ArrayList<AbstractCloudyNode>();
		//for (Iterator<Node> it = Engine.getDefault().getNodes().iterator(); it.hasNext();){
			//Object elem = it.next();
		for (Node elem : Engine.getDefault().getNodes()){
			
			if (elem instanceof AbstractCloudyNode){
				node.add((AbstractCloudyNode)elem);
			}
			
		}
		
		return node;
	}
	
	public ArrayList<AbstractCloudyNode> getNodeInLanId(int lanId){
		ArrayList<AbstractCloudyNode> nodes = this.getCloudyNodes();
		ArrayList<AbstractCloudyNode> inSameLan = new ArrayList<AbstractCloudyNode>();
		for (AbstractCloudyNode n : nodes){
			if (n.getHw().getHwFeature() instanceof PhysicalNodeFeature){
				PhysicalNodeFeature nodeFeature = (PhysicalNodeFeature) n.getHw().getHwFeature();
				if (nodeFeature.getLanId() == lanId){
					inSameLan.add(n);
				}
			}
		}
		return inSameLan;
	}


	/***
	 * Provides a list with all Data Center nodes in the network
	 * 
	 * @return
	 */
	public ArrayList<DataCenterNode> getDataCenterNodes(){
		
		ArrayList<AbstractCloudyNode> node = this.getCloudyNodes();
		ArrayList<DataCenterNode> dc = new ArrayList<DataCenterNode>();
		
		for (AbstractCloudyNode n : node) {
			
			if (n instanceof DataCenterNode){
				dc.add((DataCenterNode)n);
			}
			
		}
		
		return dc;
		
	}
	
	/**
	 * Provides a list with all Volunteer nodes in the network
	 * 
	 * @return
	 */
	public ArrayList<VolunteerNode> getVolunteerNodes(){
		
		ArrayList<AbstractCloudyNode> node = this.getCloudyNodes();
		ArrayList<VolunteerNode> v = new ArrayList<VolunteerNode>();
		
		for (AbstractCloudyNode n : node) {	
			
			if (n instanceof VolunteerNode){
				v.add((VolunteerNode)n);
			}
			
		}
		
		return v;
		
	}
	
	
	/**
	 * Provides a list with all "online" Volunteer nodes in the network
	 * 
	 * @return
	 */
	public ArrayList<VolunteerNode> getOnlineVolunteerNodes(){
		
		ArrayList<VolunteerNode> node = this.getVolunteerNodes();
		ArrayList<VolunteerNode> v = new ArrayList<VolunteerNode>();
		
		for (VolunteerNode n : node) {	
			
			if (n.getIaasAgent().getFeature().isOnline()){
				v.add(n);
			}
			
		}
		
		return v;
		
	}
	
	/**
	 * Provides a list with all "offline" Volunteer nodes in the network
	 * 
	 * @return
	 */
	public ArrayList<VolunteerNode> getOfflineVolunteerNodes(){
		
		ArrayList<VolunteerNode> node = this.getVolunteerNodes();
		ArrayList<VolunteerNode> v = new ArrayList<VolunteerNode>();
		
		for (VolunteerNode n : node) {	
			
			if (!n.getIaasAgent().getFeature().isOnline()){
				v.add(n);
			}
			
		}
		
		return v;
		
	}

	/**
	 * Provides a list with all IaasDCAgents in the network
	 * 
	 * @return
	 */
	public ArrayList<IaasDCAgent> getIaasDcAgentList(){
	
		ArrayList<AbstractCloudyNode> nodes = this.getCloudyNodes();
		ArrayList<IaasDCAgent> iaasDcAgents = new ArrayList<IaasDCAgent>();
	
		for (AbstractCloudyNode cloudy : nodes){
			if (cloudy instanceof DataCenterNode){
				iaasDcAgents.add(((DataCenterNode)cloudy).getIaasDCAgent());
			}
		}
		
		return iaasDcAgents;
	}
	
	
	/**
	 * Provides a list with all IaasAgents in the network
	 * 
	 * @return
	 */
	public ArrayList<IaasAgent> getIaasAgentList() {
		
		ArrayList<IaasAgent> iaasAgents = new ArrayList<IaasAgent>();
		
		ArrayList<AbstractCloudyNode> nodes = this.getCloudyNodes();
		
		//for (Iterator<Node> it = Engine.getDefault().getNodes().iterator(); it.hasNext();){
		for(AbstractCloudyNode cloudy : nodes){	
			
			if (cloudy instanceof VolunteerNode){
				iaasAgents.add(((VolunteerNode) cloudy).getIaasAgent());
			}
			
			else if (cloudy instanceof DataCenterNode){
				IaasDCAgent dcAgent = ((DataCenterNode) cloudy).getIaasDCAgent();
				
				for (IaasAgent a : dcAgent.getIaasAgents()){
					iaasAgents.add(a);
				}
				
			}
				
		}
	//	System.out.println("There are " + node.size() + " scienceNode at VT: " + Engine.getDefault().getVirtualTime());
		
		return iaasAgents;
	}
	
	/**
	 * Provides a list with all AcaasAgents in the network
	 * 
	 * @return
	 */
	public ArrayList<AcaasAgent> getAcaasAgentList(){
		
		ArrayList<IaasAgent> iaasAgents = this.getIaasAgentList();
		ArrayList<AcaasAgent> acaasAgents = new ArrayList<AcaasAgent>();
		
		for (IaasAgent agent : iaasAgents){
			
			acaasAgents.add(agent.getAcAgent());
			
		}
		
		return acaasAgents;
	}
	
	
	/**
	 * Provides a list with all "Online" ACaaSAgents in the network 
	 */
	public ArrayList<AcaasAgent> getOnlineAcaasAgentList(){
		ArrayList<AcaasAgent> acaasAgents = this.getAcaasAgentList();
		ArrayList<AcaasAgent> onlineAcaasAgents = new ArrayList<AcaasAgent>();
		
		for (AcaasAgent a : acaasAgents){
			
			if (a.getReferringAgent().getFeature().isOnline()){
				onlineAcaasAgents.add(a);
			}
			
		}
		
		return onlineAcaasAgents;
	}
	
	/**
	 * Obtain a list with all Application Nodes
	 * 
	 * @return
	 */
	public ArrayList<Application> getAppNodes(){
		//System.out.println("getNodes called in Log");
		ArrayList<Application> node = new ArrayList<Application>();
		//for (Iterator<Node> it = Engine.getDefault().getNodes().iterator(); it.hasNext();){
			//Object elem = it.next();
		for (Node elem : Engine.getDefault().getNodes()){
			
			if (elem instanceof Application){
				node.add((Application)elem);
			}
			
		}
		
		return node;
	}
	
	/**
	 * Obtain a list with all AppAgents in the network
	 * 
	 * @return
	 */
	public ArrayList<AppAgent> getAppAgentList(){
		
		ArrayList<AppAgent> appAgents = new ArrayList<AppAgent>();
		ArrayList<Application> app = this.getAppNodes();
		
		for (Application a : app){
			
			appAgents.add(a.getAppAgent());
		}
		
		return appAgents;
	}
	
}
