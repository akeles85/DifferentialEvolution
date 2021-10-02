/*
 * LambdaIteration.java
 *
 * Created on 08 Mart 2007 Perşembe, 20:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.evo.ecd;

import com.evo.de.util.DEException;
import java.io.*;
import java.util.*;

/**
 *
 * @author ali.keles
 * This class is designed to implement Lambda Iteration
 * It finds an optimal solution for the unit commitment problem
 */
public class LambdaIteration {
    
    private double lambda;
    
    private double delta;
    
    private double totalCost;
    
    private static final int MAXNUMOFITER = 5000;
    
    /** Creates a new instance of LambdaIteration */
    public LambdaIteration() {
        
    }
    
    /**
     *initialize lambda according to lambda_min and lambda_max
     */
    public void initLambda(PowerGenerator generators[])
    {
        double min = lambdaMin(generators);
        
        double max = lambdaMax(generators);
        
        this.lambda = ( max + min ) / 2;
        
        this.delta = ( max - min ) / 2;
  
    }
    
    /**
     *Calculate lambda max
     *
     */
    private double lambdaMax(PowerGenerator generators[])
    {   
        double lambdaMax = Double.MIN_VALUE;
        
        for(int i = 0; i < generators.length; i++)
        {
            PowerGenerator generator = generators[i];
            
            if( generator.isOn() )
            {
                PowerGeneratorData generatorData = generator.getGeneratorData();
                
                double currentLambda = generatorData.getB() + 2 * generatorData.getPmax() * generatorData.getC();
            
                if( lambdaMax < currentLambda )
                {
                    lambdaMax = currentLambda;
                }
            }
                
        }
        
        return lambdaMax;
        
    }
    
    /**
     *Calculates lambda min
     */
    private double lambdaMin(PowerGenerator generators[])
    {   
        double lambdaMin = Double.MAX_VALUE;  
        
        for(int i = 0; i < generators.length; i++)
        {
            PowerGenerator generator = generators[i];
            
            if( generator.isOn() )    //If the generator isnt open, ignore it while calculating labda
            {
                PowerGeneratorData generatorData = generator.getGeneratorData();
                
                double currentLambda = generatorData.getB() + 2 * generatorData.getPmin() * generatorData.getC();

                if( lambdaMin > currentLambda )
                {
                    lambdaMin = currentLambda;
                }
            }
                
        }
        
        return lambdaMin;
        
    }
    
    
    /**
     *Finds the Pout value of each open generator
     */
    public PowerGenerator[] calcECD(PowerGenerator generators[], final double powerDemand, final double tolerance) throws DEException
    {
        
        if(generators.length == 0)
        {
            throw new DEException("There are no generators to calculte ECD");
        }
        
        double epsilon = 0;
        //ToDo 1
        this.initLambda(generators);
        
        //ToDo 2
        
        int numOfIter = 0;
        do{
            //ToDo 2.1
            //Find all Pi of open generators with the equation of dF/dP = lambda

             int size = generators.length;

             for( int i = 0; i < size; i++ )
             {
                  PowerGenerator generator = generators[ i ] ;
                  
                 if(generator.isOn())
                 {
                    PowerGeneratorData generatorData = generator.getGeneratorData();
                    
                    generator.setPout((this.lambda - generatorData.getB()) / (2 * generatorData.getC()));
                 
                    if(generator.getPout() < generatorData.getPmin())
                    {
                        generator.setPout(generatorData.getPmin());
                    }
                 
                    if(generator.getPout() > generatorData.getPmax())
                         generator.setPout(generatorData.getPmax());
                 }
                 else
                     generator.setPout(0);
             }

             double sumationOfPower = 0;
             //ToDo 2.2
             for( int i = 0; i < size; i++)
             {
                 PowerGenerator generator = generators[ i ];
                 if( generator.isOn() )
                 {
                    sumationOfPower += generators[ i ].getPout() ;
                 }
             }

             //ToDo 2.3
             //Compare the powerDemand to total generated power

             epsilon = powerDemand - sumationOfPower;
             if( epsilon < 0)
             {
                 this.lambda = this.lambda - this.delta;
             }
             else
             {
                this.lambda = this.lambda + this.delta;
             }

             this.delta = this.delta / 2;
             numOfIter++;
             
        }while( Math.abs( epsilon ) > tolerance && numOfIter < MAXNUMOFITER);
        
        int counter = 0;
        
        double totalGenearatedPower = 0;
        
        this.totalCost = 0;
        
        for(int i = 0; i < generators.length; i++)
        {
            PowerGenerator generator = generators[ i ];
            
            totalGenearatedPower += generator.getPout();
            this.totalCost += generator.calcCost(generator.getPout());
            
        }

        return generators;
    }

    public double getTotalCost() 
    {
        return totalCost;
    }
    
}
