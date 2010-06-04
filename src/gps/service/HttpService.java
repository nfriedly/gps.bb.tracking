package gps.service;

import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.StreamConnection;

import net.rim.device.api.util.DataBuffer;

public class HttpService extends Thread {
	
	// flag to tell httpservice there is a url ready
	public boolean urlSet = false;
	
	// flag to tell parent (GpsService) the data is downloaded
	public boolean dataReady = false;
	
	public byte[] data;
	
	// the url we're downloading
	public String url = null;
	
	/**
	 * Main loop - checks if a new url has been set and downloads it if that's the case.
	 * If a new url is set while the current one is downloading, it will be ignored
	 */
	public void run(){
		while(true){
			if(url != null){
				dataReady = false;
				System.out.println("Http Service about to start download.");
				data = getPage(url);
				url = null;
				dataReady = true;
				System.out.println("Http Service finished download.");
			}
			else {
				try {
					// sleep for 2 seconds
					Thread.sleep(2000L);
				} catch (InterruptedException e) {
					// or until we receive an interrupt and have a new image to download
				}
			}
		}
	}
	
	/**
	 * Image downloader: downloads the given url and returns the result. Blocking if called directly.
	 * @param url
	 * @return image
	 */
	public byte[] getPage(String url) {
		byte[] data = new byte[256];
		try
		{ 		
			HttpConnection stream = null;
			InputStream in = null; 
			
			synchronized(this) 
			{ 
					stream = (HttpConnection)Connector.open(url);
					in = stream.openInputStream();
			} 
			
			// Extract the data from the input stream.
			
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
