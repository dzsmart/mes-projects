/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 *
 * @author Amine
 */
public class MyDouble {
    public static double parseDouble(Object val) {
        if(val == null) return 0;
        else return parseDouble(val.toString());
    }
    
    public static double parseDouble(String val) {
        val = val.replace(",", ".");
        if(val.length() == 0) return 0;
        else {
            double v;
            try {
                v = java.lang.Double.parseDouble(val);
            } catch(Exception ex) {
                v = 0;
            }
            return v;
        }
    }
    
    public static double parseDoubleCareForEmpty(String val) {
        val = val.replace(",", ".");
        if(val.length() == 0) return Double.NaN;
        else {
            double v;
            try {
                v = java.lang.Double.parseDouble(val);
            } catch(Exception ex) {
                v = Double.NaN;
            }
            return v;
        }
    }
    
    public static BigDecimal parseBigDecimal(String val) {
        val = val.replace(",", ".");
        if(val.length() == 0) return new BigDecimal(0);
        else {
            double v;
            try {
                v = java.lang.Double.parseDouble(val);
            } catch(Exception ex) {
                v = 0;
            }
            return new BigDecimal(v);
        }
    }
    
    public static double formatDouble(double val) {
        try {
            return new BigDecimal(val).setScale(3,BigDecimal.ROUND_DOWN).doubleValue();
        } catch(Exception ex) {
            return 0;
        }
    }
    
    public static double Rounding(double value) {
        return parseDouble(new DecimalFormat("#0.00").format(value));
    }
}
