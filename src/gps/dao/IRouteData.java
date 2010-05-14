package gps.dao;

public interface IRouteData {
	/**
	 * should lookup the given route id and return the associated route
	 * throws an exception if the route is not found
	 * @param routeId
	 * @return
	 * @throws Exception
	 */
	public Route getRoute(int routeId) throws Exception;
	
	/**
	 * should save the given route and return the id
	 * throws an exception if the save fails
	 * 
	 * @param route
	 * @return
	 * @throws Exception
	 */
	public int saveRoute(String name, GpsPoint[] points) throws Exception;
	
	/**
	 * should delete the given route
	 * throws an exception if the deletion fails
	 * 
	 * @param routeId
	 * @throws Exception
	 */
	public void deleteRoute(int routeId) throws Exception;
	
	/**
	 * Returns an array of routes with only the name and id filled in (no GpsPoints)
	 * throws an exception if there's an error looking up the list
	 * 
	 * @return
	 * @throws Exception
	 */
	public Route[] getRoutesList() throws Exception;
}
