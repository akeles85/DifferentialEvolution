/*
 * DE.java
 *
 * Created on 16 �ubat 2007 Cuma, 18:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.evo.de.algorithm;

import com.evo.de.*;
import com.evo.de.GUI.ProgressGUI;
import com.evo.de.problem.Problem;
import com.evo.de.util.RandomGenerator;
import com.evo.de.util.RandomGeneratorType;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author ali.keles
 */
public class DE implements Algorithm{
    
    private ArrayList<Chromosome> population;
    
    public static double F = 0.2 ;            
    
    public static double LAMBDA = 1;       //Used in some mutation operators.
    
    public static double CR = 0.3 ;          
    
    public static int populationSize = 100;
    
    public static int MAX_ITERATION = 400;
    
    private boolean useHillClimbing;
    
    private MutationType mutationType;
    
    /**
     *The problem that will be solved with DE
     */
    private Problem problem;
    
    private ProgressGUI progress;
    
    private double fixRatio = 0.3;
    
    private DE(){
        
    }
    
    /** Creates a new instance of DE */
    public DE(Problem problem, MutationType mutationType, final boolean useHillClimbing, ProgressGUI progress) throws Exception
    {
        
        if(populationSize < 4 )
            throw new Exception("Population size must be greater than 3");
        
        this.problem = problem;        
        
        this.mutationType = mutationType;
        
        this.useHillClimbing = useHillClimbing;
        
        population = new ArrayList();
        
        this.createPopulation();        //an imporant step of the constructor
        
        this.progress = progress;
    }
    
    /**
     * Starts the algorithm and returns the results
     *
     */
    public Result start() throws Exception
    {
        
        Result algoResults = new Result();
        
        this.initialization( RandomGeneratorType.BINARY );
        
        Chromosome targetVector;
        Chromosome donarVector;
        Chromosome trialVector;
        Chromosome nextGeneration;
        
        int iterNum = 0;
        boolean finished = false;
        
        Chromosome bestResult = (Chromosome)this.population.get(0).clone();
        
        ArrayList<Chromosome> nextPopulation = new ArrayList<Chromosome>();
        
        ArrayList<Chromosome> fixedPopulation = new ArrayList<Chromosome>();
        
        while(iterNum < this.MAX_ITERATION && !finished)
        {   
            if( iterNum != 0 )
                
                this.population = nextPopulation;
            
            nextPopulation = new ArrayList<Chromosome>();
            
            
            Chromosome iterationResult = (Chromosome)this.population.get(0);
           
            for(int i = 0; i < this.populationSize && !finished; i++)            //This will be one step for algorithm, one generation will be generated
            {
                targetVector = this.population.get(i);

                try
                {
                    donarVector = this.mutation(i, this.mutationType );
                }
                catch(Exception e)
                {
                    throw e;
                }

                trialVector = this.recombination(targetVector, donarVector);

                //Fitness values must be calculate before selection
                //Because selection operation will be decided according to fitness values
                //Fitness function will be with hill climbing
                trialVector.setNumOfGeneration( iterNum );
                trialVector.setFitness( this.problem.fitness(trialVector) );    //fitness values are set
                
                //NextGeneration will be targetVector or trialVector, so there is no need to calculate the fitness again
                nextGeneration = this.selection(targetVector, trialVector);
                
                nextPopulation.add(nextGeneration);
                
             //   if( RandomGenerator.genDouble() <= this.fixRatio )
                    
              //     this.problem.fixOperator( nextGeneration );
                
                iterationResult = this.chooseBetterSolution( iterationResult,nextGeneration );      
                
                if(this.problem.satisfyFunction( nextGeneration.getFitness() ))
                {
                    finished = true;
                    bestResult = nextGeneration;
                }
                
                
            }
            
            
            if( useHillClimbing )
            {
                iterationResult.setFitness( this.problem.hillClimbing(iterationResult, false));
            }
            
          //  Chromosome fixedChromosome;
          //  fixedChromosome = this.problem.fixOperator( (Chromosome)iterationResult.clone() );    
            
         //   fixedPopulation.add( fixedChromosome );
            
            this.problem.fixOperator( iterationResult );
            fixedPopulation.add( iterationResult );
            
            algoResults.addResult( iterationResult );
            

            
            iterNum ++ ;
            
            progress.increase();
            
        }
        
        System.out.println("best");
        
        Collections.sort( fixedPopulation );
        
        int size = fixedPopulation.size();
        System.out.println("size: " + size);
        for( int i = 0; i < size; i++)
        {
            bestResult = fixedPopulation.get( i );
            
            if( bestResult.isFeasible() )
            {
                System.out.println("i: "+ i);
                
                System.out.println("fitness: " + bestResult.getFitness() );
                
                System.out.println("penalty: " + bestResult.getDemandPenalty());
                
                System.out.println("penalty: " + bestResult.getUpDownPenalty());
                
                break;
            }
        }
        
        if( useHillClimbing )
        {
            bestResult.setFitness( this.problem.hillClimbing(bestResult ,true ));
        }
        
        System.out.println("UpPenalty" + bestResult.getUpDownPenalty() );
        System.out.println("CostPenalty " + bestResult.getDemandPenalty());
        algoResults.setFinalResult( bestResult );
        
        algoResults.setNumOfIteration( iterNum );
        
        return algoResults;   
    }
    
    /**
     * Give random values to genes to init
     */
    public void initialization(RandomGeneratorType type)
    {
            for(int i = 0 ; i < this.populationSize ; i++)
            {
                Chromosome chromosome = this.population.get(i);
                
                chromosome.initialize(this.populationSize,i,type);
                
                chromosome.setFitness( this.problem.fitness(chromosome) );  //set fitness values
            }
    }
    
    /**
     *Creates chromosomes for the number of population size
     *Each chromosome must take the genes from the root chromosome from the problem domain
     *Because this root chromosome has all the  parameters that are used in the problem.
     */
    private void createPopulation()
    {
        for(int i = 0; i < this.populationSize; i++)
        {
            Chromosome chromosome = (Chromosome)problem.getRootChromosome().clone();
            
            this.population.add(chromosome);                // add it to population
        }
        
    }
    
    /*
     *@return donar vector
     *
     */
    public Chromosome mutation(final int chromosomeIndex,MutationType type)throws Exception
    {    
        int[] xVectors ;
        int[] forbiddenIndexes;
        Chromosome donorVector = new Chromosome() ;
        Chromosome targetVector = (Chromosome)this.population.get( chromosomeIndex ).clone();
        Chromosome bestVector;
        Integer indexOfBest = new Integer(0);
        
        ////v = x1 + F (x2 - x3)
        if(type == MutationType.RAND_1)
        {
            //Set the forbidden indexes which cannot be index of randomly chosen vectors
            forbiddenIndexes = new int[1];
            forbiddenIndexes[ 0 ] = chromosomeIndex;
            
            //Get the 3 distinct vector from population
            xVectors = DEApp.chooseDistinct( this.population, 3, forbiddenIndexes ) ;

            Chromosome x1 = this.population.get( xVectors[0] );
            Chromosome x2 = this.population.get( xVectors[1] );
            Chromosome x3 = this.population.get( xVectors[2] );
            

            //v = x1 + F (x2 - x3)

            donorVector = x2.substract( x3 ) ;
            donorVector = donorVector.multiply( F ) ;
            donorVector = donorVector.addition( x1 ) ;
        }
        // v = x1 + F( x2 + x3 - x4 - x5)
        else if( type == MutationType.RAND_2)
        {
            forbiddenIndexes = new int[1];
            forbiddenIndexes[ 0 ] = chromosomeIndex;
            
            //Get the 5 distinct vector from population
            xVectors = DEApp.chooseDistinct( this.population, 5, forbiddenIndexes ) ;
            
            Chromosome x1 = this.population.get( xVectors[0] );
            Chromosome x2 = this.population.get( xVectors[1] );
            Chromosome x3 = this.population.get( xVectors[2] );
            Chromosome x4 = this.population.get( xVectors[3] );
            Chromosome x5 = this.population.get( xVectors[4] );
            
            donorVector = x2.addition( x3 );            //  = x2 + x3
            donorVector = donorVector.substract(x4);   //  = x2 + x3 - x4
            donorVector = donorVector.substract(x5);   // = x2 + x3 - x4 - x5
            donorVector = donorVector.multiply( F );   // F * (x2 + x3 - x4 - x5)
            donorVector = donorVector.addition(x1);    // x1 + F * (x2 + x3 - x4 - x5)
            
        }
        
        //vi,G+1= xbest,G + F(xr1,G – xr2,G)
        //best, r1, r2 and i is different 
        else if( type == MutationType.BEST_1 )
        {
            bestVector = this.getBestChromosome( indexOfBest ); //choose the best individaul in population, indexOfBest is the other return value of function
            
            forbiddenIndexes = new int[2];
            forbiddenIndexes[ 0 ] = chromosomeIndex;
            forbiddenIndexes[ 1 ] = indexOfBest.intValue();
            
             //Get the 2 distinct vector from population
            xVectors = DEApp.chooseDistinct( this.population, 2, forbiddenIndexes ) ;

            Chromosome x1 = this.population.get( xVectors[0] );
            Chromosome x2 = this.population.get( xVectors[1] );

            donorVector = x1.substract( x2 ) ;                  // = x1 - x2
            donorVector = donorVector.multiply( F ) ;           // F*( x1 - x2)
            donorVector = donorVector.addition( bestVector ) ;  // xbest + F*( x1 - x2)
            
        }
        
        // v = xbest + F( x2 + x3 - x4 - x5)
        else if( type == MutationType.BEST_2)
        {
            bestVector = this.getBestChromosome( indexOfBest );
            
            forbiddenIndexes = new int[2];
            forbiddenIndexes[ 0 ] = chromosomeIndex;
            forbiddenIndexes[ 1 ] = indexOfBest.intValue();
            
            //Get the 5 distinct vector from population
            xVectors = DEApp.chooseDistinct( this.population, 4, forbiddenIndexes ) ;
            
            Chromosome x2 = this.population.get( xVectors[0] );
            Chromosome x3 = this.population.get( xVectors[1] );
            Chromosome x4 = this.population.get( xVectors[2] );
            Chromosome x5 = this.population.get( xVectors[3] );
            
            donorVector = x2.addition( x3 );            //  = x2 + x3
            donorVector = donorVector.substract( x4 );   //  = x2 + x3 - x4
            donorVector = donorVector.substract( x5 );   // = x2 + x3 - x4 - x5
            donorVector = donorVector.multiply( F );   // F * (x2 + x3 - x4 - x5)
            donorVector = donorVector.addition( bestVector );    // xbest + F * (x2 + x3 - x4 - x5)
            
        }
        
        // v = x1 + lambda( xbest - x1 ) + F( x2 - x3 )
        else if( type == MutationType.RAND_TO_BEST_1 )
        {
            bestVector = this.getBestChromosome( indexOfBest );
            
            forbiddenIndexes = new int[2];
            forbiddenIndexes[ 0 ] = chromosomeIndex;
            forbiddenIndexes[ 1 ] = indexOfBest.intValue();
            
            //Get the 3 distinct vector from population
            xVectors = DEApp.chooseDistinct( this.population, 3, forbiddenIndexes ) ;

            Chromosome x1 = this.population.get( xVectors[0] );
            Chromosome x2 = this.population.get( xVectors[1] );
            Chromosome x3 = this.population.get( xVectors[2] );
            Chromosome term1, term2;
            
            
            //term1 = lambda * ( xbest - x1 )
            term1 = bestVector.substract( x1 );
            term1 = term1.multiply( this.LAMBDA );

            //term2 =  F (x2 - x3)
            term2 = x2.substract( x3 );
            term2 = term2.multiply( F );
            
            donorVector = term1.addition( term2 );
            
            donorVector = donorVector.addition( x1 ) ;
            
        }
        
        // v = xi + lambda(x1 - xi ) + F(x2 - x3)
        else if( type == MutationType.CURRENT_TO_RAND_1 )
        {   
            forbiddenIndexes = new int[1];
            forbiddenIndexes[ 0 ] = chromosomeIndex;
            
            //Get the 3 distinct vector from population
            xVectors = DEApp.chooseDistinct( this.population, 3, forbiddenIndexes ) ;

            Chromosome x1 = this.population.get( xVectors[0] );
            Chromosome x2 = this.population.get( xVectors[1] );
            Chromosome x3 = this.population.get( xVectors[2] );
            Chromosome term1, term2;
            
            
            //term1 = lambda * (x1 - xi )
            term1 = x1.substract( targetVector );
            term1 = term1.multiply( this.LAMBDA );

            //term2 =  F (x2 - x3)
            term2 = x2.substract( x3 );
            term2 = term2.multiply( F );
            
            donorVector = term1.addition( term2 );
            
            donorVector = donorVector.addition( targetVector ) ;
            
        }
        
        // v = xi + lambda(xb - xi ) + F(x1 - x2)
        else if( type == MutationType.CURRENT_TO_BEST_1 )
        {   
            bestVector = this.getBestChromosome( indexOfBest );
            
            forbiddenIndexes = new int[2];
            forbiddenIndexes[ 0 ] = chromosomeIndex;
            forbiddenIndexes[ 1 ] = indexOfBest.intValue();
            
            //Get the 2 distinct vector from population
            xVectors = DEApp.chooseDistinct( this.population, 2, forbiddenIndexes ) ;

            Chromosome x1 = this.population.get( xVectors[0] );
            Chromosome x2 = this.population.get( xVectors[1] );
            Chromosome term1, term2;
            
            
            //term1 = lambda(xb - xi )
            term1 = bestVector.substract( targetVector );
            term1 = term1.multiply( this.LAMBDA );

            //term2 =  F(x1 - x2)
            term2 = x1.substract( x2 );
            term2 = term2.multiply( F );
            
            donorVector = term1.addition( term2 );
            
            donorVector = donorVector.addition( targetVector ) ;
            
        }
        else if(type == mutationType.BINARY_RAND_1){
            //Set the forbidden indexes which cannot be index of randomly chosen vectors
            forbiddenIndexes = new int[1];
            forbiddenIndexes[ 0 ] = chromosomeIndex;
            
            //Get the 3 distinct vector from population
            xVectors = DEApp.chooseDistinct( this.population, 3, forbiddenIndexes ) ;

            Chromosome x1 = this.population.get( xVectors[0] );
            Chromosome x2 = this.population.get( xVectors[1] );
            Chromosome x3 = this.population.get( xVectors[2] );
            

            //v = x1 + F (x2 - x3)

            donorVector = x2.hamming( x3, F ) ;
            donorVector = donorVector.multiplyMod2( F ) ;
            donorVector = donorVector.hamming( x1, F ) ;
        
        }
                // v = x1 + F( x2 + x3 - x4 - x5)
        else if( type == MutationType.BINARY_RAND_2)
        {
            forbiddenIndexes = new int[1];
            forbiddenIndexes[ 0 ] = chromosomeIndex;
            
            //Get the 5 distinct vector from population
            xVectors = DEApp.chooseDistinct( this.population, 5, forbiddenIndexes ) ;
            
            Chromosome x1 = this.population.get( xVectors[0] );
            Chromosome x2 = this.population.get( xVectors[1] );
            Chromosome x3 = this.population.get( xVectors[2] );
            Chromosome x4 = this.population.get( xVectors[3] );
            Chromosome x5 = this.population.get( xVectors[4] );
            
            donorVector = x2.hamming( x3, F );            //  = x2 + x3
            donorVector = donorVector.hamming(x4, F);   //  = x2 + x3 - x4
            donorVector = donorVector.hamming(x5, F);   // = x2 + x3 - x4 - x5
            donorVector = donorVector.multiplyMod2( F );   // F * (x2 + x3 - x4 - x5)
            donorVector = donorVector.hamming(x1, F);    // x1 + F * (x2 + x3 - x4 - x5)
            
        }
        
        else if(type == mutationType.BINARY_BEST_2)
        {
            bestVector = this.getBestChromosome( indexOfBest );
            
            forbiddenIndexes = new int[2];
            forbiddenIndexes[ 0 ] = chromosomeIndex;
            forbiddenIndexes[ 1 ] = indexOfBest.intValue();
            
            //Get the 5 distinct vector from population
            xVectors = DEApp.chooseDistinct( this.population, 4, forbiddenIndexes ) ;
            
            Chromosome x2 = this.population.get( xVectors[0] );
            Chromosome x3 = this.population.get( xVectors[1] );
            Chromosome x4 = this.population.get( xVectors[2] );
            Chromosome x5 = this.population.get( xVectors[3] );
            
            donorVector = x2.hamming( x3,F );            //  = x2 + x3
            donorVector = donorVector.hamming( x4, F );   //  = x2 + x3 - x4
            donorVector = donorVector.hamming( x5, F );   // = x2 + x3 - x4 - x5
            donorVector = donorVector.multiplyMod2( F );   // F * (x2 + x3 - x4 - x5)
            donorVector = donorVector.hamming( bestVector, F );    // xbest + F * (x2 + x3 - x4 - x5)
            
            
        }
        
                //vi,G+1= xbest,G + F(xr1,G – xr2,G)
        //best, r1, r2 and i is different 
        else if( type == mutationType.BINARY_BEST_1 )
        {
            bestVector = this.getBestChromosome( indexOfBest ); //choose the best individaul in population, indexOfBest is the other return value of function
            
            forbiddenIndexes = new int[2];
            forbiddenIndexes[ 0 ] = chromosomeIndex;
            forbiddenIndexes[ 1 ] = indexOfBest.intValue();
            
             //Get the 2 distinct vector from population
            xVectors = DEApp.chooseDistinct( this.population, 2, forbiddenIndexes ) ;

            Chromosome x1 = this.population.get( xVectors[0] );
            Chromosome x2 = this.population.get( xVectors[1] );

            donorVector = x1.hamming( x2, F ) ;                  // = x1 - x2
            donorVector = donorVector.multiplyMod2( F ) ;           // F*( x1 - x2)
            donorVector = donorVector.hamming( bestVector, F ) ;  // xbest + F*( x1 - x2)
            
        }

        
        DEApp.mirroring(donorVector);      //if some parameters are out of bounds, they will be minored
        

        return donorVector;
    }
    
    /**
     * Recombination part of the Differential Evolution algortihm
     * Takes the targetVector x and donar vector v as parameters
     * Recombine the genes of donar vector and target vector according to random generator
     * Then adds the chosen genes to trial vector
     *@returns trial vector u
     */
    public Chromosome recombination(final Chromosome targetVector,final Chromosome donarVector)
    {
        int Irand ;
        double rand ;
        Chromosome trialVector = new Chromosome();
        
        int numOfGenes = this.problem.getRootChromosome().getNumOfGenes();
        
        //Do recombination for each gene in chromosemes
        for(int j = 0; j < numOfGenes ; j++)
        {            
            //Generate random numbers
            Irand = RandomGenerator.genInt(numOfGenes) + 1;  // 1,2, ... , D
            rand = RandomGenerator.genDouble();                         // 0, ... ,1
        
            if(rand <= this.CR || Irand == j)       //if randj,i <= CR or Irand = j
            {
                Gene gene = (Gene)donarVector.getGene(j).clone();
                
                trialVector.insertGene(gene);
            }
            else                                   //if randj,i > CR and Irand != j
            {
                Gene gene = (Gene)targetVector.getGene(j).clone();
                
                trialVector.insertGene(gene);
            }
            
        }
        
        return trialVector;
    }
    
    /*
     * Returns the new generation of the target vector
     *
     */
    public Chromosome selection(final Chromosome targetVector, final Chromosome trialVector)
    {
        Chromosome nextGeneration = new Chromosome();
        
        //if f( u_i_g+1 ) <= f(x_i_g) -> nextGeneration = u_i_g+1
        if( trialVector.getFitness() <= targetVector.getFitness() )          // <=
        {
            nextGeneration = (Chromosome)trialVector.clone();
        }
        //otherwise -> nextGeneration = x_i_g
        else
        {
            nextGeneration = (Chromosome)targetVector.clone();
        }
        
        return nextGeneration;
    }
    
    public Chromosome chooseBetterSolution(final Chromosome opp1, final Chromosome opp2)
    {
        if( opp1.getFitness() <= opp2.getFitness() ) 
        {
            return (Chromosome)opp1;
        }
        else
        {
            return (Chromosome)opp2;
        }
    }
    
    
    /**
     indexOfBestChromosme: the index of best chromosome which is returned
     */
    public Chromosome getBestChromosome(Integer indexOfBestChromosme)
    {
        int bestIndex = 0;
        
        double bestFitness = Double.MAX_VALUE;
        
        for( int i = 0; i < this.populationSize; i++)
        {
            Chromosome chromosome = this.population.get( i );
            
            double currentFitness = chromosome.getFitness();
            
            if( currentFitness < bestFitness )
            {
                bestIndex = i ;
                
                bestFitness = currentFitness;
            }
        }
        
        indexOfBestChromosme = new Integer( bestIndex );
        
        return ( Chromosome ) this.population.get( bestIndex ).clone();
    }
    
   
}
