import java.io.*; 
import java.util.*; 

public class Runner{

	static class Runners{
		String name;
		double maxSpeed;
		double accel;
		double time;
		double timeDistance;
		boolean finishedTrack;

		Runners (String name,double maxSpeed,double accel){
			this.name = name;
			this.maxSpeed = maxSpeed;
			this.accel = accel;
			this.time = maxSpeed / accel;//given our max speed and acceleration we can find the time it takes to reach maxSpeed
			this.timeDistance = (accel*.5)*(time*time);//distance given time and acceleration
			this.finishedTrack = false; 
		} 	
	}

	public static void printStats(Runners[] q){//print out the names and given stats of runners
		System.out.printf("Runner\tMax Speed(f/s)\tAcceleration(f/s/s)\n");
		for (int i = 0;i <q.length;i++ ) {
			System.out.printf(q[i].name + "\t" + q[i].maxSpeed + "\t\t" + q[i].accel);
			System.out.println();
		}
		System.out.println();		
	}

	public static void runRace(Runners[] q)throws InterruptedException{
		double courseLength = 600;
		double endTime = 0;	
		
		boolean enterOnce = true;
		double[] totalTimes = new double[q.length];



		for (int x = 0;x<q.length;x++ ) {
			System.out.printf("Time");
			System.out.printf("\t"+q[x].name);
			System.out.println();
			ArrayList<Double> numS = new ArrayList<Double>();	
			double startingTime = q[x].time;//copy of time of Runners 
			double startingDistance = q[x].timeDistance;//copy of distance of Runners
			do{
				startingTime += 0.01;
				startingTime = Math.round(startingTime * 100D) / 100D;//used to round the double to two decimal places
				startingDistance += (0.01)*q[x].maxSpeed;//0.01 seconds//put time function here to increament every 0.01 seconds then add into the overall distance
				
				if (startingTime % 10 == 0){
					System.out.printf("%.1fs\t",startingTime);	
					System.out.printf("%.2f\n",startingDistance);
					if (startingDistance >=600) {
						q[x].finishedTrack = true;
					}
				}

				if (enterOnce && startingDistance + q[x].timeDistance >= 300   ) {//do in a range && startingDistance + q[x].timeDistance <= 301
					startingTime += 2*q[x].time;
					startingDistance += 2*q[x].timeDistance;
					enterOnce = false;
				}


			}while(!q[x].finishedTrack);

		}
	}


	public static void main(String[] args) {
		Runners[] theRunners = {
			new Runners("Nelly", 30, 8),
			new Runners("Steve", 8.8, 3),
			new Runners("Usain", 41, 11)};

		printStats(theRunners);
		
		try{
			runRace(theRunners);
		}
		catch(InterruptedException e){
			System.out.println(e);
		}
		
	}
}