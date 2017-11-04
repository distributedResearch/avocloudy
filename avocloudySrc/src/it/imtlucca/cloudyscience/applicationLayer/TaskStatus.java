package it.imtlucca.cloudyscience.applicationLayer;


/**
 * Identifier for the status of Task and Job
 * 
 * Task status can change according to "Silberschatz Abraham - Galvin P. Baer"
 * 
 * NEW -(admitted)-> READY
 * READY -(scheduler dispatch)-> RUNNING
 * RUNNING -(interrupt)-> READY
 * RUNNING -(I/O or event wait)-> WAITING
 * RUNNING -(exit)-> TERMINATED (also to abort)
 * WAITING -(I/O or event completion)-> READY
 * 
 * @author Stefano Sebastio
 *
 */
public class TaskStatus {

	public static final int TASK_TERMINATED = 4;
	public static final int TASK_WAITING = 3;
	public static final int TASK_RUNNING = 2;
	public static final int TASK_READY = 1;
	public static final int TASK_NEW = 0;
	
	private int status;
	
	public TaskStatus(){
		super();
		
		this.status = TASK_NEW;
		
	}
	
	public void taskAdmitted(){
		if (this.status == TASK_NEW)
			this.status = TASK_READY;
	}
	
	public void taskSchedulerDispatch(){
		if (this.status == TASK_READY)
			this.status = TASK_RUNNING;
	}
	
	public void taskInterrupt(){
		if (this.status == TASK_RUNNING)
			this.status = TASK_READY;
	}
	
	public void taskIoEventWait(){
		if (this.status == TASK_RUNNING)
			this.status = TASK_WAITING;
	}
	
	public void taskExit(){
		if (this.status == TASK_RUNNING)
			this.status = TASK_TERMINATED;
	}
	
	public void taskIoEventCompletion(){
		if (this.status == TASK_WAITING)
			this.status = TASK_READY;
	}
	
}
