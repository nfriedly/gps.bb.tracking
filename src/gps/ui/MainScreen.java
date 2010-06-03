package gps.ui;

import net.rim.device.api.ui.UiApplication;

public class MainScreen extends UiApplication {
	
    public static void main(String argv[]) {
		MainScreen app = new MainScreen();
	    app.enterEventDispatcher();
    }

    public MainScreen() {
        pushScreen(new HomeScreen());
    }

}
