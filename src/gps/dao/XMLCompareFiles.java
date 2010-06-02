package gps.dao;

import net.rim.device.api.util.Comparator;

public class XMLCompareFiles implements Comparator {

	public int compare(Object o1, Object o2) {
		return ((String)o1).compareTo((String)o2);
	}
	 
}
