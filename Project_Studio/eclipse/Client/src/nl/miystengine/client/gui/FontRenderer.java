package nl.miystengine.client.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import nl.miystengine.client.FileBasicJava;
import nl.miystengine.client.MiystEngine;

import org.lwjgl.opengl.GL11;

public class FontRenderer
{
	public double[] charWidth = new double[256];
    public int[] colorCode = new int[32];
	public final String symbols;
    public int textColor;
    public float posX;
    public float posY;
    public float R;
    public float B;
    public float G;
    public float ALPHA;

    public FontRenderer(String as)
    {
        this.symbols = as;
        for (int t = 0; t < 32; ++t)
        {
            int v1 = (t >> 3 & 1) * 85;
            int v2 = (t >> 2 & 1) * 170 + v1;
            int v3 = (t >> 1 & 1) * 170 + v1;
            int v4 = (t >> 0 & 1) * 170 + v1;
            if (t == 6)
            {
            	v2 += 85;
            }
            if (t >= 16)
            {
            	v2 /= 4;
                v3 /= 4;
                v4 /= 4;
            }
            this.colorCode[t] = (v2 & 255) << 16 | (v3 & 255) << 8 | v4 & 255;
        }
        if(readFontTexture())
        {
        	this.readFontTexture();
        }
        else
        {
        	 System.out.print("Warning: Font not loaded!");
             System.out.print("Search location: " + MiystEngine.miystengine.getPath().source+"las.png");
        }
    }

    public boolean readFontTexture()
    {
        BufferedImage im;
        try
        {
        im = ImageIO.read(new File(MiystEngine.miystengine.getPath().source+"las.png"));
        }
        catch (IOException ex)
        {
        return false;
        }
        
        int w = im.getWidth();
        int h = im.getHeight();
        int[] wxh = new int[w * h];
        im.getRGB(0, 0, w, h, wxh, 0, w);
        int cM = 0;
        while (cM < 256)
        {
            if (cM == 32)
            {
                this.charWidth[cM] = 3 + 1;
            }
            int wi = ((w/16)-1);
            while (true)
            {
                if (wi >= 0)
                {
                boolean o = true;
                for (int c = 0; c < (h / 16) && o; ++c)
                {
                if ((wxh[((cM%16)*(w/16)+wi)+(((cM/16)*(w/16)+c)*w)] >> 24 & 255) != 0)
                {
                o = false;
                }}
                if(o)
                {
                --wi;
                continue;
                }}
                ++wi;this.charWidth[cM] = (0.5D+(wi*(8.0F/(w/16))))+1.0D;++cM;
                break;
            }
        }
		return true;
    }
 
    /**
    * Returns the width of the string.
    */
    public int getStringWidth(String s)
    {
        if (s == null)
        {
            return 0;
        }
        else
        {
            int gw = 0;
            boolean stop = false;
            for (int ls = 0; ls < s.length(); ++ls)
            {
                char c = s.charAt(ls);
                int i = this.getCharWidth(c);
                if (i < 0 && ls < s.length() - 1)
                {
                    ++ls;
                    c = s.charAt(ls);
                    if (c != 108 && c != 76)
                    {
                        if (c == 114 || c == 82)
                        {
                        	stop = false;
                        }
                    }
                    else
                    {
                    	stop = true;
                    }
                    i = 0;
                }
                gw += i;
                if (stop && i > 0)
                {
                    ++gw;
                }
            }
            return gw;
        }
    }

    /**
     * Pick how to render a single character and return the width.(xPos)
     */
    public float renderCharAtPos(int at, char letterorsymbol, String chars,int charat)
    {
    	float symbolindex = returnsSymbolThatMakePicture(letterorsymbol,chars,at,charat);
    	if((""+letterorsymbol).contains(" "))
    	{
        return 5.0F;	
    	}
    	else if(returnsSymbolThatMakePicture(letterorsymbol,chars,at,charat) != -1)
    	{
    	return this.renderDefaultChar(symbolindex == 110 ? 6 :symbolindex == 107 ? 3 :symbolindex == 104 ? 3 : symbolindex == 101 ? 2 : symbolindex == 99 ? 2 : symbolindex == 98 ? 1 : symbolindex == 96 ? 2 : 1 ,symbolindex == -666 ? at : returnsSymbolThatMakePicture(letterorsymbol,chars, at, charat), (int)rss(letterorsymbol+""));
    	}
    	else return 1.0F;
    }
    
    public int stopRendering = -1;
    
    public int returnsSymbolThatMakePicture(char letterorsymbol,String chars,int at,int charat)
    {
    	if(chars.contains("-[--[<|") && ((chars.indexOf("-[--[<|",charat) == charat)))
    	{
    		this.stopRendering = 11;
    		return 96;
    	}
    	else if(chars.contains("-]>") && ((chars.indexOf("-]>",charat) == charat)))
    	{
    		this.stopRendering = 4;
    		return 98;
    	}
    	else if(chars.contains("---[<>|") && ((chars.indexOf("---[<>|",charat) == charat)))
    	{
    		this.stopRendering = 11;
    		return 99;
    	}
    	else if(chars.contains("<>-[---") && ((chars.indexOf("<>-[---",charat) == charat)))
    	{
    		this.stopRendering = 12;
    		return 101;
    	}
    	else if(chars.contains("---[-[--") && ((chars.indexOf("---[-[--",charat) == charat)))
    	{
    		this.stopRendering = 14;
    		return 104;
    	}
    	else if(chars.contains("---[<>") && ((chars.indexOf("---[<>",charat) == charat)))
    	{
    		this.stopRendering = 9;
    		return 107;
    	}
    	else if(chars.contains("-[-<>|") && ((chars.indexOf("-[-<>|",charat) == charat)))
    	{
    		this.stopRendering = 10;
    		return 110;
    	}
    	else if(chars.contains("]()") && ((chars.indexOf("]()",charat) == charat)))
    	{
    		this.stopRendering = 3;
    		return 112;
    	}
    	else if(chars.contains("(|)") && ((chars.indexOf("(|)",charat) == charat)))
    	{
    		this.stopRendering = 3;
    		return 113;
    	}
    	else if(chars.contains("|[]") && ((chars.indexOf("|[]",charat) == charat)))
    	{
    		this.stopRendering = 3;
    		return 114;
    	}
    	else if(chars.contains("->") && ((chars.indexOf("->",charat) == charat)))
    	{
    		this.stopRendering = 1;
    		return 116;
    	}
    	else if(chars.contains("<-") && ((chars.indexOf("<-",charat) == charat)))
    	{
    		this.stopRendering = 1;
    		return 115;
    	}
    	else if(chars.contains("(R)") && ((chars.indexOf("(R)",charat) == charat)))
    	{
    		this.stopRendering = 3;
    		return 117;
    	}
    	else if(chars.contains("(B)") && ((chars.indexOf("(B)",charat) == charat)))
    	{
    		this.stopRendering = 3;
    		return 118;
    	}
    	else if(chars.contains("0F") && ((chars.indexOf("0F",charat) == charat)))
    	{
    		this.stopRendering = 2;
    		return 119;
    	}
    	else if(chars.contains("0C") && ((chars.indexOf("0C",charat) == charat)))
    	{
    		this.stopRendering = 3;
    		return 64;
    	}
    	else if(chars.contains("][") && ((chars.indexOf("][",charat) == charat)))
    	{
    		this.stopRendering = 1;
    		return 95;
    	}
    	else if(this.stopRendering > 0)
    	{ 
    	 --this.stopRendering;
    	 return -1;
    	}
    	else return -666;
    }
     
    /**
     * Renders the space the char/symbol needs to make the text smooth/readable
     */
    public float rss(String letterorsymbol)
    {
    	return (letterorsymbol+"").contains(" ") ? 1 :(letterorsymbol+"").contains("h") ?  10:(letterorsymbol+"").contains("i") ?  11:	(letterorsymbol+"").contains("j") ?  -1:(letterorsymbol+"").contains("k") ?  10:	(letterorsymbol+"").contains("p") ? -10:	(letterorsymbol+"").contains("l") ? -15:(letterorsymbol+"").contains("o") ? -10:	(letterorsymbol+"").contains("K") ? -10:	(letterorsymbol+"").contains("J") ? 10:	(letterorsymbol+"").contains("R") ? -15:	0;
    }
    
    public void drawSplitString(String s, int x, int y, int c, int tc)
    {
        this.textColor = tc;
        while (s != null &&s.endsWith("\n"))
        {
            s = s.substring(0,s.length()-1);
        }
    }

    /**
     *  Draws the string with a shadow (Not added).
     */
    public int drawStringWithShadow(String s, int x, int y, int color)
    {
        return this.drawString(s, x, y, color);
    }

    /**
     * Draws the string. Arguments: string(text) , x, y, color
     */
    public int drawString(String s, float x, float y, int color)
    {
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        return this.renderString(s, x, y, color);
    }
    
   
    public int drawStringWithColor(String s, float x, float y, float R, float G, float B, float ALPHA)
    {
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        return this.renderString(s, x, y, R,G,B,ALPHA);
    }


    public int renderString(String s, float x, float y, float R, float G, float B, float ALPHA)
    {
        if (s == null)
        {
            return 0;
        }
        else
        {
        	GL11.glColor4f(R,G,B,ALPHA);
            this.posX = (float)x;
            this.posY = (float)y;
            for (int length = 0; length < s.length(); ++length)
            {
                	char cal = s.charAt(length);
                	int ca = this.symbols.indexOf(cal);
                    if ((cal == 0 || ca == -1 ))
                    {
                        this.posX -= 1;
                        this.posY -= 1;
                    }
                    if ((cal == 0 || ca == -1 ))
                    {
                        this.posX += 1;
                        this.posY += 1;
                    }
                    this.posX += this.renderCharAtPos(ca,cal,s,length);
            }    
            return (int)this.posX;
        }
    }

    /**
     * Render single line string by setting GL color, current (posX,posY), and calling renderStringAtPos()
     */
    public int renderString(String s, float x, float y, int c)
    {
        if (s == null)
        {
            return 0;
        }
        else
        {
            if ((c & -67108864) == 0)
            {
                c |= -16777216;
            }
            this.R = (c >> 16 & 255) / 255.0F;
            this.B = (c >> 8 & 255) / 255.0F;
            this.G = (c & 255) / 255.0F;
            this.ALPHA = (float)(c >> 24 & 255) / 255.0F;
            GL11.glColor4f(this.R, this.B, this.G, this.ALPHA);
            this.posX = (float)x;
            this.posY = (float)y;;
            for (int length = 0; length < s.length(); ++length)
            {
                	char cal = s.charAt(length);
                	int ca = this.symbols.indexOf(cal);
                    if ((cal == 0 || ca == -1 ))
                    {
                        this.posX -= 1;
                        this.posY -= 1;
                    }
                    if ((cal == 0 || ca == -1 ))
                    {
                        this.posX += 1;
                        this.posY += 1;
                    }
                    this.posX += this.renderCharAtPos(ca,cal,s,length);
            }    
            return (int)this.posX;
        }
    }

    /**
     *  Returns the width of this character.
     */
    public int getCharWidth(char c)
    {
            int i = this.symbols.indexOf(c);
            if (c > 0 && i != -1)
            {
                return (int) this.charWidth[i];
            }
            else
            {
                return 0;
            }
    }

    public String trimStringToWidth(String s, int q)
    {
        StringBuilder sb = new StringBuilder();
        boolean u = false,t = false;
        int p = 0;
        for (int t2 = 0; t2 >= 0 && t2 < s.length() && p < q; t2 += 1)
        {
            char ca = s.charAt(t2);
            int w = this.getCharWidth(ca);
            if (u)
            {
                u = false;
                if (ca != 108 && ca != 76)
                {
                    if (ca == 114 || ca == 82)
                    {
                        t = false;
                    }
                }
                else
                {
                    t = true;
                }
            }
            else if (w < 0)
            {
                u = true;
            }
            else
            {
                p += w;
                if (t)
                {
                    ++p;
                }
            }
            if (p > q)
            {
                break;
            }
            sb.append(ca);
        }
        return sb.toString();
    }
    
    /**
     *  Render a character with the las.png font
     */
    public int renderDefaultChar(int length, int at,int space)
    {
        float x = (at % 16 * 8);
        float y = (at / 16 * 8);
        MiystEngine t =  MiystEngine.miystengine;
	    
        t.getTextureManager().bindTexture("/las.png",1,true);
        double cw;
        if(at > 0)
        {
        	cw = (this.charWidth[at] - 0.01F)*length;
        }
        else
        {
        	cw = 0;
        	at = 0;
        }
        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glTexCoord2f(x / 128.0F, y / 128.0F);
        GL11.glVertex3f(this.posX , this.posY, 0.0F);
        GL11.glTexCoord2f(x / 128.0F, (y + 7.99F) / 128.0F);
        GL11.glVertex3f(this.posX , this.posY + 7.99F, 0.0F);
        GL11.glTexCoord2d((x + cw - 1.0F) / 128.0F, y / 128.0F);
        GL11.glVertex3d(this.posX + cw - 1.0F , this.posY, 0.0F);
        GL11.glTexCoord2d((x + cw - 1.0F) / 128.0F, (y + 7.99F) / 128.0F);
        GL11.glVertex3d(this.posX + cw - 1.0F, this.posY + 7.99F, 0.0F);
        GL11.glEnd();
        return (int)this.charWidth[(at+space)];
    }
}
