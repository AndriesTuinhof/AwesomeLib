package awesome.net;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PacketReader
{
	private static ByteArrayInputStream bais;
	private static DataInputStream dis;
	private static int id;
	
	public static void setPacket(byte[] data)
	{
		bais = new ByteArrayInputStream(data);
		dis = new DataInputStream(bais);
		id = readShort();
	}
	
	public static int packetID()
	{
		return id;
	}

	public static int readByte()
	{
		try { return dis.readByte(); } catch (IOException e) { e.printStackTrace(); return 0;}
	}
	
	public static int readShort()
	{
		try { return dis.readShort(); } catch (IOException e) { e.printStackTrace(); return 0;}
	}
	
	public static int readInt()
	{
		try { return dis.readInt(); } catch (IOException e) { e.printStackTrace(); return 0;}
	}
	
	public static float readFloat()
	{
		try { return dis.readFloat(); } catch (IOException e) { e.printStackTrace(); return 0;}
	}
	
	public static double readDouble()
	{
		try { return dis.readDouble(); } catch (IOException e) { e.printStackTrace(); return 0;}
	}
	
	public static long readLong()
	{
		try { return dis.readLong(); } catch (IOException e) { e.printStackTrace(); return 0;}
	}
	
	public static String readString()
	{
		try { return dis.readUTF(); } catch (IOException e) { e.printStackTrace(); return null;}
	}

}
