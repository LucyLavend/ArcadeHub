package nl.miystengine.client.renderer.texture;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import nl.miystengine.client.FileBasicJava;
import nl.miystengine.client.MiystEngine;

public class GifRenderer
{
	private float gifSpeed;
	private float gif;
	private ImageFrame[] images;
	private String gifSource;
	
	public GifRenderer(String gifSource)
	{
		this.gifSource = gifSource;
		this.gifSpeed = 0.25F;
	}
	   
	public GifRenderer(String gifSource, float gifSpeed)
	{
		this.gifSource = gifSource;
		this.gifSpeed = gifSpeed;
	}
	
	public void renderGif()
	{
		if(images == null)
	   	{
		    File input = new File(FileBasicJava.source+"/" + this.gifSource + ".gif");  
		    try 
		   	{
		   		InputStream targetStream = new FileInputStream(input);	
		   		ImageFrame[] images = MiystEngine.miystengine.getTextureManager().gifReader.readGif(targetStream);
		   		this.images = images;
		   	} 
		   	catch (IOException e) 
		   	{
		   		e.printStackTrace();
		   	}
	   	}
	   	MiystEngine.miystengine.getTextureManager().bindTexture(FileBasicJava.source+"/" + this.gifSource + ".gif",this.images[(int)this.gif].getImage()); 
	}
	
	public void updateGif()
	{
		if(this.images != null)
		{
			if(this.gif < this.images.length - 1)
		   	{
		   		this.gif += this.gifSpeed;
		   	}
		   	else
		   	{
		   		this.gif = 0;
		   	}
		}
	}
}