package awesome.gui;

import awesome.core.Painter;

public class Panel extends Component{
		
	public float x, y, width, height;

	@Override
	public void logic(){
		
	}
	
	@Override
	public void draw(){
		Painter.setBlankTexture();
		Painter.setColorInt(23, 38, 45);
		Painter.drawQuad(x, y, width, height);
	}

	@Override
	public void update(float parentX, float parentY, float parentWidth, float parentHeight){
		x =parentX;
		y =parentY;
		width =parentWidth;
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
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}

}
