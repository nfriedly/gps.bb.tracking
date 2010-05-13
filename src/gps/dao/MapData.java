package gps.dao;

import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.system.EncodedImage;

public class MapData extends UiApplication {
	public final static String LOCATION_URL = "http://maps.google.com/maps/api/staticmap";
	public final static String SETTINGS_STRING = "&zoom=12&size=400x400&sensor=false";
	
	private double longitude;
	private double latitude;
	private EditField _status;
	private BitmapField _map;
	
	private static int _interval = 1;
	private LocationProvider _locationProvider;
	private Location _location;
	
	private HTTPClient _http = new HTTPClient();
	private byte[] _data = new byte[256];
	
	public static void main(String[] args)
    {
        // Create a new instance of the application and make the currently
        // running thread the application's event dispatch thread.
        new MapData().enterEventDispatcher();
    }
	
	
	public MapData() {
		
		MapDataScreen screen = new MapDataScreen();
        screen.setTitle("GPS Application");
        
		try {
			// TODO: update criteria with desired location distances, etc
			LocationProvider.getInstance(new Criteria()).setLocationListener(new LocationListenerImpl(), 1, 1, 1);
		} catch(Exception e) { System.err.println(e.toString()); }
		
		_status = new EditField(Field.NON_FOCUSABLE);
		_map = new BitmapField(); 
		
		if(startLocationUpdate()) {
			screen.add(_status);
			screen.add(_map);
		}
		
		pushScreen(screen);
	}

	/**
     * Update the GUI with the data just received.
     * @param msg The message to display
     */
    private void updateLocationScreen(final String msg, final EncodedImage map)
    {
        invokeLater(new Runnable()
        {
            public void run()
            {
                _status.setText(msg);
                _map.setImage(map);
            }
        });
    }
    
    private boolean startLocationUpdate()
    {
        boolean retval = false;
        
        try
        {
            _locationProvider = LocationProvider.getInstance(null);
            
            if ( _locationProvider == null )
            {
                // We would like to display a dialog box indicating that GPS isn't supported, 
                // but because the event-dispatcher thread hasn't been started yet, modal 
                // screens cannot be pushed onto the display stack.  So delay this operation
                // until the event-dispatcher thread is running by asking it to invoke the 
                // following Runnable object as soon as it can.
                Runnable showGpsUnsupportedDialog = new Runnable() 
                {
                    public void run() {
                        Dialog.alert("GPS is not supported on this platform, exiting...");
                        System.exit( 1 );
                    }
                };
                
                invokeLater( showGpsUnsupportedDialog );  // Ask event-dispatcher thread to display dialog ASAP.
            }
            else
            {
                // Only a single listener can be associated with a provider, and unsetting it 
                // involves the same call but with null, therefore, no need to cache the listener
                // instance request an update every second.
                _locationProvider.setLocationListener(new LocationListenerImpl(), _interval, 1, 1);
                retval = true;
            }
        }
        catch (LocationException le)
        {
            System.err.println("Failed to instantiate the LocationProvider object, exiting...");
            System.err.println(le); 
            System.exit(0);
        }        
        return retval;
    }

	private class LocationListenerImpl implements LocationListener
    {
		private EncodedImage EncodedImg;
		
        // Methods ----------------------------------------------------------------------------------------------
        /**
         * @see javax.microedition.location.LocationListener#locationUpdated(LocationProvider,Location)
         */
        public void locationUpdated(LocationProvider provider, Location location)
        {
            if(location.isValid())
            {
                longitude = location.getQualifiedCoordinates().getLongitude();
                latitude = location.getQualifiedCoordinates().getLatitude();
                
             // Grab our URL then encode the image.
                try {
                	_data = _http.getPage(LOCATION_URL + "?center=" + longitude + "," + latitude + SETTINGS_STRING);
                	EncodedImg = EncodedImage.createEncodedImage(_data,0,_data.length);
                } 
                catch(Exception e)
                {
                	Dialog.alert("No connection could be made");
                }
                
    	     // Information to be displayed on the device.
                StringBuffer sb = new StringBuffer();
                sb.append("Longitude: ");
                sb.append(longitude);
                sb.append("\n");
                sb.append("Latitude: ");
                sb.append(latitude);
                sb.append("\n");
              
             // We now have to update the screen with our new information
                MapData.this.updateLocationScreen(sb.toString(), EncodedImg);
            }
        }
  
        public void providerStateChanged(LocationProvider provider, int newState)
        {
            // Not implemented.
        }        
    }
	
    private final class MapDataScreen extends MainScreen
    {
        // Constructor
    	MapDataScreen()
        {
            RichTextField instructions = new RichTextField("Coordinates are: ",Field.NON_FOCUSABLE);
            this.add(instructions);
        }
    }
}
