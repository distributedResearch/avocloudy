package it.imtlucca.aco;

import java.util.ArrayList;
import java.util.Properties;

import it.imtlucca.cloudyscience.DataCenterNode;
import it.imtlucca.cloudyscience.util.Exp;
import it.imtlucca.cloudyscience.util.IFunction;
import it.imtlucca.cloudyscience.util.Const;
import it.imtlucca.cloudyscience.util.Line;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Resource;

/**
 * Provides the additional parameters for the ACO algorithms
 * 
 * @author Stefano Sebastio
 *
 */
public class AcoDataCenterNode extends DataCenterNode {

	public int ttl = 1;
	private static final String ANT_TTL = "antTtl";
	public float timeout = 10;
	private static final String ANT_TIMEOUT = "antTimeOut";
	
	public int numOfAnts = 1;
	private static final String NUM_OF_ANTS = "numOfAnts";
	
	public double evaporation = 1;
	private static final String EVAPORATION = "evaporation";
	public double initPheromone = 1;
	private static final String INIT_PHEROMONE = "initPheromone";
	
	public double pheromoneToDeposit_a = 1;
	private static final String PHEROMONE_TO_DEPOSIT_A = "pheromoneToDeposit_a";
	public double pheromoneToDeposit_b = -1;
	private static final String PHEROMONE_TO_DEPOSIT_B = "pheromoneToDeposit_b";
	public double pheromoneToDeposit_c = 1;
	private static final String PHEROMONE_TO_DEPOSIT_C = "pheromoneToDeposit_c";
	
	public IFunction agingFunction = null;
	private static final String CONST = "const";
	private static final String LINE = "line"; 
	private static final String EXP = "exp";
	private static final String AGING_FUNC = "agingFunc";
	
	public double weightCore = 1;
	private static final String WEIGHT_ANT_CPU_CORE = "weightAntCpuCore";
	public double weightFreq = 1;
	private static final String WEIGHT_ANT_CPU_FREQ = "weightAntCpuFreq";
	public double weightRam = 1;
	private static final String WEIGHT_ANT_MAIN_MEM = "weightAntMainMemory";
	
	public double resourceWeight = 1;
	private static final String RESOURCE_WEIGHT = "resourceWeight";
	public double finishingTimeWeight = 1;
	private static final String FINISHING_TIME_WEIGHT = "finishingTimeWeight";
	public double pheromoneWeight = 1;
	private static final String PHEROMONE_WEIGHT = "pheromoneWeight";
	public double linkQtWeight = 0;
	private static final String LINK_QT_WEIGHT = "linkQtWeight";
	
	
	public IFunction temperatureFunction = null;
	public double temperature_a = 1;
	private static final String TEMPERATURE_A = "temperature_a";
	public double temperature_b = 1;
	private static final String TEMPERATURE_B = "temperature_b";
	public double temperature_c = 1;
	private static final String TEMPERATURE_C = "temperature_c";
	private static final String TEMPERATURE_FUNC = "temperatureFunc";
	
	
	public AcoDataCenterNode(String id, Properties params,
			ArrayList<Resource> resources) throws InvalidParamsException {
		super(id, params, resources);
		System.out.println("Aco Data Center Node constructor called...");
		
		if (params.getProperty(ANT_TTL) == null)
			throw new InvalidParamsException(ANT_TTL + " param is expected.");
		try{
			ttl = Integer.parseInt(params.getProperty(ANT_TTL));
		} catch (NumberFormatException ex){
			throw new InvalidParamsException(ANT_TTL + " must be a valid int value.");
		}
		
		if (params.getProperty(ANT_TIMEOUT) == null)
			throw new InvalidParamsException(ANT_TIMEOUT + " param is expected.");
		try {
			timeout = Float.parseFloat(params.getProperty(ANT_TIMEOUT));
		} catch (NumberFormatException ex){
			throw new InvalidParamsException(ANT_TIMEOUT + " must be a valid float value.");
		}
		
		if (params.getProperty(NUM_OF_ANTS) == null)
			throw new InvalidParamsException(NUM_OF_ANTS + " param is expected.");
		try {
			numOfAnts = Integer.parseInt(params.getProperty(NUM_OF_ANTS));
		} catch (NumberFormatException ex){
			throw new InvalidParamsException(NUM_OF_ANTS + " must be a valid int value.");
		}
		
		
		if (params.getProperty(EVAPORATION) == null)
			throw new InvalidParamsException(EVAPORATION + " param is expected.");
		try {
			evaporation = Double.parseDouble(params.getProperty(EVAPORATION));
		} catch (NumberFormatException ex){
			throw new InvalidParamsException(EVAPORATION + " must be a valid double value.");
		}
		
		
		if (params.getProperty(INIT_PHEROMONE) == null)
			throw new InvalidParamsException(INIT_PHEROMONE + " param is expected.");
		try {
			initPheromone = Double.parseDouble(params.getProperty(INIT_PHEROMONE));
		} catch (NumberFormatException ex){
			throw new InvalidParamsException(INIT_PHEROMONE + " must be a valid double value.");
		}
		
		
		if (params.getProperty(WEIGHT_ANT_CPU_CORE) != null){
			try{
				weightCore = Double.parseDouble(params.getProperty(WEIGHT_ANT_CPU_CORE));
			} catch (NumberFormatException ex){
				throw new InvalidParamsException(WEIGHT_ANT_CPU_CORE + " must be a valid double value.");
			}
		}
		if (params.getProperty(WEIGHT_ANT_CPU_FREQ) != null){
			try{
				weightFreq = Double.parseDouble(params.getProperty(WEIGHT_ANT_CPU_FREQ));
			} catch (NumberFormatException ex){
				throw new InvalidParamsException(WEIGHT_ANT_CPU_FREQ + " must be a valid double value.");
			}
		}
		if (params.getProperty(WEIGHT_ANT_MAIN_MEM) != null){
			try{
				weightRam = Double.parseDouble(params.getProperty(WEIGHT_ANT_MAIN_MEM));
			} catch (NumberFormatException ex){
				throw new InvalidParamsException(WEIGHT_ANT_MAIN_MEM + " must be a valid double value.");
			}
		}
		
		
		if (params.getProperty(RESOURCE_WEIGHT) != null){
			try{
				resourceWeight = Double.parseDouble(params.getProperty(RESOURCE_WEIGHT));
			} catch (NumberFormatException ex){
				throw new InvalidParamsException(RESOURCE_WEIGHT + " must be a valid double value.");
			}
		}
		if (params.getProperty(FINISHING_TIME_WEIGHT) != null){
			try{
				finishingTimeWeight = Double.parseDouble(params.getProperty(FINISHING_TIME_WEIGHT));
			} catch (NumberFormatException ex){
				throw new InvalidParamsException(FINISHING_TIME_WEIGHT + " must be a valid double value.");
			}
		}
		if (params.getProperty(PHEROMONE_WEIGHT) != null){
			try{
				pheromoneWeight = Double.parseDouble(params.getProperty(PHEROMONE_WEIGHT));
			} catch (NumberFormatException ex){
				throw new InvalidParamsException(PHEROMONE_WEIGHT + " must be a valid double value.");
			}
		}
		if (params.getProperty(LINK_QT_WEIGHT) != null){
			try{
				linkQtWeight = Double.parseDouble(params.getProperty(LINK_QT_WEIGHT));
			} catch (NumberFormatException ex){
				throw new InvalidParamsException(LINK_QT_WEIGHT + " must be a valid double value.");
			}
		}
		
		
		
		if (params.getProperty(AGING_FUNC) == null)
			throw new InvalidParamsException(AGING_FUNC + " param is expeced.");
		else {
			
			if (params.getProperty(PHEROMONE_TO_DEPOSIT_A) == null)
				throw new InvalidParamsException(PHEROMONE_TO_DEPOSIT_A + " param is expected.");
			else {
				try {
					pheromoneToDeposit_a = Double.parseDouble(params.getProperty(PHEROMONE_TO_DEPOSIT_A));
					
					if (params.getProperty(AGING_FUNC).equals(CONST)){	
						agingFunction = new Const(pheromoneToDeposit_a);
					}
					else if (params.getProperty(AGING_FUNC).equals(LINE) || params.getProperty(AGING_FUNC).equals(EXP)){
						
						if (params.getProperty(PHEROMONE_TO_DEPOSIT_B) == null)
							throw new InvalidParamsException(PHEROMONE_TO_DEPOSIT_B + " param is expected.");
						try {
							pheromoneToDeposit_b = Double.parseDouble(params.getProperty(PHEROMONE_TO_DEPOSIT_B));
							
							if (pheromoneToDeposit_b > 0)
								throw new InvalidParamsException(PHEROMONE_TO_DEPOSIT_B + " must be a negative number to have the aging function.");
								
							if (params.getProperty(AGING_FUNC).equals(LINE))
								agingFunction = new Line(pheromoneToDeposit_a, pheromoneToDeposit_b);
							else if (params.getProperty(AGING_FUNC).equals(EXP)){
								if (params.getProperty(PHEROMONE_TO_DEPOSIT_C) !=null){
									try {
										pheromoneToDeposit_c = Double.parseDouble(params.getProperty(PHEROMONE_TO_DEPOSIT_C));
										agingFunction = new Exp(pheromoneToDeposit_a, pheromoneToDeposit_b, pheromoneToDeposit_c);
										
										if (agingFunction.getValue(0) == Double.POSITIVE_INFINITY){
											throw new InvalidParamsException(AGING_FUNC + " must not goes to infinity at 0");
										}
										
									} catch  (NumberFormatException ex){
										throw new InvalidParamsException(PHEROMONE_TO_DEPOSIT_C + " must be a valid double value.");
									}
								}
								else
									agingFunction = new Exp(pheromoneToDeposit_a, pheromoneToDeposit_b);
							}
								
						} catch (NumberFormatException ex){
							throw new InvalidParamsException(PHEROMONE_TO_DEPOSIT_B + " must be a valid double value.");
						}
					}
					else
						throw new InvalidParamsException(AGING_FUNC + " must be an aging function.");
					
				} catch (NumberFormatException ex){
					throw new InvalidParamsException(PHEROMONE_TO_DEPOSIT_A + " must be a valid double value.");
				}
				
			}
		}
		
		
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
		
	}

}
