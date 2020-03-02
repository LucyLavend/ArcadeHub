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
    /**
     * Creates and returns a direct byte buffer with the specified capacity. Applies native ordering to speed up access.
     */
    public static synchronized ByteBuffer createDirectByteBuffer(int gen_functioni_74524_0_)
    {
        return ByteBuffer.allocateDirect(gen_functioni_74524_0_).order(ByteOrder.nativeOrder());
    }

    /**
     * Creates and returns a direct int buffer with the specified capacity. Applies native ordering to speed up access.
     */
    public static IntBuffer createDirectIntBuffer(int gen_functioni_74527_0_)
    {
        return createDirectByteBuffer(gen_functioni_74527_0_ << 2).asIntBuffer();
    }

    /**
     * Creates and returns a direct float buffer with the specified capacity. Applies native ordering to speed up
     * access.
     */
    public static FloatBuffer createDirectFloatBuffer(int gen_functioni_74529_0_)
    {
        return createDirectByteBuffer(gen_functioni_74529_0_ << 2).asFloatBuffer();
    }
}
