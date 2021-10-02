/*
 * Result.java
 * This class is desinged to represent the final results of the algorithm
 *It is used to display the results on conole
 */

package com.evo.de;

import com.evo.de.algorithm.Chromosome;
import java.util.*;
/**
 *
 * @author ali.keles
 */
public class Result {
    
    private int numOfIteration;
    
    private ArrayList<Chromosome> allBestResults = new ArrayList();
    
    private Chromosome finalResult;
    
    private int iterNumOfFinalResult;
            
    /** Creates a new instance of Result */
    public Result() {
    }

    public int getNumOfIteration() {
        return numOfIteration;
    }

    public void setNumOfIteration(int numOfIteration) {
        this.numOfIteration = numOfIteration;
    }


    public Chromosome getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(Chromosome finalResult) {
        this.finalResult = finalResult;
    }

    public int getIterNumOfFinalResult() {
        return iterNumOfFinalResult;
    }

    public void setIterNumOfFinalResult(int iterNumOfFinalResult) {
        this.iterNumOfFinalResult = iterNumOfFinalResult;
    }
    
    public void addResult(Chromosome result)
    {
        this.allBestResults.add(result);
    }    
    
    public ArrayList<Chromosome> getAllResults()
    {
        return this.allBestResults;
    }

    public String toString() 
    {
        StringBuffer display = new StringBuffer("");
        
        display.append("Results " + '\n');
        
        display.append("Number of Iteration: " + this.numOfIteration + '\n');
        
        display.append("Best results from each iteration: " + '\n');
        
        for(int i = 0; i < this.allBestResults.size(); i++)
        {
            String temp = String.valueOf( this.allBestResults.get(i).getFitness() );
            
          //  display.append( i +". iteration :  " + temp + " numOfBits: " + this.countChar(temp,'0') +'\n');
            display.append( i +". iteration :  " + temp + '\n');
        }
        
        
        display.append("All time Best Result = " + this.finalResult.getFitness() + '\n');
        
        System.out.println("aaaa");
        for(int i = 0; i < this.finalResult.getNumOfGenes(); i++)
            System.out.print( this.finalResult.getGene(i) + ", ");
        
        
        return display.toString();
    }
    
    public static int countChar(final String target, final char c)
    {
        int result = 0;
        for(int i = 0;  i < target.length();  i++)
        {
            if(target.charAt(i) == c)
            {
                result++;
            }
        }
        return result;
    }
    
}
