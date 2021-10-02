/*
 * Chromosome.java
 *
 * Created on 16 Subat 2007 Cuma, 18:30
 */

package com.evo.de.algorithm;

import com.evo.de.util.RandomGenerator;
import com.evo.de.util.RandomGeneratorType;
import java.util.*;

/**
 *
 * @author ali.keles
 */
public class Chromosome implements Cloneable,Comparable{
    
    private ArrayList<Gene> genes;  //Holds the parameters
    
    private double fitness;
    
    private int numOfGeneration;
    
    private boolean hillClimbed = false;
    
    private double demandPenalty = 0.0;
    
    private double upDownPenalty = 0.0;
    
    public Chromosome() 
    {
        genes = new ArrayList();
    }
    
    /**
     * Initialize the genes in a chromosome
     * This method simply gives random numbers as the value of genes
     */
    public void initialize(final int populationSize, final int indexOfPopulation, RandomGeneratorType type)
    {    
        this.setNumOfGeneration(0);
        int numOfGene = this.genes.size();
        
        for(int j = 0; j < numOfGene; j++)
        {       
            this.genes.get(j).init(populationSize, numOfGene, j + (indexOfPopulation * numOfGene) , type);
            
        }
        
    }
    
    /**
     * Add <param>opp2 </param> to the current Chromosome and creates
     * a new Chromosome which has the value obtained from addtion.
     * Neither the current object nor <param>opp2</param> will be changed. 
     */
    public Chromosome addition(final Chromosome opp2)
    {
        Chromosome chromosome = new Chromosome();       //Create new Chromosome for result
        
        int size = genes.size();                    
        
        for(int i = 0; i < size ; i++)
        {
            Gene gene = (Gene)this.genes.get(i).clone();        //Clone the next gene 
            
            double value = this.genes.get(i).getValue() + opp2.genes.get(i).getValue(); //add the value of genes 
            
            gene.setValue(value);           //set the value of the new gene
            
            chromosome.genes.add(gene);     //add the gene to the list and go to the next gene in the chromosome
        }
        
        return chromosome;
    }
    
     /**
     * Substract <param>opp2 </param> from the current Chromosome and creates
     * a new Chromosome which has the value obtained from substraction.
     * Neither the current object nor <param>opp2</param> will be changed. 
     */
    public Chromosome substract(final Chromosome opp2)
    {    
        Chromosome chromosome = new Chromosome();       //Create new Chromosome for result
        
        int size = genes.size();                    
        
        for(int i = 0; i < size ; i++)
        {
            Gene gene = (Gene)this.genes.get(i).clone();        //Clone the next gene 
            
            double value = this.genes.get(i).getValue() - opp2.genes.get(i).getValue(); //substract the value of genes 
            
            gene.setValue(value);           //set the value of the new gene

            
            chromosome.genes.add(gene);     //add the gene to the list and go to the next gene in the chromosome
        }
        
        return chromosome;
    }
    
     /**
     * Multiply every gene of Chromosome with the given double typed multiplier 
     */
    public Chromosome multiply(final double multiplier)
    {
        Chromosome result = new Chromosome();
        
        int size = this.genes.size();
        
        for(int i = 0; i < size; i++)
        {
            Gene gene = (Gene)this.genes.get(i).clone();
            gene.setValue( gene.getValue() * multiplier );
            result.genes.add(gene);
        }
        
        return result;
    }
    
    /**
     * Get the gene from Chromosome with the given index
     */
    
    public Gene getGene(final int i)
    {
        return this.genes.get(i);
    }
    
    /**
     * Inserts a new gene to the Chromosome
     * This function usually will be used after the problem's parameters are decided
     * The copy of the given gene will be created and this copy will be added to chromosome
     */
    public void insertGene(final Gene gene)
    {
        this.genes.add((Gene)gene.clone());
    }
    
    /**
     *Returns the number of genes in a chromosome
     */
    public int getNumOfGenes()
    {
        return this.genes.size();
    }
    
    public Object clone()
    {
        Chromosome clone = new Chromosome();        //New Object
        
        clone.setNumOfGeneration( this.getNumOfGeneration() );
        
        clone.setHillClimbed( this.isHillClimbed() );
        
        clone.setFitness( this.getFitness() );      //set the fitness value
        
        clone.setDemandPenalty( this.getDemandPenalty() );
        
        clone.setUpDownPenalty( this.getUpDownPenalty() );
        
        int size = this.genes.size();               //Take the size of genes
        
        for(int i = 0; i < size; i++)
        {
            Gene cloneGene = (Gene) this.genes.get(i).clone();     //get each gene and clone them
            
            clone.genes.add(cloneGene);             //add each gene to the list of new object which is a clone
            
        }
        
        return clone;
    }

    public String toString() 
    {
        StringBuffer result = new StringBuffer("Chromosome: " + "\n");
        
        result.append("numOfGeneration: " + this.numOfGeneration + "\n");
        result.append("fitness: " + this.fitness + "\n");
        int size = this.genes.size();
       
        result.append("[ ");
        for(int i = 0; i < size - 1 ; i++)
        {
            Gene gene = this.genes.get(i);
            
            result.append(gene.toString() + ", ");
            
        }
        
        result.append(this.genes.get(size - 1 ).toString());
        
        result.append(" ]");
        
        return result.toString();
    }

    public double getFitness()
    {
        return fitness;
    }

    public void setFitness(double fitness)
    {
        this.fitness = fitness;
    }
    
    public Chromosome hamming(final Chromosome opp2, final double F)
    {    
        Chromosome chromosome = new Chromosome();       //Create new Chromosome for result
        
        int size = genes.size();                    
        
        for(int i = 0; i < size ; i++)
        {
            Gene gene = (Gene)this.genes.get(i).clone();        //Clone the next gene 
            
            int value; 
            if( this.genes.get(i).getValue() != opp2.genes.get(i).getValue() ) //substract the value of genes    
            
                value = 1;
                
            else
                value = 0;
            
            gene.setValue(value);           //set the value of the new gene

            
            chromosome.genes.add(gene);     //add the gene to the list and go to the next gene in the chromosome
        }
        
        return chromosome;
    }
    
         /**
     * Multiply every gene of Chromosome with the given double typed multiplier 
     */
    public Chromosome multiplyMod2(final double multiplier)
    {
        Chromosome result = new Chromosome();
        
        int size = this.genes.size();
        
        for(int i = 0; i < size; i++)
        {
            Gene gene = (Gene)this.genes.get(i).clone();
            gene.setValue( gene.getValue() * multiplier );
            
            if( gene.getValue() < 0.5 || gene.getValue() > -0.5)
                
                gene.setValue(0);
            
            else
                
                gene.setValue(1);
            
            result.genes.add(gene);
        }
        
        return result;
    }

    public int getNumOfGeneration() {
        return numOfGeneration;
    }

    public void setNumOfGeneration(int numOfGeneration) {
        this.numOfGeneration = numOfGeneration;
    }

    public boolean isHillClimbed() {
        return hillClimbed;
    }

    public void setHillClimbed(boolean hillClimbed) {
        this.hillClimbed = hillClimbed;
    }
    
    public boolean isFeasible()
    {
        if( Math.round( this.demandPenalty + this.upDownPenalty ) == 0.0 )
            
            return true;
        
        else
            
            return false;
    }
    
    public int compareTo(Object o)
    {
        int result = (int)(this.fitness - ((Chromosome)o).fitness);
        
        return result;
    }

    public double getDemandPenalty() {
        return demandPenalty;
    }

    public void setDemandPenalty(double demandPenalty) {
        this.demandPenalty = demandPenalty;
    }

    public double getUpDownPenalty() {
        return upDownPenalty;
    }

    public void setUpDownPenalty(double upDownPenalty) {
        this.upDownPenalty = upDownPenalty;
    }
    
}
