package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.util.vector.Vector3f;
import nl.miystengine.client.FileBasicJava;
import nl.miystengine.client.MiystEngine;

public class GameMain
{	
	/**
	 * Will be called when the Constructor of the Miyst Engine
	 */
	public GameMain(){}
	
	private static String logo;
	
	/**
	 * Set the icon of the window
	 * @throws IOException
	 */
	public static ByteBuffer setIcon() throws IOException 
    {
		try
		{
			logo = MiystEngine.miystengine.getPath().source + "Logo blue.png";
			File file = new File(logo);
			if(!file.exists())
			{
				System.out.println("Logo File doesn't exists: " + file.getPath());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    	return MiystEngine.miystengine.getTextureManager().convertImageData(logo);
    }
	
	public String displayName()
	{
		return "test framework";
	}
	
	public String Version()
	{
		return "V1.0.0";
	}
	
	/**
	 * Called just before you enter the world but after everything else is loaded.
	 * Can be used to add the body parts of the player
	 */
	public void loadingBeforeEntering(){}
	
	/**
	 * Called After creation of the Framebuffer
	 */
	public void activatedOnStart(){}

	/**
	 * Called when in Main menu, loading screen or in-game
	 */
	public void updateOnDeltaTime(){}
	
	/**
	 * Called after rendering Entity, Water and Terrain
	 */
	public void updateAfterRenderering(){}
	
	/**
	 * Will activate when loading screen is showing but only in the first part,before everything is loaded
	 * @param loadingTimer, Min 5 and Max 31
	 * @return int loading timer next
	 */
	public String loadingScreenBegin(int loadingTimer)
	{
		return "";
	}
	
	
	/**
	 * Will activate when loading screen is showing but only at the part where terrain textures should load
	 * @param loadingTimer, Min 86 and Max 109
	 * @return int loading timer next
	 */
	public String loadingScreenTerrain(int loadingTimer)
	{
		int loading = loadingTimer;
		return "";
	}
	
	/**
	 * Called just before you enter the world
	 */
	public void updateOnEnterWorld(){}

}