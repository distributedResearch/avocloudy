package it.imtlucca.cloudyscience.util;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.math3.distribution.ExponentialDistribution;

import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Process;

/**
 * Defines an Exponential Process for DEUS using Apache commons.math3
 * 
 * @author Stefano Sebastio
 *
 */
public class ExponentialProcess extends Process {

	private static final String RATE = "rate";
	private float rate = 0;
	private static final String START_VT = "startVT";
	private float startVT = 0;
	
	
	public ExponentialProcess(String id, Properties params,
			ArrayList<Node> referencedNodes, ArrayList<Event> referencedEvents)
			throws InvalidParamsException {
		super(id, params, referencedNodes, referencedEvents);
		initialize();
	}



	public void initialize() throws InvalidParamsException {

		if (params.getProperty(RATE) == null)
			throw new InvalidParamsException(RATE + " param is expected for an Exponential process.");
		try {
			rate = Float.parseFloat(params.getProperty(RATE));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(RATE + " must be a valid float value.");
		}
		
		
		if (params.getProperty(START_VT) != null) {
			try {
				startVT = Float.parseFloat(params.getProperty(START_VT));
			} catch (NumberFormatException ex) {
				throw new InvalidParamsException(START_VT + " must be a valid float value.");
			}
		}
	}

	
	public float getNextTriggeringTime(Event event, float virtualTime) {
		ExponentialDistribution ed = new ExponentialDistribution( (1/rate));
		
		double value = ed.inverseCumulativeProbability(Engine.getDefault().getSimulationRandom().nextDouble());
		double next = virtualTime + value;
		
		if (virtualTime >= Engine.getDefault().getMaxVirtualTime())
			return (Engine.getDefault().getMaxVirtualTime()+1);
		
		if (next < startVT)
			return (float)(startVT+value);
		else
			return (float)next;
		
	}

}
