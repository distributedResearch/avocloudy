package it.imtlucca.cloudyscience.util;

/**
 * Constant function
 * 
 * @author Stefano Sebastio
 *
 */
public class Const implements IFunction {

	private double value;

	public Const(double value) {
		super();
		this.value = value;
	}
	
	public double getValue(double a){
		return value;
	}

	public void setShift(double shift) {
		// void
	}
	
	
}
