/*
 * DEApp.java
 *
 * Created on 16 Subat 2007 Cuma, 23:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.evo.de.algorithm;

import com.evo.de.*;
import com.evo.de.util.RandomGenerator;
import java.util.*;

/**
 *
 * @author ali.keles
 */
public class DEApp {
    
    /** Creates a new instance of DEApp */
    public DEApp() {
    }
    
     /**
     *Takes "number" item from list which are distinct.
     *The index of the new items cannot be equal to forbiddenIndexes.
     *The created random numbers are less then list size.
     *The results will be returned with integer array
     */
    public static int[] chooseDistinct(final ArrayList list, final int number, final int []forbiddenIndexes) throws Exception
    {
        /**
         * If list size is smaller than the number of random numbers that are wanted, an exception will be thrown
         *Because in this situation there arent available items in the list
         */
        if( list.size() <= number )
        {
            throw new Exception( number + " random cannot be obtained from the list sized: " + list.size() );
        }
        
        int[] result = new int[ number ];           //The result will be returned with this list
        
        int[] savedIndex = new int[ number ];         //This array will hold the generated random numbers 
        
        Arrays.fill(savedIndex,-1);                 //Fill the array with -1 to obtain "zero" in future
        
        int counter = 0;                            //index for number of distinct numbers        
        
        while( counter < number )
        {
            //create a random number between 0 and list.size() - 1
            int random = RandomGenerator.genInt(list.size()); 
            
            //Searches the new random number in the array which holds the past random numbers 
            //It is important that the array named savedIndex must not have the default value as one of the acceptable number like 0
            if( Arrays.binarySearch( savedIndex,random ) < 0  )
            {
                boolean isExist = false;
                for( int i = 0; i < forbiddenIndexes.length ; i++ )
                {
                    if ( random == forbiddenIndexes[i] )
                        
                        isExist = true;
                }
                
                if( !isExist )
                {
                    //It is important to put the newly generated number as the first element of the array
                    //Because every time the array is sorted, the first element will be -1 while the array is not full
                    //So non of the past values will be OVERRIDEN.
                    savedIndex[0] = random ;                //record the generated number

                    Arrays.sort(savedIndex);                //sort the array to use binarySearch operation

                    result[ counter ] = random;
                   // resultList.add( list.get(random) ) ;    //add the new list member to resultList
                    counter ++ ;
                    
                }
            }
        }
        return result;
    }
    
         /**
     *Takes "number" item from list which are distinct.
     *The index of the new items cannot be equal to forbiddenIndex.
     *The created random numbers are less then list size.
     *The results will be returned with ArrayList
     */
    public static int[] chooseDistinct(final ArrayList list, final int number, final int forbiddenIndex1, final int forbiddenIndex2) throws Exception
    {
        /**If list size is smaller than the number of random numbers that are wanted, an exception will be thrown
         *Because in this situation there arent available items in the list
         */
        if( list.size() <= number )
        {
            throw new Exception( number + " random cannot be obtained from the list sized: " + list.size());
        }
        
        int[] result = new int[ number ];           //The result will be returned with this list
        
        int[] savedIndex = new int[number];         //This array will hold the generated random numbers 
        
        Arrays.fill(savedIndex,-1);                 //Fill the array with -1 to obtain "zero" in future
        
        int counter = 0;                            //index for number of distinct numbers        
        
        while(counter < number)
        {
            //create a random number between 0 and list.size() - 1
            int random = RandomGenerator.genInt(list.size()); 
            
            //Searches the new random number in the array which holds the past random numbers 
            //It is important that the array named savedIndex must not have the default value as one of the acceptable number like 0
            if(Arrays.binarySearch( savedIndex,random ) < 0 && random != forbiddenIndex1 && random != forbiddenIndex2)
            {
                //It is important to put the newly generated number as the first element of the array
                //Because every time the array is sorted, the first element will be -1 while the array is not full
                //So non of the past values will be OVERRIDEN.
                savedIndex[0] = random ;                //record the generated number
                
                Arrays.sort(savedIndex);                //sort the array to use binarySearch operation
                
                result[ counter ] = random;
               // resultList.add( list.get(random) ) ;    //add the new list member to resultList
                counter ++ ;
            }
        }
        return result;
    }
    
    public static void mirroring(Chromosome chromosome) throws Exception
    {
        int numOfGenes = chromosome.getNumOfGenes();
        
        for( int i = 0; i < numOfGenes; i++ )
        {
            Gene gene = chromosome.getGene(i);
            
            while(!gene.isAtBounds())          // if a gene is not at bounds
            {
                if(gene.getValue() > gene.getMaxBound())
                {
                    double difference = gene.getValue() - gene.getMaxBound();
                    
                    difference = gene.getMaxBound() - difference ;
                    
                    gene.setValue(difference);
                }
                
                else if(gene.getValue() < gene.getMinBound())
                {
                    double difference = gene.getMinBound() - gene.getValue();
                    
                    difference = gene.getMinBound() + difference;
                    
                    gene.setValue(difference);
                }
                
                else
                {
                    throw new Exception("There is something wrong with isAtBounds or mirroring");
                }
                
            }
        }
        
    }  
    
}
