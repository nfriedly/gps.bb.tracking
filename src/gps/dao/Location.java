package gps.dao;

public class Location extends GpsPoint{
	private String name;
	private String image;
	
	// getters and setters
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getImage() {
		return image;
	}
}
