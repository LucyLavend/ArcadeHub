package nl.miystengine.client.audio;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import nl.miystengine.client.MiystEngine;

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
	
	//Play audio example
	//MiystEngine.miystengine.getAudioPlayer().playSound("../sound/Walking_Through_Bush.wav");
}