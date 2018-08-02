package tools;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Sid Ali
 */
public class myJTableModelListBased extends AbstractTableModel {
    List<List<String>> Data = new ArrayList();
    List<Integer> NotEditableRows = new ArrayList();
    private List<String> Head = new ArrayList();
    private boolean celleEditable = true;
    
    public void removeRow(int row) {
        Data.remove(row);
    }
    
    public void setHead(List<String> head) {
        Head = head;
    }
    
    public void setNotEditableData(List<Integer> notEditableRows) {
        NotEditableRows = notEditableRows;
    }
    
    public List<String> getHead() {
        return Head;
    }
    
    public myJTableModelListBased(List<List<String>> content) {
        super();
        Data = content;
    }
    
    public myJTableModelListBased() {
        
    }
    
    public void addRow(List<String> row) {
        Data.add(row);
    }
 
    public void setCellEditable(boolean _celleEditable) {
        celleEditable = _celleEditable;
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
        try{if(NotEditableRows.get(row) == 0) return false;} catch(Exception ex) {}
        return celleEditable;
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        List<String> line = Data.get(row);
        line.set(col, value.toString());
        Data.set(row, line);
        fireTableCellUpdated(row, col);
        
    }
 
    @Override
    public int getRowCount() {
        return Data.size();
    }
 
    @Override
    public int getColumnCount() {
        return Head.size();
    }
 
    @Override
    public String getColumnName(int columnIndex) {
        return Head.get(columnIndex);
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return Data.get(rowIndex).get(columnIndex);
    }
}
