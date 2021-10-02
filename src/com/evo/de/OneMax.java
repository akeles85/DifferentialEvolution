/*
 * OneMax.java
 *
 
 *
 */

package com.evo.de;

import com.evo.de.GUI.ProgressGUI;
import com.evo.de.algorithm.Chromosome;
import com.evo.de.algorithm.*;
import com.evo.de.problem.BinarySpace;
import com.evo.de.problem.Problem;
import com.evo.de.util.RandomGenerator;
import static java.lang.Math.*;
import java.util.Arrays;

/**
 *
 * @author ali.keles
 */
public class OneMax extends Problem implements BinarySpace, Runnable{
    
    private static final int NUM_OF_BITS = 100;
    
    private int solution[] ;
    /** Creates a new instance of OneMax */
    public OneMax()
    {
        super();
        
        solution = new int[NUM_OF_BITS];
        for( int i = 0; i < NUM_OF_BITS; i++ )
        {
            if( RandomGenerator.genInt(2) == 1)
                solution[i] = 0;
            else
                solution[i] = 1;
        }
        System.out.println( Arrays.toString(solution) ) ;
        
        
        this.initParameters();
    }
    
    protected void initParameters()
    {
        try
        {
            for(int i = 0; i < NUM_OF_BITS; i++)
                this.addParameter( 0,1 );
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     *Objective is minimizing, smaller fitness function is better
     */
    public double fitness(final Chromosome chromosome)
    {
        int fitness = 0;
       
        int hamming = 0;
        
        for( int i = 0; i < NUM_OF_BITS; i++)
        {
            int value = (int)(chromosome.getGene(i).getValue());
            
            if( solution[i] != value )
            {
                hamming++;
            }
        }

        fitness = this.NUM_OF_BITS - hamming;
        
        return fitness;
    }
    
    public boolean satisfyFunction(double fitness)
    {
        if(fitness == NUM_OF_BITS)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
    /**
     * Returns the String which contains all the parameters of a solution
     */
    public String parametersToString(Chromosome chromosome)
    {
        String bitString = String.valueOf(this.toBitString(chromosome));
        
        return bitString;
        
    }
    
        /**
     * Differential Evoluiton is used for real valued parameters,
     *to use it for binary space a special function is used.
     *The parameters of this function are optimized with DE algorithm,
     *and every time when it is needed to calculate the fitness, these
     *parameters should be transformed to binary strings with the special function
     *This method transform the chromosome to a binary char array
     */
    public char[] toBitString(final Chromosome chromosome)
    {
        char[] bitString = new char[this.NUM_OF_BITS];
                
        for(int x = 0; x < this.NUM_OF_BITS; x++)
        {
            double value = chromosome.getGene(x).getValue();
            
            if(value > 0.5)
            {
                bitString[x] = '1';
            }
            else{
                bitString[x] = '0';
            }
        }
        return bitString;
    }
    
    public void run()
    {   
        Result result = new Result();
        
        int populationSize = 100;
        
        try
        {
            DE diffEvoAlgorithm = new DE(this, MutationType.BINARY_RAND_1,false, new ProgressGUI());
            
            result = diffEvoAlgorithm.start();
            
            System.out.println(result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String args[]){
        OneMax oneMax = new OneMax();
        Thread thread = new Thread(oneMax);
        thread.start();

    }
    
    
}
