package com.mycompany.a3;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;

/** Command class that fires off heal method. */
public class HealCommand extends Command
{
	private GameWorld target;
	
	public HealCommand()
	{
		super("Heal");
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
		// Checks for which item is selected.
		GameObjectCollection gamePieces = target.getGameObjectCollection();
		IIterator iGameObj = gamePieces.getIterator();
		
		while(iGameObj.hasNext())
		{
			GameObject gObj = (GameObject) iGameObj.getNext();
			
			if (gObj instanceof ISelectable)
			{
				if (((ISelectable) gObj).isSelected())
				{
					target.healAstronaut((Astronaut) gObj);
				}
			}
		}
	}
}