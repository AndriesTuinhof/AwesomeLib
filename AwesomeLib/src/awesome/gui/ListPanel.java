package awesome.gui;

import java.util.ArrayList;

import awesome.core.Input;
import awesome.core.Painter;

public class ListPanel extends Component{
	
	private float x, y, w, h, tabHeight =24f, tabWidth;
	private ArrayList<Component> children =new ArrayList<Component>();
	private ArrayList<String> hints =new ArrayList<String>();
	private Component activeTab;
	private String title;
	
	public ListPanel(){
		
	}
	
	public ListPanel(String title){
		this.title =title;
	}
	
	public ListPanel addTab(Component c){
		children.add(c);
//		hints.add(hint);
		return this;
	}

	@Override
	public void logic(){
		if(children.size() ==0) return;
		if(Input.cursorWithin(x, y +h-tabHeight, w, tabHeight)){
			int hovering =(int)((Input.getMouseX() -x)/ w *children.size());
//			UI.setHint(hints.get(hovering));
			System.out.println(hovering);
			if(Input.lClicked())
				activeTab =children.get(hovering);
		}
		
		if(activeTab !=null)
			activeTab.logic();
		
	}
	
	public void draw(){
		for(int i =0; i <children.size(); i++){			
			Painter.setColor((float)Math.sin(i) *0.15f +0.5f, (float)Math.cos(i *3) *0.15f +0.5f, (float)Math.sin(i *7 +2) *0.15f +0.5f);
			Painter.drawQuad(x +i *tabWidth, (y +h) -tabHeight, tabWidth, tabHeight);
		}
		Painter.setColor(1, 1, 1);
		Painter.drawStringCenter(title, x +w /2f, y +h -12, 12);
		if(activeTab!=null)
			activeTab.draw();
	}

	@Override
	public void update(float x, float y, float w, float h){
		this.x =x;
		this.y =y;
		this.w =w;
		this.h =h;
		
		tabWidth =w /children.size();
		
		float panelHeight =h -tabHeight;
		for(Component c: children)if(c!=null)
			c.update(x, y, w, panelHeight);
		
		if(activeTab ==null && children.size() >0)
			activeTab =children.get(0);
	}

	@Override
	public float getWidth() {
		return w;
	}

	@Override
	public float getHeight() {
		return h;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

}
