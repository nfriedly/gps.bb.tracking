package gps.dao;

/**
 * Stores and retrieves routes. Data is stored in XML files in the RouteData/ folder.
 * This version uses the .gpx file format that came from Shawn's phone
 * @author Nathan
 */

import java.io.IOException;
import java.util.Date;

import net.rim.device.api.i18n.DateFormat;
import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.io.http.HttpDateParser; // this requires access to Rim signed APIs for some reason :/
import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLRouteData_GPX extends XMLFileBase implements IRouteData {

	// this is the folder XMLFileBase will store everything in
	private String dir = "RouteData";
	
	// the date format .gpx files use
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
	
	/**
	 * Constructor, calls XMLFileBase() which ensures the data directory exists
	 * @throws IOException
	 */
	XMLRouteData_GPX() throws IOException {
		super();
	}

	/**
	 * @see gps.dao.IRouteData#getRoute(int)
	 */
	public Route getRoute(int routeId) throws Exception {
		return parseRoute(routeId + ".gpx", true);
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
	 * 
	 * NOTE: this function outputs files that are similar to the "My Tracks" 
	 * application format, but may not be exact. Specifically, the <?xml and 
	 * <?xml-stylesheet declarations, desc, color, and ele tags, namespace 
	 * attributes, and CDATA sections are omitted. 
	 * 
	 * Short example file from "My Tracks" application:
	 * 
<?xml version="1.0" encoding="ISO-8859-1" standalone="yes"?>
<?xml-stylesheet type="text/xsl" href="details.xsl"?>
<gpx
 version="1.0"
 creator="My Tracks for the G1 running Android"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xmlns="http://www.topografix.com/GPX/1/0"
 xmlns:topografix="http://www.topografix.com/GPX/Private/TopoGrafix/0/1" xsi:schemaLocation="http://www.topografix.com/GPX/1/0 http://www.topografix.com/GPX/1/0/gpx.xsd http://www.topografix.com/GPX/Private/TopoGrafix/0/1 http://www.topografix.com/GPX/Private/TopoGrafix/0/1/topografix.xsd">
<trk>
<name><![CDATA[Track 4]]></name>
<desc><![CDATA[]]></desc>
<number>4</number>
<topografix:color>c0c0c0</topografix:color>
<trkseg>
<trkpt lat="39.286749" lon="-84.322954">
<ele>208.0</ele>
<time>2010-04-24T00:37:36Z</time>
</trkpt>
<trkpt lat="39.286637" lon="-84.322868">
<ele>218.0</ele>
<time>2010-04-24T00:37:37Z</time>
</trkpt>
</trkseg>
</trk>
</gpx>
	 */
	public int saveRoute(String name, GpsPoint[] points) throws Exception {
		// create an xml document
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		
		// give it a root element named "route"
		Node gpxEl = doc.createElement("gpx");
		doc.appendChild(gpxEl);
		
		// give it a name element
		Node nameEl = doc.createElement("name");
		nameEl.setNodeValue(name);
		gpxEl.appendChild(nameEl);
		
		// give it an id and id element
		int id = nextId();
		Node idEl = doc.createElement("number");
		idEl.setNodeValue(String.valueOf(id));
		gpxEl.appendChild(idEl);		
		
		// give it a track element - essentially a container for the points element
		Node trkEl = doc.createElement("trk");
		gpxEl.appendChild(trkEl);
		
		// give it a points element - "trkseg" because .gpx tracks can have multiple segments. But ours can't.
		Node pointsEl = doc.createElement("points");
		gpxEl.appendChild(pointsEl);
		
		// add the points one by one
		for(int i=0; i<points.length; i++){
			// a point element
			Node pointEl = doc.createElement("trkpt");
			pointsEl.appendChild(pointEl);
			
			// a lon attribute 
			Node lon = doc.createAttribute("lon");
			lon.setNodeValue(String.valueOf(points[i].getLon()));
			pointEl.appendChild(lon);
			
			// a lat attribute
			Node lat = doc.createAttribute("lat");
			lat.setNodeValue(String.valueOf(points[i].getLat()));
			pointEl.appendChild(lat);
			
			// a timestamp element
			Node timestamp = doc.createElement("time");
			// use the SimpleDateFormat object to convert the date to a string
			timestamp.setNodeValue(df.formatLocal(points[i].getTimestamp().getTime()));
			pointEl.appendChild(timestamp);

		}
		writeFile(id + ".gpx", doc);
		return id;
	}

	/**
	 * @see gps.dao.IRouteData#deleteRoute(int)
	 */
	public void deleteRoute(int routeId) throws Exception {
		deleteFile(routeId + ".gpx");
	}
	
	/**
	 * Parses the XML to a Route object
	 */
	private Route parseRoute(String fileName, boolean includeData ) throws Exception{
		Route route = new Route();
		Document doc = getDoc(fileName);

		// read in the name and id from the xml file
		route.setName(doc.getElementsByTagName("name").item(0).getNodeValue());
		route.setId(Integer.parseInt(doc.getElementsByTagName("number").item(0).getNodeValue()));

		// in some instances, we only want the name and ID and can safely ignore the rest
		if(includeData){
			
			// read in the gps points from the xml file
			NodeList points = doc.getElementsByTagName("trkpt");
			
			for(int i=0; i < points.getLength(); i++){
				Node point = points.item(i);
				route.addPoint(
					new GpsPoint(
						Double.parseDouble(point.getAttributes().getNamedItem("lon").getNodeValue()),
						Double.parseDouble(point.getAttributes().getNamedItem("lat").getNodeValue()),
						parseDate(point.getFirstChild().getNodeValue()) // the date is in an oddball format in these files
					)
				);
			}
	
		}
		
		return route;
	}
	
	/**
	 * Accepts the oddball .gpx date format, regexes it into something more normal, 
	 * and uses the HTTPDateParser to parse it.
	 * 
	 * Use of HTTPDateParser requires access to RIM signed APIs (why!?)
	 * 
	 * An example date: 2010-04-24T01:08:19Z
	 * (I think Z is supposed to be followed by timezone info and a lack thereof indicates GMT)
	 * 
	 * All of this is necessary because blackberry's DateFormat object doesn't have a parse() method.
	 * Otherwise this would work:
	 * DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
	 * 
	 * @param str
	 * @return date
	 */
	private Date parseDate(String str){
		// first get rid of the "T" and "Z"
		str = str.replace('T', ' ').replace('Z', ' ');
		// add in the timezone
		str = str + " GMT";
		// and parse it
		return new Date(HttpDateParser.parse(str));
	}

}
