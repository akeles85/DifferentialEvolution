/*
 * ResultGUI.java
 *
 * Created on 06 Mayıs 2007 Pazar, 00:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.evo.de.GUI;

import com.evo.ecd.PowerGeneratorData;
import java.awt.*;
import java.io.BufferedReader;
import javax.swing.*;
import java.io.File;
import java.io.FileReader;

/**
 *
 * @author root
 */
public class ResultGUI extends JFrame{
    
    private File file;
    
    private JPanel panel = new JPanel();
    
    private JPanel resultPanel = new JPanel();
    
    private JPanel southPanel = new JPanel();
    
    private JScrollPane scrollResult = new JScrollPane();
    
    private double generator[][];
    
    private int numOfGenerator;
    
    private int numOfHour;
    
    private double bestResult;
    
    private double averageResult;
    
    private double worstResult;
    
    private double standardDeviation;
    
    private double standardError;
    
    /** Creates a new instance of ResultGUI */
    
    private ResultGUI() {
        super();
    }
    
    public ResultGUI(File file)
    {
        this();
        
        this.file = file;
        
        this.panel.setLayout(new BorderLayout());        
        
        this.scrollResult.setViewportView( this.resultPanel );
        
        this.panel.add(this.scrollResult, BorderLayout.CENTER);
        
        try{
            this.readFile();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        this.resultPanel.setLayout( new GridLayout( this.numOfHour + 1 , this.numOfGenerator + 1 ));
        
        this.resultPanel.add(new JLabel("Hour"));
        for( int i = 0; i < this.numOfGenerator ; i++)
            this.resultPanel.add(new JLabel("P"+(i+1)));
        
        for(int i = 0; i < this.numOfHour; i++){
                this.resultPanel.add( new JLabel("Hour " + (i+1)) );
            for(int j = 0; j < this.numOfGenerator; j++){
                if( i % 2 == 0)
                    this.resultPanel.add(new JLabel(String.valueOf( this.generator[i][j]) ) );
                else
                    this.resultPanel.add(new RedLabel(String.valueOf( this.generator[i][j]) ) );
            }
        }
        
        this.southPanel.setLayout( new GridLayout(5,2) );
        this.southPanel.add(new RedLabel("Best" ) );
        this.southPanel.add(new RedLabel(String.valueOf( this.bestResult) ) );
        
        
        this.southPanel.add(new JLabel("Average" ) );
        this.southPanel.add(new JLabel(String.valueOf( this.averageResult) ) );
        
        
        this.southPanel.add(new RedLabel("Worst" ) );
        this.southPanel.add(new RedLabel(String.valueOf( this.worstResult) ) );
        
        
        this.southPanel.add(new JLabel("Standard Deviation" ) );
        this.southPanel.add(new JLabel(String.valueOf( this.standardDeviation) ) );
        
        
        this.southPanel.add(new RedLabel("Standard Error" ) );
        this.southPanel.add(new RedLabel(String.valueOf( this.standardError) ) );
        
        this.panel.add(southPanel, BorderLayout.SOUTH);
        
        this.getContentPane().add( panel );
        this.setDefaultCloseOperation( this.DISPOSE_ON_CLOSE);
        
        this.setSize(400,400);
        
        
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
    
    public void readFile() throws Exception
    {   
        FileReader f = new FileReader( this.file);
        
        BufferedReader reader = new BufferedReader(f);
        
        String meta = reader.readLine();  //NUM_OF_GENERATOR * NUM_OF_HOUR
        System.out.println(meta);
        String[] metaData = meta.split("\t");
        
        this.numOfGenerator = Integer.parseInt( metaData[0] );
        
        this.numOfHour = Integer.parseInt( metaData[1] );
        
        reader.readLine();  //header
        
        this.generator = new double[this.numOfHour][this.numOfGenerator];

        
        for(int h = 0; h < this.numOfHour; h++)
        {
            String line = reader.readLine();
            String row[] = line.split("\t");
            
            for( int i = 0 ; i < this.numOfGenerator; i++ ){
                this.generator[h][i] = Double.valueOf( row[i + 1] );
            }
            
        }
        
        String line = reader.readLine();
        
        String temp[] = line.split("\t");
        
        this.bestResult = Double.valueOf( temp[1]);
        
        
        line = reader.readLine();
        
        temp = line.split("\t");
        
        this.averageResult = Double.valueOf( temp[1]);
        
        
        line = reader.readLine();
        
        temp = line.split("\t");
        
        this.worstResult = Double.valueOf( temp[1]);
        
        
        line = reader.readLine();
        
        temp = line.split("\t");
        
        this.standardDeviation = Double.valueOf( temp[1]);
        
        
        line = reader.readLine();
        
        temp = line.split("\t");
        
        this.standardError = Double.valueOf( temp[1]);
        
        f.close();
        
        for( int i = 0 ; i < this.numOfHour ; i++)
        {
            for( int j = 0; j < this.numOfGenerator; j++)
                this.generator[i][j] = Math.round( this.generator[i][j] );
            
        }
        
    }
    
    
    
}
