package com.mycompany.a3;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

/** Class for the Pause command*/
public class PauseCommand extends Command
{
  private Game target;
  
  public PauseCommand()
  {
    super("Pause");
  }
  
  /**
   * Sets the target object that this command will work on.
   * 
   * @param gw the referenced GameWorld object
   */
  public void setTarget(Game g)
  {
    target = g;
  }
  
  /** Pause the game timer. */
  @Override
  public void actionPerformed(ActionEvent ev)
  {
    if (target.getIsTimed())
    {
      target.pauseGameTimer();
    }
    else
    {
      target.startGameTimer();
    }
  }
}