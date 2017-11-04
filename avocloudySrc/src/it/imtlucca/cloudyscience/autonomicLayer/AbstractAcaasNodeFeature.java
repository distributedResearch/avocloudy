package it.imtlucca.cloudyscience.autonomicLayer;

import it.imtlucca.cloudyscience.applicationLayer.AppAgent;
import it.unipr.ce.dsg.deus.core.Engine;

import java.util.ArrayList;

/**
 * Main features for an ACaaS node.
 * It is constituted by the accepted application and the time at which the node will be able to execute them.
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractAcaasNodeFeature {

	private ArrayList<AppCache> appQueue;
	
	//TODO: il tempo dovrebbe essere ricalcolato se il modello di fine previsione non e' deterministico
	//e reso differente tra predizione e calcolo effettivo. Quindi dopo l'esecuzione occorre ricalcolare il tempo di finishQueue vedendo la situazione attuale e chi c'e' in coda adesso
	// -> potrebbe non essere piu' possibile eseguire dei task e dover chiedere aiuto dopo
	// Estimation finishing time for the Application on queue
	//XXX: e' una visione ottimistica. L'imprecisione e' fornita solo all'accettazione ma quando si inserisce il task in coda si ha gia' la misura esatta 
	//TODO:devono esserci due variabili. Una con l'effettivo FreeTime ed uno con la versione stimata. Alla fine di ogni app si corregge la stima in base a quanto ha richiesto l'app per essere
	//eseguita
	//Duration of the app on queue (the execution time required by application that are on queue)
	private float finishQueue;
	
	//Time when the cpu will become free
	private float finishTime;

	public AbstractAcaasNodeFeature(){
		super();
		this.appQueue = new ArrayList<AppCache>();
		
		this.finishQueue = 0;
		this.finishTime = 0;
	}

	public ArrayList<AppCache> getAppQueue() {
		return appQueue;
	}
	
	/**
	 * Reset the application queue, aborting all app that are there
	 * @return
	 */
	public ArrayList<AppCache> resetAppQueue(){
		ArrayList<AppCache> queue = this.appQueue;
		this.appQueue = new ArrayList<AppCache>();
		//System.out.println("finishQueue " + finishQueue);
		this.finishQueue = 0;
		this.finishTime = 0;
		
		return queue;
	}
	
	
	// TODO: simile a "public void addToAppQueue(Application app)" of PDP'13 simulator
	public void addAppOnQueue(AppAgent app, float execTime){
		
		this.finishQueue += execTime;
		if (this.finishTime == 0){
			this.finishTime += Engine.getDefault().getVirtualTime();
		}
		this.finishTime += execTime;
		
		app.getFeature().getStatus().taskAdmitted();
		this.appQueue.add(new AppCache(app, execTime));
		
		//the notifyNewTask on AcaasNodeBehavior will call the applicationScheduler on IaasNodeBehavior  
	}

	/**
	 * Gives the time at which it will be free. The duration of apps on queue (NOT the new one) plus the current time 
	 * @return
	 */
	public float getFinishEstimation() {
		//return finishQueue + Engine.getDefault().getVirtualTime();
		//System.out.println("finishTime " + finishTime);
		return finishTime;
	}
	
	/**
	 * Get the time required to execute all the application on queue
	 * @return
	 */
	public float getQueueLength(){
		return finishQueue;
	}

	
	/**
	 * Action to be taken each time an app finish its execution
	 * @return
	 */
	public AppAgent executionTerminated(){
		if (this.appQueue.size() > 0){
			AppCache a = this.appQueue.remove(0);
			//System.out.print("AppFinished: " + this.finishQueue);
			this.finishQueue -= a.getDuration();
			//if (this.finishQueue == 0)
			if (this.appQueue.size() == 0) //the queue list is empty
				this.finishTime = 0;
			else
				this.finishTime = this.finishQueue + Engine.getDefault().getVirtualTime();
			//System.out.println(" -> " + this.finishQueue);
			return a.getApp();
		}
		else
			return null;
		
		
	}

}
