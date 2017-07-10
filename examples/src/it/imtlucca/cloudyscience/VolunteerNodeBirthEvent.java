package it.imtlucca.cloudyscience;

import java.util.Properties;

import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Process;

/**
 * The birth of a new Physical Volunteer Node
 * 
 * @author Stefano Sebastio
 *
 */
public class VolunteerNodeBirthEvent extends AbstractVolunteerNodeBirthEvent {

	public VolunteerNodeBirthEvent(String id, Properties params,
			Process parentProcess) throws InvalidParamsException {
		super(id, params, parentProcess);
		
	}

	
	
}
