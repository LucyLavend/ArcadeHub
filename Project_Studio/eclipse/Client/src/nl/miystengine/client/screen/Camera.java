package nl.miystengine.client.screen;

import nl.miystengine.client.MiystEngine;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
 
public class Camera 
{
	    private float camX;
	    private float camY;
	    private float camZ;
	    private float pitch;
	    private float oldPitch;
	    private float zPitch;
	    private float walkAnimation = 0F;	
	    private boolean incdecWalkAnimation = false;
	    private float drunkWalk = 0;
	    private float walkCrouchOrLayingDown = 1F;
	    private float FOV = 120;
	    private float FOVstandard;
	    private float Aspect;
	    private float zNear = 0.1F;
	    private float zFar = 1000F;
	    private float yawn = 0;
	    
	    public float getNearPlane()
	    {
	    	return this.zNear;
	    }
    
	    public float getFarPlane()
	    {
	    	return this.zFar;
	    }
    
	  public Matrix4f createViewMatrix() 
	  {
	       Matrix4f matrix = new Matrix4f();
	       matrix.setIdentity();
	       Matrix4f.rotate((float)Math.toRadians(zPitch), new Vector3f(0, 0, 1), matrix, matrix);
	       Matrix4f.rotate((float)Math.toRadians(pitch), new Vector3f(1, 0, 0), matrix, matrix);
	       Matrix4f.rotate((float)Math.toRadians(yawn), new Vector3f(0, 1, 0), matrix, matrix);
	       Matrix4f.translate(new Vector3f(-camX,-camY,-camZ), matrix, matrix);
	       return matrix;
	  }
	  
	  public static void creatWindow()
	  {
		  MiystEngine.miystengine.fbo = new Fbo(MiystEngine.miystengine.getScreenWidth(), MiystEngine.miystengine.getScreenHeight());	
		  MiystEngine.miystengine.fbo_Out = new Fbo(MiystEngine.miystengine.getScreenWidth(), MiystEngine.miystengine.getScreenHeight(), Fbo.DEPTH_TEXTURE);
	  }
	  
	  public void setupOverlayRendering()
	  {
		  ScaledResolution scale = new ScaledResolution(MiystEngine.miystengine, MiystEngine.miystengine.getScreenWidth(), MiystEngine.miystengine.getScreenHeight());
		  GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		  GL11.glMatrixMode(GL11.GL_PROJECTION);
		  GL11.glLoadIdentity();
		  GL11.glOrtho(0.0D, scale.getScaledWidth_double(), scale.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
		  GL11.glMatrixMode(GL11.GL_MODELVIEW);
		  GL11.glLoadIdentity();
		  GL11.glTranslatef(0F, 0F, -2000F);
	  }
	  
	  public void updateCameraRender(float ticker)
	  {    
		  MiystEngine.miystengine.fbo.bindFrameBuffer();
	      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	      ScaledResolution sr = new ScaledResolution(MiystEngine.miystengine, MiystEngine.miystengine.getScreenWidth(), MiystEngine.miystengine.getScreenHeight());
	      int width = Mouse.getX() * sr.getScaledWidth() / MiystEngine.miystengine.getScreenWidth();
	      int height = sr.getScaledHeight() - Mouse.getY() * sr.getScaledHeight() / MiystEngine.miystengine.getScreenHeight() - 1;
	      GL11.glViewport(0, 0, MiystEngine.miystengine.getScreenWidth(), MiystEngine.miystengine.getScreenHeight());
	      GL11.glMatrixMode(GL11.GL_PROJECTION);
	      GL11.glLoadIdentity();
	      GL11.glMatrixMode(GL11.GL_MODELVIEW);
	      GL11.glLoadIdentity();
	      this.setupOverlayRendering();
	      GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
	      try
	      {
	    	  if(MiystEngine.miystengine.getCurrentScreen() != null)
	    	  {
	    		  MiystEngine.miystengine.getCurrentScreen().drawScreen(width, height);
	    	  }
	      }
	      catch (Throwable var12)
	      {
	    	  var12.printStackTrace();
	      }
	      MiystEngine.miystengine.fbo.unbindFrameBuffer();   
	      MiystEngine.miystengine.fbo.resolveToFBO(MiystEngine.miystengine.fbo_Out);
	      
	      MiystEngine.miystengine.fbo.framebufferRender(0,0,MiystEngine.miystengine.getScreenWidth(),MiystEngine.miystengine.getScreenHeight(),MiystEngine.miystengine.fbo_Out.getColourTexture());
	   }
	 
	  
}