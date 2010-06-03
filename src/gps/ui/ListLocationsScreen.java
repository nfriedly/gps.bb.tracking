package gps.ui;

import net.rim.device.api.collection.CollectionListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;

public class ListLocationsScreen extends TrackerBaseScreen {
	public ListLocationsScreen(){
		init();
	}
	public void init(){
		LabelField ListRoutesLabel = new LabelField("List Locations Screen");
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
	class GpsHomeMenuItem extends MenuItem {
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
	protected void makeMenu(Menu menu, int i){
		//call make menu
		super.makeMenu(menu, i);
		menu.add(new GpsBackMenuItem());
		menu.add(new GpsHomeMenuItem());
	}
}
