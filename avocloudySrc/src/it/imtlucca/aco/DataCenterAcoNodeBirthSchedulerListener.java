package it.imtlucca.aco;

import it.imtlucca.cloudyscience.DataCenterNodeBirthEvent;
import it.imtlucca.cloudyscience.DataCenterNodeBirthSchedulerListener;
import it.unipr.ce.dsg.deus.core.Event;

/**
 * 
 * Initialize the colored ant events associated to the birth of an ACO Volunteer Node for the DataCenter nodes
 * 
 * @author Stefano Sebastio
 *
 */
public class DataCenterAcoNodeBirthSchedulerListener extends DataCenterNodeBirthSchedulerListener {

	public void newEventScheduled(Event parentEvent, Event newEvent) {
		super.newEventScheduled(parentEvent, newEvent);
		
		//DataCenterSpatialAcoNodeStartupCheckEvent se = (DataCenterSpatialAcoNodeStartupCheckEvent) parentEvent;
		DataCenterNodeBirthEvent be = (DataCenterNodeBirthEvent) parentEvent;
		
		if (newEvent instanceof ColoredAntEvent){
			((ColoredAntEvent) newEvent).setAssociatedNode((AcoDataCenterNode) be.getAssociatedNode());
		}
		
	}
	
}
