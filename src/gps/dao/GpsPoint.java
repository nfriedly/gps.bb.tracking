package gps.dao;

import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationProvider;

public class GpsPoint {
	
	public LocationProvider _locationProvider;
	public Location location;
	
	// Returns a one time Location object for current location
	public Location getCurrentLoc()
	{
		try {
	        Criteria c = new Criteria();
	        c.setCostAllowed(false);
	        _locationProvider = LocationProvider.getInstance(c);
	        location = _locationProvider.getLocation(100);
        
        }  catch(Exception e) {
        	e.printStackTrace();
        }
        
        //System.out.println(location.getQualifiedCoordinates().getLongitude());
        
        return location;
	}
	
	
}
