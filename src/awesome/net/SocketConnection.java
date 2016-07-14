package awesome.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class SocketConnection
{
	private Socket socket;
	private DataOutputStream dos;
	private DataInputStream dis;
	private boolean connected = false;
	
	private byte[] inboxBuffer[] = new byte[1280][];
	private byte[] outboxBuffer[] = new byte[1280][];
	private int inboxWriteMarker, inboxReadMarker;
	private int outboxWriteMarker, outboxReadMarker;
	private Thread inboxThread, outboxThread;
	
	public SocketConnection(Socket s) throws Exception
	{
		socket = s;
		s.setTcpNoDelay(true);
		dis = new DataInputStream(socket.getInputStream());
		dos = new DataOutputStream(socket.getOutputStream());
		connected = true;
		
		inboxThread = new Thread("SocketConnection In "+s)
		{
			public void run()
			{
				inboxLoop();
			}
		};
		inboxThread.start();
		
		outboxThread = new Thread("SocketConnection Out "+s)
		{
			public void run()
			{
				outboxLoop();
			}
		};
		outboxThread.start();
	}
	
	public void close()
	{
		connected = false;
		try { 
			outLoop = false;
			if(!socket.isClosed()) socket.close(); 
			if(inboxThread!=null) inboxThread.join();
			if(outboxThread!=null) outboxThread.join();
		} catch (Exception e) { }
		
	}
	
	private void inboxLoop()
	{
		try 
		{
			while(true)
			{
				byte[] data = new byte[dis.readInt()];
				dis.readFully(data);
				storeInbox(data);
			}
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			connected = false;
		}
	}
	
	boolean outLoop = true;
	private void outboxLoop()
	{
		try 
		{
			while(outLoop)
			{
				if(!connected) break;
				boolean f = false;
				byte[] b;
				while((b=fetchNextOutPacket())!=null)
				{
					f = true;
					dos.writeInt(b.length);
					dos.write(b);
				}
				if(f) dos.flush();
				Thread.sleep(5);
			}
		}
		catch (Exception e)
		{
//			e.printStackTrace();
			connected = false;
		}
	}
	
	public boolean connected()
	{
		return connected;
	}
	
	private void storeInbox(byte[] ba) throws Exception
	{
		while(inboxFull()) Thread.sleep(20);

		inboxBuffer[inboxReadMarker++] = ba;
		if (inboxReadMarker == inboxBuffer.length) inboxReadMarker = 0;
	}
	
	public void sendPacket(byte[] ba)
	{
		while(outboxFull()){
			if(!connected) return;
			try { Thread.sleep(20); } catch (Exception e) { }
		}

		outboxBuffer[outboxReadMarker++] = ba;
		if (outboxReadMarker == outboxBuffer.length) outboxReadMarker = 0;
	}
	
	public byte[] fetchNextPacket()
	{
		if (inboxWriteMarker != inboxReadMarker)
		{
			byte[] value = inboxBuffer[inboxWriteMarker++];
			if (inboxWriteMarker == inboxBuffer.length) inboxWriteMarker = 0;
			return value;
		}
		else return null;
	}
	
	private byte[] fetchNextOutPacket()
	{
		if (outboxWriteMarker != outboxReadMarker)
		{
			byte[] value = outboxBuffer[outboxWriteMarker++];
			if (outboxWriteMarker == outboxBuffer.length) outboxWriteMarker = 0;
			return value;
		}
		else return null;
	}
	
	private boolean inboxFull()
	{
		if (inboxReadMarker + 1 == inboxWriteMarker) return true;
		if (inboxReadMarker == (inboxBuffer.length - 1) && inboxWriteMarker == 0) return true;
		return false;
	}
	
	private boolean outboxFull()
	{
		if (outboxReadMarker + 1 == outboxWriteMarker) return true;
		if (outboxReadMarker == (outboxBuffer.length - 1) && outboxWriteMarker == 0) return true;
		return false;
	}
}
