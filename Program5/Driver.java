/*
	10/8 - created Driver class 
*/

public class Driver{
	private String name;
	private DriverType type;

	Driver(String name,DriverType type){
		this.name = name;
		this.type = type;
	}
	public String getName(){
		return this.name;
	}
	public DriverType getDriverType(){
		return this.type;
	}

}