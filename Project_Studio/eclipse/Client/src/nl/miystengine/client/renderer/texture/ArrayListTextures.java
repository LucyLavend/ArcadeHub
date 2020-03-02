package nl.miystengine.client.renderer.texture;

public class ArrayListTextures
{
	public int textureID;
	public String extraNameData = "";

	public ArrayListTextures(int textureID)
	{
		this.textureID = textureID;
	}
	
	public ArrayListTextures(int textureID,String extraNameData)
	{
		this.textureID = textureID;
		this.extraNameData = extraNameData;
	}
}