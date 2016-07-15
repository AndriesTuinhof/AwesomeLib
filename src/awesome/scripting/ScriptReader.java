package awesome.scripting;

import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ScriptReader
{
	public static void main(String[] args) throws Exception
	{
		try
		{
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
			engine.eval("var Pollux = new ( Java.type('awesome.scripting.ScriptRoot') )();");
			engine.eval(new FileReader("Pollux.foo();"));
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}
