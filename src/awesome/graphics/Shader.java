package awesome.graphics;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL20.*;

public class Shader
{	
	
	private int program, vertShader, fragShader;
	private static String header;

	public Shader(InputStream vert, InputStream frag) throws Exception
	{
		vertShader = loadShader(vert, GL_VERTEX_SHADER);
		fragShader = loadShader(frag, GL_FRAGMENT_SHADER);
		program = glCreateProgram();
		glAttachShader(program, vertShader);
		glAttachShader(program, fragShader);
		glLinkProgram(program);
		glValidateProgram(program);

		String err = glGetProgramInfoLog(program, 500);
		if(err.length()>0) throw new Exception("Shader Error: "+err);
	}

	public static void setHeader(String h)
	{
		header=h;
	}
	
	
	private static int loadShader(InputStream is, int type) throws Exception
	{
		// Streams
//		InputStream is = Shader.class.getResourceAsStream("/panda/shaders/"+file);//new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isr);
		
		// Storage
		String src = "";
		String line;
		
		// Reading
		if(header!=null) src = header;
		while( (line=reader.readLine())!=null) src+=line+"\n";
		reader.close();
		
		// Compiling
		int shader = glCreateShader(type);
		glShaderSource(shader, src);
		glCompileShader(shader);
		String err = glGetShaderInfoLog(shader, 500);
		if(err.length()>0) throw new Exception("Shader Error: "+err);
		return shader;
	}
	
	
	public void bind()
	{
		glUseProgram(program);
	}
	
	
	public void destroy()
	{
		glDeleteProgram(program);
		glDeleteShader(vertShader);
		glDeleteShader(fragShader);
	}

	
	public int getProgram()
	{
		return program;
	}
	

	public void unbind()
	{
		glUseProgram(0);
	}
	
	
}
