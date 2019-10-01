//package swingExamples;
/*
	!!Given AbstractTableModel code
	modified by Julian Gonzalez
	9/25 - added constuctor to put in given data (2d string array) 
*/
import javax.swing.table.AbstractTableModel;

public class MyTableModel  extends AbstractTableModel {
	// Table data 
	String [] columnNames = {"Segment Number", "Segment length", "Segment Speed"};
	String[][] data;
	
	public MyTableModel(String[][] newData){//constructor for 2d array for j table 
		data = newData;
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
	
	@Override
	public boolean isCellEditable (int row, int col) {
		return (true);
	}
	
	@Override 
	public void setValueAt (Object value, int row, int col) {
		data[row][col] = (String) value;
		fireTableCellUpdated (row, col);
	}

}
