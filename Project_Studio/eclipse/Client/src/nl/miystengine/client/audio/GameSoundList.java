package nl.miystengine.client.audio;

import java.util.Random;
import org.lwjgl.util.vector.Vector3f;
import nl.miystengine.client.FileBasicJava;
import nl.miystengine.client.MiystEngine;

public class GameSoundList
{
	//Sounds
	private int[] buffer;
	private int bufferHitMetal;
	private int bufferHitMetal3;
	private int bufferGas;
	private int bufferHitWood;
    private int start;
    private int fly;
	private int startMAV;
	private int flyMAV;
	private int bufferDestruction;
	private int bufferShootPistol;
	private int bufferEmptyClipPistol;

	
	//End
	
	public GameSoundList()
	{
		buffer = AudioMaster.loadSound(FileBasicJava.sources + "sound/Vegetation/Walking_Through_Bush_",4);
		bufferHitMetal = AudioMaster.loadSound(FileBasicJava.sources + "sound/Hit Sound/Bullet_Hit_Metal_1.wav");
		bufferHitMetal3 = AudioMaster.loadSound(FileBasicJava.sources + "sound/Hit Sound/Bullet_Hit_Metal_3.wav");
		bufferGas = AudioMaster.loadSound(FileBasicJava.sources + "sound/Industry/Escaping_Gas_2.wav");	
		bufferHitWood = AudioMaster.loadSound(FileBasicJava.sources + "sound/Hit Sound/Bullet_Hit_Wood.wav");
		start = AudioMaster.loadSound(FileBasicJava.sources + "sound/MKV/MKV_Starting.wav");
		fly = AudioMaster.loadSound(FileBasicJava.sources + "sound/MKV/MKV_Flying.wav");
		startMAV = AudioMaster.loadSound(FileBasicJava.sources + "sound/T_Hawk/T_Hawk_Start.wav");
		flyMAV = AudioMaster.loadSound(FileBasicJava.sources + "sound/T_Hawk/T_Hawk_Fly.wav");
		bufferDestruction = AudioMaster.loadSound(FileBasicJava.sources + "sound/Industry/Stone_Destruction.wav");
		bufferShootPistol = AudioMaster.loadSound(FileBasicJava.sources + "sound/Trock_18/Trock_18_Shoot.wav");
		bufferEmptyClipPistol = AudioMaster.loadSound(FileBasicJava.sources + "sound/Trock_18/Trock_18_Clip_Empty.wav");		
	}
	
	public void playWalkPlantSound(Vector3f position)
	{
		Source source = new Source(true);
		source.setPosition(position.x, position.y, position.z);
		source.setSoundDistance(5F, 7F, 9F);
    	source.setVolume(((float)MiystEngine.miystengine.rand.nextInt(10)) + 20F); 
    	source.setPitch(((float)MiystEngine.miystengine.rand.nextInt(20)) + 90F); 
		source.setVolume(90F);
		int leaves = MiystEngine.miystengine.rand.nextInt(2) + 1;
		source.playSound(buffer[leaves]);
	}
	
	public void playHitMetalSound(Vector3f position)
	{
		Source source = new Source(true);
		source.setPosition(position.x, position.y, position.z);
    	source.setSoundDistance(5F, 7F, 19F);
    	source.setVolume(((float)MiystEngine.miystengine.rand.nextInt(10)) + 20F); 
    	source.setPitch(((float)MiystEngine.miystengine.rand.nextInt(20)) + 40F); 
		source.setVolume(90F);
		int metalSound = MiystEngine.miystengine.rand.nextInt(1) + 1;
		if(metalSound == 1)
		{
			source.playSound(bufferHitMetal);
		}
		else
		{
			source.playSound(bufferHitMetal3);
		}
	}
	
	public void playHitWoodSound(Vector3f position)
	{
		Source source = new Source(true);
		source.setPosition(position.x, position.y, position.z);
    	source.setSoundDistance(5F, 7F, 19F);
    	source.setVolume(((float)MiystEngine.miystengine.rand.nextInt(10)) + 20F); 
    	source.setPitch(((float)MiystEngine.miystengine.rand.nextInt(20)) + 40F); 
		source.setVolume(90F);
		source.playSound(bufferHitWood);
	}
	
	public Source playGasSound(Vector3f position)
	{
		Source gasSource = new Source(true);
		gasSource.setPosition(position.x,position.y,position.z);
		gasSource.setSoundDistance(2F, 3F, 4F);
		gasSource.setVolume(((float)MiystEngine.miystengine.rand.nextInt(2)) + 1F); 
		gasSource.playSound(bufferGas);
		return gasSource;
	}
	
	public Source playMKVStartSound(Vector3f position)
	{
		Source source = new Source(true);
		source.setPosition(position.x,position.y,position.z);
        source.setSoundDistance(7F, 18F, 29F);
        source.setVolume(2F);
        source.setPitch(80F);
        source.playSound(start);
        return source;
	}
	
	public Source playMKVFlySound(Vector3f position)
	{
		Source source = new Source(true);
		source.setPosition(position.x,position.y,position.z);
		source.setLoop(true);
        source.setSoundDistance(7F, 18F, 29F);
        source.setVolume(2F);
        source.setPitch(80F);
        source.playSound(fly);
        return source;
	}
	
	public Source playMAVStartSound(Vector3f position)
	{
		Source source = new Source(true);
		source.setPosition(position.x,position.y,position.z);
        source.setSoundDistance(7F, 18F, 29F);
        source.setVolume(2F);
        source.setPitch(80F);
        source.playSound(startMAV);
        return source;
	}
	
	public Source playMAVFlySound(Vector3f position)
	{
		Source source = new Source(true);
		source.setLoop(true);
		source.setPosition(position.x,position.y,position.z);
        source.setSoundDistance(7F, 18F, 29F);
        source.setVolume(2F);
        source.setPitch(80F);
        source.playSound(flyMAV);
        return source;
	}
	
	public Source playDestructionSound(Vector3f position)
	{
		Source source = new Source(true);
		source = new Source(true);
		source.setPosition(position.x,position.y,position.z);
		source.setSoundDistance(5F, 7F, 19F);
		source.setVolume(((float)MiystEngine.miystengine.rand.nextInt(10)) + 40F); 
		source.setPitch(((float)MiystEngine.miystengine.rand.nextInt(10)) + 40F); 
		source.playSound(bufferDestruction);
        return source;
	}	

	public Source playPistolShootSound(Vector3f position)
	{
		Source source = new Source(true);
		source = new Source(true);
		source.setPosition(position.x,position.y,position.z);
		source.setSoundDistance(4F, 15F, 30F);
	    source.setVolume(40F + ((float)MiystEngine.miystengine.rand.nextInt(20)));
	    source.setPitch(95F + ((float)MiystEngine.miystengine.rand.nextInt(5)));
		source.playSound(bufferShootPistol);
        return source;
	}
	
	public Source playPistolEmptySound(Vector3f position)
	{
		Source source = new Source(true);
		source = new Source(true);
		source.setPosition(position.x,position.y,position.z);
		source.setSoundDistance(4F, 15F, 30F);
	    source.setVolume(40F + ((float)MiystEngine.miystengine.rand.nextInt(20)));
	    source.setPitch(95F + ((float)MiystEngine.miystengine.rand.nextInt(5)));
		source.playSound(bufferEmptyClipPistol);
        return source;
	}

}