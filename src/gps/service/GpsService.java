package gps.service;

import java.util.Enumeration;
import java.util.Vector;

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
	
	// http is started in the constructor
	HttpService http = new HttpService();
	//byte[] data = new byte[256];
	
	private String base_url = "http://maps.google.com/maps/api/staticmap?";
	
	private LocationProvider _locationProvider;
	private int _interval = 5;
	
	private double _longitude;
	private double _latitude;
	
	boolean retval = false;
	static boolean record = false;
	
	//last point for comparison
	GpsPoint lastPoint = null;
	
	//reference to the parent screen
	HomeScreen parent;
	
	private Route current_route = new Route();
	
	public GpsService(HomeScreen homeScreen) {
		parent = homeScreen;
		
		// fire up our http service so it's ready when we need it
		http.start(); 
		
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
		
		// check if we have a image already waiting on us
		if(http.dataReady == true){
			System.out.println("Image downloaded successfully");
			try{
				// this usually works
				EncodedImage EncodedImg = EncodedImage.createEncodedImage(http.data,0,http.data.length);
				parent.bitmapField.setImage(EncodedImg);
			} catch(Exception e){
				e.printStackTrace();
			}
			// set the url back to it's default state so that we know we can give it a new one
			http.url = null;
		}
		
		// shortcut: if we havn't moved, don't do anything further
		if(point.equalsLocation(lastPoint)){
			return;
		}

		// pass the gps point to the home screen's update gps label method
		parent.location.setText("Latitude: " + point.getLat() + ", Longitude:" +  point.getLon());
		
		// if there is no url that http service is currently downloading, give it a new one
		if (http.url == null){
			System.out.println("Downloading a new image");
			String url = base_url + "center="+point.getLat()+","+point.getLon()+"&zoom=12&size=400x380&sensor=false";
			url += "&path=color:0x0000ff";
			Vector recentPoints = current_route.getRecentPoints(10);
			for (Enumeration e = recentPoints.elements() ; e.hasMoreElements() ;) {
		         GpsPoint p = (GpsPoint) e.nextElement();
		         url += "|" + p.getLat() + "," + p.getLon();
		     }
			System.out.println("current url: " + url);
			http.url = url;
			http.interrupt();
		}
		
		if(record){
			// then .. add the current location to the current route.
			// .. but we already did that.
		} 
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
