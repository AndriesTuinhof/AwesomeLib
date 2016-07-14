package awesome.gui;

import java.util.ArrayList;

import awesome.core.Painter;

public class StackPanel extends Component{
		
	public float x, y, width, height;
	public ArrayList<Component> child =new ArrayList<>(0);
	public Color color =new Color(23, 38, 45);

	@Override
	public void logic(){

		for(Component c: child)if(c !=null)
			c.logic();
	}
	
	@Override
	public void draw(){
		Painter.setBlankTexture();
		Painter.setColor(color);
		Painter.drawQuad(x, y, width, height);
		
		for(Component c: child)if(c !=null){
			c.draw();
		}
	}

	@Override
	public void update(float parentX, float parentY, float parentWidth, float parentHeight){
		x =parentX;
		y =parentY;
		width =parentWidth;
		height =parentHeight;
		for(Component c: child)if(c !=null){
			c.update(x, y, width, height);
		}
	}

	@Override
	public float getWidth(){
		return width;
	}

	@Override
	public float getHeight(){
		return height;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}

	public void addChild(Component c){
		c.update(x, y, width, height);
		child.add(c);
	}
}
