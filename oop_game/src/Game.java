/**
 * Entry point for Space Fights game (Model). Class is instantiated by the Starter
 * class.
 * 
 * @author Colin Rice
 * 
 * CSC 133 Assignment 2
 * Dr. Muyan
 * October 24th, 2016
 */
package com.mycompany.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.util.UITimer;

/** 
 * The model of game. Extends from form. Sets up all containers
 * used in the game. Attaches commands to each button and sets some
 * default styling.
 */
public class Game extends Form implements Runnable
{
    // Objects
    private GameWorld gw;
    private MapView mv;
    private ScoreView sv;
    private Container eastContainer;
    private Container southContainer;
    private Container westContainer;
    private UITimer timer;
    private boolean isTimed;
    private ButtonStyle buttonTwoS;
    private ButtonStyle buttonOneS;
    private ButtonStyle buttonOneE;
    private ButtonStyle buttonTwoE;
    private ButtonStyle buttonThreeE;
    private ButtonStyle buttonFourE;
    private ButtonStyle buttonFiveE;
    private ButtonStyle buttonOneW;
    private ButtonStyle buttonTwoW;
    private ButtonStyle buttonThreeW;
    private ButtonStyle buttonFourW;
    private ContractDoorCommand contractDoor;
    private ExpandDoorCommand expand;
    private HealCommand heal;
    private MoveDownCommand moveD;
    private MoveUpCommand moveU;
    private MoveLeftCommand moveL;
    private MoveRightCommand moveR;
    private OpenDoorCommand open;
    private RandLocAlienCommand randAlienLoc;
    private RandLocAstroCommand randAstroLoc;

    private final int WAIT_TIME = 20;
    
    /** 
     * Constructor that will create visual game board.
     */
    public Game()
    {
        gw = new GameWorld();
        mv = new MapView();
        sv = new ScoreView();
        eastContainer = new Container();
        westContainer = new Container();
        southContainer = new Container();
        
        // Add both views as observers
        gw.addObserver(mv);
        gw.addObserver(sv);
        
        // Add a timer to application
        timer = new UITimer(this);
        
        // Sets overall program layout
        this.setLayout(new BorderLayout());        
        
        // Toolbar layout and commands
        Toolbar myToolbar = new Toolbar();
        this.setToolbar(myToolbar);
        myToolbar.setTitle("Space Fights Game");
        
        SoundCommand sound = new SoundCommand();
        CheckBox soundCheck = new CheckBox();
        soundCheck.getAllStyles().setBgTransparency(255);
        soundCheck.getAllStyles().setBgColor(ColorUtil.LTGRAY);
        soundCheck.getAllStyles().setFgColor(ColorUtil.BLACK);
        soundCheck.getAllStyles().setPaddingTop(2);
        soundCheck.getAllStyles().setPaddingBottom(2);
        soundCheck.setSelected(true);
        soundCheck.setCommand(sound);
        sound.setTarget(gw);
        myToolbar.addComponentToSideMenu(soundCheck);
        
        open = new OpenDoorCommand();
        open.setTarget(gw);
        myToolbar.addCommandToSideMenu(open);
        
        AboutCommand about = new AboutCommand();
        about.setTarget(gw);
        myToolbar.addCommandToSideMenu(about);
        
        ExitCommand exit = new ExitCommand();
        exit.setTarget(gw);
        myToolbar.addCommandToSideMenu(exit);
        
        HelpCommand help = new HelpCommand();
        help.setTarget(gw);
        myToolbar.addCommandToRightBar(help);
        
        this.setTitle("Space Fights Game");
        
        this.add(BorderLayout.NORTH, sv);
		
        // Sets empty center container
        mv.getAllStyles().setBorder(Border.createLineBorder(1, ColorUtil.BLACK));
        this.add(BorderLayout.CENTER, mv);
        
        // Adding East container and commands
        eastContainer.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        eastContainer.getAllStyles().setBorder(Border.createLineBorder(1, ColorUtil.BLACK));
        eastContainer.getAllStyles().setPadding(TOP, 100);
        eastContainer.getAllStyles().setPadding(BOTTOM, 75);
        
        buttonOneE = new ButtonStyle();
        contractDoor = new ContractDoorCommand();        
        contractDoor.setTarget(gw);
        buttonOneE.setCommand(contractDoor);
        eastContainer.add(buttonOneE);
        
        buttonTwoE = new ButtonStyle();
        moveD = new MoveDownCommand();
        moveD.setTarget(gw);
        buttonTwoE.setCommand(moveD);
        eastContainer.add(buttonTwoE);
        
        buttonThreeE = new ButtonStyle();
        moveR = new MoveRightCommand();
        moveR.setTarget(gw);
        buttonThreeE.setCommand(moveR);
        eastContainer.add(buttonThreeE);
        
        buttonFourE = new ButtonStyle();
        randAlienLoc = new RandLocAlienCommand();
        randAlienLoc.setTarget(gw);
        buttonFourE.setCommand(randAlienLoc);
        eastContainer.add(buttonFourE);
        
        buttonFiveE = new ButtonStyle();
        buttonFiveE.setCommand(open);
        eastContainer.add(buttonFiveE);
        
        this.add(BorderLayout.EAST, eastContainer);
        
        // Adding West container and commands
        westContainer.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        westContainer.getAllStyles().setBorder(Border.createLineBorder(1, ColorUtil.BLACK));
        westContainer.getAllStyles().setPadding(TOP, 100);
        westContainer.getAllStyles().setPadding(BOTTOM, 100);
        
        buttonOneW = new ButtonStyle();
        expand = new ExpandDoorCommand();
        expand.setTarget(gw);
        buttonOneW.setCommand(expand);
        westContainer.add(buttonOneW);
        
        buttonTwoW = new ButtonStyle();
        moveU = new MoveUpCommand();
        moveU.setTarget(gw);
        buttonTwoW.setCommand(moveU);
        westContainer.add(buttonTwoW);
        
        buttonThreeW = new ButtonStyle();
        moveL = new MoveLeftCommand();
        moveL.setTarget(gw);
        buttonThreeW.setCommand(moveL);
        westContainer.add(buttonThreeW);
        
        buttonFourW = new ButtonStyle();
        randAstroLoc = new RandLocAstroCommand();
        randAstroLoc.setTarget(gw);
        buttonFourW.setCommand(randAstroLoc);
        westContainer.add(buttonFourW);
        
        this.add(BorderLayout.WEST, westContainer);
        
        // Adding South container and commands
        southContainer.setLayout(new FlowLayout());
        ((FlowLayout) southContainer.getLayout()).setAlign(CENTER);
        southContainer.getAllStyles().setBorder(Border.createLineBorder(1, ColorUtil.BLACK));
        
        buttonOneS = new ButtonStyle();
        heal = new HealCommand();
        heal.setTarget(gw);
        buttonOneS.setCommand(heal);
        southContainer.add(buttonOneS);
        
        buttonTwoS = new ButtonStyle();
        PauseCommand p = new PauseCommand();
        p.setTarget(this);
        buttonTwoS.setCommand(p);
        southContainer.add(buttonTwoS);
        
        this.add(BorderLayout.SOUTH, southContainer);
        
        // Add key listeners that will always be enabled
        this.addKeyListener('x', exit);
        
        // Show GUI
        this.show();
        
        // Sets height and width game values by querying center container
        gw.setHeight(mv.getHeight());
        gw.setWidth(mv.getWidth());
        
        // Create game objects and default game attributes
        gw.init();
        
        // Start the game timer
        this.startGameTimer();
        isTimed = true;
    }
    
    // Starts a thread that ticks the game clock
    public void run()
    {
    	gw.tickClock(WAIT_TIME);
    }
    
    // Cancels the game timer and sets command inactive
    public void pauseGameTimer()
    {
    	buttonTwoS.setText("Play");
    	isTimed = false;
    	timer.cancel();
    	this.setCommandsActive(false);
    	gw.setPauseSound(true);
    }
    
    // Starts the game timer and sets commands to active
    public void startGameTimer()
    {
    	buttonTwoS.setText("Pause");
    	isTimed = true;
    	timer.schedule(WAIT_TIME, true, this);
    	this.setCommandsActive(true);
    	mv.unSelectObjects();
    	gw.setPauseSound(false);
    }
    
    /**
     * Returns whether the game is being timed.
     * 
     * @return boolean
     */
    public boolean getIsTimed()
    {
    	return isTimed;
    }
    
    /**
     * Sets each command to active
     * 
     * @param b true or false
     */
    public void setCommandsActive(boolean b)
    {
    	if (b)
    	{
    		this.addKeyListener('e', expand);
            this.addKeyListener('c', contractDoor);
            this.addKeyListener('s', open);
            this.addKeyListener('r', moveR);
            this.addKeyListener('l', moveL);
            this.addKeyListener('u', moveU);
            this.addKeyListener('d', moveD);
            this.addKeyListener('o', randAstroLoc);
            this.addKeyListener('a', randAlienLoc);
            buttonOneS.setEnabled(false);
            buttonTwoS.setEnabled(true);
            buttonOneW.setEnabled(true);
            buttonTwoW.setEnabled(true);
            buttonThreeW.setEnabled(true);
            buttonFourW.setEnabled(true);
            buttonOneE.setEnabled(true);
            buttonTwoE.setEnabled(true);
            buttonThreeE.setEnabled(true);
            buttonFourE.setEnabled(true);
            buttonFiveE.setEnabled(true);
            open.setEnabled(true);
    	}
    	else
    	{
    		this.removeKeyListener('e', expand);
            this.removeKeyListener('c', contractDoor);
            this.removeKeyListener('s', open);
            this.removeKeyListener('r', moveR);
            this.removeKeyListener('l', moveL);
            this.removeKeyListener('u', moveU);
            this.removeKeyListener('d', moveD);
            this.removeKeyListener('o', randAstroLoc);
            this.removeKeyListener('a', randAlienLoc);
            buttonOneS.setEnabled(true);
            buttonOneW.setEnabled(false);
            buttonTwoW.setEnabled(false);
            buttonThreeW.setEnabled(false);
            buttonFourW.setEnabled(false);
            buttonOneE.setEnabled(false);
            buttonTwoE.setEnabled(false);
            buttonThreeE.setEnabled(false);
            buttonFourE.setEnabled(false);
            buttonFiveE.setEnabled(false);
            open.setEnabled(false);
    	}
    }
}
