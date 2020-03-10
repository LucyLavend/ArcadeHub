package nl.miystengine.client.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.PriorityQueue;
import nl.miystengine.client.MiystEngine;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Tessellator
{
    /** The byte buffer used for GL allocation. */
    private ByteBuffer byteBuffer;

    /** The same memory as byteBuffer, but referenced as an integer buffer. */
    private IntBuffer intBuffer;

    /** The same memory as byteBuffer, but referenced as an float buffer. */
    private FloatBuffer floatBuffer;

    /** The same memory as byteBuffer, but referenced as an short buffer. */
    private ShortBuffer shortBuffer;

    /** Raw integer array. */
    private int[] rawBufferList;

    /**
     * The number of vertices to be drawn in the next draw call. Reset to 0 between draw calls.
     */
    private int vertexCount;

    /** The first coordinate to be used for the texture. (X)
     * 
     */
    public double textureU;

    /** The second coordinate to be used for the texture. (Y)
     * 
     */
    private double textureV;

    /** The color (RGBA) value to be used for the following draw call. */
    private int color;

    /**
     * Whether the current draw object for this tessellator has color values.
     */
    public boolean hasColor;

    /**
     * Whether the current draw object for this tessellator has texture coordinates.
     */
    public boolean hasTexture;

    /**
     * Whether the current draw object for this tessellator has normal values.
     */
    private boolean hasNormals;

    /** The index into the raw buffer to be used for the next data. */
    private int rawBufferIndex;

    /**
     * The number of vertices manually added to the given draw call. This differs from vertexCount because it adds extra
     * vertices when converting quads to triangles.
     */
    private int addedVertices;


    /** The draw mode currently being used by the tessellator. */
    private int drawMode;

    /**
     * An offset to be applied along the x-axis for all vertices in this draw call.
     */
    private double xOffset;

    /**
     * An offset to be applied along the y-axis for all vertices in this draw call.
     */
    private double yOffset;

    /**
     * An offset to be applied along the z-axis for all vertices in this draw call.
     */
    private double zOffset;

    /** The normal to be applied to the face being drawn. */
    private int normal;

    /** The static instance of the Tessellator. */
    public static final Tessellator instance = new Tessellator(2097152);

    /** Whether this tessellator is currently in draw mode. */
    public boolean isDrawing;

    /** The size of the buffers used (in integers). */
    private int bufferSize;
    
    public boolean renderShader = false;
    float[] positions;

    private Tessellator(int b)
    {
        this.bufferSize = b;
        this.byteBuffer = GLAllocation.createDirectByteBuffer(b * 4);
        this.intBuffer = this.byteBuffer.asIntBuffer();
        this.floatBuffer = this.byteBuffer.asFloatBuffer();
        this.shortBuffer = this.byteBuffer.asShortBuffer();
        this.rawBufferList = new int[b];
    }

    /**
     * Draws the data set up in this tessellator and resets the state to prepare for new drawing.
     */
    public int draw()
    {
        if (!this.isDrawing)
        {
            throw new IllegalStateException("Tesselating has not been called!");
        }
        else
        {
            this.isDrawing = false;
            if (this.vertexCount > 0)
            {
                this.intBuffer.clear();
                this.intBuffer.put(this.rawBufferList, 0, this.rawBufferIndex);
                this.byteBuffer.position(0);
                this.byteBuffer.limit(this.rawBufferIndex * 4);
                if (this.hasTexture)
                {
                    this.floatBuffer.position(3);
                    GL11.glTexCoordPointer(2, 32, this.floatBuffer);
                    GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
                }
                if (this.hasColor)
                {
                    this.byteBuffer.position(20);
                    GL11.glColorPointer(4, true, 32, this.byteBuffer);
                    GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
                }
                if (this.hasNormals)
                {
                    this.byteBuffer.position(24);
                    GL11.glNormalPointer(32, this.byteBuffer);
                    GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
                }
                this.floatBuffer.position(0);
                GL11.glVertexPointer(3, 32, this.floatBuffer);
                GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
                GL11.glDrawArrays(this.drawMode, 0, this.vertexCount);
                GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);        
                if (this.hasTexture)
                {
                    GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
                }
                if (this.hasColor)
                {
                    GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
                }
                if (this.hasNormals)
                {
                    GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
                }
            }
            int var1 = this.rawBufferIndex * 4;
            this.reset();
            return var1;
        }
    }

    public float XTranslate = 0;
    public float YTranslate = 0;
    public float ZTranslate = 0;
    
    public float XScale = 1F;
    public float YScale = 1F;
    public float ZScale = 1F;
    
    /**
     * Set the Position of the object.
     * Call it before drawing something!
     * Call drawST() at the end to reset every object from changing the position.
     * @param x
     * @param y
     * @param z
     */
    public void setTranslate(float x,float y,float z)
    {
    	this.XTranslate += x;
    	this.YTranslate += y;
    	this.ZTranslate += z;
    }
    
    /**
     * Scale of the object.
     * Call it before drawing something!
     * Call drawST() at the end to reset every object from changing the Scale.
     * @param x: Scale x
     * @param y: scale y
     * @param z: scale z
     * @param X: X position
     * @param Y: Y position
     * @param Z: Z position
     */
    public void setScale(float x,float y,float z,float X,float Y,float Z)
    {
    	this.XScale = x;
    	this.YScale = y;
    	this.ZScale = z;
    	if(x != 1F)
    	{
    	this.setTranslate((X-(X*x))+0.5F,0.0F,0F);
    	}
    	if(y != 1F)
    	{
    	this.setTranslate(0F, (Y-(Y*y)),0F);
    	}
    	if(z != 1F)
    	{
    	this.setTranslate(0F, 0F,(Z-(Z*z))+0.5F);
    	}
    }

    /**
     * Scale of the object.
     * Call it before drawing something!
     * Call drawST() at the end to reset every object from changing the Scale.
     * @param x: Scale x
     * @param y: scale y
     * @param z: scale z
     * @param X: X position
     * @param Y: Y position
     * @param Z: Z position
     */
    public void setScale(double x, double y, double z, double X,double Y,double Z) 
    {
    	this.XScale = (float) x;
    	this.YScale = (float) y;
    	this.ZScale = (float) z;
    	if(x != 1F)
    	{
    	this.setTranslate((float) ((X-(X*x))+0.5F),0.0F,0F);
    	}
    	if(y != 1F)
    	{
    	this.setTranslate(0F, (float) (Y-(Y*y)),0F);
    	}
    	if(z != 1F)
    	{
    	this.setTranslate(0F, 0F,(float) ((Z-(Z*z))+0.5F));
    	}
	}
    
    
    public float rotationY = 0F;
    
    /**
     * Rotation at the Y.
     * Call it before drawing something!
     * Call drawST() at the end to reset every object from changing the rotation.
     * @param x: Scale x
     * @param y: scale y
     * @param z: scale z
     * @param X: X position
     * @param Y: Y position
     * @param Z: Z position
     */
    public void setRotationY(float rotate)
    {
    	this.rotationY = rotate;
    }
    
    /**
     * Draw and reset the Scale, Translate and Rotation of the given object.
     */
    public void drawST()
    {
    	this.XTranslate = 0;
    	this.YTranslate = 0;
    	this.ZTranslate = 0;
    	this.XScale = 1F;
   		this.YScale = 1F;
   		this.ZScale = 1F;
   		this.rotationY = 0F;
    }

    /**
     * Clears the tessellator state in preparation for new drawing.
     */
    private void reset()
    {
        this.vertexCount = 0;
        this.byteBuffer.clear();
        this.rawBufferIndex = 0;
        this.addedVertices = 0;
    }

    /**
     * Sets draw mode in the tessellator to draw quads.
     */
    public void startDrawingQuads()
    {
        this.startDrawing(7);
    }

    /**
     * Sets draw mode in the tessellator to draw quads.
     */
    public void startDrawingQuadsWithShaders()
    {
    	this.startDrawing(7);
        this.renderShader = true;
    }

    /**
     * Resets tessellator state and prepares for drawing (with the specified draw mode).
     */
    public void startDrawing(int t)
    {
        if (this.isDrawing)
        {
        	this.draw();
        }
        else
        {
            this.isDrawing = true;
            this.reset();
            this.drawMode = t;
            this.hasNormals = false;
            this.hasColor = false;
            this.hasTexture = false;
        }
    }

    public int vertexAdded = 0;
    
    /**
     * Adds a vertex specifying both x,y,z and the texture u,v for it.
     */
    public void addVertexWithUV(double x, double y, double z, double u, double v)
    {
    	this.hasTexture = true;
        this.textureU = u;
        this.textureV = v;
        if(this.rotationY > 0 && (this.vertexAdded == 1 || this.vertexAdded == 2))
        {
        	this.addVertex(((x*this.XScale)+this.XTranslate)-(this.rotationY/100D), (y*this.YScale)+this.YTranslate, ((z*this.ZScale)+this.ZTranslate)+(this.rotationY/100D));
        }
        else  if(this.rotationY > 0 && (this.vertexAdded == 3 || this.vertexAdded == 4))
        {
        	this.addVertex(((x*this.XScale)+this.XTranslate)+(this.rotationY/100D), (y*this.YScale)+this.YTranslate, ((z*this.ZScale)+this.ZTranslate)-(this.rotationY/100D));
        }
        else
        {
        	 this.addVertex(((x*this.XScale)+this.XTranslate), (y*this.YScale)+this.YTranslate, (z*this.ZScale)+this.ZTranslate);
        }
        ++this.vertexAdded;
        if(this.vertexAdded == 4)
        {
        	this.vertexAdded = 0;
        }
    }
    
    
    /**
     * Adds a vertex specifying both x,y,z and the texture u,v for it.
     */
    public void addVertexTerrain(double x, double y, double z, double u, double v)
    {
    	this.hasTexture = true;
        this.textureU = (u);
        this.textureV = (v);
        if(this.rotationY > 0 && (this.vertexAdded == 1 || this.vertexAdded == 2))
        {
        	this.addVertex(((x*this.XScale)+this.XTranslate)-(this.rotationY/100D), (y*this.YScale)+this.YTranslate, ((z*this.ZScale)+this.ZTranslate)+(this.rotationY/100D));
        }
        else  if(this.rotationY > 0 && (this.vertexAdded == 3 || this.vertexAdded == 4))
        {
        	this.addVertex(((x*this.XScale)+this.XTranslate)+(this.rotationY/100D), (y*this.YScale)+this.YTranslate, ((z*this.ZScale)+this.ZTranslate)-(this.rotationY/100D));
        }
        else
        {
        	 this.addVertex(((x*this.XScale)+this.XTranslate), (y*this.YScale)+this.YTranslate, (z*this.ZScale)+this.ZTranslate);
        }
        ++this.vertexAdded;
        if(this.vertexAdded == 4)
        {
        	this.vertexAdded = 0;
        }
    }

    /**
     * Adds a vertex with the specified x,y,z to the current draw call. It will trigger a draw() if the buffer gets
     * full.
     */
    public void addVertex(double x, double y, double z)
    {
        ++this.addedVertices;
        if (this.hasTexture)
        {
            this.rawBufferList[this.rawBufferIndex + 3] = Float.floatToRawIntBits((float)this.textureU);
            this.rawBufferList[this.rawBufferIndex + 4] = Float.floatToRawIntBits((float)this.textureV);
        }
        if (this.hasColor)
        {
            this.rawBufferList[this.rawBufferIndex + 5] = this.color;
        }
        if (this.hasNormals)
        {
            this.rawBufferList[this.rawBufferIndex + 6] = this.normal;
        } 
        this.rawBufferList[this.rawBufferIndex + 0] = Float.floatToRawIntBits((float)(x + this.xOffset));
        this.rawBufferList[this.rawBufferIndex + 1] = Float.floatToRawIntBits((float)(y + this.yOffset));
        this.rawBufferList[this.rawBufferIndex + 2] = Float.floatToRawIntBits((float)(z + this.zOffset));
        this.rawBufferIndex += 8;
        ++this.vertexCount;
        if (this.vertexCount % 4 == 0 && this.rawBufferIndex >= this.bufferSize - 32)
        {
        	this.draw();
            this.isDrawing = true;
        }
    }
}
