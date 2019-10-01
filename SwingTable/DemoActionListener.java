
/*
	Given Actionlisten code
	modified by Julian Gonzalez
	9-25 added Save,save as if statements
	9-26 added code for open- code to open an xml file
	9-28 added calls the xmlbuilder when user saves or save as.

*/
import java.io.*; 
import javax.swing.*; 
import java.awt.event.*; 
import javax.swing.filechooser.*; 

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
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DemoActionListener implements ActionListener, ItemListener {

	@Override
	public void itemStateChanged(ItemEvent e) {
		System.out.println("itemStateChanged " + e);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("actionPerformed " + e.getActionCommand());
		if (e.getActionCommand().equalsIgnoreCase("Exit")) {
			System.out.println("Exiting program");
			System.exit(0);
		}
		if (e.getActionCommand().equalsIgnoreCase("Open")) {
			JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); //create j file open dialog
  
            FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)","xml");//set filter to xml files
            chooser.setFileFilter(xmlfilter);
            int dialog = chooser.showOpenDialog(null); 
 
            if (dialog == JFileChooser.APPROVE_OPTION){ //if file selected print out location
                System.out.println(chooser.getSelectedFile().getAbsolutePath());
            } 
		}
		if (e.getActionCommand().equalsIgnoreCase("Save")) {
			System.out.println("Saving file");
			Table.xmlBuilder(false);//calls the xmlbuilder method in Table.java

		}
		if (e.getActionCommand().equalsIgnoreCase("Save As")) {
			System.out.println("Save as File");
			Table.xmlBuilder(true);
		}
	}
}
