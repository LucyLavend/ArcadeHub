package nl.miystengine.client.gui;

import game.GameMain;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import nl.miystengine.client.FileBasicJava;
import nl.miystengine.client.Main;
import nl.miystengine.client.MiystEngine;
import nl.miystengine.client.renderer.Tessellator;
import nl.miystengine.client.renderer.texture.ArrayListGif;
import nl.miystengine.client.renderer.texture.GifRenderer;
import nl.miystengine.client.renderer.texture.ImageFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class MainMenuGui extends ScreenGui
{	
	
    public MainMenuGui()
    {
        this.fadeToBlack = 1F;
        this.listOfGifs.add(new ArrayListGif(new GifRenderer("BG")));
    }

    @Override
    /**
     * Fired when a key is typed. 
     * This is the equivalent of KeyListener.keypressed(KeyEvent e).
     */
    protected void keypressed(char c, int i) 
    {
    	super.keypressed(c,i);
    	
    	if(!this.showButtons)
        {
    		this.showButtons = true;
        }	
     }
    
    /**
     * Activate on enter button id(First argument when creating a button)
     */
    protected void actionPerformed(ButtonBasic button)
    {
        if(button.id == 2)
        {
			MiystEngine.miystengine.shutdown();
        }
    }

    /**
     * When clicked one time and when you click the second time this function will be fired.
     */
    public void confirmClicked(boolean confirm, int bid)
    {
        if (confirm && bid == 12)
        {
            MiystEngine.miystengine.displayScreenGui(this);
        }
        else if (bid == 13)
        {
            MiystEngine.miystengine.displayScreenGui(this);
        }
    }
    
   public boolean showButtons = false;
   public float alphaStartString = 0.0F;
   public int changeStartString = 0;
   public boolean alphaStartStringBack = false;
   public boolean buttonReleased = true;
   private Tessellator tes = Tessellator.instance;
   public long second = 0;
   private int showGuiTimer;
   public int add = 0;
   
    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int x, int y)
    {
    	Gui.listOfGifs.get(0).getGifRender().renderGif(this,0,0, this.width, this.height);
	   	
        if(MiystEngine.miystengine.wasScreenResized() && this.showButtons && this.buttonList.isEmpty())
        {
        	int Y = this.height / 4 + 48;
        	int X = this.width / 2 - 100;
        	this.buttonList.add(new ButtonBasic(0, X, Y, "Campaign",0));
        	this.buttonList.add(new ButtonBasic(1, this.width / 2 - 100, this.height / 4 + 82 , "Options",0));
        	this.buttonList.add(new ButtonBasic(2, X, Y + (72/2) + 12 + 24,  "Quit",0));
        	this.add = 1;
        }
        
        if(this.showButtons && this.add == 0)
        {
        	int Y = this.height / 4 + 48;
        	int X = this.width / 2 - 100;
        	this.buttonList.add(new ButtonBasic(0, X, Y, "Campaign",0));
          	this.buttonList.add(new ButtonBasic(1, this.width / 2 - 100, this.height / 4 + 82 , "Options",0));
        	this.buttonList.add(new ButtonBasic(2, X, Y + (72/2) + 12 + 24,  "Quit",0));
        	this.add = 1;
       }
        
       if(this.fadeToBlack > 0)
       {
       		this.fadeToBlack -= 0.01F;
       }
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        {
        	//MiystEngine.miystengine.getTextureManager().bindTexture("/texture here.png",0,true);  	
	 	    GL11.glColor4f(0.7F, 0.7F, 0.7F, 1F - this.fadeToBlack);
	 	    //Tessellator tes = Tessellator.instance;
	        //tes.startDrawingQuads();
	        //tes.addVertexWithUV(0, this.height, this.zLevel,0, 1);
	        //tes.addVertexWithUV(this.width, this.height, this.zLevel, 1, 1);
	        //tes.addVertexWithUV(this.width, 0, this.zLevel, 1, 0);
	        //tes.addVertexWithUV(0 , 0, this.zLevel, 0, 0);
	        //tes.draw();
        }

        for (int i = 0; i < this.buttonList.size(); ++i)
        {  
        	ButtonBasic button = ((ButtonBasic)this.buttonList.get(i));
        	GL11.glPushMatrix();
        	GL11.glTranslatef(button.xPosition+(button.width / 2), button.yPosition+(button.height/2), 0);
            GL11.glScalef(1F + (button.rotate / 10F), 1F + (button.rotate / 10F), 1F + (button.rotate / 10F));
            GL11.glTranslatef(-(button.xPosition+(button.width / 2)), -(button.yPosition+(button.height/2)), 0);
            button.drawButton(MiystEngine.miystengine, x, y);
            GL11.glPopMatrix();
        }
        
       
      	
        if(!this.showButtons)
        {   
        	this.drawCenteredStringWithColor(fontRendererObj,"Press any key to continue", (int)(this.width / 2) , (int)(this.height / 1.1), this.alphaStartString, this.alphaStartString, this.alphaStartString, this.alphaStartString);
        	this.add = 0;
        }
        
        if(System.nanoTime() > this.second + 10000000)
        {
        	this.updateGui(x,y);	
        	this.second = System.nanoTime();
    	}

        
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,this.textureID);
		
		this.drawCenteredStringWithColor(fontRendererObj,"fps: " + MiystEngine.getMiystEngine().debugFPS, (int)(this.width / 18) , (int)(this.height / 6), 1,1,1,1);
	 }
    
     private int textureID = MiystEngine.miystengine.getTextureManager().loadTexture(MiystEngine.miystengine.getPath().sources + "missing_texture" + ".png");
    
    
     private float fadeToBlack = 1F;
 

    @Override
    public void updateGui(int x,int y)
    {
    	super.updateGui(x, y);

		if(this.showGuiTimer > 0)
		{
			--this.showGuiTimer;
		}
		
        /**
         * Change text alpha value with first screen
         */
        ++this.changeStartString;
        if(this.changeStartString > 100)
        {
        	if(!this.alphaStartStringBack  && this.alphaStartString < 0.1F)
            {
        		this.alphaStartStringBack = true;
            }
        	else if(this.alphaStartStringBack  && this.alphaStartString > 1.4F)
            {
        		this.alphaStartStringBack = false;
            }
        	this.changeStartString = 0;
        }
        if(!this.alphaStartStringBack && this.alphaStartString > 0F)
        {
        	this.alphaStartString -= 0.01F;
        }
        if(this.alphaStartStringBack && this.alphaStartString < 1.4F)
        {
        	this.alphaStartString += 0.01F;
        }
    }
}
