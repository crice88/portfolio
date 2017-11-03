package com.mycompany.a3;

import java.util.ArrayList;

/** 
 * Class that follows the Collection design pattern. Contains the 
 * inner class GameObjectIterator. Classes implement ICollection and 
 * IIterator.
 */
public class GameObjectCollection implements ICollection
{
    private ArrayList<GameObject> gamePieces;
    
    public GameObjectCollection()
    {
    	this.gamePieces = new ArrayList<GameObject>();
    }
    
    /**
     * Adds an object to the collection.
     * 
     * @param obj the selected object to add
     */
	public void add(Object obj)
	{
		this.gamePieces.add((GameObject) obj);
	}
	
	/**
	 * Assigns an iterator to the GameObjectCollection.
	 * 
	 *  @return GameObjectIterator object
	 */
	public IIterator getIterator()
	{
		return new GameObjectIterator();
	}
	
	/** 
	 * Inner class used to not expose structure to other
	 * classes. 
	 */
	private class GameObjectIterator implements IIterator
	{
		private int currIndex;
		
		public GameObjectIterator()
		{
			currIndex = -1;
		}
		
		/**
		 * Checks if there is another object in the collection.
		 * 
		 * @return a boolean result
		 */
		public boolean hasNext()
		{
			if (gamePieces.size() <= 0)
			{
				return false;
			}
			
			if (currIndex == gamePieces.size() - 1)
			{
				return false;
			}
			
			return true;
		}
		
		/**
		 * Returns the next object in the collection.
		 * 
		 * @return a GameObject from the collection
		 */
		public GameObject getNext()
		{
			currIndex++;
			return(gamePieces.get(currIndex));
		}
		
		/**
		 * Removes the current object that the iterator is pointing to. 
		 */
		public void remove()
		{
			gamePieces.remove(currIndex);
			currIndex--;
		}
	}
}