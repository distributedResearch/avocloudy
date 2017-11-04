package it.imtlucca.cloudyscience;

import it.imtlucca.cloudyscience.infrastructureLayer.dc.AbstractDataCenterNodeStartupCheckEvent;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.SchedulerListener;


/**
 * To initialize the events associated to the Birth of a DataCenter (DataCenterNodeBirthEvent)
 * 
 * @author Stefano Sebastio
 *
 */
public class DataCenterNodeBirthSchedulerListener implements SchedulerListener {


	public void newEventScheduled(Event parentEvent, Event newEvent) {
		//System.out.println("scheduler listener");
		DataCenterNodeBirthEvent be = (DataCenterNodeBirthEvent) parentEvent;
		//if (newEvent instanceof DataCenterNodeStartupCheckEvent) {
		if (newEvent instanceof AbstractDataCenterNodeStartupCheckEvent) {
			//System.out.println("associated " + be.getAssociatedNode());
			((AbstractDataCenterNodeStartupCheckEvent) newEvent).setAssociatedNode((DataCenterNode) be.getAssociatedNode());
		}
		
	}

}
