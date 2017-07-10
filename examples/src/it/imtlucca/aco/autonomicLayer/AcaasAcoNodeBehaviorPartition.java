package it.imtlucca.aco.autonomicLayer;

import java.util.ArrayList;
import java.util.HashMap;

import it.imtlucca.aco.Ant;
import it.imtlucca.aco.AntColor;
import it.imtlucca.aco.ColoredAnt;
import it.imtlucca.aco.Pheromone;
import it.imtlucca.cloudyscience.applicationLayer.AppAgent;
import it.imtlucca.cloudyscience.applicationLayer.AppNodeFeature;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodeKnowledge;
import it.imtlucca.cloudyscience.autonomicLayer.AbstractAcaasNodePolicy;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodeBehavior;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;
import it.unipr.ce.dsg.deus.core.Engine;

/**
 * ACaaS layer according the ACO (Ant Colony Optimization) model load balancing
 * 
 * @author Stefano Sebastio
 *
 */
public class AcaasAcoNodeBehaviorPartition extends AcaasNodeBehavior {

	// In the policy there are the configuration parameters that defines the ants behavior
	
	// In the knowledge there are the pheromone values associated to the neighbors path
	
	//every node has its own set k of hunter ants. These will be then reinitialized for the following task
	private ArrayList<Ant> antSet;
	
	
	//Optional Colored Ant (scout) for resources discovery 
	private ArrayList<ColoredAnt> coloredAnts;
	private AcaasAcoNodePolicy policy;
	
	public AcaasAcoNodeBehaviorPartition(AbstractAcaasNodeKnowledge knwoledge,
			AbstractAcaasNodePolicy policy) {
		super(knwoledge, policy);
		
		//this.antSet = new ArrayList<Ant>();
		this.policy = (AcaasAcoNodePolicy) policy;
		this.coloredAnts = new ArrayList<ColoredAnt>(AntColor.N_OF_COLOR);
	}

	
	/**
	 * Manage application according the ACO model.
	 * It is the 'handleEvent' according our draft on ACO load-balancing approach
	 */
	public void manageApplication(AppAgent app){
		this.initializeAnts();
		//System.out.println("manageApp by ACO called...");
		this.updateRequestCounter(app);
		//System.out.println("appid " + app.getFeature().getAppId());
		if (this.searchExecutingNode(app))
			return;
		else{
			this.updateMissCounter(app);
		}
		
	}
	
	
	/**
	 * Construct the ACO pheromone probability list and then ask to them probabilistically until a node is found
	 * or the list is exhausted 
	 */
	public boolean searchExecutingNode(AppAgent a){
		//System.out.println("searchExecutingNode with Ant");
		boolean nodeFound = false;
		
		int k = this.policy.getkAnt();

		//when an Ant found the nest the research is stopped 
		//Note that the maximum number of attempts to execute the application is disabled for the aco approach
		while(k > 0 && !nodeFound){
			//System.out.println("Ant trying " + k);
			//TODO: evitare di creare una nuova formica tutte le volte, resettare quella che c'e'
			Ant ant = this.antSet.get(k-1).setApp(a);
			//TODO: vedere se non ci sono altre strade partendo dal primo nodo allora non si fa neanche partire la formica
			
			nodeFound = this.antStep(ant);
			//System.out.println("Ant " + k + " for app " + a.getFeature().getAppId());
			/*//this.repartProbToOther(nodes);
			
			IaasAgent n = this.selectNodeProbabilistically(nodes);
			System.out.println("Launch Ant " + (k-1));
			//start an Ant, searching for the nest
			Ant ant = this.antSet.get(k-1).setApp(a);
			nodeFound = ((AcaasAcoNodeBehavior)n.getAcAgent().getBehavior()).antStep(ant);
			*/
			k--;
		}
//		System.out.println("Node " + nodeFound + " by ant " + (k+1) + " for app " + a.getFeature().getAppId());
//		if (!nodeFound)
//			System.out.println("Node " + nodeFound + " by ant " + (k+1) + " for app " + a.getFeature().getAppId());
		return nodeFound;
		
	}
	
	/**
	 * The step forward done by the Ant.
	 * 
	 * The Ant stops the exploration and comes back to home if found a nest or TTL expires 
	 * 
	 * @param ant
	 */
	public boolean antStep(Ant ant){
		//System.out.println("AntStep");
		ant.addStep(this.getReferringAgent().getReferringAgent());
		//System.out.println("ant ttl " + ant.getTtlValue());
		//check if itself will execute the app
		//if (this.askExecutionToNode(ant.getSource(),ant.getApp())){
		
		boolean reqResponse = false;
		//before submitting the request in the ACO-PARTITIONED check if the destination node belongs to the corresponding partition
		if ( (((AppNodeFeature)(ant.getApp().getFeature())).getTaskType().equals("applicationSmall") && this.getReferringAgent().getReferringAgent().getFeature().getZoneOfBelonging() == 1) ||
				(((AppNodeFeature)(ant.getApp().getFeature())).getTaskType().equals("applicationLarge") && this.getReferringAgent().getReferringAgent().getFeature().getZoneOfBelonging() == 2)  ){
			
			reqResponse = ((AcaasAcoNodeBehaviorPartition)ant.getSource().getAcAgent().getBehavior()).askExecutionToNode(this.getReferringAgent().getReferringAgent(), ant.getApp());	

		}
		else {			
			//force the TTL to increase
			ant.bonusTtl();
		}
			
		if (reqResponse){
			//if (((AcaasAcoNodeBehaviorPartition)ant.getSource().getAcAgent().getBehavior()).askExecutionToNode(this.getReferringAgent().getReferringAgent(), ant.getApp())){
				//System.out.println("ack from " + this.getReferringAgent().getReferringAgent() + " step " + ant.getPath().size());
				ant.getPath().remove(ant.getPath().size()-1);
				if (ant.getPath().size() > 0){
				
					//then the ant step back
					//return this.stepBack(ant, this.getReferringAgent().getReferringAgent());
					//return ((AcaasAcoNodeBehavior)ant.getPath().get(ant.getPath().size()-).getAcAgent().getBehavior()).stepBack(ant);
					return ((AcaasAcoNodeBehaviorPartition)ant.getPath().get(ant.getPath().size()-1).getAcAgent().getBehavior()).stepBack(ant, this.getReferringAgent().getReferringAgent());
				}
				else{ //the ant source is the same of the ant nest
					//System.out.println("ant source = nest");
					return true;
				}
		}

		
		//otherwise find another node to ask (if there are neighbors that are not in the ant path)
		else if ( (ant.getTtlValue()>0) ){
			//check if there are neighbors that aren't already in the path
			//FIXME: non serve creare l'array ???
			//System.out.println("try another step");
			//FIXME: correggere da path IaasAgent a Pheromone
			HashMap<IaasAgent,Pheromone> paths = new HashMap<IaasAgent,Pheromone>(((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromonePath());
			
			for (IaasAgent dest : ant.getPath()){
				if (paths.containsKey(dest))
					paths.remove(dest);
			}
			//paths.removeAll(ant.getPath());
			if (paths.size()>0){ 
				//TODO: necessario fare il 'new'. La lista viene creata tutte le volte ugualmente
				//System.out.println("called probabilistically by basic ant");
				IaasAgent n = this.selectNodeProbabilistically( new ArrayList<Pheromone> (paths.values()), -1, ant.getApp(), ((AcaasAcoNodeBehaviorPartition)ant.getSource().getAcAgent().getBehavior()) );
				//System.out.println("another ant step " + n);
				return ((AcaasAcoNodeBehaviorPartition)n.getAcAgent().getBehavior()).antStep(ant);
			}  
		}
		
		return this.stepBackHome(ant);

		
		
	}
	
	/**
	 * The Ant step back: the Ant release the pheromone
	 * It is called only when the ant have found the nest and then needs to release the pheromone on the reverse path
	 *  
	 * @param ant
	 */
	public boolean stepBack(Ant ant, IaasAgent from){
		//System.out.println("AntStepBack");
		//pull a path hop and come back to home
		//int step = ((AcaasAcoNodePolicy)ant.getSource().getAcAgent().getPolicy()).getTtl() - ant.getTtlValue();
		int step = ant.getPath().size()-1;
		double p = ((AcaasAcoNodePolicy)ant.getSource().getAcAgent().getPolicy()).getPheromoneToDeposit(step, 1);
		((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromonePath().get(from).depositPheromone(((AcaasAcoNodeBehaviorPartition)ant.getSource().getAcAgent().getBehavior()).policy, p);
		//System.out.println("deposit to " + this.getReferringAgent().getReferringAgent() + " from " + from + " p " + p);
		
		if (ant.getPath().size() > 1){
			IaasAgent source = ant.getPath().remove(ant.getPath().size()-1);
			//then the ant step back
			//System.out.println("Another step back through " + source);
			return ((AcaasAcoNodeBehaviorPartition)ant.getPath().get(ant.getPath().size()-1).getAcAgent().getBehavior()).stepBack(ant, source);
		}
		else {
			//System.out.println("arrived");
			return true;
		}
		
	}
	
	/**
	 * The ant comes directly to home without releasing nothing since the ttl is expired or there are not other neighbors to explore
	 * 
	 * @param ant
	 */
	public boolean stepBackHome(Ant ant){
		//System.out.println("AntStepBackHome");
		//goes directly to home
		return false;
	}
	
	/**
	 * Contact a node and ask if it can execute the application. 
	 * It is called from the source node
	 * 
	 * @param n node at which the request is addressed
	 * @param a 
	 * @return
	 */
	public boolean askExecutionToNode(IaasAgent n, AppAgent a){
		
		boolean nodeFound = false;
		//System.out.println("---> From --->" + this.getReferringAgent().getReferringAgent().getFeature().getNodeId());
		
		//System.out.print("<--- to " + n.getFeature().getNodeId() + " received ");
		//Double resp = this.requestFinishEstimation(n.getAcAgent());
		Float resp = ((AcaasNodeBehavior)n.getAcAgent().getBehavior()).requestFinishEstimation(this.getReferringAgent());
		
		
		//System.out.println("resp " + resp + " at time " + Engine.getDefault().getVirtualTime());
		if (resp != null){
			
			//if (resp < a.getFeature().getDeadline()){
				
				nodeFound = ((AcaasNodeBehavior)n.getAcAgent().getBehavior()).appExecReq(a, this.getReferringAgent());
				if (a.getReferringAgent().getReferringAgent() != n)
					a.getKnowledge().incReqCounter();
				
				if (nodeFound){
					//System.out.println("add a ack");
					//System.out.println("ACK");
					return nodeFound;
				} else {
					//System.out.println("NACK resp: " + resp + " req " + a.getFeature().getDeadline());
					//System.out.println("add a nack");
				}
			//}
			
		}
		// removed to not assign penalty for node that results offline
		else { //if the nodes is offline a penalty is assigned
			//System.out.println("offline");
			//this.ratingServer.addNodeNack(n);
		}
		
		return nodeFound;
	}
	
	
	/**
	 * Choose a node probabilistically according the pheromone path
	 * 
	 * if a color with an associated probability is specified then the relative probability is considered
	 * 
	 * After a node is chosen 
	 * @return
	 */
	private IaasAgent selectNodeProbabilistically(ArrayList<Pheromone> nodes, int color, AppAgent app, AcaasAcoNodeBehaviorPartition antOwnerBehavior){
		
		this.repartProbToOther(nodes, color, app, antOwnerBehavior);
		
		double extraction = Engine.getDefault().getSimulationRandom().nextDouble();
		
		double sumProb = 0;

		int i=-1;
		while (sumProb<extraction && (i < nodes.size()-1) ){
			i++;
			
			if (color == -1){
				sumProb += nodes.get(i).getProbability();
			}
			else
				sumProb += nodes.get(i).getColoredProbability(color);
		
		}

		IaasAgent n = nodes.remove(i).getNeighbor();
		
		return n;
	}
	
//	private IaasAgent selectNodeProbabilistically(ArrayList<Pheromone> nodes, AppAgent app){
//		return this.selectNodeProbabilistically(nodes, -1, app);
//	}
	
	
	/**
	 * Repartitioning the probability of the extracted node among the other nodes in the list
	 * 
	 * if a color with an associated probability is specified then the relative probability is considered
	 * 
	 * @param nodes
	 */
	private void repartProbToOther(ArrayList<Pheromone> nodes, int color, AppAgent app, AcaasAcoNodeBehaviorPartition antOwnerBehavior){
		//System.out.println("color " + color + ", policy " + antOwnerBehavior.policy);
		ArrayList<Pheromone> paths = null;
		//TODO: assicurarsi che serva per normalizzare !!!
		if ( (color == -1) && (app != null) ){
			// generate a list with all pheromone in the neighborhood to allow the normalization of values used in the overall pheromone 
			paths = new ArrayList<Pheromone> (((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromonePath().values());
		}
		
		double sumPheromone = 0;
		for (Pheromone n : nodes){
				if(color == -1) {
					if (app != null) { 
					//sumPheromone += n.getPheromone(this.policy);
						//sumPheromone += n.getPheromoneWithTemperature(this.policy, app);
						//Prior of Alberto's normalization suggestion
						//sumPheromone += n.getPheromoneWithTemperature(antOwnerBehavior.policy, app, this.policy);
						sumPheromone += n.getNormalizedPheromoneWithTemperature(antOwnerBehavior.policy, app, this.policy, paths);
					}
				else 
					//sumPheromone += n.getColoredPheromone(this.policy, color);
					//sumPheromone += n.getColoredPheromoneWithTemperature(this.policy, color);
					sumPheromone += n.getColoredPheromoneWithTemperature(antOwnerBehavior.policy, color, this.policy);
				
				}
		}
		double probabilityPath = ((double) 1) / sumPheromone;
		
		for (Pheromone n : nodes){

			if (color == -1){
				//n.setProbability(n.getPheromone(this.policy)*probabilityPath);
				//n.setProbability(n.getPheromoneWithTemperature(this.policy, app)*probabilityPath);
				//Prior of Alberto's normalization suggestion
				//n.setProbability(n.getPheromoneWithTemperature(antOwnerBehavior.policy, app, this.policy)*probabilityPath);
				n.setProbability(n.getNormalizedPheromoneWithTemperature(antOwnerBehavior.policy, app, this.policy, paths)*probabilityPath);
			}
			else
				//n.setColoredProbability( n.getColoredPheromone(this.policy, color)*probabilityPath, color);
				//n.setColoredProbability( n.getColoredPheromoneWithTemperature(this.policy, color)*probabilityPath, color);
				n.setColoredProbability( n.getColoredPheromoneWithTemperature(antOwnerBehavior.policy, color, this.policy)*probabilityPath, color);
			//System.out.println(n.getScoreRate() + " " + n.getNode().getFeature().getNodeId() + " prob " + n.getRateProbability());
		}
		//System.out.println("@@@@@@@@@@@@@");
	}
	/*
	private void repartProbToOther(ArrayList<Pheromone> nodes, AppAgent app, AcaasAcoNodeBehavior antOwnerBehavior){
		//System.out.println("ant basic");
		this.repartProbToOther(nodes,-1, app, antOwnerBehavior);
		return;
	}*/
	
	//TODO: gestire cosa accade al feromone con la morte/connessione/riconnessione/join 
	/**
	 * Initialize the pheromone path.
	 * 
	 * According the following state chart 
	 * Node Online-(1)->get neighbors -> initialize Pheromone Path -(offline/death)->delete Pheromone Path
	 * -> reconnection/join -> (1)
	 * 
	 * So it must be called in different situations: init, reconnection, join
	 * 
	 * 
	 * Assigns a minimum value of pheromone to every neighbor to initialize the ant algorithm 
	 * 
	 */
	public void initializePheromonePath(){
		
		for (IaasAgent a : this.getReferringAgent().getReferringAgent().getBehavior().getAllKnownNodes()) {
			
			if ( (a != this.getReferringAgent().getReferringAgent()) && (((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromone(a) == null) ){
				Pheromone p = new Pheromone( this.policy.getInitPheromone(), -1, a);
				
				((AcaasAcoNodeKnowledge)this.getKnowledge()).addPheromone(p);
			}
		}
		
		
//		// add a path for each neighbor
//		for (IaasAgent a : this.getReferringAgent().getReferringAgent().getKnowledge().getNeighbors()) {
//			Pheromone p = new Pheromone( this.policy.getInitPheromone(), -1, a);
//				
//			((AcaasAcoNodeKnowledge)this.getKnowledge()).addPheromone(p);
//		}
//		
//		//plus the supernode responsible
//		IaasAgent a = this.getReferringAgent().getReferringAgent().getKnowledge().getSupernodeResponsible();
//		
//		if ( (a != this.getReferringAgent().getReferringAgent()) && (((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromone(a) == null) ){
//			Pheromone p = new Pheromone( this.policy.getInitPheromone(), -1, a);
//			
//			((AcaasAcoNodeKnowledge)this.getKnowledge()).addPheromone(p);
//		}
		
		this.createAnts();
	}	
	
	/**
	 * Generate the Ant set available by the node.
	 * 
	 * And initialize the Colored ones
	 */
	private void createAnts(){
		this.antSet = new ArrayList<Ant>();
		for (int i=0; i < this.policy.getkAnt(); i++){
			this.antSet.add(new Ant(this.getReferringAgent().getReferringAgent()));
		}
		
	}
	
	/**
	 * Create the ant of the specified color
	 * 
	 * @param color
	 */
	public void createColoredAnts(int color){
		
		int i = this.coloredAnts.size();
		while (i <= (color)){
			this.coloredAnts.add(null);
			i++;
		}
		ColoredAnt ant = null;
		
		if (color == AntColor.FINISHING_TIME){
			ant = new ColoredAnt(this.getReferringAgent().getReferringAgent(), AntColor.FINISHING_TIME, /*Double.MAX_VALUE,*/ this.policy.getTtlColored(color));
		}
		else 
			ant = new ColoredAnt(this.getReferringAgent().getReferringAgent(), color, /*0,*/ this.policy.getTtlColored(color));
		
		this.coloredAnts.set(color, ant);

		this.initializePheromoneColorPath(color);
	}
	
	/**
	 * Initialize the path for the pheromone of the specified color
	 * 
	 * @param color
	 */
	private void initializePheromoneColorPath(int color){
		
		
		for (IaasAgent a : this.getReferringAgent().getReferringAgent().getBehavior().getAllKnownNodes()) {
		//System.out.println("A " + a);
			if ( (a != this.getReferringAgent().getReferringAgent()) && (((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromone(a) != null) ){
				Pheromone p = ((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromone(a);
				//p.depositColoredPheromone( this.policy, this.policy.getInitColoredPheromone(color), 0, color);
				p.setInitColoredPheromone(this.policy.getInitColoredPheromone(color), color);
			}
		}
		
		
		//System.out.println("From " + this.getReferringAgent().getReferringAgent());
		
//		for (IaasAgent a : this.getReferringAgent().getReferringAgent().getKnowledge().getNeighbors()) {
//			//System.out.println("Neighbor " + a);
//			Pheromone p = ((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromone(a);			
//			//p.depositColoredPheromone(this.policy, this.policy.getInitColoredPheromone(color), 0, color);
//			p.setInitColoredPheromone(this.policy.getInitColoredPheromone(color), color);
//
//		}
//		
//		
//		IaasAgent a = this.getReferringAgent().getReferringAgent().getKnowledge().getSupernodeResponsible();
//		//System.out.println("SuperNeighbor " + a);
//		if ( (a != this.getReferringAgent().getReferringAgent()) && (((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromone(a) == null) ){
//			Pheromone p = ((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromone(a);
//			//p.depositColoredPheromone( this.policy, this.policy.getInitColoredPheromone(color), 0, color);
//			p.setInitColoredPheromone(this.policy.getInitColoredPheromone(color), color);
//		}
		
	/*	a = this.getReferringAgent().getReferringAgent();
		System.out.println("itself " + a);
		if ( ((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromone(a) == null) {
			Pheromone p = ((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromone(a);
			p.setInitColoredPheromone(this.policy.getInitColoredPheromone(color), color);
		}*/
	}
	
	/**
	 * Brings back to home all ants 
	 * 
	 */
	private void initializeAnts(){
		for (int i=0; i < this.policy.getkAnt(); i++){
			this.antSet.get(i).initAnt( this.policy.getTtl() );
		}
	}
	
	/**
	 * Register the presence of a new neighbor in the overlay network and if a path does not exist add a new one
	 */
	public void newNeighborOnOverlay(IaasAgent a){
		this.addPheromonePathForNode(a);
//		//System.out.println("From " + this.getReferringAgent().getReferringAgent());
//		
//		//System.out.println("newNeighbor " + a);
//		if ( (a != this.getReferringAgent().getReferringAgent()) && (((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromone(a) == null) ){
//			Pheromone p = new Pheromone( this.policy.getInitPheromone(), -1, a);
//			for (int i=0; i < this.policy.getColoredAntPolicy().size(); i++ ){
//			
//				if ( this.policy.isColoredReleaseFunction(i) ){
//					p.setInitColoredPheromone( this.policy.getInitColoredPheromone(i), i);
//				}
//				
//			}
//			
//			((AcaasAcoNodeKnowledge)this.getKnowledge()).addPheromone(p);
//		}
//		
//		
//		this.resetColoredTemperature();
	}
	
	/** 
	 * Register the presence of a new node in the overlay i.e. a supernode or a regular node of another zone 
	 * @param a
	 */
	public void newNodeOnOverlay(IaasAgent a) {
		this.addPheromonePathForNode(a);
	}
	
	//FIXME: anche la costruzione dei path potrebbe chiamare la stessa funzione
	/**
	 * Add the pheromone path for a new node on the overlay network
	 * @param a
	 */
	private void addPheromonePathForNode(IaasAgent a){
		//System.out.println("From " + this.getReferringAgent().getReferringAgent());
		
		//System.out.println("newNeighbor " + a);
		if ( (a != this.getReferringAgent().getReferringAgent()) && (((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromone(a) == null) ){
			Pheromone p = new Pheromone( this.policy.getInitPheromone(), -1, a);
			for (int i=0; i < this.policy.getColoredAntPolicy().size(); i++ ){
			
				if ( this.policy.isColoredReleaseFunction(i) ){
					p.setInitColoredPheromone( this.policy.getInitColoredPheromone(i), i);
				}
				
			}
			
			((AcaasAcoNodeKnowledge)this.getKnowledge()).addPheromone(p);
		}
		
		
		this.resetColoredTemperature();
	}
	
	/**
	 * Reset the temperature parameter in the Boltzmann function. 
	 * 
	 * The temperature is increased when a new node join the network to increase the exploration component.
	 */
	private void resetColoredTemperature(){
		
		for (ColoredAntPolicy p : this.policy.getColoredAntPolicy()){
			if ( (p != null) && (p.getTemperatureFunction() != null) ){
				
				p.getTemperatureFunction().setShift(Engine.getDefault().getVirtualTime());			
			}
		}
		this.policy.getTemperatureFunction().setShift(Engine.getDefault().getVirtualTime());
		
	}
	
	/**
	 *
	 * Update all pheromone path for evaporation
	 * 
	 */
	public void checkAllPheromonePath(){
		ArrayList<Pheromone> pheromones = new ArrayList<Pheromone>(((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromonePath().values());
		for (Pheromone p : pheromones ){
			p.checkEvaporation(this.policy);
			
			//if ( ((AcaasAcoNodePolicy)this.getPolicy()).isFinishingTimePheromone()){
			if ( this.policy.isColoredReleaseFunction(AntColor.FINISHING_TIME)){
				p.checkEvaporationFinishingTime(this.policy);
			}
		}
		
	}

	/**
	 * Initialize and launch a colored ant
	 * 
	 * @param a
	 */
	public void initColoredAnt(ColoredAnt a){
		//System.out.println("init coloredAnt col " + a.getAntColor());
		this.repartProbToOther(new ArrayList<Pheromone>(((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromonePath().values()), 
				a.getAntColor(), null, this);
		//System.out.println("initant ");
		a.initAnt(this.getTtlForColor(a.getAntColor()));
		//System.out.println("antstep");
		this.coloredAntStep(a);
	}
	
	/**
	 * Retrieve the ttl corresponding to the selected color
	 * @param color
	 * @return
	 */
	private int getTtlForColor(int color){
		return this.policy.getTtlColored(color);
	}
	
	/**
	 * Provides the information asked by colored ant
	 * 
	 * @param color
	 */
	public double getRequestedInfo(int color){
		
		switch (color){
			case AntColor.CPU_CORE :
				//System.out.println("CPU core " + this.getReferringAgent().getReferringAgent().getFeature().getCoreNumber());
				return this.getReferringAgent().getReferringAgent().getFeature().getCoreNumber();
			case AntColor.CPU_FREQ :
				return this.getReferringAgent().getReferringAgent().getFeature().getCoreFreq();
			case AntColor.MAIN_MEMORY :
				return this.getReferringAgent().getReferringAgent().getFeature().getMainMemory();
			case AntColor.FINISHING_TIME :
				/*
				 * The requestFinishEstimation return 0 if the node is free, otherwise it reports the VT at which the node will become free. 
				 * Thus subtracting the current VT it is possible to obtain the time needed to become free starting from now 
				 */
				//System.out.println("finishEst " + this.requestFinishEstimation(this.getReferringAgent()));
				//System.out.println("current " + Engine.getDefault().getVirtualTime());
				return Math.max((this.requestFinishEstimation(this.getReferringAgent()) - Engine.getDefault().getVirtualTime()),0);
		}
		
		return 0;
	}
	
	/**
	 * The step forward done by the Colored Ant.
	 * 
	 * The Ant stops the exploration and comes back to home when TTL expires 
	 * 
	 * @param ant
	 */
	public void coloredAntStep(ColoredAnt ant){
		//System.out.println("ColoredAntStep");
		
		//Obtain the requested value
		ant.addStep(this.getReferringAgent().getReferringAgent(), this.getRequestedInfo(ant.getAntColor()));
		//System.out.println("for color " + ant.getAntColor() + " stepOn " + this.getReferringAgent().getReferringAgent());
		//check if itself will execute the app
		if ( ant.getTtlValue()>0 ){
			//check if there are neighbors that aren't already in the path

			//FIXME: correggere da path IaasAgent a Pheromone
			HashMap<IaasAgent,Pheromone> paths = new HashMap<IaasAgent,Pheromone>(((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromonePath());
			
			for (IaasAgent dest : ant.getPath()){
				if (paths.containsKey(dest))
					paths.remove(dest);
			}
			//paths.removeAll(ant.getPath());
			if (paths.size()>0){ 
				//TODO: necessario fare il 'new'
				//System.out.println("call probabilistically by color " + ant.getAntColor() + " from " + (((AcaasAcoNodeBehavior)ant.getSource().getAcAgent().getBehavior()).policy));
				IaasAgent n = this.selectNodeProbabilistically( new ArrayList<Pheromone> (paths.values()), ant.getAntColor(), null, ((AcaasAcoNodeBehaviorPartition)ant.getSource().getAcAgent().getBehavior()) );
				
				//System.out.println("coloredstep through " + n);
				((AcaasAcoNodeBehaviorPartition)n.getAcAgent().getBehavior()).coloredAntStep(ant);
				return;
			} 
			
		}
		//else {
			//System.out.println("step exhausted or no other path");
			int stepIdStart = ant.getPath().indexOf(this.getReferringAgent().getReferringAgent());
			/*ant.getPath().remove(ant.getPath().size()-1);
			if (ant.getPath().size() > 0){*/
			if (stepIdStart > 0){
				//System.out.println("stepback");
				//then the ant step back
				//((AcaasAcoNodeBehavior)ant.getPath().get(ant.getPath().size()-1).getAcAgent().getBehavior()).coloredAntStepBack(ant, this.getReferringAgent().getReferringAgent(), step);
				((AcaasAcoNodeBehaviorPartition)ant.getPath().get(stepIdStart-1).getAcAgent().getBehavior()).coloredAntStepBack(ant, this.getReferringAgent().getReferringAgent(), stepIdStart);
						//get(ant.getPath().size()-1).getAcAgent().getBehavior()).coloredAntStepBack(ant, this.getReferringAgent().getReferringAgent(), step);
				return;
			}
				
		//}

	}
	
	
	/**
	 * The Colored Ant step back: the Ant release the pheromone if it is better than the previous one
	 * It is called only when the ant have exhausted the ttl and then needs to release the pheromone on the reverse path
	 *  
	 * @param ant
	 * @param stepIdStart the index of horizon starting
	 */
	public void coloredAntStepBack(ColoredAnt ant, IaasAgent from, int stepIdStart){
		//System.out.println("AntStepBack");
		//pull a path hop and come back to home
		this.depositColoredPheromone(ant, from, stepIdStart);
		//System.out.println("to deposit " + ant.getBestColorFound());
		//depositCpuCorePheromone
		
		//if (ant.getPath().size() > 1){
		int stepBackIdStart = ant.getPath().indexOf(this.getReferringAgent().getReferringAgent());
		
		if (stepBackIdStart > 0){
			//IaasAgent source = ant.getPath().remove(ant.getPath().size()-1);
			//then the ant step back
			//System.out.println("stepback");
			//((AcaasAcoNodeBehavior)ant.getPath().get(ant.getPath().size()-1).getAcAgent().getBehavior()).coloredAntStepBack(ant, this.getReferringAgent().getReferringAgent(), stepBack);
			((AcaasAcoNodeBehaviorPartition)ant.getPath().get(stepBackIdStart-1).getAcAgent().getBehavior()).coloredAntStepBack(ant, this.getReferringAgent().getReferringAgent(), stepBackIdStart);
			return;
		}
		
	}
	
	/**
	 * Deposit the pheromone of the corresponding color
	 * @param ant
	 * @param stepIdStart index of memory aging horizon starting
	 */
	public void depositColoredPheromone(ColoredAnt ant, IaasAgent from, int stepIdStart){
		/*System.out.println("--- " + ((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromone(from));
		for (Pheromone p : ((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromonePath().values()){
			System.out.println("p " + p.getPheromone(this.policy) + " for " + p.getNeighbor());
		}*/
		//int step = ant.getPath().size()-1;
		((AcaasAcoNodeKnowledge)this.getKnowledge()).getPheromone(from).depositColoredPheromone(((AcaasAcoNodeBehaviorPartition)ant.getSource().getAcAgent().getBehavior()).policy, /*ant.getBestColorFound(),*/ stepIdStart, ant.getAntColor(), ant);
	}

	
	public ColoredAnt getColoredAnt (int color){
	
		return this.coloredAnts.get(color);
		
	}


	public AcaasAcoNodePolicy getPolicy() {
		return policy;
	}


	public ArrayList<ColoredAnt> getColoredAnts() {
		return coloredAnts;
	}
	
	
	
}
