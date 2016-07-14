package awesome.gui;

import awesome.core.Painter;

public class Label extends Component{
	
	public float x, y, w, h;
	public String title ="";
	public float fontSize =12;

	@Override
	public void logic() {
		
	}

	@Override
	public void draw() {
		Painter.setColor(c);
		Painter.drawQuad(x, y, w, h);
		
		Painter.setWhite();
		Painter.drawStringCenter(title, x +w /2f, y +h /2f, 12);
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
