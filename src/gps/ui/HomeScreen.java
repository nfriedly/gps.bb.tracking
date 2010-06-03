package gps.ui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.FieldChangeListener;

import gps.service.GpsService;

public class HomeScreen extends TrackerBaseScreen implements FieldChangeListener {
	ButtonField btnRecord;
	ButtonField btnStopRecord;
	public LabelField location;
	public BitmapField bitmapField;
	public EncodedImage encImg;
	
	public HomeScreen(){
		init();
	}
	public void init(){
		
		GpsService service = new GpsService(this);
		
		Bitmap map = new Bitmap(400, 400);
		location = new LabelField();
		
		bitmapField = new BitmapField(map, BitmapField.FIELD_HCENTER);
		add(bitmapField);	
		add(new SeparatorField());
		
		btnRecord = new ButtonField("Record", ButtonField.CONSUME_CLICK);
		btnRecord.setChangeListener(this);
	    btnStopRecord = new ButtonField("Stop", ButtonField.CONSUME_CLICK);
		btnStopRecord.setChangeListener(this);
		
		add(location);
		add(new SeparatorField());
		HorizontalFieldManager recordManager = new HorizontalFieldManager();
		
		recordManager.add(btnRecord);
		recordManager.add(btnStopRecord);
		
		add(recordManager);
	}

	class GpsRoutesMenuItem extends MenuItem{
		public GpsRoutesMenuItem(){
			super ("Saved Routes", 20, 10);
		}
		public void run(){
			//Navigate to Routes
			StoredRoutesScreen lrs = new StoredRoutesScreen();
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

		menu.add(new GpsRoutesMenuItem());
		menu.add(new GpsLocationsMenuItem());
		menu.add(new GpsSettingsMenuItem());
		
	}
	
	public void fieldChanged(Field field, int Context){
		if(field == btnRecord){
			Dialog.inform("You are currently recording");
			GpsService.startRecording();
		}
		else if(field == btnStopRecord){
			Dialog.inform("You have stopped recording");
			GpsService.stopRecording();
		}
	}
}
