
/*	Author Julian Gonzalez

	Program 2 
	Given an XML parser create a command line UI with given inputs of
		SHOW print the contents of the current element
		CHANGE param value change the <param> of the current element to value
		WRITE filename write the DOM object to an XML text file
		NEXT output the contents of the next element
		PREVIOUS output the contents of the previous element
		EXIT quit the program
	9/10 - initial main setup and changes to xmlParser/understanding how it works / reading documentation 
	9/11 - adding menu method with switch statements
	9/12 - implemented exit, previous,next and change command lines 
	9/13 - Implemented Write change and changed how XMLReader method works
	9/15 = checking comments / doing more test cases
*/
import java.util.Scanner;
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
public class XMLParser {

	static Document doc;//could change to an object?
	
	public static NodeList XMLreader(){//given xml parser with modifications 
		try {
			System.out.println("Enter the name (without file extension) of your xml file (must be located in the same place as program)");
			Scanner read = new Scanner(System.in);
			String filename = read.next();
			filename = filename + ".xml";//add file extension
			File inputFile = new File(filename);

			if (!inputFile.canRead()) {//check if file does exist 
				System.out.println("File does not exist");
				System.exit(0);
			}

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inputFile);
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

	public static void changeXML(NodeList list,int index){
		System.out.println("Enter your Tag (Case Sensitive) and corresponding data");
		Scanner read = new Scanner(System.in);
		String param = read.nextLine();
		String tagData[] = param.split(" ");
		
		if (tagData.length < 2) {
			System.out.println("Missing data");
			return;
		}
		//for loop to iterate the selected  element then checks the tags of said element to the one given by the user then changes the data to user given data
		for (int i = 0; i<list.item(index).getChildNodes().getLength() ;i++) { 
			if (list.item(index).getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE){

				Element current = (Element)list.item(index).getChildNodes().item(i);
				if (current.getTagName().equals(tagData[0])) {
					list.item(index).getChildNodes().item(i).setTextContent(tagData[1]);
					show(list,index);
					return;
				}		
			}
		}
		System.out.println("Could not find tag");
	}
	public static void writeFile(NodeList xmList){// turn the document object into xml file
		try{
			System.out.println("Enter your filename for the xml file");
			Scanner read = new Scanner(System.in);
			String filename = read.next();
			filename = filename + ".xml";//add file extension

		    TransformerFactory transformerFactory = TransformerFactory.newInstance();//reused given code
		    Transformer transformer = transformerFactory.newTransformer();
		    DOMSource source = new DOMSource(doc);
		    StreamResult result = new StreamResult(new File(filename));
		    transformer.transform(source, result);
			System.out.println("File saved!!(location is the same as the program)");		         
			// Output to console for testing
			//StreamResult consoleResult = new StreamResult(System.out);
		    //transformer.transform(source, consoleResult);

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void menu(NodeList xmlList, int index){//command line interface uses switch cases 
		Scanner scan = new Scanner(System.in);
		boolean exit = false;
		int currentNode = index;
		
		do{
			System.out.printf("Enter a single char for (S)HOW, (C)HANGE, (W)RITE, (N)EXT, (P)REVIOUS (E)XIT\nINPUT:");
			char charInput = scan.next().charAt(0);//reads the fist input of the string 
			switch(charInput){
				case 'S'://print current element
				case 's':
					show(xmlList,currentNode);
					break;
				case 'C'://change element
				case 'c': 
					changeXML(xmlList, currentNode);
					break;
				case 'W'://write element
				case 'w':
					writeFile(xmlList);
					break;
				case 'N'://Next element
				case 'n':
					if(xmlList.item(currentNode+1) != null ){//check if out of bounds or not 
						currentNode ++;
						show(xmlList,currentNode);
					}
					else{
						System.out.println("Out of bounds");
					}
					break;
				case 'P'://previous element
				case 'p':
					if(currentNode-1 != -1){//check if out of bounds or not 
						currentNode --;
						show(xmlList,currentNode);
					}
					else{
						System.out.println("Out of bounds");
					}
					break;
				case 'E'://exit program
				case 'e':
					System.out.println("Exiting...");
					exit = true;
					break;
				default://loop default
					System.out.println("Invalid input");
					break;
			}
		}while(exit != true);
	}

	public static void show(NodeList printee , int printNode){
		for (int i = 0; i<printee.item(printNode).getChildNodes().getLength() ;i++ ) { //gets the current given node and iterates through its children
			if (printee.item(printNode).getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {//checks if it is a Element and not just whitespace
				Element printTag = (Element)printee.item(printNode).getChildNodes().item(i);//gets the child tags from given index node
				System.out.printf("%s =",printTag.getTagName());
				System.out.printf("%s ",printee.item(printNode).getChildNodes().item(i).getTextContent());	
			}	
		}
		System.out.println();
	}

	public static void main(String[] args) {
		NodeList xmlNode = XMLreader();
		show(xmlNode,0);
		menu(xmlNode,0);
	}
}

