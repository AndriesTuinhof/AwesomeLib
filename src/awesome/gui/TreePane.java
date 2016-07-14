package awesome.gui;

import awesome.core.Painter;

public class TreePane<T> extends Component{

	private Tree<T> tree;
	public float x, y, w, h;

	public TreePane(){
		
	}
	
	public TreePane(Tree<T> tree){
		this.tree =tree;
	}
	
	@Override
	public void logic() {
		
	}

	@Override
	public void draw() {
		Painter.setBlankTexture();
		Painter.setWhite();
	}

	@Override
	public void update(float x, float y, float w, float h) {
		this.x =x;
		this.y =y;
		this.w =w;
		this.h =h;
		
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
		return w;
	}

	@Override
	public float getHeight() {
		return h;
	}
}
