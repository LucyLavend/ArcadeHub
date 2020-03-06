package nl.miystengine.client.screen;
 
import java.nio.ByteBuffer;

import nl.miystengine.client.FileBasicJava;
import nl.miystengine.client.MiystEngine;
import nl.miystengine.client.RawModel;
import nl.miystengine.client.model.TessellatorModel;
import nl.miystengine.client.shader.StaticShader;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.vector.Vector2f;
 
public class Fbo 
{
    public static final int DEPTH_TEXTURE = 0;
    public static final int DEPTH_RENDER_BUFFER = 1; 
    private final int width;
    private final int height;
    private int frameBuffer;
    private int colourTexture;
    private int depthTexture;
    private int depthBuffer;
    private int colourBuffer;
    private boolean multisample = false;
 
    public Fbo(int width, int height, int depthBufferType) 
    {
        this.width = width;
        this.height = height;
        this.multisample = false;
        initialiseFrameBuffer(depthBufferType);
        shader.start();
		shader.connectTextureUnits();
		shader.stop();
    }
    
    public Fbo(int width, int height) 
    {
        this.width = width;
        this.height = height;
        this.multisample = true;
        initialiseFrameBuffer(this.DEPTH_RENDER_BUFFER); 
        shader.start();
		shader.connectTextureUnits();
		shader.stop();
    }
    
    //Render
    private static String VERTEX_FILE = MiystEngine.miystengine.getPath().sourceShaders + "screen/vertexShader.txt";
   	private static String FRAGMENT_FILE = MiystEngine.miystengine.getPath().sourceShaders + "screen/fragmentShader.txt";
   	
   	public static StaticShader shader = new StaticShader(VERTEX_FILE, FRAGMENT_FILE);  
    public static float brightnessEffect = 1F;  
    public static float brightness = 1F;
    public static float saturation = 1F;
    public static float contrast = 0.1F;
    
	public static RawModel renderScreen()
	{
		TessellatorModel model = new TessellatorModel();
		model.addVerticesNumber(1);
		model.addVertexTerrain(-1,  1, 0D, 		0, 1);
		model.addVertexTerrain(1, 1, 0D, 		1, 1);
		model.addVertexTerrain(1, -1, 0D, 		1, 0);
		model.addVertexTerrain(-1, -1, 0D, 		0, 0);
		return model.drawModelVTI();
	} 
   	
       public void framebufferRender(float x,float y,int width, int height,int bufferID)
       {
    	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
           
    	    GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_TEXTURE_2D);  
            RawModel model = this.renderScreen();
       		shader.start();
      		
       		shader.brightnessEffect(brightnessEffect);
       		shader.brightness(brightness);
      		shader.saturation(saturation);
      		shader.contrast(contrast);
      		
       		shader.location_screenTexture = bufferID;
       		shader.loadNearFar(new Vector2f(MiystEngine.miystengine.getCamera().getNearPlane(),MiystEngine.miystengine.getCamera().getFarPlane()));
   			
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, bufferID);
       		GL30.glBindVertexArray(model.getVaoID());
       		GL20.glEnableVertexAttribArray(0);
       		GL20.glEnableVertexAttribArray(1);
       		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
       		GL20.glDisableVertexAttribArray(0);
       		GL20.glDisableVertexAttribArray(1);
       		GL30.glBindVertexArray(0);	
       		shader.stop();
       		GL11.glEnable(GL11.GL_DEPTH_TEST);
       }
    //
 
       /**
        * Deletes the frame buffer and its attachments when the game closes.
        */
       public void cleanUp() 
       {
           GL30.glDeleteFramebuffers(frameBuffer);
           GL11.glDeleteTextures(colourTexture);
           GL11.glDeleteTextures(depthTexture);
           GL30.glDeleteRenderbuffers(depthBuffer);
           GL30.glDeleteRenderbuffers(colourBuffer);
       }
    
       /**
        * Binds the frame buffer, setting it as the current render target. Anything
        * rendered after this will be rendered to this FBO, and not to the screen.
        */
       public void bindFrameBuffer() 
       {
           GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBuffer);
           GL11.glViewport(0, 0, width, height);
       }
 
 /**
  * Binds the current FBO to be read from (not used in tutorial 43).
  */
 public void bindToRead() 
 {
     GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
     GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, frameBuffer);
     GL11.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
 }

 
    private int createDepthTextureAttachment(int width, int height)
    {
        int texture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,texture, 0);
        return texture;
    }
    
    /**
     * Unbinds the frame buffer, setting the default frame buffer as the current
     * render target. Anything rendered after this will be rendered to the
     * screen, and not this FBO.
     */
    public void unbindFrameBuffer() 
    {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }

    /**
     * @return The ID of the texture containing the colour buffer of the FBO.
     */
    public int getColourTexture() 
    {
        return colourTexture;
    }
 
    /**
     * @return The texture containing the FBOs depth buffer.
     */
    public int getDepthTexture() 
    {
        return depthTexture;
    }
    
	public int shadowMap;
 
    /**
     * Creates the FBO along with a colour buffer texture attachment, and
     * possibly a depth buffer.
     * 
     * @param type
     *            - the type of depth buffer attachment to be attached to the
     *            FBO.
     */
    private void initialiseFrameBuffer(int type) 
    {
        createFrameBuffer();
        
        shadowMap = createDepthBufferAttachment(width, height);
        
        if(this.multisample)
        {
        	createMultisampleColorAttachment();
        }
        else
        {
        	createTextureAttachment();
        }
        
        if (type == this.DEPTH_RENDER_BUFFER) 
        {
        	createDepthBufferAttachment();
        } 
        else if (type == this.DEPTH_TEXTURE) 
        {
        	createDepthTextureAttachment();
        }
        unbindFrameBuffer();
    }
 
    public void resolveToFBO(Fbo fbo)
    {
    	if(fbo != null)
    	{
	    	GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, fbo.frameBuffer);
	    	GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, this.frameBuffer);
	    	GL30.glBlitFramebuffer(0, 0, width, height, 0, 0, fbo.width, fbo.height, GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
	    	unbindCurrentFrameBuffer();
    	}
    }
	  
    public void unbindCurrentFrameBuffer() 
    {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
    }
 
    /**
     * Creates a texture and sets it as the colour buffer attachment for this
     * FBO.
     */
    private void createTextureAttachment() 
    {
        colourTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colourTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, colourTexture,0);
    }
 
    /**
     * Adds a depth buffer to the FBO in the form of a texture, which can later
     * be sampled.
     */
    private void createDepthTextureAttachment() 
    {
        depthTexture = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, depthTexture);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, width, height, 0, GL11.GL_DEPTH_COMPONENT,GL11.GL_FLOAT, (ByteBuffer) null);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthTexture, 0);
    }
 
    /**
     * Adds a depth buffer to the FBO in the form of a render buffer. This can't
     * be used for sampling in the shaders.
     */
    private void createDepthBufferAttachment() 
    {    	
    	depthBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);  
        if(!this.multisample)
        {
        	GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, width,height);      	 
        }
        else
        {
        	GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER,4, GL14.GL_DEPTH_COMPONENT24, width,height);      	
        }
        
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER,depthBuffer);
    }
    
    /**
     * Creates a new frame buffer object and sets the buffer to which drawing
     * will occur - colour attachment 0. This is the attachment where the colour
     * buffer texture is.
     * 
     */
    private void createFrameBuffer() 
    {
        frameBuffer = GL30.glGenFramebuffers();
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
    }
    
    private static int createDepthBufferAttachment(int width, int height) 
	{
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT16, width, height, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, texture, 0);
		return texture;
	}

    /**
     * Adds a depth buffer to the FBO in the form of a render buffer. This can't
     * be used for sampling in the shaders.
     */
    private void createMultisampleColorAttachment() 
    {
        colourBuffer = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, colourBuffer);
        if(!this.multisample)
        {
        	GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_RGBA8, width,height);      	 
        }
        else
        {
        	GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER,4, GL11.GL_RGBA8, width,height);      	
        }
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_RENDERBUFFER,colourBuffer);
    }

}