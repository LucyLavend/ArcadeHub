package nl.miystengine.client;

import game.GameMain;
import io.netty.util.concurrent.GenericFutureListener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.imageio.ImageIO;

import nl.miystengine.client.audio.AudioPlayer;
import nl.miystengine.client.gui.FontRenderer;
import nl.miystengine.client.gui.Gui;
import nl.miystengine.client.gui.MainMenuGui;
import nl.miystengine.client.gui.ScreenGui;
import nl.miystengine.client.renderer.GLAllocation;
import nl.miystengine.client.renderer.Tessellator;
import nl.miystengine.client.renderer.texture.TextureManager;
import nl.miystengine.client.screen.Camera;
import nl.miystengine.client.screen.Fbo;
import nl.miystengine.client.screen.ScaledResolution;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

public class MiystEngine implements Runnable
{
	/**
	 * The characters used for the font renderer
	 */
	private String acceptedSymbols;
	/**
	 * Texture manager: set and save the texture with anti-aliasing or mipmapping
	 */
	private TextureManager renderEngine;
	
	/**
	 * Logger for errors
	 */
	private Logger logger;
	
	/**
	 * Main instance
	 */
	public static MiystEngine miystengine;  
	
	/**
	 * Speak for them self :)
	 */
	private boolean fullscreen;
	private boolean hasCrashed;
	
    /**
     * Display height and width
     */
	private int displayWidth;
	private int displayHeight;
    
	/**
	 * Delta time class
	 */
	private DeltaTime deltatimer;
	private float delta;
    
    /** The font renderer used for displaying and measuring text. */
	private FontRenderer fontRenderer;

    /** The ScreenGui that's being displayed at the moment. */
	private ScreenGui currentScreen;
	
	/**
	 * The version of the framework
	 */
    private final String launchedVersion;
    
    /**
     * FPS counter
     */
    public int debugFPS;
    
    /**
     * System time for the fps counter
     */
    long systemTime = getSystemTime();
    
    public static Random rand;
    /**
     * Set to true to keep the game loop running. Set to false by shutdown() to allow the game loop to exit cleanly.
     */
    volatile boolean running;
    
    /**
     * For the fps counter
     */
    public String debug = "";
    long debugUpdateTime;

    /** holds the current fps */
    public int fpsCounter;
    long prevFrameTime = -1L;

    /**
     * For resizing 
     */
    private boolean wasResized;
    private int timerResized = 0;
	private long lastFrameTime;
	
	private GameMain game;
	
    /**
     * The frame buffer object before it is Multisampled
     * @return Fbo without Multisampleing
     */
	public Fbo fbo;
    
    /**
     * Set the frame buffer object for Multisampled
     */
	
	public Fbo fbo_Out;
	private Camera camera;
	
	/**
	 * Location  file settings display
	 */
	private String Location = "Display.txt";
	
	/**
	 * Display settings
	 */
	private int DepthBits;
	private int Samples;
	
	/**
	 * The audio player
	 */
	private AudioPlayer audioPlayer;
	
	/**
	 * All the paths or sources of .txt, .png, .wav and gif
	 */
	private Sources pathSources;
	
	/**
	 * The constructor of the main MiystEngine Class
	 * @param isFullScreen
	 */
	
    public MiystEngine(boolean isFullScreen)
    {
    	miystengine = this;
    	this.pathSources = new Sources();
    	if(Main.backup)
    	{
    		FileBasicJava.backupFile();
    	}
    	this.audioPlayer = new AudioPlayer();
    	this.logger = LogManager.getLogger();
        this.acceptedSymbols = " !@#$%&*()\'+,-./1234567890:;<=>?\"ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    	this.deltatimer = new DeltaTime(20.0F);
    	this.wasResized = false;
    	this.running = true;
    	this.camera = new Camera();
    	this.rand = new Random();
    	this.game = new GameMain();
    	this.debugUpdateTime = getSystemTime();
        this.launchedVersion = this.pathSources.version;
        this.displayWidth = 854;
        this.displayHeight = 480;
        this.fullscreen = isFullScreen;
        ImageIO.setUseCache(false);
        lastFrameTime = getCurrentTime(); 	
    }
    
    public Sources getPath()
    {
    	return this.pathSources;
    }
    
    public AudioPlayer getAudioPlayer()
    {
    	return this.audioPlayer;
    }
    
    public Camera getCamera()
    {
    	return this.camera;
    }
    
    public FontRenderer getFondRenderer()
    {
    	return this.fontRenderer;
    }
    
    /**
     * Get the logger for debug
     * @return
     */
    public Logger getLogger()
    {
    	return this.logger;
    }

    public int getScreenWidth()
    {
    	return this.displayWidth;
    }
    
    public int getScreenHeight()
    {
    	return this.displayHeight;
    }
    
    public boolean wasScreenResized()
    {
    	return this.wasResized;
    }
    
    /**
     * Starts the game.
     */
    private void startGame() throws LWJGLException, IOException, InterruptedException
    {
    	/**
    	 * Read and set the depthbits and samples for the display
    	 */
        try
        {
			@SuppressWarnings("resource")
			BufferedReader read = new BufferedReader(new FileReader(new File(this.pathSources.source,Location)));
			String line = "";	
		    if(read != null && (line = read.readLine()) != null)
	        {
		    	String[] wholeSettingLine = line.split(":");
				if(wholeSettingLine[0].equals("DepthBits"))
	        	{
					DepthBits = Integer.parseInt(wholeSettingLine[1]);
				}
			} 
		    if(read != null && (line = read.readLine()) != null)
	        {
		    	String[] wholeSettingLine = line.split(":");
		    	if(wholeSettingLine[0].equals("Samples"))
		    	{
		    		Samples = Integer.parseInt(wholeSettingLine[1]);
		    	}
	        }
		}
        catch(Exception ex)
        {
	        ex.printStackTrace();
	        System.out.println("Couldn't load Display settings: "+Location);
	        System.out.println("Game will close now....");	
	        MiystEngine.miystengine.shutdownApplet();
        }
        
        /**
         * Display fullscreen
         */
        
        if (this.fullscreen)
        {
            Display.setFullscreen(true);
        }
        else
        {
            Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
        }
        
        /**
         * Resiszable and title
         */
        Display.setResizable(true);
        Display.setTitle(this.getGame().displayName() + "    Version: " + this.getGame().Version());
        logger.info("LWJGL Version: " + Sys.getVersion());
        
        /**
         * Set the icon
         */
        Display.setIcon(new ByteBuffer[] {this.getGame().setIcon(),this.getGame().setIcon()}); 
        try
        {
        	Display.create((new PixelFormat(4,24,0,4)).withSamples(4).withDepthBits(24));
        }
        catch (LWJGLException ex)
        {
            System.out.println("Couldn't set pixel format: "+ ex);
            try
            {
                Thread.sleep(1000L);
            }
            catch (InterruptedException var6){;}
            Display.create();
        }
        GL11.glEnable(GL13.GL_MULTISAMPLE);
        FileBasicJava.checkGLError("Startup");
        MiystEngine.miystengine.camera.creatWindow();
        FileBasicJava.checkGLError("After EntityRender");
        
        this.renderEngine = new TextureManager();
        this.fontRenderer = new FontRenderer(acceptedSymbols);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        
        if (this.fullscreen)
        {
           this.toggleFullscreen();
        }
        
        this.game.activatedOnStart();
   }
    
    public ScreenGui getCurrentScreen()
    {
    	return this.currentScreen;
    }
    
    /**
     * Sets the argument ScreenGui as the main screen.
     */
    public void displayScreenGui(ScreenGui screen)
    {
    	if(this.currentScreen != null)
    	{
    		this.currentScreen.onGuiClosed();
    	}
    	
    	if(screen == null)
    	{
    		screen = new MainMenuGui();
    		this.currentScreen = (ScreenGui)screen;
    	}
    	
    	if (screen != null)
    	{
    		ScaledResolution var2 = new ScaledResolution(this, this.displayWidth, this.displayHeight);
    		int w = var2.getScaledWidth();
    		int h = var2.getScaledHeight();
    		((ScreenGui)screen).setWorldAndResolution(w, h);
    	}
    }

    public void shutdownApplet()
    {
        try
        {
            logger.info("Stopping!");	
        }
        finally
        {
	        for(int id=0;id<TextureManager.textureList.size();++id)
	        {	
	        	 try
	             {
	        		 ++this.getTextures;
	        		 GL11.glDeleteTextures(id);
	             }
	             catch (Throwable var6)
	             {
	            	 var6.printStackTrace();
	            	 System.out.println("Texture is already gone or there is a bug in the Texture Register List?");
	            	 System.out.println("Report to the author please....");
	             }
	        }
        	
        	if(this.getTextures == TextureManager.textureList.size())
        	{
       		 System.out.println("All Textures removed successfully!");
        	}
        	System.out.println("#Game closed#");
            Display.destroy();
            if (!this.hasCrashed)
            {
                System.exit(0);
            }
        }
        System.gc();
    }
    
    public int getTextures = 0;
    public int mainScreenBufferID;
    

    /**
     * Delta time
     */
	public void updateTime()
	{
		long currentFrameTime = getCurrentTime();
		this.delta = (currentFrameTime - this.lastFrameTime) / 1000f;
		this.lastFrameTime = currentFrameTime;
	}
	
	private long getCurrentTime()
	{
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
    public void run()
    {
        this.running = true;
        try
        {
            this.startGame();
        	System.out.println("Depth bits loaded: " + GL11.glGetInteger(GL11.GL_DEPTH_BITS));
            FileBasicJava.checkGLError("Before Framebuffer");
            this.mainScreenBufferID = GL30.glGenFramebuffers();
            ScaledResolution sr = new ScaledResolution(this,this.displayWidth, this.displayHeight);
            this.currentScreen = new MainMenuGui();
            this.currentScreen.setWorldAndResolution(sr.getScaledWidth(), sr.getScaledHeight());
            FileBasicJava.checkGLError("After Start Game");
        	FileBasicJava.checkGLError("After Audio"); 		
        }
        catch (Throwable var11)
        {
        	//Show crash report on Screen
        	var11.printStackTrace();
            return;
        }
     
        while (true)
        {
            try
            {		
                while (this.running)
                {
                    if (!this.hasCrashed)
                    {
                        try
                        {
                            updateTime();
                			this.runGameLoop();
                		}
                        catch (OutOfMemoryError var10)
                        {
                        	/**
                        	 * A function to free memory if needed(Working with UI so it shoulnd't be needed because it is not that complex)
                        	 */
                            this.freeMemory();
                            System.gc();
                        }
                        continue;
                    }
                  
                    return;
                }
            }
            catch (Error e)
            {
            	e.printStackTrace();
            }
            catch (Throwable e)
            {           
                this.freeMemory();
                this.logger.fatal("Unreported exception thrown!", e);
            }
            finally
            {
            	 this.shutdownApplet();
            }    
            return;
        }
    }
    
    public GameMain getGame()
    {
    	return this.game;
    }
    
    /**
     * Free the memory.
     * Example: a texture that is loaded but not needed right now so it can be removed
     */
    private void freeMemory(){}

    /**
     * Called repeatedly from run()
     */
    private void runGameLoop()
    {
    	FileBasicJava.checkGLError("Start Game Loop");      
        if((Display.isCreated() && Display.isCloseRequested()))
        {
            this.shutdown();
        }
        this.runTick();
        FileBasicJava.checkGLError("Before delta time");
        this.deltatimer.updateTimer();
        for (float v = 0; v < this.deltatimer.elapsedTicks; ++v)
        {    
        	if(Gui.listOfGifs != null && !Gui.listOfGifs.isEmpty())
        	{
	        	for(int i = 0;i < Gui.listOfGifs.size();++i)
	            {
	            	Gui.listOfGifs.get(i).getGifRender().updateGif();
	            }
        	}
        	//Update everything here with delta time
        }

        FileBasicJava.checkGLError("Before render camera");
        if(this.camera != null)
        { 
            this.camera.updateCameraRender(delta);
        }
        FileBasicJava.checkGLError("After render camera");
        
        GL11.glFlush();
        if (!Display.isActive() && this.fullscreen)
        {
            this.toggleFullscreen();
        }
        
        this.prevFrameTime = System.nanoTime(); 
        this.wasResized();
        Thread.yield();
        
        /**
         * FPS counter
         */
        ++this.fpsCounter;
        while (getSystemTime() >= this.debugUpdateTime + 1000L)
        {
        	this.debugFPS = this.fpsCounter;
            this.debug = debugFPS + " fps, ";
            this.debugUpdateTime += 1000L;
            this.fpsCounter = 0;
        }
    }

    /**
     * handle the resizing of the screen
     */
    public void wasResized()
    {
        Display.update();

        if(this.wasResized)
        {
        	++this.timerResized;
        }
        
        if(this.timerResized > 100)
        {
        	this.wasResized = false;
        	this.timerResized = 0;
        }
        
        if (!this.fullscreen && Display.wasResized())
        {
        	this.wasResized = true;
            int var1 = this.displayWidth;
            int var2 = this.displayHeight;
            this.displayWidth = Display.getWidth();
            this.displayHeight = Display.getHeight();

            if (this.displayWidth != var1 || this.displayHeight != var2)
            {
                if (this.displayWidth <= 0)
                {
                    this.displayWidth = 1;
                }

                if (this.displayHeight <= 0)
                {
                    this.displayHeight = 1;
                }

                this.resize(this.displayWidth, this.displayHeight);
            }
        }
    }

    
    /**
     * Called when the window is closing. Sets 'running' to false which allows the game loop to exit cleanly.
     */
    public void shutdown()
    {
        this.running = false;
    }
    
    /**
     * Toggles fullscreen mode.
     */
    public void toggleFullscreen()
    {
        try
        {
            this.fullscreen = !this.fullscreen;

            if (this.fullscreen)
            {
                this.displayWidth = Display.getDisplayMode().getWidth();
                this.displayHeight = Display.getDisplayMode().getHeight();

                if (this.displayWidth <= 0)
                {
                    this.displayWidth = 1;
                }
                if (this.displayHeight <= 0)
                {
                    this.displayHeight = 1;
                }
            }
            else
            {
             
                if (this.displayWidth <= 0)
                {
                    this.displayWidth = 1;
                }

                if (this.displayHeight <= 0)
                {
                    this.displayHeight = 1;
                }
            }
            
            if (this.currentScreen != null)
            {
                this.resize(this.displayWidth, this.displayHeight);
            }
         
            Display.setFullscreen(this.fullscreen);
            this.wasResized();
        }
        catch (Exception e)
        {
            logger.error("Couldn\'t toggle fullscreen", e);
        }
    }

    /**
     * Called to resize the current screen.
     */
    private void resize(int x, int y)
    {
    	this.displayWidth = x <= 0 ? 1 : x;
        this.displayHeight = y <= 0 ? 1 : y;
        ScaledResolution sr = new ScaledResolution(this,x, y);
        if(this.currentScreen != null)
        {
        	this.currentScreen.setWorldAndResolution(sr.getScaledWidth(), sr.getScaledHeight());
        }
        MiystEngine.miystengine.camera.creatWindow();
    }
    
    /**
     * Runs the current tick.
     */
    public void runTick()
    {
        this.renderEngine.tick();

        if (this.currentScreen != null)
        {
            try
            {
                this.currentScreen.handleInput();
            }
            catch (Throwable t)
            {
            	t.printStackTrace();
            }

            if (this.currentScreen != null)
            {
                try
                {
                    this.currentScreen.updateScreen();
                }
                catch (Throwable t)
                {
                	t.printStackTrace();
                }
            }
        }

        if (this.currentScreen == null)
        {
            int me;

            while (Mouse.next())
            {
            	me = Mouse.getEventButton();
                
                long time = getSystemTime() - this.systemTime;

                if (time <= 200L)
                {
                    if (this.currentScreen != null)
                    {
                        this.currentScreen.handleMouseInput();
                    }
                }
            }
            boolean var10;

            while (Keyboard.next())
            {
                if (Keyboard.getEventKeyState())
                { 
                    if (this.currentScreen != null)
                    {
                        this.currentScreen.handleKeyboardInput();
                    }
                }
            }
        }
        this.systemTime = getSystemTime();
    }

    public static MiystEngine getMiystEngine()
    {
        return miystengine;
    }

    /**
     * Gets the system time in milliseconds.
     */
    public static long getSystemTime()
    {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    /**
     * Returns whether we're in full screen or not.
     */
    public boolean isFullScreen()
    {
        return this.fullscreen;
    }
    
    /**
     * Returns whether we're in full screen or not.
     */
    public void setFullScreen(boolean setFullScreen)
    {
    	this.fullscreen = setFullScreen;
    }

    /**
     * The TextureManager for textures
     * @return TextureManager
     */
    public TextureManager getTextureManager()
    {
        return renderEngine;
    }
}
