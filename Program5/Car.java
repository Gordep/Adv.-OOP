import java.util.ArrayList;
import java.util.List;

/*
	10/8 - created Driver class - getters and constructor
	10/9 = added  additional getters to make getting data from driver class and drivertype class easier  
	10/10 - created list for the distance the car is at and the mph of that distance
*/
public class Car{
	private double startTime;
	private Driver driver;
	private int numOfLaps;
	private List<Double> list; //for distances
	private List<Double> mph;  //for mph saved in fps

	Car(double startTime, Driver driver){
		this.startTime = startTime;
		this.driver = driver;
		this.numOfLaps = 0;
		this.list = new ArrayList<Double>();
		this.mph= new ArrayList<Double>();
	}
	//System.out.printf("%.1f  %.2f  %.2f\t",timePrinter,carArray[0].getMph(carA)*3600.0/5280.0,carArray[0].getlist(carA)/5280);//mph.get(carA)*3600.0/5280.0,list.get(carA)/5280);

	//setters for lists
	public void addToList(double distance) {
		this.list.add(distance);
	}
	public void addToMph(double currentSpeed) {
		this.mph.add(currentSpeed);
	}
	//getters for list
	/*
	public double getMph(int index) {
		return this.mph.get(index);
	}
	public double getlist(int index) {
		return this.list.get(index);
	}
	*/
	public List<Double> getList(){
		return this.list;
	}
	public List<Double> getMph(){
		return this.mph;
	}
	public void iterLap() {//used to lap two times around the track
		this.numOfLaps++; 
	}
	//getters
	public int getnumOfLaps() {
		return this.numOfLaps;
	}
	public double getStartTime(){
		return this.startTime;
	}

	public String getDriver(){
		return this.driver.getName();
	}
	
	public String getType(){
		return this.driver.getDriverType().getTypeName();
	}
	public double getFollowT(){
		return this.driver.getDriverType().getFollowTime();
	}
	public double getMaxSpeed(){
		return this.driver.getDriverType().getSpeedLimit();
	}
	public double getMaxAccel(){
		return this.driver.getDriverType().getMaxAcceleration();	
	}	

}