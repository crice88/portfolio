package com.mycompany.a3;

import com.codename1.ui.CheckBox;
import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

/** Command class that fires off setSound method. */
public class SoundCommand extends Command
{
  private GameWorld target;
  
  public SoundCommand()
  {
    super("Sound?");
  }
  
  /**
   * Sets the target object that this command will work on.
   * 
   * @param gw the referenced GameWorld object
   */
  public void setTarget(GameWorld gw)
  {
    target = gw;
  }
  
  /**
   * Overrides extended actionPerformed method
   * 
   * @param ev the event to perform
   */
  @Override
  public void actionPerformed(ActionEvent ev)
  {
    if (((CheckBox)ev.getComponent()).isSelected())
    {
      target.setSound(true);
    }
    else
    {
      target.setSound(false);
    }
  }
}