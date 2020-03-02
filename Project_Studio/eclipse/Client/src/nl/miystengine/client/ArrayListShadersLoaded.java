package nl.miystengine.client;


public class ArrayListShadersLoaded
{
	public String shaderName;
	public String shaderNameLocationVertex;
	public String shaderNameLocationFragment;
	
	public int shaderNameVertexID;
	public int shaderNameFragmentID;
	public int programID;


	public ArrayListShadersLoaded(String shaderName,int programID,String vertexN,String fragmentN,int vertexID,int fragmentID)
	{
		this.shaderName = shaderName;
		this.programID = programID;
		this.shaderNameLocationVertex = vertexN;
		this.shaderNameLocationFragment = fragmentN;
		this.shaderNameVertexID = vertexID;
		this.shaderNameFragmentID = fragmentID;	
	}
	
	private int shaderID;
	
}
