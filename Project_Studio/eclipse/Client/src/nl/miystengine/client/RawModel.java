package nl.miystengine.client;
 
public class RawModel 
{
	public int vaoID;
	public int vertexCount;
     
    public RawModel(int vaoID,int vertexCount)
    {
        this.vaoID=vaoID;
        this.vertexCount=vertexCount;
    }
 
    public int getVaoID(){return vaoID;}
    public int getVertexCount(){return vertexCount;}
}
     
     
 