package it.imtlucca.cloudyscience.infrastructureLayer.dc;

/**
 * It is the monitored load record of a IaaS Node   
 * 
 * @author Stefano Sebastio
 *
 */
public abstract class AbstractLoadRecord {

	/**
	 * Time at which the load informations are gathered
	 */
	private float time;
	
	/**
	 * The quote of utilization for the main elements of a IaaS Node
	 */
	private double ramUtil;
	private double coreUtil;
	private double cpuUtil;
	
	
	public AbstractLoadRecord(float time, double ramUtil, double coreUtil,
			double cpuUtil) {
		super();
		this.time = time;
		this.ramUtil = ramUtil;
		this.coreUtil = coreUtil;
		this.cpuUtil = cpuUtil;
	}


	public float getTime() {
		return time;
	}


	public double getRamUtil() {
		return ramUtil;
	}


	public double getCoreUtil() {
		return coreUtil;
	}


	public double getCpuUtil() {
		return cpuUtil;
	}
	
	
}
