package com.mycompany.a3;

/** Interface for each collision class*/
public interface ICollider
{
    public boolean collidesWith(ICollider otherObject);
    public void handleCollision(ICollider otherObject, GameWorld gw);
}