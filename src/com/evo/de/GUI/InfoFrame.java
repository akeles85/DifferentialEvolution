/*
 * InfoFrame.java
 *
 * Created on 03 Mayıs 2007 Perşembe, 22:20
 */

package com.evo.de.GUI;

import java.awt.Toolkit;
import javax.swing.JOptionPane;
import java.awt.Dimension;

/**
 *
 * @author  root
 */
public class InfoFrame extends javax.swing.JFrame {
    
    /** Creates new form InfoFrame */
    public InfoFrame() {
        initComponents();
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowDim = this.getSize();
        
        double x = screenDim.getWidth();
        double y = screenDim.getHeight();
        x = x / 2;
        y = y / 2;
        x = x - windowDim.getWidth() / 2;
        y = y - windowDim.getHeight() / 2;
        this.setLocation( (int)x, (int)y );
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        numOfGenerator = new javax.swing.JTextField();
        numOfHour = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Unit Commitment Problem");
        setBackground(new java.awt.Color(153, 153, 153));

        jLabel1.setText("Number of Generator");

        jLabel2.setText("Number of Time Horizon");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 24));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Unit Commitment Problem");

        jButton1.setText("OK");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(49, 49, 49)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jLabel1))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, numOfHour)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, numOfGenerator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jButton1))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 106, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(36, 36, 36)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(numOfGenerator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(numOfHour, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton1)
                .addContainerGap(151, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        
        int hour = 0, generator = 0;
        
        try{
            generator = Integer.valueOf( this.numOfGenerator.getText() ); 
            
            hour = Integer.valueOf( this.numOfHour.getText() );
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,"Please enter a valid value","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        this.setVisible(false);
        
        GradProjGUI mainFrame = new GradProjGUI( generator, hour ,0 );
        
        mainFrame.setVisible(true);
           
    }//GEN-LAST:event_jButton1MouseClicked
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InfoFrame().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField numOfGenerator;
    private javax.swing.JTextField numOfHour;
    // End of variables declaration//GEN-END:variables
    
}
