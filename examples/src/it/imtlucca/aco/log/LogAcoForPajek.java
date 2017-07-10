package it.imtlucca.aco.log;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import it.imtlucca.aco.AntColor;
import it.imtlucca.aco.autonomicLayer.AcaasAcoNodeBehavior;
import it.imtlucca.aco.autonomicLayer.AcaasAcoNodeKnowledge;
import it.imtlucca.aco.autonomicLayer.AcaasAcoNodePolicy;
import it.imtlucca.cloudyscience.autonomicLayer.AcaasNodeFeature;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;
import it.imtlucca.cloudyscience.util.NodeList;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

/**
 * 
 * This event print the network topology in Pajek format where the links are
 * weighted according the pheromone and the nodes are sized according the number of tasks
 * that they have executed
 * 
 * Pajek file format:
 * http://vlado.fmf.uni-lj.si/pub/networks/pajek/doc/draweps.htm
 * 
 * @author Stefano Sebastio
 *
 */
public class LogAcoForPajek extends Event{

	private ArrayList<IaasAgent> nodes;
	
	private String pheromoneType;
	//overall, freq, memory, cpu, finishingTime
	public static final String PHEROMONE_TO_PLOT = "pheromoneToPlot";
	public static final String OVERALL = "overall";
	public static final String CPU_CORE = "cpu";
	public static final String CPU_FREQ = "freq";
	public static final String MAIN_MEMORY = "memory";
	public static final String FINISHING_TIME = "finishingTime";
	
	private boolean normalizeValues = false;
	private static final String NORMALIZED = "normalized";
	
	
	public LogAcoForPajek(String id, Properties params, Process parentProcess)
			throws InvalidParamsException {
		super(id, params, parentProcess);
		
		if (params.getProperty(PHEROMONE_TO_PLOT) != null){
			
			if (params.getProperty(PHEROMONE_TO_PLOT).equals(OVERALL)){
				pheromoneType = OVERALL;
			}
			else if (params.getProperty(PHEROMONE_TO_PLOT).equals(CPU_CORE)){
				pheromoneType = CPU_CORE;
			}
			else if (params.getProperty(PHEROMONE_TO_PLOT).equals(CPU_FREQ)){
				pheromoneType = CPU_FREQ;
			}
			else if (params.getProperty(PHEROMONE_TO_PLOT).equals(MAIN_MEMORY)){
				pheromoneType = MAIN_MEMORY;
			}
			else if (params.getProperty(FINISHING_TIME).equals(FINISHING_TIME)){
				pheromoneType = FINISHING_TIME;
			}
			else
				throw new InvalidParamsException(PHEROMONE_TO_PLOT + " must be a valid pheromone type");
		}
		
		if (params.getProperty(NORMALIZED) != null){
			try {
				normalizeValues = Boolean.parseBoolean(params.getProperty(NORMALIZED));
			}
			catch (NumberFormatException ex){
				throw new InvalidParamsException(NORMALIZED + " must be a valid boolean value.");
			}
		}
		
		
		this.nodes = null;
	}


	public void run() throws RunException {
		System.out.println("Logging ACO for Pajek...");
		getLogger().info("##### Network links:");
		
		NodeList nodesList = new NodeList();
		this.nodes = nodesList.getIaasAgentList();
		
		getLogger().info("*Vertices " + this.nodes.size());
		
		int k=1;
		
		for (IaasAgent node : nodes){
			
			int hit = ((AcaasNodeFeature)node.getAcAgent().getFeature()).getHitCounter();
			
				if (node.getFeature().isSupernode()){
					
					getLogger().info(k + " \"" + node.getFeature().getNodeId() + "\" ic Green bc Black x_fact " + hit + " y_fact " + hit);
					//System.out.println("Found a Supernode");
				} else{
					getLogger().info(k + " \"" + node.getFeature().getNodeId() + "\" ic Yellow bc Black x_fact " + hit + " y_fact " + hit);
				}
			k++;	

		}
		
		//FIXME: correggere i link dei datacenter 
		
		getLogger().info("*Arcs");
		
		
		for (IaasAgent node : this.nodes){
			
			if ( (node.getKnowledge() != null) && node.getKnowledge().getFeature().isOnline()){
				
				((AcaasAcoNodeBehavior)node.getAcAgent().getBehavior()).checkAllPheromonePath();
				
				//TODO: vedere se e' corretto averlo commentato
				/*
				//Print supernodes
				for (int i=0; i<node.getKnowledge().getSupernodes().size(); i++){
					//if (node.getKnowledge().getFeature().isOnline() && node.getKnowledge().getSupernodes().get(i).getKnowledge().getFeature().isOnline()){
					if (node.getKnowledge().getSupernodes().get(i).getKnowledge().getFeature().isOnline()){
						double weight = ((AcaasAcoNodeKnowledge) node.getAcAgent().getKnowledge()).getPheromone(node.getKnowledge().getSupernodes().get(i)).getPheromone();
						//getLogger().info(q + " " + node.nodeId + " " + node.getNodeKnwoledge().getSupernodes().get(i).nodeId + " c Black");
						//getLogger().info(q + " " +node.nodeId+ "-" +(nodes.indexOf(node)+1) + " " +node.getNodeKnwoledge().getSupernodes().get(i).nodeId +"-"+(nodes.indexOf(node.getNodeKnwoledge().getSupernodes().get(i))+1) + " c Black");
						//getLogger().info(q + " " + (findIndexFromId(nodes, node.nodeId)+1) + " " + (findIndexFromId(nodes, node.getNodeKnwoledge().getSupernodes().get(i).nodeId)+1) + " c Black");
						getLogger().info((findIndexFromId(nodes, node.getFeature().getNodeId())+1) + " " + (findIndexFromId(nodes, node.getKnowledge().getSupernodes().get(i).getFeature().getNodeId())+1) + " " + weight + " c Black");
						
						//q++;
					}
				}*/
				//  source destination weight "label"
				Hashtable<Integer, Double> pheromoneSums = null;
				if (normalizeValues){
					pheromoneSums = sumPheromonePerNode(node);
				}
				
				//Print all pheromone
				AcaasAcoNodePolicy policy = (AcaasAcoNodePolicy)node.getAcAgent().getPolicy();
				for ( IaasAgent n : ((AcaasAcoNodeKnowledge)node.getAcAgent().getKnowledge()).getPheromonePath().keySet()){
					
					if (n.getKnowledge().getFeature().isOnline()){
						
						double weight = ((AcaasAcoNodeKnowledge) node.getAcAgent().getKnowledge()).getPheromone(n).getPheromone(policy); 
						System.out.println(findNode(node) + " -> " + findNode(n) + " = " + weight);
						//TODO: decidere cosa plottare
						
						if (pheromoneType.equals(OVERALL)){
							if (normalizeValues){
								weight /= pheromoneSums.get(-1);
							}
						
							if (node.getFeature().getZoneOfBelonging() == n.getFeature().getZoneOfBelonging())
								getLogger().info( findNode(node) + " " + findNode(n) + " " + weight + " c Red");
							else {
								getLogger().info( findNode(node) + " " + findNode(n) + " " + weight + " c Yellow");
							}
						}
					
						if ( ((AcaasAcoNodePolicy)node.getAcAgent().getPolicy()).isColoredReleaseFunction(AntColor.CPU_CORE)){
							double cpuPheromone = (((AcaasAcoNodeKnowledge) node.getAcAgent().getKnowledge()).getPheromone(n).getColoredPheromone(policy, AntColor.CPU_CORE));
							

							if (pheromoneType.equals(CPU_CORE)){
								if (normalizeValues){
									cpuPheromone /= pheromoneSums.get(AntColor.CPU_CORE);
								}
								
								System.out.println("cpu: " + findNode(node) + " -> " + findNode(n) + " = " + cpuPheromone);
								if (node.getFeature().getZoneOfBelonging() == n.getFeature().getZoneOfBelonging())
									getLogger().info( findNode(node) + " " + findNode(n) + " " + cpuPheromone + " c Red");
								else {
									getLogger().info( findNode(node) + " " + findNode(n) + " " + cpuPheromone + " c Yellow");
								}
							}
						}

						if ( ((AcaasAcoNodePolicy)node.getAcAgent().getPolicy()).isColoredReleaseFunction(AntColor.CPU_FREQ)){
							double cpuFreqPheromone = (((AcaasAcoNodeKnowledge) node.getAcAgent().getKnowledge()).getPheromone(n).getColoredPheromone(policy, AntColor.CPU_FREQ));
							

							if (pheromoneType.equals(CPU_FREQ)){
								if (normalizeValues){
									cpuFreqPheromone /= pheromoneSums.get(AntColor.CPU_FREQ);
								}
								
								System.out.println("cpuFreq: " + findNode(node) + " -> " + findNode(n) + " = " + cpuFreqPheromone);
								if (node.getFeature().getZoneOfBelonging() == n.getFeature().getZoneOfBelonging())
									getLogger().info( findNode(node) + " " + findNode(n) + " " + cpuFreqPheromone + " c Red");
								else {
									getLogger().info( findNode(node) + " " + findNode(n) + " " + cpuFreqPheromone + " c Yellow");
								}
							}
						}

						if ( ((AcaasAcoNodePolicy)node.getAcAgent().getPolicy()).isColoredReleaseFunction(AntColor.MAIN_MEMORY)){
							double mainMemPheromone = (((AcaasAcoNodeKnowledge) node.getAcAgent().getKnowledge()).getPheromone(n).getColoredPheromone(policy, AntColor.MAIN_MEMORY));
							

							if (pheromoneType.equals(MAIN_MEMORY)){
								if (normalizeValues){
									mainMemPheromone /= pheromoneSums.get(AntColor.MAIN_MEMORY);
								}
								
								System.out.println("mainMem(ram): " + findNode(node) + " -> " + findNode(n) + " = " + mainMemPheromone);
								//getLogger().info( findNode(node) + " " + findNode(n) + " " + mainMemPheromone + " c Red");
								if (node.getFeature().getZoneOfBelonging() == n.getFeature().getZoneOfBelonging())
									getLogger().info( findNode(node) + " " + findNode(n) + " " + mainMemPheromone + " c Red");
								else {
									getLogger().info( findNode(node) + " " + findNode(n) + " " + mainMemPheromone + " c Yellow");
								}
							}
						}

						if ( ((AcaasAcoNodePolicy)node.getAcAgent().getPolicy()).isColoredReleaseFunction(AntColor.FINISHING_TIME)){
							double finishingTimePheromone = (((AcaasAcoNodeKnowledge) node.getAcAgent().getKnowledge()).getPheromone(n).getColoredPheromone(policy, AntColor.FINISHING_TIME));
							

							if (pheromoneType.equals(FINISHING_TIME)){
								if (normalizeValues){
									finishingTimePheromone /= pheromoneSums.get(AntColor.FINISHING_TIME);
								}
								
								System.out.println("finishingTime: " + findNode(node) + " -> " + findNode(n) + " = " + finishingTimePheromone);
								if (node.getFeature().getZoneOfBelonging() == n.getFeature().getZoneOfBelonging())
									getLogger().info( findNode(node) + " " + findNode(n) + " " + finishingTimePheromone + " c Red");
								else {
									getLogger().info( findNode(node) + " " + findNode(n) + " " + finishingTimePheromone + " c Yellow");
								}
							}
						}
						
//						if (node.getFeature().getZoneOfBelonging() == n.getFeature().getZoneOfBelonging())
//							getLogger().info( findNode(node) + " " + findNode(n) + " " + weight + "\"<" + cpuPheromone +", " + cpuFreqPheromone + ", " + mainMemPheromone + ", " + finishingTimePheromone +">\" c Red");
//						else
//							getLogger().info( findNode(node) + " " + findNode(n) + " " + weight + "\"<" + cpuPheromone +", " + cpuFreqPheromone + ", " + mainMemPheromone + ", " + finishingTimePheromone +">\" c Brown");
//						
					}
					
				}
			
			}
		
		}
		
		System.out.println("Logging for Pajek completed.");
	}

	
	/**
	 * Retrieve the index of a node used by Pajek from the nodes list 
	 * 
	 * @param a
	 * @return
	 */
	private int findNode(IaasAgent a){
		
		int index = this.nodes.indexOf(a);
		
		if (index == -1){
			System.err.println(a.getFeature().getNodeId() + " not found");
		}
		
		return (index+1);
		
	}
	
	/**
	 * Sum all the pheromones values by type, to use for normalization 
	 * 
	 * 
	 * @param node
	 * @return an hashtable with the pheromone sums (the key "-1" is the overall pheromone)
	 */
	private Hashtable<Integer, Double> sumPheromonePerNode(IaasAgent node){
		
		double overall=0;
		double cpu=0;
		double freq=0;
		double memory=0;
		double finishingTime=0;
		
		Hashtable<Integer, Double> pheromoneSums = new Hashtable<Integer, Double>();
		
		AcaasAcoNodePolicy policy = (AcaasAcoNodePolicy)node.getAcAgent().getPolicy();
		for ( IaasAgent n : ((AcaasAcoNodeKnowledge)node.getAcAgent().getKnowledge()).getPheromonePath().keySet()){
			
			
			overall += ((AcaasAcoNodeKnowledge) node.getAcAgent().getKnowledge()).getPheromone(n).getPheromone(policy); 
			cpu += (((AcaasAcoNodeKnowledge) node.getAcAgent().getKnowledge()).getPheromone(n).getColoredPheromone(policy, AntColor.CPU_CORE));
			freq += (((AcaasAcoNodeKnowledge) node.getAcAgent().getKnowledge()).getPheromone(n).getColoredPheromone(policy, AntColor.CPU_FREQ));
			memory += (((AcaasAcoNodeKnowledge) node.getAcAgent().getKnowledge()).getPheromone(n).getColoredPheromone(policy, AntColor.MAIN_MEMORY));
			finishingTime += (((AcaasAcoNodeKnowledge) node.getAcAgent().getKnowledge()).getPheromone(n).getColoredPheromone(policy, AntColor.FINISHING_TIME));
			
		}
		
		pheromoneSums.put(-1, overall);
		pheromoneSums.put(AntColor.CPU_CORE, cpu);
		pheromoneSums.put(AntColor.CPU_FREQ, freq);
		pheromoneSums.put(AntColor.MAIN_MEMORY, memory);
		pheromoneSums.put(AntColor.FINISHING_TIME, finishingTime);
		
		return pheromoneSums;
	}
}
