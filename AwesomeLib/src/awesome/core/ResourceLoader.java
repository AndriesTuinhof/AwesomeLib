package awesome.core;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Files;

import static  org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_info_from_memory;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.stb.STBVorbis.*;

import awesome.graphics.Model;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageResize;

import awesome.graphics.Collada;
import awesome.graphics.Shader;
import awesome.graphics.Wavefront;
import awesome.util.ThreadedFileLoader;

public class ResourceLoader{
	
	private static String resourcePath;
	
	protected static void create(String _resourcePath){
		resourcePath =_resourcePath;		
	}
	
	public static Shader getShader(String shader){	//TODO: FIX THIS
		try {
			InputStream a = ResourceLoader.class.getResourceAsStream("/panda/shaders/"+shader+".vert");
			InputStream b = ResourceLoader.class.getResourceAsStream("/panda/shaders/"+shader+".frag");
			return new Shader(a,b);
		}
		catch(Exception e) {
//			System.out.println("BLUEPRINT: Failed to load shader '"+shader+"'");	wtf jon this is not blueprint
			e.printStackTrace();
			return null;
		}
	}

	public static int getTexture(File file, boolean flip){
		try{
			return getTexture(Files.readAllBytes(file.toPath()), flip);
		}
		catch(Exception e){
			System.out.println(file);
			e.printStackTrace();
			return -1;
		}		
	}
	
	public static int getTexture(String file, boolean flip){
		try{
			return getTexture(Files.readAllBytes(new File(resourcePath+file).toPath()), flip);
		}
		catch(Exception e){
			System.out.println(file);
			e.printStackTrace();
			return -1;
		}		
	}
	
	public static int getTexture(byte[] rawBytes, boolean flip){	//TODO: use the flip boolean 
		try{
			ByteBuffer buffer =BufferUtils.createByteBuffer(rawBytes.length);
			buffer.put(rawBytes);
			buffer.flip();
			
			IntBuffer w =BufferUtils.createIntBuffer(1);
			IntBuffer h =BufferUtils.createIntBuffer(1);
			IntBuffer comp =BufferUtils.createIntBuffer(1);

			// Use info to read image metadata without decoding the entire image.
			if(flip)
				STBImage.stbi_set_flip_vertically_on_load(1);
			if (stbi_info_from_memory(buffer, w, h, comp) ==0)
				throw new RuntimeException("Failed to read image information: " +stbi_failure_reason());
			
			int width =w.get(0);
			int height =h.get(0);
			int comps =comp.get(0);

			ByteBuffer image =stbi_load_from_memory(buffer, w, h, comp, 0);
			if (image ==null)
				throw new RuntimeException("Failed to load image: " +stbi_failure_reason());
			
			return getTexture(image, width, height, comps);
			
		}
		catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
		
	protected static int getTexture(ByteBuffer pixels, int width, int height, int comps)
	{
		try
		{			
			int texID = glGenTextures();

			glBindTexture(GL_TEXTURE_2D, texID);
			boolean hasAlpha = comps==4;
			
			if(hasAlpha)
			{
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			}

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			
			int type = (hasAlpha? GL_RGBA : GL_RGB);
			glTexImage2D(GL_TEXTURE_2D, 0, type, width, height, 0, type, GL_UNSIGNED_BYTE, pixels);
			
			// Generate MIPMAPS
			int www = width/2;
			int level = 1;
			ByteBuffer lastLevel = pixels;
			while(true)
			{
				ByteBuffer subTexture = BufferUtils.createByteBuffer(lastLevel.capacity());
				STBImageResize.stbir_resize_uint8(lastLevel, www*2, www*2, 0, subTexture, www, www, 0, comps);
				glTexImage2D(GL_TEXTURE_2D, level, type, www, www, 0, type, GL_UNSIGNED_BYTE, subTexture);
				lastLevel = subTexture;
				level++;
				if(www<=1) break;
				www/=2;
			}

			
			return texID;
		}
		catch(Exception e)
		{
			System.out.println("ENGINE: Couldn't load texture!");
//			System.out.println(file);
			e.printStackTrace();
			return -1;
		}
	}
		
	public static Wavefront getWavefront(String file)
	{
		try
		{
			return new Wavefront(openFile(file));
		}
		catch(Exception e)
		{
			System.out.println("ENGINE: Couldn't load wavefront file!");
			e.printStackTrace();
		}
		return null;
	}

	public static Model getModel(String file)
	{
		try
		{
			return new Model(openFile(file));
		}
		catch(Exception e)
		{
			System.out.println("ENGINE: Couldn't load model file!");
			e.printStackTrace();
		}
		return null;
	}

	public static int getAudio(String file){
		try{
			IntBuffer channels = BufferUtils.createIntBuffer(1);
			IntBuffer sample_rate = BufferUtils.createIntBuffer(1);
			ShortBuffer data = stb_vorbis_decode_filename(resourcePath+file, channels, sample_rate);
			int buffer = AL10.alGenBuffers();
			
			if(channels.get(0)>2) throw new Exception("Audio file had more than 2 channels, can't load..."); 
			int format = channels.get(0)==2? AL10.AL_FORMAT_STEREO16 : AL10.AL_FORMAT_MONO16;
			AL10.alBufferData(buffer, format, data, sample_rate.get(0));
			return buffer;
		}
		catch(Exception e){
			System.out.println("ENGINE: Couldn't load audio!");
			e.printStackTrace();
		}
		return -1;
	}
	
	public static Collada loadCollada(String file)
	{
		try
		{
			return new Collada(openFile(file));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
		
	public static InputStream openFile(String filename) throws Exception
	{
		return new FileInputStream(resourcePath+filename);
	}
	
	public static InputStream openFileSafe(String filename)
	{
		try {
			return new FileInputStream(resourcePath+filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String readString(String file) {
		try {
			return new String(Files.readAllBytes(new File(resourcePath+file).toPath()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static DataOutputStream saveFile(String filename) throws FileNotFoundException {
		return new DataOutputStream(new FileOutputStream(resourcePath+filename));
	}

	public static boolean fileExists(String filename) {
		return new File(resourcePath+filename).exists();
	}
	
	public static boolean deleteFile(String filename) {
		return new File(resourcePath+filename).delete();
	}
	
	public static ThreadedFileLoader openFileThreaded(String filename)
	{
		return new ThreadedFileLoader(resourcePath+filename);
	}
	
	public static String getPath()
	{
		return resourcePath;
	}


}
