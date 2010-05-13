package gps.ui;

import net.rim.device.api.ui.UiApplication;

public class GpsTrackingApplication extends UiApplication {
	public GpsTrackingApplication(){
		HomeScreen hs = new HomeScreen();
		pushScreen(hs);
	}
	public static void main(String[] args){
		GpsTrackingApplication gpsApplication = new GpsTrackingApplication();
		gpsApplication.enterEventDispatcher();
	}
	

}
