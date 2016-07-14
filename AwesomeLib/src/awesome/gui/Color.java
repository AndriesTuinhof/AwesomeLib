package awesome.gui;

public class Color {

	public int r, g, b, a;

	public Color(){
		
	}
	
	public Color(int r, int g, int b){
		this.r =r;
		this.g=g;
		this.b =b;
		a =255;
	}
	
	public Color(int r, int g, int b, int a){
		this.r =r;
		this.g=g;
		this.b =b;
		this.a =a;
	}
	
	public void set(int r, int g, int b){
		this.r =r;
		this.g=g;
		this.b =b;
	}
	
	public void set(int r, int g, int b, int a){
		this.r =r;
		this.g=g;
		this.b =b;
		this.a =a;		
	}
}
