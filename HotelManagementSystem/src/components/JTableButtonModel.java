package components;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

public class JTableButtonModel extends AbstractTableModel {
    private Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
    private Vector<String> columns = new Vector<String>();
    public String getColumnName(int column) {
        return columns.elementAt(column);
    }
    public void addColumn(String name) {
        columns.add(name);
    }
    public int getRowCount() {
        return rows.size();
    }
    public int getColumnCount() {
        return columns.size();
    }
    public Object getValueAt(int row, int column) {
        try {
            return rows.get(row).elementAt(column);
        } catch (Exception ex) {
            return "";
        }

    }
    public void addRow(Vector<Object> row) {
        rows.add(row);
    }
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    public Class getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    }

    public void setRowCount(int i) {
        if(i ==0) {
            rows.clear();
        }
    }

    public void setColumnCount(int i) {
        if(i==0) {
            columns.clear();
        }
    }
}
