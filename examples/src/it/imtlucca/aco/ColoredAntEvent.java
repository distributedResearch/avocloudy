package it.imtlucca.aco;

import java.util.Properties;

import it.imtlucca.aco.autonomicLayer.AcaasAcoNodeBehavior;
import it.imtlucca.aco.autonomicLayer.AcaasAcoNodePolicy;
import it.imtlucca.aco.autonomicLayer.ColoredAntPolicy;
import it.imtlucca.cloudyscience.DataCenterNode;
import it.imtlucca.cloudyscience.VolunteerNode;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;
import it.imtlucca.cloudyscience.util.Const;
import it.imtlucca.cloudyscience.util.Exp;
import it.imtlucca.cloudyscience.util.IFunction;
import it.imtlucca.cloudyscience.util.Line;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.NodeEvent;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

/**
 * The colored ant definition and action
 * 
 * @author Stefano Sebastio
 *
 */
public class ColoredAntEvent extends NodeEvent {

	
	public IFunction antFunction = null;
	public IFunction antAgingFunction = null;
	public IFunction temperatureFunction = null;
	
	public int color;
	public static final String ANT_COLOR = "antColor";
	public static final String CPU_CORE = "cpuCore";
	public static final String CPU_FREQ = "cpuFreq";
	public static final String MAIN_MEMORY = "mainMemory";
	public static final String FINISHING_TIME = "finishingTime";
	
	private static final String CONST = "const";
	private static final String LINE = "line";
	private static final String EXP = "exp";
	
	public double pheromone_a = 1;
	private static final String PHEROMONE_A = "pheromone_a";
	public double pheromone_b = 1;
	private static final String PHEROMONE_B = "pheromone_b";
	public double pheromone_c = 1;
	private static final String PHEROMONE_C = "pheromone_c";
	private static final String ANT = "ant";

	public int ttl = 3; 
	private static final String TTL = "ttl";
	
	public double initPheromone = 1;
	private static final String INIT_PHEROMONE = "initPheromone";
	
	private double evaporation = 0.1;
	private static final String EVAPORATION = "evaporation";
	
	public double pheromoneAging_a = 1;
	private static final String PHEROMONE_AGING_A = "pheromoneAging_a";
	public double pheromoneAging_b = -1;
	private static final String PHEROMONE_AGING_B = "pheromoneAging_b";	
	public double pheromoneAging_c = 1;
	private static final String PHEROMONE_AGING_C = "pheromoneAging_c";
	private static final String ANT_AGING_FUNC = "agingFunc";
	
	
	public double temperature_a = 1;
	private static final String TEMPERATURE_A = "temperature_a";
	public double temperature_b = 1;
	private static final String TEMPERATURE_B = "temperature_b";
	public double temperature_c = 1;
	private static final String TEMPERATURE_C = "temperature_c";
	private static final String TEMPERATURE_FUNC = "temperatureFunc";
	
	
	
	
	
	public ColoredAntEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
		
		
		if (params.getProperty(ANT) != null) {
			
			if(params.getProperty(ANT_COLOR) == null)
				throw new InvalidParamsException(ANT_COLOR + " Undefined ant color...");
			try {
				if (params.getProperty(ANT_COLOR).equals(MAIN_MEMORY)){
					color = AntColor.MAIN_MEMORY;
				}
				else if (params.getProperty(ANT_COLOR).equals(CPU_FREQ)){
					color = AntColor.CPU_FREQ;
				}
				else if (params.getProperty(ANT_COLOR).equals(CPU_CORE)){
					color = AntColor.CPU_CORE;
				}
				else if (params.getProperty(ANT_COLOR).equals(FINISHING_TIME)){
					color = AntColor.FINISHING_TIME;
				}
			} catch (NumberFormatException ex){
				throw new InvalidParamsException(ANT_COLOR + " must be a valid ant color");
			}
			
			//System.out.println("color " + color);
			
			if ( (params.getProperty(TTL)==null) || (params.getProperty(PHEROMONE_A)==null) || (params.getProperty(INIT_PHEROMONE)==null) )
				throw new InvalidParamsException("Since " + ANT + " is setted " + TTL + " " + PHEROMONE_A + " " + INIT_PHEROMONE + " params are expeced.");
			try {
				ttl = Integer.parseInt(params.getProperty(TTL));
				pheromone_a = Double.parseDouble(params.getProperty(PHEROMONE_A));
				initPheromone = Double.parseDouble(params.getProperty(INIT_PHEROMONE));
				
				if ((color == AntColor.FINISHING_TIME) && 
						( (params.getProperty(EVAPORATION) == null) || (params.getProperty(PHEROMONE_AGING_A)==null) || (params.getProperty(ANT_AGING_FUNC) == null) ) ) {
					throw new InvalidParamsException("Since " + ANT + " is a FinishingTime ant " + EVAPORATION + " " + PHEROMONE_AGING_A + " " + ANT_AGING_FUNC + " params are expeced.");
				} else {
				
					if (params.getProperty(EVAPORATION) != null)//{
						evaporation = Double.parseDouble(params.getProperty(EVAPORATION));
						
					if (params.getProperty(ANT_AGING_FUNC) != null) {
					
						pheromoneAging_a = Double.parseDouble(params.getProperty(PHEROMONE_AGING_A));
					
						if (params.getProperty(ANT_AGING_FUNC).equals(CONST)){
							antAgingFunction = new Const(pheromoneAging_a);
							//FIXME: to skip the check on b<0
							pheromoneAging_b = -1; 
						}
						else if (params.getProperty(ANT_AGING_FUNC).equals(LINE) || params.getProperty(ANT_AGING_FUNC).equals(EXP)){
							
							if (params.getProperty(PHEROMONE_AGING_B) == null)
								throw new InvalidParamsException(PHEROMONE_AGING_B + " param is expected.");
							try {
								pheromoneAging_b = Double.parseDouble(params.getProperty(PHEROMONE_AGING_B));
								
								if (params.getProperty(ANT_AGING_FUNC).equals(LINE))
									antAgingFunction = new Line(pheromoneAging_a,  pheromoneAging_b);
								else if (params.getProperty(ANT_AGING_FUNC).equals(EXP)){
									if (params.getProperty(PHEROMONE_AGING_C) != null){
										
										try {
											pheromoneAging_c = Double.parseDouble(params.getProperty(PHEROMONE_AGING_C));
											antAgingFunction = new Exp(pheromoneAging_a, pheromoneAging_b, pheromone_c);
										} catch (NumberFormatException ex){
											throw new InvalidParamsException(PHEROMONE_AGING_C + " must be a valid double value.");
										}
									}
									else 
										antAgingFunction = new Exp(pheromoneAging_a, pheromoneAging_b);
								}
							}
							catch (NumberFormatException ex){
								throw new InvalidParamsException(PHEROMONE_AGING_B + " must be a valid double value.");
							}
							
						}
					}
					//}
				}
				
				
				
				if (params.getProperty(ANT).equals(CONST)){
					antFunction = new Const(pheromone_a);
					//FIXME: to skip the check on b<0
					pheromone_b = -1; 
				}
				else if (params.getProperty(ANT).equals(LINE) || params.getProperty(ANT).equals(EXP)){
					
					if (params.getProperty(PHEROMONE_B) == null)
						throw new InvalidParamsException(PHEROMONE_B + " param is expected.");
					try {
						pheromone_b = Double.parseDouble(params.getProperty(PHEROMONE_B));
							
						if (params.getProperty(ANT).equals(LINE))
							antFunction = new Line(pheromone_a, pheromone_b);
						else if (params.getProperty(ANT).equals(EXP)){
						
							if (params.getProperty(PHEROMONE_C) !=null){
								try {
									pheromone_c = Double.parseDouble(params.getProperty(PHEROMONE_C));
									antFunction = new Exp(pheromone_a, pheromone_b, pheromone_c);
								} catch  (NumberFormatException ex){
									throw new InvalidParamsException(PHEROMONE_C + " must be a valid double value.");
								}
							}
							else
								antFunction = new Exp(pheromone_a, pheromone_b);
							
						}
					} catch (NumberFormatException ex){
						throw new InvalidParamsException(PHEROMONE_B + " must be a valid double value.");
					}
				}
				else
					throw new InvalidParamsException(ANT + " must be a valid ant function.");
				
				
				
				if (params.getProperty(TEMPERATURE_FUNC)!=null) { 
					

					if (params.getProperty(TEMPERATURE_A)==null)
						throw new InvalidParamsException("Since " + TEMPERATURE_FUNC + " is setted at least " + TEMPERATURE_A +  " param is expeced.");

					temperature_a = Double.parseDouble(params.getProperty(TEMPERATURE_A));
					
					if (params.getProperty(TEMPERATURE_FUNC).equals(CONST)){
						temperatureFunction = new Const(temperature_a);
					}
					else if (params.getProperty(TEMPERATURE_FUNC).equals(LINE) || params.getProperty(TEMPERATURE_FUNC).equals(EXP)){
						
						if (params.getProperty(TEMPERATURE_B) == null)
							throw new InvalidParamsException(TEMPERATURE_B + " param is expected.");
						try {
							temperature_b = Double.parseDouble(params.getProperty(TEMPERATURE_B));
								
							if (params.getProperty(TEMPERATURE_FUNC).equals(LINE))
								temperatureFunction = new Line(temperature_a, temperature_b);
							else if (params.getProperty(TEMPERATURE_FUNC).equals(EXP)){
							
								if (params.getProperty(TEMPERATURE_C) !=null){
									try {
										temperature_c = Double.parseDouble(params.getProperty(TEMPERATURE_C));
										temperatureFunction = new Exp(temperature_a, temperature_b, temperature_c);
									} catch  (NumberFormatException ex){
										throw new InvalidParamsException(TEMPERATURE_C + " must be a valid double value.");
									}
								}
								else
									temperatureFunction = new Exp(temperature_a, temperature_b);
								
							}
						} catch (NumberFormatException ex){
							throw new InvalidParamsException(TEMPERATURE_B + " must be a valid double value.");
						}
					}
					else
						throw new InvalidParamsException(TEMPERATURE_FUNC + " must be a valid ant function.");
					
				}
				
				
				
				
				
			} catch (NumberFormatException ex){
				throw new InvalidParamsException(TTL + " must be a valid int value. " + PHEROMONE_A + " " + INIT_PHEROMONE + " " + TEMPERATURE_A + " must be valid double values. Or could be errors in the format of other parameters");
			}
			
			this.checkFunction();
		}
		
		
	}

	/**
	 * Check the correctness of the ant functions (pheromone deposit and aging)
	 * 
	 * @return
	 * @throws InvalidParamsException
	 */
	private boolean checkFunction() throws InvalidParamsException{
		
		if (antFunction.getValue(0) == Double.POSITIVE_INFINITY){
			throw new InvalidParamsException("Ant Function must not goes to infinity at 0");
		}
		if (antAgingFunction != null){
			if (antAgingFunction.getValue(0) == Double.POSITIVE_INFINITY){
				throw new InvalidParamsException("Ant Aging Function must not goes to infinity at 0");
			}
		}
		
		if (color == AntColor.FINISHING_TIME){
			if ( (pheromone_b > 0) || (pheromoneAging_b > 0))
				throw new InvalidParamsException("the b-parameter must be negative for the finishingTime ant functions");
				
		}
		
		return true;
	}
		
	public void run() throws RunException {
		
		if (getParentProcess() == null)
			throw new RunException("A parent process must be set in order to run " + getClass().getCanonicalName());
		
		if ( getAssociatedNode() instanceof VolunteerNode){
			
			AcoVolunteerNode node = (AcoVolunteerNode) getAssociatedNode();

			if (node.getIaasAgent().getFeature().isOnline()){
			
				AcaasAcoNodePolicy policy = (AcaasAcoNodePolicy) node.getIaasAgent().getAcAgent().getPolicy();
		
				AcaasAcoNodeBehavior behavior = (AcaasAcoNodeBehavior) node.getIaasAgent().getAcAgent().getBehavior();
					
					if (!policy.isColoredReleaseFunction(color)){
					
						if (color == AntColor.FINISHING_TIME){
							policy.setAntPolicy(new ColoredAntPolicy(antFunction, initPheromone, ttl, temperatureFunction, evaporation, antAgingFunction), color);
						}
						else{
							policy.setAntPolicy(new ColoredAntPolicy(antFunction, initPheromone, ttl, temperatureFunction, antAgingFunction), color);
						}
						//System.out.println("setted " + policy);
						behavior.createColoredAnts(color);
					} 
				//	System.out.println("called antcolored init");
					behavior.initColoredAnt(behavior.getColoredAnt(color));
	 
			}
		}
		else if (getAssociatedNode() instanceof DataCenterNode) {
			AcoDataCenterNode nodeDc = (AcoDataCenterNode) getAssociatedNode();
			
			for (IaasAgent node : nodeDc.getIaasDCAgent().getIaasAgents()){
			
				if (node.getFeature().isOnline()){
					
					AcaasAcoNodePolicy policy = (AcaasAcoNodePolicy) node.getAcAgent().getPolicy();
			
					AcaasAcoNodeBehavior behavior = (AcaasAcoNodeBehavior) node.getAcAgent().getBehavior();
					
					if (!policy.isColoredReleaseFunction(color)){
					
						if (color == AntColor.FINISHING_TIME){
							policy.setAntPolicy(new ColoredAntPolicy(antFunction, initPheromone, ttl, temperatureFunction, evaporation, antAgingFunction), color);
						}
						else{
							policy.setAntPolicy(new ColoredAntPolicy(antFunction, initPheromone, ttl, temperatureFunction, antAgingFunction), color);
						}
						
						behavior.createColoredAnts(color);
					} 
					behavior.initColoredAnt(behavior.getColoredAnt(color));
		 
				}
			}
		}
	}

}
