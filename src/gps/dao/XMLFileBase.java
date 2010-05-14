package gps.dao;

/**
 * Base class for XML classes to extend. Supports XML and file system operations.
 *  @author Nathan
 */

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

import net.rim.device.api.util.Arrays;
import net.rim.device.api.xml.jaxp.DOMInternalRepresentation;
import net.rim.device.api.xml.jaxp.XMLWriter;
import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLFileBase {
	
	// this is the root folder on the blackberry where all of the xml data will be stored
	protected String rootDir = "gps_xml_data";
	
	// this is where the current class's directory will be stored to keep different file types seperate
	// each file that extends this should overwrite this variable
	private String dir; 
	
	XMLFileBase() throws IOException{
		String root = this.getRootFileSystem();
		if(root != null){
			rootDir = root + "/" + rootDir;
		}
		this.createDir(rootDir);
		if(dir != null){
			dir = rootDir + "/" + dir;
			// createDir() only creates the directory if it doesn't already exist
			createDir(dir);
		} else {
			dir = rootDir;
		}
	}
	
	/**
	 * Lists all of the files in this class's directory
	 * @throws IOException 
	 * @return String[] files
	 */
	protected String[] listFiles() throws IOException{
		FileConnection fcon;
		fcon = (FileConnection)Connector.open( dir, Connector.READ );
		Enumeration e = fcon.list("*.xml", false); // only .xml files and skip hidden files
		String[] files = {};
		String file;
		while (e.hasMoreElements()) {
			file = (String) e.nextElement();
			if(!file.endsWith("/")){
				files[files.length] = file;
			}
		}
		return files;
	}
	
	/**
	 * Reads the given XML file and returns a w3c.org.dom.Document 
	 * @return XML Document
	 */
	protected Document getDoc(String filename) throws Exception {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        return docBuilder.parse(getInputSource(filename));
	}
	
	/**
	 * similar to a database's next autoincrement value function
	 * @return suggested element id
	 * @throws IOException 
	 */
	protected int nextId() throws IOException{
		String[] files = listFiles();
		if(files.length == 0){
			return 1;
		}
		Arrays.sort(files, new XMLCompareFiles());
		String top = files[files.length-1];
		top = top.substring(0, top.indexOf("."));
		int itop = Integer.parseInt(top);
		return itop + 1;
	}
	
	// everything below here is private
	
	
	// based on http://www.birkbecktech.eu/?p=124
	private String getRootFileSystem()
	{
		String root = null;
		Enumeration e = FileSystemRegistry.listRoots();
		while (e.hasMoreElements()) {
			root = (String) e.nextElement();
			if( root.equalsIgnoreCase("sdcard/") ) {
				return "file:///SDCard";
			} else if( root.equalsIgnoreCase("store/") ) {
				return "file:///store";
			}
		}
		return null;
	}
	
	private String path(String fileName){
		return dir + "/" + fileName;
	}

	// opens the file and converts it into an InputSource
	private InputSource getInputSource( String fileName ) throws IOException
	{
		FileConnection fcon;
		fcon = (FileConnection)Connector.open( path(fileName), Connector.READ );
		return new InputSource(fcon.openInputStream());
	}
	
	// writes data to a file, creating it if necessary
	protected void writeFile( String fileName, String content) throws IOException
	{
		FileConnection fcon;
		fcon = (FileConnection)Connector.open( path(fileName), Connector.READ_WRITE );
		// make sure the file exists
		if (!fcon.exists()) {
			fcon.create();
		}
		// make sure the file's not a directory
		if(!fcon.isDirectory()){
			fcon.delete();
			fcon.create();
		}
		DataOutputStream dos = fcon.openDataOutputStream();
		dos.writeChars(content);
		dos.close();
		fcon.close();
	}
	
	protected void writeFile(String file, Document doc) throws IOException, SAXException {
		writeFile(file, serialize(doc));		
	}
	
	// convert a Document to a string
	private String serialize(Document doc) throws SAXException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        XMLWriter writer = new XMLWriter(os); 
        writer.setPrintCompressedOutput();
        DOMInternalRepresentation.parse(doc, writer);
        return os.toString();
	}
	
	// deletes a file
	protected void deleteFile( String fileName) throws IOException
	{
		FileConnection fcon;
		fcon = (FileConnection)Connector.open( path(fileName), Connector.READ_WRITE );
		if(fcon.exists()){
			fcon.delete();
		}
		fcon.close();
	}	

	
	// creates the directory or throws an exception trying
	// does nothing if the directory (or a file with the same name) already exists
	private void createDir( String dirName) throws IOException
	{
		FileConnection fcon;
		fcon = (FileConnection)Connector.open( dirName, Connector.READ_WRITE );
		
		// only create the dir if it doesn't already exist
		if (!fcon.exists()) {
			fcon.mkdir();
		}
		// if it exists but it's not a directory, make it a directory.
		if(!fcon.isDirectory()){
			fcon.delete();
			fcon.mkdir();
		}
		fcon.close();
	}
	
}
