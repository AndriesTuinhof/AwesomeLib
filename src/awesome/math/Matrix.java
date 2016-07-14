package awesome.math;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Matrix
{
	private FloatBuffer buffer;
	public float[] m, tmp;
	
	
	public Matrix()
	{
		buffer = BufferUtils.createFloatBuffer(16);
		m = new float[16];
		tmp = new float[16];
	}
	
	
	public FloatBuffer asBuffer()
	{
		buffer.clear();
		buffer.put(m);
		buffer.flip();
		return buffer;
	}
	
	public void putTransposed(FloatBuffer b)
	{
		b.put(m[0]).put(m[4]).put(m[8]).put(m[12]);
		b.put(m[1]).put(m[5]).put(m[9]).put(m[13]);
		b.put(m[2]).put(m[6]).put(m[10]).put(m[14]);
		b.put(m[3]).put(m[7]).put(m[11]).put(m[15]);
	}
	
	public void putTransposed(float[] f){
		f[0] =m[0];
		f[1] =m[4];
		f[2] =m[8];
		f[3] =m[12];
		f[4] =m[1];
		f[5] =m[5];
		f[6] =m[9];
		f[7] =m[13];
		f[8] =m[2];
		f[9] =m[6];
		f[10] =m[10];
		f[11] =m[14];
		f[12] =m[3];
		f[13] =m[7];
		f[14] =m[11];
		f[15] =m[15];
	}
	
	public void setIdentity()
	{
		m[0]=1; m[1]=0; m[2]=0; m[3]=0;
		m[4]=0; m[5]=1; m[6]=0; m[7]=0;
		m[8]=0; m[9]=0; m[10]=1; m[11]=0;
		m[12]=0; m[13]=0; m[14]=0; m[15]=1;
	}

	public void empty(){
		for(int i=0;i<16;i++) m[i] = 0;
	}

	private float[] tmpMult = new float[16];
	public void setMult(Matrix l, Matrix r)
	{
		multiply(l.m, r.m, m);
	}
	
	
	private void multiply(float[] l, float[] r, float[] o)
	{
		int i=0;
		for(int y=0;y<4;y++)for(int x=0;x<4;x++)
		{
			float v=0;
			v += l[y*4+0] * r[0*4+x];
			v += l[y*4+1] * r[1*4+x];
			v += l[y*4+2] * r[2*4+x];
			v += l[y*4+3] * r[3*4+x];
			tmpMult[i++] = v;
		}
		for(i=0;i<16;i++) o[i] = tmpMult[i];
	}
	
	
	public float[] multiplyVector(float x, float y, float z, float w, float[] o)
	{ // TODO: Should sort out data in and data out
		for(int i=0; i<4; i++) o[i] = x*m[i*4+0]+y*m[i*4+1]+z*m[i*4+2]+w*m[i*4+3];
		return o;
	}
	
	public float[] screenProjectVector(float x, float y, float z, float w, float[] o)
	{ // TODO: Should sort out data in and data out
		for(int i=0; i<4; i++) o[i] = x*m[i*4+0]+y*m[i*4+1]+z*m[i*4+2]+w*m[i*4+3];
		o[3] = Math.abs(o[3]);
		o[0]/=o[3]; 
		o[1]/=o[3];
		o[2]/=o[3];
		return o;
	}
	
	
	public void loadOrtho(float left, float right, float bottom, float top, float near, float far)
	{
		m[0] = 2f/(right-left);
		m[5] = 2f/(top-bottom);
		m[10] = -2f/(far-near);
		
		m[3] = -(right+left)/(right-left);
		m[7] = -(top+bottom)/(top-bottom);
		m[11] = -(far+near)/(far-near);
		m[15] = 1;
	}
	
	
	public void rotateX(float angle)
	{
		for(int i=0;i<16;i++) tmp[i] = 0;
		tmp[5] = (float)Math.cos(Math.toRadians(angle));
		tmp[6] = (float)-Math.sin(Math.toRadians(angle));
		tmp[9] = (float)Math.sin(Math.toRadians(angle));
		tmp[10] = (float)Math.cos(Math.toRadians(angle));
		tmp[0] = 1;
		tmp[15] = 1;
		multiply(m, tmp, m);
	}
	
	
	public void rotateY(float angle)
	{
		for(int i=0;i<16;i++) tmp[i] = 0;
		tmp[0] = (float)Math.cos(Math.toRadians(angle));
		tmp[2] = (float)-Math.sin(Math.toRadians(angle));
		tmp[8] = (float)Math.sin(Math.toRadians(angle));
		tmp[10] = (float)Math.cos(Math.toRadians(angle));
		tmp[5] = 1;
		tmp[15] = 1;
		multiply(m, tmp, m);
	}
	
	
	public void rotateZ(float angle)
	{
		for(int i=0;i<16;i++) tmp[i] = 0;
		tmp[0] = (float)Math.cos(Math.toRadians(angle));
		tmp[1] = (float)-Math.sin(Math.toRadians(angle));
		tmp[4] = (float)Math.sin(Math.toRadians(angle));
		tmp[5] = (float)Math.cos(Math.toRadians(angle));
		tmp[10] = 1;
		tmp[15] = 1;
		multiply(m, tmp, m);
	}
	
	public void rotate(float x, float y, float z){
		rotateX(x);
		rotateY(y);
		rotateZ(z);
	}
	
	public void translate(float x, float y, float z)
	{
		for(int i=0;i<16;i++) tmp[i] = 0;
		tmp[0] = 1;
		tmp[5] = 1;
		tmp[10] = 1;
		tmp[15] = 1;
		tmp[3] = x;
		tmp[7] = y;
		tmp[11] = z;
		multiply(m, tmp, m);
	}

	
	public void scale(float x, float y, float z)
	{
		for(int i=0;i<16;i++) tmp[i] = 0;
		tmp[0] = x;
		tmp[5] = y;
		tmp[10] = z;
		tmp[15] = 1.0f;
		multiply(m, tmp, m);
	}
	

	public void setPerspective(float fov, float ratio, float near, float far)
	{
		for(int i=0;i<16;i++) tmp[i] = 0;
		float mA = 1f / (float) Math.tan(Math.toRadians(fov/2f));
		float depth = far-near;
		tmp[0] = mA/ratio;
		tmp[5] = mA;
		tmp[10] = -(far+near)/depth;
		tmp[11] = (-2*far*near)/depth;
		tmp[14] = -1f;
		multiply(m, tmp, m);
	}
	
	
	private float[] tmpVerts = new float[4];
	private void checkPlanes(float x, float y, float z, float[] viewMatrix, boolean[] planes)
	{
		screenProjectVector(x, y, z, 1, tmpVerts);
		
		if(tmpVerts[0]>-1) planes[0] = true; // Left
		if(tmpVerts[0]<1) planes[1] = true; // Right
		if(tmpVerts[1]>-1) planes[2] = true; // Bottom
		if(tmpVerts[1]<1) planes[3] = true; // Top
		if(tmpVerts[2]>0) planes[4] = true; // Near
		if(tmpVerts[2]<1) planes[5] = true; // Far
	}

	
	private boolean[] tmpSides = new boolean[6];
	public boolean cubeWithinView(float x, float y, float z, float w, float d, float h)
	{
		// A
		for(int i=0; i<6; i++) tmpSides[i] = false;
		checkPlanes(x, y, z, m, tmpSides);
		checkPlanes(x+w, y, z, m, tmpSides);
		checkPlanes(x+w, y+d, z, m, tmpSides);
		checkPlanes(x, y+d, z, m, tmpSides);
		checkPlanes(x, y, z+h, m, tmpSides);
		checkPlanes(x+w, y, z+h, m, tmpSides);
		checkPlanes(x+w, y+d, z+h, m, tmpSides);
		checkPlanes(x, y+d, z+h, m, tmpSides);
		
		boolean bob = true;
		for(int i=0; i<6;i++) bob = bob&&tmpSides[i];
		return bob;
	}
	
	
	private float[] tmpV = new float[4];
	public boolean vertexWithinView(float x, float y, float z)
	{
		for(int i=0;i<4;i++) tmpV[i] = 0;
		screenProjectVector(x, y, z, 1, tmpV);
		if(tmpV[0]>1||tmpV[1]>1||tmpV[2]>1) return false;
		if(tmpV[0]<-1||tmpV[1]<-1||tmpV[2]<-1) return false;
		return true;
	}
	
	
	public boolean sphereWithinView(float x, float y, float z, float radius)
	{
		screenProjectVector(x, y, z, 1.0f, tmpV);
		float r = radius / tmpV[3]; // Perspective Divide
		if(tmpV[0]-r>1||tmpV[1]-r>1||tmpV[2]-r>1) return false;
		if(tmpV[0]+r<-1||tmpV[1]+r<-1||tmpV[2]+r<0) return false;
		return true;
	}

	
	public void set(Matrix mat)
	{
		for(int i=0;i<16;i++) m[i] = mat.m[i]; 
	}

	
	public void set(float[] fs)
	{
		for(int i=0;i<16;i++) m[i] = fs[i];
	}
	
	public Matrix inverse(){
	    Matrix matrix =new Matrix();

	    matrix.m[0] = m[5]  * m[10] * m[15] - 
	             m[5]  * m[11] * m[14] - 
	             m[9]  * m[6]  * m[15] + 
	             m[9]  * m[7]  * m[14] +
	             m[13] * m[6]  * m[11] - 
	             m[13] * m[7]  * m[10];

	    matrix.m[4] = -m[4]  * m[10] * m[15] + 
	              m[4]  * m[11] * m[14] + 
	              m[8]  * m[6]  * m[15] - 
	              m[8]  * m[7]  * m[14] - 
	              m[12] * m[6]  * m[11] + 
	              m[12] * m[7]  * m[10];

	    matrix.m[8] = m[4]  * m[9] * m[15] - 
	             m[4]  * m[11] * m[13] - 
	             m[8]  * m[5] * m[15] + 
	             m[8]  * m[7] * m[13] + 
	             m[12] * m[5] * m[11] - 
	             m[12] * m[7] * m[9];

	    matrix.m[12] = -m[4]  * m[9] * m[14] + 
	               m[4]  * m[10] * m[13] +
	               m[8]  * m[5] * m[14] - 
	               m[8]  * m[6] * m[13] - 
	               m[12] * m[5] * m[10] + 
	               m[12] * m[6] * m[9];

	    matrix.m[1] = -m[1]  * m[10] * m[15] + 
	              m[1]  * m[11] * m[14] + 
	              m[9]  * m[2] * m[15] - 
	              m[9]  * m[3] * m[14] - 
	              m[13] * m[2] * m[11] + 
	              m[13] * m[3] * m[10];

	    matrix.m[5] = m[0]  * m[10] * m[15] - 
	             m[0]  * m[11] * m[14] - 
	             m[8]  * m[2] * m[15] + 
	             m[8]  * m[3] * m[14] + 
	             m[12] * m[2] * m[11] - 
	             m[12] * m[3] * m[10];

	    matrix.m[9] = -m[0]  * m[9] * m[15] + 
	              m[0]  * m[11] * m[13] + 
	              m[8]  * m[1] * m[15] - 
	              m[8]  * m[3] * m[13] - 
	              m[12] * m[1] * m[11] + 
	              m[12] * m[3] * m[9];

	    matrix.m[13] = m[0]  * m[9] * m[14] - 
	              m[0]  * m[10] * m[13] - 
	              m[8]  * m[1] * m[14] + 
	              m[8]  * m[2] * m[13] + 
	              m[12] * m[1] * m[10] - 
	              m[12] * m[2] * m[9];

	    matrix.m[2] = m[1]  * m[6] * m[15] - 
	             m[1]  * m[7] * m[14] - 
	             m[5]  * m[2] * m[15] + 
	             m[5]  * m[3] * m[14] + 
	             m[13] * m[2] * m[7] - 
	             m[13] * m[3] * m[6];

	    matrix.m[6] = -m[0]  * m[6] * m[15] + 
	              m[0]  * m[7] * m[14] + 
	              m[4]  * m[2] * m[15] - 
	              m[4]  * m[3] * m[14] - 
	              m[12] * m[2] * m[7] + 
	              m[12] * m[3] * m[6];

	    matrix.m[10] = m[0]  * m[5] * m[15] - 
	              m[0]  * m[7] * m[13] - 
	              m[4]  * m[1] * m[15] + 
	              m[4]  * m[3] * m[13] + 
	              m[12] * m[1] * m[7] - 
	              m[12] * m[3] * m[5];

	    matrix.m[14] = -m[0]  * m[5] * m[14] + 
	               m[0]  * m[6] * m[13] + 
	               m[4]  * m[1] * m[14] - 
	               m[4]  * m[2] * m[13] - 
	               m[12] * m[1] * m[6] + 
	               m[12] * m[2] * m[5];

	    matrix.m[3] = -m[1] * m[6] * m[11] + 
	              m[1] * m[7] * m[10] + 
	              m[5] * m[2] * m[11] - 
	              m[5] * m[3] * m[10] - 
	              m[9] * m[2] * m[7] + 
	              m[9] * m[3] * m[6];

	    matrix.m[7] = m[0] * m[6] * m[11] - 
	             m[0] * m[7] * m[10] - 
	             m[4] * m[2] * m[11] + 
	             m[4] * m[3] * m[10] + 
	             m[8] * m[2] * m[7] - 
	             m[8] * m[3] * m[6];

	    matrix.m[11] = -m[0] * m[5] * m[11] + 
	               m[0] * m[7] * m[9] + 
	               m[4] * m[1] * m[11] - 
	               m[4] * m[3] * m[9] - 
	               m[8] * m[1] * m[7] + 
	               m[8] * m[3] * m[5];

	    matrix.m[15] = m[0] * m[5] * m[10] - 
	              m[0] * m[6] * m[9] - 
	              m[4] * m[1] * m[10] + 
	              m[4] * m[2] * m[9] + 
	              m[8] * m[1] * m[6] - 
	              m[8] * m[2] * m[5];

		float det;
	    det = m[0] * matrix.m[0] + m[1] * matrix.m[4] + m[2] * matrix.m[8] + m[3] * matrix.m[12];


	    det =1.0f / det;
	    for(int i =0; i <m.length; i++)
	        m[i] *=det;
	    return matrix;
	}
	
	@Override
	public String toString(){
		return m[0]+" "+m[1]+" "+m[2]+" "+m[3]+" "+m[4]+" "+m[5]+" "+m[6]+" "+m[7]+" "+m[8]+" "+m[9]+" "+m[10]+" "+m[11]+" "+m[12]+" "+m[13]+" "+m[14]+" "+m[15];
	}
}
