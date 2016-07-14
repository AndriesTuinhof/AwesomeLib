package awesome.net;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketWriter
{
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();
	private DataOutputStream dos = new DataOutputStream(baos);
	private static PacketWriter instance = new PacketWriter();
	
	public static PacketWriter instance()
	{
		return instance;
	}
	
	public static PacketWriter createPacket(int id)
	{
		instance.baos.reset();
		writeShort(id);
		return instance;
	}
	
	public static byte[] getPacketAsData()
	{
		return instance.baos.toByteArray();
	}

	public static PacketWriter writeByte(int v)
	{
		try { instance.dos.writeByte(v); } catch (IOException e) {}
		return instance;
	}
	
	public static PacketWriter writeShort(int v)
	{
		try { instance.dos.writeShort(v); } catch (IOException e) {}
		return instance;
	}
	
	public static PacketWriter writeInt(int v)
	{
		try { instance.dos.writeInt(v); } catch (IOException e) {}
		return instance;
	}
	
	public static PacketWriter writeFloat(float v)
	{
		try { instance.dos.writeFloat(v); } catch (IOException e) {}
		return instance;
	}
	
	public static PacketWriter writeDouble(double v)
	{
		try { instance.dos.writeDouble(v); } catch (IOException e) {}
		return instance;
	}
	
	public static PacketWriter writeLong(long v)
	{
		try { instance.dos.writeLong(v); } catch (IOException e) {}
		return instance;
	}
	
	public static PacketWriter writeString(String v)
	{
		try { instance.dos.writeUTF(v); } catch (IOException e) {}
		return instance;
	}
}
