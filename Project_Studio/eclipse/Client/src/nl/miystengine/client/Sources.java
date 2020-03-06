package nl.miystengine.client;

import java.util.Calendar;


public class Sources
{
	public String currentUsersHomeDir = System.getProperty("user.home");
	public String Disk = "C";
	public Calendar cal = Calendar.getInstance();
	public String name;
	public String source = "../textures/";
	public String sources = Disk + ":\\";
	public String sourceShaders = "../shaders/";
	public String date = cal.getTime()+"/";
	public String version = "1.0.0";
	
}