package nl.miystengine.client;

public class PassingSeconds
{
	private long second = 0;
    public boolean secondspassed = false;
    
    public void seconds()
    {
    	if(System.nanoTime() > second + 10000000)
        {
    		secondspassed = true;
    		second = System.nanoTime();
        }
    }
    
    public long getTimePassed()
    {
    	return second;
    }
}