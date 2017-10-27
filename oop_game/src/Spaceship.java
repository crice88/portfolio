package com.mycompany.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

/** 
 * Concrete class Spaceship is a Rescuer. Implements functionality of the Spaceship
 * class, as specified in the documents. Follows Singleton design pattern. 
 */
public class Spaceship extends Rescuers implements IDrawable
{ 
	// Single global reference to the spaceship.
	private static Spaceship ship;
	private Sound doorOpenSound;
	
	// Makes sure that no one has access to the constructor directly.
	private Spaceship(float x, float y, Sound s) 
	{
		this.setSize(100);
        this.setLocationX(x);
        this.setLocationY(y);
        super.setColor(ColorUtil.argb(UPPER_BOUND_COLOR, 0, 0, 0));
        this.doorOpenSound = s;
	}
	
    /** Private constructor that adheres to the Singleton pattern. */
    public static Spaceship getShip(float x, float y, Sound s)
    {
    	if (ship == null)
    	{
    		ship = new Spaceship(randNum((int) x, 0), randNum((int) y, 0), s);
    	}
    	return ship;
    }
    
    /** 
     * Overrides Rescuers toString() method to include description information specific
     * to the Spaceship class.
     * 
     *  @return custom string
     */
    @Override
    public String toString()
    {
        String s;
        
        s = "Spaceship: " + super.toString();
        
        return s;
    }
    
    /** 
     * Overrides superclass setColor so the color can not be changed after the 
     * object is instantiated. 
     */
    @Override
    public void setColor(int n) {};
    
    /** 
     * Overrides superclass setSize in order to keep the size of the spaceship within it's
     * pre-defined bounds. 
     */
    @Override
    public void setSize(int s)
    {        
        if (s < LOWER_BOUND_SHIP)
        {
            super.setSize(LOWER_BOUND_SHIP);
        }
        else if (s > UPPER_BOUND_SHIP)
        {
            super.setSize(UPPER_BOUND_SHIP);
        }
        else
        {
            super.setSize(s);
        }
    }
    
    /**
     * Draws the spaceship object as a square.
     * 
     * @param g The graphics object to manipulate
     * @param pCmpRelPrnt the point that is relative to the parent
     */
    public void draw(Graphics g, Point pCmpRelPrnt)
    {
    	g.setColor(this.getColor());
    	g.drawRect((int) (pCmpRelPrnt.getX() + this.getLocationX()), (int) (pCmpRelPrnt.getY() + this.getLocationY()), 
    			   super.getSize(), super.getSize(), 10);
    }
    
    /**
     * Returns the door opening sound.
     * 
     * @return Sound
     */
    public Sound getOpenDoorSound()
    {
    	return doorOpenSound;
    }
}
