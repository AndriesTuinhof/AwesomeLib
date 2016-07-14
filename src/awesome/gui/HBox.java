package awesome.gui;

import java.util.ArrayList;
import java.util.List;

import awesome.core.Painter;

public class HBox extends Component
{
	private float x, y, width, height;
	private ArrayList<Component> children = new ArrayList<Component>();
	
	public List<Component> getChildren()
	{
		return children;
	}
	
	@Override
	public void draw()
	{
		Painter.setBlankTexture();
		Painter.setColorInt(23, 38, 45);
		Painter.drawQuad(x, y, width, height);
		for(Component c: children) if(c!=null) c.draw();
	}

	@Override
	public void update(float px, float py, float pw, float ph)
	{
		x = px;
		y = py;
		width = pw;
		height = ph;
		float x_offset = 20;
		for(Component c: children) if(c!=null)
		{
			c.update(x+x_offset, y+5, c.getWidth(), c.getHeight());
			x_offset += c.getWidth()+5;
		}
		
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
	public void logic() {
		for(Component c: children) c.logic();
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
