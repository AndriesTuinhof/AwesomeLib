package awesome.gui;

public abstract class Component {

	public Color c =new Color();
	
	public abstract void logic();
	public abstract void draw();
	public abstract void update(float x, float y, float w, float h);
	
	public abstract float getX();
	public abstract float getY();
	public abstract float getWidth();
	public abstract float getHeight();
}
