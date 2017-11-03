package com.mycompany.a3;

import java.util.Observable;
import java.util.Observer;

import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

/** 
 * Map view that displays the current game information. Called when something
 * in the game world has changed. Implements the observer/observable design pattern.
 */
public class MapView extends Container implements Observer
{
  private GameWorld gw;
  private Point p = new Point(this.getX(), this.getY());
  
  // Implements abstract method
  public void update(Observable o, Object arg)
  {
    gw = (GameWorld) arg;
    
    p.setX(this.getX());
    p.setY(this.getY());
    
    gw.printMap();
    repaint();
  }
  
  /**
   * Paint each drawable object.
   * 
   * @param g the graphics object to pass
   */
  public void paint(Graphics g)
  {
    super.paint(g);
    
    GameObjectCollection gamePieces = gw.getGameObjectCollection();
    IIterator iGameObj = gamePieces.getIterator();
    
    while(iGameObj.hasNext())
    {
      GameObject gObj = (GameObject) iGameObj.getNext();
      
      if (gObj instanceof Alien)
      {
        Alien a = (Alien) gObj;
        a.draw(g, p);
      }
      else if (gObj instanceof Astronaut)
      {
        Astronaut a = (Astronaut) gObj;
        a.draw(g, p);
      }
      else
      {
        Spaceship s = (Spaceship) gObj;
        s.draw(g, p);
      }
    }
  }
  
  /**
   * Called when a user clicks within the MapView.
   * 
   * @param x the x location of the click
   * @param y the y location of the click
   */
  public void pointerPressed(int x, int y)
  {
    x = x - getParent().getAbsoluteX();
    y = y - getParent().getAbsoluteY();
    
    Point pPtrRelPrnt = new Point(x, y);
    Point pCmpRelPrnt = new Point(getX(), getY());
    
    GameObjectCollection gamePieces = gw.getGameObjectCollection();
    IIterator iGameObj = gamePieces.getIterator();
    
    while(iGameObj.hasNext())
    {
      GameObject gObj = (GameObject) iGameObj.getNext();
      
      if (gObj instanceof ISelectable)
      {
        if (((ISelectable) gObj).contains(pPtrRelPrnt, pCmpRelPrnt))
        {
          if (gw.getIsPaused())
          {
            ((ISelectable) gObj).setSelectable(true);
          }
        }
        else
        {
          ((ISelectable) gObj).setSelectable(false);
        }
      }
    }
    repaint();
  }
  
  /**
   * Unselects all selectable objects. 
   */
  public void unSelectObjects()
  {
    GameObjectCollection gamePieces = gw.getGameObjectCollection();
    IIterator iGameObj = gamePieces.getIterator();
    
    while(iGameObj.hasNext())
    {
      GameObject gObj = (GameObject) iGameObj.getNext();
      
      if (gObj instanceof ISelectable)
      {
        ((ISelectable) gObj).setSelectable(false);
      }
    }
    repaint();
  }
}