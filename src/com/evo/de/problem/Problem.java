/*
 * Problem.java
 *
 * Created on 17 Subat 2007 Cumartesi, 01:34
 */

package com.evo.de.problem;

import com.evo.de.algorithm.Chromosome;
import com.evo.de.algorithm.Gene;
import java.lang.Math;

/**
 *
 * @author ali.keles
 */
public abstract  class Problem {
    
    /**
     * Every chromosome must be created from this chromosome.
     * Because the parameters of problem are inserted to this chromosome and
     * each chromosome in the problem domain should include these parameters
     */
    private Chromosome rootChromosome;          
    
    /** Creates a new instance of Problem */
            
    public Problem()
    {
        rootChromosome = new Chromosome();
    }
    
    /**
     * Returns smaller values for the individuals who has higher fitness
     */
    public abstract double fitness(final Chromosome chromosome);
    
    /**
     *Returns true if the fitness is acceptable
     */
    public abstract boolean satisfyFunction(double fitness);
    
    /**
     * Add parameters of problem
     */
    protected abstract void initParameters();
    
    
    /**
     *Add a parameter to the chromosome as a gene
     *
     */
    public void addParameter(final double minBound, final double maxBound) throws Exception
    {
        Gene gene = new Gene();
        
        gene.setBounds(minBound,maxBound);
        
        this.getRootChromosome().insertGene(gene);
    }

    /**
     * Root Chromosome is the chromosome that has the appropriate parameters used by problem
     * Every chromosome in the algorithm must have a relation with this chromosome.
     */
    public Chromosome getRootChromosome()
    {
        return rootChromosome;
    }
    
    public abstract String parametersToString(Chromosome chromosome);          
    
    /*If hill climbing is wanted to be used, this function must be overriden
     *or there will be no effect of this function
     *
     */
    public double hillClimbing(Chromosome chromosome, final boolean flag)
    {
        return this.fitness(chromosome);
    }

    public Chromosome fixOperator(Chromosome chromosome )
    {
        return chromosome;
    }

    /*
     * sigma = sqrt( 1 / N * ( sum( square(x_i - x_mean) ) ) )
     */
    public double standardDeviation(double array[])
    {
        double sigma = 0;
        double sumation = 0;
        double term = 0;
        double mean = 0;
        
        for( int i = 0; i < array.length; i++)
        {
            mean += array[ i ];
        }
        
        mean = mean / (double)array.length;
        
        for( int i = 0; i < array.length; i++)
        {
            term = Math.pow( array[ i ]  - mean, 2 ) ;
            sumation += term;
        }
        
        sigma = Math.sqrt( sumation / (double)array.length );
        
        return sigma;
    }
    
    /*
     *standard_error=standard_deviation/SQRT(N)
     */
    public double standardError( double array[] )
    {
        double deviation = this.standardDeviation( array );
        double error = 0;
        
        error = deviation / Math.sqrt( array.length );
        
        return error;
        
    }
       
}




