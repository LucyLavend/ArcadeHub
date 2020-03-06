package nl.miystengine.client.shader;
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import nl.miystengine.client.ArrayListShadersLoaded;
import nl.miystengine.client.FileBasicJava;
import nl.miystengine.client.MiystEngine;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public abstract class ShaderProgram 
{
    protected int programID;
    protected int vertexShaderID;
    protected int geometryShaderID;
    protected int fragmentShaderID;
    public static int location_viewMatrix;
    private static FloatBuffer matrixB = BufferUtils.createFloatBuffer(16);
    public static List<ArrayListShadersLoaded> shadersList = new ArrayList<ArrayListShadersLoaded>();

    public ShaderProgram(String vertexFile,String fragmentFile,String geometryFile,boolean hasGeometry)
    {
    	vertexShaderID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER);
    	geometryShaderID = loadShader(geometryFile, GL32.GL_GEOMETRY_SHADER);
    	fragmentShaderID = loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);
    	programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
    	GL20.glAttachShader(programID, geometryShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);    
        String shaderName = vertexFile.replace("VertexShader.txt", "");
        String finalName = shaderName.replace(MiystEngine.miystengine.getPath().sourceShaders, "");
        shadersList.add(new ArrayListShadersLoaded(finalName,programID, vertexFile, fragmentFile, vertexShaderID, fragmentShaderID));
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocations();
    }
    
    public ShaderProgram(String vertexFile,String fragmentFile)
    {
    	vertexShaderID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER);
    	fragmentShaderID = loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);
    	programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);    
        String shaderName = vertexFile.replace("VertexShader.txt", "");
        String finalName = shaderName.replace(MiystEngine.miystengine.getPath().sourceShaders, "");
        shadersList.add(new ArrayListShadersLoaded(finalName,programID, vertexFile, fragmentFile, vertexShaderID, fragmentShaderID));
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocations();
    }
    
    public ShaderProgram(){}
    
    public ShaderProgram(String vertexFile,String fragmentFile, String[] inVariables) 
	{
		int vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		int fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		String shaderName = vertexFile.replace("VertexShader.txt", "");
	    String finalName = shaderName.replace(MiystEngine.miystengine.getPath().sourceShaders, "");
	    shadersList.add(new ArrayListShadersLoaded(finalName,programID, vertexFile, fragmentFile, vertexShaderID, fragmentShaderID));
		bindAttributes(inVariables);
		GL20.glLinkProgram(programID);
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
	}
   
	private void bindAttributes(String[] inVariables)
    {
		for(int i=0;i<inVariables.length;i++)
		{
			GL20.glBindAttribLocation(programID, i, inVariables[i]);
		}
	}

    public void loadViewMatrix()
    {
        this.loadMatrix(location_viewMatrix, MiystEngine.miystengine.getCamera().createViewMatrix());
    }
     
    protected void getAllUniformLocations()
    {
    	this.location_viewMatrix = this.getUniformLocation("viewMatrix");
    }
     
    protected int getUniformLocation(String uniformName)
    {
        return GL20.glGetUniformLocation(programID,uniformName);
    }
     
    public void start()
    {
        GL20.glUseProgram(programID);
    }
     
    public void stop()
    {
        GL20.glUseProgram(0);
    }
     
    public void cleanUp()
    {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }
     
    protected abstract void bindAttributes();
     
    protected void bindAttribute(int attribute, String variableName)
    {
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }
     
    protected void loadFloat(int location, float value)
    {
        GL20.glUniform1f(location, value);
    }
    
    protected void loadInt(int location, int value)
    {
        GL20.glUniform1i(location, value);
    }
 
    protected void loadVector(int location, Vector3f vector)
    {
        GL20.glUniform3f(location,vector.x,vector.y,vector.z);
    }
    
    protected void loadVector(int location, Vector4f vector)
    {
        GL20.glUniform4f(location,vector.x,vector.y,vector.z,vector.w);
    }
     
    protected void loadBoolean(int location, boolean value)
    {
        int toLoad = 0;
        if(value)
        {
        	toLoad = 1;
        }
        GL20.glUniform1i(location, toLoad);
    }
     
    protected void loadMatrix(int location, Matrix4f matrix)
    {	
    	matrix.store(matrixB);
    	matrixB.flip();
        GL20.glUniformMatrix4(location, false, matrixB);
    }
    
    private static String errorline;
    
    protected static int loadShader(String file, int type)
    {
        StringBuilder shaderSource = new StringBuilder();
        System.out.println("[Client] Read Shader: " + file);
        String line;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine()) != null)
            {
                shaderSource.append(line).append("//\n");
                errorline = line;
            }
            reader.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            //System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE)
        {
        	//IngameGui.arraylistTips.add(new ArrayListStrings("Could not compile shader!", 2000,new float[]{1F,0F,0F}));
          	//IngameGui.arraylistTips.add(new ArrayListStrings("Shader: "+file+" Error at character " + errorline, 2000,new float[]{1F,0F,0F}));
          	//IngameGui.arraylistTips.add(new ArrayListStrings("Could not compile shader!", 2000,new float[]{1F,0F,0F}));
          	//IngameGui.arraylistTips.add(new ArrayListStrings(GL20.glGetShaderInfoLog(shaderID, 500), 2000,new float[]{1F,0F,0F}));
            System.err.println("Could not compile shader!");
            System.err.println("Shader: "+file+" Error at character " + errorline);
            System.err.println(GL20.glGetShaderInfoLog(shaderID, 500));
            //MiystEngine.miystengine.shutdownApplet();
        }
        
        return shaderID;
    }

	public void load4FVector(int location_offset, Vector4f vector) 
	{
		GL20.glUniform4f(location_offset,vector.x,vector.y,vector.z,vector.w);	
	}
	
	public void load3FVector(int location_offset, Vector3f vector) 
	{
		GL20.glUniform3f(location_offset,vector.x,vector.y,vector.z);	
	}

	public void load2fVector(int location_addLight, Vector2f vector2f) 
	{
		GL20.glUniform2f(location_addLight,vector2f.x,vector2f.y);
	}

 
}