package awesome.core;

import org.lwjgl.glfw.GLFW;



public class Timing{
	private static float timescale = 1, timescaleEngine = 1;
	private static float fps = 60;
	private static float mult = 1;
	
	private static double lastTime;
	protected static void update(){
		double now = GLFW.glfwGetTime();
		double passed = now-lastTime;
		fps = (float)(1d/passed);
		if(fps<12) fps = 12;
		timescaleEngine = (60f/fps);
		timescale = timescaleEngine*mult;
		lastTime = now;
	}
	
	public static float delta(){
		return timescale;
	}
	
	
	public static float engineDelta(){
		return timescaleEngine;
	}	
	
	public static void setTimescale(float _mult){
		mult = _mult;
	}	
	
	public static float getTimescale(){
		return mult;
	}	
	
	public static float getFPS(){
		return fps;
	}
}
