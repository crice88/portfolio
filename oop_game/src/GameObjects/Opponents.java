package com.mycompany.a3;

import java.util.ArrayList;

import com.codename1.charts.util.ColorUtil;

/**
 * Provides a second layer of abstraction for GameObject hierarchy. Groups
 * all functionality common to Opponents. Class is a GameObject and implements
 * the IMoving interface. 
 */
public abstract class Opponents extends GameObject implements IMoving, ICollider
{
  // Final named constants
  private final int MAX_NUM_ALIENS = 30;
  private final int MIN_NUM_ALIENS = 2;
  private final int RAND_DIR_UPPER_BOUND = 180;
  private final int RAND_DIR_LOWER_BOUND = 40;
  
    // Private fields
    private int direction;
    private int speed;
    private ArrayList<Opponents> collisionList = new ArrayList<Opponents>();
    private Sound alienAstronautCollisionSound;
    
    /**
     * Sets the Opponent objects direction.
     * 
     * @param n the direction that will be set. 
     */
    public void setDirection(int n)
    {
      this.direction = n;
    }
    
    /**
     * Sets the Opponent speed
     * 
     * @param n the speed to set the object to. 
     */
    public void setSpeed(int n)
    {
        this.speed = n;
    }
    
    /**
     * Gets the current Opponents speed. 
     * 
     * @return returns the speed of the object. 
     */
    public int getSpeed()
    {
        return this.speed;
    }
    
    /**
     * Gets the current Opponents direction. 
     * 
     * @return returns the direction of the object. 
     */
    public int getDirection()
    {
        return this.direction;
    }
    
    /**
     * Reverse the direction of an Opponent object. Used when an opponent hits a 
     * wall. Random direction bound between 150 and 60.
     */
    public void reverseDirection()
    {
      int r = super.randNum(RAND_DIR_UPPER_BOUND, RAND_DIR_LOWER_BOUND);
        this.direction = (this.direction + r) % 360;
    }
    
    /**
     * Move an object on the board. Location is changed based on speed, direction
     * and the time that has elapsed.
     * 
     * @param elapsedTime the time that the game clock has ticked.
     */
    public void move(int elapsedTime)
    {        
        this.newLocation(this.getSpeed(), this.getDirection(), elapsedTime);
    }
    
    /**
     * Overrides toString() method to print common traits in Opponents class.
     * 
     * @return custom string
     */
    @Override
    public String toString()
    {
        String s;
        
        s =     "loc=" + Math.round(this.getLocationX() * 10.0f)/10.0f 
                + ","  + Math.round(this.getLocationY() * 10.0f)/10.0f 
                + " color=[" + ColorUtil.alpha(this.getColor()) + "," 
                +              ColorUtil.red(this.getColor())   + "," 
                +              ColorUtil.blue(this.getColor())  + "," 
                +              ColorUtil.green(this.getColor()) + "]"         
                + " size="   + super.getSize() 
                + " speed=" + this.getSpeed()
                + " dir="    + this.getDirection();
        
        return s;
    }
    
    /**
     * Check to see if an ICollider object has collided with another object.
     * 
     * @param obj the object involved in the collision
     * 
     * @return boolean
     */
    public boolean collidesWith(ICollider obj)
    {
        Opponents o = (Opponents) obj;
        boolean result = false;
        
        int L1 = (int) (this.getLocationX() + (this.getSize() / 2));
        int T1 = (int) (this.getLocationY() + (this.getSize() / 2));
        int R1 = (int) (this.getLocationX() + (this.getSize() * 1.5));
        int B1 = (int) (this.getLocationY() + (this.getSize() * 1.5));
        
        int L2 = (int) (o.getLocationX() + (o.getSize() / 2));
        int T2 = (int) (o.getLocationY() + (o.getSize() / 2));
        int R2 = (int) (o.getLocationX() + (o.getSize() * 1.5));
        int B2 = (int) (o.getLocationY() + (o.getSize() * 1.5));
        
        if (((R1 < L2) || (L1 > R2)) || (((T2 > B1) || (T1 > B2))))
        {
            result = false;
        }
        else
        {
          result = true;
        }
        
        return result;
    }
    
    /**
     * Handle both Alien-Alien and Alien-Astronaut collision.
     * 
     * @param obj the object in the collision.
     * @param gw the gameworld that the collision occurred in
     */
    public void handleCollision(ICollider obj, GameWorld gw)
    {
        Opponents o = (Opponents) obj;
        
        // If the collision has already been handled, return
        if (this.collisionList.contains(o) || o.collisionList.contains(this))
        {
          return;
        }
        
        if (o instanceof Alien && this instanceof Alien)
        {
               GameObjectCollection gamePieces = gw.getGameObjectCollection();
               
               // Check to make sure there are more than 2 Aliens remaining and the max 
               // number of aliens hasn't been reached.
               if (gw.getAlienRemain() < MIN_NUM_ALIENS || gw.getAlienRemain() == MAX_NUM_ALIENS)
               {
                   return;
               }
               
               // Check to see if the object is newly created.
               if (((Alien) o).getNewlyCreated())
               {
                 return;
               }
               else if (((Alien) this).getNewlyCreated())
               {
                 return;
               }

               // Creates alien at one parents' x and the other parents' y to randomize creation location.
               Alien a = new Alien(this.getLocationX(), o.getLocationY(), gw.getAlienAlienCollisionSound(), gw.getAlienAstronautCollisionSound());

               // Add a new alien to the collection
               gamePieces.add(a);
               gw.setGameObjectCollection(gamePieces);     
               
               // Add each object to each others collision list
               this.collisionList.add(o);
               o.collisionList.add(this);
               
               // Play a collision sound if the sound is on.
               if (gw.getSound())
               {
                   ((Alien) this).getAlienAlienCollisionSound().play();
               }
        }
        else if (obj instanceof Astronaut && this instanceof Alien)
        {
            Astronaut a = (Astronaut) obj;
            a.attacked();
            
            if(gw.getSound())
            {
                this.getAlienAstronautCollisionSound().play();
            }
            
            ((Opponents) a).collisionList.add(this);
            ((Opponents) this).collisionList.add(a);
        }
        gw.updateOpponentsRemaining();
    }
    
    /**
     * Returns the collision sound.
     * 
     * @return Sound
     */
    public Sound getAlienAstronautCollisionSound()
    {
      return alienAstronautCollisionSound;
    }
    
    /**
     * Returns the collision sound.
     * 
     * @return Sound
     */
    public void setAlienAstronautCollisionSound(Sound s)
    {
      alienAstronautCollisionSound = s;
    }
    
    /**
     * Returns the opponents collision list.
     * 
     * @return ArrayList<Opponents>
     */
    public ArrayList<Opponents> getCollisionList()
    {
      return this.collisionList;
    }
}
