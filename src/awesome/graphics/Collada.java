package awesome.graphics;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import awesome.math.Matrix;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;

public class Collada
{
	public static float[] readFloats(String s)
	{
		String[] sa = s.trim().split(" ");
		float[] a = new float[sa.length];
		for(int i=0;i<sa.length;i++) a[i]=Float.parseFloat(sa[i]);
		return a;
	}
	
	private int[] readInts(String s)
	{
		String[] sa = s.trim().split(" ");
		int[] a = new int[sa.length];
		for(int i=0;i<sa.length;i++) a[i]=Integer.parseInt(sa[i]);
		return a;
	}
	
	
	
	/*********************************************************************
	 * COLLADA
	 ********************************************************************/
	private Mesh[] meshes;
	private Bone[] bones;
	private boolean dirtyAnimation; // TODO: This might not be needed if all bones are uploading anyway (unless we use this for calcs)
	// ... see below comment!

		// TODO: Now models share the same shader, all required bones must be uploaded before model is drawn!
	public Collada(InputStream in){
		try{
			// Read File
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			meshes = new Mesh[Integer.parseInt(reader.readLine().trim().split(" ")[1])];
			for(int m=0; m<meshes.length; m++) meshes[m] = new Mesh(reader);
			bones = new Bone[Integer.parseInt(reader.readLine().trim().split(" ")[1])];
			for(int s=0; s<bones.length; s++) bones[s] = new Bone(reader, null);
			reader.close();
			// Upload mesh data to GPU
			for(Mesh m: meshes) m.linkController(); 
			for(Mesh m: meshes) m.render();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public Bone boneSearch(String bone)
	{
		return findBone(bone, bones);
	}
	
	public Bone findBone(String s, Bone[] p)
	{
		for(Bone b: p)
		{
			if(b.bone_id.equalsIgnoreCase(s)) return b;
			Bone ba = findBone(s, b.children);
			if(ba!=null) return ba;
		}
		return null;
	}
	

	public void positionMeshes(float t1, float t2, float ratio)
	{

		for(Mesh m: meshes) m.controller.positionBones(t1, t2, ratio);
	}

	


	public void destroy()
	{
		for(Mesh m: meshes) m.free();
	}
	
	
	public void draw(Matrix modelMatrix)
	{
		GL20.glUniformMatrix4fv(9, true, modelMatrix.asBuffer());
		if(dirtyAnimation) for(Mesh m: meshes) m.controller.uploadSkeleton();

		for(Mesh m: meshes) m.draw();
	}
	
	public void setAnimationDirty()
	{
		dirtyAnimation = true;
	}
	

	public Bone[] getBones()
	{
		return bones;
	}
	

	public void setMeshColor(int m, float r, float g, float b)
	{
		meshes[m].red = r;
		meshes[m].green = g;
		meshes[m].blue = b;
	}
	
	public void setMeshTexture(int m, int t)
	{
		meshes[m].texture = t;
	}
	
	
	
	
	/*********************************************************************
	 * MESH
	 ********************************************************************/
	private class Mesh
	{
		public String mesh_name;
		public float[] positions, normals, map;
		public int[] polyData;
		public int polyCount, glArray, glBuffer;
		public Controller controller;
		public float red=1, green=1, blue=1;
		public int texture = -1;
		
		public Mesh(BufferedReader reader) throws Exception
		{
			mesh_name = reader.readLine().trim().split("\"")[1];
			positions = readFloats(reader.readLine());
			normals = readFloats(reader.readLine());
			map = readFloats(reader.readLine());
			polyCount = Integer.parseInt(reader.readLine().split(" ")[1]);
			polyData = readInts(reader.readLine());
			controller = new Controller(reader);
			glArray = glGenVertexArrays();
			glBuffer = glGenBuffers();
			
			// GRR
			float[] tmp = new float[4];
			for(int v=0; v<positions.length/3; v++)
			{
				controller.bindShapeMatrix.multiplyVector(positions[v*3+0], positions[v*3+1], positions[v*3+2], 1.0f, tmp);
				for(int i=0; i<3; i++) positions[v*3+i] = tmp[i];
			}
		}
		
		public void linkController() {
			for(int i=0; i<controller.jointNames.length;i++) 
				controller.targetBones[i]=findBone(controller.jointNames[i], bones);
		}

		public void draw()
		{
			if(texture!=-1) glBindTexture(GL_TEXTURE_2D, texture);
			glUniform3f(8, red, green, blue);
			glBindVertexArray(glArray);
			glDrawArrays(GL_TRIANGLES, 0, polyCount*3);
			glBindVertexArray(0);
		}

		public void render()
		{
			int controllercount = controller==null? 0 : 16;
			FloatBuffer buf = BufferUtils.createFloatBuffer(polyCount*3*(3+3+2+controllercount));
			int i=0;
			for(int f=0;f<polyCount; f++)
			{
				int vcount = polyData[i++];
				for(int v=0; v<vcount; v++)
				{
					int iPos=polyData[i++], iNor=polyData[i++], iTex= polyData[i++];
					buf.put(positions[iPos*3+0]).put(positions[iPos*3+1]).put(positions[iPos*3+2]); // Location: 1
					buf.put(normals[iNor*3+0]).put(normals[iNor*3+1]).put(normals[iNor*3+2]); // Location: 2
					buf.put(map[iTex*2+0]).put(map[iTex*2+1]); // Location 3
					if(controller!=null) controller.uploadVertex(iPos, buf);
				}
			}
			buf.flip();
			glBindVertexArray(glArray);
			glBindBuffer(GL_ARRAY_BUFFER, glBuffer);
			glBufferData(GL_ARRAY_BUFFER, buf, GL_STATIC_DRAW);
			for(int a=9; a<=15; a++) glEnableVertexAttribArray(a);
			int stride = 4*3 + 4*3 + 4*2 + 4*4 + 4*4 + 4*4 + 4*4;
			glVertexAttribPointer(9, 3, GL_FLOAT, false, stride, 0);
			glVertexAttribPointer(10, 3, GL_FLOAT, false, stride, 12);
			glVertexAttribPointer(11, 2, GL_FLOAT, false, stride, 24);
			glVertexAttribPointer(12, 4, GL_FLOAT, false, stride, 32);
			glVertexAttribPointer(13, 4, GL_FLOAT, false, stride, 48);
			glVertexAttribPointer(14, 4, GL_FLOAT, false, stride, 64);
			glVertexAttribPointer(15, 4, GL_FLOAT, false, stride, 80);
			glBindVertexArray(0);
		}
		
		public void free()
		{
			glDeleteVertexArrays(glArray);
			glDeleteBuffers(glBuffer);
		}
		
		public String toString()
		{
			return "MESH["+mesh_name+"]";
		}
	}
	
	
	
	
	/*********************************************************************
	 * CONTROLLER
	 ********************************************************************/
	private class Controller
	{
		public String[] jointNames;
		public Bone[] targetBones;
		public float[][] invBindMatrices;
		public float[][] vertexBindings;
		public Matrix bindShapeMatrix = new Matrix(); // Moves geometry to where it was when bound to bones. EG. Hats, clothes
		
		public Controller(BufferedReader reader) throws Exception
		{
			bindShapeMatrix.set(readFloats(reader.readLine()));
			jointNames = reader.readLine().trim().split(" ");
			targetBones = new Bone[jointNames.length];
			invBindMatrices = new float[jointNames.length][];
			for(int m=0; m<jointNames.length;m++) invBindMatrices[m] = readFloats(reader.readLine());
			vertexBindings = new float[Integer.parseInt(reader.readLine().trim().split(" ")[1])][];
			for(int i=0; i<vertexBindings.length; i++) vertexBindings[i] = readFloats(reader.readLine()); // NOT FLOATS.. more like: n INT FLOAT
		}



		Matrix tmpMatrix = new Matrix();
		Matrix tmpBindMatrix = new Matrix();
		public void uploadSkeleton()
		{
			for(int i=0; i<targetBones.length; i++)
			{
				tmpBindMatrix.set(invBindMatrices[i]);
				tmpMatrix.setMult(targetBones[i].worldMatrix, tmpBindMatrix);
				glUniformMatrix4fv(10+i, true, tmpMatrix.asBuffer());
			}
		}

		public void uploadVertex(int v, FloatBuffer buf)
		{
			float[] dat = vertexBindings[v];
			float[] bonei = new float[8];
			float[] bonew = new float[8];
			int ii = Math.min(8, (int)dat[0]);
			for(int i=0; i<ii; i++)
			{
				bonei[i] = dat[1+i*2+0];
				bonew[i] = dat[1+i*2+1];
			}
			buf.put(bonei[0]).put(bonei[1]).put(bonei[2]).put(bonei[3]); // 4
			buf.put(bonew[0]).put(bonew[1]).put(bonew[2]).put(bonew[3]); // 5
			buf.put(bonei[4]).put(bonei[5]).put(bonei[6]).put(bonei[7]); // 6
			buf.put(bonew[4]).put(bonew[5]).put(bonew[6]).put(bonew[7]); // 7
		}		
		

		
		private void positionBones(float t1, float t2, float ratio)
		{
			for(Bone b: bones) b.updatePosition(t1, t2, ratio);
		}
		
	}



	
	
	


}
