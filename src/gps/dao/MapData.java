package gps.dao;

import gps.service.HttpService;

import javax.microedition.location.Location;

import net.rim.device.api.system.EncodedImage;

public class MapData {
	
	public final static String LOCATION_URL = "http://maps.google.com/maps/api/staticmap";
	public final static String SETTINGS_STRING = "&zoom=12&size=400x400&sensor=false";
	
	private HttpService _http;
	
	private EncodedImage EncodedImg;
	private byte[] _data = new byte[256];
	
	private double longitude;
	private double latitude;
	
	public EncodedImage MapImage(Location location){
		
		longitude = location.getQualifiedCoordinates().getLongitude();
		latitude = location.getQualifiedCoordinates().getLatitude();
		
		try {
        	_data = _http.getPage(LOCATION_URL + "?center=" + longitude + "," + latitude + SETTINGS_STRING);
        	EncodedImg = EncodedImage.createEncodedImage(_data,0,_data.length);
        } 
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        
		return EncodedImg;
	}
}
