package nl.miystengine.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.NonOptionArgumentSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import com.google.common.collect.HashMultimap;
import com.google.gson.Gson;

public class Main
{
    public static boolean backup = true;

    public static void main(String[] option)
    {
        OptionParser op = new OptionParser();
        op.allowsUnrecognizedOptions();
        op.accepts("fullscreen");
        NonOptionArgumentSpec noa = op.nonOptions();
        OptionSet Option = op.parse(option);
        List l = Option.valuesOf(noa);
        MiystEngine tsw = new MiystEngine(Option.has("fullscreen"));
        if (!l.isEmpty())
        {
            System.out.println("Completely ignored arguments: " + l);
        }
        Thread.currentThread().setName("Client thread");
        tsw.run();
    }
    
     
}
