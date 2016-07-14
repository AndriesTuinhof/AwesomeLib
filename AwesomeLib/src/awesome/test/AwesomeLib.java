package awesome.test;

import java.io.File;

import awesome.core.PandaEngine;

public class AwesomeLib {

	public static void main(String[] args) {
		try{
			System.out.println("This is a library not a program!");

			PandaEngine.create(null);
			
			
			for(int i =0;  i <5; i++){
				
				PandaEngine.update();
				Thread.sleep(1000);
			}
			
			PandaEngine.destroy();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
