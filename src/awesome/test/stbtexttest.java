package awesome.test;

import awesome.core.Input;
import awesome.core.Painter;
import awesome.core.PandaDisplay;
import awesome.core.PandaEngine;
import awesome.core.ResourceLoader;
import awesome.util.Keyboard;

public class stbtexttest {

	public static void main(String[] arg){
		try{
			PandaEngine.create("D:/Dropbox/Betapanda/AnimationEditor/Resources/");
			PandaDisplay.setTitle("TEST");
		
			System.out.println(""+PandaDisplay.getVersion());
//			int tex =ResourceLoader.getTexture("font.png", false);
			int tex =ResourceLoader.getTexture("font.png", true);
			
			boolean running =true;			
			while(running){
				if(Input.keyPressed(Keyboard.KEY_ESCAPE) || PandaDisplay.closePressed()) running =false;

//				Painter.testDrawTest("adsf", 50, 50, 12);
				
				Painter.setWhite();
				Painter.setTexture(tex);
				Painter.drawQuad(0, 0, PandaDisplay.getWidth(), PandaDisplay.getHeight());
//				Painter.testDrawTest("Adam's dick is small", 50, 200, 20);
				
				PandaEngine.update();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
