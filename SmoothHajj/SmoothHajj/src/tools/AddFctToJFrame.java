/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author Amine
 * 
 * C'est une interface pour ajouter des fonctions dans le standard JFrame
 */
public class AddFctToJFrame extends javax.swing.JInternalFrame {
    
    // Set fonction est utilisé dans BonLivraisonMan.java
    public void setDateField(String date, JTextField field) {}
    
    public void setDateColumn(String date, JTable table, int row, int col) {}
}
