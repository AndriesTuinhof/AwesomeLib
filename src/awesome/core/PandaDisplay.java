package awesome.core;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

public class PandaDisplay{
	
	private static long window;
	private static int width =1280, height =720;
	private static boolean closePressed;
	private static boolean wasResized;
	private static boolean fullscreen;
	private static boolean borderless;
	private static String windowTitle ="";
	private static boolean maximised;
	private static boolean mouseGrabbed;
	private static int swapInterval = 0;
	private static int preferredMonitorIndex = 0;
	
	// Used for accessing screen-space cursor location.
	private static int directCursorX, directCursorY, directMouseDown;
	private static IntBuffer xint =BufferUtils.createIntBuffer(1);
	private static IntBuffer yint =BufferUtils.createIntBuffer(1);
	private static DoubleBuffer xdouble =BufferUtils.createDoubleBuffer(1);
	private static DoubleBuffer ydouble =BufferUtils.createDoubleBuffer(1);
	
	// Keep GLFW callbacks in RAM by storing static.
	private static GLFWKeyCallback keyCallback;
	private static GLFWCursorPosCallback mousePosCallback;
	private static GLFWMouseButtonCallback mouseButtonCallback;
	private static GLFWScrollCallback mouseScrollCallback;
	private static GLFWWindowCloseCallback windowCloseCallback;
	private static GLFWWindowSizeCallback windowSizeCallback;
	private static GLFWWindowIconifyCallback windowIconifyCallback;
	private static GLFWDropCallback dropCallback;
    private static GLFWErrorCallback errorCallback;
		
	private static String version =Version.getVersion();
	private static String os =System.getProperty("os.name");
    
	protected static void create(){		
		//It needs to be initialized before able to do anything with Lwjgl
        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
        
        
		//This declares what opengl is being used.
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		       
       
		glfwSetErrorCallback((errorCallback =GLFWErrorCallback.createPrint(System.err)));
        
		glfwDefaultWindowHints();
		if(borderless) glfwWindowHint(GLFW_DECORATED, GL_FALSE);
		PointerBuffer b =glfwGetMonitors();
		if(preferredMonitorIndex >=b.capacity()) preferredMonitorIndex =0;
		if(fullscreen) window =glfwCreateWindow(width, height, windowTitle, b.get(preferredMonitorIndex), 0L);
		else window =glfwCreateWindow(width, height, windowTitle, 0L, 0L);
		if(borderless) glfwSetWindowPos(window, 100, 100);		
		
		glfwMakeContextCurrent(window);
		glfwSwapInterval(swapInterval);
		
		// The callbacks that need to be initialized before able to use input, etc.		
		glfwSetKeyCallback(window, (keyCallback =new KeyboardKeyCallback()));
		glfwSetMouseButtonCallback(window, (mouseButtonCallback =new MouseButtonCallback()));
		glfwSetCursorPosCallback(window,(mousePosCallback =new MousePosCallback()));
		glfwSetScrollCallback(window, (mouseScrollCallback =new MouseScrollCallback()));
		glfwSetWindowCloseCallback(window, (windowCloseCallback =new WindowCloseCallback()));
		glfwSetWindowSizeCallback(window, (windowSizeCallback =new WindowSizeCallback()));
		glfwSetWindowIconifyCallback(window, (windowIconifyCallback =new WindowIconifyCallback()));
		glfwSetDropCallback(window, (dropCallback =new DropCallbackCallback()));
		// End Callbacks
		
//		GLContext.createFromCurrent();
		glfwShowWindow(window);
		GL.createCapabilities();
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}
	
	public static String getVersion(){
		return version;
	}
	
	public static String getOS(){
		return os;
	}
	
	public static void enableFullscreen(){
		fullscreen = true;
		width = 1920;
		height = 1080;
	}
	
	
	public static void setPreferredScreen(int i){
		preferredMonitorIndex =i;
	}


	public static void setSwapInterval(int i){
		swapInterval = i;
	}
	
	
	public static void moveWindow(int x, int y){
		if(x ==0 && y ==0) return;
		int newX =x +xint.get(0);
		int newY =y +yint.get(0);
		
		if(newY >0 &&maximised){
			toggleWindowMaximised();
		}
		
		if(newY <=0){ // Dragged to top, need to go big
			if(!maximised)
				toggleWindowMaximised();
		}
		else{
			GLFW.glfwSetWindowPos(window, newX, newY);
		}
	}
	
	
	public static void setBorderless(boolean _borderless){
		borderless =_borderless;
	}
	

	public static boolean isBorderless(){
		return borderless;
	}
	
	public static void setSize(int w, int h){
		width =w;
		height =h;
	}
	
	public static String getTitle(){
		return windowTitle;
	}
	
	
	protected static void destroy(){
		destroyGraphics();
	}
	
	private static void destroyGraphics(){
		glfwDestroyWindow(window);
	}
	
	protected static void update(){
		UI.draw3DView();
		UI.draw();
		glfwSwapBuffers(window);
		glfwPollEvents();
		
		// Update real cursor location.
		glfwGetWindowPos(window, xint, yint);
		glfwGetCursorPos(window, xdouble, ydouble);
		directCursorX =(int)(xint.get(0) +xdouble.get(0));
		directCursorY =(int)(yint.get(0) +ydouble.get(0));
		directMouseDown =glfwGetMouseButton(window, 0);

	}
	
	
	public static void toggleWindowMaximised(){
		maximised =!maximised;
		if(maximised){
			glfwSetWindowSize(window, 1920, 1080 -40);	//TODO: what is that 40?
			glfwSetWindowPos(window, 0, 0);
		}
		else{
			glfwSetWindowSize(window, 1280, 720);
		}
	}
	
	
	public static int getDirectCursorX(){
		return directCursorX;
	}
	
	public static int getDirectCursorY(){
		return directCursorY;
	}
	
	public static boolean getDirectMouseDown(){
		return directMouseDown ==GLFW_PRESS;
	}
	
	
	public static boolean wasResized(){
		boolean b =wasResized;
		wasResized =false;
		return b;
	}
	
	
	public static boolean closePressed(){
		boolean b =closePressed;
		closePressed =false;
		return b;
	}

	
	public static void setTitle(String title){
		glfwSetWindowTitle(window, title);
		windowTitle =title;
	}
	
	public static int getWidth(){
		return width;
	}
	
	public static int getHeight(){
		return height;
	}
	
	public static boolean isMouseGrabbed(){
		return mouseGrabbed;
	}

	public static void setMouseGrabbed(boolean grab){
		glfwSetInputMode(window, GLFW.GLFW_CURSOR, grab ?GLFW.GLFW_CURSOR_DISABLED :GLFW.GLFW_CURSOR_NORMAL);
		mouseGrabbed =grab;
	}
	
	
	private static class MouseButtonCallback extends GLFWMouseButtonCallback{
		public void invoke(long window, int button, int action, int mods){
			Input.mouseButton(window, button, action, mods);
		}
	}
	
	private static class MouseScrollCallback extends GLFWScrollCallback{
		public void invoke(long window, double xoffset, double yoffset){
			Input.scroll(window, xoffset, yoffset);
		}
	}
	
	private static class WindowCloseCallback extends GLFWWindowCloseCallback{
		public void invoke(long window){
			closePressed =true;
		}
	}
	
	private static class WindowSizeCallback extends GLFWWindowSizeCallback{
		public void invoke(long window, int _width, int _height){
			width =_width;
			height =_height;
			wasResized =true;
			UI.update();
		}
	}
	
	private static class KeyboardKeyCallback extends GLFWKeyCallback{
		public void invoke(long window, int key, int scancode, int action, int mods){
			Input.setKey(key, action);
		}
	}
	
	private static class MousePosCallback extends GLFWCursorPosCallback{
		public void invoke(long window, double x, double y){
			Input.cursorPos(window, x, y);
		}		
	}

	private static class WindowIconifyCallback extends GLFWWindowIconifyCallback{
		public void invoke(long window, boolean b){
//			if(i==GL_TRUE) glfwIconifyWindow(window);
			// FIXME: Should I need this? Perhaps to stop drawing...
		}
	}

	private static class DropCallbackCallback extends GLFWDropCallback{
		@Override
		public void invoke(long w, int count, long pointer){
//			long a =MemoryUtil.memGetAddress(pointer);
////			PointerBuffer pointers = MemoryUtil.memPointerBuffer(a, count);
//			int t =0;
//			for(int i =0; i <count; i ++){
////				long address = MemoryUtil.memGetAddress(pointers.get(i));
//				String s = MemoryUtil.memDecodeUTF8(a+t);
//				t +=s.length() +1;
////				Ovr
//				System.out.println("File drop callback, count "+count+", data "+s);
//			}
//			System.out.println("Fin.");
		}
	}

	public static void requestClose(){
		closePressed =true;
	}


}
