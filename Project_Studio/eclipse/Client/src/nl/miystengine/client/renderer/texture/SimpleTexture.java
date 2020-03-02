package nl.miystengine.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class SimpleTexture implements ITextureObject
{
	private TextureUtil textureutil;
    protected  String textureLocation;
 
    protected int glTextureId = -1;
    
    public SimpleTexture(String tl)
    {
    	super();
        this.textureLocation = tl;
        this.textureutil = new TextureUtil();
    }

	public int getGlTextureId()
    {
        if (this.glTextureId == -1)
        {
            this.glTextureId = GL11.glGenTextures();
        }

        return this.glTextureId;
    }
	
	public boolean loadTextureWImageBuffer(IResourceManager irm,BufferedImage image)
    {
    	if (this.glTextureId != -1)
        {
            GL11.glDeleteTextures(this.glTextureId);
            this.glTextureId = -1;
        }
        InputStream is = null;
        try
        {
            if(this.textureLocation != null)
            {
            	BufferedImage bi = image;
            	this.textureutil.uploadTextureImage(this.getGlTextureId(), bi);
        	}
            return true;
        }
        finally
        {
            if (is != null)
            {
                try 
                {
					is.close();
				} 
                catch (IOException e) 
				{
					e.printStackTrace();
					return false;
				}
            }
        }
    }

    public boolean loadTexture(IResourceManager irm)
    {
    	if (this.glTextureId != -1)
        {
            GL11.glDeleteTextures(this.glTextureId);
            this.glTextureId = -1;
        }
        InputStream is = null;
        try
        {
            if(this.textureLocation != null)
            {
            	BufferedImage bi = ImageIO.read(new File(this.textureLocation));
            	this.textureutil.uploadTextureImage(this.getGlTextureId(), bi);
        	}
            return true;
        }
        catch (IOException e) 
        {
			return false;
		}
        finally
        {
            if (is != null)
            {
                try 
                {
					is.close();
				} 
                catch (IOException e) 
				{
					e.printStackTrace();
					return false;
				}
            }
        }
    }

}
