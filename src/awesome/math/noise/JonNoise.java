package awesome.math.noise;

import awesome.util.FastMath;


public class JonNoise
{
	private static int seed = 0;
	private static float stepScalar = 0.5f;
	private static float scalarScalar = 0.5f;
	
	public static void setSeed(int i)
	{
		seed = i;
	}
	
	public static void setStepScalar(float f)
	{
		stepScalar = f;
	}
	
	public static void setScalarScalar(float f)
	{
		scalarScalar = f;
	}
	
	public static float noise(float x, float y){
		float ans=0;
		float step = 2048;
		float scalar = 1;
		float max = 0;
		
		scalarScalar = 0.45f + go(-x,-y,2048)*0.05f;
		
		for(float i=0; i<12; i++)
		{
			max+=scalar;
			ans += go(x,y, step)*scalar;
			step *= stepScalar;
			scalar *= scalarScalar;
		}
		return ans/max;
	}
	
	private static float go(float x, float y, float step)
	{
		float ax = (FastMath.floor(x/step))*step;
		float ay = (FastMath.floor(y/step))*step;
		float fx = (x%step)/step;
		float fy = (y%step)/step;
		
		float h1 = rand(ax, ay);
		float h2 = rand(ax+step, ay);
		float h3 = rand(ax, ay+step);
		float h4 = rand(ax+step, ay+step);
		
		float h5 = blend(h1, h2, fx);
		float h6 = blend(h3, h4, fx);
		
		float h7 = blend(h5, h6, fy);
		
		return h7;
	}
	
	private static float blend(float a, float b, float c)
	{
		if(c<0) c = 1+c;
		float f = c*3.141592f;
		c = (1-(float)Math.cos(f)) * 0.5f;
		
		return b*c + a*(1.0f-c);
	}
	
	private static float rand(float a, float b)
	{
		int x = (int) a;
		int y = (int) b;
		int n = x + y * 57 + seed;
		n = (n<<13) ^ n;
		
		float f = (float) ( 1.0 - ( (n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0);  
		return f;
	}
}