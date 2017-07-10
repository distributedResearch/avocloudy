package it.imtlucca.cloudyscience.util;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.math3.distribution.WeibullDistribution;

import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Process;

/**
 * Defines a Weibull Process for DEUS using Apache commons.math3
 * 
 * @author Stefano Sebastio
 *
 */
public class WeibullProcess extends Process {

	private static final String SHAPE = "shape";
	private float shape = 0;
	private static final String SCALE = "scale";
	private float scale = 0;
	private static final String START_VT = "startVT";
	private float startVT = 0;
	
	public WeibullProcess(String id, Properties params,
			ArrayList<Node> referencedNodes, ArrayList<Event> referencedEvents)
			throws InvalidParamsException {
		super(id, params, referencedNodes, referencedEvents);
		initialize();
	}
	
	public void initialize() throws InvalidParamsException {
		if (params.getProperty(SHAPE) == null)
			throw new InvalidParamsException(SHAPE + " param is expected for a Weibull process.");
		try{
			shape = Float.parseFloat(params.getProperty(SHAPE));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(SHAPE + " must be a valid float value.");
		}
		
		if (params.getProperty(SCALE) == null)
			throw new InvalidParamsException(SCALE + " param is expected for a Weibull process.");
		try {
			scale = Float.parseFloat(params.getProperty(SCALE));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(SCALE + " must be a valid float value.");
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
		WeibullDistribution wd = new WeibullDistribution(shape, scale);
		
		double value = wd.inverseCumulativeProbability(Engine.getDefault().getSimulationRandom().nextDouble());
		double next = virtualTime + value;
		
		if (virtualTime >= Engine.getDefault().getMaxVirtualTime())
			return (Engine.getDefault().getMaxVirtualTime()+1);
		
		if (next < startVT)
			return (float)(startVT+value);
		else
			return (float)next;
	}


}
