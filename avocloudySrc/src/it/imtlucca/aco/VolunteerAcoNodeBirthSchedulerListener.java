package it.imtlucca.aco;

import it.imtlucca.cloudyscience.VolunteerNodeBirthSchedulerListener;
import it.unipr.ce.dsg.deus.core.Event;

/**
 * Initialize the colored ant events associated to the birth of an ACO Volunteer Node
 * 
 * @author Stefano Sebastio
 *
 */
public class VolunteerAcoNodeBirthSchedulerListener extends VolunteerNodeBirthSchedulerListener {
	
	public void newEventScheduled(Event parentEvent, Event newEvent) {
		super.newEventScheduled(parentEvent, newEvent);
		
		VolunteerAcoNodeBirthEvent be = (VolunteerAcoNodeBirthEvent) parentEvent;
		
		if (newEvent instanceof ColoredAntEvent){
			((ColoredAntEvent) newEvent).setAssociatedNode((AcoVolunteerNode) be.getAssociatedNode());
		}
	}
	
}
