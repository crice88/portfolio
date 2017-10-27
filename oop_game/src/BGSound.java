package com.mycompany.a3;

import java.io.InputStream;

import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Display;

/**
 * Encapsulates the background sound used in the game.
 */
public class BGSound implements Runnable
{
    private Media m;
    
    /**
     * Sets the passed filename as a new piece of media.
     * 
     * @param filename the name of the file to open
     */
    public BGSound(String fileName)
    {
        try
        {
            InputStream is = Display.getInstance().getResourceAsStream(getClass(), "/" + fileName);
            
            m = MediaManager.createMedia(is, "audio/wav", this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    // Pauses the music playing
    public void pause()
    {
        m.pause();
    }
    
    // Plays the music
    public void play()
    {
        m.play();
    }
    
    // Plays the music from the beginning
    public void run()
    {
        m.setTime(0);
        m.play();
    }
}