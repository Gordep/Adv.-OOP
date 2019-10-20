/*
 * 10/8 - created Segment class 
 * 
 */
public class Segment{
	private double number;
	private double length;
	private double speedLimit;
	
	Segment(double number, double length, double speedLimit) {
		this.number = number;
		this.length = length;
		this.speedLimit = speedLimit;
	}
	
	public double getNumber() {
		return this.number;
	}
	public double getLength() {
		return this.length;
	}
	public double getSpeedLimit() {
		return this.speedLimit;
	}
}