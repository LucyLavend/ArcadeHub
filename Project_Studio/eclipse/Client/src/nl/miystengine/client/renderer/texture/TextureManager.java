package nl.miystengine.client.renderer.texture;

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
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import javax.imageio.ImageIO;
import nl.miystengine.client.FileBasicJava;
import nl.miystengine.client.MiystEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TextureManager implements ITickable
{
    private final Map mapTextureObjects = Maps.newHashMap();
    private final List listTickables = Lists.newArrayList();
    private final Map mapTextureCounters = Maps.newHashMap();
    private IResourceManager theResourceManager;
    public static List<ArrayListTextures> textureList = new ArrayList<ArrayListTextures>();
    public GifReader gifReader;
    
    public TextureManager()
    {
    	this.gifReader = new GifReader();
    }
    
    public static ByteBuffer convertImageData(String s) throws IOException 
    { 
    	BufferedImage Icon = ImageIO.read(new File(s));
        ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),new int[] {8,8,8,8},true,false,ComponentColorModel.TRANSLUCENT,DataBuffer.TYPE_BYTE);
        int texWidth = Icon.getWidth();
        int texHeight = Icon.getHeight();
        WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,texWidth,texHeight,4,null);
        BufferedImage texImage = new BufferedImage(glAlphaColorModel,raster,false,new Hashtable());
        Graphics g = texImage.getGraphics();
        g.setColor(new Color(0f,0f,0f,0f));
        g.fillRect(0,0,texWidth,texHeight);
        g.drawImage(Icon,0,0,null);
        byte[] data = ((DataBufferByte)texImage.getRaster().getDataBuffer()).getData(); 
        ByteBuffer imageBuffer = ByteBuffer.allocateDirect(data.length); 
        imageBuffer.put(data, 0, data.length); 
        imageBuffer.flip();
        return imageBuffer; 
    } 

    
    public static void loadMipmap(int mipmap,boolean Texture2D,float mipmapBiasExtra)
    {
    	float mipmapBias;
    	GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        mipmapBias = 0F + mipmapBiasExtra;	
        if(GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic)
        {
        	float amount = Math.min(4F, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));	
        	GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
        }
        else
        {
        	System.out.println("Texture Filter Anisotropic NOT supported");
        	System.out.println("Textures far away will appear kinda blurry.....");
        }
        	
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);	
    }
    
    /**
     * bindTheTextureandRemoveEdges when turned true
     * @param source
     */
    public void bindTexture(String source,int mipmap,boolean Texture2D)
    {
    	 try
  	    {
        String sourceFinal = MiystEngine.miystengine.getPath().source + source;
        int textureID;
    	if(sourceFinal.contains("None"))
    	{
    		sourceFinal = sourceFinal.replace("None", "Normal");
    	}
    	
    	Object o = (ITextureObject)this.mapTextureObjects.get(sourceFinal);
    	
        if(!sourceFinal.contains("None"))
        {
        	if(o == null)
        	{
        		try
        		{
        			textureID = this.loadTexture(sourceFinal, (ITextureObject)(o = new SimpleTexture(sourceFinal)));
        			textureList.add(new ArrayListTextures(((ITextureObject)o).getGlTextureId()));
        			this.loadMipmap(mipmap, Texture2D,1F);
        		}
        		catch(Exception e)
        		{
        			textureID = ((ITextureObject)o).getGlTextureId();
        		}
        	}
        	else
        	{
        		textureID = ((ITextureObject)o).getGlTextureId();
        	}
        	GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
       } 
  	  }
  	    catch (Exception v)
  	    {
  	    	v.printStackTrace();
  	    }
    }
    
    public int loadTexture(String source)
    {
    	int textureID;
    	Object o = (ITextureObject)this.mapTextureObjects.get(source);
        if(o == null)
        {
        	try
        	{
        		textureID = this.loadTexture(source, (ITextureObject)(o = new SimpleTexture(source)));
        		textureList.add(new ArrayListTextures(((ITextureObject)o).getGlTextureId()));
        		TextureManager.loadMipmap(2, false,1F);
        		return textureID;
        	}
        	catch(Exception e)
        	{
        		e.printStackTrace();	
        		return ((ITextureObject)o).getGlTextureId();
        	}
        }
        else return ((ITextureObject)o).getGlTextureId();
	 }  
    
    public static int textureSize = 0;
    

    public int loadTexture(String loc, final ITextureObject ito)
    {
        boolean isTexture = ((ITextureObject)ito).loadTexture(this.theResourceManager);
        this.mapTextureObjects.put(loc, ito);
        if(!isTexture)
        {
           System.out.println("[Server thread/ERROR]: TextureManager Error: Texture Missing in SimpleTexture,TextureManager and TextureUtil.");
           System.out.println("Texture Location: "+loc);
           return 0;
        }
        else return ito.getGlTextureId();
    }
   
    public ITextureObject getTexture(String s)
    {
        return (ITextureObject)this.mapTextureObjects.get(s);
    }

    public void tick()
    {
        Iterator var1 = this.listTickables.iterator();
        while (var1.hasNext())
        {
            ITickable var2 = (ITickable)var1.next();
            var2.tick();
        }
    }

}
