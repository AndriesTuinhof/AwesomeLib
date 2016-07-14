package awesome.gui;


public abstract class GamePanel extends Component implements Renderer3D{
	
	public float x, y, width, height;
	
	public GamePanel(){
	}

	@Override
	public void update(float x, float y, float w, float h){
		this.x =x;
		this.y =y;
		this.width =w;
		this.height =h;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}

	@Override
	public float getWidth(){
		return width;
	}

	@Override
	public float getHeight(){
		return height;
	}

}
