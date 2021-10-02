/*
 * Progress.java
 *
 * Created on 07 Mayıs 2007 Pazartesi, 16:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.evo.de.GUI;

import javax.swing.JProgressBar;

/**
 *
 * @author root
 */
public class ProgressGUI {
    
    private JProgressBar currentProgress;
    
    private JProgressBar allProgress;
    
    private int numOfRun;
    
    private int numOfIter;
    
    private int current = 0;
    
    private int total = 0;
    
    /** Creates a new instance of Progress */
    public ProgressGUI() {
        currentProgress = new JProgressBar();
        allProgress = new JProgressBar();
    }
    
    public ProgressGUI(JProgressBar current, JProgressBar all, final int run, final int iter)
    {
        this.currentProgress = current;
        this.allProgress = all;
        this.numOfRun = run;
        this.numOfIter = iter;
    }

    public JProgressBar getCurrentProgress() {
        return currentProgress;
    }

    public JProgressBar getAllProgress() {
        return allProgress;
    }
    
    public void increase()
    {
        current++;
        total++;
        
        this.currentProgress.setValue( (current * 100) / this.numOfIter );
        this.allProgress.setValue( (total * 100) / (this.numOfIter * this.numOfRun));
        
        if( current == this.numOfIter)
            current = 0;
        
    }
    
    public void finish()
    {
        this.currentProgress.setValue(100);
        this.allProgress.setValue(100);
        current = 0;
        total = 0;
    }
    
}
