package gps.dao;

import java.util.Vector;

/**
 * Stores a list of GpsPoints and a name and ID
 * @author nathan
 */

public class Route {
	private Vector points = new Vector();
	private String name;
	private int id;

	/**
	 * Sets all points at the same time
	 * @param points
	 */
	public void setPoints(Vector points) {
		this.points = points;
	}
	
	/**
	 * adds a single point to the route
	 * @param point
	 */
	public void addPoint(GpsPoint point){
		points.addElement( point );
	}

	public Vector getPoints() {
		return points;
	}
	
	/**
	 * Reads and returns the last few points
	 * todo: skip points in a line, and possibly start skilling a point every so often towards the end to return a longer path.
	 * @param howmany - an upper limit, not the guaranteed size of the returned data
	 * @return a short list of recent points
	 */
	public Vector getRecentPoints(int howmany) {
		// shortcut: if they ask for more data than we have, just return everything
		if(howmany > points.size()){
			return points;
		}
		
		// create a vector of sixe howmany that increases by 1 if it hits it's size limit (since it's really an array with a fixed size)
		// we're only using a vector for consistency
		Vector recentPoints = new Vector(howmany,1);
		
		// how many points do we still need to add to our return
		int pointsRemaining = howmany;
		
		// what's our current position in the points vector, starting at the end. 
		int curPos = points.size()-1;
		GpsPoint lastPoint = null;

		
		// otherwise, grab the most recent x points
		while(pointsRemaining > 0 && curPos > 0){
			
			// pull out our next point
			GpsPoint nextPoint = (GpsPoint) points.elementAt(curPos);
			
			// make sure it's not the same as our previous point (at least one of the lat or the lon must be different)
			// null lastPoint is allowed since it indicates we're on our first point and no other checks are necessary
			if(lastPoint == null || !nextPoint.equalsLocation(lastPoint))
			{
				lastPoint = nextPoint;
				//todo: additional testing to avoid multiple points in a line (google only needs the first and last)
				
				// set the element at pointsRemaining since we're working backwards
				recentPoints.setElementAt(nextPoint, pointsRemaining);
				
				// decrement pointsRemaining only if we actually used the last point
				pointsRemaining--;
			}
			
			// decrement curPos regardless of whether we kept the point (decrement because we're going backwards)
			curPos--;
		}
		return recentPoints;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
}
