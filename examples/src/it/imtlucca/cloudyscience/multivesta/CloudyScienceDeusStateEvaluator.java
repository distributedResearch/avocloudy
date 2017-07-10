package it.imtlucca.cloudyscience.multivesta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import vesta.mc.IStateEvaluator;
import vesta.mc.NewState;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasAgent;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodeFeature;
import it.imtlucca.cloudyscience.autonomicLayer.AppTypeStatistics;
import it.imtlucca.cloudyscience.util.NodeList;

/**
 * Provides an interfacing with multivesta. Its aim is give access to model properties 
 * 
 * @author Stefano Sebastio
 *
 */
public class CloudyScienceDeusStateEvaluator implements IStateEvaluator {
	/**
	 * Logging like PDP'13 simulator
	 **/
	private HashMap<String, AppTypeStatistics> appTypeStatistics;
	/*private HashMap<String, Integer> reqReceivedType;// = new HashMap<String, Integer>();
	private HashMap<String, Integer> reqUnmetType;// = new HashMap<String, Integer>();
	private HashMap<String, Integer> reqSatisfiedType;// = new HashMap<String, Integer>();
	
	/** 
	 * Queue theory info: Waiting and Sojourn Info
	 */
	/*private HashMap<String, Double> waitingTimeType;// = new HashMap<String, Double>();
	private HashMap<String, Double> sojournTimeType;// = new HashMap<String, Double>();
	*/
	//private int waitingTimeCounter;// = 0;
	//private int sojournTimeCounter;// = 0;
	
	
	
	public double getVal(int which, NewState engine) {
		
		//System.out.println("***** DEUS State evaluator 'Start'");
		//13/04/22 
	/*	this.reqReceivedType = new HashMap<String, Integer>();
		this.reqUnmetType = new HashMap<String, Integer>();
		this.reqSatisfiedType = new HashMap<String, Integer>();
		
		this.waitingTimeType = new HashMap<String, Double>();
		this.sojournTimeType = new HashMap<String, Double>();
		*/
		this.appTypeStatistics = new HashMap<String, AppTypeStatistics>();
		//this.waitingTimeCounter = 0;
		//this.sojournTimeCounter = 0;
		//13/04/22
		
		NodeList nodes = new NodeList();
		//System.out.println("nodeList size " + nodes);
		ArrayList<AcaasAgent> acaasAgents = nodes.getAcaasAgentList();
		
		int ownAppReq = 0;
		int unmetCounter = 0;
		int hitCounter = 0;
		int remoteReqAccepted = 0;
		int remoteReqRefused = 0;
		
		int runningApp = 0;
		//System.out.println("###########SEED: " + Engine.getDefault().getCurrentSeed());
		//counts the nodes that actively participate in the network (the ones that have executed or are running at least one task)
		int activeNodes = 0;
		//System.out.println("acaas number " + acaasAgents.size());
		//evaluate the node that have executed more and the one that have executed less App 
		//evaluated according the executed and the running apps
		int minExecApp = Integer.MAX_VALUE;
		int maxExecApp = Integer.MIN_VALUE;
		
		ArrayList<Integer> executedAndRunningApps = new ArrayList<Integer>();
		
		for (AcaasAgent a : acaasAgents){
			ownAppReq += ((AcaasNodeFeature)a.getFeature()).getOwnAppReq();
			unmetCounter += ((AcaasNodeFeature)a.getFeature()).getUnmetCounter();
			hitCounter += ((AcaasNodeFeature)a.getFeature()).getHitCounter();
			remoteReqAccepted += ((AcaasNodeFeature)a.getFeature()).getRemoteReqAccepted();
			remoteReqRefused += ((AcaasNodeFeature)a.getFeature()).getRemoteReqRefused();
			
			runningApp += ((AcaasNodeFeature)a.getFeature()).getAppQueue().size();
			
			//waitingTimeCounter += ((AcaasNodeFeature)a.getFeature()).getWaitingTimeCounter();
			//sojournTimeCounter += ((AcaasNodeFeature)a.getFeature()).getSojournTimeCounter();
			
			executedAndRunningApps.add(((AcaasNodeFeature)a.getFeature()).getHitCounter()  + ((AcaasNodeFeature)a.getFeature()).getAppQueue().size());
			
			if (  (((AcaasNodeFeature)a.getFeature()).getAppQueue().size() > 0) || (((AcaasNodeFeature)a.getFeature()).getHitCounter() > 0) )
				activeNodes++;
			
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
			}
			
			//check
			if ((y != ((AcaasNodeFeature)a.getFeature()).getUnmetCounter()) || (x != ((AcaasNodeFeature)a.getFeature()).getHitCounter() )){
				System.err.println("Counter missmatch");
				System.out.println("reqUnmet " + y + " where " + ((AcaasNodeFeature)a.getFeature()).getUnmetCounter());
				System.out.println("reqSatisfied " + x + " where " + ((AcaasNodeFeature)a.getFeature()).getHitCounter());
				System.exit(1);
			}
			
		}
		//System.out.println("HitCounter " + hitCounter + ", RunnningApp " + runningApp + ", activeNodes " + activeNodes);
		/*System.out.println("ownAppReq: " + ownAppReq);
		System.out.println("unmetApp: " + unmetCounter);
		System.out.println("hitApp: " + hitCounter);
		System.out.println("remoteAppReqAccepted: " + remoteReqAccepted);
		System.out.println("runningApp: " + runningApp);
	*/
		
		/**
		System.out.println("***Hit (and on execution) : " +  (1.0-((double)unmetCounter/(double)ownAppReq)));
		System.out.println("***TOT remoteReqAccepted (migrated): " + remoteReqAccepted);
		System.out.println("***useless messages ratio: " + ((double)(ownAppReq-remoteReqAccepted))/(double)ownAppReq);
		**/
		
		/*
		Set<String> appKeys = this.appTypeStatistics.keySet();
		Iterator<String> appKeyIter = appKeys.iterator();
		ArrayList<String> appTypes = new ArrayList<String>();
		while(appKeyIter.hasNext()) {
			appTypes.add(appKeyIter.next());
			
		}*/
		
		//FIXME: settato in modo diretto e non preso dinamicamente !!!! Altrimenti nei primi step uno dei tipi di app potrebbe non essere presente e si ha errore
		String appSmall = "applicationSmall";//appKeyIter.next();
		String appLarge = "applicationLarge";//appKeyIter.next();
		
	/*	String appSmall = null;
		String appLarge = null;
		if (appTypes.size()>0){
			appSmall = appTypes.get(0);
		}
		if (appTypes.size()>1){
			appLarge = appTypes.get(1);
		}
		*/
		//double hitAndExec = (1.0-((double)unmetCounter/(double)ownAppReq));
		double meanExecutedTaskPerNode;
		if (activeNodes == 0)
			meanExecutedTaskPerNode = 0;
		else
			meanExecutedTaskPerNode = (double) (hitCounter+runningApp) / (double) activeNodes;
		
		
		double varianceExecutedTask = this.executedTaskVariance(executedAndRunningApps, meanExecutedTaskPerNode);
		
		double hitAndExec;
		if (ownAppReq ==0)
			hitAndExec = 1;
		else 
			hitAndExec= (1.0-((double)unmetCounter/(double)ownAppReq));
		
		//System.out.println("***** DEUS State evaluator 'Finish'");
		switch(which){
		
			case 11:
				if ( (this.appTypeStatistics.get(appSmall) != null) && (this.appTypeStatistics.get(appSmall).getReqUnmet() != 0) ) {
					//System.out.println("***Hit (and on execution) rate for " + appSmall + ": " + (1.0-((double)this.appTypeStatistics.get(appSmall).getReqUnmet()/ ((double)this.appTypeStatistics.get(appSmall).getReqReceived())) ));
					return (1.0-((double)this.appTypeStatistics.get(appSmall).getReqUnmet()/ ((double)this.appTypeStatistics.get(appSmall).getReqReceived())));
				}
				else
					return 1.0;
				
			case 12:
				if ((this.appTypeStatistics.get(appLarge) != null) && (this.appTypeStatistics.get(appLarge).getReqUnmet() != 0) ) {
					//System.out.println("***Hit (and on execution) rate for " + appLarge + ": " + (1.0-((double)this.appTypeStatistics.get(appLarge).getReqUnmet()/ ((double)this.appTypeStatistics.get(appLarge).getReqReceived())) ));
					return (1.0-((double)this.appTypeStatistics.get(appLarge).getReqUnmet()/ ((double)this.appTypeStatistics.get(appLarge).getReqReceived())));
				}
				else 
					return 1.0;
			case 13:
				//System.out.println("***Hit (and on execution) : " +  hitAndExec);
				return hitAndExec;
			case 14:
				System.out.println("***TOT remoteReqAccepted (migrated): " + remoteReqAccepted);
				return remoteReqAccepted;
			case 15:
				if ( (remoteReqRefused + remoteReqAccepted) != 0){
					System.out.println(("***useless messages ratio: " + ((double)remoteReqRefused)/(double)(remoteReqRefused + remoteReqAccepted)));
					//System.out.println("***useless messages ratio: " + ((double)(ownAppReq-remoteReqAccepted))/(double)ownAppReq);
					//return (((double)(ownAppReq-remoteReqAccepted))/(double)ownAppReq);
					return ((double)remoteReqRefused)/((double)(remoteReqRefused + remoteReqAccepted));
				}
				else
					return 0.0;
			case 16: //It considers the executed and the running tasks
				System.out.println("mean task per node : " + meanExecutedTaskPerNode);
				return meanExecutedTaskPerNode;
			case 17:
				System.out.println("MaxTasksPerNode : " + maxExecApp);
				return maxExecApp;
			case 18:
				System.out.println("MinTasksPerNode : " + minExecApp);
				return minExecApp;
			case 19:
				if ((this.appTypeStatistics.get(appSmall) != null ) && (this.appTypeStatistics.get(appSmall).getWaitingTime() != 0) ){
					double overallWaitingTimeType = this.appTypeStatistics.get(appSmall).getWaitingTime();
					int overallWaitingTimeTypeCounter = this.appTypeStatistics.get(appSmall).getWaitingTimeCounter(); 
					System.out.println("***Mean Waiting for " + appSmall + ": " + (float)(overallWaitingTimeType/(float)overallWaitingTimeTypeCounter));
					return (float)(overallWaitingTimeType/(float)overallWaitingTimeTypeCounter);
				}
				else 
					return 0.0;
			case 20:
				if ((this.appTypeStatistics.get(appSmall) != null ) && (this.appTypeStatistics.get(appSmall).getSojournTime() != 0) ){
					double overallSojournTimeType = this.appTypeStatistics.get(appSmall).getSojournTime();
					int overallSojournTimeTypeCounter = this.appTypeStatistics.get(appSmall).getSojournTimeCounter();
					System.out.println("***Mean Sojourn for " + appSmall + ": " + (float)(overallSojournTimeType/(float)overallSojournTimeTypeCounter));
					return (float)(overallSojournTimeType/(float)overallSojournTimeTypeCounter); 
				}
				else
					return 0.0;
				
			case 21:
				if ((this.appTypeStatistics.get(appLarge) != null) && (this.appTypeStatistics.get(appLarge).getWaitingTime() != 0) ){
					double overallWaitingTimeType = this.appTypeStatistics.get(appLarge).getWaitingTime();
					int overallWaitingTimeTypeCounter = this.appTypeStatistics.get(appLarge).getWaitingTimeCounter(); 
					System.out.println("***Mean Waiting for " + appLarge + ": " + (float)(overallWaitingTimeType/(float)overallWaitingTimeTypeCounter));
					return (float)(overallWaitingTimeType/(float)overallWaitingTimeTypeCounter);
				}
				else 
					return 0.0;
			case 22:
				if ((this.appTypeStatistics.get(appLarge) != null) && (this.appTypeStatistics.get(appLarge).getSojournTime() != 0) ){
					double overallSojournTimeType = this.appTypeStatistics.get(appLarge).getSojournTime();
					int overallSojournTimeTypeCounter = this.appTypeStatistics.get(appLarge).getSojournTimeCounter();
					System.out.println("***Mean Sojourn for " + appLarge + ": " + (float)(overallSojournTimeType/(float)overallSojournTimeTypeCounter));
					return (float)(overallSojournTimeType/(float)overallSojournTimeTypeCounter); 
				}
				else
					return 0.0;	
				
			case 23:
				System.out.println("variance task per node : " + varianceExecutedTask);
				return varianceExecutedTask;
				
			case 815:
				System.out.println("***TOT remoteReq: " + remoteReqAccepted + remoteReqRefused);
				return (remoteReqAccepted + remoteReqRefused);
				
//			case 51:
//				if ( (this.appTypeStatistics.get(appSmall) != null) && (this.appTypeStatistics.get(appSmall).getReqUnmet() != 0) ) {
//					System.out.println("atVT " + Engine.getDefault().getVirtualTime() + " appSmall " + this.appTypeStatistics.get(appSmall).getReqReceived());
//					return ((double)this.appTypeStatistics.get(appSmall).getReqReceived());
//				}
//			case 50:
//				//if (Engine.getDefault().getVirtualTime() <= Engine.getDefault().getMaxVirtualTime() )
//				if ((Engine.getDefault().getVirtualTime() <= Engine.getDefault().getMaxVirtualTime() && Engine.getDefault().getEventsList().size() > 0)) 
//					return 1.0;
//				else
//					return 0.0;
			default:
				return 0.0;
		}
		
		
		
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
	
	public void updateSojournTime(String type, double addition, int counter){
		if (!this.appTypeStatistics.containsKey(type))
			this.appTypeStatistics.put(type, new AppTypeStatistics(type));
		
		this.appTypeStatistics.get(type).addSojournTime(addition);
		//(counter-1) since the first one is just added when the sojourn time is updated
		this.appTypeStatistics.get(type).addSojournTimeCounter( (counter-1));
	}
	
	private double executedTaskVariance(ArrayList<Integer> values, double mean){
		
		double variance = 0;
		
		for (int v : values){
			variance += Math.pow((v-mean), 2);
		}
		
		variance = variance / (values.size()-1);
		
		return variance;
		
	}



	@Override
	public double getVal(String arg0, NewState arg1) {
		// TODO Auto-generated method stub
		return 0;
	}
}
