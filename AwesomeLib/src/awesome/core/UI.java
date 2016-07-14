package awesome.core;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import awesome.gui.Component;
import awesome.gui.Popup;
import awesome.gui.Renderer3D;

public class UI {
	
	private static Component child;
	
	private static Object objectFocus;
	private static Popup popup;
	
	public static ArrayList<Renderer3D> view3d =new ArrayList<>();
	
	public static void logic(){
		if(popup !=null)
			popup.popupLogic();
		if(child !=null)
			child.logic();	
	}
	
	protected static void draw(){
		GL11.glViewport(0, 0, PandaDisplay.getWidth(), PandaDisplay.getHeight());
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		// Draw UI
		if(child !=null)
			child.draw();
		
		// Draw popped up panel with background fade
		if(popup!=null)popup.popupDraw();
		
		
		// Draw border
		if(!PandaDisplay.isBorderless())
			return;
		float size =2;
		Painter.setBlankTexture();
		Painter.setColor(0.45f, 0.45f, 0.55f, 0.7f);
		Painter.drawQuad(0, 0, size, PandaDisplay.getHeight());
		Painter.drawQuad(PandaDisplay.getWidth()-size, 0, size, PandaDisplay.getHeight());
		Painter.drawQuad(0, 0, PandaDisplay.getWidth(), size);
	}
	
	protected static void logic3DView(){
		for(Renderer3D r: view3d)if(r !=null)
			r.logic3DScene();
	}
	
	protected static void draw3DView(){
		for(Renderer3D r: view3d)if(r !=null){
			r.draw3DScene();
		}
	}
	
	public static void addChild(Component c){
		child =c;
		update();
	}

	protected static void update(){
		if(child !=null)
			child.update(0, 0, PandaDisplay.getWidth(), PandaDisplay.getHeight());
	}
	
	public static void setFocus(Object o){
		objectFocus =o;
	}
	
	public static Object getFocusObject(){
		return objectFocus;
	}
	
	public static void setPopup(Popup o){
		popup =o;
	}
	
	public static Popup getPopupObject(){
		return popup;
	}
	
}
