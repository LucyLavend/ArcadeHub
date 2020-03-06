package nl.miystengine.client.audio;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioPlayer
{
	
	public void playSound(String soundFile) 
	{
		File f = new File(soundFile);
		try
		{
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());  
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}