package nl.miystengine.client.shader;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;
import nl.miystengine.client.FileBasicJava;
import nl.miystengine.client.MiystEngine;

public class StaticShader extends ShaderProgram
{
	public int location_firstpersontexture; 
	public int location_screenTexture; 
	public int location_depthTexture; 
	public int location_refractionTexture; 
	public int location_AddLight; 
	protected int location_NearFar;
	private int location_hasFullBlurr;
	private int brightness;
	private int brightnessEffect;
	private int saturation;
	private int contrast;
	
	public StaticShader(String VERTEX_FILE,String FRAGMENT_FILE) 
	{
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() 
	{
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
	}
	
	
	@Override
	protected void getAllUniformLocations() 
	{
		this.location_refractionTexture = super.getUniformLocation("refraction");
		this.location_depthTexture = super.getUniformLocation("depth");
		this.location_screenTexture = super.getUniformLocation("screen");
		this.location_firstpersontexture = super.getUniformLocation("firstperson");
		this.location_AddLight = super.getUniformLocation("addScreenEffects");
		this.location_NearFar = super.getUniformLocation("NearFar");
		this.location_hasFullBlurr = super.getUniformLocation("hasBlurr");
		this.brightness = super.getUniformLocation("brightness");
		this.brightnessEffect = super.getUniformLocation("brightnessEffect");
		this.saturation = super.getUniformLocation("saturation");
		this.contrast = super.getUniformLocation("contrast");
	}
	
	public void brightness(float i)
	{
		super.loadFloat(this.brightness, i);
	}
	
	public void brightnessEffect(float i)
	{
		super.loadFloat(this.brightnessEffect, i);
	}
	
	public void saturation(float i)
	{
		super.loadFloat(this.saturation, i);
	}
	
	public void contrast(float i)
	{
		super.loadFloat(this.contrast, i);
	}
	
	public void hasFullBlurr(int i)
	{
		super.loadInt(this.location_hasFullBlurr, i);
	}

	public void useLightPPE(int a)
	{
		super.loadInt(location_AddLight, a);
	}
	
	public void connectTextureUnits()
	{
		getAllUniformLocations();
		super.loadInt(this.location_firstpersontexture, 3);
		super.loadInt(this.location_refractionTexture, 2);
		super.loadInt(this.location_depthTexture, 1);
		super.loadInt(this.location_screenTexture, 0);
	}

	public void loadNearFar(Vector2f s)
	{
		super.load2fVector(location_NearFar, s);
	}
	
}
