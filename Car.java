/*
	Author - Julian Gonzalez
	
	Program 3 is an extension of program 1 where we found the mph of a car at a certain time interval
	In program 3 we are modifying the our code to accept xml input files for segments on the track.
	This will be accomplished with the work that was done on program 2 where we create an xml file reader.
	9/19 - added xml reader from program 2
	9/20 - added check for duplicates and gap methods
	9/21 - significant changes to segment method making it more reusable.
	9/22 - spell checking and adding more code
*/
import java.util.*; 
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Car{
	
	//add public?
	//constants 
	static List<Double> list = new ArrayList<Double>();//list to hold the values of distances at certain times
	static List<Double> mph = new ArrayList<Double>();//to hold values of the mph at certain times
	//formula mph*5280ft/3600ft // can use 1mph = 1.46667fps conversion but it is an approximate
	
	double startTime;
	String name; 
	Car(float startTime,String name){
		this.startTime = startTime;
		this.name = name;
	}

	public static void main(String[] args) {
		Car[] students = {
			new Car(0,"A"),//delay for each driver
			new Car(60,"B"),
			new Car(120,"C")};
		NodeList segments = XMLreader();
		getTrackInfo(segments);
		

		System.out.printf("Time\tcar %s\t\tcar %s\t\tcar %s\n",students[0].name,students[1].name,students[2].name);//prints the initial line of the data
		printData(students);//prints the rest of the data
	}
	/*
		methods independent from the class  can be used without the object cars

	*/	
	public static void getTrackInfo(NodeList track){//method gets the data from our nodelist and stores them into arrayList in order
		List<Double> segmentNumber = new ArrayList<Double>();//seg track num
		List<Double> segmentLength = new ArrayList<Double>();//seg track length
		List<Double> segmentSpeeds = new ArrayList<Double>();//seg track speed

		for (int i = 0;i<track.getLength();i++ ) {//for loop to get data required
			//Node x = tracks.item(i)
			segmentNumber.add(Double.parseDouble(track.item(i).getChildNodes().item(1).getTextContent()));
		}

		Collections.sort(segmentNumber);//sorts the arrayList 
		Double[] segArray = new Double[segmentNumber.size()];
		segmentNumber.toArray(segArray);//convert to array for convenience when checking for duplicates and gaps 
		if (!noDups(segArray) || !noGaps(segArray)) {//if statement to check if there is a gap in segment numbers and if there are duplicates
			System.exit(-1);//if i wanted to be saucy i could let them edit segment numbers in the nodelist
		}

		//this section of code gathers all of the data related to each segment in order and puts them in there respective array list
		int exit = 0;
		int iter = 0;
		do{
			if (segmentNumber.get(exit)== Double.parseDouble(track.item(iter).getChildNodes().item(1).getTextContent())){
				//convert the length and speed to feet and feet per second then add them to array list
				segmentLength.add((Double.parseDouble(track.item(iter).getChildNodes().item(3).getTextContent()))*5280.0);//change the number 3?	
				segmentSpeeds.add((Double.parseDouble(track.item(iter).getChildNodes().item(5).getTextContent()))*5280.0/3600.0);
				exit++;
			}
			iter ++;
			if (iter == track.getLength()) {
				iter = 0;
			}
		}while(exit != track.getLength());
		checkLengths(segmentLength);//check if the length of the segment is valid
		checkSpeed(segmentSpeeds);//check if speeds fall within range 5-80
		calculateSegments(segArray,segmentLength,segmentSpeeds);
	}
	public static void calculateSegments(Double[] array,List<Double> sLengths,List<Double> sSpeeds ){//bulk of the code // a modified version of my segment calculator in program 1 can be reused as opposed to program 1's
		double totalTraveled = 0;
		double acceleration = 15;
		for (int currentSeg = 0;currentSeg<array.length;currentSeg++ ) {
			double currentSpeed = sSpeeds.get(currentSeg);
			double prevSpeed = 0;
			if (currentSeg != 0 ) {//allows for checking prev index
				if (array[currentSeg-1]!= null) {//gets the previous speed for the acceleration process in the current segment
				prevSpeed = sSpeeds.get(currentSeg-1);
				}
			}
			double segTime = 0;
			double segDistance = 0;
			if (currentSeg != array.length-1) {//used to know if we are at the last segment and if we are not get values from previous segment
				segTime = (currentSpeed - prevSpeed)/acceleration;
				segDistance = 0 * segTime + (0.5*acceleration)*(segTime*segTime);
			}
			do{//same do while loop used in program 1
				segTime+=0.01;
				segTime = Math.round(segTime * 100D) / 100D;//used to round the double to two decimal places
				
				segDistance +=(0.01)*currentSpeed;
				if (segTime % 30 == 0) {//adds the distance and time of every 30 seconds into the list
					list.add(segDistance + totalTraveled);
					mph.add(currentSpeed);
				}

			}while(segDistance < sLengths.get(currentSeg));
			totalTraveled += sLengths.get(currentSeg);//assuming perfect transitions between segments adding the lengths to this to be used for the overall total distance traveled		
		}
		System.out.printf("Total distance to be traveled %.2f miles\n",totalTraveled/5280.0);
	}

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


///////////////////////////////////////////////////////////////////////////////////////
	//REUSED CODE FROM PREVIOUS PROGRAMS 1 AND 2
	////////////
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
				System.out.printf("%.1f  %.2f  %.2f\t",timePrinter,mph.get(carA)*3600.0/5280.0,list.get(carA)/5280);
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


	//reused xml reader code from program 2
	public static NodeList XMLreader(){//given xml parser with modifications 
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


			Element elements = doc.getDocumentElement();
			NodeList nList = elements.getChildNodes();

			for (int i = 0 ;i<nList.getLength() ;i++ ) {//removes the whitetext (#text) from the nodelist
				if (nList.item(i).getNodeType() == Node.TEXT_NODE) {
					nList.item(i).getParentNode().removeChild(nList.item(i));
				}
			}			
	        return nList;
			
		} catch (Exception e ) {
			e.printStackTrace();
		}
		return null;
	}
}
