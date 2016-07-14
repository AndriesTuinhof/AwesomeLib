package awesome.util;

import java.io.File;
import java.nio.file.Files;

/**
 * Reads a file from the HDD using a separate thread.
 */
public class ThreadedFileLoader
{
	private Thread thread;
	private String filename;
	private byte[] data = null;
	
	public ThreadedFileLoader(String filename)
	{
		this.filename = filename;
		thread = new LoaderThread();
		thread.start();
	}
	
	private class LoaderThread extends Thread
	{
		public void run()
		{
			try
			{
				Thread.sleep((long) (Math.random()*100));
				data = Files.readAllBytes(new File(filename).toPath());
			}
			catch(Exception e)
			{
				System.out.println("Exception loading file via Thead: ");
				e.printStackTrace();
			}
		}
	}


	public byte[] getData()
	{
		return data;
	}
	
	public boolean fileReady()
	{
		return data!=null;
	}
	
	public void waitTillFinished()
	{
		try
		{
			thread.join();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
