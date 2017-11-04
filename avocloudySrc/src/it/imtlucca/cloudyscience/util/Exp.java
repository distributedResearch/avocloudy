package it.imtlucca.cloudyscience.util;

/**
 * 
 * Exponential 
 * y = a*exp(b*x)+c
 * 
 * @author Stefano Sebastio
 *
 */
public class Exp implements IFunction {

	private double a;
	private double b;
	private double c;
	
	private double shift;
	
	public Exp(double a, double b) {
		this(a, b, 0);
	}
	
	public Exp(double a, double b, double c){
		super();
		this.a = a;
		this.b = b;
		this.c = c;
		
		this.shift = 0;
	}
	
	public double getValue(double x) {
		
		return (a*Math.exp(b*(x-shift))+c);
	}

	public void setShift(double shift) {
		this.shift = shift;
		
	}

	

}
