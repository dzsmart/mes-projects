/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import  java.io.*;
import  org.apache.poi.hssf.usermodel.HSSFSheet;
import  org.apache.poi.hssf.usermodel.HSSFWorkbook;
import  org.apache.poi.hssf.usermodel.HSSFRow;
/**
 *
 * @author Amine
 */
public class CreateExlFile {
    public static void main(String[]args) {
        String[] head = new String[]{"No.", "Name", "Address", "Email"};
        String[][] Data = new String[][]{{"1", "Sankumarsingh", "India", "sankumarsingh@gmail.com"},
                                         {"2", "Sankumarsingh", "India", "sankumarsingh@gmail.com"},
                                         {"3", "Sankumarsingh", "India", "sankumarsingh@gmail.com"},
                                         {"4", "Sankumarsingh", "India", "sankumarsingh@gmail.com"}, };
        createExcelFile("C:\\Users\\Amine\\Desktop\\ff/NewExcelFile.xls", "FirstSheet", head, Data);
    }
    
    public static boolean createExcelFile(String filename, String sheetName, String[] head, String[][] Data) {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(sheetName);  

            HSSFRow rowhead = sheet.createRow((short)0);
            for(int i=0; i<head.length; i++) {
                rowhead.createCell(i).setCellValue(head[i]);
            }

            
            for(int i=0; i<Data.length; i++) {
                HSSFRow row = sheet.createRow((short)i+1);
                for(int j=0; j<Data[0].length; j++) {
                    row.createCell(j).setCellValue(Data[i][j]);
                }
                for(int colNum = 0; colNum<row.getLastCellNum();colNum++) workbook.getSheetAt(0).autoSizeColumn(colNum);
            }

            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
            //System.out.println("Your excel file has been generated!");
            
            return true;
        } catch ( Exception ex ) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
}
