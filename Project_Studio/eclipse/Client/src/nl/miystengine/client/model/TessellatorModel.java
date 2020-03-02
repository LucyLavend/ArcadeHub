package nl.miystengine.client.model;

import java.util.Random;

import nl.miystengine.client.Loader;
import nl.miystengine.client.RawModel;

import org.lwjgl.util.vector.Vector3f;


public class TessellatorModel 
{
	public TessellatorModel()
	{
		this.instance = this;
	}
	
	TessellatorModel instance;
	float[]vertices;
	int verticesNumber = -1;
	float[]textureCoords;
	int indicesNumber = -1;
	int textureCoordsNumber = -1;
	int[]indices;
	float[]normals;
	int normalsNumber = -1;
	public RawModel model;
	public boolean switchVertices = false;
	public int switchVerticesNumber = 0;
	public int maxIndices=0;
	public int IndicesCounter = 0;
	private boolean increaseY = false;
	private float increaseYF = 0F;
	private boolean increaseX = false;
	private float increaseXF = 0F;
	private boolean increaseZ = false;
	private float increaseZF = 0F;
	
	/**
	 * Code to check Texture use area.
	 */
	private float textureUMin = 0F;
	private float textureUMax = 0F;
	private float textureVMin = 0F;
	private float textureVMax = 0F;

	/**
	 * How many double triangles you draw(4 x addVertexTerrain = 1 vertc)
	 */
	public void addVerticesNumber(int vertc)
	{
		int VERTC = vertc*4;
		this.vertices = new float[3*VERTC];
		this.textureCoords = new float[2*VERTC];
		this.maxIndices = (2*VERTC)-(((3*VERTC)-(2*VERTC))/2);
		this.indices = new int[this.maxIndices];
		this.normals = new float[3*VERTC];
	}
	
	public void addVTCIN(int indices,int vertices,int textureCoords,int normals)
	{
		this.indices = new int[indices];
		this.vertices = new float[vertices];
		this.textureCoords = new float[textureCoords];
		this.normals = new float[normals];
	}
	
	public void addVerticesandIndices(int i,double x,double y,double z)
	{
		this.vertices[++this.verticesNumber] = (float)x+this.increaseXF;
		this.vertices[++this.verticesNumber] = (float)y+this.increaseYF;
		this.vertices[++this.verticesNumber] = (float)z+this.increaseZF;
		this.indices[++this.indicesNumber] = i;
	}
	
	public void addIndices(int i)
	{
		this.indices[++this.indicesNumber] = i;
	}
	
	public void addVertices(double x,double y,double z)
	{
		this.vertices[++this.verticesNumber] = (float)x+this.increaseXF;
		this.vertices[++this.verticesNumber] = (float)y+this.increaseYF;
		this.vertices[++this.verticesNumber] = (float)z+this.increaseZF;
	}
	
	public void addTextureCoords(int u,int v)
	{
		this.textureCoords[++this.textureCoordsNumber] = u;
		this.textureCoords[++this.textureCoordsNumber] = v;
	}
	
	public void addNormals(float x,float y,float z)
	{
		float xx = x;
		float yy = y+this.increaseYF;
		float zz = z;
		if(xx==0){xx=0.1F;}
		if(yy==0){yy=0.1F;}
		if(zz==0){zz=0.1F;}
		Vector3f vec= new Vector3f(xx,yy,zz);
		vec.normalise();
		this.normals[++this.normalsNumber] = vec.x;
		this.normals[++this.normalsNumber] = vec.y;
		this.normals[++this.normalsNumber] = vec.z;	
	}
	
	
	
	public void setIncreaseY(boolean r,float i)
	{
		this.increaseY = r;
		this.increaseYF = i;
	}
	
	public void setIncreaseX(boolean r,float i)
	{
		this.increaseX = r;
		this.increaseXF = i;
	}
	
	public void setIncreaseZ(boolean r,float i)
	{
		this.increaseZ = r;
		this.increaseZF = i;
	}
	
	public void addVertexTerrain(float x,float y,float z,float u,float v,int Vx,int Vy,int Vz)
	{
		this.vertices[++this.verticesNumber] = x+this.increaseXF;
		this.vertices[++this.verticesNumber] = y+this.increaseYF;
		this.vertices[++this.verticesNumber] = z+this.increaseZF;

		if(u==0F){this.textureCoords[++this.textureCoordsNumber]=0.00097F;}
		else if(u==1F){this.textureCoords[++this.textureCoordsNumber]=1F-0.00097F;}
		else{this.textureCoords[++this.textureCoordsNumber] = u;}
		
		if(v==0F){this.textureCoords[++this.textureCoordsNumber]=0.00097F;}
		else if(v==1F){this.textureCoords[++this.textureCoordsNumber]=1F-0.00097F;}
		else{this.textureCoords[++this.textureCoordsNumber] = v;}
		
		if(u < this.textureUMin)
		{
		this.textureUMin = u;	
		}
		if(v < this.textureVMin)
		{
		this.textureVMin = v;	
		}
		if(u > this.textureUMax)
		{
		this.textureUMax = u;	
		}
		if(v > this.textureVMax)
		{
		this.textureVMax = v;	
		}
		this.indices[++this.indicesNumber] = Vx;
		this.indices[++this.indicesNumber] = Vy;
		this.indices[++this.indicesNumber] = Vz;
		float xx = x;
		float yy = y+this.increaseYF;
		float zz = z;
		if(xx==0){xx=0.1F;}
		if(yy==0){yy=0.1F;}
		if(zz==0){zz=0.1F;}
		Vector3f vec= new Vector3f(xx,yy,zz);
		vec.normalise();
		this.normals[++this.normalsNumber] = vec.x;
		this.normals[++this.normalsNumber] = vec.y;
		this.normals[++this.normalsNumber] = vec.z;
		
		if(this.switchVertices)
		{
		this.switchVertices=false;
		this.switchVerticesNumber+=4;
		this.IndicesCounter+=6;
		}
		else if(!this.switchVertices)
		{
		this.switchVertices=true;
		}
	}
	
	
	public void addVertexTerrain(float x,float y,float z,float u,float v)
	{
		this.vertices[++this.verticesNumber] = x+this.increaseXF;
		this.vertices[++this.verticesNumber] = y+this.increaseYF;
		this.vertices[++this.verticesNumber] = z+this.increaseZF;

		if(u==0F){this.textureCoords[++this.textureCoordsNumber]=0.00097F;}
		else if(u==1F){this.textureCoords[++this.textureCoordsNumber]=1F-0.00097F;}
		else{this.textureCoords[++this.textureCoordsNumber] = u;}
		
		if(v==0F){this.textureCoords[++this.textureCoordsNumber]=0.00097F;}
		else if(v==1F){this.textureCoords[++this.textureCoordsNumber]=1F-0.00097F;}
		else{this.textureCoords[++this.textureCoordsNumber] = v;}
		
		if(u < this.textureUMin)
		{
		this.textureUMin = u;	
		}
		if(v < this.textureVMin)
		{
		this.textureVMin = v;	
		}
		if(u > this.textureUMax)
		{
		this.textureUMax = u;	
		}
		if(v > this.textureVMax)
		{
		this.textureVMax = v;	
		}
		
		if(this.switchVertices && this.IndicesCounter < this.maxIndices)
		{
		this.indices[++this.indicesNumber] = 0+this.switchVerticesNumber;
		this.indices[++this.indicesNumber] = 1+this.switchVerticesNumber;
		this.indices[++this.indicesNumber] = 3+this.switchVerticesNumber;
		}
		else if(!this.switchVertices && this.IndicesCounter < this.maxIndices)
		{
		this.indices[++this.indicesNumber] = 3+this.switchVerticesNumber;
		this.indices[++this.indicesNumber] = 1+this.switchVerticesNumber;
		this.indices[++this.indicesNumber] = 2+this.switchVerticesNumber;
		}
		
		float xx = x;
		float yy = y+this.increaseYF;
		float zz = z;
		if(xx==0){xx=0.1F;}
		if(yy==0){yy=0.1F;}
		if(zz==0){zz=0.1F;}
		Vector3f vec= new Vector3f(xx,yy,zz);
		vec.normalise();
		this.normals[++this.normalsNumber] = vec.x;
		this.normals[++this.normalsNumber] = vec.y;
		this.normals[++this.normalsNumber] = vec.z;
		
		if(this.switchVertices)
		{
		this.switchVertices=false;
		this.switchVerticesNumber+=4;
		this.IndicesCounter+=6;
		}
		else if(!this.switchVertices)
		{
		this.switchVertices=true;
		}
	}
	
	public void addVertexTerrain(double x,double y,double z,double u,double v)
	{
		this.vertices[++this.verticesNumber] = (float)x+this.increaseXF;
		this.vertices[++this.verticesNumber] = (float)y+this.increaseYF;
		this.vertices[++this.verticesNumber] = (float)z+this.increaseZF;
		
		this.textureCoords[++this.textureCoordsNumber] = (float)u;
		this.textureCoords[++this.textureCoordsNumber] = (float)v;
		
		if(this.switchVertices && this.IndicesCounter < this.maxIndices)
		{
		this.indices[++this.indicesNumber] = 0+this.switchVerticesNumber;
		this.indices[++this.indicesNumber] = 1+this.switchVerticesNumber;
		this.indices[++this.indicesNumber] = 3+this.switchVerticesNumber;
		}
		else if(!this.switchVertices && this.IndicesCounter < this.maxIndices)
		{
		this.indices[++this.indicesNumber] = 3+this.switchVerticesNumber;
		this.indices[++this.indicesNumber] = 1+this.switchVerticesNumber;
		this.indices[++this.indicesNumber] = 2+this.switchVerticesNumber;
		}
		
		this.normals[++this.normalsNumber] = 0F;
		this.normals[++this.normalsNumber] = 0.2F;
		this.normals[++this.normalsNumber] = 0F;
		
		if(this.switchVertices)
		{
		this.switchVertices=false;
		this.switchVerticesNumber+=4;
		this.IndicesCounter+=6;
		}
		else if(!this.switchVertices)
		{
		this.switchVertices=true;
		}
	}
	
	public RawModel drawModel()
	{
		this.model = new Loader().loadToVAO(vertices,textureCoords, normals, indices);	
		this.vertices = new float[0];
		this.textureCoords = new float[0];
		this.indices = new int[0];
		this.normals = new float[0];
		this.textureCoordsNumber = -1;
		this.verticesNumber = -1;
		this.normalsNumber = -1;
		this.switchVertices = false;
		this.switchVerticesNumber = 0;
		this.indicesNumber = -1;
		this.increaseY = false;
		this.increaseYF = 0F;
		this.increaseX = false;
		this.increaseXF = 0F;
		this.increaseZ = false;
		this.increaseZF = 0F;
		return this.model;
	}

	public RawModel drawModelVTI()
	{
		this.model = new Loader().loadToVAO(vertices,textureCoords, indices);	
		this.vertices = new float[0];
		this.textureCoords = new float[0];
		this.indices = new int[0];
		this.normals = new float[0];
		this.textureCoordsNumber = -1;
		this.verticesNumber = -1;
		this.normalsNumber = -1;
		this.switchVertices = false;
		this.switchVerticesNumber = 0;
		this.indicesNumber = -1;
		this.increaseY = false;
		this.increaseYF = 0F;
		this.increaseX = false;
		this.increaseXF = 0F;
		this.increaseZ = false;
		this.increaseZF = 0F;
		return this.model;
	}

}