/*
	10/8 - created DriverType class - getters, setter and constructor
*/
public class DriverType{
	
	private String typeName;
	private double followTime;
	private double speedLimit;
	private double maxAcceleration;


	//constructor
	DriverType(String typeName,double followTime,double speedLimit, double maxAcceleration){
		this.typeName = typeName;
		this.followTime = followTime;
		this.speedLimit = speedLimit;
		this.maxAcceleration = maxAcceleration;
	}
	//set data // might not need
	public void setData(String typeName,double followTime,double speedLimit, double maxAcceleration){
		this.typeName = typeName;
		this.followTime = followTime;
		this.speedLimit = speedLimit;
		this.maxAcceleration = maxAcceleration;
	}

	//getters for driver type
	public String getTypeName(){
		return this.typeName;
	}
	public double getFollowTime(){
		return this.followTime;
	}
	public double getSpeedLimit(){
		return this.speedLimit;
	}
	public double getMaxAcceleration(){
		return this.maxAcceleration;
	}

}