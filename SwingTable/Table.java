//package swingExamples;

/*
	Author - Julian Gonzalez
	
	Program 4 asks us to create a program using swing. We are to read in xml fils into a jtable that the user can edit
	and save to their will, added functionality to save, save as and creating backups of xml file


	This will be accomplished with the work that was done on program 2 where we create an xml file reader.
	9/25= added xml reader from program 2, added no create2darray method to transform the nodelist into a 2d array for JTable
	9/26 - implemented backup function to create a backup of the file selected by the user at the start
	9/27 - added addtional code in Table object to handle required input methods (save,save as) implemented an open file method
	9/28 - created the xmlBuilder method takes 2d array and creates an xml file with it
	9/29 -  checking and adding comments 
*/

import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.*;
import java.util.*; 
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.io.File;
import java.io.FilenameFilter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class Table implements TableModelListener {
	private static String[][] dataTable;//uesd for data in the jtable and xml
	private static String fPath;//dirty way to have the file path on hand for save function

	public Table() {
		JFrame frame = new JFrame ("Track Data");
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu ("File");
		JMenuItem menuItemOpen = new JMenuItem ("Open");
		JMenuItem menuItemExit = new JMenuItem ("Exit");
		JMenuItem menuItemSave = new JMenuItem ("Save");
		JMenuItem menuItemSaveAs = new JMenuItem ("Save As");

		ActionListener al = new DemoActionListener();
		menuItemOpen.addActionListener(al);
		menuItemExit.addActionListener(al);
		menuItemSave.addActionListener(al);//new option
		menuItemSaveAs.addActionListener(al);//new option

		menu.add(menuItemOpen);
		menu.add(menuItemSave);//new
		menu.add(menuItemSaveAs);//new
		menu.add(menuItemExit);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);

		//Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



		NodeList xml = XMLreader(getfilePath());//call getFilepath to find xml file to be used for table
		dataTable = create2dArray(xml);

		JTable table = new JTable (new MyTableModel(dataTable));//adds 2d array as parameter for Abstract jtable

		
		table.getModel().addTableModelListener(this);

		// create a scroll pane and put table in the scroll pane
		JScrollPane scrollPane = new JScrollPane (table);
		table.setFillsViewportHeight(true);
		frame.add(scrollPane);

		// put window in center of screen
		frame.setLocationRelativeTo(null);		

		// Show frame
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void tableChanged(TableModelEvent e) {//modified to change the 2darray as well
		int row = e.getFirstRow();
		int col = e.getColumn();
		TableModel model = (TableModel)e.getSource();
		String colName = model.getColumnName(col);
		Object data = model.getValueAt(row,  col);
		dataTable[row][col] = data.toString();//added line to update 2d array 
		System.out.println("new data is > " + data.toString());
		System.out.println(data);
	}
	
	
	public static void main(String[] args) {
		Table ste = new Table();
	}
	public static String getfilePath(){ //method to get file path of user file
		JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); //create j file open dialog
        FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)","xml");//set filter to xml files
        chooser.setFileFilter(xmlfilter);//add filter parameter choose
        int dialog = chooser.showOpenDialog(null); 
 		String filepath = "0";//used to check if the user selected a file.
        if (dialog == JFileChooser.APPROVE_OPTION){ //
            filepath = chooser.getSelectedFile().getAbsolutePath();//set file path to chosen Jfilechooser
            fPath = filepath;//used for the save as function
        } 
        return filepath;
        /////////////
	}
	public static void xmlBuilder(Boolean format){//converts the 2d array of data into an xml file//used for save and save as // whitespace and tabbing is not applied?
		String[][] xml2d = dataTable;
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			Element rootElem = doc.createElement("MODEL");//root element for the xml file
			doc.appendChild(rootElem);

			for (int i = 0;i<xml2d.length;i++ ) {// populates the xml file with segments and its child tags
				Element segment = doc.createElement("SEGMENT");
				rootElem.appendChild(segment);

				Element segmentNum = doc.createElement("SEGMENT_NUMBER");
				segmentNum.appendChild(doc.createTextNode(xml2d[i][0]));
				segment.appendChild(segmentNum);

				Element segmentLen = doc.createElement("LENGTH");
				segmentLen.appendChild(doc.createTextNode(xml2d[i][1]));
				segment.appendChild(segmentLen);

				Element segmentSped = doc.createElement("SPEED_LIMIT");
				segmentSped.appendChild(doc.createTextNode(xml2d[i][2]));
				segment.appendChild(segmentSped);
			}

			//make dialog for filename
			String filename;
			if (format) {//true == save as
				filename = (JOptionPane.showInputDialog("Enter filename (no file extension)"));	
				filename = filename + ".xml";//add file extension
			}
			else{//just save the file in program location
				File p = new File(fPath);
				filename= p.getName().toString();
			}

			//// REUSED CODE FROM XML PARSER LAB
		    TransformerFactory transformerFactory = TransformerFactory.newInstance();//reused given code
		    Transformer transformer = transformerFactory.newTransformer();
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");//adds proper formating for xml file
		    DOMSource source = new DOMSource(doc);
		    StreamResult result = new StreamResult(new File(filename));
			transformer.transform(source, result);
			System.out.println("File saved!!(location is the same as the program)");


		}catch(Exception e ) {
			e.printStackTrace();
		}
	}
	public static NodeList XMLreader(String filepath){//xml parser used in program 2 with minor changes
		try {
			if (filepath.equals("0")) {//if user did not select any file
				System.out.println("No file selected");
				System.exit(0);
			}
			File inputFile = new File(filepath);

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
			createBak(inputFile);//backup the current file		
	        return nList;
			
		} catch (Exception e ) {
			e.printStackTrace();
		}
		return null;
	}

	public static String[][] create2dArray(NodeList track){//gets the nodelist from the xml file and creates a 2d array from the data
		List<String> segmentNumber = new ArrayList<String>();//seg track num
		List<String> segmentLength = new ArrayList<String>();//seg track length
		List<String> segmentSpeeds = new ArrayList<String>();//seg track speed

		for (int i = 0;i<track.getLength();i++ ) {//for loop to get data required
			/*
			Element trackelm = (Element)track.item(i);
			segmentNumber.add(trackelm.getTextContent());//getElementsByTagName("SEGMENT_NUMBER");
			segmentLength.add(trackelm.getTextContent());//getElementsByTagName("LENGTH");
			segmentSpeeds.add(trackelm.getTextContent());//getElementsByTagName("SPEED_LIMIT");
			*/
			segmentNumber.add((track.item(i).getChildNodes().item(1).getTextContent()));//
			segmentLength.add(((track.item(i).getChildNodes().item(3).getTextContent())));
			segmentSpeeds.add(((track.item(i).getChildNodes().item(5).getTextContent())));
		}
	 
		String[] segArray = new String[segmentNumber.size()];
		String[] segLeng = new String[segmentLength.size()];
		String[] segSped = new String[segmentSpeeds.size()];
		segmentNumber.toArray(segArray);
		segmentLength.toArray(segLeng);
		segmentSpeeds.toArray(segSped);

		String[][] new2dArr = new String[segArray.length][3];
		for (int rows = 0;rows<new2dArr.length;rows++) {//puts the three arrays of the segment into a 2d array with rows being the segment and col being the tag info
			for (int cols = 0;cols<new2dArr[rows].length;cols++ ) {
				if (cols == 0) {
					new2dArr[rows][cols] = segArray[rows]; 
				}
				if (cols == 1) {
					new2dArr[rows][cols] = segLeng[rows];	
				}
				if (cols == 2) {
					new2dArr[rows][cols] = segSped[rows];
				}
			}
		}

		//xmlBuilder(new2dArr);
		return new2dArr;

	}

	public static void createBak(File oldFile){//method to create a backup of the the first opened xml file
		File backup = new File("backup.xml");
		try{
			Files.copy(oldFile.toPath(),backup.toPath(),StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Back up created at program location");
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}
}

