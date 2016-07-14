package awesome.core;


import awesome.graphics.legacy.Immediate3D;

public class PandaEngine{
	
	public static void create(String resourcePath){
		try{
			ResourceLoader.create(resourcePath);
			PandaDisplay.create();
			Painter.create();
		}
		catch(Exception e){
			System.out.println("EngineWindow.create() creation exception:");
			throw new RuntimeException(e);
		}
	}
	public static void create(String title, String resourcePath){
		create(resourcePath);
		PandaDisplay.setTitle(title);
	}
	
	public static void update(){
		UI.logic();
		UI.logic3DView();
		PandaDisplay.update();
		Input.update();
		Timing.update();
	}
	
	public static void destroy(){
		Painter.destroy();
		Immediate3D.destroy();
		PandaDisplay.destroy();
	}
}
