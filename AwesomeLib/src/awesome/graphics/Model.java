package awesome.graphics;

import awesome.core.ResourceLoader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;

/**
 * Created by Jon on 13/07/2015.
 */
public class Model
{
    private Wavefront mesh;
    public Wavefront collision; // FIXME: Set to private once ray trace tests complete
    private int diffuseTexture, normalTexture, specularTexture, glowTexture;

    public Model(InputStream is) throws Exception
    {
        DataInputStream dis = new DataInputStream(is);
        if(dis.readBoolean())
        {
            byte[] d = new byte[dis.readInt()];
            dis.readFully(d);
            mesh = new Wavefront(new ByteArrayInputStream(d));
        }

        if(dis.readBoolean())
        {
            byte[] d = new byte[dis.readInt()];
            dis.readFully(d);
            collision = new Wavefront(new ByteArrayInputStream(d));
        }

        if(dis.readBoolean())
        {
            byte[] d = new byte[dis.readInt()];
            dis.readFully(d);
            diffuseTexture = ResourceLoader.getTexture(d, false);
        }

        if(dis.readBoolean())
        {
            byte[] d = new byte[dis.readInt()];
            dis.readFully(d);
            normalTexture = ResourceLoader.getTexture(d, false);
        }

        if(dis.readBoolean())
        {
            byte[] d = new byte[dis.readInt()];
            dis.readFully(d);
            specularTexture = ResourceLoader.getTexture(d, false);
        }

        if(dis.readBoolean())
        {
            byte[] d = new byte[dis.readInt()];
            dis.readFully(d);
            glowTexture = ResourceLoader.getTexture(d, false);
        }
    }

    public void draw()
    {
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        if(glowTexture>0) GL11.glBindTexture(GL11.GL_TEXTURE_2D, glowTexture);
        else GL11.glBindTexture(GL11.GL_TEXTURE_2D, -1);
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        if(specularTexture>0) GL11.glBindTexture(GL11.GL_TEXTURE_2D, specularTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        if(normalTexture>0) GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        if(diffuseTexture>0) GL11.glBindTexture(GL11.GL_TEXTURE_2D, diffuseTexture);
        if(mesh!=null) mesh.draw();
//        if(collision!=null) collision.draw();
    }

    public void destroy()
    {
        GL11.glDeleteTextures(diffuseTexture);
        GL11.glDeleteTextures(normalTexture);
        GL11.glDeleteTextures(specularTexture);
        GL11.glDeleteTextures(glowTexture);
        if(mesh!=null) mesh.destroy();
    }
}
