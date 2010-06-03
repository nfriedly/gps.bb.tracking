package gps.dao;

import java.util.Date;

import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationProvider;

public class GpsPoint {
	
	private double lon;
	private double lat;
	private Date timestamp;
	
	// empty constructor
	public GpsPoint(){
	}
	
	// constructor
	// probably the primary way of setting lon/lat
	public GpsPoint(double lon, double lat){
		this.lon = lon;
		this.lat = lat;
		this.setTimestamp(new Date());
	}

	// last constructor - accepts lon, lat, date
	public GpsPoint(double lon, double lat, Date timestamp){
		this.lon = lon;
		this.lat = lat;
		this.setTimestamp(timestamp);
	}

	
	// getters and setters:
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public double getLat() {
		return lat;
	}	

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLon() {
		return lon;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Date getTimestamp() {
		return timestamp;
	}	
}
