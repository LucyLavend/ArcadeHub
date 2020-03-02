package nl.miystengine.client.audio;

import java.util.Random;

import nl.miystengine.client.MiystEngine;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;

public class Source
{
	private int sourceID;
	private boolean hasDistance = false;
	private boolean wasPlaying = false;
	private int IDinArrayList = 0;
	private float x,y,z;
	private float volume;
	public static boolean stopPlayingSounds = false;
	
	public Source()
	{
		sourceID = AL10.alGenSources();
		AudioMaster.source.add(new SourceArrayList(this));
		this.IDinArrayList = AudioMaster.source.size() - 1;
	}
	
	public Source(boolean hasDistance)
	{
		sourceID = AL10.alGenSources();
		if(hasDistance)
		{
			AL10.alDistanceModel(AL11.AL_EXPONENT_DISTANCE);
			this.hasDistance = hasDistance;
		}
		AudioMaster.source.add(new SourceArrayList(this));
	}
	
	/**
	 * @param rollOff: How steep the curve is
	 * @param distanceRef: Where the sound will be fading
	 * @param maxDistance: Maximal distance the sound can be heard
	 */
	public void setSoundDistance(float rollOff,float distanceRef,float maxDistance)
	{
		AL10.alSourcef(sourceID, AL10.AL_ROLLOFF_FACTOR, rollOff);
		AL10.alSourcef(sourceID, AL10.AL_REFERENCE_DISTANCE, distanceRef);
		AL10.alSourcef(sourceID, AL10.AL_MAX_DISTANCE, maxDistance);
	}
	
	public boolean getDistanceFadeOut()
	{
		return this.hasDistance;
	}
	
   /**
	* Must be between 100 - 0
	* @param volume
	*/
	public void setVolume(float volume)
	{
		if(!this.stopPlayingSounds)
		{
			this.volume = volume;
			AL10.alSourcef(sourceID, AL10.AL_GAIN, volume / 100F);
		}
	}
	
	public float getVolume()
	{
		if(!this.stopPlayingSounds)
		{
			return this.volume;
		}
		else return 0;
	}
	
	/**
	* Must be between 100 - 0
	* @param pitch
	*/
	public void setPitch(float pitch)
	{
		if(!this.stopPlayingSounds)
		{
			AL10.alSourcef(sourceID, AL10.AL_PITCH, pitch / 100F);
		}
	}
	
	public void setVelocity(float x,float y,float z)
	{
		if(!this.stopPlayingSounds)
		{
			AL10.alSource3f(sourceID, AL10.AL_VELOCITY,x ,y ,z);
		}
	}
	
	public void setLoop(boolean loop)
	{
		if(!this.stopPlayingSounds)
		{
			AL10.alSourcef(sourceID, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
		}
	}
	
	public boolean isPlaying()
	{
		if(!this.stopPlayingSounds)
		{
			return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
		}
		else return false;
	}
	
	public void pause()
	{
		if(!this.stopPlayingSounds)
		{
			AL10.alSourcePause(sourceID);
		}
	}
	
	public void continuePlaying()
	{
		if(!this.stopPlayingSounds)
		{
			AL10.alSourcePlay(sourceID);
		}
	}
	
	public void stop()
	{
		AL10.alSourceStop(sourceID);	
	}
	
	/**
	* Must be between 100 - 0
	* @param pitch
	*/
	public void setPosition(float x,float y,float z)
	{
		if(!this.stopPlayingSounds)
		{
			AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}
	
	/**
	* Must be between 100 - 0
	* The x, y, z of the Source wont be saved and still keeps the origin Coords
	* @param pitch
	*/
	public void setPosition2(float x,float y,float z)
	{
		if(!this.stopPlayingSounds)
		{
			AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
		}
	}
	
	/**
	 * Get X position
	 * @return
	 */
	public float getX()
	{
		return x;
	}
	
	/**
	 * Get X position
	 * @return
	 */
	public float getY()
	{
		return y;
	}
	
	/**
	 * Get Z position
	 * @return
	 */
	public float getZ()
	{
		return z;
	}
	
	public float getDistance(float playerPositionX, float playerPositionZ)
	{
		float xFinal = playerPositionX - x;
		float zFinal = playerPositionZ - z;
		return xFinal + zFinal;
	}
	
	public void playSound(int buffer)
	{
		stop();
		if(!this.stopPlayingSounds)
		{
			AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
			continuePlaying();
			this.wasPlaying = true;
			int error = AL10.alGetError();

			if(error != 40961 && error != 0)
			{
				System.out.println("Audio Error: " + error);
			}
			if(error == 40965)
			{
				System.out.println("Audio Error: memory full, " + error);
			}
		}
	}
	
	public boolean getWasPlaying()
	{
		if(!this.stopPlayingSounds)
		{
			if(this.wasPlaying && !this.isPlaying())
			{
				this.wasPlaying = false;
				this.delete();
				if(AudioMaster.source.size() > this.IDinArrayList)
				{
					AudioMaster.source.remove(this.IDinArrayList);
				}
			}
			return this.wasPlaying;
		}
		else return false;
	}
	
	public void playSoundAt(int buffer,float x,float y,float z)
	{
		if(!this.stopPlayingSounds)
		{
			AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
			AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
			AL10.alSourcePlay(sourceID);
		}
	}

	public void delete()
	{
		stop();
		AL10.alDeleteSources(sourceID);
	}
	
	//Play audio attracts enemy
	//https://www.soundsnap.com/tags/reload?page=3
	
	/**
	 * distanceHeard:
	 * 1 is Silenced pistol
	 * 2 is silenced Assault Rifle
	 * 3 is pistol
	 * 4 is Assault Rifle, PDW
	 * 5 is Light Machine Gun
	 * 6 is Sniper Rifle
	 */
	public void playGunSound(int bufferID, int distanceHeard,float x,float y,float z)
	{
		if(distanceHeard ==1)
		{
			this.setSoundDistance(1F, 2F, 4F);
		}
		if(distanceHeard == 2)
		{
			this.setSoundDistance(2F, 3F, 5F);
		}
		if(distanceHeard == 3)
		{
			this.setSoundDistance(4F, 9F, 14F);
		}
		if(distanceHeard == 4)
		{
			this.setSoundDistance(5F, 11F, 18F);
		}
		if(distanceHeard == 5)
		{
			this.setSoundDistance(9F, 19F, 30F);
		}
		if(distanceHeard == 6)
		{
			this.setSoundDistance(12F, 23F, 35F);
		}
		this.setVolume(80F + ((float)MiystEngine.miystengine.rand.nextInt(20)));
		this.setPitch(90F + ((float)MiystEngine.miystengine.rand.nextInt(10)));
		AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
		this.playSound(bufferID);
	}
}