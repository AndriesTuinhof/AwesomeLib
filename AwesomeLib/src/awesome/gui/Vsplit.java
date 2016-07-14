package awesome.gui;


public class Vsplit extends Component{
	
	private float x, y, width, height, prefTopHeight, prefBottomHeight;
	private Component topChild, bottomChild;	
	@Override
	public void logic(){
		if(topChild!=null)
			topChild.logic();
		if(bottomChild!=null)
			bottomChild.logic();
	}

	@Override
	public void draw(){
		if(bottomChild!=null)
			bottomChild.draw();
		if(topChild!=null)
			topChild.draw();
	}
	
	public void setTopHeight(float h){
		prefTopHeight =h;
		prefBottomHeight =0;
	}
	
	public void setBottomHeight(float h){
		prefBottomHeight =h;
		prefTopHeight =0;
	}
	
	public void setTopChild(Component c){
		topChild =c;
	}
	
	public void setBottomChild(Component c){
		bottomChild =c;
	}

	@Override
	public void update(float px, float py, float pw, float ph){
		x =px;
		y =py;
		width =pw;
		height =ph;
		
		float topHeight =height /2f;
		float bottomHeight =height /2f;
		
		if(prefTopHeight !=0){
			topHeight =prefTopHeight;
			bottomHeight =height -topHeight;
		}
		
		if(prefBottomHeight !=0){
			bottomHeight =prefBottomHeight;
			topHeight =height -bottomHeight;
		}

		if(bottomChild !=null)
			bottomChild.update(x, y, width, bottomHeight);
		if(topChild !=null)
			topChild.update(x, y+bottomHeight, width, topHeight);
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}
	
	@Override
	public float getWidth() {
		return width;
	}

	@Override
	public float getHeight() {
		return height;
	}



}
