package it.imtlucca.spatialaco;

import java.util.Properties;

import it.imtlucca.aco.AcoDataCenterNode;
import it.imtlucca.aco.AcoVolunteerNode;
import it.imtlucca.aco.AntColor;
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
import it.imtlucca.spatialaco.autonomicLayer.AcaasSpatialAcoNodeBehavior;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.NodeEvent;
import it.unipr.ce.dsg.deus.core.Process;
import it.unipr.ce.dsg.deus.core.RunException;

/**
 * The periodic pulse definition and action
 * 
 * @author Stefano Sebastio
 *
 */
public class PeriodicPulseEvent extends NodeEvent {

	
	public IFunction antFunction = null;
	public IFunction antAgingFunction = null;
	public IFunction temperatureFunction = null;
	
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
	
	public double initPheromoneMemory = 1;
	private static final String INIT_PHEROMONE_MEMORY = "initPheromoneMemory";
	public double initPheromoneCpuCore = 1;
	private static final String INIT_PHEROMONE_CPUCORE = "initPheromoneCpuCore";
	public double initPheromoneCpuFreq = 1;
	private static final String INIT_PHEROMONE_CPUFREQ = "initPheromoneCpuFreq";
	public double initPheromoneFinishingTime = 1;
	private static final String INIT_PHEROMONE_FINISHINGTIME = "initPheromoneFinishingTime";
	
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
	
	
	
	
	
	public PeriodicPulseEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
		
		
		if (params.getProperty(ANT) != null) {
			
			
			if (params.getProperty(PHEROMONE_A)==null)
				throw new InvalidParamsException("Since PeriodicResourcePulse is setted " + PHEROMONE_A + " params is expeced.");
			try {
				pheromone_a = Double.parseDouble(params.getProperty(PHEROMONE_A));
				if (params.getProperty(INIT_PHEROMONE_MEMORY) != null)
					initPheromoneMemory = Double.parseDouble(params.getProperty(INIT_PHEROMONE_MEMORY));
				if (params.getProperty(INIT_PHEROMONE_CPUCORE) != null)
					initPheromoneCpuCore = Double.parseDouble(params.getProperty(INIT_PHEROMONE_CPUCORE));
				if (params.getProperty(INIT_PHEROMONE_CPUFREQ) != null)
					initPheromoneCpuFreq = Double.parseDouble(params.getProperty(INIT_PHEROMONE_CPUFREQ));
				if (params.getProperty(INIT_PHEROMONE_FINISHINGTIME) != null)
					initPheromoneFinishingTime = Double.parseDouble(params.getProperty(INIT_PHEROMONE_FINISHINGTIME));
				
				if ((params.getProperty(PHEROMONE_AGING_A)==null) || (params.getProperty(ANT_AGING_FUNC) == null) ) {
					throw new InvalidParamsException("Since PeriodicResourcePulse is setted " + PHEROMONE_AGING_A + " " + ANT_AGING_FUNC + " params are expeced.");
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
				throw new InvalidParamsException("Errors in the format of some PeriodicResourcePulse parameters");
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
										
				if (initPheromoneFinishingTime > 0) {
					if ( !policy.isColoredReleaseFunction(AntColor.FINISHING_TIME)){
						policy.setAntPolicy(new ColoredAntPolicy(antFunction, initPheromoneFinishingTime, 1, temperatureFunction, evaporation, antAgingFunction), AntColor.FINISHING_TIME);
						behavior.createColoredAnts(AntColor.FINISHING_TIME);
					}
					behavior.initColoredAnt(behavior.getColoredAnt(AntColor.FINISHING_TIME));
				}
				if (initPheromoneMemory > 0 ) {
					if (!policy.isColoredReleaseFunction(AntColor.MAIN_MEMORY)){
						policy.setAntPolicy(new ColoredAntPolicy(antFunction, initPheromoneMemory, 1, temperatureFunction, antAgingFunction), AntColor.MAIN_MEMORY);
						behavior.createColoredAnts(AntColor.MAIN_MEMORY);
					}
					behavior.initColoredAnt(behavior.getColoredAnt(AntColor.MAIN_MEMORY));
				}
				if (initPheromoneCpuCore > 0 ) {
					if (!policy.isColoredReleaseFunction(AntColor.CPU_CORE)){
						policy.setAntPolicy(new ColoredAntPolicy(antFunction, initPheromoneCpuCore, 1, temperatureFunction, antAgingFunction), AntColor.CPU_CORE);
						behavior.createColoredAnts(AntColor.CPU_CORE);
					}
					behavior.initColoredAnt(behavior.getColoredAnt(AntColor.CPU_CORE));
				}
				if (initPheromoneCpuFreq > 0 ) {
					if (!policy.isColoredReleaseFunction(AntColor.CPU_FREQ)){
						policy.setAntPolicy(new ColoredAntPolicy(antFunction, initPheromoneCpuFreq, 1, temperatureFunction, antAgingFunction), AntColor.CPU_FREQ);
						behavior.createColoredAnts(AntColor.CPU_FREQ);
					}
					behavior.initColoredAnt(behavior.getColoredAnt(AntColor.CPU_FREQ));
				}
				//System.out.println("setted " + policy); 
				//	System.out.println("called antcolored init");
			}
		}
		else if (getAssociatedNode() instanceof DataCenterNode) {
			AcoDataCenterNode nodeDc = (AcoDataCenterNode) getAssociatedNode();
			
			for (IaasAgent node : nodeDc.getIaasDCAgent().getIaasAgents()){
			
				if (node.getFeature().isOnline()){
					
					AcaasAcoNodePolicy policy = (AcaasAcoNodePolicy) node.getAcAgent().getPolicy();
			
					AcaasSpatialAcoNodeBehavior behavior = (AcaasSpatialAcoNodeBehavior) node.getAcAgent().getBehavior();
					
					if (initPheromoneFinishingTime > 0) {
						if ( !policy.isColoredReleaseFunction(AntColor.FINISHING_TIME)){
							policy.setAntPolicy(new ColoredAntPolicy(antFunction, initPheromoneFinishingTime, 1, temperatureFunction, evaporation, antAgingFunction), AntColor.FINISHING_TIME);
							behavior.createColoredAnts(AntColor.FINISHING_TIME);
						}
						//behavior.initPeriodicPulse(behavior.getColoredAnt(AntColor.FINISHING_TIME));
					}
					if (initPheromoneMemory > 0 ) {
						if (!policy.isColoredReleaseFunction(AntColor.MAIN_MEMORY)){
							policy.setAntPolicy(new ColoredAntPolicy(antFunction, initPheromoneMemory, 1, temperatureFunction, antAgingFunction), AntColor.MAIN_MEMORY);
							behavior.createColoredAnts(AntColor.MAIN_MEMORY);
						}
						//behavior.initPeriodicPulse(behavior.getColoredAnt(AntColor.MAIN_MEMORY));
					}
					if (initPheromoneCpuCore > 0 ) {
						if (!policy.isColoredReleaseFunction(AntColor.CPU_CORE)){
							policy.setAntPolicy(new ColoredAntPolicy(antFunction, initPheromoneCpuCore, 1, temperatureFunction, antAgingFunction), AntColor.CPU_CORE);
							behavior.createColoredAnts(AntColor.CPU_CORE);
						}
						//behavior.initPeriodicPulse(behavior.getColoredAnt(AntColor.CPU_CORE));
					}
					if (initPheromoneCpuFreq > 0 ) {
						if (!policy.isColoredReleaseFunction(AntColor.CPU_FREQ)){
							policy.setAntPolicy(new ColoredAntPolicy(antFunction, initPheromoneCpuFreq, 1, temperatureFunction, antAgingFunction), AntColor.CPU_FREQ);
							behavior.createColoredAnts(AntColor.CPU_FREQ);
						}
						//behavior.initPeriodicPulse(behavior.getColoredAnt(AntColor.CPU_FREQ));
					}
					
					behavior.initPeriodicPulse();
				}
			}
		}
	}

}
