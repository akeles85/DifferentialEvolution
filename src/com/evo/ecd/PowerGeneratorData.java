/*
 * PowerGeneratorData.java
 *
 * Created on 22 Mart 2007 Perşembe, 23:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.evo.ecd;

/**
 *
 * @author root
 */
public class PowerGeneratorData {
    
    private double a;
    
    private double b;
    
    private double c;
    
    private double fuelCost;
    
    private String name;
    
    private int type;
    
    private double Pmax;
    
    private double Pmin;
    
    private int hourForColdStart;
    
    private double coldStartUpCost;
    
    private double hotStartUpCost;
    
    private int initialState;
    
    private double t_on;
    
    private double t_off;
    
    /** Creates a new instance of PowerGeneratorData */
    public PowerGeneratorData() {
    }
    
    public void print()
    {
        System.out.println("Name: " + getName() );
        
        System.out.println("Pmin: " + this.getPmin() );
        
        System.out.println("Pmax: " + this.getPmax() );
        
        System.out.println("a: " + this.getA() );
        
        System.out.println("b: " + this.getB() );
        
        System.out.println("c: " + this.getC() );
        
        System.out.println("Fuel Cost: " + this.getFuelCost() );
        
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getFuelCost() {
        return fuelCost;
    }

    public void setFuelCost(double fuelCost) {
        this.fuelCost = fuelCost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getPmax() {
        return Pmax;
    }

    public void setPmax(double Pmax) {
        this.Pmax = Pmax;
    }

    public double getPmin() {
        return Pmin;
    }

    public void setPmin(double Pmin) {
        this.Pmin = Pmin;
    }

    public int getHourForColdStart() {
        return hourForColdStart;
    }

    public void setHourForColdStart(int hourForColdStart) {
        this.hourForColdStart = hourForColdStart;
    }

    public double getColdStartUpCost() {
        return coldStartUpCost;
    }

    public void setColdStartUpCost(double coldStartUpCost) {
        this.coldStartUpCost = coldStartUpCost;
    }

    public double getHotStartUpCost() {
        return hotStartUpCost;
    }

    public void setHotStartUpCost(double hotStartUpCost) {
        this.hotStartUpCost = hotStartUpCost;
    }

    public int getInitialState() {
        return initialState;
    }


    public void setInitialState(int initialState)
    {
        this.initialState = initialState;
        
    }

    public double getMinOn() 
    {
        return t_on;
    }

    public void setMinOn(double t_on)
    {
        this.t_on = t_on;
    }

    public double getMinOff() 
    {
        return t_off;
    }

    public void setMinOff(double t_off)
    {
        this.t_off = t_off;
    }

    
}
