package awesome.gui.menu;

import awesome.core.Input;
import awesome.core.Painter;
import awesome.core.UI;
import awesome.gui.ActionListener;

public class MenuButton extends MenuItem{
	
	private String text;
	private float x, y, width, height;
	private boolean mouseHovering;
	private ActionListener actionTarget;

	public MenuButton(){
		text ="";
	}
	
	public MenuButton(String itemText){
		text = itemText;
	}
	
	public MenuButton setActionListener(ActionListener l){
		actionTarget = l;
		return this;
	}
	
	public String getText(){
		return text;
	}

	@Override
	public void logic(){
		mouseHovering =false;
		if(!Input.cursorWithin(x, y, width, height)) return;
		mouseHovering =true;
		if(!Input.lClicked()) return;
		if(actionTarget!=null) actionTarget.onActionEvent();
		UI.setPopup(null);
		Input.cancelMouseButtons();
	}

	@Override
	public void draw(){
		Painter.setWhite();
		if(mouseHovering)
			Painter.setColor(0.9f, 0.9f, 0.9f);
		Painter.drawStringCenter(text, x +width /2f, y +height /2f, 12);
		
	}


	@Override
	public void popupLogic() {
		if(!Input.cursorWithin(x+width, y-height, width, height)){
			if(Input.lClicked()) UI.setPopup(null);
			return;
		}

		Input.cancelInput();
	}

	@Override
	public void popupDraw(){
		if(parent !=null){
			if(parent instanceof Menu){
				Menu m =(Menu)parent;
				m.popupDraw();
			}
			else
				parent.draw();
		}
	}
		
	@Override
	public void update(float parentX, float parentY, float parentWidth, float parentHeight) {
		x =parentX;
		y =parentY;
		width =text.length()*8.5f;
		height =parentHeight;
	}

	@Override
	public float getWidth(){
		return width;
	}

	@Override
	public float getHeight(){
		return height;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}
}
