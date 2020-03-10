package nl.miystengine.client.gui;

import java.util.ArrayList;
import java.util.List;
import nl.miystengine.client.MiystEngine;
import nl.miystengine.client.renderer.Tessellator;
import nl.miystengine.client.renderer.texture.ArrayListGif;
import org.lwjgl.opengl.GL11;

public class Gui
{
    protected float zLevel;		
    public static List<ArrayListGif> listOfGifs = new ArrayList<ArrayListGif>();
    

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return true;
    }
    
    /**
     * Update is capped and will not change with higer or lower FPS
     */
    public void updateOnSeconds(){}

    /**
     * Draws a rectangle with a vertical gradient between the specified colors.
     */
    protected void drawGradientRect(int i, int j, int k, int l, int m, int n)
    {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator var15 = Tessellator.instance;
        var15.startDrawingQuads();
        var15.addVertex((double)k, (double)j, (double)this.zLevel);
        var15.addVertex((double)i, (double)j, (double)this.zLevel);
        var15.addVertex((double)i, (double)l, (double)this.zLevel);
        var15.addVertex((double)k, (double)l, (double)this.zLevel);
        var15.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Renders the specified text to the screen, center-aligned.
     */
    public void drawCenteredString(FontRenderer font, String s, int x, int y, int ii)
    {
        font.drawString(s, x - font.getStringWidth(s) / 2, y , ii);
    }
    
    /**
     * Renders the specified text to the screen, center-aligned.
     */
    public void drawCenteredStringWithColor(FontRenderer font, String s, int x, int y, float R, float G, float B, float ALPHA)
    {
        font.drawStringWithColor(s, x - font.getStringWidth(s) / 2, y, R, G, B, ALPHA);
    }
    
    /**
     * Renders the specified text to the screen, center-aligned.
     */
    public void drawStringWithColor(FontRenderer font, String s, int x, int y, float R, float G, float B, float ALPHA)
    {
        font.drawStringWithColor(s, x, y, R, G, B, ALPHA);
    }

    /**
     * Renders the specified text to the screen.
     */
    public void drawString(FontRenderer f, String t, int x, int y, int c)
    {
        f.drawStringWithShadow(t, x, y, c);
    }

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public void drawTexturedModalRect(int x, int y, int u, int v, int w, int h)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator tes = Tessellator.instance;
        tes.startDrawingQuads();
        tes.addVertexWithUV((double)(x + 0), (double)(y + h), (double)this.zLevel, (double)((u + 0) * var7), (double)((v + h) * var8));
        tes.addVertexWithUV((double)(x + w), (double)(y + h), (double)this.zLevel, (double)((u + w) * var7), (double)((v + h) * var8));
        tes.addVertexWithUV((double)(x + w), (double)(y + 0), (double)this.zLevel, (double)((u + w) * var7), (double)((v + 0) * var8));
        tes.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((u + 0) * var7), (double)((v + 0) * var8));
        tes.draw();
    }
    
    
    public void drawTexturedModalRect(int x, int y, int u, int v, int w, int h,int i)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator tes = Tessellator.instance;
        tes.startDrawingQuads();
        tes.addVertexWithUV((double)(x + 0), (double)(y + h), (double)this.zLevel, (double)((u + 0) * var7), (double)((v + h) * var8));
        tes.addVertexWithUV((double)(x + w), (double)(y + h), (double)this.zLevel, (double)((u + w) * var7), (double)((v + h) * var8));
        tes.addVertexWithUV((double)(x + w), (double)(y + 0), (double)this.zLevel, (double)((u + w) * var7), (double)((v + i) * var8));
        tes.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((u + 0) * var7), (double)((v + i) * var8));
        tes.draw();
    }
   
    public void drawTexturedModalRectSize(int x, int y, int u, int v, int w, int h,double widht,double height)
    {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator tes = Tessellator.instance;
        tes.startDrawingQuads();
        tes.addVertexWithUV((double)(x + 0), (double)(y + height), (double)this.zLevel, (double)((u + 0) * var7), (double)((v + h) * var8));
        tes.addVertexWithUV((double)(x + widht), (double)(y + height), (double)this.zLevel, (double)((u + w) * var7), (double)((v + h) * var8));
        tes.addVertexWithUV((double)(x + widht), (double)(y + 0), (double)this.zLevel, (double)((u + w) * var7), (double)((v + 0) * var8));
        tes.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((u + 0) * var7), (double)((v + 0) * var8));
        tes.draw();
    }
    
}
