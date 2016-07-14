package awesome.gui.input;
import awesome.core.Input;
import awesome.core.Painter;
import awesome.core.PandaDisplay;
import awesome.gui.ActionListener;
import awesome.gui.Component;

public class Button extends Component{
	
	private String value ="", hint;
	private float x, y, width, height =20;
	private boolean mouseHovering, mousePress;
	private ActionListener listener;
	private int hotkey;
	
	public Button(String textValue, float _width, String hintText){
		value = textValue;
		width = _width;
		hint = hintText;
	}
	
	public Button(){
		width =120;
	}
	
	public Button setTitle(String s){
		value =s;
		return this;
	}
	
	public Button setListener(ActionListener l){
		listener = l;
		return this;
	}
	
	public Button setHotkey(int key){
		hotkey = key;
		return this;
	}
	
	public String getValue(){
		return value;
	}

	public void draw(){
		Painter.setColorInt(52, 56, 56);
		if(mouseHovering)
			Painter.setWhite();
		Painter.drawQuad(x, y, width, height);
		
		Painter.setColorInt(52, 56, 56);
		Painter.drawQuad(x +2, y +2, width -4, height -4);
				
		Painter.setWhite();
		Painter.drawStringCenter(value, x +width /2f, y +height /2f, 12);		
		
	}

	@Override
	public void update(float px, float py, float pw, float ph){
		x =px;
		y =py;
		width =pw;
		height =ph;
	}


	public float getWidth(){
		return width;
	}


	public float getHeight(){
		return height;
	}

	@Override
	public void logic(){
		if(hotkey>0 && Input.keyPressed(hotkey)){
			onActionEvent();
			Input.cancelKeys();
			return;
		}
		
		mouseHovering =false;
		mousePress =false;
		if(!Input.cursorWithin(x, y, width, height)) return;
		mouseHovering =true;
		if(PandaDisplay.getDirectMouseDown()) mousePress = true;
//		UI.setHint(hint);
		if(!Input.lReleased())return;
		onActionEvent();
		Input.cancelMouseButtons();
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
		if(listener!=null)
			listener.onActionEvent();
	}
	
	public boolean isButtonDown(){
		return mousePress;
	}
}
