package awesome.core;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

//import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public class PandaAudio {

	private static long device;
	private static long context;
	
	private static boolean isCreated =false;
	
	public static void create(){
		device =alcOpenDevice((ByteBuffer)null);
		if(device ==0)
			throw new IllegalStateException("Failed to open the default device.");

		ALCCapabilities deviceCaps =ALC.createCapabilities(device);

		context =alcCreateContext(device, (IntBuffer)null);
		if(context ==0)
			throw new IllegalStateException("Failed to create an OpenAL context.");

		alcMakeContextCurrent(context);
		AL.createCapabilities(deviceCaps);
		
		isCreated =true;
	}
	
	public static boolean isCreated(){
		return isCreated;
	}
	
	public static void destroy(){
//		if (decoder !=null)
//			stb_vorbis_close(decoder.handle);
//
//		alDeleteBuffers(buffers);
//		alDeleteSources(source);

		alcDestroyContext(context);
		alcCloseDevice(device);
	}
}
