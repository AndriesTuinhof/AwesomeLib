package awesome.gui;

import java.util.ArrayList;

public class Tree<T> extends TreeItem<T>{
	
	public ArrayList<TreeItem<T>> objects;
	private boolean show;
	
	public Tree(){
		objects =new ArrayList<TreeItem<T>>();
	}
	
	public boolean isShowing(){
		return show;
	}
	
	public void setShow(boolean show){
		this.show =show;
	}
}
