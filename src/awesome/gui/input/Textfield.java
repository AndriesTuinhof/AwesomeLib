package awesome.gui.input;

import awesome.core.Input;
import awesome.core.Painter;
import awesome.core.UI;
import awesome.gui.ActionListener;
import awesome.gui.Component;
import awesome.gui.Validator;
import awesome.util.Keyboard;

public class Textfield extends Component{
	
	private String value ="";
	private float x, y, width, height;
	private int maxinumCharachters =20;
	private boolean mouseHovering;
	
	public Validator validator;
	public ActionListener onChangeListener;	
	
	public Textfield(String textValue, float width){
		this.value =textValue;
		this.width =width;
	}
	
	public Textfield(){
		
	}
	
	public String getValue(){
		return value;
	}
	
	public Textfield setValue(String s){
		value =s;
		return this;
	}

	public Textfield setMaxinumCharachters(int i){
		maxinumCharachters =i;
		return this;
	}

	@Override
	public void logic(){
		mouseHovering =false;
		if(UI.getFocusObject() ==this){
			if(value.length() !=0 && Input.keyPressed(Keyboard.KEY_BACKSPACE))
				value =value.substring(0, value.length() -1);
			else if(value.length() <maxinumCharachters){
				int tc =Input.typedCharacter();
				String s =Character.toString((char)tc);
				if(Input.typedCharacter() !=0 && tc !=259){
					if(onChangeListener !=null)
						onChangeListener.onActionEvent();
					value +=s;
				}
			}
		}
		if(Input.cursorWithin(x, y, width, height))
			mouseHovering =true;
		if(!Input.lClicked())
			return;
		if(!Input.cursorWithin(x, y, width, height)){
			UI.setFocus(null);
			return;
		}
		if(UI.getFocusObject() !=this) UI.setFocus(this);
		Input.cancelMouseButtons();
	}
	
	public void draw(){
		Painter.setColorInt(52, 56, 56);
		if(mouseHovering) Painter.setWhite();
		if(validator !=null && !validator.validate(getValue()))
			Painter.setColorInt(245, 41, 11);
		Painter.drawQuad(x, y, width, height);
		Painter.setColorInt(52, 56, 56);
		Painter.drawQuad(x +2, y +2, width -4, height -4);
		Painter.setWhite();
		Painter.drawString(value, x +2, y +2, 12);
		
		if(UI.getFocusObject() ==this){
			float cw =value.length() *45 *12;
			Painter.drawQuad(this.x +cw, y, 2, height);
		}
	}

	@Override
	public void update(float px, float py, float pw, float ph){
		x = px;
		y = py;
		width =pw;
		height =ph;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	public float getWidth(){
		return width;
	}

	public float getHeight(){
		return height;
	}
}
