/*
 * RandomGenerator.java
 *
 * Created on 16 �ubat 2007 Cuma, 21:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.evo.de.util;

import java.util.Random;

/**
 *
 * @author ali.keles
 */
public class RandomGenerator {
    
    private static Random generator = new Random();
    
    private int prime[];
    
    /** Creates a new instance of RandomGenerator */
    public RandomGenerator()
    {
        prime = new int[1];
        prime[0] = 2;
        
    }
    
    /**
     *   0 <= x < 1
     *  x * ( max - min ) + min
     *   -> min <= x < max
     */
    public static double genRealNum(final double min, final double max)
    {
        double result = ( getGenerator().nextDouble() * ( max - min ) ) + min ;
        
        return result;
    }
    
    /**
     *Implementaion is taken from Differential Evolution
     */
    public double halten( final int totalPopulation, final int numOfParam, final int indexOfParam)
    {
        int []primes = this.findPrimeNumbers( totalPopulation );
        
        int p1,p2,i;
        double sum,x;
        
        i = totalPopulation;
        
        p1 = primes[ indexOfParam ];
        
        p2 = p1;
        
        sum = 0;
        
        do{
            x = i % p1;
            
            sum += x / p2;
            
            i =  (int) i / p1;
            
            p2 = p2 * p1;           
            
        }while( i > 0);
        
        return sum;
        
    }
    
    public int[] findPrimeNumbers( final int size )
    {
        int result[];
        
        //If the prime numbers have already been calculated, do not calculate again
        if( size == this.prime.length )
            
            result = this.prime;
        
        else
        {
            PrimeNumber factory = PrimeNumber.getInstance();
            
            result = factory.findPrimeNumbers(size);
            
            this.prime = result;
            
        }
        
        return result;
    }
    
    /**
     *@return 0 <= n <= x - 1
     *
     */
    public static int genInt(final int x)
    {
        int result = getGenerator().nextInt(x);
        return result;
    }
    
    public static double genDouble()
    {
        double result = getGenerator().nextDouble();
        return result;
    }

    public static Random getGenerator() {
        return generator;
    }

}
