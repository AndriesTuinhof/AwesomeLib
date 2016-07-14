package awesome.gui;

public class Hsplit extends Component{
	
	private float x, y, width, height, prefLeftWidth, prefRightWidth;
	private Component leftChild, rightChild;

	@Override
	public void logic(){
		if(leftChild !=null)
			leftChild.logic();
		if(rightChild !=null)
			rightChild.logic();
	}
	
	public void draw() {
		if(leftChild !=null)
			leftChild.draw();
		if(rightChild !=null)
			rightChild.draw();
	}
	
	public void setLeftWidth(float w){
		prefLeftWidth =w;
		prefRightWidth =0;
	}
	
	public void setRightWidth(float w){
		prefRightWidth =w;
		prefLeftWidth =0;
	}
	
	public void setLeftChild(Component c){
		leftChild =c;
	}
	
	public void setRightChild(Component c){
		rightChild =c;
	}

	@Override
	public void update(float x, float y, float w, float h){
		this.x =x;
		this.y =y;
		this.width =w;
		this.height =h;
		
		float leftWidth =width /2f;
		float rightWidth =width /2f;
		
		if(prefLeftWidth !=0){
			leftWidth =prefLeftWidth;
			rightWidth =width-leftWidth;
		}
		
		if(prefRightWidth !=0){
			rightWidth =prefRightWidth;
			leftWidth =width-rightWidth;
		}
		
		if(prefLeftWidth ==0 && prefRightWidth ==0){
			prefRightWidth =width /2f;
			rightWidth =prefRightWidth;
			leftWidth =width-rightWidth;
		}
		
		if(leftChild !=null)
			leftChild.update(x, y, leftWidth, height);
		if(rightChild !=null)
			rightChild.update(x+ leftWidth, y, rightWidth, height);
	}

	@Override
	public float getWidth(){
		return width;
	}

	@Override
	public float getHeight(){
		return height;
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
