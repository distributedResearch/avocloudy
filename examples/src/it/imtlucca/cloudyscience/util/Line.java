package it.imtlucca.cloudyscience.util;

/**
 * Line function
 * y = b*x + a
 * 
 * @author Stefano Sebastio
 *
 */
public class Line implements IFunction {

	private double a;
	private double b;

	private double shift;
	
	public Line(double a, double b) {
		super();
		this.a = a;
		this.b = b;
		
		this.shift = 0;
	}
	
	
	public double getValue(double x) {
		return (b*(x-shift)+a);
	}

	public void setShift(double shift) {
		this.shift = shift;
		
	}


}
