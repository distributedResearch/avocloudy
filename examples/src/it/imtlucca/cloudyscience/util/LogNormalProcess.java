package it.imtlucca.cloudyscience.util;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.math3.distribution.LogNormalDistribution;

import it.unipr.ce.dsg.deus.core.Engine;
import it.unipr.ce.dsg.deus.core.Event;
import it.unipr.ce.dsg.deus.core.InvalidParamsException;
import it.unipr.ce.dsg.deus.core.Node;
import it.unipr.ce.dsg.deus.core.Process;


/**
 * 
 * Defines a LogNormal Process for DEUS using Apache commons.math3
 * 
 * @author Stefano Sebastio
 *
 */
public class LogNormalProcess extends Process {

	private static final String SD_LOG = "sdlog";
	private float sdlog = 0;
	private static final String MEAN_LOG = "meanlog";
	private float meanlog = 0;
	private static final String START_VT = "startVT";
	private float startVT = 0;
	
	public LogNormalProcess(String id, Properties params,
			ArrayList<Node> referencedNodes, ArrayList<Event> referencedEvents)
			throws InvalidParamsException {
		super(id, params, referencedNodes, referencedEvents);
		initialize();
	}

	public void initialize() throws InvalidParamsException {
		if (params.getProperty(SD_LOG) == null)
			throw new InvalidParamsException(SD_LOG + " param is expected for a LogNormal process.");
		try {
			sdlog = Float.parseFloat(params.getProperty(SD_LOG));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(SD_LOG + " must be a valid float value.");
		}
		
		if (params.getProperty(MEAN_LOG) == null)
			throw new InvalidParamsException(MEAN_LOG + " param is expected for a LogNormal process.");
		try {
			meanlog = Float.parseFloat(params.getProperty(MEAN_LOG));
		} catch (NumberFormatException ex) {
			throw new InvalidParamsException(MEAN_LOG + " must be a valid float value.");
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
//		System.out.println("Mu " + shape + " Var " + scale);
		//LogNormalDistribution lnd = new LogNormalDistribution(rng, shape, scale, LogNormalDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
		LogNormalDistribution lnd = new LogNormalDistribution(meanlog, sdlog);
//		System.out.println("Sample_dist " + lnd.sample());
		//System.out.println("mean_dist " + lnd.getNumericalMean() + " variance_dist " + lnd.getNumericalVariance());
		
		double value = lnd.inverseCumulativeProbability(Engine.getDefault().getSimulationRandom().nextDouble());
		double next = virtualTime + value;
		//System.out.println("LogMean " + lnd.getNumericalMean());
		//it should not be necessary
		if (virtualTime >= Engine.getDefault().getMaxVirtualTime())
			return (Engine.getDefault().getMaxVirtualTime()+1);
	
		if (next < startVT)
			return (float) (startVT + value);
		else
			return (float)next;
		
//		while(value < startVT || value < virtualTime ){
//			//System.out.println("tryAgain");
//			value = lnd.inverseCumulativeProbability(Engine.getDefault().getSimulationRandom().nextDouble());
//			//System.out.println("values " + value);	
//		}
		//System.out.println("FOUND");
		
//		return (float)value;
		
//		if (virtualTime + (float) lnd.sample() < startVT) {
//			return startVT + (float) lnd.sample();
//		}
//		else 
//			return virtualTime + (float) lnd.sample();
	}

}
