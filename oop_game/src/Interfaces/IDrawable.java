package com.mycompany.a3;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

/** Interface for each drawable object */
public interface IDrawable
{
	public void draw(Graphics g, Point pCmpRelPrnt);
}