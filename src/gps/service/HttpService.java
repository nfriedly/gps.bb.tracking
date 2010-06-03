package gps.service;

import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.StreamConnection;

import net.rim.device.api.util.DataBuffer;

public class HttpService extends Thread {
	
	// Reference to the parent to call when we have new image data
	GpsService parent;
	
	// the url we're downloading
	String url;
	
	public HttpService(GpsService parent, String url){
		this.parent = parent;
		this.url = url;
	}
	
	public void run(){
		byte[] image = getPage(url);
		parent.newImage(image);
	}
	public byte[] getPage(String url) {
		
		HttpConnection stream = null;
		InputStream in = null; 
		
		synchronized(this) 
		{ 
			try
			{ 
				stream = (HttpConnection)Connector.open(url);
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
