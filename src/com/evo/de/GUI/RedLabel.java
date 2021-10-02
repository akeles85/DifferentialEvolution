/*
 * RedLabel.java
 *
 * Created on 06 Mayıs 2007 Pazar, 02:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.evo.de.GUI;

import java.awt.Color;
import javax.swing.JLabel;

/**
 *
 * @author root
 */
public class RedLabel extends JLabel{
    
    /** Creates a new instance of RedLabel */
    public RedLabel() {
        super();
        this.setForeground( Color.BLUE);
        this.setBackground(Color.DARK_GRAY);
    }
    
    public RedLabel( String value){
        super(value);
        this.setForeground( Color.BLUE);
        this.setBackground(Color.DARK_GRAY);
    }
    
}
