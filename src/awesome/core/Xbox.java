package awesome.core;

public class Xbox{
		
	public static int btn_start =8, btn_back =9, btn_A =10, btn_B =11, btn_Y =12, btn_X =13, btn_leftShoulder =14, btn_rightShoulder =15, btn_rightThumb =16, btn_leftThumb =17;
	public static int btn_up =0, btn_right =2, btn_down =4, btn_left =6;
	public static int btn_upperRight =1, btn_lowerRight =3, btn_lowerLeft =5, btn_upperLeft =7;

	public static int pollPeriod =1, queuePolPeriod =10;
	public static float deadLThumb =0.2f, deadRThumb =0.2f;
		
	private static Controller[] xboxCont; 
	private static Controller mainController;
	
	private static int activeControllers;
	
	public static void setup(int controllers){
		xboxCont =new Controller[controllers];
	}
	
	public static void update(){
		if(xboxCont ==null)
			return;
		for(Controller c: xboxCont)if(c !=null)
			c.update();
	}
	
	public static Controller getController(int i){
		return xboxCont[i];
	}
	
	public static Controller createController(int pollPeriod, int queuePolPeriod, float deadLThumb, float deadRThumb){
		for(int i =0; i <xboxCont.length; i++)if(xboxCont[i] ==null){
			Controller c =new Controller(i, pollPeriod, queuePolPeriod, deadLThumb, deadRThumb);
			xboxCont[i] =c;
			if(mainController ==null)
				mainController =c;
			activeControllers ++;
			return c;
		}
			
		return null;
	}
	
	public static Controller createController(){
		return createController(Xbox.pollPeriod, Xbox.queuePolPeriod, Xbox.deadLThumb, Xbox.deadRThumb);
	}
	
	public static Controller getMainController(){
		return mainController;
	}
	
	public static void removeController(Controller cont){
		for(int i =0; i <xboxCont.length; i ++)if(xboxCont[i] !=null && xboxCont[i] ==cont){
			xboxCont[i] =null;
			activeControllers --;
			return;
		}
	}
	
	public static void removeController(int cont){
		if(xboxCont[cont] !=null){
			xboxCont[cont] =null;
			activeControllers --;
		}
	}
	
	public static int getActiveControllers(){
		return activeControllers;
	}
	
	public static void dispose(){
		for(Controller c: xboxCont)if(c !=null){
			c.release();
			c.remove();
			c =null;
		}
		
	}
}

