package it.imtlucca.cloudyscience.autonomicLayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.imtlucca.cloudyscience.applicationLayer.AppAgent;
import it.imtlucca.cloudyscience.applicationLayer.AppNodeFeature;
import it.imtlucca.cloudyscience.infrastructureLayer.CloudSiteComparator;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgentDataCenter;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgentVolunteer;
import it.imtlucca.cloudyscience.physicalLayer.Hardware;
import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.Event;

/**
 * Remake of the PDP'13 paper simulator
 * 
 * @author Stefano Sebastio
 *
 */
public class AcaasNodeBehavior extends AbstractAcaasNodeBehavior {

	public AcaasNodeBehavior(AbstractAcaasNodeKnowledge knwoledge,
			AbstractAcaasNodePolicy policy) {
		super(knwoledge, policy);
	}

	/**
	 * Manage application according the PDP'13 paper. 
	 * It is considered as an own application.
	 * 
	 * "public void ownApplication(Application app)" of PDP'13 simulator
	 */
	public void manageApplication(AppAgent app) {
	
		//System.out.println("ManageApplication");
		
	//	AcaasNodeFeature acaasFeature = (AcaasNodeFeature)(this.getKnowledge().getFeature()); 
		
		
		this.updateRequestCounter(app);
		
		if (((AcaasNodePolicy)this.getPolicy()).askingToVolunteer()){
			if (this.searchExecutingNode(app))
				return;
			else {
				this.updateMissCounter(app);
			//	System.out.println("Miss");
				
				//TODO: non si considera il check to expand sul datacenter
				
				
			}
		}
		else{
			//the isOnline() check is added to consider a node that want to gracefully disconnect from the network and assign to other nodes its application charge
			if (this.getReferringAgent().getReferringAgent().getFeature().isOnline() && this.getReferringAgent().getReferringAgent().getBehavior().estimateTaskAcceptance(app, this.getReferringAgent())){
				
				//double execTime = this.getReferringAgent().getReferringAgent().getBehavior().estimateTaskExit(app);
				//this.getReferringAgent().getFeature().addAppOnQueue(app, execTime);
				//The task is directly send in the node's queue since the 
				notifyNewTask(app);
				
			} else {
				this.updateMissCounter(app);
				
				
				//TODO: non si considera il check to expand sul datacenter
			}
		}
		
	}
	
	
	/**
	 * Search for an executing node
	 * 
	 * "public boolean appVolunteer(Application app)" of PDP'13 simulator
	 */
	public boolean searchExecutingNode(AppAgent a){
		
		boolean nodeFound = false;
		
		// Ask: 1) to itself 2) to neighbors 3) to supernodes (to not overload the supernodes)
		
		
		// 1) Ask to itself if it is offline (for migration case)
		//System.out.println("AppDeadline " + a.getFeature().getDeadline());
		//System.out.println("nodeFreeing " + this.getReferringAgent().getFeature().getFinishEstimation());
		if (this.getReferringAgent().getReferringAgent().getFeature().isOnline() && (this.getReferringAgent().getFeature().getFinishEstimation() < a.getFeature().getDeadline()) ){
			
			nodeFound = this.appExecReq(a, this.getReferringAgent());
			
			if (nodeFound)
				return nodeFound;
			
			
		} 
		//else { 
		// ask to other nodes 
			
			//XXX: la lista dovrebbe essere di AcaaS e non IaaS
		
			// 2) Ask to neighbors or
			// 3) Ask to Supernodes (the supernodes aren't the ones that are contacted first since they are already busy with other stuff)
			ArrayList<IaasAgent> nodes = this.getReferringAgent().getReferringAgent().getKnowledge().getNeighbors();
			//FIXME: l'aggiunta alla lista tutte le volte che occorre fare la richiesta rallenta notevolmente la simulazione !!!
			//XXX: adesso si chiede soltanto ai propri vicini (stessa zona)
			//////nodes.addAll(this.askNeighborToSupernodes(this.orderSupernodesAccordingSite()));
			//TODO: chiedere ad ogni supernodo la lista dei vicini
			
			//FIXME: tolto per rifare PDP'13 
			//nodes.addAll(this.getReferringAgent().getReferringAgent().getKnowledge().getSupernodes());
		/**	System.out.print("Node IaaS Id " + this.getReferringAgent().getReferringAgent());// + " and ACaaS " + this.getReferringAgent());
			System.out.println("   In own zone " + this.getReferringAgent().getReferringAgent().getFeature().getZoneOfBelonging() + " known neighbor " + nodes.size() + " supernodes " + this.getReferringAgent().getReferringAgent().getKnowledge().getSupernodes().size());
			
			System.out.println("neighbor list " + nodes);
			**/
			// 2) Ask to neighbors
			nodeFound = this.askExecutionToNodeList(nodes, a); 
			if (nodeFound)
				return nodeFound;
			//System.out.println("Known neighbor " + nodes.size() + " in own zone " + this.getReferringAgent().getReferringAgent().getFeature().getZoneOfBelonging() + " supernodes " + this.getReferringAgent().getReferringAgent().getKnowledge().getSupernodes().size());
			//FIXME: dopo aver chiesto ai vicini si dovrebbe contattare il supernodo di zona per chiedere a tutti gli altri nodi della stessa zona e poi passare ad altre zone
			
			/*for (IaasAgent n : nodes) {
				
				if (n.getAcAgent().getFeature().getFinishEstimation() < a.getFeature().getDeadline()){
					
					//nodeFound = ((AcaasNodeBehavior)n.getAcAgent().getBehavior()).appExecReq(a, n.getAcAgent());
					nodeFound = ((AcaasNodeBehavior)n.getAcAgent().getBehavior()).appExecReq(a, this.getReferringAgent());
					
					a.getKnowledge().incReqCounter();
					
					if (nodeFound)
						return nodeFound;
				}
				
			}*/
			
			// 3) Ask to supernodes that will provide a list with the nodes in other zones
			//TODO: chiedere una lista aggiornata dei supernodi al proprio responsabile
			//XXX: altrimenti si rischia di avere una visione solo parziale dei site che esistono nella rete 
			//this.getReferringAgent().getReferringAgent().getBehavior().updateSupernodesList();
			//XXX: vedere come ripristinarlo perche' altrimenti si esegue un sovraccarico sul nodo per ogni app. Deve essere eseguito come funzione di manutenzione della rete periodicamente
			//TODO: importante da fixare
			ArrayList<IaasAgent> supernodes = this.orderSupernodesAccordingSite();
			//FIXME: controllare che non si riesegua la richiesta ai nodi nella propria zona se il primo supernodo e' il supernodo responsabile della zona
			for (IaasAgent s : supernodes){
				
				//retrieve a list of nodes in the supdenode's zone according their distance  
				ArrayList<IaasAgent> neighbors = this.askNeighborToSupernode(s); // no need to shuffle since the list from supernode is just retrieved shuffled
				nodeFound = this.askExecutionToNodeList(neighbors, a);
				
				if (nodeFound)
					return nodeFound;
				/*for (IaasAgent n : neighbors){
					if (n.getAcAgent().getFeature().getFinishEstimation() < a.getFeature().getDeadline()){
						
						//nodeFound = ((AcaasNodeBehavior)n.getAcAgent().getBehavior()).appExecReq(a, n.getAcAgent());
						nodeFound = ((AcaasNodeBehavior)n.getAcAgent().getBehavior()).appExecReq(a, this.getReferringAgent());
						
						a.getKnowledge().incReqCounter();
						
						if (nodeFound)
							return nodeFound;
					}
				}*/
			}
			
			
		//}
		
		return nodeFound;
		
		
		//TODO: non considerato un costo di trasmissione tra le zone a differenza di pdp'13

		
	}
	
	/**
	 * Ask to a node list if there is someone that can execute the application 
	 * 
	 * @param nodes
	 * @return
	 */
	protected boolean askExecutionToNodeList(List<IaasAgent> nodes, AppAgent a){
		boolean nodeFound = false;
		
		//TODO: inserire un counter dei null ricevuti. Se la cache e' molto degradata occorre ricontattare il supernodo responsabile per aggiornarla
		//FIXME: il check dovrebbe essere fatto su tutte le ACaaS sopra la IaaS. Potrebbero esserci piu' VM nella stessa macchina fisica
			for (IaasAgent n : nodes){
				if (a.getKnowledge().getReqCounter() < ( (AcaasNodePolicy)this.getPolicy()).getMaxNumOfAttempt()){
				//if (n.getAcAgent().getFeature().getFinishEstimation() < a.getFeature().getDeadline()){
				//System.out.println("To contact " + n.getAcAgent());
				Float resp = this.requestFinishEstimation(n.getAcAgent());
				//System.out.println("REsp " + resp);
				//if (resp == null)
				//	System.out.println("req to an offline node");
				if (resp != null) {
					//if (this.requestFinishEstimation(n.getAcAgent()) < a.getFeature().getDeadline()){
					//TODO: controllare se corretto tolto se non si vuole valutare il volunteer
					//if (resp < a.getFeature().getDeadline()){
						//System.out.println("Req num " + a.getKnowledge().getReqCounter() + " for app " + a.getFeature().getAppId());
						//nodeFound = ((AcaasNodeBehavior)n.getAcAgent().getBehavior()).appExecReq(a, n.getAcAgent());
						nodeFound = ((AcaasNodeBehavior)n.getAcAgent().getBehavior()).appExecReq(a, this.getReferringAgent());
						
						a.getKnowledge().incReqCounter();
						
						if (nodeFound)
							return nodeFound;
					//}
				}
			}
		}
		
		return nodeFound;
		
	}
	
	/**
	 * Request to another ACaasAgent (the requesting) its finishing estimation time to satisfy prior reqests.
	 * It does NOT consider the new application
	 * 
	 * @param requesting
	 * @return
	 */
	public Float requestFinishEstimation(AcaasAgent requesting) {
		//System.out.println("Requesting " + requesting);
		
		//System.out.println("responce " + requesting.askFinishEstimation());
		//if(requesting.askFinishEstimation() == null)
			//System.out.println("answer with null ");
		//TODO: trasformare in messaggio cosi' da poter inserire dei ritardi per la richiesta
		return requesting.askFinishEstimation();
	}
	
	/**
	 * Evaluates requests to application execution according the PDP'13 paper.
	 * 
	 * "public boolean appRequest(Application app, boolean remote, ScienceNode n)" of PDP'13 simulator
	 */
	public boolean appExecReq(AppAgent app, AcaasAgent sourceAgent){
		//TODO:
		
		boolean acceptReq = false;
		//System.out.println("nodeId " + this.getReferringAgent().getReferringAgent().getFeature().getNodeId());
		boolean remoteReq = (sourceAgent != this.getReferringAgent());
		//System.out.println("isRemote=" + remoteReq + " from sourceAgent " + sourceAgent + " to " + this.getReferringAgent());
		
		acceptReq = this.getReferringAgent().getReferringAgent().getBehavior().estimateTaskAcceptance(app, sourceAgent)
				&& ( (!remoteReq) || (((AcaasNodeFeature)this.getKnowledge().getFeature()).actualMissRate() < ((AcaasNodePolicy)this.getPolicy()).getMissRateTolerance()) );
		
		
		if(acceptReq){
			//double execTime = this.getReferringAgent().getReferringAgent().getBehavior().estimateTaskExit(app);
			//this.getReferringAgent().getFeature().addAppOnQueue(app, execTime);
			((AcaasNodeBehavior)sourceAgent.getBehavior()).sendTaskForExecution(app, this.getReferringAgent());
			//08/06/2013 removed since the task are send through an event
			//notifyNewTask(app);
			
			
			if (remoteReq){
				//this is a remote request
				((AcaasNodeFeature)this.getKnowledge().getFeature()).incRemoteReqAccepted();
				//XXX: add req is removed since it is never used
				// Update the world history
				//((AcaasNodeKnowledge) this.getKnowledge()).addReqRecord(sourceAgent.getAcaasId(), this.getReferringAgent().getAcaasId());
			} 
			
		} else {
			if (!remoteReq)
				((AcaasNodeFeature)this.getKnowledge().getFeature()).incRemoteReqRefused();
		}
		
		return acceptReq;
	}
	
	
	
	
	public void updateRequestCounter(AppAgent a){
		AcaasNodeFeature acaasFeature = (AcaasNodeFeature) this.getKnowledge().getFeature();
		
		acaasFeature.incReq();
		
		
		String appType = ((AppNodeFeature) a.getFeature()).getTaskType();
		acaasFeature.incReqReceived(appType);
		
	}
	
	public void updateMissCounter(AppAgent a){
		AcaasNodeFeature acaasFeature = (AcaasNodeFeature) this.getKnowledge().getFeature();
		
		acaasFeature.incUnmetCounter();
		acaasFeature.getHistoryMissHit().add(0.0);
		
		//System.out.println("Miss for " + a.getFeature().getAppId() + " from node " + this.getReferringAgent().getReferringAgent().getFeature().getNodeId());
		
		//TODO: senza il cambio di stato del task ad aborted
		
		String appType = ((AppNodeFeature) a.getFeature()).getTaskType();
		acaasFeature.incReqUnmet(appType);
		
	}
	
	public void updateHitCounter(AppAgent a){
		AcaasNodeFeature acaasFeature = (AcaasNodeFeature) this.getKnowledge().getFeature();
		
		acaasFeature.incHitCounter();
		acaasFeature.getHistoryMissHit().add(1.0);
		
		String appType = ((AppNodeFeature) a.getFeature()).getTaskType();
		acaasFeature.incReqSatisfied(appType);

		this.updateSojournTimeInfo(a);
	}
	
	/**
	 * Update the information about the 'Waiting time' when an Application starts its execution
	 * The 'Waiting time' is defined as the time that the Application 'spend' before that its execution starts.
	 * 
	 * If an application is restarted (after a migration) the list will contain differents waiting time one for each application restart. 
	 * Every starting from app generation and not from its reboot  
	 * @param a
	 */ 
	public void updateWaitingTimeInfo(AppAgent a){
		AcaasNodeFeature acaasFeature = (AcaasNodeFeature) this.getKnowledge().getFeature();
		
		//acaasFeature.incWaitingCounter();
		String appType = ((AppNodeFeature) a.getFeature()).getTaskType();
		
		float appWaiting = a.getFeature().getStartTime() - a.getFeature().getGenerationTime();
//		if (appWaiting > 100 || a.getFeature().getAppId() == 822342405){
//			System.out.println("appInit " + a.getFeature().getGenerationTime() + " appStart " + a.getFeature().getStartTime() + " dead " + a.getFeature().getDeadline() + " getDuration " + a.getFeature().getDuration() + " appId " + a.getFeature().getAppId());
//			System.out.println("currentTime " + Engine.getDefault().getVirtualTime());
//			System.out.println("nodeExec " + this.getReferringAgent().getReferringAgent().getFeature().getNodeId());//360015279
//			System.out.println("appWaiting " + appWaiting);//822342405
//			
//		}
		acaasFeature.addWaitingTime(appType, appWaiting);
		
	}
	
	/**
	 * Update the information about the 'Sojourn time' when an Application left the cloud (since it have completed its execution).
	 * The 'sojourn time' is defined as the time that the Application 'spend' in the cloud
	 * @param a
	 */
	public void updateSojournTimeInfo(AppAgent a){
		AcaasNodeFeature acaasFeature = (AcaasNodeFeature) this.getKnowledge().getFeature();
		
		//acaasFeature.incSojournCounter();
		String appType = ((AppNodeFeature) a.getFeature()).getTaskType();
		
		float appSojourn = a.getFeature().getFinishTime() - a.getFeature().getGenerationTime();
		//System.out.println("finishTime " + a.getFeature().getFinishTime());
		//System.out.println("generationTime " + a.getFeature().getGenerationTime());
		//System.out.println("currentTime " + Engine.getDefault().getVirtualTime());
		/*if (appSojourn < 1250){
			System.out.println("AAAA");
			
			System.out.println("finishTime " + a.getFeature().getFinishTime());
			System.out.println("generationTime " + a.getFeature().getGenerationTime());
			System.out.println("currentTime " + Engine.getDefault().getVirtualTime());
		}*/
	/*	if (appSojourn < a.getFeature().getDuration()){
			System.out.println("TriggerTime: " + a.getFeature().getFinishTime() + " where now " + Engine.getDefault().getVirtualTime() + " needed time " + appSojourn);	
			System.out.println("for app " + a.getFeature().getAppId());
		}
		*/	
		acaasFeature.addSojournTime(appType, appSojourn);

	}
	
	
	/**
	 * Notify the referring IaaS Node the presence of a new task on queue.
	 * 
	 * An event is raised to model the task transmission
	 * 
	 * @param app
	 */
	public void notifyNewTask(AppAgent app){
		// this is done according the real duration of the application on the current archiecture and not with a simple estimation
		//double execTime = this.getReferringAgent().getReferringAgent().getBehavior().estimateTaskExit(app);
		//double execTime = this.getReferringAgent().getReferringAgent().getBehavior().evaluateTaskExit(app);
		int execTime = this.getReferringAgent().getReferringAgent().getBehavior().evaluateTaskDuration(app);
		this.getReferringAgent().getFeature().addAppOnQueue(app, execTime);
		//System.out.println("currentTime " + Engine.getDefault().getVirtualTime() + " ueue " + this.getReferringAgent().getFeature().getQueueLength());
		//notify the VM scheduler on IaaS the presence of a new task on queue.
		//every time that a new task is added on queue the application scheduler on IaasNodeBehavior is called to evaluate the possibility to execute the app
		this.getReferringAgent().getReferringAgent().getBehavior().applicationScheduler();
	}
	
	/**
	 * Generate an event to send a task with the network model
	 * 
	 * If the task doesn't have an associated size info the task is istantanly send to destination
	 * 
	 */
	public void sendTaskForExecution(AppAgent app, AcaasAgent dest){
		if ( (app.getFeature().getSize() != -1) && (this.getReferringAgent() != dest )){

			IaasAgent externalIaas = dest.getReferringAgent();
			Hardware externalPoint = null;
			if (externalIaas instanceof IaasAgentDataCenter){
				externalPoint = ((IaasAgentDataCenter) externalIaas).getDcAgent().getHws().get(0);
			} else if (externalIaas instanceof IaasAgentVolunteer){
				externalPoint = ((IaasAgentVolunteer) externalIaas).getHardwareResources();
			}
			
			float triggerTime = Engine.getDefault().getVirtualTime();
			IaasAgent internalIaas = this.getReferringAgent().getReferringAgent();
			if (internalIaas instanceof IaasAgentDataCenter){
				//FIXME: considerare il caso in cui si hanno piu' hw e non prendere il primo
				triggerTime += (float) ((IaasAgentDataCenter) internalIaas).getDcAgent().getHws().get(0).evaluateOverheadTime(externalPoint, app);
			} else if (internalIaas instanceof IaasAgentVolunteer){
				triggerTime += (float) ((IaasAgentVolunteer) internalIaas).getHardwareResources().evaluateOverheadTime(externalPoint, app);
			}
			if (triggerTime == Engine.getDefault().getVirtualTime()) {//to prevent the creation of an event that will be executed instantly
				((AcaasNodeBehavior)dest.getBehavior()).notifyNewTask(app);
			}
			else {
				
				Event e = Engine.getDefault().createEvent("sendAppForExec", triggerTime );
				//System.out.println("sendAppForExec " + triggerTime + " current " + Engine.getDefault().getVirtualTime());
				SendTaskToExec sendTaskEvent = (SendTaskToExec) e;
				sendTaskEvent.setAcaasAgent(dest);
				sendTaskEvent.setAppAgent(app);
				
				Engine.getDefault().insertIntoEventsList(sendTaskEvent);
			}
		} else {
			
			((AcaasNodeBehavior)dest.getBehavior()).notifyNewTask(app);
		}
	}
	
	
	/**
	 * Order the supernodes according the sites' distance
	 */
	public ArrayList<IaasAgent> orderSupernodesAccordingSite(){
		ArrayList<IaasAgent> supernodes = this.getReferringAgent().getReferringAgent().getKnowledge().getSupernodes();
	//	System.out.println("supenodes known " + supernodes.size());
	//	System.out.println(supernodes);
		Collections.sort(supernodes, new CloudSiteComparator());
	//	System.out.println("post " + supernodes);
		return supernodes;
	}
	
	/**
	 * Get a list of nodes in each zone through the supernode responsible
	 * 
	 * @param supernodes
	 * @return
	 */
	public ArrayList<IaasAgent> askNeighborToSupernodes(ArrayList<IaasAgent> supernodes){
		ArrayList<IaasAgent> nodeList = new ArrayList<IaasAgent>();
		
		for (IaasAgent s : supernodes){
			//nodeList.add(s);
			//nodeList.addAll(s.getBehavior().getNNeighbor(-1)); // to obtain all the nodes in the supernode zone
			nodeList.addAll(this.askNeighborToSupernode(s));
		//	System.out.println("neighbors in a zone " + s.getBehavior().getNNeighbor(-1).size());
		}
		
		return nodeList;
		
	}
	
	/**
	 * Get a list of nodes in the supdenode's zone. 
	 * - - - (REPLACED) The supernode add itself to the list as last node to not be overloaded with other work
	 * 
	 * @param supernode
	 * @return
	 */
	public ArrayList<IaasAgent> askNeighborToSupernode(IaasAgent supernode){
		ArrayList<IaasAgent> neighbor = supernode.getBehavior().getNNeighbor(-1);
		// to prevent the risk of contact everytimes the node in the same order (and thus overload always the same nodes)
		Collections.shuffle(neighbor, Engine.getDefault().getSimulationRandom());
	//	neighbor.add(supernode);
		
		return neighbor;
	}
}
