package it.imtlucca.cloudyscience.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodeFeature;
import it.imtlucca.cloudyscience.autonomicLayer.AppTypeStatistics;
import it.imtlucca.cloudyscience.util.NodeList;
import it.unipr.ce.dsg.deus.automator.AutomatorLogger;
import it.unipr.ce.dsg.deus.automator.LoggerObject;
import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

/**
 * Logging of the main features for ACaaS nodes in the network
 * 
 * @author Stefano Sebastio
 *
 */
public class AcaasNodeLog extends Event {

	

	/**
	 * Logging like PDP'13 simulator
	 **/
	private HashMap<String, AppTypeStatistics> appTypeStatistics = new HashMap<String, AppTypeStatistics>();
	
	/*
	private HashMap<String, Integer> reqReceivedType = new HashMap<String, Integer>();
	private HashMap<String, Integer> reqUnmetType = new HashMap<String, Integer>();
	private HashMap<String, Integer> reqSatisfiedType = new HashMap<String, Integer>();
	
	/** 
	 * Queue theory info: Waiting and Sojourn Info
	 */
/*	private HashMap<String, Double> waitingTimeType = new HashMap<String, Double>();
	private HashMap<String, Double> sojournTimeType = new HashMap<String, Double>();
	*/
	//private int waitingTimeCounter = 0;
	//private int sojournTimeCounter = 0;
	
	//private HashMap<String, Integer> waitingTimeCounter = new HashMap<String, Integer>();
	//private HashMap<String, Integer> sojournTimeCounter = new HashMap<String, Integer>();
	
	public AcaasNodeLog(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);

	}

	public void run() throws RunException {
		AutomatorLogger al = new AutomatorLogger();
		ArrayList<LoggerObject> fileValue = new ArrayList<LoggerObject>();
		
		NodeList nodes = new NodeList();
		
		ArrayList<AcaasAgent> acaasAgents = nodes.getAcaasAgentList();
		
		System.out.println("totACaasNodes: " + acaasAgents.size());

		int ownAppReq = 0;
		int unmetCounter = 0;
		int hitCounter = 0;
		int remoteReqAccepted = 0;
		int remoteReqRefused = 0;
		
		int runningApp = 0;
		//System.out.println("###########SEED: " + Engine.getDefault().getCurrentSeed());
		//counts the nodes that actively participate in the network (the ones that have executed or are running at least one task)
		int activeNodes = 0;
		
		//evaluate the node that have executed more and the one that have executed less App 
		int minExecApp = Integer.MAX_VALUE;
		int maxExecApp = Integer.MIN_VALUE;
		
		ArrayList<Integer> executedAndRunningApps = new ArrayList<Integer>();
		
		for (AcaasAgent a : acaasAgents){
			//System.out.println("agent " + a.getAcaasId());
			ownAppReq += ((AcaasNodeFeature)a.getFeature()).getOwnAppReq();
			unmetCounter += ((AcaasNodeFeature)a.getFeature()).getUnmetCounter();
			hitCounter += ((AcaasNodeFeature)a.getFeature()).getHitCounter();
			remoteReqAccepted += ((AcaasNodeFeature)a.getFeature()).getRemoteReqAccepted();
			remoteReqRefused += ((AcaasNodeFeature)a.getFeature()).getRemoteReqRefused();
			//System.out.println("ownAppReq " + ownAppReq + " remoteReqAccepted " + remoteReqAccepted + " remoteReqRefused " + remoteReqRefused);
			//System.out.println("hit " + hitCounter + " miss " + unmetCounter);
			
			runningApp += ((AcaasNodeFeature)a.getFeature()).getAppQueue().size();
			
		//	waitingTimeCounter += ((AcaasNodeFeature)a.getFeature()).getWaitingTimeCounter();
		//	sojournTimeCounter += ((AcaasNodeFeature)a.getFeature()).getSojournTimeCounter();
			//System.out.println("sojournCounter " + sojournTimeCounter);
			
			
			executedAndRunningApps.add(((AcaasNodeFeature)a.getFeature()).getHitCounter()  + ((AcaasNodeFeature)a.getFeature()).getAppQueue().size());
			
			if (  (((AcaasNodeFeature)a.getFeature()).getAppQueue().size() > 0) || (((AcaasNodeFeature)a.getFeature()).getHitCounter() > 0) )
				activeNodes++;
			/*
			if ( ((AcaasNodeFeature)a.getFeature()).getHitCounter() > maxExecApp )
				maxExecApp = ((AcaasNodeFeature)a.getFeature()).getHitCounter();
			if ( ((AcaasNodeFeature)a.getFeature()).getHitCounter() < minExecApp )
				minExecApp = ((AcaasNodeFeature)a.getFeature()).getHitCounter();
				*/
			if ( ( ((AcaasNodeFeature)a.getFeature()).getHitCounter() + ((AcaasNodeFeature)a.getFeature()).getAppQueue().size() ) > maxExecApp )
				maxExecApp = ((AcaasNodeFeature)a.getFeature()).getHitCounter()  + ((AcaasNodeFeature)a.getFeature()).getAppQueue().size();
			if ( ( ((AcaasNodeFeature)a.getFeature()).getHitCounter() + ((AcaasNodeFeature)a.getFeature()).getAppQueue().size() ) < minExecApp )
				minExecApp = ((AcaasNodeFeature)a.getFeature()).getHitCounter() + ((AcaasNodeFeature)a.getFeature()).getAppQueue().size();
				
			
			
			/**
			 * PDP'13 simulator logging for statistics by App type
			 **/
			int x = 0, y=0;

			//analyze the performance for every class of application membership on a single node
			Set<String> key_set = ((AcaasNodeFeature)a.getFeature()).getAppTypeStatistics().keySet();	
			Iterator<String> iter = key_set.iterator();	
			while(iter.hasNext()) {
				String key = iter.next();
				this.updateReqCounter(key, ((AcaasNodeFeature)a.getFeature()).getReqReceived(key));
				
				this.updateHitCounter(key, ((AcaasNodeFeature)a.getFeature()).getReqSatisfied(key));
				x+= ((AcaasNodeFeature)a.getFeature()).getReqSatisfied(key);
				
				this.updateMissCounter(key, ((AcaasNodeFeature)a.getFeature()).getReqUnmet(key));
				y += ((AcaasNodeFeature)a.getFeature()).getReqUnmet(key);
				
				
				//queue statistics				
				this.updateWaitingTime(key, ((AcaasNodeFeature)a.getFeature()).getWaitingTime(key), ((AcaasNodeFeature)a.getFeature()).getWaitingTimeCounter(key));
				this.updateSojournTime(key, ((AcaasNodeFeature)a.getFeature()).getSojournTime(key), ((AcaasNodeFeature)a.getFeature()).getSojournTimeCounter(key));
				
				//System.out.println("Sojourn " + ((AcaasNodeFeature)a.getFeature()).getSojournTime(key));
			}

			
			
			//check
			if ((y != ((AcaasNodeFeature)a.getFeature()).getUnmetCounter()) || (x != ((AcaasNodeFeature)a.getFeature()).getHitCounter() )){
				System.err.println("Counter missmatch");
				System.out.println("reqUnmet " + y + " where " + ((AcaasNodeFeature)a.getFeature()).getUnmetCounter());
				System.out.println("reqSatisfied " + x + " where " + ((AcaasNodeFeature)a.getFeature()).getHitCounter());
				System.exit(1);
			}
			
		}
		System.out.println("ownAppReq: " + ownAppReq);
		System.out.println("unmetApp: " + unmetCounter);
		System.out.println("hitApp: " + hitCounter);
		System.out.println("remoteAppReqAccepted: " + remoteReqAccepted);
		System.out.println("runningApp: " + runningApp);
		

		
		//analyze the performance for every class of application membership on a single node
		Set<String> key_set = this.appTypeStatistics.keySet();	
		Iterator<String> iter = key_set.iterator();	
		while(iter.hasNext()) {
			String key = iter.next();
 	
			System.out.print("TOT For application type " + key +  " on tot req " + this.appTypeStatistics.get(key).getReqReceived());
			System.out.println(" the hits are " + this.appTypeStatistics.get(key).getReqSatisfied() + " and miss " + this.appTypeStatistics.get(key).getReqUnmet());
			
			fileValue.add(new LoggerObject("tot_" + key + "_req", this.appTypeStatistics.get(key).getReqReceived()));
			
			fileValue.add(new LoggerObject("tot_" + key + "_hit", this.appTypeStatistics.get(key).getReqSatisfied()));
			
			fileValue.add(new LoggerObject("tot_" + key + "_miss", this.appTypeStatistics.get(key).getReqUnmet()));
			
			if (this.appTypeStatistics.get(key).getReqReceived() == 0){
				fileValue.add(new LoggerObject("hit_rate_for_" + key, 1));
			} else {
				fileValue.add(new LoggerObject("hit_rate_for_" + key, ((double)this.appTypeStatistics.get(key).getReqSatisfied())/ ((double)this.appTypeStatistics.get(key).getReqReceived()) ));
			}
			
			if (this.appTypeStatistics.get(key).getReqUnmet() == 0){
				fileValue.add(new LoggerObject("Hit_(and_on_execution)_rate_for_" + key, 1 ) );
				System.out.println("***Hit (and on execution) rate for " + key + ": " + 1 );
			} else {
				fileValue.add(new LoggerObject("Hit_(and_on_execution)_rate_for_" + key, (1.0-((double)this.appTypeStatistics.get(key).getReqUnmet())/ ((double)this.appTypeStatistics.get(key).getReqReceived()) ) ));
				System.out.println("***Hit (and on execution) rate for " + key + ": " + (1.0-((double)this.appTypeStatistics.get(key).getReqUnmet())/ ((double)this.appTypeStatistics.get(key).getReqReceived()) ));
			}
			
			//TODO: aggiungere waiting time (con mean, max, min, etc) -> per avere min,max occorre aggiungere queste info a ACaaSFeature che non 
			// deve raccogliere solo un agglomerato ma anche queste info
			//TODO: aggiungere sojourn time (waiting time+service time) (con ...)
			
			if (this.appTypeStatistics.get(key).getWaitingTime() == 0){
				fileValue.add(new LoggerObject("Mean_Waiting_for_" + key, 0 ) );
				System.out.println("***Mean Waiting for " + key + ": " + 0);
			} else {
				double overallWaitingTimeType = this.appTypeStatistics.get(key).getWaitingTime();
				int overallWaitingTimeTypeCounter = this.appTypeStatistics.get(key).getWaitingTimeCounter();
				//fileValue.add(new LoggerObject("Mean_Waiting_for_" + key, (float)(overallWaitingTimeType/(float)waitingTimeCounter) ) );
				fileValue.add(new LoggerObject("Mean_Waiting_for_" + key, (float)(overallWaitingTimeType/(float)overallWaitingTimeTypeCounter) ) );
				//System.out.println("***Mean Waiting for " + key + ": " + (float)(overallWaitingTimeType/(float)waitingTimeCounter));
				System.out.println("***Mean Waiting for " + key + ": " + (float)(overallWaitingTimeType/(float)overallWaitingTimeTypeCounter));
			}
			
			if (this.appTypeStatistics.get(key).getSojournTime() == 0){
				fileValue.add(new LoggerObject("Mean_Sojourn_for_" + key, 0 ) );
				System.out.println("***Mean Sojourn for " + key + ": " + 0);
			} else {
				double overallSojournTimeType = this.appTypeStatistics.get(key).getSojournTime();
				int overallSojournTimeTypeCounter = this.appTypeStatistics.get(key).getSojournTimeCounter();
				//FIXME:
				//fileValue.add(new LoggerObject("Mean_Sojourn_for_" + key, (float)(overallSojournTimeType/(float)sojournTimeCounter) ) );
				fileValue.add(new LoggerObject("Mean_Sojourn_for_" + key, (float)(overallSojournTimeType/(float)overallSojournTimeTypeCounter) ) );
				//System.out.println("***Mean Sojourn for " + key + ": " + (float)(overallSojournTimeType/(float)sojournTimeCounter));
				System.out.println("***Mean Sojourn for " + key + ": " + (float)(overallSojournTimeType/(float)overallSojournTimeTypeCounter));
			}
			
		} 
		double hitAndExec;
		if (ownAppReq ==0)
			hitAndExec = 1;
		else 
			hitAndExec= (1.0-((double)unmetCounter/(double)ownAppReq));
		
		//fileValue.add(new LoggerObject("***Hit (and on execution) rate : ", (1.0-((double)(hitCounter+runningApp)/(double)ownAppReq)) ) );
		//System.out.println("***Hit (and on execution) rate : " + (1.0-((double)(hitCounter+runningApp)/(double)ownAppReq)));
		//fileValue.add(new LoggerObject("***Hit (and on execution) : ", (1.0-((double)unmetCounter/(double)ownAppReq))));
		fileValue.add(new LoggerObject("Hit_(and_on_execution)", hitAndExec));
		System.out.println("***Hit (and on execution) : " +  hitAndExec);
		
		fileValue.add(new LoggerObject("TOT_remoteReqAccepted_(migrated)", remoteReqAccepted));
		System.out.println("***TOT remoteReqAccepted (migrated): " + remoteReqAccepted);
		//fileValue.add(new LoggerObject("useless_messages_ratio", ((double)(ownAppReq-remoteReqAccepted))/(double)ownAppReq));
		//System.out.println("***useless messages ratio: " + ((double)(ownAppReq-remoteReqAccepted))/(double)ownAppReq);
		if ((remoteReqRefused + remoteReqAccepted) != 0){
			fileValue.add(new LoggerObject("useless_messages_ratio", ((double)remoteReqRefused)/(double)(remoteReqRefused + remoteReqAccepted)));
			System.out.println(("***useless messages ratio: " + ((double)remoteReqRefused)/(double)(remoteReqRefused + remoteReqAccepted)));			
		} else {
			fileValue.add(new LoggerObject("useless_messages_ratio", 0 ));
			System.out.println(("***useless messages ratio: " + 0 ));	
		}

		
		// number of serviced tasks per node 
		System.out.println("Active nodes : " + activeNodes + " on tot nodes " + acaasAgents.size());
		fileValue.add(new LoggerObject("tasks_nodes_ratio", (hitAndExec/activeNodes)));
		System.out.println("***tasks nodes ratio : " + (hitAndExec/activeNodes));
		
		double meanExecutedTaskPerNode;
		if (activeNodes == 0)
			meanExecutedTaskPerNode = 0;
		else
			meanExecutedTaskPerNode = (double) (hitCounter+runningApp) / (double) activeNodes;
		//fileValue.add(new LoggerObject("(overall)_task_nodes_ratio", (hitAndExec/acaasAgents.size())));
		fileValue.add(new LoggerObject("mean_task_per_node", meanExecutedTaskPerNode));
		//System.out.println("(overall) task nodes ratio : " + (hitAndExec/acaasAgents.size()));
		System.out.println("mean task per node : " + meanExecutedTaskPerNode);
		
		
		double varianceExecutedTask = this.executedTaskVariance(executedAndRunningApps, meanExecutedTaskPerNode);
		fileValue.add(new LoggerObject("variance_task_per_node", varianceExecutedTask));
		System.out.println("variance task per node : " + varianceExecutedTask);
		
		System.out.println("MaxTasksPerNode : " + maxExecApp);
		fileValue.add(new LoggerObject("MaxTasksPerNode", maxExecApp));
		System.out.println("MinTasksPerNode : " + minExecApp);
		fileValue.add(new LoggerObject("MinTasksPerNode", minExecApp));
		
		
		System.out.println("refusedReq: " + remoteReqRefused);	
		System.out.println("allReq: " + (remoteReqRefused + remoteReqAccepted));
		
		
		//TODO: aggiungere metrica paper "Tomas-Carrion" app rifiutate + completate oltre la QoS richiesta
		

		
		
		al.write(Engine.getDefault().getVirtualTime(), fileValue);
	}

	
	
	
	
	public void updateReqCounter(String type, int addition){
		if (!this.appTypeStatistics.containsKey(type))
			this.appTypeStatistics.put(type, new AppTypeStatistics(type));
		
		this.appTypeStatistics.get(type).addReqReceivedSet(addition);
		
	}
	
	public void updateHitCounter(String type, int addition){
		if (!this.appTypeStatistics.containsKey(type))
			this.appTypeStatistics.put(type, new AppTypeStatistics(type));
		
		this.appTypeStatistics.get(type).addReqSatisfiedSet(addition);
		
	}
	

	public void updateMissCounter(String type, int addition){
		if (!this.appTypeStatistics.containsKey(type))
			this.appTypeStatistics.put(type, new AppTypeStatistics(type));
		
		this.appTypeStatistics.get(type).addReqUnmetSet(addition);

	}
	
	
	public void updateWaitingTime(String type, double addition, int counter){
		if (!this.appTypeStatistics.containsKey(type))
			this.appTypeStatistics.put(type, new AppTypeStatistics(type));

		this.appTypeStatistics.get(type).addWaitingTime(addition);
		//(counter-1) since the first one is just added when the waiting time is updated
		this.appTypeStatistics.get(type).addWaitingTimeCounter( (counter-1));
	}
	
//	public void updateWaitingTimeCounter(String type, int addition){
//		if (!this.waitingTimeCounter.containsKey(type))
//			this.waitingTimeCounter.put(type, addition);
//		else {
//			int actualWaitingCounter = this.waitingTimeCounter.get(type);
//			this.waitingTimeCounter.remove(type);
//			this.waitingTimeCounter.put(type, addition+actualWaitingCounter);
//		}
//	}
	
	public void updateSojournTime(String type, double addition, int counter){
		if (!this.appTypeStatistics.containsKey(type))
			this.appTypeStatistics.put(type, new AppTypeStatistics(type));
		
		this.appTypeStatistics.get(type).addSojournTime(addition);
		//(counter-1) since the first one is just added when the sojourn time is updated
		this.appTypeStatistics.get(type).addSojournTimeCounter( (counter-1));
	}
	
//	public void updateSojournTimeCounter(String type, int addition){
//		if (!this.sojournTimeCounter.containsKey(type))
//			this.sojournTimeCounter.put(type, addition);
//		else {
//			int actualSojournCounter = this.sojournTimeCounter.get(type);
//			this.sojournTimeCounter.remove(type);
//			this.sojournTimeCounter.put(type, addition+actualSojournCounter);
//		}
//	}
	
	private double executedTaskVariance(ArrayList<Integer> values, double mean){
		
		double variance = 0;
		
		for (int v : values){
			variance += Math.pow((v-mean), 2);
		}
		
		variance = variance / (values.size()-1);
		
		return variance;
		
	}
}
