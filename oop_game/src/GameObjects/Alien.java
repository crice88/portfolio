package com.mycompany.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

/** 
 * Concrete class that defines the Alien class functionality. Alien is an
 * Opponents. 
 */
public class Alien extends Opponents implements IDrawable
{
    // Private final variables that can not be changed.
    private final int ALIEN_SPEED = 200;
    private final int ALIEN_SPEED_CONST = 1;
    
    // Class variables
    private Sound newAlienSound;   
    private boolean newlyCreated = false;
    private int timeAlive;
    
    /** Overloaded constructor to be used to create an Alien at a specific location. */
    public Alien(float x, float y, Sound s1, Sound s2)
    {
        super.setColor(ColorUtil.argb(UPPER_BOUND_COLOR, 0, 0, UPPER_BOUND_COLOR));
        super.setSize(randNum(UPPER_BOUND_SIZE, LOWER_BOUND_SIZE));
        super.setSpeed(ALIEN_SPEED * ALIEN_SPEED_CONST);
        this.setDirection(randNum(UPPER_BOUND_DIR, 0));
        this.setLocationX(x);
        this.setLocationY(y);
        this.newlyCreated = true;
        this.timeAlive = 0;
        this.newAlienSound = s1;
        super.setAlienAstronautCollisionSound(s2);
    }
    
    /** Overloaded constructor to be used when an alien is created during gameplay. */
    public Alien(float x, float y, Sound s1, Sound s2, boolean b)
    {
        super.setColor(ColorUtil.argb(UPPER_BOUND_COLOR, 0, 0, UPPER_BOUND_COLOR));
        super.setSize(randNum(UPPER_BOUND_SIZE, LOWER_BOUND_SIZE));
        super.setSpeed(ALIEN_SPEED * ALIEN_SPEED_CONST);
        this.setDirection(randNum(UPPER_BOUND_DIR, 0));
        this.setLocationX(x);
        this.setLocationY(y);
        this.newAlienSound = s1;
        super.setAlienAstronautCollisionSound(s2);
        this.newlyCreated = b;
        this.timeAlive = 0;
    }
    
    /** 
     * Overrides Opponents toString() method to include fields that are specific to the
     * Alien class.
     * 
     *  @return custom string
     */
    public String toString()
    {
        String s;
        
        s = "Alien: " + super.toString();
        
        return s;
    }

    /**
     * Draws the alien object as a circle.
     * 
     * @param g The graphics object to manipulate
     * @param pCmpRelPrnt the point that is relative to the parent
     */
    public void draw(Graphics g, Point pCmpRelPrnt)
    {
        g.setColor(this.getColor());
      g.drawRect(pCmpRelPrnt.getX() + (int) this.getLocationX(), pCmpRelPrnt.getY() + (int) this.getLocationY(), 
            super.getSize(), super.getSize(), 0);
      g.drawArc(pCmpRelPrnt.getX() + (int) this.getLocationX(), pCmpRelPrnt.getY() + (int) this.getLocationY(), 
            super.getSize(), super.getSize(), 0, 360);
    }
    
    /**
     * Designates an alien object as newly created.
     * 
     * @param b newly created or not
     */
    public void setNewlyCreated(boolean b)
    {
        newlyCreated = b;
    }
    
    /**
     * Gets whether an object is newly created.
     * 
     * @return boolean
     */
    public boolean getNewlyCreated()
    {
        return newlyCreated;
    }
    
    /**
     * Gets the sound assigned to the object.
     * 
     * @return Sound
     */
    public Sound getAlienAlienCollisionSound()
    {
      return newAlienSound;
    }    
    
    /**
     * Increments the time the object has been alive.
     */
    public void incrementTimeAlive()
    {
      timeAlive++;
    }
    
    /**
     * Returns how long the object has been in existance.
     * 
     * @return int
     */
    public int getTimeAlive()
    {
      return this.timeAlive;
    }
    // Overrides setters so speed, and color cannot be changed after object is instantiated.   
    @Override
    public void setColor(int x) {};
    
    @Override
    public void setSpeed(int x) {};
    
    @Override
    public void setSize(int n) {};
}
