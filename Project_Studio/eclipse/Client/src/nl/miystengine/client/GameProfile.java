package nl.miystengine.client;

import java.util.Random;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.lwjgl.input.Keyboard;

/**
 * This file is just a test for the local host multiplayer.
 * 18-2-2019 It's broken now...(Multiplayer is still full with bugs and can't be used right now!)
 * 13-7-2019 On my way to delete the Multiplayer part
 * Remove many "server" files
 * @author Cefas.
 */
public class GameProfile
{
	public final UUID id;
    private final String name;
    private boolean legacy;
    public static boolean consoleOpened = false;
    public static int consoleTimer = 0;
    public static double ShowHelp = 0;
  
    public GameProfile(UUID id, String name)
    {
        if (id == null && StringUtils.isBlank(name))
        {
            throw new IllegalArgumentException("Name and ID cannot both be blank");
        }
        else
        {
            this.id = id;
            this.name = name;
        } 
        text = "Use Tab to open Console....";
        consoleOpened = true;
        canSendAgain=100;
    }
    
	
  
    public static void AdminCommands()
    {
    	openorclosechat();
    	if(resetTextTimer>0)
   	 	{
   		 --resetTextTimer;
   	 	}
        if(consoleTimer > 0)
        {
        	--consoleTimer;
        }
        if(canSendAgain > 0)
        {
       	 --canSendAgain;
        }
        if(canSendAgain  > 0 && canSendAgain  < 4)
        {
       	 text = "";
        }
    }
    
    public static String text = "";
    public static int resetTextTimer = 0;
    public static int canSendAgain = 0;
    public static int texTimerFPSFix = 20;

    
    public static void openorclosechat()
    {
    	 if(consoleOpened&&resetTextTimer==0){
       	 if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)&&Keyboard.isKeyDown(Keyboard.KEY_MINUS)){text +="_";resetTextTimer=texTimerFPSFix;}
       	 else if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){text +=" ";resetTextTimer=texTimerFPSFix;}
       	 else if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)&&Keyboard.isKeyDown(Keyboard.KEY_MINUS)){text +="_";resetTextTimer=texTimerFPSFix;}
       	 else if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)&&Keyboard.isKeyDown(Keyboard.KEY_1)){text +="!";resetTextTimer=texTimerFPSFix;}
       	 else if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)&&Keyboard.isKeyDown(Keyboard.KEY_1)){text +="!";resetTextTimer=texTimerFPSFix;}
       	 else if(Keyboard.isKeyDown(Keyboard.KEY_0)){text +="0";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_1)){text +="1";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_2)){text +="2";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_3)){text +="3";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_4)){text +="4";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_5)){text +="5";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_6)){text +="6";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_7)){text +="7";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_8)){text +="8";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_9)){text +="9";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_MINUS)){text +="-";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_A)){text +="A";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_B)){text +="B";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_C)){text +="C";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_D)){text +="D";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_E)){text +="E";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_F)){text +="F";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_G)){text +="G";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_H)){text +="H";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_I)){text +="I";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_J)){text +="J";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_K)){text +="K";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_L)){text +="L";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_M)){text +="M";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_N)){text +="N";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_O)){text +="O";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_P)){text +="P";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_Q)){text +="Q";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_R)){text +="R";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_S)){text +="S";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_T)){text +="T";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_U)){text +="U";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_V)){text +="V";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_W)){text +="W";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_X)){text +="X";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_Y)){text +="Y";resetTextTimer=texTimerFPSFix;}
    	 else if((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))&&Keyboard.isKeyDown(Keyboard.KEY_Z)){text +="Z";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_A)){text +="a";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_B)){text +="b";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_C)){text +="c";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_D)){text +="d";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_E)){text +="e";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_F)){text +="f";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_G)){text +="g";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_H)){text +="h";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_I)){text +="i";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_J)){text +="j";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_K)){text +="k";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_L)){text +="l";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_M)){text +="m";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_N)){text +="n";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_O)){text +="o";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_P)){text +="p";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_Q)){text +="q";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_R)){text +="r";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_S)){text +="s";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_T)){text +="t";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_U)){text +="u";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_V)){text +="v";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_W)){text +="w";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_X)){text +="x";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_Y)){text +="y";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_Z)){text +="z";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_COMMA+2)){text +="/";resetTextTimer=texTimerFPSFix;}
    	 else if(Keyboard.isKeyDown(Keyboard.KEY_COMMA+1)){text +=".";resetTextTimer=texTimerFPSFix;}
    	 else if(text.length()>0&&Keyboard.isKeyDown(Keyboard.KEY_BACK)){text =text.substring(0, text.length()-1);resetTextTimer=texTimerFPSFix;}
    	 if(canSendAgain==0&&Keyboard.isKeyDown(Keyboard.KEY_RETURN)&&!text.contains("Invalid Command")){
    	 }}

    	 
    	 if(canSendAgain==1){consoleOpened = false;}
    	 
    }
    
    

    public UUID getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public boolean isComplete()
    {
        return id != null && StringUtils.isNotBlank(getName());
    }

    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        else if (o != null && getClass() == o.getClass())
        {
            GameProfile that = (GameProfile)o;

            if (id != null)
            {
                if (!id.equals(that.id))
                {
                    return false;
                }
            }
            else if (that.id != null)
            {
                return false;
            }

            if (name != null)
            {
                if (name.equals(that.name))
                {
                    return true;
                }
            }
            else if (that.name == null)
            {
                return true;
            }

            return false;
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

}
