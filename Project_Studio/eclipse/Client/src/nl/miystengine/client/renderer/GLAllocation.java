package nl.miystengine.client.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

public class GLAllocation
{  
    public static synchronized ByteBuffer createDirectByteBuffer(int i)
    {
        return ByteBuffer.allocateDirect(i).order(ByteOrder.nativeOrder());
    }

    public static IntBuffer createDirectIntBuffer(int i)
    {
        return createDirectByteBuffer(i << 2).asIntBuffer();
    }

    public static FloatBuffer createDirectFloatBuffer(int i)
    {
        return createDirectByteBuffer(i << 2).asFloatBuffer();
    }
}
