package nl.miystengine.client.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALCdevice;
import org.lwjgl.util.WaveData;

public class AudioMaster
{
	//https://www.youtube.com/watch?v=ZJ-Nq-os2YI
	private static List<Integer> bufferList = new ArrayList<Integer>();
	public static List<SourceArrayList> source = new ArrayList<SourceArrayList>();
	public static GameSoundList soundList;
		
	public static void init()
	{
		try 
		{
			AL.create();
		} 
		catch (LWJGLException e) 
		{
			e.printStackTrace();
		}
		soundList = new GameSoundList();
	}
	
	public static void setListenerData()
	{
		AL10.alListener3f(AL10.AL_POSITION, 0,0,0);
		AL10.alListener3f(AL10.AL_VELOCITY, 0,0,0);
	}
	
	public static void updateListenerPosition(float x,float y,float z)
	{
		AL10.alListener3f(AL10.AL_POSITION, x,y,z);
	}
	
	public static int loadSound(String file)
	{
		int buffer = AL10.alGenBuffers();
		bufferList.add(buffer);
		try 
		{
			WaveData waveFile = WaveData.create(new BufferedInputStream(new FileInputStream(new File(file))));
			AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
			waveFile.dispose();
		} 
		catch (FileNotFoundException e1) 
		{
			e1.printStackTrace();
		}
		return buffer;
	}
	
	public static int[] loadSound(String file,int howManySounds)
	{
		int[] buffers = new int[howManySounds];
		try
		{
			for(int i = 1;i < howManySounds;++i)
			{
				int buffer = AL10.alGenBuffers();
				buffers[i] = buffer;
				bufferList.add(buffer);
				try 
				{
					WaveData waveFile = WaveData.create(new BufferedInputStream(new FileInputStream(new File(file + i + ".wav"))));
					AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
					waveFile.dispose();
					System.out.println("Load Sounds: " + file + i + ".wav");
				} 
				catch (FileNotFoundException e1) 
				{
					e1.printStackTrace();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return buffers;
	}
	
	public static void cleanUpBuffers()
	{
		for(int buffer : bufferList)
		{
			AL10.alDeleteBuffers(buffer);
		}
	}
	
	public static void cleanUp()
	{
		for(int buffer : bufferList)
		{
			AL10.alDeleteBuffers(buffer);
		}
		AL.destroy();
	}
}