/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 *
 * @author Amine
 */
public class MyDates {
    public static Date parse(String dateInString, String format) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            date = formatter.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return date;
    }
    
    public static String format(String dateInString, String format) {
        String dateFormatted = "";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(dateInString);
            dateFormatted = new SimpleDateFormat(format).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        return dateFormatted;
    }
    
    public static String format(String dateInString) {
        String format = "yyyy-MM-dd";
        return format(dateInString, format);
    }
}
