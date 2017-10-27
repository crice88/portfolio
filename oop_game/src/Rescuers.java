package com.mycompany.a3;

import com.codename1.charts.util.ColorUtil;

/** 
 * Class is designed to abstract a second layer in the GameObject hierarchy. Groups all 
 * objects that have common 'Rescuer' traits. Class is a GameObject and implements IGuided.
 */
public abstract class Rescuers extends GameObject implements IGuided
{
    // Private final fields 
    private static final int MOVE_DISTANCE = 10;
    
    /** 
     * Methods move objects location based on X coordinate and 
     * pre-defined MOVE_DISTANCE. 
     */
    public void moveLeft()
    {
        this.setLocationX(this.getLocationX() - MOVE_DISTANCE);
    }
    
    public void moveRight()
    {
        this.setLocationX(this.getLocationX() + MOVE_DISTANCE);
    }
    
    public void moveUp()
    {
        this.setLocationY(this.getLocationY() + MOVE_DISTANCE);
    }
    
    public void moveDown()
    {
        this.setLocationY(this.getLocationY() - MOVE_DISTANCE);
    }
    
    /** 
     * Moves object to a specific location. 
     * 
     * @param x a float that represents the x coordinate. 
     * @param y a float that represents the y coordinate. 
     * */
    public void jumpToLoc(float x, float y)
    {
        this.setLocationX(x);
        this.setLocationY(y);
    }
    
    /**
     * Overrides toString() to print Rescuers common traits. 
     * 
     * @return the custom string
     */
    @Override
    public String toString()
    {
    	String s;
    	

        s = "loc=" + Math.round(this.getLocationX() * 10.0f)/10.0f 
            + ","  + Math.round(this.getLocationY() * 10.0f)/10.0f 
            + " color=[" + ColorUtil.alpha(this.getColor()) + "," 
            +              ColorUtil.red(this.getColor())   + "," 
            +              ColorUtil.blue(this.getColor())  + "," 
            +              ColorUtil.green(this.getColor()) + "]"         
            + " size="   + this.getSize();
        
        return s;
    }
}
