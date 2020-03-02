package nl.miystengine.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ITextureObject
{
	int getGlTextureId();

	boolean loadTexture(IResourceManager theResourceManager);
	
	boolean loadTextureWImageBuffer(IResourceManager theResourceManager,BufferedImage image);
}
