/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;
import view.mainFrame;

/**
 *
 * @author Amine
 */
public class JDatePickerPanel extends JDialog {

    private AddFctToJFrame Parent = null;
    private UtilDateModel model;
    /**
     * Creates new form JDatePickerPanel
     * @param _mainFrame
     * @param P
     * @param _Field
     * @param modal
     */
    public JDatePickerPanel(mainFrame _mainFrame, AddFctToJFrame P, final JTextField _Field, boolean modal) {
        super(_mainFrame, modal);
        Parent = P;
        setWindowStyle();
        model = new UtilDateModel();
        model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(model.isSelected()) {
                    int year = model.getYear();
                    int month = model.getMonth()+1;
                    int day = model.getDay();
                    String date = year+"-";
                    date += (month < 10) ? "0"+month+"-":month+"-";
                    date += (day < 10) ? "0"+day:day;
                    Parent.setDateField(date, _Field);
                    JDatePickerPanel.this.dispose();
                }
            }
        });
        Shared();
    }
    
    public JDatePickerPanel(mainFrame _mainFrame, AddFctToJFrame P, final JTable table, final int row, final int col, boolean modal) {
        super(_mainFrame, modal);
        Parent = P;
        setWindowStyle();
        model = new UtilDateModel();
        model.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(model.isSelected()) {
                    int year = model.getYear();
                    int month = model.getMonth()+1;
                    int day = model.getDay();
                    String date = year+"-";
                    date += (month < 10) ? "0"+month+"-":month+"-";
                    date += (day < 10) ? "0"+day:day;
                    Parent.setDateColumn(date, table, row, col);
                    JDatePickerPanel.this.dispose();
                }
            }
        });
        Shared();
    }

    private void Shared() {
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl panel = new JDatePanelImpl(model, p);
        panel.setShowYearButtons(true);
        MainPanel.setLayout(new BorderLayout());
        MainPanel.add((JComponent) panel);
        panel.setShowYearButtons(false);
        panel.setShowYearButtons(true);
        
        MainPanel.revalidate();
        MainPanel.repaint();
    }
    
    private void setWindowStyle() {
        initComponents();
        
        //this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        // Set Windows Icon
        List<Image> icons = new ArrayList<Image>();
        icons.add(new javax.swing.ImageIcon(getClass().getResource("/img/factory.png")).getImage());
        this.setIconImages(icons);
        // Set Window In Screen Center
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        MainPanelLayout.setVerticalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(MainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if(!model.isSelected()) {
            JOptionPane.showMessageDialog(this, "Vous devez sélectionner une date !");
        }
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JDatePickerPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JDatePickerPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JDatePickerPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JDatePickerPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new JDatePickerPanel(null, true).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel MainPanel;
    // End of variables declaration//GEN-END:variables
}