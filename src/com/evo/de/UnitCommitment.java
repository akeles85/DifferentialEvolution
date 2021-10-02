/*
 * UnitCommitment.java
 *
 * Created on 19 Mart 2007 Pazartesi, 18:56
 *
 */

package com.evo.de;

import com.evo.de.GUI.*;
import com.evo.de.algorithm.*;
import com.evo.de.problem.*;
import com.evo.de.util.DEException;
import com.evo.ecd.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import static java.lang.Math.*;

/**
 *
 * @author Ali Keles
 * This class is designed to represent the problem of the Unit Commitment
 * It uses DE algorithm to find out a solutiın
 */
public class UnitCommitment extends Problem implements BinarySpace, Runnable{
    
    /**
     *Holds the data of power generating units
     *This generators are made as a single matrix to save memory space
     *Instead of holding separate matrixes for each iteration, only one matrix is hold for the whole algorithm
     * The only thing that must be changed for each iteration is the status of generators.
     */
    private PowerGenerator generators[][];         
    
    /**
     *Object for solving lambda iteration
     */
    private LambdaIteration lambdaIteration;
    
    /**
     * Number of hour for data set
     */
    private int numOfHour;
    
    /**
     * number of generator in data set
     */
    private int numOfGenerator;
    
    
    private double powerDemand[];
    
    private double powerReserve[];
    
    /** 
     * Constant for penalty 
     */
    private static double M = 200;    //Constant which is used in penalty function
    
    /**
     * Constant for penalty
     */
    private static double K = 50;    //Constant which is used in penalty function
    
    /**
     *Constant for lambda Iteration
     */
    private double TOLERANCE = 0.0001;
    
    /**
     *Mutation type
     */
    private MutationType mutationType = MutationType.BINARY_RAND_1;
    
    private int numOfRun = 1;
    
    /**
     *Progress bar that shows the status of algorithm
     */
    private ProgressGUI progress;
    
    private File file;
    
    
    /** Creates a new instance of Unit Commitment 
     *progress and file comes from GUI
     */
    public UnitCommitment(ProgressGUI progress, File file)
    {
        super();
        
        lambdaIteration = new LambdaIteration();
        
        this.progress = progress;
        
        this.file = file;
    }
    
    /**
     *Initialize parameters
     */
    protected void initParameters()
    {
        try
        {
            for(int i = 0; i < this.numOfHour * this.numOfGenerator; i++)
                
                 this.addParameter(0, 1);   //min and max Bounds
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     *Reads the input file.
     *Input file contains data about power generating units
     *It also includes the hourtly demand and reserve
     */
    private ArrayList<PowerGeneratorData> readFile(File file) throws Exception
    {
        
        ArrayList<PowerGeneratorData> list = new ArrayList();
        
        FileReader f = new FileReader(file);
        
        BufferedReader reader = new BufferedReader(f);
        
        String meta = reader.readLine();  //NUM_OF_GENERATOR * NUM_OF_HOUR
        
        String[] metaData = meta.split("\t");
        
        this.numOfGenerator = Integer.parseInt( metaData[0] );
        
        this.numOfHour = Integer.parseInt( metaData[1] );
        
        generators = new PowerGenerator[numOfHour][numOfGenerator];
        
        powerDemand = new double[numOfHour];
        
        powerReserve = new double[numOfHour];
        
        this.initParameters();
        
        reader.readLine();  //header
        
        int lines = this.numOfGenerator;
        while(true)
        {
            String line = reader.readLine();
            
            if( lines == 0)
                break;
            
            lines--;
            
            PowerGeneratorData generatorData = new PowerGeneratorData();
            
            String column[] = line.split("\t");
            
            generatorData.setName(column[0]);
            
            generatorData.setPmax(Double.valueOf( column[1] ));
            
            generatorData.setPmin(Double.valueOf( column[2] ));
            
            generatorData.setA(Double.valueOf( column[3] ));
            
            generatorData.setB(Double.valueOf( column[4] ));
            
            generatorData.setC(Double.valueOf( column[5] ));
            
            generatorData.setMinOn( Double.valueOf( column[6] ).intValue() );
            
            generatorData.setMinOff( Double.valueOf( column[7] ).intValue() );
            
            generatorData.setFuelCost(Double.valueOf( column[8] ));
            
            generatorData.setHotStartUpCost(Double.valueOf( column[9] ));
            
            generatorData.setColdStartUpCost(Double.valueOf( column[10] ));
            
            generatorData.setHourForColdStart(Double.valueOf( column[11] ).intValue());
            
            generatorData.setInitialState(Integer.valueOf( column[12] ));
            
            
            list.add(generatorData);
            
        }
        
        reader.readLine();  //header 
        String []demands = reader.readLine().split("\t");  //Power Demand
        for(int i = 0; i < this.numOfHour; i++)
        {
            powerDemand[ i ] = Double.valueOf( demands[i] );
            
        }
        
        String []reserves = reader.readLine().split("\t");  //Power Reserve
        for(int i = 0; i < this.numOfHour; i++)
        {
            powerReserve[ i ] = Double.valueOf( reserves[i] );
        }
        
        for(int i = 0 ; i < this.numOfHour; i++)
        {
            System.out.println("d: " + powerDemand[i] );
            System.out.println("r: " + powerReserve[i] );
        }
        
        f.close();
                
        return list;
    }
       
    /**
     *Objective is minimizing, smaller fitness function is better
     */
    public double fitness(final Chromosome chromosome) 
    {      
        /**
         * Set the generators status according to chromosome
         */
        for(int i = 0; i < this.numOfHour; i++)
        {   
            int j = 0;
            //set the status of generators
            for( PowerGenerator p : this.generators[i] )
            {
                if( chromosome.getGene( i * this.numOfGenerator + j ).getValue() == 0 )
                {
                    p.setOn(false);
                }
                else
                {
                    p.setOn(true);
                }
                
                j++;
            }
        }
        
        /**
         * get the costs according to status of generators
         */
        double startUp = this.startUpCost() ;
        double fuelCost = this.totalFuelCost();
        
        /**
         *calculate penalty terms
         */
        int numOfGeneration = chromosome.getNumOfGeneration();
        double demandPenalty = this.demandPenalty( numOfGeneration );
        double upDownPenalty = this.upDownTimePenalty( numOfGeneration );
        double penalty = demandPenalty + upDownPenalty;
        
        /**
         *sets the calculated values to chromosome
         */
        chromosome.setDemandPenalty( demandPenalty );
        chromosome.setUpDownPenalty( upDownPenalty );

        double fitness =  fuelCost + startUp + penalty;
        chromosome.setFitness(fitness);
   
        return fitness;
    }  
    
    
    public double hillClimbing( Chromosome chromosome , final boolean fixed) 
    {         
        double bestFitness = Double.MAX_VALUE;
        double currentFitness = 0;
        double oldFitness = chromosome.getFitness();
        
        int index_i = 0;
        int index_j = 0;
        for( int i = 0; i < this.numOfHour; i++ )
        {
            for(int j = 0; j < this.numOfGenerator; j++)
            {
                //Find the next neighbour by changing the j.th generator status
                double value = chromosome.getGene( i * this.numOfGenerator + j ).getValue();
                
                double newValue = value == 1 ? 0 : 1;
                chromosome.getGene( i * this.numOfGenerator + j ).setValue( newValue );
                
                
                //Calculate fitness
                if( fixed ){
                    
                    Chromosome newChromosome = (Chromosome) chromosome.clone();
                    this.fixOperator( newChromosome );

                    if( newChromosome.isFeasible() ){
                        System.out.println("b");
                        currentFitness =  newChromosome.getFitness();
                    }
                    else
                        currentFitness = Double.MAX_VALUE;
                }
                
                else
                    currentFitness = this.fitness( chromosome );
                 

                if( bestFitness > currentFitness){

                    bestFitness = currentFitness;
                    
                    index_i = i ;
                    
                    index_j = j ;
                    
                }

                //Return to original solution
                chromosome.getGene( i * this.numOfGenerator + j ).setValue( value );
                
            }
        }
        //System.out.println("-------------");
        
        if( oldFitness > bestFitness )
        {
            double value = chromosome.getGene( index_i * this.numOfGenerator +  index_j ).getValue();

            if( value == 0)

               chromosome.getGene( index_i * this.numOfGenerator +  index_j ).setValue( 1 );

            else

                chromosome.getGene( index_i * this.numOfGenerator +  index_j ).setValue( 0 );

            if( fixed )
            {
                this.fixOperator(chromosome);
                this.hillClimbing( chromosome, true );
            }
        }
        
    //    System.out.println("newFitness: " + this.fitness( chromosome ));
        
        return this.fitness(chromosome);
    }  
    
    /**
     *There is no satisfying status, so it returns false
     *This method is designed if the algorithm has a satisfying status
     */
    public boolean satisfyFunction(double fitness)
    {
         return false;
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
     *Returns the bitString in chromosome
     */
    public char[] toBitString(final Chromosome chromosome)
    {
        char []bitString;
        
        bitString = new char[this.numOfGenerator * this.numOfHour];
        
        for(int i = 0; i < this.numOfGenerator * this.numOfHour; i++)
        {
            int value = (int)chromosome.getGene(i).getValue();
            
            bitString[i] = String.valueOf( value ).charAt(0);
            
        }
        return bitString;
    }
    
    
    /**
     * Calculates the total Fuel Cost with lambda Iteration
     */
    public double totalFuelCost()
    {
        double totalFuelCost = 0;
        
        try{
            //Calculate ECD for each hour
            for(int i = 0; i < this.numOfHour; i++)
            {   
                this.lambdaIteration.calcECD(this.generators[i], this.powerDemand[i],this.TOLERANCE);
                
                totalFuelCost += this.lambdaIteration.getTotalCost();
            }
             
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return totalFuelCost;
        
    }
    
    
    /**
     *Calculates start-up costs
     *
     */
    public double startUpCost()
    {
        double totalStartUpCost = 0;
        
        /**
        *If the initial state has a minus sign, it means that the generator is closed for the previous n hour
        *If it has a positive sign, it means that it is open for the previous n hour
        */
        for(int genNum = 0; genNum < this.numOfGenerator; genNum++)
        {
            PowerGeneratorData generatorData = this.generators[ 0 ][ genNum ].getGeneratorData();
            
            int initialState = generatorData.getInitialState();
            
            if( initialState < 0)
                
                this.generators[ 0 ][ genNum ].setStopDuration( -1 * initialState );
            
            else
                
                this.generators[ 0 ][ genNum ].setStopDuration(0);
            
            totalStartUpCost += this.generators[ 0 ][ genNum ].startUpCost();
        }
 
        //Calculate startUp for each hour and each generator
        for(int hour = 1; hour < this.numOfHour; hour++)        //first hour will have a cost 0. So it wont be calculated
        {   
            //set the status of generators
            for(int genNum = 0; genNum < this.numOfGenerator; genNum++)
            {
                //If generator is open at the previous hour, the stopDuration of the 
                //generator at the current time will be zero
                if( this.generators[ hour - 1][ genNum ].isOn() )
                {
                    generators[hour][genNum].setStopDuration( 0 );
                }
                //If the generator is closed at the previous hour,
                //the number of hour that it stayed closed, will be incremented by one
                else
                {
                    int stopDuration = generators[ hour - 1][ genNum ].getStopDuration() + 1;

                    generators[ hour ][ genNum ].setStopDuration(stopDuration);
                }

                //After the stopDuraions are set, startUp costs can be calculated
                totalStartUpCost += this.generators[ hour ][ genNum ].startUpCost();
            }
        }

        return totalStartUpCost;
    }
    
    
    /**
     *Calculates the penalty for demand
     *Penalty term occurs,
     *If the total Pmax of the open generators is smaller than total(demand + reserve)
     *or the total Pout of the open generators is smaller than total( demand )
     *
     */
    public double demandPenalty(int numOfGeneration)
    {
        double cost = 0;
        
        double term1 = 0,term2 = 0;
        
        for( int t = 0; t < this.numOfHour; t++ )
        {
            // max ( 0 , u(t) + r(t) - sumation(Pmax(i) * v(i)) )
            term1 += max( 0, ( this.powerDemand[ t ] + this.powerReserve[ t ] - PowerGenerator.totalOnPmax( this.generators[t] ) ) ) ;
            
            // max ( 0 , u(t) - generatedPower)
            term2 += max( 0, this.powerDemand[ t ] - PowerGenerator.totalOnPout( this.generators[t] ) );
        }
        
        cost = this.M * ( term1 + term2 );
             
        return cost;
        
    }
    
    /**
     *Calculates the penalty for t_up and t_down 
     */
    public double upDownTimePenalty(int numOfGeneration)
    {
        int totalViolated = 0;    
            
        for( int unit = 0; unit < this.numOfGenerator; unit++)
        {
            boolean []statusOfGenerator = new boolean[this.numOfHour];
            for(int hour = 0; hour < this.numOfHour; hour++)
            {
                statusOfGenerator[ hour ] = this.generators[ hour ][ unit ].isOn();
            }
            
            totalViolated += this.generators[ 0 ][ unit ].violetedNumber( statusOfGenerator );
        }
        
        // k * n 
         double result = this.K * (double)totalViolated ;

        return result;
    }
    
    
    /**
     *Fixes a chromosome according to penalty terms of chromosome
     *
     */
    public Chromosome fixOperator(Chromosome chromosome )
    {
        for(int i = 0; i < this.numOfHour; i++)
        {   
            int j = 0;
            //set the status of generators
            for( PowerGenerator p : this.generators[i] )
            {
                if( chromosome.getGene( i * this.numOfGenerator + j ).getValue() == 0 )
                {
                    p.setOn(false);
                }
                else
                {
                    p.setOn(true);
                }
                
                j++;
            }
        }
        
        /**
         *If the updown penalty is bigger than demand penalty, fix the upDown constraint
         */
        if( chromosome.getUpDownPenalty() > chromosome.getDemandPenalty())
        {
            this.fixTUpDown( chromosome );
            this.fitness( chromosome );         //after fixing the solution, calculate the fitness
            return chromosome;
        }
        /**
         *If not, dont fix anything
         */
        else
        {
            return chromosome;  
        }
    }
    
    
    /**
     *Fixes the solution not to violate the constraint of tup and tdown
     */
    public Chromosome fixTUpDown( final Chromosome chromosome)
    {  
        /**
         * If penalty is small enough dont calculate
         */
        if( Math.round( chromosome.getUpDownPenalty() ) == 0 )
            return chromosome;

        for( int unit = 0; unit < this.numOfGenerator; unit++)
        {
            boolean []statusOfGenerator = new boolean[this.numOfHour];
            for(int hour = 0; hour < this.numOfHour; hour++)
            {
                statusOfGenerator[ hour ] = this.generators[ hour ][ unit ].isOn();
            }
            
            /**
             *Fix the solution for a generator
             */
            statusOfGenerator = this.generators[ 0 ][ unit ].fixViolated( statusOfGenerator );
            
            /**
             *After fixing the solution, set the chromosome according to new values
             */
            for( int hour = 0; hour < this.numOfHour; hour++)
            {   
                if( statusOfGenerator[ hour ] == true)
                    
                    chromosome.getGene( unit + hour * this.numOfGenerator ).setValue( 1 );
                
                else
                    
                    chromosome.getGene( unit + hour * this.numOfGenerator ).setValue( 0 );
            }
        }
        
        return chromosome;
    }
    
    public void run()
    {
        double avareageCost = 0;

        Result bestResult = new Result();

        double bestFitness = Double.MAX_VALUE;

        double worstFitness = Double.MIN_VALUE;
        
        boolean useHillClimbing = true;

        double []runResults = new double[ numOfRun ];
                        
        try
        {   
            ArrayList<PowerGeneratorData> generatorsData = this.readFile(file);

            this.setGeneratorsData(generatorsData);
            
            for( int i = 0; i < numOfRun; i++ ){

                Result result = new Result();

                DE diffEvoAlgorithm = new DE(this, this.mutationType, useHillClimbing ,this.progress);

                result = diffEvoAlgorithm.start();
                
                System.out.println(result);

                double currentFitness = result.getFinalResult().getFitness();
                
                runResults[ i ] = currentFitness;
                
                avareageCost += currentFitness;
                
                if( currentFitness < bestFitness )
                {
                    bestResult = result;
                    
                    bestFitness = currentFitness;
                }
                
                if( currentFitness > worstFitness) 
                    
                    worstFitness = currentFitness; 
                
            }
            
            
            avareageCost = avareageCost / numOfRun;
            double deviation = this.standardDeviation( runResults );
            double error =  this.standardError( runResults );
            System.out.println("result: " + avareageCost );
            System.out.println("bestResult: " + bestFitness);
            System.out.println("worstResult: " + worstFitness);
            System.out.println("standard dev: " + deviation);
            System.out.println("standard error: " + error);
            
            this.printGeneratorsTable( bestResult.getFinalResult(), bestFitness, avareageCost, worstFitness, deviation, error );
            
            for(int i = 0; i < runResults.length; i++)
                System.out.println( runResults[i]);
            
            ResultGUI resultGui = new ResultGUI(new File("output.txt"));
            
            resultGui.setVisible( true );
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null,e.getMessage());
            progress.finish();
        }
    }
    
    public void printGeneratorsTable( Chromosome chromosome, double best, double average, double worst, double dev, double err)throws DEException,Exception
    {
        
        //Set the generators status according to solution matrix
        for(int i = 0; i < this.numOfHour; i++)
        {   
            int j = 0;
            //set the status of generators
            for( PowerGenerator p : this.generators[i] )
            {
                if( chromosome.getGene( i * this.numOfGenerator + j ).getValue() == 0 )
                {
                    p.setOn(false);
                }
                else
                {
                    p.setOn(true);
                }
                
                j++;
            }
        }
        
        String fileName = "output.txt";
        
        BufferedWriter file = new BufferedWriter(new FileWriter(fileName));    
        
        file.write( this.numOfGenerator + "\t" + this.numOfHour + "\n");
        
        file.write("Hour" + "\t");
        
        for(int i = 0; i < this.numOfGenerator; i++)
            file.write("P"+i+"\t");
        file.write("\n");
        
        for(int hour = 0; hour < this.numOfHour; hour++)
        {
            PowerGenerator [] generators = this.lambdaIteration.calcECD(this.generators[hour],this.powerDemand[hour],this.TOLERANCE);
            
            file.write("Hour" + hour + "\t");
            for(PowerGenerator p : generators)
            {
                p.print();
                
                file.write( p.getPout() + "\t");
                
            }
            
            file.write("\n");
        }
        
        file.write( "best" + "\t" + best + "\n");
        file.write( "average" + "\t" + average + "\n");
        file.write( "worst" + "\t" + worst + "\n");
        file.write( "standard_deviaton" + "\t" + dev + "\n");
        file.write( "standard_error" + "\t" + err + "\n");
        file.close();
        
    }
    
    
    /**
     *numOfHour and numOfGenerator must already been set.
     *This method sets the generator data for PowerGenerator objects 
     *The data is seperated from PowerGenerator in order to lower the memory need
     */
    public void setGeneratorsData(ArrayList<PowerGeneratorData> generatorsData)
    {
        this.generators = new PowerGenerator[this.numOfHour][this.numOfGenerator];
        
        for(int i = 0; i < this.numOfHour; i ++)
        {
            for(int j = 0; j < this.numOfGenerator; j++)
            {
                this.generators[i][j] = new PowerGenerator();           //create the objects
                    
                this.generators[i][j].setGeneratorData( generatorsData.get( j ) );    //Set the generators' data
                
            }
            
        }
        
    }
    

    
    public void setConstants( double F, double CR, int numOfPop,int numOfIter, double K, double M, int mutType, int numOfRun)
    {
        this.K = K;
        this.M = M;
        this.numOfRun = numOfRun;
        
        DE.F = F;
        DE.CR = CR;
        DE.populationSize = numOfPop;
        DE.MAX_ITERATION = numOfIter;
        
        switch( mutType )
        {
            case 0:
                this.mutationType = mutationType.BINARY_RAND_1;
                break;
            case 1:
                this.mutationType = mutationType.BINARY_RAND_2;
                break;
            case 2:
                this.mutationType = mutationType.BINARY_BEST_1;
                break;
            case 3:
                this.mutationType = mutationType.BINARY_BEST_2;
                break;
            default:
                break;
        }
        
        
        
    }
    
 
    public static void main(String args[])
    {
        File file = new File("/home/alikeles/test_files/input3");
        
        UnitCommitment unitCom = new UnitCommitment(new ProgressGUI(), file );
        
        Thread t = new Thread(unitCom);
        
        t.start();
        
    }
    
}

