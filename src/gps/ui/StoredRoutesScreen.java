package gps.ui;

import gps.ui.ShowLocationScreen.GpsBackMenuItem;
import gps.ui.ShowLocationScreen.GpsHomeMenuItem;
import gps.ui.ShowLocationScreen.GpsSettingsMenuItem;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;

public class StoredRoutesScreen extends TrackerBaseScreen {
	public StoredRoutesScreen(){
		init();
	}
	public void init(){
		LabelField ListRoutesLabel = new LabelField("Stored Routes Screen");
		add(ListRoutesLabel);
	}
	class GpsBackMenuItem extends MenuItem{
		public GpsBackMenuItem(){
			super ("Back", 20, 10);
		}
		public void run(){
			//Navigate to Back
		}
	}
	class GpsHomeMenuItem extends MenuItem{
		public GpsHomeMenuItem(){
			super ("Home", 20, 10);
		}
		public void run(){
			//Navigate to Home
			HomeScreen hs = new HomeScreen();
			UiApplication thisUiApplication = UiApplication.getUiApplication();
			thisUiApplication.pushScreen(hs);
		}
	}
	class GpsStatisticsMenuItem extends MenuItem{
		public GpsStatisticsMenuItem(){
			super ("Statistics", 20, 10);
		}
		public void run(){
			//Navigate to Statistics
			StatisticsScreen hs = new StatisticsScreen();
			UiApplication thisUiApplication = UiApplication.getUiApplication();
			thisUiApplication.pushScreen(hs);
		}
	}
	protected void makeMenu(Menu menu, int i){
		//call make menu
		super.makeMenu(menu, i);
		menu.add(new GpsBackMenuItem());
		menu.add(new GpsHomeMenuItem());
		menu.add(new GpsStatisticsMenuItem());
	}
}