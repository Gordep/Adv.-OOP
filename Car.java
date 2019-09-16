/*
	Author - Julian Gonzalez

	Lab project 1 for CS 3331
	Program to predict the location of drivers on a course
	given different top speeds for each segment(1 mile) of a three mile course
	three different cars will be doing the same track but starting at different times
	9/6-Initial setup adding instances, main.
	9/7-Adding calculation methods and print statements.
	9/7 -Add comments making code look nicer
	9/12 - revise code making it more OO 
	9/13 - Final touches adding/checking comments and cleaning up the code

*/
import java.util.*; 
public class Car{

	//not very OO?
	static List<Double> list = new ArrayList<Double>();//list to hold the values of distances at certain times
	static List<Double> mph = new ArrayList<Double>();//to hold values of the mpg at certain times
	//formula mpg*5280ft/3600ft // can use 1mpg = 1.46667fps conversion but it is an approximate
	
	double startTime;
	String name; 
	Car(float startTime,String name){
		this.startTime = startTime;
		this.name = name;
	}

	public static void main(String[] args) {
		double acceleration = 15.0;//given 15fps 
		double trackLength = 5280.0;//course length in feetstartTime = 15.0;

		segment1(acceleration,trackLength);
		segment2(acceleration,trackLength);
		segment3(acceleration,trackLength);
		Car[] students = {
			new Car(0,"A"),//delay for each student driver
			new Car(60,"B"),
			new Car(120,"C")};

		System.out.printf("Time\tcar %s\t\tcar %s\t\tcar %s\n",students[0].name,students[1].name,students[2].name);//prints the initial line of the data
		printData(students);//prints the rest of the data
	}
	/*
		methods independent from the class  can be used without the object cars

	*/
	public static void printData(Car[] carArary){///prints the gathered data into desired format		
		int carA = 0;
		int carB = 0;
		int carC = 0;
		double timePrinter = 30.0;
		System.out.printf("00.0\t0.0 0.0\t\t0.0 0.0\t\t0.0 0.0\n");
		do{
			//////////////////////////////////////////
			//This block prints out the times for the first car starting at 0
			if(timePrinter>=carArary[0].startTime) {
				System.out.printf("%.1f  %.2f  %.2f\t",timePrinter,mph.get(carA)*3600.0/5280.0,list.get(carA)/5280);//converting the	
				if (carA<list.size()-1) carA++;//single line if statement to prevent out of bounds error
			}
			/////////////////////////////////////
			//this block prints out the times and distances for the car starting at 60 seconds
			if (timePrinter>carArary[1].startTime) {
			 	System.out.printf("%.2f  %.2f\t",mph.get(carB)*3600.0/5280.0,list.get(carB)/5280);
			 	if (carB<list.size()-1) carB++;
			}
			else System.out.printf("0.0  0.0\t");				
			////////////////////////////////////
			//Prints out the times and distances for the last car
			if (timePrinter>carArary[2].startTime) {
				System.out.printf("%.2f  %.2f",mph.get(carC)*3600.0/5280.0,list.get(carC)/5280);
				carC++;		
			}
			else System.out.printf("0.0  0.0");
			/////////////////////
			System.out.println();
			timePrinter += 30.0;
		}while(carC != list.size());
	}
	public static void segment1(double acceleration,double trackLength){//calculates the distances and speeds of the first segment 
		double segmentOneSpeed = (20)*5280.0 / 3600.0;  //20mph converted to fps for section 1
		double segTimeA = (segmentOneSpeed - 0)/acceleration;// time = (vf-vi)/a / final velocity - initial velocity divided by acceleration to get time
		double segDistanceA = 0 * segTimeA + (0.5*acceleration)*(segTimeA*segTimeA);//distance formula given initial velocity time and acceleration.
		double time = segTimeA;
		double distance = segDistanceA;
		do{
			time+=0.01;
			time = Math.round(time * 100D) / 100D;//used to round the double to two decimal places

			distance +=(0.01)*segmentOneSpeed;//increase distance according to the current speed limit
			if (time % 30 == 0) {//adds the distance at every 30 seconds to the array list
				list.add(distance);
				mph.add(segmentOneSpeed);

			}
		}while(distance < trackLength);
	}
	public static void segment2(double acceleration,double trackLength){//calculates the distances and speeds of the second segment 
		double segmentOneSpeed = (20)*5280.0 / 3600.0;  //20mph converted to fps for section 1
		double segmentTwoSpeed = (60)*5280.0 / 3600.0;  //60mph converted to fps for section 2
		double segTimeA = (segmentTwoSpeed - segmentOneSpeed)/acceleration;
		double segDistanceA = 0 * segTimeA + (0.5*acceleration)*(segTimeA*segTimeA);

		double time = segTimeA;
		double distance = segDistanceA;//increase distance according to the current speed limit
		double lastItem = 5280.0;//assuming perfect transition so it would start 1 mile
		do{
			time+=0.01;
			time = Math.round(time * 100D) / 100D;//used to round the double to two decimal places
			
			distance +=(0.01)*segmentTwoSpeed;
			if (time % 30 == 0) {//adds the distance and time of every 30 seconds into the list
				list.add(distance+lastItem);
				mph.add(segmentTwoSpeed);
			}

		}while(distance < trackLength);
	}
	public static void segment3(double acceleration,double trackLength){//calculates the distances and speeds of the third segment 
		//no need to calculate acceleration distance and time as this happens in the second segment of the track
		double segmentThreeSpeed = (30)*5280.0 / 3600.0;  //30mph converted to fps for section 3
		double time = 0; 
		double distance = 0;
		double lastItem = 5280.0*2;//assuming perfect transition so it would start 2 mile
		do{
			time+=0.01;
			time = Math.round(time * 100D) / 100D;//used to round the double to two decimal places

			distance +=(0.01)*segmentThreeSpeed;//increase distance according to the current speed limit
			if (time % 30 == 0) {//adds the distance and time of every 30 seconds into the list
				list.add(distance+lastItem);
				mph.add(segmentThreeSpeed);
			}
		}while(distance < trackLength);
	}	
}	    	
