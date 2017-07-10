package it.imtlucca.cloudyscience.applicationLayer;

/**
 * Basic features of an Application.
 * It is constituted by its type (i.e. "small" or "large" as in PDP'13 simulator).
 * 
 * @author Stefano Sebastio
 *
 */
public class AppNodeFeature extends AbstractAppNodeFeature {

	public String taskType = null; 
	
	public AppNodeFeature(int deadlineOffset, int parallelism, int duration,
			int ram, int appId, float paralFrac) {
		super(deadlineOffset, parallelism, duration, ram, appId, paralFrac);
		
		//this.taskType = taskType;
	}
	
	public AppNodeFeature(int deadlineOffset, int parallelism, int duration,
			int ram, int appId, float paralFrac, float size) {
		super(deadlineOffset, parallelism, duration, ram, appId, paralFrac, size);
		
		//this.taskType = taskType;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	
	
}
