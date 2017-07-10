package it.imtlucca.cloudyscience.util;

/**
 * General structure for the function
 * 
 * @author Stefano Sebastio
 *
 */
public interface IFunction {

	//public double shift = 0;
	
	public double getValue(double x);
	public void setShift(double shift);
}
