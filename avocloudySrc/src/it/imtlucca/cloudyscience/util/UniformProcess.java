package it.imtlucca.cloudyscience.util;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.math3.distribution.UniformRealDistribution;

import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Process;

/**
 * 
 * Defines a Uniform Process for DEUS using Apache commons.math3
 * 
 * @author Stefano Sebastio
 *
 */
public class UniformProcess extends Process {


	private static final String LOWER = "lower";
	private float lower = 0;
	private static final String UPPER = "upper";
	private float upper = 0;
	private static final String START_VT = "startVT";
	private float startVT = 0;
	
	public UniformProcess(String id, Properties params,
			ArrayList<Node> referencedNodes, ArrayList<Event> referencedEvents)
			throws InvalidParamsException {
		super(id, params, referencedNodes, referencedEvents);
		initialize();
	}	
	
	public void initialize() throws InvalidParamsException {
		if (params.getProperty(LOWER) == null)
			throw new InvalidParamsException(LOWER + " param is expected for a Uniform process.");
		try {
			lower = Float.parseFloat(params.getProperty(LOWER));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(LOWER + " must be a valid float value.");
		}
		
		if (params.getProperty(UPPER) == null)
			throw new InvalidParamsException(UPPER + " param is expected for a Uniform process. ");
		try {
			upper = Float.parseFloat(params.getProperty(UPPER));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(UPPER + " must be a valid float value.");
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
		//RandomGenerator rng = new MersenneTwister(Engine.getDefault().getCurrentSeed());
		
		//UniformRealDistribution ud = new UniformRealDistribution(rng, lower, upper);
		UniformRealDistribution ud = new UniformRealDistribution(lower, upper);
		
		//System.out.println("lower " + lower + " upper " + upper);
//		System.out.println("density 15 " + ud.density(15));
//		System.out.println("density 1000 " + ud.density(1000));
//		System.out.println("density 40000 " + ud.density(40000));
//		System.out.println("density 50000 " + ud.density(50000));
		
		double value = ud.inverseCumulativeProbability(Engine.getDefault().getSimulationRandom().nextDouble());
		double next = virtualTime + value;
		//System.out.println("values " + values);
		//System.out.println("mean " + ud.getNumericalMean());
		//System.out.println("sample " + ud.sample());
		
		if (virtualTime >= upper)
			return (Engine.getDefault().getMaxVirtualTime()+1);
		
//		while(value < startVT || value < virtualTime ){
//			//System.out.println("tryAgain");
//			value = ud.inverseCumulativeProbability(Engine.getDefault().getSimulationRandom().nextDouble());
//			//System.out.println("values " + value);	
//		}
//		//System.out.println("FOUND");
//		
//		return (float)value;
		
		if (next < startVT)
			return (float) (startVT + value);
		else
			return (float)next;
		
//		if (ud.sample()< startVT){
//			return startVT + (float)ud.sample();
//		}
//		else 
//			return (float)ud.sample();
		
		
//		if (virtualTime + (float) ud.sample() < startVT) {
//			return startVT + (float) ud.sample();
//		}
//		else 
//			return virtualTime + (float) ud.sample();

	}


}
