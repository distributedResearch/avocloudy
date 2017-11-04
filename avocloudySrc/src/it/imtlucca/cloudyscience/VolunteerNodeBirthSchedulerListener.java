package it.imtlucca.cloudyscience;

import it.imtlucca.cloudyscience.infrastructureLayer.AbstractIaasNodeDeathEvent;
import it.imtlucca.cloudyscience.infrastructureLayer.AbstractIaasNodeDisconnectionEvent;
import it.imtlucca.cloudyscience.infrastructureLayer.IaasNodeJoinEvent;
import it.imtlucca.cloudyscience.infrastructureLayer.AbstractIaasNodeReconnectionEvent;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.SchedulerListener;

/**
 * To initialize the events associated to the Birth of a Volunteer (VolunteerNodeBirthEvent)
 * 
 * @author Stefano Sebastio
 *
 */
public class VolunteerNodeBirthSchedulerListener implements SchedulerListener {

	public void newEventScheduled(Event parentEvent, Event newEvent) {
		//System.out.println("volunteer scheduler listener");
		AbstractVolunteerNodeBirthEvent be = (AbstractVolunteerNodeBirthEvent) parentEvent;
		//System.out.println("Associated " + be.getAssociatedNode());
		if (newEvent instanceof IaasNodeJoinEvent) {
			((IaasNodeJoinEvent) newEvent).setAssociatedNode((VolunteerNode) be.getAssociatedNode());
		} 
		else if (newEvent instanceof AbstractIaasNodeDeathEvent) {
			((AbstractIaasNodeDeathEvent) newEvent).setAssociatedNode((VolunteerNode) be.getAssociatedNode());
		}
		else if (newEvent instanceof AbstractIaasNodeDisconnectionEvent) {
			//System.out.println("associate a disconnection");
			((AbstractIaasNodeDisconnectionEvent) newEvent).setAssociatedNode((VolunteerNode) be.getAssociatedNode());
		}
		else if (newEvent instanceof AbstractIaasNodeReconnectionEvent) {
			((AbstractIaasNodeReconnectionEvent) newEvent).setAssociatedNode((VolunteerNode) be.getAssociatedNode());
		}
	}

}
