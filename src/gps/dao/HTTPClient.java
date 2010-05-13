package gps.dao;

import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import net.rim.device.api.util.DataBuffer;

public class HTTPClient {
	final byte[] getPage(String url) {
		StreamConnection stream = null;
		InputStream in = null; 
		
		System.out.println(url);
		
		synchronized(this) 
		{ 
			try
			{ 
				stream = (StreamConnection)Connector.open(url);
				in = stream.openInputStream();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		} 
		
		// Extract the data from the input stream.
		byte[] data = new byte[256];
		
		try
		{
			DataBuffer db = new DataBuffer(); 
			int chunk = 0;
			
			while ( -1 != (chunk = in.read(data))) 
			{
				db.write(data, 0, chunk);
			}
			
			in.close(); 
			// Here is your image in byte format.
			data = db.getArray(); 
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return data;
	}
}