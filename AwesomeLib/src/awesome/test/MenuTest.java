package awesome.test;

import awesome.core.Input;
import awesome.core.Painter;
import awesome.core.PandaDisplay;
import awesome.core.PandaEngine;
import awesome.core.UI;
import awesome.gui.Hsplit;
import awesome.gui.ScrollPane;
import awesome.gui.Slider;
import awesome.gui.StackPanel;
import awesome.gui.TitlePane;
import awesome.gui.Tree;
import awesome.gui.TreeItem;
import awesome.gui.TreeObject;
import awesome.gui.TreePane;
import awesome.gui.Vsplit;
import awesome.gui.input.Button;
import awesome.util.Keyboard;

public class MenuTest {

	public static void main(String[] arg){
		try{
//			PandaEngine.create("MenuTest", "C:/Users/Andries/Dropbox/Betapanda/PolluxProject/Resources/", "font.png");
			PandaEngine.create("D:/Dropbox/Betapanda/AnimationEditor/Resources/");
		
			StackPanel pane =new StackPanel();
			Hsplit hs1 =new Hsplit();
			hs1.setRightWidth(10);
			hs1.setRightChild(new ScrollPane());
			
			Vsplit vs1 =new Vsplit();
			vs1.setTopChild(hs1);
			vs1.setBottomHeight(10);
			vs1.setBottomChild(new ScrollPane(true));

			Vsplit vs2 =new Vsplit();
			
			StackPanel st2 =new StackPanel(){
				
				Button button;
				Slider slider;
				{
					button =new Button();
					button.setTitle("Button");
					slider =new Slider();
				}
				
				@Override 
				public void logic(){
					button.logic();
					slider.logic();
				}
				
				@Override 
				public void draw(){
					Painter.setBlankTexture();
					Painter.setColorInt(220, 220, 220);
					Painter.drawQuad(x, y, width, height);
					button.draw();
					slider.draw();
				}
				
				@Override
				public void update(float x, float y, float w, float h){
					this.x =x;
					this.y =y;
					this.width =w;
					this.height =h;
					button.update(x +(w -100) /2f, y +(h -20) /2f, 100, 20);
					slider.update(x +(w -100) /2f, y +(h -20) /4f, 100, 20);
				}
			};
			TitlePane tp =new TitlePane();
			tp.addChild(st2);
			tp.setTitle("test Title");
			vs2.setTopChild(tp);
			
			Tree<String> tree =new Tree<>();
			tree.objects.add(new TreeItem<String>());
			TreePane<String> treep =new TreePane<>(tree);
			vs2.setBottomChild(treep);
			
			hs1.setLeftChild(vs2);
			
			pane.addChild(vs1);
			UI.addChild(pane);
			
			boolean running =true;			
			while(running){
				if(Input.keyPressed(Keyboard.KEY_ESCAPE) || PandaDisplay.closePressed()) running =false;
				
				PandaEngine.update();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
