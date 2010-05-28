package gps.service;

import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.UiApplication;

import gps.dao.GpsPoint;
import gps.dao.ISettingsData;
import gps.dao.Route;
import gps.ui.HomeScreen;

public class GpsService extends Thread implements ISettingsData {
	
	private String base_url = "http://maps.google.com/maps/api/staticmap?";
	
	private LocationProvider _locationProvider;
	private int _interval = 3;
	
	private double _longitude;
	private double _latitude;
	
	boolean retval = false;
	boolean record = false;
	
	
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
	
	public void updateMap(GpsPoint point){
		final HttpService http = new HttpService();
		byte[] data = new byte[256];
		// Add it to the current route
		//current_route.addPoint(point);
		// pass the gps point to the home screen's update gps label method
		parent.location.setText(point.getLat() + ", " +  point.getLon());
		// take the current_route and convert it to a url
		// have the http service download that url
		// update the screen
		// if we're currently downloading a url, just return without doing anything	
		// potential optimization: skip points that are very close to each other or in the middle of a straight line when building the map url
		if(record == false && http.status == true){
			data = http.getPage(base_url + "center="+point.getLat()+","+point.getLon()+"&zoom=12&size=400x400&sensor=false");
			EncodedImage EncodedImg = EncodedImage.createEncodedImage(data,0,data.length);
			parent.map.setImage(EncodedImg);
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
