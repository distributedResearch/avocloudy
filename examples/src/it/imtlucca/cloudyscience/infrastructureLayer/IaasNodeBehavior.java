package it.imtlucca.cloudyscience.infrastructureLayer;

import java.util.ArrayList;

import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeBehavior;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeFeature;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeKnowledge;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodePolicy;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodeBehavior;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodeFeature;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodeKnowledge;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodePolicy;
import it.unipr.ce.dsg.deus.core.Engine;

/**
 * Main duties of the IaaS node behavior.
 * It manages the overlay network formed among the IaaS nodes
 * It start the ACaaS node.
 * 
 * @author Stefano Sebastio
 *
 */
public class IaasNodeBehavior extends AbstractIaasNodeBehavior {

	public IaasNodeBehavior(AbstractIaasNodeKnowledge knwoledge,
			AbstractIaasNodePolicy policy, IaasAgent referringAgent) {
		super(knwoledge, policy, referringAgent);
	}
	
	public IaasNodeBehavior(AbstractIaasNodeKnowledge knwoledge,
			AbstractIaasNodePolicy policy) {
		super(knwoledge, policy);
	}

	/**
	 * Join of the IaaS node to the overlay network
	 */
	public void join(){
		//System.out.println("Join function called");
		//System.out.println("Supernode " + this.getKnowledge().getSupernodeResponsible());
		//System.out.println("SupernodeS " + this.getKnowledge().getSupernodeResponsible());
		//System.out.println("Neighbors " + this.getKnowledge().getNeighbors());
		this.getKnowledge().getFeature().goOnline();
		//if the cache is not clean then it is a re-connection
		if ( (this.getKnowledge().getSupernodeResponsible() != null) || 
				( (this.getKnowledge().getNeighbors() != null) && (this.getKnowledge().getNeighbors().size() != 0) ) || 
				( (this.getKnowledge().getSupernodes() != null) && (this.getKnowledge().getSupernodes().size() != 0) ) ) {
			//System.out.println("Re-connection");
			
			
			// 1) try to find the current supernode responsible
			IaasAgent supernodeResp = this.getReferringAgent().getKnowledge().getSupernodeResponsible();
			//check if it has already a supernode in the cache and if it is different from itself. Then it should still be online and be a supernode for the same zone  
			if ( (supernodeResp != null) && (supernodeResp != this.getReferringAgent()) && 
					(supernodeResp.getFeature().isOnline()) && (supernodeResp.getFeature().isSupernode()) && 
					(supernodeResp.getFeature().getZoneOfBelonging() == this.getKnowledge().getFeature().getZoneOfBelonging()) ){
				
				
				this.updateCacheAfterReconnection(supernodeResp);
				//System.out.println("again connected");
				
				
			} else { //try to obtain update info form neighbors or other supernodes. Since is was a previous supernode responsible for the zone 
				IaasAgent newSupernode = null;
				for (IaasAgent n :  this.getKnowledge().getNeighbors()){
					if ( n.getFeature().isOnline() && (n.getFeature().getZoneOfBelonging() == this.getKnowledge().getFeature().getZoneOfBelonging()) ){
						IaasAgent candidateSupernode = n.getKnowledge().getSupernodeResponsible(); 
						
						if (candidateSupernode.getKnowledge().getFeature().isOnline() 
								&& (candidateSupernode.getKnowledge().getFeature().getZoneOfBelonging() == this.getKnowledge().getFeature().getZoneOfBelonging()) ){
							
							newSupernode = candidateSupernode;
							break;
						}
						 
					}
				}
				if (newSupernode != null){
					this.getKnowledge().setSupernodeResponsible(newSupernode);
					this.updateCacheAfterReconnection(supernodeResp);
					//System.out.println("again connected");
				}
				else { 
					//TODO: try to obtain info from another supernode. First to act connecting to a bootstrap 
					
					//empty all the cache and execute a new bootstrap
					this.getReferringAgent().getKnowledge().clearOverlayKnowledge();

					this.join();
					return;
					
				} 
				
				
				
			}
			
			
		}
		else { // This is the first connection to the network (no cache info available)

			//check if there are no other supernodes in this zone
			int zone = this.getKnowledge().getFeature().getZoneOfBelonging();
		//	System.out.println("get RegerringAgent " + this.getReferringAgent());
			//System.out.println("get AllIaasSuperNodesInZone " + this.getReferringAgent().getAllSuperNodesInZone(zone));
			
			// New supernode case
			if (this.getReferringAgent().getAllSuperNodesInZone(zone).size() == 0){ // the retrieved supernodes list contains only the online nodes
				//System.out.println("No supernode in the current zone");
				
				//set itself as supernode for the zone
				this.getKnowledge().getFeature().setAsSupernode();
				// is referring agent of itself
				this.getKnowledge().setSupernodeResponsible(this.getReferringAgent());
	
				
				//retrieve a list with all supernodes and then notify itself. Every supernode must know all other supernodes
				this.getKnowledge().addSupernodes(this.getReferringAgent().getIaasSuperNodes());
				//this.getKnowledge().addUpToNSupernodes(this.getReferringAgent().getIaasSuperNodes(), this.getReferringAgent().getPolicy().getnSupernodeAtInit()); // no perche' i supernodi devono conoscersi tutti tra loro
				// notify itself to other supernodes as a colleague
				this.signalPresenceAsSupernodeToNodes(this.getKnowledge().getSupernodes(), this.getReferringAgent());
				//in this manner every supernodes doesn't have itself in the supernodes list
				
				
				// retrieve the list with all nodes in the zone. It should be 0 since it is setted up as Supernode  
				//this.getKnowledge().addNeighbors(this.getReferringAgent().getAllIaasNodesInZone(zone)); // tolto altrimenti un nodo e' vicino di se stesso
				//System.out.println("neighbor in a zone without a supernode " + this.getKnowledge().getNeighbors().size());
				
			//	System.out.println("neighbors in a new zone " + this.getReferringAgent().getAllIaasNodesInZone(zone).size());
				
				//FIXME: tolto altrimenti i vicini non sono soltanto quelli nella stessa zona
				/*
				// ask to other supernodes until reach at least 'n' neighbor (if are available)
				if (this.getKnowledge().getNeighbors().size() < this.getPolicy().getnConnectionInit() || this.getPolicy().getnConnectionInit() ==-1){
					ArrayList<IaasAgent> sl = this.getKnowledge().getSupernodes();
					for(IaasAgent s : sl){
						this.getKnowledge().addNeighbors(s.getKnowledge().getNeighbors());
						
						if (this.getKnowledge().getNeighbors().size() >= this.getPolicy().getnConnectionInit() && this.getPolicy().getnConnectionInit() !=-1)
							break;
					}
				}*/
				//System.out.println("neighbors acquired " + this.getKnowledge().getNeighbors().size() + " by " + this);
				//FIXME: controllare se e' inutile. E' un nuovo supernodo quindi non ha vicini (stessa zona)
				// notify itself to other neighbors nodes
				this.signalPresenceToNodes(this.getKnowledge().getNeighbors(), this.getReferringAgent());
				
				
			} else { // there is already a supernode in the current zone
				
				// set supernode as responsible
				IaasAgent s = this.getReferringAgent().getAllSuperNodesInZone(zone).get(0);
				this.getKnowledge().setSupernodeResponsible(s);
				s.getBehavior().signalPresence(this.getReferringAgent());
				
				
				// ask for a list of supernodes (and notify to them //XXX: tolto il 130408)
				ArrayList<IaasAgent> sl = s.getBehavior().getNSupernodes(this.getPolicy().getnSupernodeAtInit());
				if (sl == null)
					System.out.println("supernodeList empty");
	//			System.out.println("known other supernodes " + sl.size());
				this.getKnowledge().addSupernodes(sl);
				//XXX: tolto il 130408: non necessario che tutti i nodi si segnalino a tutti i supernodi 
				//this.signalSupernodePresenceToPeers(this.getReferringAgent());// to inform the other supernodes about the presence of a new node in another zone
				
				// ask for a list of neighbor and notify to them 
				ArrayList<IaasAgent> nl = s.getBehavior().getNNeighbor(this.getPolicy().getnConnectionInit());
				this.getKnowledge().addNeighbors(nl);
				//System.out.println("neighbors for zone " + this.getKnowledge().getNeighbors().size());
				// notify itself to other neighbors nodes
				this.signalPresenceToNodes(this.getKnowledge().getNeighbors(), this.getReferringAgent());
			}
			/**
			System.out.print("Node IaaS Id " + this.getReferringAgent());
			System.out.println("   Neighbor in zone " + this.getKnowledge().getFeature().getZoneOfBelonging() + " are " + this.getKnowledge().getNeighbors().size() + " where supernodes are " + this.getKnowledge().getSupernodes().size());
			**/
	
		}
		// if there is not a ACaaS Node generate a new one
		if (this.getReferringAgent().getAcAgent() == null){
			this.generateAcaasNode();
			//System.out.println(" ACaaS " + this.getReferringAgent().getAcAgent());
		}
		
	}
	
	
	/**
	 * Selects a zone and join to the network
	 */
	public void connect(){
		this.getKnowledge().getFeature().goOnline();
		//System.out.println("Concrete connect called...");
		
		int zone;
		if (this.getPolicy().isRandomZoneAssignemnt()){
			zone = (int) (Engine.getDefault().getSimulationRandom().nextDouble()*(this.getKnowledge().getNumberOfRegionalZones()));
			//System.out.println("zone " + zone);
			
			//System.out.println("Zone " + this.getKnowledge().getFeature().getZoneOfBelonging() + " setted...");
		} else {
			zone = this.getReferringAgent().getPolicy().getZoneAssignement();
			
		}
		this.getKnowledge().getFeature().setZoneOfBelonging(zone);
		this.join();
	}
	
	/**
	 * Regular disconnection from the network.
	 * It inform other nodes that it is going offline to keep the cache information updated
	 * 
	 * Note: It assumes that all the disconnection process is executed without interruption. To prevents possible inconsistent state (i.e. lack of supernode in a zone, etc.) 
	 */
	//TODO: considerare il caso in cui ci sono app in esecuzione. C'e' una perdita delle app eseguite
	public void disconnect(){
		//set offline
		this.getKnowledge().getFeature().goOffline();
		//try to migrate the app that are on queue
		this.tryToMigrate();
		
		// inform of its departure from the network
		
		//inform all other nodes about the offline state (2aII). 
		for (IaasAgent a : this.getKnowledge().getNeighbors()){
			a.getKnowledge().getNeighbors().remove(this.getReferringAgent());
		}
		
		//supernode case
		if (this.getKnowledge().getFeature().isSupernode()){
			
			// inform the others supernodes about its leving from the network
			for (IaasAgent s : this.getKnowledge().getSupernodes()){
				s.getKnowledge().getNeighbors().remove(this.getReferringAgent());
			}
			
			
			//set iself as a non supernode
			this.getKnowledge().getFeature().unsetAsSupernode();
		
			
			// try to substitute itself with another node in the same zone (if available)
			
			// 1a) select the new supernode   // 1b) and the new supernode change its state
			IaasAgent newSupernode = this.selectNextSupernode();
			if (newSupernode != null) {
			
				// 2a) inform all nodes in the network (other supernodes (2aI) and neighbors (2aII))

				
				//of its departure and the reference of a new supernode
				
				//the new reference for the neighbor
				this.signalPresenceToNodes(this.getKnowledge().getNeighbors(), newSupernode);
				
				//the new reference for the other supernodes
				this.signalSupernodePresenceToPeers(newSupernode);

				
				// 3) when the nodes receive a new supernode responsible, then must register on it

				//The older nodes must register themselfs to the new supernode. It is the opposite action of join action
				
				// ask to neighbors to register to new supernode
				pullNotifyFromList(newSupernode, this.getKnowledge().getNeighbors());
				
				// ask to other supernodes to register to new supernode
				pullNotifyFromList(newSupernode, this.getKnowledge().getSupernodes());
				
				
			}

			
			
		}
		else {
			// (2aII) and to the supernode responsible (2aIII)
			
			//notify the supernode responsible for it about its leaving from the network
			this.getKnowledge().getSupernodeResponsible().getKnowledge().removeNeighbor(this.getReferringAgent());

		}
		
		
		//the erase action is done after the disconnection
		//this.eraseAcceptedApp();
		
	}

	/**
	 * Abrubtly exit of a node from the network.
	 * 
	 * Since it can cause the destruction of the network if it is a supernode is needed a machanism that check the presence of the supernode (i.e. periodically)
	 */
	//TODO: non completata e non utilizzabile. Cosi' si distruggerebbe la rete non avendo modo di ripristinare un supernodo. Occorre aggiungere funzioni per permettere il ripristino
	public void death(){
//	    if (this.getReferringAgent().getFeature().getNodeId() == 360015279)
//	    	System.out.println("DeathOf " + 360015279);
		this.getKnowledge().getFeature().goOffline();
		this.eraseAcceptedApp();
	//	System.out.println("death From " + this.getReferringAgent());
		//System.out.println("supernode " + this.getKnowledge().getSupernodeResponsible());
	}
	
	
	/**
	 * The previous supernode randomly select its successor.
	 * If there is an available successor its characteristics are changed to supernode 
	 * 
	 * @return
	 */
	public IaasAgent selectNextSupernode(){
		ArrayList<IaasAgent> contactedCandidate = new ArrayList<IaasAgent>();
		
		if (this.getKnowledge().getNeighbors() != null && this.getKnowledge().getNeighbors().size() != 0) {

			while (contactedCandidate.size() != this.getKnowledge().getNeighbors().size()){
				int supernodeIndex = Engine.getDefault().getSimulationRandom().nextInt(this.getKnowledge().getNeighbors().size());
				IaasAgent successor = this.getKnowledge().getNeighbors().get(supernodeIndex);
				
				if (this.informAsSupernode(successor)) {
					return successor;
				}
				else {
					if (!contactedCandidate.contains(successor)){
						contactedCandidate.add(successor);
					}
				}
			}
			return null;
		}
		else
			return null;
	}
	
	/**
	 * Inform a node a becomed supernode
	 *
	 */
	public boolean informAsSupernode(IaasAgent newSupernode){
		
		if (newSupernode.getFeature().isOnline()){
			newSupernode.getFeature().setAsSupernode();
			return true;
		}
		else 
			return false;
	}

	
	/**
	 * Request to a list of nodes to nofity themselfs (requesters) to a target agent.
	 * 
	 * @param targetAgent
	 * @param requested
	 */
	public void pullNotifyFromList(IaasAgent targetAgent, ArrayList<IaasAgent> requested){
		for (IaasAgent r : requested){
			r.getBehavior().nofityItself(targetAgent);
		}
	}
	
	
	/**
	 * Generates the upperlying ACaaS node that provides services to the Application layer.
	 * 
	 * If during node birth a ACaaS agent template is provided it is used. Otherwise the default agent is created 
	 */
	public void generateAcaasNode(){
		
		if (this.getReferringAgent().getAcaasTemplate() != null){
			this.getReferringAgent().setAcAgent(this.getReferringAgent().getAcaasTemplate());
			//System.out.println("setted node policy 'missTolerance' " + ((AcaasNodePolicy)(this.getReferringAgent().getAcAgent().getPolicy())).getMissRateTolerance());
			return;
		}
		
		AbstractAcaasNodeFeature feature = new AcaasNodeFeature();
		AbstractAcaasNodePolicy policy = new AcaasNodePolicy(this.getReferringAgent().getAppCriteria().getMissRateTolerance(),this.getReferringAgent().getAppCriteria().isAskingToVolunteer(), this.getReferringAgent().getAppCriteria().getMaxNumOfAttempt());
		AbstractAcaasNodeKnowledge knowledge = new AcaasNodeKnowledge(feature);
		AbstractAcaasNodeBehavior behavior = new AcaasNodeBehavior(knowledge, policy);
		
		AcaasAgent acAgent = new AcaasAgent(behavior, knowledge, policy, feature, this.getReferringAgent());
		behavior.setReferringAgent(acAgent);
		
		this.getReferringAgent().setAcAgent(acAgent);
		
		
	}
	
	/**
	 * Update the cache info after a reconection.
	 * It signals the presence to the supernode responsible and update the information about all other supernodes 
	 */
	public void updateCacheAfterReconnection(IaasAgent supernodeResp){
		//signal to its responsible that it is again online
		supernodeResp.getBehavior().signalPresence(this.getReferringAgent());
		//obtain updated info from it
		
		//2) update the supernodelist
		// ask for a list of supernodes
		ArrayList<IaasAgent> sl = supernodeResp.getBehavior().getNSupernodes(this.getPolicy().getnSupernodeAtInit());
		this.getKnowledge().addSupernodes(sl);
		
		// 3) it is not important to update the list of neighbors. It will be updated when inconsistency are detected

	}

}
