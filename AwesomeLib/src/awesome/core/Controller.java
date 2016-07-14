package awesome.core;

import ch.aplu.xboxcontroller.XboxController;
import ch.aplu.xboxcontroller.XboxControllerAdapter;

public class Controller extends XboxControllerAdapter{
	
	private boolean[] btns =new boolean[18];
	private boolean[] btnsPre =new boolean[18];
	private boolean[] btnsRaw =new boolean[18];
	private float rThumbDir, rThumbMag;
	private float lThumbDir, lThumbMag;
	private float lTrigger, rTrigger;
	private static int lastButtonPressed=-1;
	
	
	private boolean isConnected;
	private XboxController xc;
	private int controllerNumber;
	
	public Controller(int cont, int pollPeriod, int queuePolPeriod, float deadLThumb, float deadRThumb){
		xc =new XboxController(pollPeriod, queuePolPeriod);
		xc.setLeftThumbDeadZone(deadLThumb);
		xc.setRightThumbDeadZone(deadRThumb);
		xc.addXboxControllerListener(this);
		isConnected =xc.isConnected();
		controllerNumber =cont;
	}	

	public void remove(){
		Xbox.removeController(controllerNumber);
	}
	
	public void update(){
		for(int i =0; i <btns.length; i ++){
			btnsPre[i] =btns[i];
			btns[i] =btnsRaw[i];
		}		
	}
	
	public void cancelInput(){
		for(int i =0; i <btns.length; i++)
			cancelButton(i);
		this.lThumbDir =0;
		this.rThumbDir =0;
		this.lThumbMag =0;
		this.rThumbMag =0;
		this.lTrigger =0;
		this.rTrigger =0;
	}
	
	public void cancelABXY(){
		for(int i =Xbox.btn_A; i <Xbox.btn_X; i++)
			cancelButton(i);
	}
	public void cancelDpad(){
		for(int i =0; i <Xbox.btn_start; i++){
			btns[i] =false;
			btnsPre[i] =false;
		}
	}
	
	public void cancelTriggers(){
		this.lTrigger =0;
		this.rTrigger =0;		
	}
	
	public void cancelButton(int... key){
		for(int i =0; i <key.length; i++){
			btns[key[i]] =false;
			btnsPre[key[i]] =false;		
		}
	}
	
	public void cancelButtonExclude(int... exclude){
		boolean[] btnStored = new boolean[btns.length];
		boolean[] btnPreStored = new boolean[btns.length];
		for(int key: exclude) {
			btnStored[key] = btns[key];
			btnPreStored[key] = btnsPre[key];			
		}
		
		for(int i =0; i <btns.length; i++) {
			btns[i] =false;
			btnsPre[i] =false;		
		}
		
		for(int key: exclude) {
			btns[key] = btnStored[key];
			btnsPre[key] = btnPreStored[key];			
		}
		
		
	}
	
	public void release(){
		xc.release();
	}	
	public float rThumbDirection(){
		return this.rThumbDir;
	}	
	public float rThumbMagnitude(){
		return this.rThumbMag;
	}	
	public float lThumbDirection(){
		return this.lThumbDir;
	}	
	public float lThumbMagnitude(){
		return this.lThumbMag;
	}	
	public float rTrigger(){
		return this.rTrigger;
	}	
	public float lTrigger(){
		return this.lTrigger;
	}
	public boolean buttonPressed(int key){
		return btns[key] && !btnsPre[key];		
	}	
	public boolean buttonReleased(int key){
		return btns[key] && !btnsPre[key];
	}
	public boolean buttonDown(int key){
		return btns[key];
	}
	public boolean isConnected(){
		return isConnected;
	}
	
	public int fetchLastButtonPressed(){
		int i = lastButtonPressed;
		lastButtonPressed = -1;
		return i;
	}
	
	public void vibrate(float vibration, float vibration2) {
		xc.vibrate((int)(65535*vibration), (int)(65535*vibration2));
	}	
	
	@Override
	public void leftTrigger(double value) {
		lTrigger =(float) value;
	}
	@Override
	public void rightTrigger(double value) {
		rTrigger =(float) value;
	}
	@Override
	public void rightThumbDirection(double direction) {
		rThumbDir = (float) direction;
	}	
	public void rightThumbMagnitude(double magnitude) {
		rThumbMag = (float) magnitude;
	}	
	@Override
	public void leftThumbDirection(double direction) {
		lThumbDir = (float) direction;
	}	
	public void leftThumbMagnitude(double magnitude) {
		lThumbMag = (float) magnitude;
	}	
	@Override
	public void start(boolean pressed) {
		btnsRaw[Xbox.btn_start] =pressed;
		if(pressed) lastButtonPressed = Xbox.btn_start;
	}	
	@Override
	public void back(boolean pressed) {
		btnsRaw[Xbox.btn_back] =pressed;
		if(pressed) lastButtonPressed = Xbox.btn_back;
	}	
	@Override
	public void buttonA(boolean pressed) {
		btnsRaw[Xbox.btn_A] =pressed;
		if(pressed) lastButtonPressed = Xbox.btn_A;
	}
	@Override
	public void buttonB(boolean pressed) {
		btnsRaw[Xbox.btn_B] =pressed;
		if(pressed) lastButtonPressed = Xbox.btn_B;
	}
	@Override
	public void buttonX(boolean pressed) {
		btnsRaw[Xbox.btn_X] =pressed;
		if(pressed) lastButtonPressed = Xbox.btn_X;
	}
	@Override
	public void buttonY(boolean pressed) {
		btnsRaw[Xbox.btn_Y] =pressed;
		if(pressed) lastButtonPressed = Xbox.btn_Y;
	}
	@Override
	public void isConnected(boolean connected) {
		isConnected =connected;
	}
	@Override
	public void leftShoulder(boolean pressed) {
		btnsRaw[Xbox.btn_leftShoulder] =pressed;
		if(pressed) lastButtonPressed = Xbox.btn_leftShoulder;
	}
	@Override
	public void rightShoulder(boolean pressed) {
		btnsRaw[Xbox.btn_rightShoulder] =pressed;
		if(pressed) lastButtonPressed = Xbox.btn_rightShoulder;
	}
	@Override
	public void leftThumb(boolean pressed) {
		btnsRaw[Xbox.btn_leftThumb] =pressed;
		if(pressed) lastButtonPressed = Xbox.btn_leftThumb;
	}
	@Override
	public void rightThumb(boolean pressed) {
		btnsRaw[Xbox.btn_rightThumb] =pressed;
		if(pressed) lastButtonPressed = Xbox.btn_rightThumb;
	}
	@Override
	public void dpad(int direction, boolean pressed) {
		btnsRaw[direction] =pressed;
	}
}
