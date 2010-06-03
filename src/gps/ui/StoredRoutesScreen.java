package gps.ui;

import java.util.Vector;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;

public class StoredRoutesScreen extends TrackerBaseScreen {
	public StoredRoutesScreen(){
		init();
	}
	public void init(){
//		LabelField ListRoutesLabel = new LabelField("Stored Routes Screen");
//		add(ListRoutesLabel);
		
		this.LoadList();
	}
	
	public void LoadList() {
		
	    try {		 
	    	
	    	// add the model to the list
		    final RouteListField routeListView = new RouteListField();
		    routeListView.setEmptyString("List is empty", DrawStyle.HCENTER);
		    final RouteListModel routeListModel = new RouteListModel(routeListView);
		    
		    // create a context menu on the list...
	//	    myListView.addToContextMenu(myListModel.getAddMenuItem(0, 0));
	//	    myListView.addToContextMenu(myListModel.getRemoveMenuItem(0, 0));
	//	    myListView.addToContextMenu(myListModel.getModifyMenuItem(0, 0));
	//	    myListView.addToContextMenu(myListModel.getEraseMenuItem(0, 0));


//		    gps.dao.Route[] storedRoute = new gps.service.GpsService().getRoutesList();
//			for (int index = 0; index < storedRoute.length -1; index ++) {
//				routeListModel.insert(storedRoute[index].getName(), storedRoute[index].getId());
//			}
			
		    routeListModel.insert("SSSS", 0);
		    routeListModel.insert("IIII", 1);
//		    routeListModel.drawListRow(routeListView, new Graphics(null), index, y, w)
		    // layout components on the screen...
		    {
		      // get the MainScreen's VerticalFieldManager...
		      Manager vfm = getMainManager();
	
		      // add components to the vfm
		      vfm.add(routeListView);
	
		      // add a separator
		      vfm.add(new SeparatorField(SeparatorField.LINE_HORIZONTAL));
	
		      // add something to the title
		      setTitle(new LabelField("Stored Routes Screen"));
		    }
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	class RouteListField extends ListField {
		private Menu _contextMenu = new Menu();

		public void addToContextMenu(MenuItem menuitem) {
	    	if (menuitem != null) _contextMenu.add(menuitem);
		}

		public ContextMenu getContextMenu() {
		    // DO NOT call ContextMenu.getInstance()... this will produce null in many cases!!!
	
		    ContextMenu cMenu = super.getContextMenu();
		    cMenu.clear();
	
		    int size = _contextMenu.getSize();
	
		    for (int i = 0; i < size; i++) {
		    	cMenu.addItem(_contextMenu.getItem(i));
		    }
	
		    return cMenu;
		}
		
		protected boolean navigationClick(int status, int time) {
		    // In case you need the selected item
		    int selIdx = this.getSelectedIndex();
		    
		    // Add code here later... if needed.
			    	
		    return true;
		}

	}
	
	class RouteListModel implements ListFieldCallback {

		  private Vector _data = new Vector();
		  private ListField _view;
		  private int _defaultRowHeight = 32;
		  private int _defaultRowWidth = _defaultRowHeight;
		  private int _textImagePadding = 5;
		  //private Bitmap _bitmap;

		  /** constructor that saves a ref to the model's view - {@link ListField}, and binds this model to the view */
		  public RouteListModel(ListField list) {
		    // save a ref to the list view
		    _view = list;


		    // bind this model to the given view
		    list.setCallback(this);

		    // set the default row height
		    _view.setRowHeight(_defaultRowHeight);

		    // load the bitmap to use in the cell rendering
		    //_bitmap = // load some bitmap of your choice here
		  }

		  //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		  // implement ListFieldCallback interface
		  //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

		  /** list row renderer */
		  public void drawListRow(ListField list, Graphics g, int index, int y, int w) {

		    String text = (String) _data.elementAt(index);

		    // draw the text /w ellipsis if it's too long...
		    g.drawText(text,
		               _defaultRowWidth + _textImagePadding, y,
		               DrawStyle.LEADING | DrawStyle.ELLIPSIS,
		               w - _defaultRowWidth - _textImagePadding);

		    // draw the to the left of the text...
		    //g.drawBitmap(0, y, _bitmap.getWidth(), _bitmap.getHeight(), _bitmap, 0, 0);

		  }

		  /** list row data accessor */
		  public Object get(ListField list, int index) {
		    return _data.elementAt(index);
		  }

		  /** used for filtering list elements */
		  public int indexOfList(ListField list, String p, int s) {
		    return _data.indexOf(p, s);
		  }

		  /** used for rendering list... provide the width of the list in pixels */
		  public int getPreferredWidth(ListField list) {
		    return Display.getWidth();
		  }

		  //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		  // data manipulation methods...  not part of the interface
		  //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

		  /** mutator, which syncs model and view */
		  public void insert(String toInsert, int index) {
		    // update the model
		    _data.addElement(toInsert);

		    // update the view
		    _view.insert(index);
		  }
		  /** mutator, which syncs model and view */
		  public void delete(int index) {
		    // update the model
		    _data.removeElementAt(index);

		    // update the view
		    _view.delete(index);
		  }
		  /** mutator, which syncs model and view */
		  public void erase() {
		    int size = _data.size();

		    // update the view
		    for (int i = 0; i < size; i++) {
		      delete(size - i - 1);
		    }
		  }
		  public void modify(String newValue, int index) {
		    // update the model
		    _data.removeElementAt(index);
		    _data.insertElementAt(newValue, index);

		    // update the view
		    _view.invalidate(index);
		  }
		  public int size() {
		    return _data.size();
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
	class GpsStatisticsMenuItem extends MenuItem {
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
		menu.add(new GpsHomeMenuItem());
		menu.add(new GpsStatisticsMenuItem());
	}
}