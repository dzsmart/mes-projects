/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

/**
 *
 * @author Amine
 */
public class MyInteger {
    
    public static int parseInt(String val) {
        if(val.length() == 0) return 0;
        else {
            int v;
            try {
                v = Integer.parseInt(val);
            } catch(Exception ex) {
                v = 0;
            }
            return v;
        }
    }
}
