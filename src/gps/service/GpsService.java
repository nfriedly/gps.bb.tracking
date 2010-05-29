package gps.service;

import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

import net.rim.device.api.ui.UiApplication;

import gps.dao.GpsPoint;
import gps.dao.ISettingsData;
import gps.dao.Route;
import gps.ui.HomeScreen;

public class GpsService extends Thread implements ISettingsData {
	
	private LocationProvider _locationProvider;
	private int _interval = 1;
	
	private double _longitude;
	private double _latitude;
	boolean retval = false;
	
	//reference to the parent screen
	HomeScreen parent;
	
	private Route current_route = new Route();
	
	public GpsService(HomeScreen homeScreen) {
		parent = homeScreen;
	}

	public void run() {
		startLocationUpdate();
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
		// Add it to the current route
		current_route.addPoint(point);
		// pass the gps point to the home screen's update gps label method
		
		// take the current_route and convert it to a url
		// have the http service download that url
		// update the screen
		// if we're currently downloading a url, just return without doing anything	
		// potential optimization: skip points that are very close to each other or in the middle of a straight line when building the map url
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
                    }
                };
                
                UiApplication.getUiApplication().invokeLater( showGpsUnsupportedDialog );
                stopLocationUpdates();
            }
        }
  
        public void providerStateChanged(LocationProvider provider, int newState)
        {
            // Not implemented.
        }        
    }
}
