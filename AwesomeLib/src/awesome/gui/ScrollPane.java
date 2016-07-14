package awesome.gui;

import awesome.core.Input;
import awesome.core.Painter;
import awesome.core.PandaDisplay;
import awesome.core.UI;

public class ScrollPane extends Component{
	
	private float x, y, width, height, tabSize, value;
	private boolean mouseHovering, mousePressed;
	private ActionListener listener;
	
	private boolean isHorizontal;
	
	public ScrollPane(){
		this(false);
	}
	
	public ScrollPane(boolean horizontal){
		tabSize =height *0.1f;
		isHorizontal =horizontal;
	}
	
	@Override
	public void logic(){
		float cx, cy, cw, ch;
		float h =getValue() *((isHorizontal) ?width :height);
		cx =(isHorizontal ? x +h :x);
		cy =(isHorizontal ? y :y +h);
		cw =(isHorizontal ?tabSize :width);
		ch =(isHorizontal ?height :tabSize);
		float to =tabSize /(isHorizontal ?width :height);
		value =Math.max(Math.min(value, 1.0f -to), 0.0f);	
	
		if(Input.cursorWithin(x, y, width, height)){
			if(Input.lClicked() ||Input.lDown()){
				UI.setFocus(this);
//				value =((isHorizontal ?Input.getMouseDeltaX() :Input.getMouseDeltaY())) /(((isHorizontal ? width :height)));
				if(isHorizontal)
					value =(Input.getMouseX() -this.x -tabSize /2f) /(width);
				else
					value =(Input.getMouseY() -this.y -tabSize /2f) /(height);
					System.out.println(value);
			}
		}
		else{
			if(Input.lClicked()){
				UI.setFocus(null);
				mousePressed =false;
			}
		}
		
		if(!Input.lDown() || UI.getFocusObject() !=this)
			mousePressed =false;
		
		if(UI.getFocusObject() ==this){
			float f =Input.getMouseWheelDelta() *((tabSize /2f) /(((isHorizontal) ?width :height)));
			if(f !=0f)
				value +=f;
			value =Math.max(Math.min(value, 1.0f -to), 0.0f);			
		}
		
		if(mousePressed){
			float delta =(isHorizontal) ?Input.getMouseDeltaX() :Input.getMouseDeltaY() -tabSize;
			value +=delta /((isHorizontal ? width :height));
			if(delta !=0.0f)
				onValueChange();
			System.out.println(value);
		}
		

//		float h =getValue() *((isHorizontal) ?width :height);
		
		if(!Input.cursorWithin(cx, cy, cw, ch))
			return;
		
		mouseHovering =true;
		if(PandaDisplay.getDirectMouseDown())
			mousePressed =true;

	}

	public void draw(){
		Painter.setColorInt(52, 56, 56);
		Painter.drawQuad(x, y, width, height);
		Painter.setWhite();

		float cx, cy, cw, ch;
		float h =getValue() *((isHorizontal) ?width :height);
		cx =(isHorizontal ? x +h :x);
		cy =(isHorizontal ? y :y +h);
		cw =(isHorizontal ?tabSize :width);
		ch =(isHorizontal ?height :tabSize);

		Painter.setColorInt(0, 95, 107);
		Painter.drawQuad(cx, cy, cw, ch);
	}

	@Override
	public void update(float x, float y, float w, float h){
		this.x =x;
		this.y =y;
		this.width =w;
		this.height =h;
		if(isHorizontal)
			tabSize =width *0.1f;
		else
			tabSize =height *0.1f;
	}

	@Override
	public float getX(){
		return x;
	}

	@Override
	public float getY(){
		return y;
	}

	public float getWidth(){
		return width;
	}

	public float getHeight(){
		return height;
	}
	
	public float getValue(){
		return value;
	}
	
	public void onValueChange(){
		if(listener!=null)
			listener.onActionEvent();
	}
	
	public void setTabHeight(float height){
		tabSize =height;		
		tabSize =Math.max(tabSize, 4);
	}
}
