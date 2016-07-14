package awesome.graphics;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Wavefront
{

	// Data TODO: Private
	public List<Float> positions;
	private List<Float> normals;
	private List<Float> uvcoords;
	public List<int[]> faces;
	private float[] tangents;
	private int glBuffer, glArrayObject;
	
	// Loader
	public Wavefront(InputStream in) throws Exception
	{
		// Reading
		InputStreamReader isr = new InputStreamReader(in);
		BufferedReader reader = new BufferedReader(isr);
		String inputLine;
		
		// Storage
		positions = new ArrayList<Float>(0);
		normals = new ArrayList<Float>(0);
		uvcoords = new ArrayList<Float>(0);
		faces = new ArrayList<int[]>(0);
		
		// Extraction
		while( (inputLine=reader.readLine()) !=null)
		{
			// String cleaning
			inputLine = inputLine.replaceAll("  ", " ");
			inputLine = inputLine.trim().toLowerCase();

			// Parsing
			if(inputLine.startsWith("v "))
				parseFloats(inputLine, positions, 3);
			if(inputLine.startsWith("vn "))
				parseFloats(inputLine, normals, 3);
			if(inputLine.startsWith("vt "))
				parseFloats(inputLine, uvcoords, 2);
			if(inputLine.startsWith("f "))
				parseFace(inputLine, faces);
		}
		
		// Render
		generateTangents();
		measure();
		glBuffer = GL15.glGenBuffers();
		glArrayObject = GL30.glGenVertexArrays();
		render();
	}
	
	private void generateTangents()
	{
		tangents = new float[normals.size()/3 * 4];
		float[] biTangents = new float[tangents.length];
		Vector3f v1Pos=new Vector3f(), v2Pos=new Vector3f(), v3Pos=new Vector3f();
		Vector2f v1Uv=new Vector2f(), v2Uv=new Vector2f(), v3Uv=new Vector2f();
		for(int f=0;f<faces.size();f++)
		{
			int[] face = faces.get(f);
			// Position Indices
			int v1_p_i = face[0*3+0]*3, v2_p_i = face[1*3+0]*3, v3_p_i = face[2*3+0]*3;
			// UV Coordinates Indices
			int v1_uv_index = face[0*3+1]*2, v2_uv_index = face[1*3+1]*2, v3_uv_index = face[2*3+1]*2;
			// Tangent Indices
			int v1_t_i = face[0*3+2]*4, v2_t_i = face[1*3+2]*4, v3_t_i = face[2*3+2]*4;
			// Position Data
			v1Pos.set(positions.get(v1_p_i+0), positions.get(v1_p_i+1), positions.get(v1_p_i+2));
			v2Pos.set(positions.get(v2_p_i+0), positions.get(v2_p_i+1), positions.get(v2_p_i+2));
			v3Pos.set(positions.get(v3_p_i+0), positions.get(v3_p_i+1), positions.get(v3_p_i+2));
			// UV Data
			v1Uv.set(uvcoords.get(v1_uv_index+0), uvcoords.get(v1_uv_index+1));
			v2Uv.set(uvcoords.get(v2_uv_index+0), uvcoords.get(v2_uv_index+1));
			v3Uv.set(uvcoords.get(v3_uv_index+0), uvcoords.get(v3_uv_index+1));
			// Differences
			float x1 = v2Pos.x-v1Pos.x, x2 = v3Pos.x-v1Pos.x;
			float y1 = v2Pos.y-v1Pos.y, y2 = v3Pos.y-v1Pos.y;
			float z1 = v2Pos.z-v1Pos.z, z2 = v3Pos.z-v1Pos.z;
			float s1 = v2Uv.x-v1Uv.x, s2 = v3Uv.x-v1Uv.x;
			float t1 = v2Uv.y-v1Uv.y, t2 = v3Uv.y-v1Uv.y;
			// Magic
			float r = 1.0f / (s1*t2 - t1*s2);
			// S direction in (world space I think)
			float sdir_x = (t2 * x1 - t1 * x2) * r;
			float sdir_y = (t2 * y1 - t1 * y2) * r;
			float sdir_z = (t2 * z1 - t1 * z2) * r;
			// T direction in (world space I think)
			float tdir_x = (s1 * x2 - s2 * x1) * r;
			float tdir_y = (s1 * y2 - s2 * y1) * r;
			float tdir_z = (s1 * z2 - s2 * z1) * r;
			// accumulate
			tangents[v1_t_i+0] += sdir_x;
			tangents[v1_t_i+1] += sdir_y;
			tangents[v1_t_i+2] += sdir_z;
			
			tangents[v2_t_i+0] += sdir_x;
			tangents[v2_t_i+1] += sdir_y;
			tangents[v2_t_i+2] += sdir_z;
			
			tangents[v3_t_i+0] += sdir_x;
			tangents[v3_t_i+1] += sdir_y;
			tangents[v3_t_i+2] += sdir_z ;
			// accumulate
			biTangents[v1_t_i+0] += tdir_x;
			biTangents[v1_t_i+1] += tdir_y;
			biTangents[v1_t_i+2] += tdir_z;
			
			biTangents[v2_t_i+0] += tdir_x;
			biTangents[v2_t_i+1] += tdir_y;
			biTangents[v2_t_i+2] += tdir_z;
			
			biTangents[v3_t_i+0] += tdir_x;
			biTangents[v3_t_i+1] += tdir_y;
			biTangents[v3_t_i+2] += tdir_z;
		}
		
		Vector3f tan = new Vector3f();
		Vector3f nor = new Vector3f();
		Vector3f btan = new Vector3f();
		Vector3f genBtan = new Vector3f();
		for(int n=0;n<normals.size()/3;n++)
		{
			// FIX TANGENTS
			int norIndex = n*3;
			int tanIndex = n*4;
			nor.set(normals.get(norIndex),normals.get(norIndex+1),normals.get(norIndex+2));
			tan.set(tangents[tanIndex], tangents[tanIndex+1], tangents[tanIndex+2]);
			
			if(tan.length()==0) tan.set(nor.z, nor.x, nor.y); // Fake bad / mirrored tangents
			float dot = Vector3f.dot(nor, tan);
			tan.x = tan.x - nor.x*dot;
			tan.y = tan.y - nor.y*dot;
			tan.z = tan.z - nor.z*dot;
			tan.normalise();
			
			// CHECK HANDEDNESS
			btan.set(biTangents[tanIndex], biTangents[tanIndex+1], biTangents[tanIndex+2]);
			Vector3f.cross(nor, tan, genBtan);
			float w = (Vector3f.dot(genBtan, btan)<0.0f)? -1.0f : 1.0f;
			if(btan.length()>0) btan.normalise();
			
			// Write Data
			tangents[tanIndex+0] = tan.x;
			tangents[tanIndex+1] = tan.y;
			tangents[tanIndex+2] = tan.z;
			tangents[tanIndex+3] = w;
			
			biTangents[tanIndex+0] = btan.x;
			biTangents[tanIndex+1] = btan.y;
			biTangents[tanIndex+2] = btan.z;
		}
	}
	
	
	// Reads floats from a line
	private static void parseFloats(String s, List<Float> l, int n)
	{
		String[] args = s.split(" ");
		for(int i=0; i<n; i++)
			l.add(Float.parseFloat(args[i+1]));
	}
	
	
	// Face
	private static void parseFace(String line, List<int[]> list)
	{
		String[] vertices = line.split(" ");
		int vertexCount = vertices.length-1;
		if(vertexCount!=3) throw new RuntimeException("Obj file contains non-triangles ["+vertexCount+"]");
		int[] indices = new int[vertexCount*3];
		for(int i=0; i<vertexCount; i++)
		{
			String[] refs = vertices[i+1].split("/");
			indices[i*3+0] = Integer.parseInt(refs[0])-1; // Position
			indices[i*3+1] = Integer.parseInt(refs[1])-1; // UV
			indices[i*3+2] = Integer.parseInt(refs[2])-1; // Normal
		}
		list.add(indices);
	}
	
	
	// Render
	private void render()
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(faces.size()*3*12);
		for(int[] face: faces)
		{
			for(int v=0; v<3; v++)
			{
				int posIndex = face[v*3+0]*3;
				int uvIndex = face[v*3+1]*2;
				int norIndex = face[v*3+2]*3;
				int tanIndex = face[v*3+2]*4;
				float x,y,z;
				
				x = positions.get(posIndex);
				y = positions.get(posIndex+1);
				z = positions.get(posIndex+2);
				buffer.put(x).put(y).put(z);
				
				x = normals.get(norIndex);
				y = normals.get(norIndex+1);
				z = normals.get(norIndex+2);
				buffer.put(x).put(y).put(z);

				x = uvcoords.get(uvIndex);
				y = uvcoords.get(uvIndex+1);
				buffer.put(x).put(1.0f-y);
				
				buffer.put(tangents[tanIndex+0])
				.put(tangents[tanIndex+1])
				.put(tangents[tanIndex+2])
				.put(tangents[tanIndex+3]);
			}
		}
		
		buffer.flip();
		GL30.glBindVertexArray(glArrayObject);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, glBuffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glEnableVertexAttribArray(4);
		GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, 48, 0);
		GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 48, 12);
		GL20.glVertexAttribPointer(3, 2, GL11.GL_FLOAT, false, 48, 24);
		GL20.glVertexAttribPointer(4, 4, GL11.GL_FLOAT, false, 48, 32);
		GL30.glBindVertexArray(0);
	}
	
	
	// Draw
	public void draw()
	{
		GL30.glBindVertexArray(glArrayObject);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, faces.size()*3);
		GL30.glBindVertexArray(0);
	}
	
	public void draw(int instances)
	{
		GL30.glBindVertexArray(glArrayObject);
		GL31.glDrawArraysInstanced(GL11.GL_TRIANGLES, 0, faces.size()*3, instances);
		GL30.glBindVertexArray(0);
	}
	
	// Free
	public void destroy()
	{
		GL30.glDeleteVertexArrays(glArrayObject);
		GL15.glDeleteBuffers(glBuffer);
	}
	
	
	// Measure the size
	public float size;
	public float maxX, maxY, maxZ, minX, minY, minZ;
	public void measure()
	{
		float minx=9999, miny=9999, minz=9999;
		float maxx=-9999, maxy=-9999, maxz=-9999;
		for(int i=0; i<positions.size()/3; i++)
		{
			int idx = i*3;
			float x = positions.get(idx+0);
			float y = positions.get(idx+1);
			float z = positions.get(idx+2);
			minx = Math.min(x, minx);
			maxx = Math.max(x, maxx);
			miny = Math.min(y, miny);
			maxy = Math.max(y, maxy);
			minz = Math.min(z, minz);
			maxz = Math.max(z, maxz);
		}
		this.maxX =maxx;
		this.maxY =maxy;
		this.maxZ =maxz;
		this.minX =minx;
		this.minY =miny;
		this.minZ =minz;
		float xlen = maxx-minx;
		float ylen = maxy-miny;
		float zlen = maxz-minz;
		size = (float)Math.sqrt(xlen*xlen+ylen*ylen+zlen*zlen);
	}
	
	// End of class
}
