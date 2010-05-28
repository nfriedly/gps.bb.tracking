package gps.ui;

import javax.microedition.location.Location;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.FieldChangeListener;

import gps.service.GpsService;

public class HomeScreen extends TrackerBaseScreen implements FieldChangeListener {
	ButtonField btnRecord;
	ButtonField btnStopRecord;
	public LabelField location;
	public BitmapField map;
	
	public HomeScreen(){
		init();
	}
	public void init(){
		
		GpsService service = new GpsService(this);
		
		location = new LabelField();
		map = new BitmapField(); 
		btnRecord = new ButtonField("Record", ButtonField.CONSUME_CLICK | ButtonField.FIELD_LEFT);
		btnRecord.setChangeListener(this);
	    btnStopRecord = new ButtonField("Stop", ButtonField.CONSUME_CLICK | ButtonField.FIELD_RIGHT);
		btnStopRecord.setChangeListener(this);
		HorizontalFieldManager recordManager = new HorizontalFieldManager();
		
		recordManager.add(map);
		recordManager.add(location);
		
		recordManager.add(btnRecord);
		recordManager.add(btnStopRecord);
		
		add(recordManager);
	}
	
	class GpsCurLocationMenuItem extends MenuItem {
		
		public GpsCurLocationMenuItem(){
			super ("My Location", 20, 10);
		}

		public void run(){
			//double longitude = coordinates.getQualifiedCoordinates().getLongitude();
			//double latitude = coordinates.getQualifiedCoordinates().getLatitude();
			
			//System.out.println(longitude);
			
			//Dialog.inform("Current Location - Longitude: " + longitude + " Latitude: " + latitude);
		}
	}
	class GpsRoutesMenuItem extends MenuItem{
		public GpsRoutesMenuItem(){
			super ("Routes", 20, 10);
		}
		public void run(){
			//Navigate to Routes
			ListRoutesScreen lrs = new ListRoutesScreen();
			UiApplication thisUiApplication = UiApplication.getUiApplication();
			thisUiApplication.pushScreen(lrs);
		}
	}
	class GpsLocationsMenuItem extends MenuItem{
		public GpsLocationsMenuItem(){
			super ("Locations", 20, 10);
		}
		public void run(){
			//Navigate to Locations
			ListLocationsScreen lls = new ListLocationsScreen();
			UiApplication thisUiApplication = UiApplication.getUiApplication();
			thisUiApplication.pushScreen(lls);
		}
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
		menu.add(new GpsCurLocationMenuItem());
		menu.add(new GpsRoutesMenuItem());
		menu.add(new GpsLocationsMenuItem());
		menu.add(new GpsSettingsMenuItem());
		
	}
	public void fieldChanged(Field field, int Context){
		if(field == btnRecord){
			Dialog.inform("You are currently recording");
		}
		else if(field == btnStopRecord){
			Dialog.inform("You have stopped recording");
		}
	}
}
