package it.imtlucca.cloudyscience.infrastructureLayer.dc;

import java.util.ArrayList;

import it.imtlucca.cloudyscience.autonomicLayer.AppCriteria;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasAgent;
import it.imtlucca.cloudyscience.infrastructureLayer.NetworkCriteria;
import it.imtlucca.cloudyscience.infrastructureLayer.VmCriteria;
import it.imtlucca.cloudyscience.physicalLayer.Hardware;


/**
 * 
 * Describes the agent available on a Data Center Node.
 * 
 * It is the central point at Data Center level, since it is the 'collector' for the
 * main building block (Behavior, Knowledge and Policy).
 * 
 * Moreover it manages the underlying hardware infrastructure.
 * 
 * At running time it can have a set of IaaS Agents that manages.
 * 
 * @author Stefano Sebastio
 *
 */
public class IaasDCAgent {

	private AbstractIaasDCNodeBehavior behavior = null;
	
	private AbstractIaasDCNodeKnowledge knowledge = null;
	
	private AbstractIaasDCNodePolicy policy = null;
	
	
	
	//Reference to its IaaS Node Agents
	private ArrayList<IaasAgent> iaasAgents = null;
	
	//Reference to underlying physical machines
	private ArrayList<Hardware> hws = null;
	
	
	
	//FIXME: spostare i 'criteria' sotto policy e accedere a queste
	// Criteria for Nodes creation
	private VmCriteria vmCriteria = null;
	private NetworkCriteria netCriteria = null;
	
	// Criteria for Application Node creation
	private AppCriteria appCriteria = null;

	
	
	public IaasDCAgent(AbstractIaasDCNodeBehavior behavior,
			AbstractIaasDCNodeKnowledge knowledge,
			AbstractIaasDCNodePolicy policy) {
		super();
		this.behavior = behavior;
		this.knowledge = knowledge;
		this.policy = policy;
		
		this.iaasAgents = new ArrayList<IaasAgent>();
		this.hws = new ArrayList<Hardware>();
	}

	public AbstractIaasDCNodeBehavior getBehavior() {
		return behavior;
	}

	public AbstractIaasDCNodeKnowledge getKnowledge() {
		return knowledge;
	}

	public AbstractIaasDCNodePolicy getPolicy() {
		return policy;
	}

	public ArrayList<IaasAgent> getIaasAgents() {
		return iaasAgents;
	}

	public ArrayList<Hardware> getHws() {
		return hws;
	}
	
	public void addIaasAgent(IaasAgent a) {
		this.iaasAgents.add(a);
	}
	
	//FIXME: deve essere visto come un'estrazine complessiva dei vari hw presenti
	public void addHws(Hardware h) {
		this.hws.add(h);
	}

	/**
	 * Set criteria under which the VM component of the upper-lying Iaas Node 
	 * @param vmCriteria
	 */
	public void setVmCriteria(VmCriteria vmCriteria) {
		this.vmCriteria = vmCriteria;
	}

	/**
	 * Set criteria under which the IaaS Node must behave in the Overlay Network
	 * @param netCriteria
	 */
	public void setNetCriteria(NetworkCriteria netCriteria) {
		this.netCriteria = netCriteria;
	}

	public VmCriteria getVmCriteria() {
		return vmCriteria;
	}

	public NetworkCriteria getNetCriteria() {
		return netCriteria;
	}

	

	public AppCriteria getAppCriteria() {
		return appCriteria;
	}

	public void setAppCriteria(AppCriteria appCriteria) {
		this.appCriteria = appCriteria;
	}

	
	
	
	
	
}
