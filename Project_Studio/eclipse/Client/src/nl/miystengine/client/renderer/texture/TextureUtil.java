package nl.miystengine.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import nl.miystengine.client.MiystEngine;
import nl.miystengine.client.renderer.GLAllocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;

public class TextureUtil
{
    private IntBuffer dataBuffer = GLAllocation.createDirectIntBuffer(4194304);
  
    public int uploadTextureImage(int texture, BufferedImage bi)
    {
    	GL11.glDeleteTextures(texture);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        ContextCapabilities var0 = GLContext.getCapabilities();
        if (var0.GL_EXT_texture_filter_anisotropic)
        {
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, 34046, 1.0F);
        }
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, bi.getWidth() >> 0, bi.getHeight() >> 0, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, (IntBuffer)null);
    	int widht = bi.getWidth();
        int height = bi.getHeight();
        int var1 = 4194304 / widht;
        int[] var2 = new int[var1 * widht];
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        for (int var9 = 0; var9 < widht * height; var9 += widht * var1)
        {
            int var10 = var9 / widht;
            int var11 = Math.min(var1, height - var10);
            int var12 = widht * var11;
            bi.getRGB(0, var10, widht, var11, var2, 0, widht);
            int[] var3 = var2;
            dataBuffer.clear();
            dataBuffer.put(var3, 0, var12);
            dataBuffer.position(0).limit(var12);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D,0,0,0 + var10, widht, var11, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, dataBuffer);
        }
        return texture;
    }

}
