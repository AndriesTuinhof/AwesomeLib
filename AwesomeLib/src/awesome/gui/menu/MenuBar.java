package awesome.gui.menu;

import java.util.ArrayList;

import awesome.core.Input;
import awesome.core.Painter;
import awesome.gui.Component;

public class MenuBar extends Component
{
	private ArrayList<MenuItem> menus = new ArrayList<MenuItem>();
	private float x, y, width, height;
	
	public MenuBar(){
		x = 0;
		y = 0;
	}
	
	public void addMenu(MenuItem m){
		menus.add(m);
	}

	
	public void draw(){
		Painter.setBlankTexture();
		Painter.setColor(43/255f, 47/255f, 49/255f);
		Painter.drawQuad(x, y, width, height);
		for(MenuItem m: menus)if(m!=null)
			m.draw();
	}


	public void update(float px, float py, float pw, float ph){
		x =px;
		height =ph;
		y =(py +ph) -height;
		float menuSpacing =5f;
		float x_offset =10;
		for(MenuItem m: menus){
			m.update(x+x_offset, y, width, height);
			x_offset +=m.getWidth() +menuSpacing;
		}
		width =x_offset;
		width =pw;
	}


	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	public float getWidth(){
		return width;
	}

	public float getHeight(){
		return height;
	}


	public void logic(){
		for(MenuItem m: menus) if(m!=null) m.logic();
		if(Input.cursorWithin(x, y, width, height)) Input.cancelMouseButtons();
	}

}
