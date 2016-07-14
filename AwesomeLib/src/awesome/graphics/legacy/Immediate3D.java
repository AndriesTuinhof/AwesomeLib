package awesome.graphics.legacy;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import awesome.graphics.Shader;
import awesome.math.Matrix;

public class Immediate3D
{
	
	private static final int DRAWING_BUFFER_SIZE = 100*6*8;
	private static int glVertexArray, glBuffer;
	private static FloatBuffer buffer;
	private static Shader shader;
	private static int WHITE_TEXTURE;
	private static Matrix matrix = new Matrix();
	
	private static float red=1, green=1, blue=1, alpha=1;
	
	public static void create() throws Exception
	{
		buffer = BufferUtils.createFloatBuffer(DRAWING_BUFFER_SIZE);
		shader = new Shader(Immediate3D.class.getResourceAsStream("immediate.vert"), Immediate3D.class.getResourceAsStream("immediate.frag"));
		
		glBindVertexArray(glVertexArray=glGenVertexArrays());
		glBindBuffer(GL_ARRAY_BUFFER, glBuffer=glGenBuffers());
		glBufferData(GL_ARRAY_BUFFER, DRAWING_BUFFER_SIZE*4, GL_STREAM_DRAW);
		for(int i=0; i<3; i++) glEnableVertexAttribArray(i+1);
		int stride = 36;
		glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, 0); // VERTEX
		glVertexAttribPointer(2, 4, GL_FLOAT, false, stride, 12); // COLOUR
		glVertexAttribPointer(3, 2, GL_FLOAT, false, stride, 28); // TEX UV
		glBindVertexArray(0);
		
		
		
		
		WHITE_TEXTURE = glGenTextures();
		FloatBuffer buff = BufferUtils.createFloatBuffer(4);
		buff.put(1).put(1).put(1).put(1).flip();
		glBindTexture(GL_TEXTURE_2D, WHITE_TEXTURE);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 1, 1, 0, GL_RGBA, GL_FLOAT, buff);
	}
	
	public static void setMatrix(Matrix m)
	{
		matrix.set(m);
	}
	
	
	public static void destroy()
	{
		glDeleteVertexArrays(glVertexArray);
		glDeleteBuffers(glBuffer);
		glDeleteTextures(WHITE_TEXTURE);
	}
	
	
	public static void setBlankTexture() {
		glBindTexture(GL_TEXTURE_2D, WHITE_TEXTURE);
	}


	public static void setColor(float r, float g, float b, float a) {
		red = r;
		green = g;
		blue = b;
		alpha = a;
	}

	
	public static void setColor(float r, float g, float b) {
		setColor(r, g, b, 1);
	}
	
	
	public static void drawQuad(float x, float y, float w, float h, float z)
	{
		drawQuad(x, y, w, h, z, 1, 1);
	}
	
	public static void drawQuad(float x, float y, float w, float h, float z, float uscale, float vscale)
	{
		matrix.setIdentity();
		buffer.clear();
		vertex(x, y, z, 0, 0);
		vertex(x, y+h, z, 0, vscale);
		vertex(x+w, y+h, z, uscale, vscale);
		
		vertex(x, y, z, 0, 0);
		vertex(x+w, y+h, z, uscale, vscale);
		vertex(x+w, y, z, uscale, 0);
		buffer.flip();
		
		shader.bind();
		glBindVertexArray(glVertexArray);
		glBindBuffer(GL_ARRAY_BUFFER, glBuffer);
		glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
		glDrawArrays(GL_TRIANGLES, 0, 6);
		glBindVertexArray(0);
	}

	public static void drawLine(float x1, float y1, float z1, float x2, float y2, float z2) {
		drawLine(x1, y1, z1, x2, y2, z2, null);
	}
	
	public static void drawLine(float x1, float y1, float z1, float x2, float y2, float z2, Matrix m) {
		if(m==null) matrix.setIdentity();
		else matrix.set(m);
		buffer.clear();
		glLineWidth(3);
		setBlankTexture();
		vertex(x1, y1, z1, 0, 0);
		vertex(x2, y2, z2, 1, 1);
		buffer.flip();
		
		shader.bind();
		glBindVertexArray(glVertexArray);
		glBindBuffer(GL_ARRAY_BUFFER, glBuffer);
		glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
		glDrawArrays(GL_LINES, 0, 2);
		glBindVertexArray(0);
	}
	
	
	public static void drawCube(float x, float y, float z, float w, float d, float h, Matrix m) {
		if(m!=null) matrix.set(m);
		else matrix.setIdentity();
		
		buffer.clear();
		// BASE
		vertex(x, y, z, 0, 0);
		vertex(x, y+d, z, 1, 1);
		vertex(x+w, y+d, z, 1, 1);
		vertex(x, y, z, 0, 0);
		vertex(x+w, y+d, z, 1, 1);
		vertex(x+w, y, z, 1, 0);
		// TOP
		vertex(x, y, z+h, 0, 0);
		vertex(x+w, y+d, z+h, 1, 1);
		vertex(x, y+d, z+h, 1, 1);
		vertex(x, y, z+h, 0, 0);
		vertex(x+w, y, z+h, 1, 0);
		vertex(x+w, y+d, z+h, 1, 1);
		// FRONT
		vertex(x, y, z, 0, 0);
		vertex(x+w, y, z, 1, 1);
		vertex(x+w, y, z+h, 1, 1);
		vertex(x, y, z, 0, 0);
		vertex(x+w, y, z+h, 1, 0);
		vertex(x, y, z+h, 1, 1);
		// REAR
		vertex(x, y+d, z, 0, 0);
		vertex(x+w, y+d, z+h, 1, 1);
		vertex(x+w, y+d, z, 1, 1);
		vertex(x, y+d, z, 0, 0);
		vertex(x, y+d, z+h, 1, 1);
		vertex(x+w, y+d, z+h, 1, 0);
		// RIGHT
		vertex(x, y, z, 0, 0);
		vertex(x, y+d, z, 1, 1);
		vertex(x, y+d, z+h, 1, 1);
		vertex(x, y, z, 0, 0);
		vertex(x, y+d, z+h, 1, 0);
		vertex(x, y, z+h, 1, 1);
		// LEFT
		vertex(x+w, y, z, 0, 0);
		vertex(x+w, y+d, z+h, 1, 1);
		vertex(x+w, y+d, z, 1, 1);
		vertex(x+w, y, z, 0, 0);
		vertex(x+w, y, z+h, 1, 1);
		vertex(x+w, y+d, z+h, 1, 0);
		
		
		buffer.flip();
		
		shader.bind();
		glBindVertexArray(glVertexArray);
		glBindBuffer(GL_ARRAY_BUFFER, glBuffer);
		glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
		glDrawArrays(GL_TRIANGLES, 0, 6*6);
		glBindVertexArray(0);
	}
	
	private static float[] tmpVertex = new float[4];
	private static void vertex(float x, float y, float z, float u, float v) {
		matrix.multiplyVector(x, y, z, 1.0f, tmpVertex);
		buffer.put(tmpVertex[0]).put(tmpVertex[1]).put(tmpVertex[2]);
		buffer.put(red).put(green).put(blue).put(alpha);
		buffer.put(u).put(v);
	}


	public static void setTexture(int tex) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
	}


	public static void bindShader() {
		shader.bind();
	}
}
