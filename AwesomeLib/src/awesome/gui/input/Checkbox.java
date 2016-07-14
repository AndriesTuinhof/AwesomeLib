package awesome.gui.input;
import awesome.core.Input;
import awesome.core.Painter;
import awesome.core.PandaDisplay;
import awesome.gui.ActionListener;
import awesome.gui.Component;

public class Checkbox extends Component{
	
	private float x, y, size =20;
	private boolean mouseHovering, mousePress;
	private ActionListener listener;
	private boolean value;
	
	public Checkbox(){
		
	}
	
	public Checkbox(boolean value){
		this.value =value;
	}
	
	public Checkbox setListener(ActionListener l){
		listener =l;
		return this;
	}
	
	public boolean getValue(){
		return value;
	}

	@Override
	public void logic(){		
		mouseHovering =false;
		mousePress =false;
		if(!Input.cursorWithin(x, y, size, size)) return;
		mouseHovering =true;
		if(PandaDisplay.getDirectMouseDown()) mousePress = true;
		if(!Input.lReleased()) return;
		onActionEvent();
		value =!value;
		Input.cancelMouseButtons();
	}

	public void draw(){
		Painter.setWhite();

		Painter.setColorInt(52, 56, 56);
		if(mouseHovering)
			Painter.setWhite();
		Painter.drawQuad(x, y, size, size);
		
		Painter.setColorInt(52, 56, 56);
		Painter.drawQuad(x +2, y +2, size -4, size -4);
		
		
		if(!value)return;
		Painter.setColorInt(0, 95, 107);
		Painter.drawQuad(x +4, y +4, size -8, size -8);
	}

	@Override
	public void update(float px, float py, float pw, float ph){
		x =px;
		y =py;
		size =Math.min(pw, ph);
	}


	public float getWidth(){
		return size;
	}


	public float getHeight(){
		return size;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}
	
	public void onActionEvent(){
		if(listener!=null) listener.onActionEvent();
	}
	
	public boolean isButtonDown(){
		return mousePress;
	}
}
