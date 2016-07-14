package awesome.core;
import org.lwjgl.glfw.GLFW;


public class Input{
	
	/*
	 * This class stores and handles the data that is been given by multiple callbacks in @PandaDisplay.
	 */
	
	// KEYBOARD
	private static int typedChar, typedCharRaw;
	private static boolean[] keys =new boolean[1024];
	private static boolean[] keyspre =new boolean[1024];
	private static boolean[] rawKeys =new boolean[1024];
	
	// MOUSE
	private static float mouseX, mouseY, oldMouseX, oldMouseY, rawMouseX, rawMouseY;
	private static float dwheel, rawWheel;	
	private static boolean[] mouseButtons =new boolean[14];
	private static boolean[] mouseButtonsPre =new boolean[14];
	private static boolean[] rawMouseButtons =new boolean[14];
	
	protected static void update(){
		// Keys input
		for(int i =0; i <keys.length; i++){
			keyspre[i] =keys[i];
			keys[i] =rawKeys[i];
		}
		
		typedChar =typedCharRaw;
		typedCharRaw =0;

		// Mouse input
		oldMouseX =mouseX;
		oldMouseY =mouseY;
		mouseX =rawMouseX;
		mouseY =rawMouseY;
		
		for(int i =0; i <mouseButtons.length; i++){
			mouseButtonsPre[i] =mouseButtons[i];
			mouseButtons[i] =rawMouseButtons[i];
		}

		dwheel =rawWheel;
		rawWheel =0;
	}	
	
	protected static void cursorPos(long window, double x, double y){
		rawMouseX =(float)x;
		rawMouseY =PandaDisplay.getHeight() -(float)y;	//flipping it.
	}

	protected static void mouseButton(long window, int button, int action, int mods){
		if(action ==GLFW.GLFW_PRESS) rawMouseButtons[button] =true;
		if(action ==GLFW.GLFW_RELEASE) rawMouseButtons[button] =false;
	}
		
	protected static void scroll(long arg0, double arg1, double arg2){
		rawWheel =(float)arg2;
	}
	
	protected static void setKey(int key, int action){
		if(key <0) return;
		typedCharRaw =key;//FIXME: THIS IS NOT PERFECT
		if(action ==GLFW.GLFW_RELEASE) rawKeys[key] =false;
		if(action ==GLFW.GLFW_PRESS) rawKeys[key] =true;
	}
		
	/*
	 * Public Library Methods
	 */
	public static void cancelInput(){
		cancelKeys();
		cancelMouseButtons();
		dwheel =0;
		rawWheel =0;
	}
		
	public static void cancelKeys(){
		for(int i =0; i <keys.length; i++){
			keys[i] =false;
			keyspre[i] =false;
			rawKeys[i] =false;
		}
	}
		
	public static void cancelMouseButtons(){
		for(int i=0;i<mouseButtons.length;i++){
			mouseButtons[i] =false;
			mouseButtonsPre[i] =false;
			rawMouseButtons[i] =false;
		}
	}
	
	public static void cancelMouseScroll(){
		rawWheel = 0;
		dwheel = 0;
	}
	
	public static boolean keyDown(int k){
		return keys[k];
	}
	
	public static boolean keyPressed(int k){
		return keys[k] && !keyspre[k];
	}

	public static boolean keyReleased(int k){
		return !keys[k] && keyspre[k];
	}

	public static boolean lClicked(){
		return !mouseButtonsPre[0] && mouseButtons[0];
	}
	
	public static boolean rClicked(){
		return !mouseButtonsPre[1] && mouseButtons[1];
	}
	
	public static boolean middleClicked(){
		return !mouseButtonsPre[2] && mouseButtons[2];
	}
	
	public static boolean middleDown(){
		return mouseButtonsPre[2] && mouseButtons[2];
	}
	
	public static int typedCharacter(){
		return typedChar;
	}
		
	public static boolean mouseButtonDown(int b){
		return mouseButtons[b];
	}
	
	public static boolean lDown(){
		return mouseButtons[0];
	}
	
	public static boolean rDown(){
		return mouseButtons[1];
	}

	public static boolean lReleased(){
		return mouseButtonsPre[0] && !mouseButtons[0];
	}
	
	public static float getMouseWheelDelta(){
		return dwheel;
	}
	
	public static float getMouseDeltaX(){
		return mouseX - oldMouseX;
	}
	
	public static float getMouseDeltaY(){
		return mouseY - oldMouseY;
	}
	
	public static float getMouseX(){
		return mouseX;
	}
	
	public static float getMouseY(){
		return mouseY;
	}
	
	public static boolean cursorWithin(float x, float y, float w, float h){
		float mx =getMouseX();
		float my =getMouseY();
		return mx >=x && my >=y && mx <=x+w && my <=y+h;
	}


}
