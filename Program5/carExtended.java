/*
	Author - Julian Gonzalez
	Program 5 extends our previous program 3 to be able to accept more parameters for each driver
	-Each driver has a driver_type which determines the max speed a driver is willing to go same for accelerations
	-The program must support classes of drivers and the type they are
	-must change xml parser to accept the new parameters
	-finally the track must loop twice
	
	
	8/10 - created driver and Drivertype method creators and changed how xml parser handles the extra parameters of the new lab
	8/10 - reused methods from program 3
	8/11 - started expanding calculate distance method//trying to construct what is asked of us
	8/12 - use of older style to calculate distance and speed with some adjustments
	

*/
import java.util.*; 
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
//import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class carExtended{
	
	public static void main(String[] args) {
		NodeList[] nodeArray = XMLreader();
		Segment[] track = getTrackinfo(nodeArray[0]);//create array of segments that are ordered 
		DriverType[] types = createDriverTypes(nodeArray[1]);//index 1 for type data as created in xmlreader method

		Driver[] drivers = createDrivers(nodeArray[2],types);//create array of drivers and their driver type
		Car[] students = {	new Car(0,drivers[0]),
							new Car(60,drivers[1]),
							new Car(120,drivers[2])};
		//calculate distances for the three drivers//do it twice with previous distance traveled
		double traveled = 0;
		traveled = calDis(students[0],track,traveled);
		calDis(students[0],track,traveled);
		
		traveled = 0;
		traveled  = calDis(students[1],track,traveled);
		calDis(students[1],track,traveled);
		
		traveled = 0;
		traveled = calDis(students[2],track,traveled);
		calDis(students[2],track,traveled);
		
		
		System.out.printf("Time\tcar %s\t\tcar %s\t\tcar %s\n",students[0].getDriver(),students[1].getDriver(),students[2].getDriver());//prints the initial line of the data
		printData(students);
	}	
	
	/*
	*/
	public static double calDis(Car cars, Segment[] track,double traveled) {//reused code from lab 3//changed to work with the car object and segment object
		double totalTraveled = traveled;
		double acceleration = cars.getMaxAccel();
		for (int currentSeg = 0;currentSeg<track.length;currentSeg++ ) {
			double currentSpeed = track[currentSeg].getSpeedLimit();//get speed limit of track in feet per second 
			if(cars.getMaxSpeed()<currentSpeed) {
				currentSpeed = cars.getMaxSpeed();
			}
			double prevSpeed = 0;
			if (currentSeg != 0 ) {//allows for checking prev index
				if (track[currentSeg-1]!= null) {//gets the previous speed for the acceleration process in the current segment
				prevSpeed = track[currentSeg-1].getSpeedLimit();
				}
			}
			double segTime = 0;
			double segDistance = 0;
			if (currentSeg != track.length-1) {//used to know if we are at the last segment and if we are not get values from previous segment
				segTime = (currentSpeed - prevSpeed)/acceleration;
				segDistance = 0 * segTime + (0.5*acceleration)*(segTime*segTime);
			}
			do{//same do while loop used in program 1
				segTime+=0.01;
				segTime = Math.round(segTime * 100D) / 100D;//used to round the double to two decimal places
				
				segDistance +=(0.01)*currentSpeed;
				if (segTime % 30 == 0) {//adds the distance and time of every 30 seconds into the list
					cars.addToList(segDistance + totalTraveled);
					cars.addToMph(currentSpeed);
				}

			}while(segDistance < track[currentSeg].getLength());
			totalTraveled += track[currentSeg].getLength();
		}
		return totalTraveled;
		
	}
	public static void printData(Car[] carArray){///prints the gathered data into desired format		
		int carA = 0,carB = 0,carC = 0;
		List<Double> listA = carArray[0].getList();
		List<Double> listB = carArray[1].getList();
		List<Double> listC = carArray[2].getList();
		List<Double> mphA = carArray[0].getMph();
		List<Double> mphB = carArray[1].getMph();
		List<Double> mphC = carArray[2].getMph();
		double timePrinter = 30.0;
		System.out.printf("00.0\t0.0 0.0\t\t0.0 0.0\t\t0.0 0.0\n");
		do{
			//////////////////////////////////////////
			//This block prints out the times for the first car starting at 0
			if(timePrinter>=carArray[0].getStartTime()) {
				System.out.printf("%.1f  %.2f  %.2f\t",timePrinter,mphA.get(carA)*3600.0/5280.0,listA.get(carA)/5280);
				if (carA<listA.size()-1) carA++;//single line if statement to prevent out of bounds error
			}
			/////////////////////////////////////
			//this block prints out the times and distances for the car starting at 60 seconds
			if (timePrinter>carArray[1].getStartTime()) {
			 	System.out.printf("%.2f  %.2f\t",mphB.get(carB)*3600.0/5280.0,listB.get(carB)/5280);
			 	if (carB<listB.size()-1) carB++;
			}
			else System.out.printf("0.0  0.0\t");				
			////////////////////////////////////
			//Prints out the times and distances for the last car
			if (timePrinter>carArray[2].getStartTime()) {
				System.out.printf("%.2f  %.2f",mphC.get(carC)*3600.0/5280.0,listC.get(carC)/5280);
				carC++;		
			}
			else System.out.printf("0.0  0.0");
			/////////////////////
			System.out.println();
			timePrinter += 30.0;
		}while(carC != listC.size());
	}
	
	public static DriverType[] createDriverTypes(NodeList nodes){//method to get driver types from the xml file and put them in an array
		DriverType[] typeArray = new DriverType[nodes.getLength()-3];//subtract 3 from total as driver type is seen in the three drivers //assuming driver type is first in xml file
		for (int i = 0;i<nodes.getLength()-3;i++ ) {//subtract 3 from total as driver type is seen in the three drivers 
			
			Element node = (Element) nodes.item(i);
			
			String typeName = node.getElementsByTagName("TYPE_NAME").item(0).getTextContent();
			double followTime = Double.parseDouble(node.getElementsByTagName("FOLLOW_TIME").item(0).getTextContent());
			double speedLimit = Double.parseDouble(node.getElementsByTagName("SPEED_LIMIT").item(0).getTextContent());
			double maxAcceleration = Double.parseDouble(node.getElementsByTagName("MAX_ACCELERATION").item(0).getTextContent());

			DriverType temp = new DriverType(typeName,followTime,speedLimit,maxAcceleration);//create temp DriverType object
			typeArray[i] = temp;//add temp into array
		}

		return typeArray;
	}

	public static Driver[] createDrivers(NodeList drivers, DriverType[] type){//new method to create array of drivers with their types
		Driver[] driverArray = new Driver[drivers.getLength()];
		for (int i =0 ;i<drivers.getLength();i++ ) {//for loop to go through the nodelist of drivers
			Element node = (Element) drivers.item(i);

			String name = node.getElementsByTagName("NAME").item(0).getTextContent(); //get text content for driver at current node index
			String typeOfDriver = node.getElementsByTagName("DRIVER_TYPE").item(0).getTextContent();//get driverType object and add it into driver
			
			DriverType typeTemp = null;
			for (int j = 0;j<type.length ;j++ ) {//used to find the type of driver the current driver is by taking typeOfDriver and comparing it to the array of driver types
				if (typeOfDriver.equals(type[j].getTypeName())) {
					typeTemp = type[j];
				}
			}
			Driver temp = new Driver(name,typeTemp);//create temp Driver object
			driverArray[i] = temp;//add temp into array
		}
		return driverArray;
	}

	public static Segment[] getTrackinfo(NodeList track){//reused from program 3 changed to support getElementsbytagName
		List<Double> segmentNumber = new ArrayList<Double>();//seg track num
		List<Double> segmentLength = new ArrayList<Double>();//seg track length
		List<Double> segmentSpeeds = new ArrayList<Double>();//seg track speed
		for (int i=0;i<track.getLength();i++ ) {
			Element elem = (Element) track.item(i);
			segmentNumber.add(Double.parseDouble(elem.getElementsByTagName("SEGMENT_NUMBER").item(0).getTextContent()));			
		}
		Collections.sort(segmentNumber);//sorts the arrayList 
		Double[] segArray = new Double[segmentNumber.size()];
		segmentNumber.toArray(segArray);//convert to array for convenience when checking for duplicates and gaps 
		if (!noDups(segArray) || !noGaps(segArray)) {System.exit(-1);}//if statement to check if there is a gap in segment numbers and if there are duplicates
		
		//this section of code gathers all of the data related to each segment in order and puts them in there respective array list
		int exit = 0;
		int iter = 0;
		do{
			Element elem = (Element) track.item(iter);
			if (segmentNumber.get(exit)== Double.parseDouble(elem.getElementsByTagName("SEGMENT_NUMBER").item(0).getTextContent())){
				//convert the length and speed to feet and feet per second then add them to array list
				segmentLength.add(Double.parseDouble(elem.getElementsByTagName("LENGTH").item(0).getTextContent())*5280.0);//change the number 3?	
				segmentSpeeds.add(Double.parseDouble(elem.getElementsByTagName("SPEED_LIMIT").item(0).getTextContent())*5280.0/3600.0);
				exit++;
			}
			iter ++;
			if (iter == track.getLength()) {
				iter = 0;
			}
		}while(exit != track.getLength());
		checkLengths(segmentLength);//check if the length of the segment is valid
		checkSpeed(segmentSpeeds);//check if speeds fall within range 5-80
		
		Double[] segLen = new Double[segmentLength.size()]; 
		segmentLength.toArray(segLen);
		Double[] segSpeed = new Double[segmentSpeeds.size()]; 
		segmentSpeeds.toArray(segSpeed);
		
		Segment[] courseTrack = new Segment[segArray.length];
		
		for(int i = 0; i<courseTrack.length;i++) {//for loop to create array of segments 
			Segment temp = new Segment(segArray[i],segLen[i],segSpeed[i]);//temp of object used for the segments
			courseTrack[i] = temp;//add temp into array
		}
		return courseTrack;
	}


	
	
	///////////////reused code///////////////////////////
	public static void checkLengths(List<Double> lengths){//checks if segment lengths are valid
		double lowerBound = .5*5280.0;//convert .5 miles to feet
		for (int i = 0;i<lengths.size() ;i++ ) {
			if (lengths.get(i)<lowerBound) {
				System.out.printf("length for segment %d is too small (.5 mile min)\n",i+1);
				System.exit(-1);
			}
		}		
	}
	public static void checkSpeed(List<Double> speeds){//speeds must be from 5 to 80 mph
		double lowerBound = 5.0*5280.0/3600.0; //5 mph to fps
		double upperBound = 80.0*5280.0/3600.0;
		for (int i = 0;i<speeds.size() ;i++ ) {
			if (speeds.get(i)>upperBound || speeds.get(i)<lowerBound) {
				System.out.printf("Speed for segment %d is either to slow or fast (5-80mph limit)\n",i+1);
				System.exit(-1);
			}
		}
	}

	public static boolean noDups(Double[] array){//o(n^2) sadly //checks if the sorted array of track segments has no duplicates in it 
		for (int i = 0;i<array.length;i++ ) {
			for (int j = i+1;j<array.length;j++ ) {
				if (array[i].equals(array[j]) ) {
					System.out.println("XML file contains duplicates in segment number");
					return false;
				}
			}
		}
		return true;
	}
	public static boolean noGaps(Double[] array){//checks if the sorted array of track segments has no gaps in it 
	 	for (int i = 0;i<array.length;i++ ) {
	 		if ((double)i+1 != array[i]) {
	 			System.out.println("XML file contains gaps in segment number");
	 			return false;
	 		}
	 	}
	 	return true;
	}
	//reused xml reader code from program 2
	public static NodeList[] XMLreader(){//given xml parser with modifications 
		try {
			System.out.println("Enter the name (without file extension) of your xml file (must be located in the same place as program)");
			Scanner read = new Scanner(System.in);
			String filename = read.next();
			filename = filename + ".xml";//add file extension
			File inputFile = new File(filename);

			if (!inputFile.canRead()) {//check if file does exist 
				System.out.println("File does not exist");
				System.exit(-1);
			}

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			//get elements by tag name segment,driver_type,driver
			//Element elements = doc.getDocumentElement();
			// create array of nodelist that hold separate node lit for the corresponding Tags
			NodeList[] nodeArray = new NodeList[3];
			NodeList segmentList = doc.getElementsByTagName("SEGMENT");
			nodeArray[0]= segmentList;
			NodeList typeList = doc.getElementsByTagName("DRIVER_TYPE");
			nodeArray[1]= typeList;
			NodeList driverList = doc.getElementsByTagName("DRIVER");
			nodeArray[2]= driverList;

	        return nodeArray;
			
		} catch (Exception e ) {
			e.printStackTrace();
		}
		return null;
	}
}