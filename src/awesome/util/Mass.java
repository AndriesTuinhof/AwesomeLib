package awesome.util;

import awesome.core.Timing;

public class Mass{
	
	public float x, y, z;
	public float xspeed, yspeed, zspeed;
	public float lastx, lasty, lastz;
	public float weight =1;

	public boolean moved =false;

	public void stepMomentum(){
		lastx =x;
		lasty =y;
		lastz =z;
		float a =1f /weight;
		x +=(xspeed *Timing.delta()) *a;
		y +=(yspeed *Timing.delta()) *a;
		z +=(zspeed *Timing.delta()) *a;
		moved =(Math.sqrt((x -lastx) *(x -lastx) +(y -lasty) *(y -lasty) +(z -lastz) *(z -lastz)) !=0);
	}		
	
	public void translate(float x, float y, float z){
		this.x =x;
		this.y =y;
		this.z =z;
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x =x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y =y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z =z;
	}

	public float getXspeed() {
		return xspeed;
	}

	public void setXspeed(float xspeed) {
		this.xspeed =xspeed;
	}

	public float getYspeed() {
		return yspeed;
	}

	public void setYspeed(float yspeed) {
		this.yspeed =yspeed;
	}

	public float getZspeed() {
		return zspeed;
	}

	public void setZspeed(float zspeed) {
		this.zspeed =zspeed;
	}

	public float getLastx() {
		return lastx;
	}

	public void setLastx(float lastx) {
		this.lastx =lastx;
	}

	public float getLasty() {
		return lasty;
	}

	public void setLasty(float lasty) {
		this.lasty =lasty;
	}

	public float getLastz() {
		return lastz;
	}

	public void setLastz(float lastz) {
		this.lastz =lastz;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight =weight;
	}

	public boolean isMoved() {
		return moved;
	}	

	public float distance(Mass m)	{
		return (float)Math.sqrt((x-m.x)*(x-m.x)+(y-m.y)*(y-m.y)+(z-m.z)*(z-m.z));		
	}
	
	public float distance(float x, float y, float z)	{
		return (float)Math.sqrt((this.x-x)*(this.x-x)+(this.y-y)*(this.y-y)+(this.z-z)*(this.z-z));
	}

	public float speed(){
		return (float)Math.hypot(xspeed, yspeed);		
	}
	
	@Override
	public String toString() {
		return "Mass [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
}