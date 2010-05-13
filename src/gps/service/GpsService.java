package gps.service;

import javax.microedition.location.Location;

import gps.dao.ISettingsData;
import gps.dao.GpsPoint;


public class GpsService implements ISettingsData {
	
	private Location _location;
	
	public Location currentLocation()
	{ 
		GpsPoint gps = new GpsPoint();
		
		_location = gps.getCurrentLoc();
		
		return _location;
	}

}
