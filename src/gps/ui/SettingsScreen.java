package gps.ui;
import java.io.IOException;

import org.xml.sax.SAXException;

import gps.dao.XMLSettingsData;
import gps.dao.ISettingsData;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.xml.parsers.ParserConfigurationException;

public class SettingsScreen extends TrackerBaseScreen implements FieldChangeListener {
	CheckboxField metricCheckBox;
	EditField fuelEditField;
	ButtonField saveButton;
	public SettingsScreen (){
		init();
	}
	public void init(){
		LabelField settingsLabel = new LabelField("Settings Screen");
		add(settingsLabel);
		add(new SeparatorField());
		fuelEditField = new EditField("Fuel Rate:", "");
		saveButton = new ButtonField("Save");
		add(fuelEditField);
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
	protected void makeMenu(Menu menu, int i){
		//call make menu
		super.makeMenu(menu, i);
		menu.add(new GpsHomeMenuItem());
	}
	public void fieldChanged(Field field, int context) {
        ISettingsData settings = null;
        if (field == metricCheckBox)
       {
       }
        if (field == saveButton)
       {
            double price = Double.parseDouble(fuelEditField.getText());
            try {
               settings = XMLSettingsData.getInstance();
               settings.setDollarsPerGallon(price);
       } catch (Exception e) {
           // something happened, alert the user!
    	   
       }
       }

    }
}
