package com.mycompany.a3;

import java.io.InputStream;

import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Display;

/** Class encapsulates each game sound effect */
public class Sound
{
    private Media m;
    
    /**
     * Sets the passed filename as a new piece of media.
     * 
     * @param filename the name of the file to open
     */
    public Sound(String fileName)
    {
        try
        {
            InputStream is = Display.getInstance().getResourceAsStream(getClass(), "/" + fileName);
            
            m = MediaManager.createMedia(is,  "audio/wav");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    // Plays the sound effect.
    public void play()
    {
        m.setTime(0);
        m.play();
    }
}