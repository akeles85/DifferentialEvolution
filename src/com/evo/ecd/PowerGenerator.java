/*
 * PowerGenerator.java
 *
 * Created on 08 Mart 2007 Perşembe, 20:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.evo.ecd;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import java.util.*;
/**
 *
 * @author root
 */
public class PowerGenerator {
    
    private double Pout;
    
    private boolean on;
    
    private int stopDuration;   //indicates how many hours does the generator stay closed
    
    private PowerGeneratorData generatorData;
    
    /** Creates a new instance of PowerGenerator */
    public PowerGenerator() 
    {
        setGeneratorData(new PowerGeneratorData());
    }
    
    public void print()
    {
        
        System.out.print( this.Pout + "  ");
        
    }
    
    /**
     *Calculates the total cost according to amount of power
     */
    public double calcCost(final double amountOfPower)
    {
        double cost = 0;
        
        if( this.isOn() )
        {
            cost = this.getGeneratorData().getA() + this.getGeneratorData().getB() * this.Pout + this.getGeneratorData().getC() * Math.pow( this.Pout, 2 ) ;
            
            cost = cost * this.getGeneratorData().getFuelCost();
        }
        return cost;
    }

    /**
     *Calculates the start up cost accoring to the time that it has been closed
     */
    public double startUpCost()
    {
        double cost = 0;
        
        if( this.isOn() && this.stopDuration != 0)
        {
            if( this.stopDuration <= this.generatorData.getHourForColdStart() )
            {    
                cost = this.generatorData.getHotStartUpCost();
                
            }
            
            else
            {
                cost = this.getGeneratorData().getColdStartUpCost();
            }
            
        }
        else
        {
            cost = 0;       //If it is closed there will be no startUpCost
        }
        
        return cost;
    }
    
    /**
     *Calculates the sumation of generators' Pmax which are open
     *
     */
    public static double totalOnPmax( PowerGenerator inGenerators[] )
    {
        double result = 0;
        
        PowerGeneratorData generatorData;
        
        for(int genNum = 0; genNum < inGenerators.length; genNum++ )
        {
            if( inGenerators[genNum].isOn() )
            {
                generatorData = inGenerators[ genNum ].getGeneratorData();
            
                result += generatorData.getPmax();
                
            }
        }
        
        return result;
        
    }

     /**
     *Calculates the sumation of generators' Pout which are open
     *
     */
    public static double totalOnPout( PowerGenerator inGenerators[] )
    {
        double result = 0;
        
        for(int genNum = 0; genNum < inGenerators.length ; genNum++ )
        {
            if( inGenerators[genNum].isOn() )
            {
                result += inGenerators[ genNum ].getPout();
            }
        }
        
        return result;
    }
            
    public double getPout() {
        return Pout;
    }

    public void setPout(double Pout) {
        this.Pout = Pout;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean status)
    {
        this.on = status;
        
        if( this.on == false )      //if it is closed, no power will be generated
            this.Pout = 0.0;
    }

    public PowerGeneratorData getGeneratorData() {
        return generatorData;
    }

    public void setGeneratorData(PowerGeneratorData generatorData) {
        this.generatorData = generatorData;
    }

    public int getStopDuration() {
        return stopDuration;
    }

    public void setStopDuration(int stopDuration) {
        this.stopDuration = stopDuration;
    }
    
    public static double maxPenaltyCost (PowerGenerator generators[], double power )
    {
        double maxCost = Double.MIN_VALUE;
        
        for( int i = 0; i < generators.length; i++ )
        {
            double cost = 0;
        
            cost = generators[i].getGeneratorData().getA() + generators[i].getGeneratorData().getB() * power + generators[i].getGeneratorData().getC() * Math.pow( power, 2 ) ;
            
            cost = cost * generators[i].getGeneratorData().getFuelCost();
            
            if( cost > maxCost )
                
                maxCost = cost;
            
        }
        
        return maxCost;
       
    }
    
    public int violetedNumber(boolean []inStatus)
    {
        int violated = 0;
        int closed = 0;
        int opened = 0;
        boolean status_changed = false;
        boolean []status = new boolean [ inStatus.length + 1];      //for initial state,        
        
        if( this.generatorData.getInitialState() < 0 )
        {    
            closed = -1 * this.generatorData.getInitialState();
            
            status[ 0 ] = false;
        }
        
        else
        {    
            opened = this.generatorData.getInitialState();
            
            status[ 0 ] = true;
        }
        
        //copy the array to status, the first index is belonged to initial state 
        for( int i = 0; i < inStatus.length ; i++)
            
            status[ i + 1 ] = inStatus[ i ];
        
        for( int i = 1 ; i < status.length ; i++)
        {
            if( status[i] == false )
            {
                closed++;
                
            }
            else
                opened++;
        
            if( status[i] != status[i - 1] )
            {
                status_changed = true;
            }
            else{
                status_changed = false;
            }

            if( status_changed )
            {
                if( status[ i - 1 ] == false)
                {
                    if( closed < this.generatorData.getMinOff() )
                    {
                        //violated += 2 * Math.round(this.getGeneratorData().getPmax() / meanPmax ) ; //IMPORTANT
                        violated +=  this.getGeneratorData().getPmax(); 
                        
                        status[ i ] = false;
                        opened = 0;
                        closed++;
                    }
                    else
                    {
                        closed = 0;
                    }
                }

                else
                {
                    if( opened < this.generatorData.getMinOn() )
                    {
                        violated+= this.getGeneratorData().getPmin();
                        status[ i ] = true;
       //                 System.out.println("acilmali");
       //                 System.out.println("violated " + violated);
                        closed = 0;
                        opened++;
                    }
                    else
                    {
                        opened = 0;
                    }
                }
            }
               
        }
        
        //System.out.println("new Array: "+  Arrays.toString( status ) );
        
       // System.out.println("Total Violated: " + violated);
        
        return violated;
    }
    
    public boolean[] fixViolated(boolean []inStatus)
    {  
        int closed = 0;
        int opened = 0;
        boolean status_changed = false;
        boolean []status = new boolean [ inStatus.length + 1];      //for initial state, 
        
       // System.out.println( Arrays.toString( inStatus ) );
        
        if( this.generatorData.getInitialState() < 0 )
        {    
            closed = -1 * this.generatorData.getInitialState();
            
            status[ 0 ] = false;
        }
        
        else
        {    
            opened = this.generatorData.getInitialState();
            
            status[ 0 ] = true;
        }
        
        //copy the array to status, the first index is belonged to initial state 
        for( int i = 0; i < inStatus.length ; i++)
            
            status[ i + 1 ] = inStatus[ i ];
        
        for( int i = 1 ; i < status.length ; i++)
        {
            if( status[i] == false )
            {
                closed++;
                
            }
            else
                opened++;
        
            if( status[i] != status[i - 1] )
            {
                status_changed = true;
            }
            else
            {
                status_changed = false;
            }

            if( status_changed )
            {
                if( status[ i - 1 ] == false)
                {
                    if( closed < this.generatorData.getMinOff() )
                    {
                        status[ i ] = false;
                        opened = 0;
                        closed++;
                //        System.out.println("kapa");
                    }
                    else
                    {
                        closed = 0;
                    }
                }

                else
                {
                    if( opened < this.generatorData.getMinOn() )
                    {
                        status[ i ] = true;
                   //     System.out.println("ac");
                        closed = 0;
                        opened++;
                    }
                    else
                    {
                        opened = 0;
                    }
                }
            }
               
        }
        
        boolean []result = new boolean[ status.length - 1 ];
        
        for( int i = 0; i < result.length; i++ )
            result[ i ] = status[ i + 1];
            
        return result;
    }
}
