package com.mycompany.a3;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

/** Command class that fires off expandShipSize method. */
public class ExpandDoorCommand extends Command
{
	GameWorld target;
	
	public ExpandDoorCommand()
	{
		super("Expand");
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
		target.expandShipSize();
	}
}