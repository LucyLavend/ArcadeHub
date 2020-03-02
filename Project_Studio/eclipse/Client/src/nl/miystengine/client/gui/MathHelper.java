package nl.miystengine.client.gui;

import java.util.Random;

public class MathHelper
{
	
    public static int ceiling_float_int(float getFloat)
    {
        int var1 = (int)getFloat;
        return getFloat > (float)var1 ? var1 + 1 : var1;
    }

    public static int ceiling_double_int(double getDouble)
    {
        int var2 = (int)getDouble;
        return getDouble > (double)var2 ? var2 + 1 : var2;
    }
}