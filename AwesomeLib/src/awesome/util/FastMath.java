package awesome.util;

public class FastMath {
	public static float sin(float angle) {
		return (float)Math.sin(Math.toRadians(angle));
	}
	
	
	public static float cos(float angle) {
		return (float)Math.cos(Math.toRadians(angle));
	}
	
	
	public static float tan(float angle) {
		return (float)Math.tan(Math.toRadians(angle));
	}
	
	
	public static float atan(float t) {
		return (float)Math.toDegrees(Math.atan(t));
	}


	public static float hypot(float a, float b) {
		return (float)Math.hypot(a, b);
	}
	
	public static float hypot(float a, float b, float c)
	{
		return hypot(hypot(a,b),c);
	}


	public static float floor(float f) {
		return (float) Math.floor(f);
	}
	
	public static float round(float f) {
		return (float) Math.round(f);
	}


	public static float random(float f) {
		return (float)Math.random()*f;
	}
	
	
}
