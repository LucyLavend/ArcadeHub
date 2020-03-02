package nl.miystengine.client.screen;

import nl.miystengine.client.MiystEngine;
import nl.miystengine.client.gui.MathHelper;

public class ScaledResolution
{
    private int scaledWidth;
    private int scaledHeight;
    private double scaledWidthD;
    private double scaledHeightD;
    private double scaleFactor;

    public ScaledResolution(MiystEngine tsw, int w, int h)
    {
        this.scaledWidth = w;
        this.scaledHeight = h;
        this.scaleFactor = 2;
        this.scaledWidthD = (double)this.scaledWidth / this.scaleFactor;
        this.scaledHeightD = (double)this.scaledHeight / this.scaleFactor;
        this.scaledWidth = MathHelper.ceiling_double_int(this.scaledWidthD);
        this.scaledHeight = MathHelper.ceiling_double_int(this.scaledHeightD);
    }

    public int getScaledWidth()
    {
        return this.scaledWidth;
    }

    public int getScaledHeight()
    {
        return this.scaledHeight;
    }

    public double getScaledWidth_double()
    {
        return this.scaledWidthD;
    }

    public double getScaledHeight_double()
    {
        return this.scaledHeightD;
    }
}
