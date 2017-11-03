package com.mycompany.a3;

import java.util.Observable;
import java.util.Random;

import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.SideMenuBar;

/** 
 * Class that provides functionality to the game. All methods call GameObject methods that
 * manipulate individual objects of type GameObject. GameWorld implements IGuided and keeps 
 * track of what is happening in the game. Is designated as an Observable.
 */
public class GameWorld extends Observable implements IGuided
{
    // Dynamic ArrayList to hold all of the GameObjects.
    private GameObjectCollection gamePieces = new GameObjectCollection();
    
    // Private final variables that cannot be changed.
    private final int START_NUM_ALIENS = 3;
    private final int START_NUM_ASTRO = 4;
    private final int GOBJ_CREATION_OFFSET = 100;
    private final int LENGTH_OF_TIME_ALIVE = 500;
    private final int DOOR_SIZE_CONSTANT = 20;
    private final String ALIEN_ALIEN_SOUND_FILENAME = "alien-alien_crash.wav";
    private final String ALIEN_ASTRONAUT_SOUND_FILENAME = "alien-astro_crash.wav";
    private final String DOOR_OPEN_SOUND_FILENAME = "door_opening.wav";
    private final String BG_SOUND_FILENAME = "voyeng.wav";
    
    // Private fields 
    private int astroRescued;
    private int alienSneakedIn;
    private int astroRemaining;
    private int alienRemaining;
    private int score;
    private boolean isSound = false;
    private boolean isPaused = false;
    private int upperBoundX;
    private int upperBoundY;
    private Sound alienAlienCollisionSound = new Sound(ALIEN_ALIEN_SOUND_FILENAME);
    private Sound alienAstronautCollisionSound = new Sound(ALIEN_ASTRONAUT_SOUND_FILENAME);
    private Sound doorOpenSound = new Sound(DOOR_OPEN_SOUND_FILENAME);
    private BGSound bgSound = new BGSound(BG_SOUND_FILENAME);
    
    /** Sets up default game values. */
    public void init()
    {
        gameObjSetup(START_NUM_ALIENS, START_NUM_ASTRO);
        astroRescued = 0;
        alienSneakedIn = 0;
        astroRemaining = START_NUM_ASTRO;
        alienRemaining = START_NUM_ALIENS;
        isSound = true;
        
        bgSound.play();
        
        setChanged();
        notifyObservers(this);
    }
    
    /** 
     * Fills gamePieces array with the passed number of aliens and astronauts. 
     * Called at the beginning of the game. 
     * 
     * @param numAlien number of Alien objects to create. 
     * @param numAstro number of Astronaut objects to create.  
     */
    private void gameObjSetup(int numAlien, int numAstro)
    {
        int i;
        
        for (i = 0; i < numAlien; i++)
        {
          // Objects are created at an offset to make sure that they are created fully inbounds.
            gamePieces.add(new Alien(GameObject.randNum(upperBoundX - GOBJ_CREATION_OFFSET, 0), 
                           GameObject.randNum(upperBoundY - GOBJ_CREATION_OFFSET, 0), alienAlienCollisionSound,
                           alienAstronautCollisionSound, false));
        }
        
        for (i = 0; i < numAstro; i++ )
        {
            gamePieces.add(new Astronaut(GameObject.randNum(upperBoundX - GOBJ_CREATION_OFFSET, 0),
                           GameObject.randNum(upperBoundY - GOBJ_CREATION_OFFSET, 0), alienAstronautCollisionSound));
        }
        
        Spaceship ship = Spaceship.getShip(GameObject.randNum(upperBoundX - GOBJ_CREATION_OFFSET, 0), 
                                           GameObject.randNum(upperBoundY - GOBJ_CREATION_OFFSET, 0), doorOpenSound);
        gamePieces.add(ship);
    }
    
    /** 
     * Moves spaceship object to specified location. 
     * 
     * @param x float that designates x coordinate. 
     * @param y float that designates y coordinate.
     */
    public void jumpToLoc(float x, float y)
    {
      Spaceship s;
      IIterator iGameObj = gamePieces.getIterator();
      
      while (iGameObj.hasNext())
      {
        GameObject gObj = (GameObject) iGameObj.getNext();
        if(gObj instanceof Spaceship)
        {
          s = (Spaceship) gObj;
            
          s.setLocationX(x);
              s.setLocationY(y);
              
              setChanged();
              notifyObservers(this);
        }
      }
    }
    
    /** Methods that call spaceships methods of the same name. */
    public void moveLeft()
    {
      Spaceship s;
      IIterator iGameObj = gamePieces.getIterator();
      
      while (iGameObj.hasNext())
      {
        GameObject gObj = (GameObject) iGameObj.getNext();
        if(gObj instanceof Spaceship)
        {
          s = (Spaceship) gObj;
            
          s.moveLeft();
          
          setChanged();
                notifyObservers(this);
        }
      }
    }
    
    public void moveRight()
    {
      Spaceship s;
      IIterator iGameObj = gamePieces.getIterator();
      
      while (iGameObj.hasNext())
      {
        GameObject gObj = (GameObject) iGameObj.getNext();
        if(gObj instanceof Spaceship)
        {
          s = (Spaceship) gObj;
            
          s.moveRight();
          
          setChanged();
                notifyObservers(this);
        }
      }
    }
    
    public void moveUp()
    {
      Spaceship s;
      IIterator iGameObj = gamePieces.getIterator();
      
      while (iGameObj.hasNext())
      {
        GameObject gObj = (GameObject) iGameObj.getNext();
        if(gObj instanceof Spaceship)
        {
          s = (Spaceship) gObj;
            
          s.moveUp();
          
          setChanged();
                notifyObservers(this);
        }
      }
    }
    
    public void moveDown()
    {
      Spaceship s;
      IIterator iGameObj = gamePieces.getIterator();
      
      while (iGameObj.hasNext())
      {
        GameObject gObj = (GameObject) iGameObj.getNext();
        if(gObj instanceof Spaceship)
        {
          s = (Spaceship) gObj;
            
          s.moveDown();
          
          setChanged();
                notifyObservers(this);
        }
      }
    }
    
    /** Moves the spaceship to a random Aliens location. */ 
    public void jumpRandLocAlien()
    {
        int j;
        int z;
        
        if (alienRemaining == 0)
        {
          System.out.println("No Aliens are left!");
            return;
        }
        
        IIterator iGameObj = gamePieces.getIterator();
        Random r = new Random();
        float[] x = new float[alienRemaining];
        float[] y = new float[alienRemaining];
        
        j = 0;
        
        while(iGameObj.hasNext())
        {
          GameObject gObj = (GameObject) iGameObj.getNext();
          
            if (gObj instanceof Alien)
            {
                // Every alien found, place it's x and y location into 
                // corresponding arrays. 
                Alien a = (Alien) gObj;
                x[j] = a.getLocationX();
                y[j] = a.getLocationY();
                j++;
            }
        }
        
        z = r.nextInt(j);
        
        // Jump the spaceships location to that of a random Aliens location. 
        // 'z' is the random index chosen that corresponds to an Aliens coordinates. 
        jumpToLoc(x[z], y[z]);
    }
    
    /** 
     * Move the spaceship to a random Astronauts location. No need
     * to check whether Astronauts exist as the game ends when all
     * have been rescued.
     */
    public void jumpRandLocAstro()
    {
        int j;
        int z;
        
        IIterator iGameObj = gamePieces.getIterator();
        Random r = new Random();
        float[] x = new float[astroRemaining];
        float[] y = new float[astroRemaining];
        
        j = 0;
        
        while(iGameObj.hasNext())
        {
          GameObject gObj = (GameObject) iGameObj.getNext();
          
            if (gObj instanceof Astronaut)
            {
                 
                // Every astronaut found, place it's x and y location into 
                // corresponding arrays. 
                Astronaut a = (Astronaut) gObj;
                x[j] = a.getLocationX();
                y[j] = a.getLocationY();
                j++;
            }
        }
        
        z = r.nextInt(j);
        
        // Jump the spaceships location to that of a random Astronauts location. 
        // 'z' is the random index chosen that corresponds to an Astronauts coordinates. 
        jumpToLoc(x[z], y[z]);
    }
    
    /** Increases the door size of the ship. */
    public void expandShipSize()
    {
        Spaceship s;
        IIterator iGameObj = gamePieces.getIterator();
        
        while(iGameObj.hasNext())
        {
          GameObject gObj = (GameObject) iGameObj.getNext();
          
          if(gObj instanceof Spaceship)
          {
            s = (Spaceship) gObj;
            
            s.setSize(s.getSize() + DOOR_SIZE_CONSTANT);
                
                setChanged();
                notifyObservers(this);
          }
        }
    }
    
    /** Decreases the door size of the ship. */
    public void decreaseShipSize()
    {
        Spaceship s;
        IIterator iGameObj = gamePieces.getIterator();
        
        while(iGameObj.hasNext())
        {
          GameObject gObj = (GameObject) iGameObj.getNext();
          
          if(gObj instanceof Spaceship)
          {
            s = (Spaceship) gObj;
            
            s.setSize(s.getSize() - DOOR_SIZE_CONSTANT);
                
                setChanged();
                notifyObservers(this);
          }
        }
    }
    
    /** Increments the 'game clock' which moves each opponent object and checks for collisions. */
    public void tickClock(int elapsedTime)
    {
        IIterator iGameObj = gamePieces.getIterator();
        
        while(iGameObj.hasNext())
        {
          GameObject gObj = (GameObject) iGameObj.getNext();
          
            // Every object that implements IMoving interface, call their move() method. 
            if (gObj instanceof IMoving)
            {
                IMoving iObj = (IMoving) gObj;
                
                this.checkBoundary(gObj);
                
                iObj.move(elapsedTime);
            }
        }
        
        // Handle any collisions between objects
        iGameObj = gamePieces.getIterator();
        
        while (iGameObj.hasNext())
        {
            GameObject gObj = (GameObject) iGameObj.getNext();
            
            if (gObj instanceof ICollider)
            {
                ICollider curObj = (ICollider) gObj;
                
                IIterator iGameObj2 = gamePieces.getIterator();
                
                while(iGameObj2.hasNext())
                {
                    GameObject gObj2 = (GameObject) iGameObj2.getNext();
                    
                    if (gObj2 instanceof ICollider)
                    {
                        ICollider otherObj = (ICollider) gObj2;
                        
                        if (otherObj != curObj)
                        {
                          // If an object isn't collidiing see if it is in the collision list. If it is, remove it.
                            if (curObj.collidesWith(otherObj))
                            {
                                curObj.handleCollision(otherObj, this);
                            }
                            else if(((Opponents) curObj).getCollisionList().contains(otherObj) &&
                                ((Opponents) otherObj).getCollisionList().contains(curObj))
              {
                ((Opponents) curObj).getCollisionList().remove(otherObj);
                ((Opponents) otherObj).getCollisionList().remove(curObj);
              }
                        }
                    }  
                }
            }
        }
        this.incrementTimeAlive();
        setChanged();
        notifyObservers(this);
    }
    
    /**
     * Increments each newly created aliens time alive variable. Used
     * to determine when another collision can occur due to the issue
     * of running into each other immediately after creation.
     */
    public void incrementTimeAlive()
    {
      IIterator iGameObj = gamePieces.getIterator();
      
      while(iGameObj.hasNext())
        {
          GameObject gObj = (GameObject) iGameObj.getNext();

            if (gObj instanceof Alien)
            {
              Alien a = (Alien) gObj;
              
                if (a.getNewlyCreated() && a.getTimeAlive() > LENGTH_OF_TIME_ALIVE)
                {
                  a.setNewlyCreated(false);
                }
                else if (a.getNewlyCreated())
                {
                  a.incrementTimeAlive();
                }
            }
        }
    }
    
    /**
     * Checks to make sure each moving object is within the game 
     * bounds. Reverses direction if it is not.
     * 
     * @param gObj object to check boundary condition. 
     */
    public void checkBoundary(GameObject gObj)
    {
      Opponents o = (Opponents) gObj;
      
      if (o.getLocationX() + o.getSize() >= upperBoundX ||
        o.getLocationX() < 0)
      {
        o.reverseDirection();
      }
      else if (o.getLocationY() + o.getSize() >= upperBoundY ||
           o.getLocationY() < 0)
      {
        o.reverseDirection();
      }
    }
    
    /** 
     * Opens the spaceship door and checks to see if an Alien or Astronauts
     * is within the bounding square of the spaceship.
     */
    public void openDoor()
    {
        int L1 = 0;
        int T1 = 0;
        int R1 = 0;
        int B1 = 0;
        Spaceship s;
        
        IIterator iGameObj = gamePieces.getIterator();
        
        while(iGameObj.hasNext())
        {
          GameObject gObj = (GameObject) iGameObj.getNext();
          
          if(gObj instanceof Spaceship)
          {
            s = (Spaceship) gObj;

                // Finds the distance from the ships center, then assigns 
                // x and y boundaries based on those coordinates. 
                L1 = (int) (s.getLocationX() + s.getSize()) - (s.getSize() / 2);
                T1 = (int) (s.getLocationY() + s.getSize()) - (s.getSize() / 2);
                R1 = (int) (s.getLocationX() + s.getSize()) + (s.getSize() / 2);
                B1 = (int) (s.getLocationY() + s.getSize()) + (s.getSize() / 2);
                
                if(isSound)
                {
                    s.getOpenDoorSound().play();
                }
          }
        }        
        
        // Get another iterator to scan through the full collection
        iGameObj = gamePieces.getIterator();
        
        while(iGameObj.hasNext())
        {
          GameObject gObj = (GameObject) iGameObj.getNext();
          
            if (gObj instanceof Alien)
            {
                Alien a = (Alien) gObj;
                
                int L2 = (int) (a.getLocationX() + a.getSize())  - (a.getSize() / 2);
                int T2 = (int) (a.getLocationY() + a.getSize()) - (a.getSize() / 2);
                int R2 = (int) (a.getLocationX() + a.getSize()) + (a.getSize() / 2);
                int B2 = (int) (a.getLocationY() + a.getSize()) + (a.getSize() / 2);
                
                // Check if an Alien object is within the bounds of the spaceships door. 
                if (((R1 < L2) || (L1 > R2)) || ((T2 > B1) || (T1 > B2))) {}
                else
                {             
                    // Alien was let in, so decrease score by 10 and remove the Alien from the 
                    // GameObject collection. 
                    score -= 10;
                    iGameObj.remove();
                    
                    // Adjust score board.
                    this.alienSneakedIn++;
                }
            }
            else if (gObj instanceof Astronaut)
            {
                Astronaut a = (Astronaut) gObj;
                
                int L2 = (int) (a.getLocationX() + a.getSize())  - (a.getSize() / 2);
                int T2 = (int) (a.getLocationY() + a.getSize()) - (a.getSize() / 2);
                int R2 = (int) (a.getLocationX() + a.getSize()) + (a.getSize() / 2);
                int B2 = (int) (a.getLocationY() + a.getSize()) + (a.getSize() / 2);
                
                // Check if an Astronaut object is within the bounds of the spaceships door. 
                if (((R1 < L2) || (L1 > R2)) || ((T2 > B1) || (T1 > B2))) {}
                else
                {
                    // Astronaut was let in, so increase players score by 5 plus the saved astronauts health.
                    score += (5 + a.getHealth());
                    iGameObj.remove(); 
                    
                    // Adjust score board.
                    this.astroRescued++;
                }
            }
        }
        this.updateOpponentsRemaining();
        
        // If all astronauts have been rescued call noAstroRemain() which will print the final score and exit the game.
        if (astroRemaining == 0)
        {
            noAstroRemain();
        }
        
        this.setChanged();
        this.notifyObservers(this);
    }
    
    /**
     * Updates all opponents scores. Accurately tracks the number of aliens
     * and astronauts remaining in the game world. Called after score command.
     */
    public void updateOpponentsRemaining()
    {
      IIterator iGameObj = gamePieces.getIterator();
      int x = 0;
      int y = 0;
      
      while(iGameObj.hasNext())
      {
        GameObject gObj = (GameObject) iGameObj.getNext();
        
        if (gObj instanceof Alien)
        {
          x++;
        }
        else if (gObj instanceof Astronaut)
        {
          y++;
        }
      }
      
      this.alienRemaining = x;
      this.astroRemaining = y;
    }
    
    /** 
     * Calls each objects overridden toString() method. Kept
     * for debugging purposes. 
     */
    public void printMap()
    {        
        IIterator iGameObj = gamePieces.getIterator();

        while(iGameObj.hasNext())
        {
          GameObject gObj = (GameObject) iGameObj.getNext();
          System.out.println(gObj.toString());
        }
    }
    
    /** Prompts to exit the game. */
    public void exitGame()
    {
      if (Dialog.show("Exit", "Would you like to Exit??", "Ok", "Cancel"))
      {
         Display.getInstance().exitApplication();  
      }
    }
    
    /** Creates a dialog box that has some about information */
    public void about()
    {
      Dialog.show("About", "Colin Rice\n\nCSC 133\n\nVersion: 0.3", "Ok", null);
    }
    
    /** Displays a help box dialog */
    public void help()
    {
      String helpText = "Keys that can be used for playing Space Fights\n\n" +
                "e - Expand ship size\nc - Contract ship size\n" +
                "s - Open ship door\nr - Move ship right\nl - Move ship left\n" +
                "u - Move ship up\nd - Move ship down\no- Jump to random astronaut\n" +
                "a - Jump to random alien\nw - Create an alien\nf - Fight an alien\n" +
                "t - Tick the game clock\nx - Exit game";
      Dialog.show("Help", helpText, "Ok", null);
    }
    
    /** Exits game when there are no astronauts remaining. Displays final score. */
    private void noAstroRemain()
    {
      String finalScore = "Game over, no astronauts remain...\n" +
                "Final Score is...\n" +
                "Aliens snuck in: " + this.alienSneakedIn +
                "\nAstronauts rescued: " + this.astroRescued +
                "\nTotal points: " + this.score;
      Dialog.show("Game Over", finalScore, "Ok", null);
      Display.getInstance().exitApplication();       
    }
    
    /**
     * Returns the number of astronauts remaining.
     * 
     * @return number of astronauts
     */
    public int getAstroRemain()
    {
      return astroRemaining;
    }
    
    /**
     * Returns the number of aliens remaining.
     * 
     * @return number of aliens
     */
    public int getAlienRemain()
    {
      return alienRemaining;
    }
    
    /**
     * Returns the number of astronauts rescued.
     * 
     * @return number of astronauts rescued
     */
    public int getAstroRescue()
    {
      return astroRescued;
    }
    
    /**
     * Returns the number of aliens snuck onboard.
     * 
     * @return number of aliens
     */
    public int getAlienSnuckIn()
    {
      return alienSneakedIn;
    }
    
    /**
     * Returns the current score
     * 
     * @return score of the game
     */
    public int getScore()
    {
      return score;
    }
    
    /**
     * Returns whether the sound is on
     * 
     * @return state of sound
     */
    public boolean getSound()
    {
      return isSound;
    }
    
    /**
     * Sets the sound variable.
     * 
     * @param b true/false if sound is on
     */
    public void setSound(boolean b)
    {
      isSound = b;
      
      if(isSound && (isPaused == false))
      {
        bgSound.play();
      }
      else
      {
        bgSound.pause();
      }
      
        SideMenuBar.closeCurrentMenu();
      
      setChanged();
        notifyObservers(this);
    }
    
    /**
     * If game is being paused, disable sound but do not try to
     * close the context menu. Also handle the case where user turns on
     * sound while game is paused.
     * 
     * @param b true or false
     */
    public void setPauseSound(boolean b)
    { 	
      if(b)
      {
        isPaused = true;
        bgSound.pause();
      }
      else
      {
        if (isSound == false)
        {
          isPaused = false;
          return;
        }
        isPaused = false;
        bgSound.play();
      }
      
      setChanged();
        notifyObservers(this);
    }
    
    /**
     * Calls the passed astronauts heal method.
     * 
     * @param a astronaut to heal.
     */
    public void healAstronaut(Astronaut a)
    {   	
      a.heal();
    }
    
    /**
     * Returns whether the game is paused or not.
     * 
     * @return boolean
     */
    public boolean getIsPaused()
    {
      return isPaused;
    }
    
    /**
     * Sets the height of the game screen
     * 
     * @param y the height
     */
    public void setHeight(int y)
    {
      upperBoundY = y;
    }
    
    /**
     * Sets the width of the game screen
     * 
     * @param x the width
     */
    public void setWidth(int x)
    {
      upperBoundX = x;
    }
    
    /**
     * Returns the width of the MapView.
     * 
     * @return int
     */
    public int getWidth()
    {
      return upperBoundX;
    }
    
    /**
     * Returns the height of the MapView.
     * 
     * @return int
     */
    public int getHeight()
    {
      return upperBoundY;
    }
    
    /**
     * Returns the Game object collection.
     * 
     * @return GameObjectCollection
     */
    public GameObjectCollection getGameObjectCollection()
    {
      return this.gamePieces;
    }
    
    /**
     * Returns a collision sound.
     * 
     * @return Sound
     */
    public Sound getAlienAlienCollisionSound()
    {
        return alienAlienCollisionSound;
    }
    
    /**
     * Retuns a collision sound.
     * 
     * @return Sound
     */
    public Sound getAlienAstronautCollisionSound()
    {
        return alienAstronautCollisionSound;
    }
    
    /**
     * Returns the door open sound.
     * 
     * @return Sound
     */
    public Sound getDoorOpenSound()
    {
        return doorOpenSound;
    }
    
    /**
     * Sets the game object collection. Used when objects are added
     * from outside the game world.
     * 
     * @param g the game object collection
     */
    public void setGameObjectCollection(GameObjectCollection g)
    {
        this.gamePieces = g;
    }
}
