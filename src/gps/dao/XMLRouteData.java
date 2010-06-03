package gps.dao;

/**
 * Stores and retrieves routes. Data is stored in XML files in the RouteData/ folder
 * @author Nathan
 */

import java.io.IOException;
import java.util.Date;

import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLRouteData extends XMLFileBase implements IRouteData {

	// this is the folder XMLFileBase will store everything in
	private String dir = "RouteData";
	
	/**
	 * Constructor, calls XMLFileBase() which ensures the data directory exists
	 * @throws IOException
	 */
	XMLRouteData() throws IOException {
		super();
	}

	/**
	 * @see gps.dao.IRouteData#getRoute(int)
	 */
	public Route getRoute(int routeId) throws Exception {
		return parseRoute(routeId + ".xml", true);
	}

	/**
	 * @see gps.dao.IRouteData#getRoutesList()
	 */
	public Route[] getRoutesList() throws Exception {
		String[] files = listFiles();
		Route[] routes = {};
		for( int i=0; i < files.length; i++){
			routes[routes.length] = parseRoute(files[i], false);
		}
		return routes;
	}

	/**
	 * @see gps.dao.IRouteData#saveRoute(gps.dao.Route)
	 */
	public int saveRoute(String name, GpsPoint[] points) throws Exception {
		// create an xml document
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		
		// give it a root element named "route"
		Node routeEl = doc.createElement("route");
		doc.appendChild(routeEl);
		
		// give it a name element
		Node nameEl = doc.createElement("name");
		nameEl.setNodeValue(name);
		routeEl.appendChild(nameEl);
		
		// give it an id and id element
		int id = nextId();
		Node idEl = doc.createElement("id");
		idEl.setNodeValue(String.valueOf(id));
		routeEl.appendChild(idEl);		
		
		// give it a points element
		Node pointsEl = doc.createElement("points");
		routeEl.appendChild(pointsEl);
		
		// add the points one by one
		for(int i=0; i<points.length; i++){
			// a point element
			Node pointEl = doc.createElement("point");
			pointsEl.appendChild(pointEl);
			
			// a lon element 
			Node lon = doc.createElement("lon");
			lon.setNodeValue(String.valueOf(points[i].getLon()));
			pointEl.appendChild(lon);
			
			// a lat element
			Node lat = doc.createElement("lat");
			lat.setNodeValue(String.valueOf(points[i].getLat()));
			pointEl.appendChild(lat);
			
			// a timestamp element
			Node timestamp = doc.createElement("timestamp");
			timestamp.setNodeValue(String.valueOf(points[i].getTimestamp().getTime()));
			pointEl.appendChild(timestamp);

		}
		writeFile(id + ".xml", doc);
		return id;
	}

	/**
	 * @see gps.dao.IRouteData#deleteRoute(int)
	 */
	public void deleteRoute(int routeId) throws Exception {
		deleteFile(routeId + ".xml");
	}
	
	/**
	 * Parses the XML to a Route object
	 */
	private Route parseRoute(String fileName, boolean includeData ) throws Exception{
		Route route = new Route();
		Document doc = getDoc(fileName);

		// read in the name and id from the xml file
		route.setName(doc.getElementsByTagName("name").item(0).getNodeValue());
		route.setId(Integer.parseInt(doc.getElementsByTagName("id").item(0).getNodeValue()));

		// in some instances, we only want the name and ID and can safely ignore the rest
		if(includeData){
			
			// read in the gps points from the xml file
			// probably not the best way of doing this, because if the xml is ever missing a tag then everything is wrong
			// but we're writing the xml so it should work for now
			NodeList lons = doc.getElementsByTagName("lon");
			NodeList lats = doc.getElementsByTagName("lat");
			NodeList times = doc.getElementsByTagName("timestamp");
			
			for(int i=0; i < lons.getLength(); i++){
				route.addPoint(
					new GpsPoint(
						Double.parseDouble(lons.item(i).getNodeValue()),
						Double.parseDouble(lats.item(i).getNodeValue()),
						new Date(Long.parseLong(times.item(i).getNodeValue()))
					)
				);
			}
	
		}
		
		return route;
	}

}
