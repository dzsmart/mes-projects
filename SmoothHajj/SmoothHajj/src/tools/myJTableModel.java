package tools;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Sid Ali
 */
public class myJTableModel extends AbstractTableModel {
    public Object[][] Data;
 
    private String[] head = {};
 
    public void setHead(String[] _head) {
        head = _head;
    }
    
    public String[] getHead() {
        return head;
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        Data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
 
    public myJTableModel(Object[][] content) {
        super();
        Data = content;
    }
    
    public myJTableModel() {
        
    }
    
    public void removeRow(int index) {
        Object[][] tmpData = new Object[Data.length - 1][head.length];
        int k =0;
        for(int i=0; i<Data.length; i++) {
            for(int j=0; j<Data.length; j++) {
                if(i == index) continue;
                tmpData[k++][j] = Data[i][j];
            }
        }
        
        Data = tmpData;
    }
    
    public void addRow(Vector row) {
        
    }
 
    @Override
    public int getRowCount() {
        return Data.length;
    }
 
    @Override
    public int getColumnCount() {
        return head.length;
    }
 
    @Override
    public String getColumnName(int columnIndex) {
        return head[columnIndex];
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return Data[rowIndex][columnIndex];
    }
}
