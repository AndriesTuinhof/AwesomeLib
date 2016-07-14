package awesome.core;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.file.Path;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBTTAlignedQuad;

import static org.lwjgl.stb.STBTruetype.*;

import awesome.graphics.Shader;
import awesome.gui.Color;

public class Painter {

	private static Shader DRAWING_SHADER;
	private static final int DRAWING_BUFFER_SIZE = 100*6*8*2;
	private static int glVertexArray, glBuffer;
	private static FloatBuffer buffer;
	public static int fontTexture, WHITE_TEXTURE;
	
	private static float red, green, blue, alpha;
	private static int BITMAP_W =512, BITMAP_H =512;
	
	private static HashMap<String, fontBitmap> fonts =new HashMap<String, fontBitmap>();
	private static fontBitmap DEFAULT_FONT;
	private static float DEFAULT_FONT_SIZE =24f;
	
	protected static void create() throws Exception{
		DRAWING_SHADER =new Shader(Painter.class.getResourceAsStream("assets/painter.vert"), Painter.class.getResourceAsStream("assets/painter.frag"));
		
		fontTexture =ResourceLoader.getTexture("font.png", false);	//TODO: we should use stb to load font, not bitmaps

		buffer =BufferUtils.createFloatBuffer(DRAWING_BUFFER_SIZE);
		glBindVertexArray(glVertexArray =glGenVertexArrays());
		glBindBuffer(GL_ARRAY_BUFFER, glBuffer =glGenBuffers());
		glBufferData(GL_ARRAY_BUFFER, DRAWING_BUFFER_SIZE *4, GL_STREAM_DRAW);
		for(int i =0; i <3; i ++) glEnableVertexAttribArray(i +1);
		int stride =32;
		glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, 0); // VERTEX
		glVertexAttribPointer(2, 4, GL_FLOAT, false, stride, 8); // COLOUR
		glVertexAttribPointer(3, 2, GL_FLOAT, false, stride, 24); // TEX UV
		glBindVertexArray(0);
		
		
		WHITE_TEXTURE =glGenTextures();
		FloatBuffer buff =BufferUtils.createFloatBuffer(4);
		buff.put(1).put(1).put(1).put(1).flip();
		glBindTexture(GL_TEXTURE_2D, WHITE_TEXTURE);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 1, 1, 0, GL_RGBA, GL_FLOAT, buff);
		glBindTexture(GL_TEXTURE_2D, 0);	
	}	
			
	protected static void destroy(){
		glDeleteVertexArrays(glVertexArray);
		glDeleteBuffers(glBuffer);
		glDeleteTextures(fontTexture);
		glDeleteTextures(WHITE_TEXTURE);
	}
		
	public static void setBlankTexture(){
		glBindTexture(GL_TEXTURE_2D, WHITE_TEXTURE);
	}

	private static final float[] xb =new float[1];
	private static final float[] yb =new float[1];
	private static final STBTTAlignedQuad q =STBTTAlignedQuad.malloc();
	
	public static void addFont(String fontName, Path path, float fontSize, int w, int h){
		if(path ==null)
			return;
		
		fonts.put(fontName, new fontBitmap(path, fontSize, w, h));
	}

	public static float getDefaultFontSize(){
		return DEFAULT_FONT_SIZE;
	}
	
	public static void setDefaultFont(Path path){
		DEFAULT_FONT =new fontBitmap(path, DEFAULT_FONT_SIZE, BITMAP_W, BITMAP_H);
	}
	
	public static void setDefaultFont(fontBitmap _default){
		DEFAULT_FONT =_default;
	}

	public static void text(String s, float xpos, float ypos, float fontScale){
		if(DEFAULT_FONT ==null)
			return;
		
		text(s, xpos, ypos, fontScale, DEFAULT_FONT);
	}
	
	public static void text(String s, float xpos, float ypos, float fontScale, String font){
		text(s, xpos, ypos, fontScale, fonts.getOrDefault(font, DEFAULT_FONT));
	}
	
	public static void text(String s, float xpos, float ypos, float fontScale, fontBitmap font){
		fontBitmap b =font;
		if(s ==null) s ="";
		if(b ==null) b =DEFAULT_FONT;
			
		char[] characters =s.toCharArray();
		int nVertices =0;
		buffer.clear();
		xb[0] =xpos;
		yb[0] =ypos;
		
		float cx, cy, cw, ch;
		float cs, ct, csw, cth;
		for(char c: characters){	
			cx =q.x0();
			cy =q.y0();
			cw =q.x1() -q.x0();
			ch =q.y1() -q.y0();
			
			cs =q.s0();
			ct =q.t0();
			csw =q.s1() -q.s0();
			cth =q.t1() -q.t0();
			
			stbtt_GetPackedQuad(font.getCharData(), BITMAP_W, BITMAP_H, c, xb, yb, q, true);
			vertex(cx, 		cy, 	cs, 		ct);// Bottom Left			
			vertex(cx +cw, 	cy, 	cs +csw, 	ct);// Bottom Right			
			vertex(cx, 		cy +ch, cs, 		ct +cth);// Top Left			
			
			vertex(cx, 		cy +ch, cs, 		ct +cth);// Top Left				
			vertex(cx +cw, 	cy, 	cs +csw, 	ct);// Bottom Right			
			vertex(cx +cw, 	cy +ch, cs +csw, 	ct +cth);// Top Left	


			nVertices +=6;
		}
		buffer.flip();
		
		DRAWING_SHADER.bind();
		glBindTexture(GL_TEXTURE_2D, font.texture);
		glBindVertexArray(glVertexArray);
		glBindBuffer(GL_ARRAY_BUFFER, glBuffer);
		glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
		glDrawArrays(GL_TRIANGLES, 0, nVertices);
		glBindTexture(GL_TEXTURE_2D, WHITE_TEXTURE);
		glBindVertexArray(0);
	}
	
	public static void setWhite(){
		red =1;
		green =1;
		blue =1;
		alpha =1;
	}
	
	public static void setWhite(float a){
		red =1;
		green =1;
		blue =1;
		alpha =a;
	}
	
	public static void setColorInt(int r, int g, int b) {
		setColor(r /255f, g /255f, b /255f, 1);
	}	
	
	public static void setColor(Color c){
		setColorInt(c.r, c.g, c.b, c.a);
	}
	
	public static void setColorInt(int r, int g, int b, int a) {
		red =r /255f;
		green =g /255f;
		blue =b /255f;
		alpha =a /255f;
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
	
	public static void drawQuad(float x, float y, float w, float h) {
		drawQuad(x, y, w, h, true);
	}
	
	public static void drawQuadNoShader(float x, float y, float w, float h) {
		drawQuad(x, y, w, h, false);
	}
	
	public static void drawQuadFlipped(float x, float y, float w, float h){
		buffer.clear();
		vertex(x, y, 1, 1);
		vertex(x+w, y, 0, 1);
		vertex(x+w, y+h, 0, 0);
		
		vertex(x, y, 1, 1);
		vertex(x+w, y+h, 0, 0);
		vertex(x, y+h, 1, 0);
		buffer.flip();
		
		glBindVertexArray(glVertexArray);
		glBindBuffer(GL_ARRAY_BUFFER, glBuffer);
		glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
		glDrawArrays(GL_TRIANGLES, 0, 6);
		glBindVertexArray(0);	
	}
	
	private static void drawQuad(float x, float y, float w, float h, boolean usesShader) {
		buffer.clear();
		vertex(x, y, 0, 0);
		vertex(x+w, y, 1, 0);
		vertex(x+w, y+h, 1, 1);
		
		vertex(x, y, 0, 0);
		vertex(x+w, y+h, 1, 1);
		vertex(x, y+h, 0, 1);
		buffer.flip();
		
		if(usesShader)DRAWING_SHADER.bind();
		glBindVertexArray(glVertexArray);
		glBindBuffer(GL_ARRAY_BUFFER, glBuffer);
		glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
		glDrawArrays(GL_TRIANGLES, 0, 6);
		glBindVertexArray(0);
	}
	
	public static void vertex(float x, float y, float u, float v) {
		float width = PandaDisplay.getWidth();
		float height = PandaDisplay.getHeight();
		buffer.put(x/width*2-1).put(y/height*2-1);
		buffer.put(red).put(green).put(blue).put(alpha);
		buffer.put(u).put(v);
	}
	
	public static void drawStringCenter(String s, float xpos, float ypos, float fontScale) {
		xpos -= s.length()*fontScale*0.5f*0.7;
		ypos -= fontScale/2f;
		drawString(s, xpos, ypos, fontScale);
	}
	
	public static int drawString(String s, float xpos, float ypos, float fontScale) {
		// Character width 45
		// Character height 64
		if(s ==null) s ="";
		float scale =fontScale /45f;
		char[] characters =s.toCharArray();
		int nVertices =0;
		buffer.clear();
		float x =xpos, y =ypos;
		
		for(char c: characters){
			if(c =='\n'){ // Newline
				x =xpos;
				y -=fontScale *1.2f;
				continue; // Next letter
			}			
			int index =c -33;
			if(c>'\\')
				index --;
			if(c>'`')
				index --;
			int row =index /11, column =index -row *11;
			float u =(float)(column *45) /512f, v =(float)(row *64) /512f;
			
			float uW =45f /512f, vW =64f /512f; // Widths of a character in UV space
			float cW =45 *scale, cH =64 *scale; // Character width on screen
			if(c ==' '){
				x +=cW *0.7f; // Make Gap
				continue; // Don't draw space
			}			
			vertex(x, y, u, v+vW);// Bottom Left			
			vertex(x+cW, y, u+uW, v+vW);// Bottom Right			
			vertex(x, y+cH, u, v);// Top Left			
			
			vertex(x, y+cH, u, v);// Top Left			
			vertex(x+cW, y, u+uW, v+vW);// Bottom Right			
			vertex(x+cW, y+cH, u+uW, v);// Top Right

			x +=cW *0.7f; // Move character offset
			nVertices +=6;
		}
		buffer.flip();
		
		DRAWING_SHADER.bind();
		glBindTexture(GL_TEXTURE_2D, fontTexture);
		glBindVertexArray(glVertexArray);
		glBindBuffer(GL_ARRAY_BUFFER, glBuffer);
		glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
		glDrawArrays(GL_TRIANGLES, 0, nVertices);
		glBindTexture(GL_TEXTURE_2D, WHITE_TEXTURE);
		glBindVertexArray(0);
		return buffer.limit();
	}
	
	public static void setTexture(int tex) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
	}
	
	private static int vertices =0;
	public static void beginDrawing(){
		buffer.clear();
		vertices =0;
		
		DRAWING_SHADER.bind();		
	}
	
	public static void setVertex(float x, float y, float u, float v){
		vertex(x, y, u, v);
		vertices++;
	}
	
	public static void stopDrawing(int gl){
		buffer.flip();

		glBindVertexArray(glVertexArray);
		glBindBuffer(GL_ARRAY_BUFFER, glBuffer);
		glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
		glDrawArrays(gl, 0, vertices);
		glBindVertexArray(0);
	}


}
