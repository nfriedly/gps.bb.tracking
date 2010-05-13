package gps.ui;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;

public class SettingsScreen extends TrackerBaseScreen implements FieldChangeListener {
	CheckboxField metricCheckBox;
	public SettingsScreen (){
		init();
	}
	public void init(){
		LabelField settingsLabel = new LabelField("Settings Screen");
		add(settingsLabel);
		add(new SeparatorField());
		EditField fuelEditField = new EditField("Fuel Rate:", "");
		add(fuelEditField);
		metricCheckBox = new CheckboxField("Use Metric Units", false);
		metricCheckBox.setChangeListener(this);
		add(metricCheckBox);
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
	public void fieldChanged(Field field, int context) {
		if (field == metricCheckBox)
		{
		
		}
		
	}
}
