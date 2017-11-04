package it.imtlucca.cloudyscience.applicationLayer;

import it.unipr.ce.dsg.deus.core.Engine;

/**
 * Main features of the Application.
 * It is constituted by functional characteristics and requirements. 
 * Moreover the task execution status is reported
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractAppNodeFeature {

	private int deadlineOffset;
	private int parallelism;
	private int duration;
	private int ram;
	private float paralFrac; // f parameter in the Amdahl's law
	private float size = -1;
	
	private int appId;
	
	private float generationTime;
	private float startTime = -1;
	private float finishTime = -1;
	
	private float deadline = -1;
	
	//FIXME: dovrebbe essere staccato il concetto di task da quello di App 
	private TaskStatus status;
	
	
	public AbstractAppNodeFeature(int deadlineOffset, int parallelism,
			int duration, int ram, int appId, float paralFrac) {
		super();
		this.deadlineOffset = deadlineOffset;
		this.parallelism = parallelism;
		this.duration = duration;
		this.ram = ram;
		this.paralFrac = paralFrac;
		
		this.appId = appId;
		this.generationTime = Engine.getDefault().getVirtualTime();
		
		if (deadlineOffset == -1){
			this.deadline = Float.MAX_VALUE;
		}
		else 
			this.deadline = this.generationTime + this.deadlineOffset + this.duration;	
		
		this.status = new TaskStatus();
	}

	
	public AbstractAppNodeFeature(int deadlineOffset, int parallelism,
			int duration, int ram, int appId, float paralFrac, float size) {
		this(deadlineOffset, parallelism, duration, ram, appId, paralFrac);
		
		this.size = size;
		
	}

	public int getDeadlineOffset() {
		return deadlineOffset;
	}


	public int getParallelism() {
		return parallelism;
	}


	public float getParalFrac() {
		return paralFrac;
	}
	
	
	public int getDuration() {
		return duration;
	}


	public int getRam() {
		return ram;
	}


	public int getAppId() {
		return appId;
	}


	public float getGenerationTime() {
		return generationTime;
	}


	public float getStartTime() {
		return startTime;
	}


	public float getFinishTime() {
		return finishTime;
	}


	public void setStartTime(float startTime) {
		this.startTime = startTime;
		this.status.taskSchedulerDispatch();
	}


	public void setFinishTime(float finishTime) {
		this.finishTime = finishTime;
		this.status.taskExit();
	}


	public TaskStatus getStatus() {
		return status;
	}


	public float getDeadline() {
		return deadline;
	}

	
	public float getSize() {
		return size;
	}
	
}
