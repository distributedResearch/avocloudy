package it.imtlucca.cloudyscience.infrastructureLayer;

import it.imtlucca.cloudyscience.applicationLayer.AppAgent;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodeBehavior;
import it.imtlucca.cloudyscience.autonomicLayer.AppCache;
import it.imtlucca.cloudyscience.physicalLayer.Hardware;
import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Behavior of the IaaS node.
 * It constitutes the VM functionalities (included the OS scheduling) 
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractIaasNodeBehavior {


	private AbstractIaasNodeKnowledge knowledge;
	private AbstractIaasNodePolicy policy;
	
	
	private IaasAgent referringAgent;
	
	private boolean cpuIdle;
		
	
	public AbstractIaasNodeKnowledge getKnowledge() {
		return knowledge;
	}


	public AbstractIaasNodePolicy getPolicy() {
		return policy;
	}


	public IaasAgent getReferringAgent() {
		return referringAgent;
	}

	
	public void setKnowledge(AbstractIaasNodeKnowledge knwoledge) {
		this.knowledge = knwoledge;
	}


	public void setPolicy(AbstractIaasNodePolicy policy) {
		this.policy = policy;
	}


	public void setReferringAgent(IaasAgent referringAgent) {
		this.referringAgent = referringAgent;
	}


	public void getNodeLoad() {
		
	}


	public AbstractIaasNodeBehavior(AbstractIaasNodeKnowledge knowledge,
			AbstractIaasNodePolicy policy, IaasAgent referringAgent) {
		super();
		this.knowledge = knowledge;
		this.policy = policy;
		this.referringAgent = referringAgent;
		
		this.cpuIdle = true;
	}
	
	public AbstractIaasNodeBehavior(AbstractIaasNodeKnowledge knowledge,
			AbstractIaasNodePolicy policy) {
		super();
		this.knowledge = knowledge;
		this.policy = policy;
		
		this.cpuIdle = true;
	}
	
	public boolean isCpuIdle(){
		return this.cpuIdle;
	}
	
	//Functions to manage the Overlay Network
	
	public void join(){
	}
	
	public void connect() {
		System.out.println("****Connect from AbstractIaasNodeBehavior---");
	}
	
	public void disconnect(){
		System.out.println("***Disconnect form AbstractIaasNodeBehavior---");
	}
	// Get informations about the other Nodes in the same Zone
	
	
	/**
	 * Get a list with all known nodes i.e., supernodes plus neighbors
	 * 
	 */
	public List<IaasAgent> getAllKnownNodes(){
		List<IaasAgent> allKnownNodes = new ArrayList<IaasAgent>(getReferringAgent().getKnowledge().getSupernodes());  
		allKnownNodes.addAll(getReferringAgent().getKnowledge().getNeighbors());
		return allKnownNodes;
	}
	
	/**
	 * Register the signalation of a joined node in the network
	 * 
	 * @param newNode
	 */
	public void signalPresence(IaasAgent newNode){
		
		//add to the list of supernodes acquaintances
		if (this.getKnowledge().getFeature().isSupernode() && newNode.getFeature().isSupernode()){
			this.getKnowledge().addSupernode(newNode);
			return;
		}
		
		if (this.getKnowledge().getFeature().getZoneOfBelonging() == newNode.getFeature().getZoneOfBelonging()){
			//if this is the supernode responsible add it on neighbor list
			if (this.getKnowledge().getFeature().isSupernode() ) {
				this.getKnowledge().addNeighbor(newNode);
			}
			//if this is not supernode for the newNode zone, then check if it ongoing connections have available slots
			else if ( (this.getKnowledge().getNeighbors().size() < this.getPolicy().getnConnectionOngoing()) || (this.getPolicy().getnConnectionOngoing() ==-1) ) {
				//FIXME: dovrebbe essere segnalato come neigbor ma di un'altra zona: this.getReferringAgent().newNodeNotify(newNode);
				this.getKnowledge().addNeighbor(newNode);
			}
		} else if ( ((IaasNodePolicy) this.getPolicy()).isConnectionAmongZonesOngoing() ) {
			
			if ( (this.getKnowledge().getNeighbors().size() < this.getPolicy().getnConnectionOngoing()) || (this.getPolicy().getnConnectionOngoing() ==-1) ) {
				
				//FIXME: dovrebbe essere segnalato come neigbor ma di un'altra zona: this.getReferringAgent().newNodeNotify(newNode);
				this.getKnowledge().addNeighbor(newNode);
			}
			
		}
		
		/**
		//if this is the supernode responsible add it on neighbor list
		if (this.getKnowledge().getFeature().isSupernode() && (this.getKnowledge().getFeature().getZoneOfBelonging() == newNode.getFeature().getZoneOfBelonging())){
			
			this.getKnowledge().addNeighbor(newNode);
			
		}
		//FIXME: aggiungendo un nodo di un altra zona alla lista dei nodi di zona si mischiano nella stessa lista nodi con stesso site a quelli con site differenti
		//if this is not supernode for the newNode zone, then check if it ongoing connections have available slots
		else if ( this.getKnowledge().getFeature().getZoneOfBelonging() == newNode.getFeature().getZoneOfBelonging() ) {
			if ( (this.getKnowledge().getNeighbors().size() < this.getPolicy().getnConnectionOngoing()) || (this.getPolicy().getnConnectionOngoing() ==-1) ) {
				this.getKnowledge().addNeighbor(newNode);
			}
		} 
		 **/
	}
	
	/**
	 * Notify to target agent the presence of itself
	 * 
	 * @param target
	 */
	public void nofityItself(IaasAgent target){
		target.getBehavior().signalPresence(this.getReferringAgent());
	}
	
	/**
	 * Notify itself to an agent list
	 * 
	 * @param targetList
	 */
	public void notifyItselfToList(ArrayList<IaasAgent> targetList){
		for (IaasAgent a : targetList){
			this.nofityItself(a);
		}
	}
	
	
	/**
	 * Signals the presence 'newNode' to a list of IaaS nodes
	 * 
	 * @param nodes
	 * @param newNode
	 */
	public void signalPresenceToNodes(ArrayList<IaasAgent> nodes, IaasAgent newNode){
		//System.out.println("Signals the presence to a list: " + nodes);
		for (IaasAgent a : nodes){
			a.getBehavior().signalPresence(newNode);
		}
		
	}
	
	/**
	 * Signals the presence of a 'newSupernode' to all other known IaaS Supernodes
	 * 
	 * @param newSupernode
	 */
	public void signalSupernodePresenceToPeers(IaasAgent newSupernode){
		//System.out.println("Signals the presence to a list: " + nodes);
		for (IaasAgent a : this.getKnowledge().getSupernodes()){
			a.getBehavior().signalPresence(newSupernode);
		}
		
	}
	
	/**
	 * Register the signalation of a joined supernode in the network
	 * 
	 * @param newNode
	 */
	public void signalPresenceAsSupernode(IaasAgent newNode){
		
		//if this is the supernode responsible add it on neighbor list
		if (this.getKnowledge().getFeature().isSupernode() ) {
			this.getKnowledge().addSupernode(newNode);
			return;
		} 
//TODO: check potrebbe non servire. Si e' sicuri di aggiungere un nuovo supernodo. Pensare a quale caso potrebbe servire
		if (this.getKnowledge().getFeature().getZoneOfBelonging() == newNode.getFeature().getZoneOfBelonging() || ((IaasNodePolicy) this.getPolicy()).isConnectionAmongZonesOngoing()){
			
			//if this is not supernode for the newNode zone, then check if it ongoing connections have available slots
			if ( (this.getKnowledge().getSupernodes().size() < this.getPolicy().getnSupernodeAtInit()) || (this.getPolicy().getnSupernodeAtInit() ==-1) ) {
				this.getKnowledge().addSupernode(newNode);
			}
		}
			
		/**
		//if this is a supernode add it on supernode list
		if ( this.getKnowledge().getFeature().isSupernode() ){// && (this.getKnowledge().getFeature().getZoneOfBelonging() == newNode.getFeature().getZoneOfBelonging())){
			
			this.getKnowledge().addSupernode(newNode);
			
		}
		
		//if this is not supernode for the newNode zone, then check if it ongoing connections have available slots
		else if (this.getKnowledge().getSupernodes().size() < this.getPolicy().getnSupernodeAtInit() || this.getPolicy().getnSupernodeAtInit() == -1) {
			this.getKnowledge().addSupernode(newNode);
		}
**/
	}
	
	/**
	 * Signals the presence of a 'newNode' of type supernode to a list of IaaS nodes
	 * 
	 * @param nodes
	 * @param newNode
	 */
	public void signalPresenceAsSupernodeToNodes(ArrayList<IaasAgent> nodes, IaasAgent newNode){
		//System.out.println("Signals the presence to a list: " + nodes);
		for (IaasAgent a : nodes){
			a.getBehavior().signalPresenceAsSupernode(newNode);
		}
		
	}
	
	/**
	 * Used to ask an updated list of supernodes
	 * 
	 * @return
	 */
	public ArrayList<IaasAgent> getSupernodesList(){
		return this.getSupernodesList();
	}
	
	
	public void updateSupernodesList(){
		IaasAgent supernode = this.getKnowledge().getSupernodeResponsible();
		this.getKnowledge().addSupernodes(supernode.getBehavior().getSupernodesList());
	}
	
	
	
	//TODO: the same for the departure
	/**
	 * Asks to obtain a list with 'n' supernodes (asking to the supernode responsible).
	 * 
	 * @param n
	 */
	public ArrayList<IaasAgent> getNSupernodes(int n){
		
		// n is set to '-1' to obtain all supernodes
		if (this.getKnowledge().getSupernodes().size() < n || n ==-1){
			
			return this.getKnowledge().getSupernodes();
		}
		else {
			ArrayList<IaasAgent> as = new ArrayList<IaasAgent>();
			ArrayList<IaasAgent> ks = this.getKnowledge().getSupernodes();
			//for(int i = 0; i < ks.size(); i++ ){
			//TODO: sostituire con shuffle per la scelta di un sottoinsieme di nodi dalla lista (Collections.shuffle, sublist)
			int i=0;
			do {
				int si = (int) (Engine.getDefault().getSimulationRandom().nextDouble()*(ks.size()+1));
				
				if (!as.contains(ks.get(si))){
					as.add(ks.get(si));
					i++;
				}
			} while (i<n);
			
			return as;
		}
	}
	
	/**
	 * Gives a list with 'n' nodes (asking to the supernode responsible).
	 * The nodes are given shuffled to not overload every times the same nodes
	 * 
	 * @param n
	 * @return
	 */
	public ArrayList<IaasAgent> getNNeighbor(int n){
		
		// shuffle the neighbor to don't ask every time to the same nodes
		Collections.shuffle(this.getKnowledge().getNeighbors(), Engine.getDefault().getSimulationRandom());
				
		
		if (this.getKnowledge().getNeighbors().size() < n || n == -1){
			return this.getKnowledge().getNeighbors();
		}
		else {
			ArrayList<IaasAgent> an = new ArrayList<IaasAgent>();
			ArrayList<IaasAgent> kn = this.getKnowledge().getNeighbors();

			//since the list is just shuffled is possible to get the first n neighbors
			for (IaasAgent a : kn.subList(0, n)){
				an.add(a);
			}			
			/*int i=0;
			do {

				int ni = (int) (Engine.getDefault().getSimulationRandom().nextDouble()*kn.size());
				
				if (!an.contains(kn.get(ni))){
					an.add(kn.get(ni));
					i++;
				}
			} while (i<n);
			*/
			
			return an;
		}
	}
	
	
	public void generateAcaasNode(){
		
	}
	
	/**
	 * Estimate if the Application 'app' can be executed on this IaaS node considering its requirements (memory and deadline)
	 * 
	 * @param app
	 * @return
	 */
	//TODO: per fare una stima se puo' soddisfare le richieste del task
	public boolean estimateTaskAcceptance(AppAgent app, AcaasAgent sourceAgent){
		float netOverhead = this.evaluateNetOverhead(app, sourceAgent);
		/*float netOverhead = 0;
		
		if (sourceAgent != this.getReferringAgent().getAcAgent()) {
			//System.out.println("Source ACAgent and destination ACAgent are different");
			IaasAgent externalIaas = sourceAgent.getReferringAgent();
			Hardware externalPoint = null;
			if (externalIaas instanceof IaasAgentDataCenter){
				externalPoint = ((IaasAgentDataCenter) externalIaas).getDcAgent().getHws().get(0);
			} else if (externalIaas instanceof IaasAgentVolunteer){
				externalPoint = ((IaasAgentVolunteer) externalIaas).getHardwareResources();
			}
			
			
			
			
			IaasAgent internalIaas = this.getReferringAgent();
			if (internalIaas instanceof IaasAgentDataCenter){
				//FIXME: considerare il caso in cui si hanno piu' hw e non prendere il primo
				netOverhead = ((IaasAgentDataCenter) internalIaas).getDcAgent().getHws().get(0).evaluateOverheadTime(externalPoint, app);
			} else if (internalIaas instanceof IaasAgentVolunteer){
				netOverhead = ((IaasAgentVolunteer) internalIaas).getHardwareResources().evaluateOverheadTime(externalPoint, app);
			}
		}*/
			
		
		//System.out.println("### Probing of " + this.getKnowledge().getFeature().getNodeId() + " for App " + app.getFeature().getAppId());
		//System.out.println("reqRAM " + app.getFeature().getRam());
		//System.out.println("availableRAM " + this.getKnowledge().getFeature().getMainMemory());
		if (app.getFeature().getRam() > this.getKnowledge().getFeature().getMainMemory()){
			//TODO: non considerata la richiesta di espansione ram nel caso data center
			return false;
		}
		
		float expectedEnd = this.estimateTaskExit(app);
		// add the network overhead to transmit the application among a pair of nodes
		expectedEnd += netOverhead;
		
		//FIXME: togliere ed usare il netOverhead tra i punti fisici della rete
		//08/06/2013 revoede since the new evaluate network overhead is considered
		//netOverhead = this.cloudSiteOverhead(sourceAgent.getReferringAgent());
		//expectedEnd += netOverhead;
		//FIXME: togliere
		
	/*	System.out.println("currentTime " + Engine.getDefault().getVirtualTime() + " expectedEnd " + expectedEnd + " deadline " + app.getFeature().getDeadline() + " on " + this.getReferringAgent().getFeature().getNodeId()
				+ " from " + sourceAgent.getReferringAgent().getFeature().getNodeId());
*/
		if (expectedEnd <= app.getFeature().getDeadline() ){
			
			return true;
			
		}
			
		else{
				//System.out.println("Deadline unsatisfiable");
				//System.out.println("currentTime " + Engine.getDefault().getVirtualTime());
				return false;
		}
			
		
		
	}
	
	/**
	 * Evaluate the network overhead for task transmission
	 * 
	 * @param app
	 * @param sourceAgent
	 * @return
	 */
	public float evaluateNetOverhead(AppAgent app, AcaasAgent sourceAgent){
		
		float netOverhead = 0;
		if (sourceAgent != this.getReferringAgent().getAcAgent()) {
			//System.out.println("Source ACAgent and destination ACAgent are different");
			IaasAgent externalIaas = sourceAgent.getReferringAgent();
			Hardware externalPoint = null;
			if (externalIaas instanceof IaasAgentDataCenter){
				externalPoint = ((IaasAgentDataCenter) externalIaas).getDcAgent().getHws().get(0);
			} else if (externalIaas instanceof IaasAgentVolunteer){
				externalPoint = ((IaasAgentVolunteer) externalIaas).getHardwareResources();
			}
			
			
			
			
			IaasAgent internalIaas = this.getReferringAgent();
			if (internalIaas instanceof IaasAgentDataCenter){
				//FIXME: considerare il caso in cui si hanno piu' hw e non prendere il primo
				netOverhead = ((IaasAgentDataCenter) internalIaas).getDcAgent().getHws().get(0).evaluateOverheadTime(externalPoint, app);
			} else if (internalIaas instanceof IaasAgentVolunteer){
				netOverhead = ((IaasAgentVolunteer) internalIaas).getHardwareResources().evaluateOverheadTime(externalPoint, app);
			}
		}
		return netOverhead;
	}
	
	
	/**
	 * Estimate the time needed to execute the application on the current machine.
	 * 
	 * Following the Rough Set technique of "Estimating Job Execution time and Handling Missing Job Requirements Using Rough Set in Grid Scheduling" [Selvi et al.]
	 * the accuracy in estimating runtimes is 74.05%, thus we assume a uniform error of +/- 13% (half of the total 26% of committed error) respects to the real task duration.
	 * It is supposed that there is a database that permit to start using the Rough Set technique since the node boot.
	 * 
	 * @param app
	 * @return
	 */
	//TODO: il modello di predizione puo' anche utilizzare una correzione basata sulla storia rispetto ad un valore statico del 13%
	public float estimateTaskExit(AppAgent app){

		float realDuration = this.evaluateTaskDuration(app);
		//TODO: correggere con lo 0.13 
		float estimationError = realDuration*0.0f;
		//double estimationError = realDuration*0.13;
		float appReqTime = (float) ((realDuration - estimationError) + (Engine.getDefault().getSimulationRandom().nextDouble())*(2*estimationError));  
		//System.out.println("appReqTime " + appReqTime);
		//double expectedEnd = Engine.getDefault().getVirtualTime() + appReqTime;
		float expectedEnd = appReqTime;
		//System.out.println("expectedEnd " + expectedEnd);
		//TODO: inserire la valutazione dei costi di trasmissione
		//inserita in estimateTaskAcceptance();
		
		//System.out.println("thisAppQueueSize " + this.getReferringAgent().getAcAgent().getFeature().getAppQueue().size());
		
		if (!this.getReferringAgent().getAcAgent().getFeature().getAppQueue().isEmpty()){
			//System.out.println("forCurrentappQueue " + this.getReferringAgent().getAcAgent().getFeature().getFinishEstimation());
			//the Current virtual time is just embedded on the finish estimation time
			expectedEnd += this.getReferringAgent().getAcAgent().getFeature().getFinishEstimation(); 
			//to take into account the amount of execution required by the applicaitons that are just on queue
			//expectedEnd += this.getReferringAgent().getAcAgent().getFeature().getQueueFinish();
		} else{
			//System.out.println("noOtherApp");
			expectedEnd += Engine.getDefault().getVirtualTime();
		}
		return expectedEnd;
	}
	
	/**
	 * Evaluate the time needed to finish the execution the App on the current architecture. It is not an estimate, it is the real duration
	 * 
	 * @param app
	 * @return
	 */
	public float evaluateTaskExit(AppAgent app){
		/**
		//System.out.println("cpu " + this.getKnowledge().getFeature().getCoreNumber());
		//System.out.println("appParallelism " + app.getFeature().getParallelism());
		int maxExploitableParal = Math.min(this.getKnowledge().getFeature().getCoreNumber(), app.getFeature().getParallelism());
		//System.out.println("exploitableParall " + maxExploitableParal);
		
		//System.out.println("appDuration " + app.getFeature().getDuration());
		double appReqTime = Math.ceil((double)(app.getFeature().getDuration()/maxExploitableParal)/(double)this.getKnowledge().getFeature().getCoreFreq());
		**/
		float appReqTime = this.evaluateTaskDuration(app);
		
		//System.out.println("appReqTime " + appReqTime);
		//double expectedEnd = Engine.getDefault().getVirtualTime() + appReqTime;
		float expectedEnd = appReqTime;
		//System.out.println("expectedEnd " + expectedEnd);
		//TODO: inserire la valutazione dei costi di trasmissione
		
		if (!this.getReferringAgent().getAcAgent().getFeature().getAppQueue().isEmpty())
			expectedEnd += this.getReferringAgent().getAcAgent().getFeature().getFinishEstimation();
		else
			expectedEnd += Engine.getDefault().getVirtualTime();
		
		return expectedEnd;
	}
	
	/**
	 * Evaluate the task duration using the current architecture
	 * 
	 * Task execution time is evaluated according an extended Amdahl's law where there is:
	 * -) a sequential section '1-f'
	 * -) a parallelizable section 'f' up to 'taskParal'
	 * 
	 * Thus is we consider to have available on VM 'coreNumber' cores, we obtain: 
	 * 
	 * f task fraction that can exploit the maximum parallelism 
	 * T_exec = 1-f + f/min(taskParal, coreNumber), with unitary base (reference) frequency 
	 * 
	 * both parts of the equation are executed with core frequency 'F', thus:
	 * 
	 * T_exec = (1-f)/F + f/(min(taskParal,coreNumber)*F)
	 * 
	 * 
	 * @param app
	 * @return
	 */
	//TODO: possibile separare la esecuzione della durata del task da quella della valutazione (dove si considera tutto parallelizzabile) ??? 
	public int evaluateTaskDuration(AppAgent app){
		
		//System.out.println("cpu " + this.getKnowledge().getFeature().getCoreNumber());
		//System.out.println("appParallelism " + app.getFeature().getParallelism());
		int maxExploitableParal = Math.min(this.getKnowledge().getFeature().getCoreNumber(), app.getFeature().getParallelism());
		//System.out.println("exploitableParall " + maxExploitableParal);
		
		float sequentialPartDuration = app.getFeature().getDuration()*(1-app.getFeature().getParalFrac());
		float parallelizablePartDuration = app.getFeature().getDuration() - sequentialPartDuration;
		
		
		//System.out.println("appDuration " + app.getFeature().getDuration());
		//double appReqTime = Math.ceil((double)(app.getFeature().getDuration()/maxExploitableParal)/(double)this.getKnowledge().getFeature().getCoreFreq());
		int appReqTime = (int) Math.ceil((float)(parallelizablePartDuration/maxExploitableParal)/(double)this.getKnowledge().getFeature().getCoreFreq());
		appReqTime += Math.ceil((float)sequentialPartDuration/(double)this.getKnowledge().getFeature().getCoreFreq());
		
		return appReqTime;
	}
	
	
	//TODO: esegue lo scheduling dell'evento fine esecuzione per i task che sono stati accettati
	//crea l'Event fine exec
	//TODO: correggere nel caso di un modello piu' sofisticato con piu' cpu che eseguono in parallelo
	/**
	 * Check if the nodes can perform the execution of a task (if it is idle), and eventually call the launch of an application on queue 
	 */
	public void applicationScheduler(){
		//check if there are other tasks on execution
		if (this.cpuIdle)
			this.launchAppExecution();
		
	}
	
	/**
	 * Execute an application.
	 * It calculates the actual finishing time for application execution 
	 * 
	 * "public void executeApp(Application app)" of PDP'13 simulator
	 */
	private void launchAppExecution(){
		
		if (!this.getReferringAgent().getAcAgent().getFeature().getAppQueue().isEmpty()) {
			this.cpuIdle = false;
			// the application is still in the queue. It will be eliminated after the execution ends
			AppAgent app = this.getReferringAgent().getAcAgent().getFeature().getAppQueue().get(0).getApp();
			
			//int maxExploitableParal = Math.min(this.getKnowledge().getFeature().getCoreNumber(), app.getFeature().getParallelism());
			//double appReqTime = Math.ceil((double)(app.getFeature().getDuration()/maxExploitableParal)/(double)this.getKnowledge().getFeature().getCoreFreq());
			int appReqTime = this.evaluateTaskDuration(app);
			
			int executionEnd = (int) Math.round(Engine.getDefault().getVirtualTime()) + appReqTime;
			
			Event e = Engine.getDefault().createEvent("executionEnd", executionEnd );
/*
			if (appReqTime < app.getFeature().getDuration()){
				System.out.println("TriggerTime: " + e.getTriggeringTime() + " where now " + Engine.getDefault().getVirtualTime() + " needed time " + appReqTime);	
			}
			
			if (app.getFeature().getAppId() == 411818657 || app.getFeature().getAppId() == 800424409){
				System.out.println("app is " + app.getFeature().getAppId());
				System.out.println("started at " + Engine.getDefault().getVirtualTime());
				System.out.println("needed time " + appReqTime);
				System.out.println("will finish " + executionEnd);
				System.out.println("setted execution end " + (e.getTriggeringTime()));
				System.out.println("max float " + Float.MAX_VALUE);
			}
	*/		
			//System.out.println("TriggerTime: " + e.getTriggeringTime() + " where now " + Engine.getDefault().getVirtualTime() + " needed time " + appReqTime);
			ExecutionEndEvent endEvent = (ExecutionEndEvent) e;
			endEvent.setIaasAgent(this.getReferringAgent());
			endEvent.setAppAgent(app);
			//this.getReferringAgent()
			//e.setAssociatedNode(associatedNode)
			//System.out.println("executionEnd " + e.getTriggeringTime());
			Engine.getDefault().insertIntoEventsList(endEvent);
			
			app.getFeature().setStartTime(Engine.getDefault().getVirtualTime());
			((AcaasNodeBehavior) this.getReferringAgent().getAcAgent().getBehavior()).updateWaitingTimeInfo(app);
			
			app.getKnowledge().addPerformingNode(this.getReferringAgent().getAcAgent());
			
			
			//FIXME: aggiornare il provision of Free della cpu
		}
		
	}
	
	/**
	 * Called at the triggering VT of the application execution termination. 
	 * 
	 * When an application finish its execution the queue is checked to start a waiting application.
	 * 
	 * "public void executionTerminated()" of PDP'13 simulator
	 */
	public void appExecutionEnded(AppAgent a){
		if (this.cpuIdle){
			//if the cpu is alredy idle then it means that the task was gone offline and the application was aborted
			return;
		}
		
		this.cpuIdle = true;
		
		AppAgent app = this.getReferringAgent().getAcAgent().getFeature().getAppQueue().get(0).getApp();
		if (app != a){
			//the terminated application is not the first one in the execution queue. So it is an aborted application
			return;
		}

		
		//update the prevision free time
		//app = this.getReferringAgent().getAcAgent().getFeature().getAppQueue().remove(0);
		app = this.getReferringAgent().getAcAgent().getFeature().executionTerminated();
		//System.out.println("ealine " + app.getFeature().getDeadline() + " currentTime " + Engine.getDefault().getVirtualTime());
		//if the node is offline the application is not consider as an hit
		if ( (app!= null) && (app.getFeature().getDeadline() >= Engine.getDefault().getVirtualTime()) && (this.getKnowledge().getFeature().isOnline()) ){
			app.getFeature().setFinishTime(Engine.getDefault().getVirtualTime());
			((AcaasNodeBehavior) this.getReferringAgent().getAcAgent().getBehavior()).updateHitCounter(app);
			//this.applicationScheduler();
			this.getReferringAgent().getAcAgent().getBehavior().notifyAppExecutionEnd(true);
			
		} else {
			((AcaasNodeBehavior) this.getReferringAgent().getAcAgent().getBehavior()).updateMissCounter(app);
			this.getReferringAgent().getAcAgent().getBehavior().notifyAppExecutionEnd(false);
			//TODO: check to expand sul DataCenter
		}
		this.applicationScheduler();
		
		
	}
	
	
	/**
	 * Delete all Application on execution and on queue. Ad sign a penalty a for those
	 */
	public void eraseAcceptedApp(){
		
		// update the finishing estimation time and empties the queue
		ArrayList<AppCache> apps = this.getReferringAgent().getAcAgent().getFeature().resetAppQueue();
		//System.out.println("death apps " + apps.size());
		for (AppCache a : apps){
			((AcaasNodeBehavior) this.getReferringAgent().getAcAgent().getBehavior()).updateMissCounter(a.getApp());
			//System.out.println("add miss by " + this.getReferringAgent().getFeature().getNodeId());
		}
		
		//free the cpu and set the running app as completed (aborted)
		if(!this.cpuIdle){
			//if the cpu is working then the first App on queue is the running one
			apps.get(0).getApp().getFeature().getStatus().taskExit();
			
			this.cpuIdle = true;
		}
		
	}
	
	/**
	 * Try to migrate all app on queue since the node is going offline
	 */
	public void tryToMigrate(){
		
		ArrayList<AppCache> apps = this.getReferringAgent().getAcAgent().getFeature().getAppQueue();
		
		for (AppCache a : apps){
			this.getReferringAgent().getAcAgent().getBehavior().manageApplication(a.getApp());
		}
		
	}
	
	/**
	 * Evaluate the transmission overhead for the nodes in different site
	 * @param externalNode
	 * @return
	 * 
	 * "costOfRemote" of "checkIfSatisfiable()" of PDP'13 simulator
	 * 
	 * @deprecated use "Hardware.evaluateOverheadTime(Hardware otherPoint, AppAgent app)"
	 */
	public float cloudSiteOverhead(IaasAgent externalNode){
		/** from pdp'13 simulator
		int originNodeRegion = originNode.regionalZoneOfAffiliation; // the region of the node FROM which coming the request - where the application is currently delivered 
		int localNodeRegion = this.regionalZoneOfAffiliation; // the region of the node AT which is requested the execution 
		double costOfRemote = (priceVTforRemote/numberOfRegionalZone)*(Math.abs(originNodeRegion-localNodeRegion)%numberOfRegionalZone);
		**/
		
		int localNodeRegion = this.getKnowledge().getFeature().getZoneOfBelonging();
		int remoteNodeRegion = externalNode.getFeature().getZoneOfBelonging();
		int priceForRemoteSite = ((IaasNodeKnowledge) this.getKnowledge()).getOverheadForRemoteSite();
		int numOfRegionalZone = this.getKnowledge().getNumberOfRegionalZones();
		float costOfRemoteExecution = (priceForRemoteSite/numOfRegionalZone)*(Math.abs(localNodeRegion-remoteNodeRegion)%numOfRegionalZone);
		
		return costOfRemoteExecution;
	}
	
	//TODO: "public double estimateMissRate()" di PDP'13 che pero' non era utilizzato
	
}
