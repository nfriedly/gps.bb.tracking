package gps.service;

import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.UiApplication;

import gps.dao.GpsPoint;
import gps.dao.Route;
import gps.ui.HomeScreen;

public class GpsService{
	
	HttpService http;
	byte[] data = new byte[256];
	
	private String base_url = "http://maps.google.com/maps/api/staticmap?";
	
	private LocationProvider _locationProvider;
	private int _interval = 5;
	
	private double _longitude;
	private double _latitude;
	
	boolean retval = false;
	static boolean record = false;
	
	
	// are we currently waiting on an image to download?
	private boolean downloading = false;
	
	//reference to the parent screen
	HomeScreen parent;
	
	private Route current_route = new Route();
	
	public GpsService(HomeScreen homeScreen) {
		parent = homeScreen;
		
		if(startLocationUpdate()){
			System.out.println("GPS Initialized!");
		}
	}
	
	private boolean startLocationUpdate()
	{
		try {
			
			_locationProvider = LocationProvider.getInstance(null);
			
			if ( _locationProvider == null )
			{
	        	retval = false;
			} else {
				_locationProvider.setLocationListener(new LocationListenerImpl(this), _interval, 1, 1);
				retval = true;
			}
			
		} catch (LocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        return retval;
    }

	private boolean stopLocationUpdates(){
		_locationProvider.setLocationListener(null, -1, -1, -1);
    	_locationProvider.reset();
    	
		return retval;
	}
	
	public static void startRecording(){
		record = true;
	}
	
	public static void stopRecording(){
		record = false;
	}
	
	public void updateMap(GpsPoint point){
		// Add it to the current route
		current_route.addPoint(point);
		// pass the gps point to the home screen's update gps label method
		parent.location.setText("Latitude: " + point.getLat() + ", Longitude:" +  point.getLon());
		
		
		// if we're not already downloading an image
		if(downloading == false){
			// then start downloading one:
			downloading = true;
			// take the current_route and convert it to a url
			// and pass it to a new http service
			http = new HttpService(this, base_url + "center="+point.getLat()+","+point.getLon()+"&zoom=12&size=400x400&sensor=false");
			// and start the download. 
			// It'll take care of itself from here and call newImage() when it's done
			http.start();
		}
		// else: do not start to download an image, just update the screen
		
		if(record){
			// then .. add the current location to the current route.
			// .. but we already did that.
		} 
	}
	
	/**
	 * This is called by http service when it finishes downloading an image
	 * @param data
	 */
	public void newImage(byte[] data) {
		downloading = false;
		EncodedImage EncodedImg = EncodedImage.createEncodedImage(data,0,data.length);
		parent.bitmapField.setImage(EncodedImg);
	}
	
	private class LocationListenerImpl implements LocationListener
    {
		// keep a reference to the parent class to call whenever we have new data
		private GpsService parent;
		
		public LocationListenerImpl(GpsService parent){
			this.parent = parent;
		}
        public void locationUpdated(LocationProvider provider, Location location)
        {
            if(location.isValid())
            {
            	_longitude = location.getQualifiedCoordinates().getLongitude();
            	_latitude = location.getQualifiedCoordinates().getLatitude();

            	Runnable showGpsUnsupportedDialog = new Runnable() 
                {
                    public void run() {
						// every time we get new data....
						// create a GpsPoint (our internal data format)
						GpsPoint point = new GpsPoint(_longitude, _latitude);
						
						// and tell the parent class to update the map
						parent.updateMap(point);
						System.out.println("Listener Output: " + point.getLat() + ", " + point.getLon());
                    }
                };
                
                UiApplication.getUiApplication().invokeLater( showGpsUnsupportedDialog );
            }
        }
  
        public void providerStateChanged(LocationProvider provider, int newState)
        {
            // Not implemented.
        }        
    }

}
