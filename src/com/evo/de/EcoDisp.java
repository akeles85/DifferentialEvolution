/*
 * EcoDisp.java
 *
 *This is a test class for Lambda Iteration
 *The class must be updated according to latest additions.
 *Because it is only used to test Lambda Iteration and after this step there are lots of change in software
 */

package com.evo.de;

import com.evo.ecd.LambdaIteration;
import com.evo.ecd.PowerGenerator;
import com.evo.ecd.PowerGeneratorData;
import java.io.*;
import java.util.*;

/**
 *
 * @author root
 */
public class EcoDisp {
    
    private LambdaIteration lambdaIteration = new LambdaIteration();
    
    private int numOfGenerator;
    
    private PowerGenerator generators[];
    
    
    
    /** Creates a new instance of EcoDisp */
    public EcoDisp() {
        generators = new PowerGenerator[numOfGenerator];
    }
    
    private ArrayList<PowerGeneratorData> readFile(String filename) throws Exception
    {
        
        ArrayList<PowerGeneratorData> list = new ArrayList();
        
        FileReader f = new FileReader(filename);
        
        BufferedReader reader = new BufferedReader(f);
        
        reader.readLine();  //header
        
        while(true)
        {
            String line = reader.readLine();
            
            if( line == null )
                break;
            
            PowerGeneratorData generatorData = new PowerGeneratorData();
            
            String column[] = line.split("\t");
            
            generatorData.setName(column[0]);
            
            generatorData.setPmax(Double.valueOf( column[1] ));
            
            generatorData.setPmin(Double.valueOf( column[2] ));
            
            generatorData.setA(Double.valueOf( column[3] ));
            
            generatorData.setB(Double.valueOf( column[4] ));
            
            generatorData.setC(Double.valueOf( column[5] ));
            
            generatorData.setFuelCost(Double.valueOf( column[6] ));
            
            generatorData.setHotStartUpCost(Double.valueOf( column[7] ));
            
            generatorData.setColdStartUpCost(Double.valueOf( column[8] ));
            
            generatorData.setHourForColdStart(Double.valueOf( column[9] ).intValue());
            
            generatorData.setInitialState( Integer.valueOf(column[10]));
            
            list.add(generatorData);
            
        }
        
        f.close();
        
        this.numOfGenerator = list.size();
                
        return list;
    }
    
    public void solve(final double demand,final double tolerance)throws Exception
    {
        this.generators = this.lambdaIteration.calcECD(this.generators,demand,tolerance);
        
        double totalCost = 0;
        for(int i = 0; i < this.generators.length; i++){
            generators[i].print();
            totalCost += generators[i].calcCost(generators[i].getPout());
            System.out.println(i + ". :" + totalCost);
            
        }
        
        System.out.println("Total Cost: "+totalCost);
        
    }
    
        /**
     *numOfHour and numOfGenerator must already been set.
     *This method sets the generator data for PowerGenerator objects 
     *The data is seperated from PowerGenerator in order to lower the memory need
     */
    public void setGeneratorsData(ArrayList<PowerGeneratorData> generatorsData)
    {
        this.generators = new PowerGenerator[this.numOfGenerator];
        

        for(int j = 0; j < this.numOfGenerator; j++)
        {
            this.generators[j] = new PowerGenerator();           //create the objects
            
            this.generators[j].setOn(false);

            this.generators[j].setGeneratorData( generatorsData.get( j ) );    //Set the generators' data

        }   
        
        this.generators[3].setOn(true);
                
    }
    
    public static void main(String args[])throws Exception
    {
        EcoDisp ecoDisp = new EcoDisp();
        
        ArrayList<PowerGeneratorData> generatorsData = ecoDisp.readFile("/home/alikeles/test_files/input3");
        
        ecoDisp.setGeneratorsData(generatorsData);
        
        ecoDisp.solve(450,0.00001);
    }
    
}
