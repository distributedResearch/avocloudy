package it.imtlucca.cloudyscience.infrastructureLayer.dc;

/**
 * Basic building block of a load. A single record about the load of a node resources 
 * 
 * @author Stefano Sebastio
 *
 */
public class LoadRecord extends AbstractLoadRecord {

	
	private int nodeId;
	
	
	public LoadRecord(float time, double ramUtil, double coreUtil,
			double cpuUtil, int id) {
		super(time, ramUtil, coreUtil, cpuUtil);
		this.nodeId = id;
	}

	/**
	 * Obtain the identifier of the nodes by which the load record belongs
	 * @return
	 */
	public int getNodeId() {
		return nodeId;
	}
	
	

}
