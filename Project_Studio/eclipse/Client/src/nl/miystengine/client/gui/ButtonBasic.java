package nl.miystengine.client.gui;

import nl.miystengine.client.FileBasicJava;
import nl.miystengine.client.MiystEngine;
import nl.miystengine.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;


public class ButtonBasic extends Gui
{
	public String buttonTextures = new String("button.png");
    public int color = -5;
    public int width;
    public int height;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean visible;
    private boolean hover;
    private int packedFGColour;
    private int loc;
    public boolean swingBack = false;
    public float movePlank = 0;
    private boolean isWhiteText = false;
    
    public int xString;
    public int yString;

    public ButtonBasic(int id, int x, int y, String stringdisplay,int textureID)
    {
        this(id, x, y, 200, 20, stringdisplay,20 * textureID);
    }
    
    public ButtonBasic(int id, int x, int y, String stringdisplay,int textureID,int height)
    {
        this(id, x, y, 200, 20, stringdisplay,20 * textureID);
        this.textureY = 20 * height;
    }
    
    public ButtonBasic(int id, int x, int y,int yyy, String stringdisplay,int textureID)
    {
        this(id, x, y, 200, 20, stringdisplay,textureID);
        this.height = yyy;
        this.loc = yyy-20;
        this.textureY = 20 * textureID;
    }

    public ButtonBasic(int id, int x, int y, int w, int h, String displayString,int textureID)
    {
        this.enabled = true;
        this.visible = true;
        this.id = id;
        this.xPosition = x;
        this.yPosition = y;
        this.width = w;
        this.height = h;
        this.displayString = displayString;
        this.textureY = 20 * textureID;
    }
    
    public ButtonBasic(int xString,int yString,boolean isEnabled,boolean isForShow,int id, int x, int y, int w, int h, String displayString,int textureID)
    {
    	this.xString = xString;
    	this.yString = yString;
    	this.isWhiteText = isForShow;
        this.width = 200;
        this.height = 20;
        this.enabled = isEnabled;
        this.visible = true;
        this.id = id;
        this.xPosition = x;
        this.yPosition = y;
        this.width = w;
        this.height = h;
        this.displayString = displayString;
        this.textureY = 20 * textureID;
    }
    
    public ButtonBasic(boolean isForShow,int id, int x, int y, int w, int h, String displayString,int textureID)
    {
    	this.enabled = isForShow;
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = id;
        this.xPosition = x;
        this.yPosition = y;
        this.width = w;
        this.height = h;
        this.displayString = displayString;
        this.textureY = 20 * textureID;
    }
    
    public ButtonBasic(boolean specialButton,int id, int x, int y, String texturelocation,int sizeSpecialButton,int textureID)
    {
    	this.specialButton = specialButton;
        this.width =  20;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = id;
        this.xPosition = x;
        this.yPosition = y;
        this.buttonTextures = texturelocation;
        this.sizeSpecialButton = sizeSpecialButton;
        this.textureY = 20 * textureID;
    }
    
    public ButtonBasic(int id, int x, int y, int w, int h, String displayString, int color,int textureID)
    {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = id;
        this.xPosition = x;
        this.yPosition = y;
        this.width = w;
        this.height = h;
        this.displayString = displayString;
        this.color = color;
        this.textureY = 20 * textureID;
    }
    
    public boolean specialButton = false;
    public int sizeSpecialButton = 0;
    
    public ButtonBasic(int id, int x, int y, int w, int h, String displayString, String texturelocation,int textureID)
    {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = id;
        this.xPosition = x;
        this.yPosition = y;
        this.width = w;
        this.height = h;
        this.displayString = displayString;
        this.buttonTextures = texturelocation;
        this.textureY = 20 * textureID;
   }
    
    /**
     * Returns 0 if the button is disabled, 1 if the mouse is not hovering over this button and 2 if it IS hovering over
     * this button.
     */
    public int getHoverState(boolean b)
    {
        byte state = 1;

        if (!this.enabled)
        {
        	state = 0;
        }
        else if (b)
        {
        	state = 2;
        }

        return state;
    }
    
    public float rotate;
    
    private int textureY = 20;
    
    /**
     * Draws this button to the screen.
     */
    public void drawButton(MiystEngine main, int x, int y)
    {
        if (this.visible)
        {
            main.getTextureManager().bindTexture(buttonTextures,1,true);
            GL11.glColor4f(1, 1, 1, 1);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        	
            if(this.specialButton)
            {
                Tessellator tes = Tessellator.instance;
            	tes.startDrawingQuads();
            	tes.addVertexWithUV(this.xPosition+0, this.yPosition+0, 0, 0 ,0);
            	tes.addVertexWithUV(this.xPosition+this.sizeSpecialButton, this.yPosition+0, 0, 1 ,0);
            	tes.addVertexWithUV(this.xPosition+this.sizeSpecialButton, this.yPosition+this.sizeSpecialButton, 0, 1 ,1);
            	tes.addVertexWithUV(this.xPosition+0, this.yPosition+this.sizeSpecialButton, 0, 0 ,1);
            	tes.draw();
            }
            else
            {
            	this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, (46 + 1 * 20) + this.textureY, this.width / 2, this.height,this.loc);
            	this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, (46 + 1 * 20) + this.textureY, this.width / 2, this.height,this.loc);
            }
            
            this.mouseDragged(main, x, y);
            if(this.color == -5)
            {
            	color = 14737632;
            }
            if (packedFGColour != 0)
            {
            	color = packedFGColour;
            }
            else if (!this.enabled)
            {
            	color = 10526880;
            }
            if(this.isWhiteText)
            {
            	color = 14737632;
            }
            this.drawCenteredString(main.getFondRenderer(), this.displayString, (this.xPosition + this.width / 2) - this.xString, (this.yPosition + (this.height - 8) / 2) - this.yString, color);
            //GL11.glDisable(GL11.GL_BLEND);
        }
    }

    protected void mouseDragged(MiystEngine main, int x, int y) {}

    public void mouseReleased(int x, int y) {}
    
    public boolean mousePressed(MiystEngine main, int x, int y)
    {
    	if(this.specialButton)
    	{
    		return this.enabled && this.visible && x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.sizeSpecialButton + this.width && y < this.yPosition + this.sizeSpecialButton + this.height;
    	}
    	else return this.enabled && this.visible && x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
    }

}