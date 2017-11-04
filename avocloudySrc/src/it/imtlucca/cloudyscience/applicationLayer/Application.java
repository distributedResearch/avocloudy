package it.imtlucca.cloudyscience.applicationLayer;

import java.util.ArrayList;
import java.util.Properties;

import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Resource;

/**
 * 
 * Define an application through its functional and non functional requirements
 * 
 * 
 * @author Stefano Sebastio
 *
 */
public class Application extends Node {
	
	//to de-select a parameter
	private static final String NO_OPT = "no";
	
	final float minTaskDeadline;
	private static final String MIN_DEADLINE_OFFSET_TO_DURATION = "minDeadlineOffsetToDuration";
	final float maxTaskDeadline;
	private static final String MAX_DEADLINE_OFFSET_TO_DURATION = "maxDeadlineOffsetToDuration";
	
	final int minTaskDegree;
	private static final String MIN_TASK_DEGREE_OF_PARALLELISM = "taskMinDegreeOfParallelism";
	final int maxTaskDegree;
	private static final String MAX_TASK_DEGREE_OF_PARALLELISM = "taskMaxDegreeOfParallelism";
	
	final float minParalFrac;
	private static final String MIN_PARALLELIZABLE_FRACTION = "paralFractionMin";
	final float maxParalFrac;
	private static final String MAX_PARALLELIZABLE_FRACTION = "paralFractionMax";
	
	final int minTaskDuration;
	private static final String MIN_TASK_DURATION = "taskMinDuration";
	final int maxTaskDuration;
	private static final String MAX_TASK_DURATION = "taskMaxDuration";
	
	final int minTaskRam;
	private static final String MIN_TASK_RAM = "taskMinRAM";
	final int maxTaskRam;
	private static final String MAX_TASK_RAM = "taskMaxRAM";
	
	float minTaskSize = -1;
	private static final String MIN_TASK_SIZE = "taskMinSize";
	float maxTaskSize = -1;
	private static final String MAX_TASK_SIZE = "taskMaxSize";
	
	private AppAgent appAgent;
	
	
	public Application(String id, Properties params,
			ArrayList<Resource> resources) throws InvalidParamsException {
		super(id, params, resources);
		
		if (params.getProperty(MIN_DEADLINE_OFFSET_TO_DURATION) == null)
			throw new InvalidParamsException(MIN_DEADLINE_OFFSET_TO_DURATION + " param is expected.");
		try {
			//if min or max deadline offset is set to 'NO' then the deadline is not consider (both for min and max case) 
			if (params.getProperty(MIN_DEADLINE_OFFSET_TO_DURATION).equals(NO_OPT)){
				minTaskDeadline = -1;
			}
			else 
				minTaskDeadline = Float.parseFloat(params.getProperty(MIN_DEADLINE_OFFSET_TO_DURATION));
		} catch (NumberFormatException ex){
			throw new InvalidParamsException(MIN_DEADLINE_OFFSET_TO_DURATION + " must be a valid double value (or the 'no' keyword).");
		}
		
		if (params.getProperty(MAX_DEADLINE_OFFSET_TO_DURATION) == null)
			throw new InvalidParamsException(MAX_DEADLINE_OFFSET_TO_DURATION + " param is expected.");
		try {
			//if min or max deadline offset is set to 'NO' then the deadline is not consider (both for min and max case) 
			if (params.getProperty(MAX_DEADLINE_OFFSET_TO_DURATION).equals(NO_OPT)){
				maxTaskDeadline = -1;
			}
			else 
				maxTaskDeadline = Float.parseFloat(params.getProperty(MAX_DEADLINE_OFFSET_TO_DURATION));
		} catch (NumberFormatException ex){
			throw new InvalidParamsException(MAX_DEADLINE_OFFSET_TO_DURATION + " must be a valid double value  (or the 'no' keyword).");
		}
		
		
		if (params.getProperty(MIN_TASK_DEGREE_OF_PARALLELISM) == null)
			throw new InvalidParamsException(MIN_TASK_DEGREE_OF_PARALLELISM + " param is expected.");
		try {
			minTaskDegree = Integer.parseInt(params.getProperty(MIN_TASK_DEGREE_OF_PARALLELISM));
		} catch (NumberFormatException ex){
			throw new InvalidParamsException(MIN_TASK_DEGREE_OF_PARALLELISM + " must be a valid int value.");
		}
		
		if (params.getProperty(MAX_TASK_DEGREE_OF_PARALLELISM) == null)
			throw new InvalidParamsException(MAX_TASK_DEGREE_OF_PARALLELISM + " param is expected.");
		try {
			maxTaskDegree = Integer.parseInt(params.getProperty(MAX_TASK_DEGREE_OF_PARALLELISM));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(MAX_TASK_DEGREE_OF_PARALLELISM + " must be a valid int value.");
		}
		
		if (params.getProperty(MIN_PARALLELIZABLE_FRACTION) == null)
			throw new InvalidParamsException(MIN_PARALLELIZABLE_FRACTION + " param is expected.");
		try {
			minParalFrac = Float.parseFloat(params.getProperty(MIN_PARALLELIZABLE_FRACTION));
		} catch (NumberFormatException ex){
			throw new InvalidParamsException(MIN_PARALLELIZABLE_FRACTION + " must be a valid double value.");
		}
		
		if (params.getProperty(MAX_PARALLELIZABLE_FRACTION) == null)
			throw new InvalidParamsException(MAX_PARALLELIZABLE_FRACTION + " param is expected.");
		try {
			maxParalFrac = Float.parseFloat(params.getProperty(MAX_PARALLELIZABLE_FRACTION));
		} catch (NumberFormatException ex){
			throw new InvalidParamsException(MAX_PARALLELIZABLE_FRACTION + " must be a valid double value.");
		}
		
		
		
		if (params.getProperty(MIN_TASK_DURATION) == null)
			throw new InvalidParamsException(MIN_TASK_DURATION + " param is expected.");
		try {
			minTaskDuration = Integer.parseInt(params.getProperty(MIN_TASK_DURATION));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(MIN_TASK_DURATION + " must be a valid int value.");
		}
		
		if (params.getProperty(MAX_TASK_DURATION) == null)
			throw new InvalidParamsException(MAX_TASK_DURATION + " param is expected.");
		try {
			maxTaskDuration = Integer.parseInt(params.getProperty(MAX_TASK_DURATION));
		} catch (NumberFormatException ex){
			throw new InvalidParamsException(MAX_TASK_DURATION + " must be a valid int value.");
		}
		
		
		if (params.getProperty(MIN_TASK_RAM) == null)
			throw new InvalidParamsException(MIN_TASK_RAM + " param is expected.");
		try {
			minTaskRam = Integer.parseInt(params.getProperty(MIN_TASK_RAM));
		} catch (NumberFormatException ex){
			throw new InvalidParamsException(MIN_TASK_RAM + " must be a valid int value.");
		}
		
		if (params.getProperty(MAX_TASK_RAM) == null)
			throw new InvalidParamsException(MAX_TASK_RAM + " param is expected.");
		try {
			maxTaskRam = Integer.parseInt(params.getProperty(MAX_TASK_RAM));
		} catch (NumberFormatException ex){
			throw new InvalidParamsException(MAX_TASK_RAM + " must be a valid int value.");
		}
		
		
		if ( (params.getProperty(MAX_TASK_SIZE) != null) && (params.getProperty(MIN_TASK_SIZE) != null) ){
			try{
				minTaskSize = Float.parseFloat(params.getProperty(MIN_TASK_SIZE));
				maxTaskSize = Float.parseFloat(params.getProperty(MAX_TASK_SIZE));
			} catch (NumberFormatException ex){
				throw new InvalidParamsException(MIN_TASK_SIZE + " and " + MAX_TASK_SIZE + " must be valid double values");
			}
		}
		
	}

	public void initialize() throws InvalidParamsException {

	}
	
	
	public Object clone(){
		
		Application clone = (Application) super.clone();
		
		
		float deadline;
		if ( (minTaskDeadline == -1) || (maxTaskDeadline == -1) )
			deadline = -1;
		else 
			deadline = minTaskDeadline + (float)(Engine.getDefault().getSimulationRandom().nextDouble()*(maxTaskDeadline-minTaskDeadline));
	
		int parallelism = minTaskDegree + (int)(Engine.getDefault().getSimulationRandom().nextDouble()*(maxTaskDegree-minTaskDegree+1));
		int duration = minTaskDuration + (int)(Engine.getDefault().getSimulationRandom().nextDouble()*(maxTaskDuration-minTaskDuration+1));
		int ram = minTaskRam + (int)(Engine.getDefault().getSimulationRandom().nextDouble()*(maxTaskRam-minTaskRam+1));
		float paralFrac = minParalFrac + (float)(Engine.getDefault().getSimulationRandom().nextDouble()*(maxParalFrac-minParalFrac));
		
				
		int deadlineOffset;
		if (deadline == -1)
			deadlineOffset = -1;
		else 
			deadlineOffset = (int) (duration*deadline);
		
		int appId = Engine.getDefault().generateResourceKey();
		
		
		AbstractAppNodeFeature appFeature;
		if ( (minTaskSize != -1) && (maxTaskSize != -1) ){
			float size = minTaskSize + (float)(Engine.getDefault().getSimulationRandom().nextDouble()*(maxTaskSize-minTaskSize));
			appFeature = new AppNodeFeature(deadlineOffset, parallelism, duration, ram, appId, paralFrac, size);
			
		}
		else 
			appFeature = new AppNodeFeature(deadlineOffset, parallelism, duration, ram, appId, paralFrac);
		AbstractAppNodeKnowledge appKnowledge = new AppNodeKnowledge(appFeature);
		AbstractAppNodePolicy appPolicy = new AppNodePolicy();
		AbstractAppNodeBehavior appBehavior = new AppNodeBehavior(appKnowledge, appPolicy);
		
		AppAgent appAgent = new AppAgent(appBehavior, appKnowledge, appPolicy, appFeature);
		clone.appAgent = appAgent;
		//System.out.println("Application created..." + appAgent.getFeature().getAppId() + " at VT " + appAgent.getFeature().getGenerationTime());
		return clone;
	}

	public AppAgent getAppAgent() {
		return appAgent;
	}

	
	
	
}
