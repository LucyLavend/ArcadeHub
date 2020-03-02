package nl.miystengine.client.audio;


public class SourceArrayList
{
	private Source source;
	
    public SourceArrayList(Source source)
    {
    	this.source = source;
    }
    
    public Source getSource()
    {
    	return source;
    }
    
    public void setSource(Source source)
    {
    	this.source = source;
    }
}