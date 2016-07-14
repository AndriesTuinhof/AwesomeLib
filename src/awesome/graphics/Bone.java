package awesome.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.io.BufferedReader;

import awesome.math.Matrix;

public class Bone
{
	public String bone_id;
	public Bone parent;
	public Bone[] children;
	
	public Matrix localMatrix = new Matrix();
	public Matrix worldMatrix = new Matrix();
	
	public float[] animatedMatrices;
	
	
	
	public Bone(BufferedReader reader, Bone p) throws Exception
	{
		bone_id = reader.readLine().trim().split("\"")[1];
		parent = p;
		animatedMatrices = Collada.readFloats(reader.readLine());
		int joints = Integer.parseInt(reader.readLine().trim().split(" ")[1]);
		children = new Bone[joints];
		for(int i=0; i<joints; i++) children[i] = new Bone(reader, this);
	}
	
	public void updatePosition(float t1, float t2, float ratio)
	{
		for(int i=0;i<16;i++) localMatrix.m[i] = 0; // Clear matrix
		addPosition(t1, 1-ratio);
		addPosition(t2, ratio);
		
		
		if(parent!=null) worldMatrix.setMult(parent.worldMatrix, localMatrix);
		else worldMatrix.set(localMatrix);
		for(Bone b: children) b.updatePosition(t1, t2, ratio);
		
	}
	
	private void addPosition(float t, float amount)
	{
		int m0 = (int)Math.floor(t), m1 = (int)Math.ceil(t);
		if(m1*16>=animatedMatrices.length) m1 = 0;
		float f = t-m0;
		
		// Add to local matrix weighting on amount
		for(int i=0;i<16;i++)
		{
			float cellValue = animatedMatrices[16*m0+i]*(1-f) + animatedMatrices[16*m1+i]*f; 
			localMatrix.m[i] += cellValue*amount;
		}
	}
	
	
	public void draw()
	{
		for(Bone b: children) b.draw();
		if(parent==null) return;
		glBegin(GL_LINES);
		glColor3f(1,0,0);
		glVertex3f(worldMatrix.m[3], worldMatrix.m[7], worldMatrix.m[11]);
		glColor3f(0,0,1);
		glVertex3f(parent.worldMatrix.m[3], parent.worldMatrix.m[7], parent.worldMatrix.m[11]);
		glEnd();
	}
}
