package tenChannelflyWalkController;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class EditableTableModel extends DefaultTableModel {

	public EditableTableModel() {
		super();
		//		String[] columnNames = {"Channel (0 to 16)", "Duration (sec)"};
		//		Object[][] table = new Object[3][2];
		//		table[0] = columnNames;
		//		String[] row1 = {"Lmao1", "Lma2"};
		//		String[] row2 = {"Cell3", "Cell4"};
		//		table[1] = row1;
		//		table[2] = row2;
		//		this.addRow(row1);
		//	

	}
	public Class getColumnClass(int column){
		switch (column) {
		case 0:
			return Long.class;
		default:
			return Boolean.class;
		}
	}
	public boolean isCellEditable(int row, int column){
		return true;
	}


}
