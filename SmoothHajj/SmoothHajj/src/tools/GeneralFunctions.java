/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.awt.Component;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Amine
 */
public class GeneralFunctions {
    
    public static void resizeColumnWidth(JTable table) {
        TableCellRenderer renderer;
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            String columnName = String.valueOf(table.getModel().getColumnName(column));
            int width = getStringMetrix(columnName, table.getFont(), "WIDTH")+5;
            
            for (int row = 0; row < table.getRowCount(); row++) {
                renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            if(width > 300) width=300;
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }
    
    public static void setColumnWidths(JTable table, Vector ColumnsToViewFromGrid, int[] widths) {
        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 0; i < ColumnsToViewFromGrid.size(); i++) {
            int columnToView = Integer.parseInt(ColumnsToViewFromGrid.get(i).toString());
            int columnToViewWidth = widths[columnToView];
            
            columnModel.getColumn(i).setPreferredWidth(columnToViewWidth);
        }
    }
    
    public static int getStringMetrix(String str, Font font, String whatToFind) {
        AffineTransform affinetransform = new AffineTransform();     
        FontRenderContext frc = new FontRenderContext(affinetransform,true,true);
        int textwidth = (int)(font.getStringBounds(str, frc).getWidth());
        int textheight = (int)(font.getStringBounds(str, frc).getHeight());
        if(whatToFind.equals("WIDTH")) return textwidth;
        else /*if(whatToFind.equals("HEIGHT"))*/ return textheight;
    }
}
