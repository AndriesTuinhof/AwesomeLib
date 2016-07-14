package awesome.core;


import static org.lwjgl.opengl.GL11.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;

import static org.lwjgl.stb.STBTruetype.*;

public class fontBitmap{

	public int texture;
	private float fontSize =24;
	private STBTTPackedchar.Buffer chardata;
	
	public fontBitmap(Path path, float fontSize, int BITMAP_W, int BITMAP_H){
		try{
			this.fontSize =fontSize;
			texture =glGenTextures();
			chardata =STBTTPackedchar.malloc(6 *128);
			STBTTPackContext pc =STBTTPackContext.malloc();
			byte[] RAW_BUFFER =Files.readAllBytes(path);
			ByteBuffer FONT_BYTE_BUFFER =BufferUtils.createByteBuffer(RAW_BUFFER.length);
			FONT_BYTE_BUFFER.put(RAW_BUFFER);
			FONT_BYTE_BUFFER.flip();

			ByteBuffer bitmap =BufferUtils.createByteBuffer(BITMAP_W *BITMAP_H);

			stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1, null);
			int p = (0) * 128 + 32;
			chardata.limit(p + 95);
			chardata.position(p);
			stbtt_PackSetOversampling(pc, 1, 1);
			stbtt_PackFontRange(pc, FONT_BYTE_BUFFER, 0, fontSize, 32, chardata);

			p = (1) * 128 + 32;
			chardata.limit(p + 95);
			chardata.position(p);
			stbtt_PackSetOversampling(pc, 2, 2);
			stbtt_PackFontRange(pc, FONT_BYTE_BUFFER, 0, fontSize, 32, chardata);

			p = (2) * 128 + 32;
			chardata.limit(p + 95);
			chardata.position(p);
			stbtt_PackSetOversampling(pc, 3, 1);
			stbtt_PackFontRange(pc, FONT_BYTE_BUFFER, 0, fontSize, 32, chardata);
			
			chardata.clear();
			stbtt_PackEnd(pc);

			glBindTexture(GL_TEXTURE_2D, texture);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_W, BITMAP_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public STBTTPackedchar.Buffer getCharData(){
		return chardata;
	}
	
	public float getFontsize(){
		return fontSize;
	}
}