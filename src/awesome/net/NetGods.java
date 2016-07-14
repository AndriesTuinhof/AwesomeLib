package awesome.net;

import java.io.DataInputStream;
import java.net.Socket;

public class NetGods implements Runnable{
	
	private static Socket sock;
	public static void start(){
		new Thread(new NetGods(), "Networking").start();
	}
	
	public static void stop(){
		try{
			if(sock!=null)
				sock.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	
	public void run(){
		try(Socket s =new Socket("betapanda.co.uk", 1234)){
			sock =s;
			DataInputStream dis =new DataInputStream(s.getInputStream());
			while(true){
				int size =dis.readShort();
				byte[] b =new byte[size];
				dis.readFully(b);				
				
				PacketReader.setPacket(b);
				System.out.println(PacketReader.packetID());
				System.out.println(PacketReader.readLong());
//				PandaEngineFoo.getOverlay().chatbox.output(PacketReader.readFloat()+"");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
