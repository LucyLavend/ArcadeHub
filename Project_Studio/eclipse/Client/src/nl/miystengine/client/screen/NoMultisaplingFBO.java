package nl.miystengine.client.screen;

import java.nio.ByteBuffer;

import nl.miystengine.client.MiystEngine;
import nl.miystengine.client.RawModel;
import nl.miystengine.client.model.TessellatorModel;
import nl.miystengine.client.shader.StaticShader;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL42;
import org.lwjgl.util.vector.Vector2f;

public class NoMultisaplingFBO 
{

	protected static final int REFLECTION_WIDTH = 320;
	private static final int REFLECTION_HEIGHT = 180;
	
	protected static final int REFRACTION_WIDTH = 1280;
	private static final int REFRACTION_HEIGHT = 720;

	private int reflectionFrameBuffer;
	private int reflectionTexture;
	private int reflectionDepthBuffer;
	
	private int refractionFrameBuffer;
	private int refractionTexture;
	private int refractionDepthTexture;
	
    private static String VERTEX_FILE = MiystEngine.miystengine.getPath().sourceShaders + "screen/vertexShader.txt";
   	private static String FRAGMENT_FILE = MiystEngine.miystengine.getPath().sourceShaders + "screen/fragmentShader.txt";
   	
   	public static StaticShader shader = new StaticShader(VERTEX_FILE, FRAGMENT_FILE);  
    public static float brightnessEffect = 1F;  
    public static float brightness = 1F;
    public static float saturation = 1F;
    public static float contrast = 0.1F;

	public NoMultisaplingFBO() 
	{
		//call when loading the game
		initialiseReflectionFrameBuffer();
		initialiseRefractionFrameBuffer();
	}
	
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

	public void cleanUp() 
	{
		//call when closing the game
		GL30.glDeleteFramebuffers(reflectionFrameBuffer);
		GL11.glDeleteTextures(reflectionTexture);
		GL30.glDeleteRenderbuffers(reflectionDepthBuffer);
		GL30.glDeleteFramebuffers(refractionFrameBuffer);
		GL11.glDeleteTextures(refractionTexture);
		GL11.glDeleteTextures(refractionDepthTexture);
	}

	public void bindReflectionFrameBuffer() {//call before rendering to this FBO
		bindFrameBuffer(reflectionFrameBuffer,REFLECTION_WIDTH,REFLECTION_HEIGHT);
	}
	
	public void bindRefractionFrameBuffer() {//call before rendering to this FBO
		bindFrameBuffer(refractionFrameBuffer,REFRACTION_WIDTH,REFRACTION_HEIGHT);
	}
	
	public void unbindCurrentFrameBuffer() {//call to switch to default frame buffer
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

	public int getReflectionTexture() {//get the resulting texture
		return reflectionTexture;
	}
	
	public int getRefractionTexture() {//get the resulting texture
		return refractionTexture;
	}
	
	public int getRefractionDepthTexture(){//get the resulting depth texture
		return refractionDepthTexture;
	}

	private void initialiseReflectionFrameBuffer() {
		reflectionFrameBuffer = createFrameBuffer();
		reflectionTexture = createTextureAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT);
		reflectionDepthBuffer = createDepthBufferAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT);
		unbindCurrentFrameBuffer();
	}
	
	private void initialiseRefractionFrameBuffer() {
		refractionFrameBuffer = createFrameBuffer();
		refractionTexture = createTextureAttachment(REFRACTION_WIDTH,REFRACTION_HEIGHT);
		refractionDepthTexture = createDepthTextureAttachment(REFRACTION_WIDTH,REFRACTION_HEIGHT);
		unbindCurrentFrameBuffer();
	}
	
	void bindFrameBuffer(int frameBuffer, int width, int height){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);//To make sure the texture isn't bound
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL11.glViewport(0, 0, width, height);
	}

	private int createFrameBuffer() {
		int frameBuffer = GL30.glGenFramebuffers();
		//generate name for frame buffer
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		//create the framebuffer
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		//indicate that we will always render to color attachment 0
		return frameBuffer;
	}

	private int createTextureAttachment( int width, int height) {
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height,
				0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
				texture, 0);
		return texture;
	}
	
	private int createDepthTextureAttachment(int width, int height){
		int texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height,
				0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
				texture, 0);
		return texture;
	}

	private int createDepthBufferAttachment(int width, int height) {
		int depthBuffer = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width,
				height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
				GL30.GL_RENDERBUFFER, depthBuffer);
		return depthBuffer;
	}

}