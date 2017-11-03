package com.mycompany.a3;

import java.util.Random;

/**
 * Abstract class that is the super class of all of the objects 
 * used within the game. Contains all common elements of class hierarchy.
 */
public abstract class GameObject
{
    // Class variables 
    private float locationX;
    private float locationY;
    private int size;
    private int color;
    
    // Static named variables to be used throughout entire program
    public static final int UPPER_BOUND_DIR = 359;
    public static final int UPPER_BOUND_SIZE = 80;
    public static final int LOWER_BOUND_SIZE = 50;
    public static final int UPPER_BOUND_SHIP = 1024;
    public static final int LOWER_BOUND_SHIP = 50;
    public static final int UPPER_BOUND_COLOR = 255;
    
    /**
     * Generates a random number given an upper bound and a lower bound. 
     * 
     * @param x upper bound of the random number.
     * @param y lower bound of the random number.
     * 
     * @return  a randomly selected integer found between x and y. 
     */
    public static int randNum(int x, int y)
    {
        int n;
        Random r; 
        
        r = new Random();
        n = r.nextInt(x - y) + y;
        
        return n;
    }
    
    /** 
     * Gets the color of the selected object. 
     * 
     * @return integer representing the ColorUtil color in argb().
     */
    public int getColor()
    {
        return this.color;
    }
    
    /**
     * Sets the objects color.
     * 
     * @param x a color in argb().
     */
    public void setColor(int x)
    {
        this.color = x;
    }
    
    /** 
     * Gets the x location of the selected object. 
     * 
     * @return float that represents the objects x coordinate.
     */
    public float getLocationX()
    {
        return this.locationX;
    }
    
    /**
     * Gets the y location of the selected object. 
     * 
     * @return float that represents the objects y coordinate.
     */
    public float getLocationY()
    {
        return this.locationY;
    }
    
    /**
     * Sets the objects x coordinate. Method is overwritten in Opponent class
     * to handle boundary issues.
     * 
     * @param f the x coordinate that you would like to change.
     */
    public void setLocationX(float f)
    {
      this.locationX = f;
    }
    
    /**
     * Sets the objects y coordinate. Method is overwritten in Opponent class
     * to handle boundary issues.
     * 
     * @param f the y coordinate that you would like to change.
     */
    public void setLocationY(float f)
    {
        this.locationY = f;
    }
    
    /**
     * Gets the current size of the object.
     * 
     * @return integer representing objects size. 
     */
    public int getSize()
    {
        return this.size;
    }
    
    /**
     * Sets the current size of the object.
     * 
     * @param n integer size to be changed to.
     */
    public void setSize(int n)
    {
        this.size = n;
    }
    
    /**
     * Changes objects location based on speed and direction. To be used when the game
     * clock has ticked.
     * 
     * @param speed     the speed of the object.
     * @param direction the direction the object is heading.
     * @param time the length of time that is incremented every tick.
     */
    public void newLocation(int speed, int direction, int time)
    {
        float deltaX;
        float deltaY;
        float theta;
        
        float distance = speed * (time / 1000.0f);
        
        theta = (float) (Math.toRadians(90 - direction));
        
        deltaX = (float) (Math.cos(theta) * distance);
        deltaY = (float) (Math.sin(theta) * distance);
        
        this.setLocationX(this.getLocationX() + deltaX);
        this.setLocationY(this.getLocationY() + deltaY);
    }
    
    /**
     * Allows the toString() method to be called without having to cast
     * each instance. 
     */
    @Override
    public abstract String toString();
}
