package gps.dao;

import java.io.IOException;
import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;
import net.rim.device.api.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Reads and records settings in the settings.xml file
 * @author nathan
 */

public class XMLSettingsData extends XMLFileBase implements ISettingsData {
	
	// local instance of the settings file
	private Document doc;
	
	// local instances of the settings nodes
	private Node metricsEnabled;
	private Node dollarsPerGallon;
	private Node milesPerGallon;

	/**
	 * Reads the xml file into memory or creates one if that fails
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	XMLSettingsData() throws IOException, ParserConfigurationException {
		super(); // could throw an IO exception if there's trouble finding/creating the folder
		try {
			// will throw an exception if the file doesn't exist
			doc = getDoc("settings.xml");
			// will throw an exception if the document doesn't have exactly 1 of each node
			metricsEnabled = getNode("metricsEnabled");
			dollarsPerGallon = getNode("dollarsPerGallon");
			milesPerGallon = getNode("milesPerGallon");
		} catch (Exception e) {
			// if there were any exceptions in reading the settings file, this will create a new one
			createDoc();
		}
	}
	
	/**
	 * Creates the doc if the xml file is corrupted or doesn't exist
	 */
	private void createDoc() throws ParserConfigurationException{
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        doc = docBuilder.newDocument();
        
        // give it a root element named "settings"
		Node settingsEl = doc.createElement("settings");
		doc.appendChild(settingsEl);
		
		// add in elements for  Metrics Enabled, Dollars Per Gallon, and Miles Per Gallon
		// all boolean and double values are saved as strings because that's all xml will accept
		metricsEnabled = doc.createElement("metricsEnabled");
		metricsEnabled.setNodeValue("0");
		settingsEl.appendChild(metricsEnabled);	
		
		dollarsPerGallon = doc.createElement("dollarsPerGallon");
		dollarsPerGallon.setNodeValue("0");
		settingsEl.appendChild(dollarsPerGallon);
		
		milesPerGallon = doc.createElement("milesPerGallon");
		milesPerGallon.setNodeValue("0");
		settingsEl.appendChild(milesPerGallon);
	}
	
	/**
	 * Makes sure there is exactly one of an element in the document and returns it
	 */
	private Node getNode(String name) throws Exception{
		NodeList elements = doc.getElementsByTagName(name);
		if(elements.getLength() != 1) {
			throw new Exception();
		}
		return elements.item(1);
	}
	
	/**
	 * Saves the document to the settings.xml file
	 * @throws IOException
	 * @throws SAXException
	 */
	private void save() throws IOException, SAXException{
		writeFile("settings.xml", doc);
	}

	
	// getters and setters below
	
	public double getDollarsPerGallon() {
		return Double.parseDouble(dollarsPerGallon.getNodeValue());
	}

	public boolean getMetrictsEnabled() {
		// simple method of turning a string into a bool: if it's 0 it's false, anything else is true
		return !metricsEnabled.getNodeValue().equals("0");
	}

	public double getMilesPerGallon() {
		return Double.parseDouble(milesPerGallon.getNodeValue());
	}

	public void setDollarsPerGallon(double price) throws IOException, SAXException {
		dollarsPerGallon.setNodeValue(Double.toString(price));
		save();
	}

	public void setMetricsEnabled(boolean enabled) throws IOException, SAXException {
		// I imagine there's a shorter/better way to do this, but I don't know it off the top of my head
		String strEnabled = "0";
		if(enabled){
			strEnabled = "1";
		}
		metricsEnabled.setNodeValue(strEnabled);
		save();
	}

	public void setMilesPerGallon(double mpg) throws IOException, SAXException {
		milesPerGallon.setNodeValue(Double.toString(mpg));
		save();
	}
	
}
