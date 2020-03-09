package nl.miystengine.client.renderer.texture;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;

import nl.miystengine.client.FileBasicJava;
import nl.miystengine.client.MiystEngine;
import nl.miystengine.client.gui.Gui;
import nl.miystengine.client.gui.MainMenuGui;
import nl.miystengine.client.gui.ScreenGui;

public class GifRenderer
{
	private float gifSpeed;
	private double gif;
	private ImageFrame[] images;
	private String gifSource;
	private float tickGif;
	
	public GifRenderer(String gifSource)
	{
		this.gifSource = gifSource;
		this.gifSpeed = 0.25F;
		this.gif = 1;
		this.tickGif = 0;
	}
	   
	public GifRenderer(String gifSource, float gifSpeed)
	{
		this.gifSource = gifSource;
		this.gifSpeed = gifSpeed;
		this.gif = 1;
		this.tickGif = 0;
	}
	
	private BufferedImage bufferedImage;
	
	public void renderGif(ScreenGui gui,double positionX,double positionY, int width, int height)
	{
		if(images == null)
	   	{
		    File input = new File(MiystEngine.miystengine.getPath().source+"/" + this.gifSource + ".gif");  
		    try 
		   	{
		   		InputStream targetStream = new FileInputStream(input);	
		   		ImageFrame[] images = MiystEngine.miystengine.getTextureManager().gifReader.readGif(targetStream);
		   		this.images = images;
		   		//Create texture
		   		bufferedImage = new BufferedImage(this.images[0].getWidth(), this.images[0].getHeight() * (this.images.length - 1), BufferedImage.TYPE_INT_RGB);
			    Graphics2D g2d = bufferedImage.createGraphics(); 
				
			    for(int x = 0;x < this.images[0].getWidth();++x)
			    {
			    	for(int i = 0;i < this.images.length - 1;++i)
			    	{
				    	for(int y = 0;y < this.images[i].getHeight();++y)
				    	{
				    		if(y < this.images[i].getHeight())
				    		{
				    			g2d.setColor(new Color(this.images[i].getImage().getRGB(x, y)));
				    		}
				    		
			          		g2d.fillRect(x, y + (i * this.images[i].getHeight()), 1, 1);
				    	}
			    	}
			    }
			    
			    File file = new File(MiystEngine.miystengine.getPath().source + "/blendMap.png");
			    
			    try 
			    {
					ImageIO.write(bufferedImage, "png", file);
				} 
			    catch (IOException e) 
			    {
					e.printStackTrace();
				}
		   	} 
		   	catch (IOException e) 
		   	{
		   		e.printStackTrace();
		   	}
	   	}
		
		double gifHeightEnd = height * (this.images.length - 1);
		double gifHeightBegin = height * -this.gif;
	   	MiystEngine.miystengine.getTextureManager().bindTexture(MiystEngine.miystengine.getPath().source+"/blendmap.png",0,true); 	
	   	gui.drawTexturedNoTexture(positionX, positionY, width ,gifHeightEnd,0, gifHeightBegin);
	}
	
	private double test;
	
	public void updateGif()
	{
		if(this.images != null)
		{
			this.tickGif += this.gifSpeed;
			
			++this.gif;
			if(this.gif > this.images.length - 1)
			{
				this.gif = 1;
			}
		}
	}
}