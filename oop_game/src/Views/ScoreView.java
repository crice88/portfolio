package com.mycompany.a3;

import java.util.Observable;
import java.util.Observer;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Border;

/** 
 * Sets the score view container. Creates several labels and updates them 
 * when game world changes. Implements the Observer/Observable design pattern.
 */
public class ScoreView extends Container implements Observer 
{
	// Private class variables used so that update can use them as well
	private Label astroRemainValLabel;
	private Label alienRemainValLabel;
	private Label alienSnuckInValLabel;
	private Label astroRescueValLabel;
	private Label soundValLabel;
	private Label scoreValLabel;
	
	public ScoreView()
	{
		Label astroRemainLabel;
		Label alienRemainLabel;
		Label alienSnuckInLabel;
		Label astroRescueLabel;
		Label soundLabel;
		Label scoreLabel;
		
		this.setLayout(new BoxLayout(BoxLayout.X_AXIS));
		this.getAllStyles().setBorder(Border.createLineBorder(1, ColorUtil.BLACK));
		
		astroRemainLabel = new Label("Astronauts Remain:");
		astroRemainLabel.getAllStyles().setPadding(LEFT, 5);
		astroRemainLabel.getAllStyles().setFgColor(ColorUtil.BLUE);
		this.add(astroRemainLabel);
		astroRemainValLabel = new Label();
		astroRemainValLabel.getAllStyles().setPadding(LEFT, 3);
		astroRemainValLabel.getAllStyles().setPadding(RIGHT, 3);
		astroRemainValLabel.getAllStyles().setFgColor(ColorUtil.BLUE);
		this.add(astroRemainValLabel);
		
		alienRemainLabel = new Label("Aliens Remain:");
		alienRemainLabel.getAllStyles().setFgColor(ColorUtil.BLUE);
		this.add(alienRemainLabel);
		alienRemainValLabel = new Label();
		alienRemainValLabel.getAllStyles().setPadding(LEFT, 3);
		alienRemainValLabel.getAllStyles().setPadding(RIGHT, 3);
		alienRemainValLabel.getAllStyles().setFgColor(ColorUtil.BLUE);
		this.add(alienRemainValLabel);
		
		alienSnuckInLabel = new Label("Aliens Snuck In:");
		alienSnuckInLabel.getAllStyles().setFgColor(ColorUtil.BLUE);
		this.add(alienSnuckInLabel);	
		alienSnuckInValLabel = new Label();
		alienSnuckInValLabel.getAllStyles().setPadding(LEFT, 3);
		alienSnuckInValLabel.getAllStyles().setPadding(RIGHT, 3);
		alienSnuckInValLabel.getAllStyles().setFgColor(ColorUtil.BLUE);
		this.add(alienSnuckInValLabel);
		
		astroRescueLabel = new Label("Astronauts Rescued:");
		astroRescueLabel.getAllStyles().setFgColor(ColorUtil.BLUE);
		this.add(astroRescueLabel);
		astroRescueValLabel = new Label();
		astroRescueValLabel.getAllStyles().setPadding(LEFT, 3);
		astroRescueValLabel.getAllStyles().setPadding(RIGHT, 3);
		astroRescueValLabel.getAllStyles().setFgColor(ColorUtil.BLUE);
		this.add(astroRescueValLabel);
		
		soundLabel = new Label("Sound:");
		soundLabel.getAllStyles().setFgColor(ColorUtil.BLUE);
		this.add(soundLabel);
		soundValLabel = new Label();
		soundValLabel.getAllStyles().setPadding(LEFT, 4);
		soundValLabel.getAllStyles().setPadding(RIGHT, 4);
		soundValLabel.getAllStyles().setFgColor(ColorUtil.BLUE);
		this.add(soundValLabel);
		
		scoreLabel = new Label("Score:");
		scoreLabel.getAllStyles().setFgColor(ColorUtil.BLUE);
		this.add(scoreLabel);
		scoreValLabel = new Label();
		scoreValLabel.getAllStyles().setPadding(LEFT, 5);
		scoreValLabel.getAllStyles().setPadding(RIGHT, 5);
		scoreValLabel.getAllStyles().setFgColor(ColorUtil.BLUE);
		this.add(scoreValLabel);
	}
	
	// Change text when something changes in the game world. Make sure to repaint labels.
	public void update(Observable o, Object arg)
	{
		GameWorld gw = (GameWorld) arg;
		
		astroRemainValLabel.setText(Integer.toString(gw.getAstroRemain()));
		alienRemainValLabel.setText(Integer.toString(gw.getAlienRemain()));
		alienSnuckInValLabel.setText(Integer.toString(gw.getAlienSnuckIn()));
		astroRescueValLabel.setText(Integer.toString(gw.getAstroRescue()));
		
		if (gw.getSound())
			soundValLabel.setText("On");
		else
			soundValLabel.setText("Off");
		
		scoreValLabel.setText(Integer.toString(gw.getScore()));
		
		this.repaint();
	}
}