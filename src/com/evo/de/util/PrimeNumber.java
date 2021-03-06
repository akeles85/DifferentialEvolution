package com.evo.de.util;
import java.util.Arrays;
/*
 * PrimeNumber.java
 *
 * Created on 31 Mart 2007 Cumartesi, 00:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author root
 */
public class PrimeNumber {
    
    private static PrimeNumber instance;
    
    private PrimeNumber(){
        
    }
    
    public static synchronized PrimeNumber getInstance()
    {
        if( instance == null)
            instance = new PrimeNumber();
        
        return instance;
        
    }
    
    // check whether a number is prime
    
    public boolean isPrime(int n) {
        
        // 2 is the smallest prime
        
        if (n <= 2) {
            return n == 2;
        }
        
        // even numbers other than 2 are not prime
        
        if (n % 2 == 0) {
            return false;
        }
        
        // check odd divisors from 3
        // to the square root of n
        
        for (int i = 3, end = (int)Math.sqrt(n);
        i <= end; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     *size is the number of prime numbers that wanted to be generated
     */
    public int[] findPrimeNumbers(int size)
    {
        int[] result = new int[size];
        
        for(int i = 0,number = 2; i < size; i++)
        {
            while (!isPrime(number))
            {
                number++;
            }
            
            result[i] = number;
            
            number++;
        }
        return result;
    }
    
    public static void main(String args[])
    {
        PrimeNumber p = PrimeNumber.getInstance();
        
        int numbers[] = p.findPrimeNumbers(200);
        
        System.out.println( Arrays.toString(numbers) );
        
    }
}
