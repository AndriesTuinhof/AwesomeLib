package awesome.gui.menu;

import java.util.ArrayList;

import awesome.core.Input;
import awesome.core.Painter;
import awesome.core.UI;

public class Menu extends MenuItem{
	
	private ArrayList<MenuItem> items = new ArrayList<MenuItem>();
	private String text;
	private float x, y, width, height;
	private boolean mouseHovering, mousePressed;

	public Menu(String textValue){
		text = textValue;
	}
	
	public Menu(){
		text ="";
	}

	@Override
	public void update(float x, float y, float w, float h){
		this.x =x;
		this.y =y;
		this.width =text.length() *8.5f;
		this.height =h;
		
		float y_offset =0;
		for(MenuItem item: items)if(item!=null){
			y_offset -=h; // here or after item update?
			float width =(parent !=null)? this.width :0;
			item.update(x+w, y+y_offset, w, h);
		}
	}
	@Override
	public void logic(){
		mousePressed =false;
		if(UI.getPopupObject() ==this)
			mousePressed = true;
		mouseHovering =false;
		if(!Input.cursorWithin(x, y, width, height))
			return;
		mouseHovering =true;
		if(Input.lClicked())
			UI.setPopup(this);
		for(MenuItem i: items)
			i.logic();
	}
	
	public void draw(){
//		Painter.setColor(.4f, .4f, .5f, 0.7f);
		if(mouseHovering) Painter.setColor(.5f, .5f, .6f, 0.7f);
		if(mousePressed) Painter.setColor(.3f, .3f, .4f, 0.7f);// TODO: Are these two lines needed?
		if(parent !=null){
			Painter.setColor(43/255f, 47/255f, 49/255f);
			Painter.drawQuad(x, y, width, height);
		}
		Painter.setColor(1, 1, 1, 1);
		Painter.drawString(text, x+2, y+1, 12);
		
	}

	@Override
	public void popupLogic() {
		if(parent !=null){
			if(this ==UI.getPopupObject()){
				parent.popupLogic();
			}
		}
		for(MenuItem i: items){
			if(i !=UI.getPopupObject()){
				i.popupLogic();
			}
		}

		if(this ==UI.getPopupObject())
		Input.cancelInput();
	}

	@Override
	public void popupDraw(){
		if(parent !=null){
			parent.popupDraw();
		}
		

		System.out.println(UI.getPopupObject()+"");
//		Painter.setColor(0,0,0,0.5f);
//		Painter.drawQuad(drawerX+4, drawerY-4, drawerWidth, drawerHeight);
//		Painter.setColor(.4f, .4f, .5f);
//		Painter.drawQuad(drawerX, drawerY, drawerWidth, drawerHeight);

		Painter.setColor(1, 1, 1);

		if(items.size() >0)
			for(MenuItem item: items) if(item!=null) item.draw();
		
		if(this !=UI.getPopupObject()){
			Painter.setColor(1f, .4f, .5f, 0.7f);
			Painter.drawQuad(x, y+2, width, height);
		}
		else{
			Painter.setColor(0.1f, .2f, .8f, 0.7f);
			Painter.drawQuad(x, y+2, width, height);			
		}
	}	
	
	public void addMenuItem(MenuItem m){
		items.add(m);
		m.parent =this;
	}
	
	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public float getHeight() {
		return height;
	}
	
	@Override
	public String toString() {
		return text;
	}
}
