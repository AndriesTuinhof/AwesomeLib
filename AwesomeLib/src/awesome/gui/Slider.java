package awesome.gui;

import awesome.core.Input;
import awesome.core.Painter;
import awesome.core.PandaDisplay;

public class Slider extends Component{
	
	private float x, y, width, height, tabWidthScale =0.1f, value;
	private boolean mouseHovering, mousePress;
	
	public Slider(float width, float tabWidthscale, float value){
		this.width =width;
		this.value =value;
		this.tabWidthScale =tabWidthscale;
	}

	public Slider(){
		
	}

	@Override
	public void logic(){		
		float tabx =x +value *(width -tabWidthScale);
		
		mouseHovering =false;
		if(!Input.lDown())
			mousePress =false;
		
		if(mousePress){
			float delta = Input.getMouseDeltaX();
			value=Math.max(Math.min(value+delta/(width-getTabWidth()), 1.0f), 0.0f);
			if(delta !=0.0f) onValueChange();
		}
		
		if(!Input.cursorWithin(tabx, y, getTabWidth(), height)) return;
		mouseHovering = true;
		if(PandaDisplay.getDirectMouseDown()) mousePress = true;
//		UI.setHint(hint);

	}
	
	public void draw(){
		// Background
		Painter.setColor(.15f, .15f, .25f, 1.0f);
		Painter.drawQuad(x, y, width, height);
//		Painter.setColor(1, 1, 1, 1.0f);
		
		float tabx =x +value *(width -getTabWidth());
		

		Painter.setColorInt(0, 95, 107);
		Painter.drawQuad(tabx, y, getTabWidth(), height);

	}

	@Override
	public void update(float x, float y, float w, float h){
		this.x =x;
		this.y =y;
		this.width =w;
		this.height =h;
	}

	public float getWidth(){
		return width;
	}

	public float getHeight(){
		return height;
	}
	
	@Override
	public float getX(){
		return x;
	}

	@Override
	public float getY(){
		return y;
	}
	
	public void onValueChange(){
//		if(listener!=null) listener.onActionEvent(value);
	}

	
	public float getTabWidth() {
		return width *tabWidthScale;
	}

	public void setTabWidthScale(float tabWidth){
		this.tabWidthScale = tabWidth;
	}
	
	public float getValue(){
		return value;
	}

	public void setValue(float value){
		this.value = value;
	}

	public boolean isMouseHovering(){
		return mouseHovering;
	}

	public boolean isMousePress(){
		return mousePress;
	}
}
