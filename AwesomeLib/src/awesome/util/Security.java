package awesome.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

public class Security {
	
	public static byte[] hash(File f, String hashMethod, int arraySize){
		try{
			MessageDigest md =MessageDigest.getInstance(hashMethod);
			InputStream is =new FileInputStream(f);
			DigestInputStream dis =new DigestInputStream(is, md);
			byte[] array =new byte[arraySize];
			while((dis.read(array)) !=-1);
			
			byte[] digest =md.digest();
			dis.close();
			return digest;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String hashString(File f, String hashMethod, int arraySize){
		return DatatypeConverter.printHexBinary(hash(f, hashMethod, arraySize));
	}
	
	public static byte[] hash(byte[] bytes, String hashMethod, int arraySize){
		try{
			MessageDigest md =MessageDigest.getInstance(hashMethod);
			byte[] digest =md.digest(bytes);
			return digest;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static String hashString(byte[] bytes, String hashMethod, int arraySize){
		return DatatypeConverter.printHexBinary(hash(bytes, hashMethod, arraySize));
	}
}
