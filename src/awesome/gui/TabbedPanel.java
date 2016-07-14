package awesome.gui;

import java.util.ArrayList;

import awesome.core.Input;
import awesome.core.Painter;
import awesome.core.UI;

public class TabbedPanel extends Component
{
	private float x, y, width, height, tabHeight=24f, tabWidth;
	private ArrayList<Component> children = new ArrayList<Component>();
	private ArrayList<String> hints = new ArrayList<String>();
	private Component activeTab;
	
	
	public TabbedPanel addTab(Component c, String hint)
	{
		children.add(c);
		hints.add(hint);
		return this;
	}
	
	public void draw(){
		Painter.setBlankTexture();
		Painter.setColorInt(23, 38, 45);
		for(int i=0;i<children.size();i++)
		{
			
			Painter.setColor((float)Math.sin(i)*0.15f+0.5f, (float)Math.cos(i*3)*0.15f+0.5f, (float)Math.sin(i*7+2)*0.15f+0.5f);
			Painter.drawQuad(x+i*tabWidth, (y+height)-tabHeight, tabWidth, tabHeight);
		}
		if(activeTab!=null) activeTab.draw();
		
	}

	@Override
	public void update(float px, float py, float pw, float ph)
	{
		x = px;
		y = py;
		width = pw;
		height = ph;
		
		tabWidth = width/children.size();
		
		float panelHeight = height-tabHeight;
		for(Component c: children) if(c!=null) c.update(x, y, width, panelHeight);
		
		if(activeTab==null && children.size()>0) activeTab = children.get(0);
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
	public void logic()
	{
		if(children.size()==0) return;
		if(Input.cursorWithin(x, (y+height)-tabHeight, width, tabHeight))
		{
			int hovering =(int)((Input.getMouseX()-x)/width*children.size());
//			UI.setHint(hints.get(hovering));
			if(Input.lClicked()) activeTab = children.get(hovering);
		}
		
		if(activeTab!=null) activeTab.logic();
		
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return x;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return y;
	}

}
