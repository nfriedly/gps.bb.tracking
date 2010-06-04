package gps.ui;

import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;

public class NewLocationScreen extends TrackerBaseScreen {
	public NewLocationScreen(){
		init();
	}
	public void init(){
		LabelField ListRoutesLabel = new LabelField("New Location Screen");
		add(ListRoutesLabel);
	}
	class SaveRouteMenuItem extends MenuItem  {
		public SaveRouteMenuItem(){
			super ("Save", 20, 10);
		}
		public void run(){
			//Save the new route
			SaveRoute ss = new SaveRoute();
			UiApplication thisUiApplication = UiApplication.getUiApplication();
			thisUiApplication.pushScreen(ss);
		}
	}
	class SaveRoute extends TrackerBaseScreen {
		// dummy class
	}
	class GpsSettingsMenuItem extends MenuItem  {
		public GpsSettingsMenuItem(){
			super ("Settings", 20, 10);
		}
		public void run(){
			//Navigate to settings
			SettingsScreen ss = new SettingsScreen();
			UiApplication thisUiApplication = UiApplication.getUiApplication();
			thisUiApplication.pushScreen(ss);
		}
	}
	protected void makeMenu(Menu menu, int i){
		//call make menu
		super.makeMenu(menu, i);
		menu.add(new SaveRouteMenuItem());
		menu.add(new GpsSettingsMenuItem());
	}

}
