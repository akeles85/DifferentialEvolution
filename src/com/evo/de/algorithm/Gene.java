package com.evo.de.algorithm;
/*
 * Gene.java
 *
 * Created on 16 Subat 2007 Cuma, 19:01
 */

import com.evo.de.util.RandomGenerator;
import com.evo.de.util.RandomGeneratorType;

/**
 * This class will hold the parameters for differential evolution algorithm
 * Each parameter has its own value, minBound and maxBound
 * @author ali.keles
 */
public class Gene implements Cloneable{
    
    private double value;
    
    private double maxBound;
    
    private double minBound;
    
    /** 
     * This constructor creates a gene for chromosome. 
     *Firstly, it will generate a gene which has a minumum bound at -5.2
     *and a maximum bound at 5.2. The default value of gene is equal to 0. 
     */
    public Gene()
    {    
        value = 0;
        maxBound = 5.2;
        minBound = -5.2;
    }
    
    /**
     * Sets the maximum and minumum bounds of the value that gene can take
     * If maxBound is smaller or equal to minBound, an exception will be thrown
     *
     */
    public void setBounds(final double minBound, final double maxBound) throws Exception
    {
        if(minBound >= maxBound)
        {
            throw new Exception("The bound values are not valid!");
        }
        this.minBound = minBound;
        this.maxBound = maxBound;
    }
    
    /**
     * Checks if a gene is at the bounds
     *
     */
    public boolean isAtBounds()
    {    
        if(this.value >= this.getMinBound() && this.value <= this.getMaxBound())
            return true;
        
        else
            return false;
    }
    
    //Initialize the gene's value according to max and min bounds
    public void init(final int numOfPopulation, final int numOfParam, final int indexOfParam,RandomGeneratorType type)
    {
        if( type == RandomGeneratorType.RAND)
            
            this.value = RandomGenerator.genRealNum(this.getMinBound(), this.getMaxBound());
        
        else if( type == RandomGeneratorType.HALTEN)
        {
            RandomGenerator generator = new RandomGenerator();
        
            //[0,1)
            double halten = generator.halten(numOfPopulation * numOfParam , numOfParam ,indexOfParam );                

            // value = halten * ( max - min ) + min
            this.value = ( halten * ( this.getMaxBound() - this.getMinBound() ) ) + this.getMinBound();
        }
        
        else if(type == RandomGeneratorType.BINARY)
        {
            if( RandomGenerator.genInt(2) == 0 )
                
                this.value = 0;
            
            else
                
                this.value = 1;
            
        }
    }
    
    /**
     *Returns true if two object is equal to each other
     *Also hashcode is overrided
     *
     */
    public boolean equals(Object object){
        
        if(this == object)
            return true;
        
        if(object == null || this.getClass() != object.getClass())  //avoid doing (object instanceof Gene) Because it will cause to error in inheritence
            return false;
        
        Gene copyObject = (Gene)object;
        
        return (this.value == copyObject.value && this.getMinBound() == copyObject.getMinBound() && this.getMaxBound() == copyObject.getMaxBound());
    }
    
    /**
     *If new elements are added to class Gene, this function should be updated!
     */
    public int hashCode()
    {    
        int hash = 3;
        hash = 31 * hash + (int)this.value;
        hash = 31 * hash + (int)this.getMinBound();
        hash = 31 * hash + (int)this.getMaxBound();
        return hash;
    }
    
    /**
     *Copies the contents of the object into another object
     *
     */
    protected Object clone() {
        Gene cloneGene = new Gene();
        cloneGene.value = this.value;
        cloneGene.minBound = this.getMinBound();
        cloneGene.maxBound = this.getMaxBound();
        return cloneGene;
    }

    public double getValue() {
        return value;
    }
    
    public void setValue(double value) 
    {
        this.value = value;
    }

    public String toString()
    {
        String result = String.valueOf(this.value);
        
        return result;
    }

    public double getMaxBound() {
        return maxBound;
    }

    public double getMinBound() {
        return minBound;
    }
    
}
