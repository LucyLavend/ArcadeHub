package nl.miystengine.client.gui;

import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import nl.miystengine.client.MiystEngine;
import nl.miystengine.client.renderer.Tessellator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

public class ScreenGui extends Gui
{
    protected long second = 0;

    public int width;
    public int height;

    /** A list of all the buttons in this gui. */
    protected List buttonList = new ArrayList();

    /** The FontRenderer used by ScreenGui */
    protected FontRenderer fontRendererObj;

    /** The button that was just pressed. */
    private ButtonBasic selectedButton;
    private int eventButton;
    private long lastMouseEvent;
    public static String backGroundTexture = "/Menu/wooden hut background.png";
    
    public int oldMouseX,oldMouseY;

    public void updateGui(int x, int y)
    {
    	for (int i = 0; i < this.buttonList.size(); ++i)
        {  
        	ButtonBasic button = ((ButtonBasic)this.buttonList.get(i));
            boolean hoverState = button.enabled && button.visible && x >= button.xPosition && y >= button.yPosition && x < button.xPosition + button.width && y < button.yPosition + button.height;
            if(button.movePlank < 10000 && hoverState)
            {
            	if(button.swingBack&&button.rotate > 2)
            	{
            		button.swingBack = false;
            	}
            	
            	if(!button.swingBack&&button.rotate<-2)
            	{
            		button.swingBack = true;
            	}	
            	
            	if(button.movePlank<10)
            	{
            		this.oldMouseX = x;
            		this.oldMouseY = y;
            	}
            	
            	for(int q =0;q < 500;++q)
            	{
            		if(button.swingBack)
            		{
            			button.rotate += 0.0005;
            			button.movePlank += 0.5F;
            		}
            		else if(!button.swingBack)
            		{
            			button.rotate -= 0.0005;
            			button.movePlank += 0.5F;	
            		}
            	}
            }
            else if(!hoverState&&button.rotate>0)
            {	
            	for(int q =0;q < 25;++q)
            	{
            		if(!hoverState&&button.rotate > 0)
            		{
            			button.rotate -= 0.001;
            		}	
            		else if(!hoverState && this.oldMouseX != x && this.oldMouseY != y)
            		{
            			button.movePlank = 0;
            		}
            	}
            }
            else if(!hoverState&&button.rotate < 0)
            {	
            	for(int q = 0;q < 25;++q)
            	{
            		if(!hoverState&&button.rotate < 0)
            		{
            			button.rotate += 0.001;
            		}	
            		else if(!hoverState && this.oldMouseX != x && this.oldMouseY != y)
            		{
            			button.movePlank=0;
            		}
            	}
            }
        }
    }
    
    public void updateOnSecond(){}
    
    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int x, int y)
    { 
    	GL11.glPushMatrix();
        this.drawTextured(0, 0, this.width,this.height,0,0, backGroundTexture);
        GL11.glPopMatrix();
          
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
            
        if(System.nanoTime() > this.second + 10000000)
        {
        	this.updateGui(x,y);	
        	this.second = System.nanoTime();
    	}
    }
    
    public void drawTexturedNoTexture(double x, double y, double widht,double height, double moveX,double moveY)
    {
        Tessellator tes = Tessellator.instance;
        tes.startDrawingQuads();
        tes.addVertexWithUV(x + moveX, y + height + moveY, this.zLevel,0, 1);
        tes.addVertexWithUV((x + widht) + moveX, (y + height) + moveY, this.zLevel, 1, 1);
        tes.addVertexWithUV((x + widht) + moveX, y + moveY, this.zLevel, 1, 0);
        tes.addVertexWithUV(x + moveX , y + moveY, this.zLevel, 0, 0);
        tes.draw();
    }
    
    public void drawTexturedNoColor(double x, double y, double widht,double height, double moveX,double moveY,String texture)
    {
    	MiystEngine.miystengine.getTextureManager().bindTexture(texture,1,true);
        Tessellator tes = Tessellator.instance;
        tes.startDrawingQuads();
        tes.addVertexWithUV(x + moveX, y + height + moveY, this.zLevel,0, 1);
        tes.addVertexWithUV((x + widht) + moveX, (y + height) + moveY, this.zLevel, 1, 1);
        tes.addVertexWithUV((x + widht) + moveX, y + moveY, this.zLevel, 1, 0);
        tes.addVertexWithUV(x + moveX , y + moveY, this.zLevel, 0, 0);
        tes.draw();
    }
    
    public void drawTextured(double x, double y, double widht,double height, double moveX,double moveY,String texture)
    {
 	    GL11.glColor3f(0.7F, 0.7F, 0.7F);
 	    MiystEngine.miystengine.getTextureManager().bindTexture(texture,0,true);  	
        Tessellator tes = Tessellator.instance;
        tes.startDrawingQuads();
        tes.addVertexWithUV(x + moveX, y + height + moveY, this.zLevel,0, 1);
        tes.addVertexWithUV((x + widht)+moveX, (y + height)+ moveY, this.zLevel, 1, 1);
        tes.addVertexWithUV((x + widht)+moveX, y + moveY, this.zLevel, 1, 0);
        tes.addVertexWithUV(x + moveX , y + moveY, this.zLevel, 0, 0);
        tes.draw();
    }


    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keypressed(KeyEvent e).
     */
    protected void keypressed(char c, int p)
    {
        if (p == 1)
        {
        	MiystEngine.miystengine.displayScreenGui((ScreenGui)null);
        }
    }
  
    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseEvent)
    {
        if (mouseEvent == 0)
        {
            for (int i = 0; i < this.buttonList.size(); ++i)
            {
                ButtonBasic buttonbasic = (ButtonBasic)this.buttonList.get(i);

                if (buttonbasic.mousePressed(MiystEngine.miystengine, mouseX, mouseY))
                {
                    this.selectedButton = buttonbasic;
                    this.actionPerformed(buttonbasic);
                }
            }
        }
    }

    protected void actionPerformed(ButtonBasic button) {}

    /**
     * Causes the screen to lay out its subcomponents again. This is the equivalent of the Java call
     * Container.validate()
     */
    public void setWorldAndResolution(int x, int y)
    {
        this.fontRendererObj = MiystEngine.miystengine.getFondRenderer();
        this.width = x;
        this.height = y;
        this.buttonList.clear();
        this.initGui();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {}

    /**
     * Delegates mouse and keyboard input.
     */
    public void handleInput()
    {
        if (Mouse.isCreated())
        {
            while (Mouse.next())
            {
                this.handleMouseInput();
            }
        }

        if (Keyboard.isCreated())
        {
            while (Keyboard.next())
            {
                this.handleKeyboardInput();
            }
        }
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput()
    {
        int x = Mouse.getEventX() * this.width / MiystEngine.miystengine.getScreenWidth();
        int y = this.height - Mouse.getEventY() * this.height / MiystEngine.miystengine.getScreenHeight() - 1;
        int eb = Mouse.getEventButton();

        if (Mouse.getEventButtonState())
        {
            this.eventButton = eb;
            this.lastMouseEvent = MiystEngine.getSystemTime();
            this.mouseClicked(x, y, this.eventButton);
        }
    }

    /**
     * Handles keyboard input.
     */
    public void handleKeyboardInput()
    {
        if (Keyboard.getEventKeyState())
        {
            this.keypressed(Keyboard.getEventCharacter(), Keyboard.getEventKey());
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {}

    /**
     * "Called when the screen is unloaded. Used to disable keyboard repeat events."
     */
    public void onGuiClosed() {}
    
    

    public void drawDefaultBackground()
    {
    	this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680); 
    }

    public void confirmClicked(boolean b, int i) {}


    /**
     * Returns true if either shift key is down
     */
    public static boolean isShiftKeyDown()
    {
        return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
    }
}
