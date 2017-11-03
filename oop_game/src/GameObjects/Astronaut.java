package com.mycompany.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

/** Concrete class for GameObjects' Astronaut. Astronaut is an Opponents. */
public class Astronaut extends Opponents implements IDrawable, ISelectable
{
    // Private field unique to Astronaut 
    private int health;
    private boolean isSelect = false;
    
    // Private final variables that cannot be changed 
    private final int ASTRO_SPEED_CONST = 40;
    private final int STARTING_HEALTH = 5;
    private final int NUM_POINTS_TRIANGLE = 3;
    
    /** Constructor */
    public Astronaut(float x, float y, Sound s)
    {
        health = STARTING_HEALTH;
        this.setSpeed(STARTING_HEALTH * ASTRO_SPEED_CONST);
        this.setDirection(randNum(UPPER_BOUND_DIR, 0));
        super.setSize(randNum(UPPER_BOUND_SIZE, LOWER_BOUND_SIZE));
        this.setLocationX(x);
        this.setLocationY(y);
        this.setColor(ColorUtil.argb(UPPER_BOUND_COLOR, UPPER_BOUND_COLOR, 0, 0));
        super.setAlienAstronautCollisionSound(s);
    }
    
    /** 
     * Attacked method that is executed when an Astronaut object 
     * encounters an Alien. Decrements health by one, changes speed, and color.
     * If health goes below 0, set health, speed and direction to 0.
     * 
     * @param void
     */
    public void attacked()
    {
        this.health = health - 1;
        
        if (this.health > 0)
        {
        	int new_red = (STARTING_HEALTH - this.health) * 51;
        	this.setColor(ColorUtil.argb(UPPER_BOUND_COLOR, UPPER_BOUND_COLOR, new_red, new_red));
            this.setSpeed(this.health * ASTRO_SPEED_CONST);
        }
        else
        {
        	this.health = 0;
        	this.setDirection(0);
        	this.setSpeed(0);
        	this.setColor(ColorUtil.argb(UPPER_BOUND_COLOR, UPPER_BOUND_COLOR, 229, 204));
        }
    }
    
    /** 
     * Gets the objects current health.
     * 
     * @return returns the current health of astronaut. 
     */
    public int getHealth()
    {
        return this.health;
    }
    
    /** 
     * Overrides Opponents toString() method to include fields that are specific to the
     * Astronaut class.
     * 
     *  @return custom string
     */
    public String toString()
    {
        String s;
        
        s = "Astronaut: " + super.toString() + " health=" + this.health;
        
        return s;
    }

    /** 
     * Overrides move method so when Astronaut is out of health, it does not move. 
     * 
     * @param void
     */
    public void move(int e, int height, int width)
    {
    	if (this.health > 0)
    	{
    		super.move(e);
    	}
    }
    
    /**
     * Draws the alien object as a triangle.
     * 
     * @param g The graphics object to manipulate
     * @param pCmpRelPrnt the point that is relative to the parent
     */
    public void draw(Graphics g, Point pCmpRelPrnt)
    {	
    	int bottomLeftX = (int) (pCmpRelPrnt.getX() + this.getLocationX());
    	int bottomLeftY = (int) (pCmpRelPrnt.getY() + this.getLocationY());
    	int bottomRightX = (int) (pCmpRelPrnt.getX() + this.getLocationX()) + this.getSize();
    	int bottomRightY = (int) (pCmpRelPrnt.getY() + this.getLocationY());
    	int topX = (int) (pCmpRelPrnt.getX() + this.getLocationX()) + (this.getSize() / 2);
    	int topY = (int) (pCmpRelPrnt.getY() + this.getLocationY()) + this.getSize();
    	
    	int[] xPoints = { bottomLeftX, bottomRightX, topX };
    	int[] yPoints = { bottomLeftY, bottomRightY, topY };
    	
    	g.setColor(this.getColor());
    	g.drawRect(pCmpRelPrnt.getX() + (int) this.getLocationX(), pCmpRelPrnt.getY() + (int) this.getLocationY(), 
  			  	   super.getSize(), super.getSize(), 0);
    	
    	// Check if the object is selected. If it is, fill it in.
    	if (this.isSelected())
    	{
    		g.fillPolygon(xPoints, yPoints, NUM_POINTS_TRIANGLE);
    	}
    	else
    	{
        	g.drawPolygon(xPoints, yPoints, NUM_POINTS_TRIANGLE);
    	}
    }
    
    /**
     * Sets the object as selectable.
     * 
     * @param b true or false
     */
    public void setSelectable(boolean b)
    {
    	isSelect = b;
    }
    
    /**
     * Sees if the object is selected.
     * 
     * @return boolean
     */
    public boolean isSelected()
    {
    	return isSelect;
    }
    
    /**
     * Checks to see if the pointer is within the object.
     * 
     * @param pPtrRelPrnt the pointer relative to the parent.
     * @param pCmpRelPrnt the component relative to the parent.
     * 
     * @return boolean
     **/
    public boolean contains(Point pPtrRelPrnt, Point pCmpRelPrnt)
    {
    	int pX = pPtrRelPrnt.getX();
    	int pY = pPtrRelPrnt.getY();
    	int xLoc = pCmpRelPrnt.getX() + (int) this.getLocationX();
    	int yLoc = pCmpRelPrnt.getY() + (int) this.getLocationY();
    	
    	if ((pX >= xLoc) && (pX <= xLoc + this.getSize()) &&
    		(pY >= yLoc) && (pY <= yLoc + this.getSize()))
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    /**
     * Reverts the objects health to full.
     */
    public void heal()
    {
    	this.health = STARTING_HEALTH;
    	this.setColor(ColorUtil.rgb(UPPER_BOUND_COLOR, 0, 0));
    	this.setSpeed(STARTING_HEALTH * ASTRO_SPEED_CONST);
    }
    
 // Overriding setSize method so Opponents can't change size after instantiation.
    @Override
    public void setSize(int n) {};
}
