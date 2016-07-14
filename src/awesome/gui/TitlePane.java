package awesome.gui;

import awesome.core.Painter;
import awesome.core.PandaDisplay;
import awesome.gui.input.Button;

public class TitlePane extends Panel{

	public float x, y, w, h;
	public float titlebarHeight =20;
	
	public String title ="";
	public Button hideButton;
	
	public Component child;
	public boolean show =true;
	
	public TitlePane() {
		hideButton =new Button();
		hideButton.setTitle(">");
		hideButton.setListener(new ActionListener() {
			
			@Override
			public void onActionEvent() {
				show =!show;
				hideButton.setTitle((show? "^" : ">"));
				
			}
		});
	}
	
	public TitlePane(Component c){
		this();
		addChild(c);
	}
	
	@Override
	public void logic(){
		hideButton.logic();
		hideButton.update(x +w -20, y +h -titlebarHeight, 20, titlebarHeight);
		

		if(show && child !=null)
			
			child.logic();
	}
	
	@Override
	public void draw(){
		Painter.setBlankTexture();
		Painter.setColorInt(23, 38, 45);
		Painter.drawQuad(x, y, w, h);
		Painter.setColorInt(251, 125, 111);
		Painter.drawQuad(x, y +h -titlebarHeight, w, titlebarHeight);
		hideButton.draw();
		Painter.setWhite();
		Painter.drawStringCenter(title, x +w /2f, y +h -titlebarHeight /2f, titlebarHeight /2f);
		if(show && child !=null)
			child.draw();
		
	}

	@Override
	public void update(float x, float y, float width, float height){
		this.x =x;
		this.y =y;
		this.w =width;
		this.h =height;
		
		if(child !=null)
			child.update(x, y, width, height -titlebarHeight);
	}

	@Override
	public float getWidth(){
		return w;
	}

	@Override
	public float getHeight(){
		return h;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public void addChild(Component c){
		this.child =c;
		child.update(x, y, w, h -titlebarHeight);
	}

	public void setTitle(String title){
		this.title =title;
	}
}
