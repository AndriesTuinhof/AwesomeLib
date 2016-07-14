package awesome.gui;

import java.util.ArrayList;
import java.util.List;

import awesome.core.Painter;

public class Vbox extends Component{
	
	private float x, y, w, h;
	private ArrayList<Component> children;
	
	public Vbox(){
		children =new ArrayList<Component>();
	}

	public Vbox(ArrayList<Component> children){
		this.children =children;
	}
	
	public Vbox(Component... comps){
		this();
		addChild(comps);
	}
	
	public List<Component> getChildren(){
		return children;
	}

	@Override
	public void logic(){
		for(Component c: children) c.logic();
	}
	
	@Override
	public void draw(){
		Painter.setBlankTexture();
		Painter.setColorInt(23, 38, 45);
		Painter.drawQuad(x, y, w, h);
		for(Component c: children) if(c!=null) c.draw();
	}

	@Override
	public void update(float x, float y, float w, float h){
		this.x =x;
		this.y =y;
		this.w =w;
		this.h =h;
		float y_offset =h;
		for(Component c: children)if(c !=null){
			c.update(x, y +y_offset -c.getHeight(), w, c.getHeight());
			y_offset -=c.getHeight();
		}
	}

	public void addChild(Component c){
		children.add(c);
		update(x, y, w, h);
	}

	public void addChild(Component... comps){
		for(Component c: comps)
			children.add(c);
		update(x, y, w, h);
	}
	
	@Override
	public float getWidth(){
		return w;
	}

	@Override
	public float getHeight(){
		return h;
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
