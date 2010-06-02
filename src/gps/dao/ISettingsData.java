package gps.dao;

import java.io.IOException;

import org.xml.sax.SAXException;

/** 
 * Stores program-wide settings used for calculating metrics and other items.
 * @author nathan
 */

public interface ISettingsData {

	/**
	 * Getter and setter for boolean metricsEnabled
	 * @return boolean
	 */
	public boolean getMetrictsEnabled();
	
	public void setMetricsEnabled(boolean enabled) throws IOException, SAXException;
	
	/**
	 * Getter and setter for price per gallon of fuel (in dollars per gallon)
	 * @return double 
	 */
	public double getDollarsPerGallon();
	
	public void setDollarsPerGallon(double price) throws IOException, SAXException;

	/**
	 * Getter and setter for vehicle fuel efficiency (in miles per gallon)
	 * @return double
	 */
	public double getMilesPerGallon();
	
	public void setMilesPerGallon(double mpg) throws IOException, SAXException;
	
	
}
