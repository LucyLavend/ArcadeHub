package nl.miystengine.client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class FileBasicJava
{	
	public static void copyFile(String from, String to)
	{
		   File srcDir = new File(MiystEngine.miystengine.getPath().sources + from);
           File destDir = new File(MiystEngine.miystengine.getPath().sources + to);
           try 
           {
        	   FileUtils.copyFile(srcDir, destDir);
           }
           catch (IOException e) 
           {
        	   e.printStackTrace();
           }
	} 
	
	public static File[] loadFilesFromFolder(String patch)
	{
		File path = new File(patch);
		return path.listFiles();	
	}

	public static File loadFileFromFolder(String patch)
	{
		return new File(patch);	
	}
	
	public static void copyDirectory(String from, String to)
	{
		   File srcDir = new File(MiystEngine.miystengine.getPath().sources + from);
           File destDir = new File(MiystEngine.miystengine.getPath().sources + to);
           try 
           {
        	   FileUtils.copyDirectory(srcDir, destDir);
           }
           catch (IOException e) 
           {
        	   e.printStackTrace();
           }
	}  
      
	public static void backupFile()
	{
		MiystEngine.miystengine.getPath().date = MiystEngine.miystengine.getPath().date.replaceAll(":", ".");
		   
		createFolder(MiystEngine.miystengine.getPath().source + "/../../../backup/" + MiystEngine.miystengine.getPath().date,false);
		   
           File destDir = new File(MiystEngine.miystengine.getPath().source + "/../../../backup/" + MiystEngine.miystengine.getPath().date);
           
           File srcDir = new File(MiystEngine.miystengine.getPath().source + "/../eclipse/Client/src/");
           
           try 
           {
        	   FileUtils.copyDirectory(srcDir, destDir);
           }
           catch (IOException e) 
           {
        	   e.printStackTrace();
           }
	}
	
	public static float trimFloats(float r)
	{
		String raw = ""+r;		
		if(raw.length() < 5)
		{
			return r;	
		}
		else
		{
			float rawSmaller = Float.parseFloat( new String((r + "").replace("Infinity","0")));
			
			if(("" + rawSmaller).length() < 5)
			{
				if(new String(r + "").contains("Infinity"))
				{
					System.out.println("Infinity Number Detected: set to 0!");
				}
				return rawSmaller;	
			}
			else return Float.parseFloat((""+rawSmaller).substring(0,5));
		}
	}
	
		 
	public static void downloadTexture(String input , String output)
	{
		try 
		{
			downloadImage(input, output);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	 /**
     * Checks for an OpenGL error. If there is one, prints the error ID and error string.
     */
	public static void checkGLError(String s)
    {
        int var2 = GL11.glGetError();
        MiystEngine miystengine = MiystEngine.miystengine;
        if (var2 != 0)
        {
        	if(var2 != 1282 && var2 != 1281)
        	{
        		String var3 = GLU.gluErrorString(var2);
        		miystengine.getLogger().error("!#########{ GL ERROR }#########!");
        		miystengine.getLogger().error("@ " + s);
        		miystengine.getLogger().error(var2 + ": " + var3);
        		if(var3.contains("Stack underflow"))
        		{
        			miystengine.getLogger().error("You probably forgot Push or Pop!");
        		}
        	}
        }
    }
	
	public static void downloadImage(String input , String output) throws IOException
	{
		 String url2 = "http://www.mijnalbum.nl/"+input+".jpg";
		 URL url = new URL(url2);
	     InputStream in = new BufferedInputStream(url.openStream()); 
	     OutputStream out = new BufferedOutputStream(new FileOutputStream(output));
	     for ( int i; (i = in.read()) != -1; ) 
	     {
	         out.write(i);
	     }
	     in.close();
	     out.close();
	}
	
	
	/**
	 * Search for the webPage by URLName
	 * @param URLName
	 * @return
	 */
	public static boolean doesURLExists(String URLName)
	{
	    try 
	    {
	      HttpURLConnection.setFollowRedirects(false);
	      HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
	      con.setRequestMethod("HEAD");
	      return (con.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND);
	    }
	    catch (Exception e) 
	    {
	       e.printStackTrace();
	       return false;
	    }
	  } 
	
	@SuppressWarnings("resource")
	public static String readFromBasicFile(String file,String folder) throws Exception 
	{
		int character;
		StringBuffer buffer = new StringBuffer("");
		String readFile = folder+file+".txt";
		FileInputStream inputStream = new FileInputStream(new File(readFile));
		if(readFile.getBytes() != null)
		{
			while((character = inputStream.read()) != -1)
			buffer.append((char) character);
			inputStream.close();
			return buffer.toString();
		}
		else
		{
			return null;
		}
	}
	
	
	
	public static String readFromFile(File file)
	{
		try 
		{
		FileInputStream inputStream;
		try 
		{
			inputStream = new FileInputStream(file);
			int character;
			StringBuffer buffer = new StringBuffer("");
			while((character = inputStream.read()) != -1)
			buffer.append((char) character);
			inputStream.close();
			return buffer.toString();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	} 
	catch (Exception e) 
	{
		e.printStackTrace();
	}
		return "0";
	}
	
	 /**
	 * Create an folder and subfolder to make data more obvious.
	 * Write In: saves/LegendsofAgartha Data
	 * +
	 * subFolder(/subFolderName)
	 */
	public static void createFolder(String subFolder,boolean sendError)
	{	
			BufferedWriter writer;
	        String timeLog = subFolder;
	        File logFile = new File(timeLog);
	        logFile.mkdirs();
	        try 
	        {
				writer = new BufferedWriter(new FileWriter(logFile));
			} 
	        catch (IOException e) 
			{	
	        	if(!sendError)
	        	{
	        		e.printStackTrace();
	        	}
			}
	 }
	
	 /**
	 * Create an folder and subfolder to make data more obvious.
	 * subFolder(/subFolderName)
	 */
	public static void createBasicFolder(String subFolder)
	{	
		BufferedWriter writer;
	    File logFile = new File(MiystEngine.miystengine.getPath().sources + subFolder);
	    logFile.mkdirs();
	    try
	    {
			writer = new BufferedWriter(new FileWriter(logFile));
		} 
	    catch (IOException e) 
		{
	       String output = e.toString();
	       if(!logFile.exists()) 
	       {
	    	   System.out.println(output);
	       }
		}
	  }
	 
	 public static void removeFile(String Directory,String deleteFile) 
	 {
		 File fileFromString = new File(Directory);
		 for (File file: fileFromString.listFiles()) 
			
		 if (!file.isDirectory() && file.getName().contains(deleteFile)) 
		 {
			 file.delete();
		 }
	 }
	 
		 
		 public static void writeBasic(String[] writStringInFile,String writeNameFile)
		 {
		     BufferedWriter writer = null;
		     String timeLog =  writeNameFile;
		     File logFile = new File(timeLog);
		     try 
		     {
		    	 writer = new BufferedWriter(new FileWriter(logFile));
		    	 for(int i=0;i<writStringInFile.length;++i)
		    	 {
		    		 writer.write(writStringInFile[i]);
		    		 writer.newLine(); 
		    	 }
		    	 writer.close();
		     } 
		     catch (IOException e) 
		     {
				e.printStackTrace();
			 }
		 }
		 
		 public static boolean containsNumber(String text)
		 {
			    if(text.contains("1"))
				{
					return true;
				}
			    else if(text.contains("2"))
				{
					return true;
				}
			    else if(text.contains("3"))
				{
					return true;
				}
			    else if(text.contains("4"))
				{
					return true;
				}
			    else if(text.contains("5"))
				{
					return true;
				}
			    else if(text.contains("6"))
				{
					return true;
				}
			    else if(text.contains("7"))
				{
					return true;
				}
			    else if(text.contains("8"))
				{
					return true;
				}
			    else if(text.contains("9"))
				{
					return true;
				}
			    else if(text.contains("0"))
				{
					return true;
				}
			    else
				{
					return false;
				}
		 }
		 
		 public static boolean containsSymbol(String text)
		 {
			    if(text.contains("~"))
				{
					return true;
				}
			    else if(text.contains("`"))
				{
					return true;
				}
			    else if(text.contains("!"))
				{
					return true;
				}
			    else if(text.contains("@"))
				{
					return true;
				}
			    else if(text.contains("#"))
				{
					return true;
				}
			    else if(text.contains("$"))
				{
					return true;
				}
			    else if(text.contains("%"))
				{
					return true;
				}
			    else if(text.contains("^"))
				{
					return true;
				}
			    else if(text.contains("&"))
				{
					return true;
				}
			    else if(text.contains("*"))
				{
					return true;
				}
			    else if(text.contains("("))
				{
					return true;
				}
			    else if(text.contains(")"))
				{
					return true;
				}
			    else if(text.contains("-"))
				{
					return true;
				}
			    else if(text.contains("_"))
				{
					return true;
				}
			    else if(text.contains("+"))
				{
					return true;
				}
			    else if(text.contains("="))
				{
					return true;
				}
			    else if(text.contains("\'"))
				{
					return true;
				}
			    else if(text.contains("|"))
				{
					return true;
				}
			    else if(text.contains(","))
				{
					return true;
				}
			    else if(text.contains("<"))
				{
					return true;
				}
			    else if(text.contains(">"))
				{
					return true;
				}
			    else if(text.contains("/"))
				{
					return true;
				}
			    else if(text.contains("?"))
				{
					return true;
				}
			    else if(text.contains(";"))
				{
					return true;
				}
			    else if(text.contains(":"))
				{
					return true;
				}
			    else if(text.contains("'"))
				{
					return true;
				}
				else
				{
					return false;
				}
		 }
		 
		 
		    public static boolean containsLetters(String text)
			{
				if(text.contains("a"))
				{
					return true;
				}
				else if(text.contains("b"))
				{
					return true;
				}
				else if(text.contains("c"))
				{
					return true;
				}
				else if(text.contains("d"))
				{
					return true;
				}
				else if(text.contains("e"))
				{
					return true;
				}
				else if(text.contains("f"))
				{
					return true;
				}
				else if(text.contains("g"))
				{
					return true;
				}
				else if(text.contains("h"))
				{
					return true;
				}
				else if(text.contains("i"))
				{
					return true;
				}
				else if(text.contains("j"))
				{
					return true;
				}
				else if(text.contains("k"))
				{
					return true;
				}
				else if(text.contains("l"))
				{
					return true;
				}
				else if(text.contains("m"))
				{
					return true;
				}
				else if(text.contains("n"))
				{
					return true;
				}
				else if(text.contains("o"))
				{
					return true;
				}
				else if(text.contains("p"))
				{
					return true;
				}
				else if(text.contains("q"))
				{
					return true;
				}
				else if(text.contains("r"))
				{
					return true;
				}
				else if(text.contains("s"))
				{
					return true;
				}
				else if(text.contains("t"))
				{
					return true;
				}
				else if(text.contains("u"))
				{
					return true;
				}
				else if(text.contains("v"))
				{
					return true;
				}
				else if(text.contains("w"))
				{
					return true;
				}
				else if(text.contains("x"))
				{
					return true;
				}
				else if(text.contains("y"))
				{
					return true;
				}
				else if(text.contains("z"))
				{
					return true;
				}
				if(text.contains("A"))
				{
					return true;
				}
				else if(text.contains("B"))
				{
					return true;
				}
				else if(text.contains("C"))
				{
					return true;
				}
				else if(text.contains("D"))
				{
					return true;
				}
				else if(text.contains("E"))
				{
					return true;
				}
				else if(text.contains("F"))
				{
					return true;
				}
				else if(text.contains("G"))
				{
					return true;
				}
				else if(text.contains("H"))
				{
					return true;
				}
				else if(text.contains("I"))
				{
					return true;
				}
				else if(text.contains("J"))
				{
					return true;
				}
				else if(text.contains("K"))
				{
					return true;
				}
				else if(text.contains("L"))
				{
					return true;
				}
				else if(text.contains("M"))
				{
					return true;
				}
				else if(text.contains("N"))
				{
					return true;
				}
				else if(text.contains("O"))
				{
					return true;
				}
				else if(text.contains("P"))
				{
					return true;
				}
				else if(text.contains("Q"))
				{
					return true;
				}
				else if(text.contains("R"))
				{
					return true;
				}
				else if(text.contains("S"))
				{
					return true;
				}
				else if(text.contains("T"))
				{
					return true;
				}
				else if(text.contains("U"))
				{
					return true;
				}
				else if(text.contains("V"))
				{
					return true;
				}
				else if(text.contains("W"))
				{
					return true;
				}
				else if(text.contains("X"))
				{
					return true;
				}
				else if(text.contains("Y"))
				{
					return true;
				}
				else if(text.contains("Z"))
				{
					return true;
				}	
				else return false;
			}
		 
}