package com.toursims.mobile;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.toursims.mobile.LocalizationService.MyBinder;
import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.controller.KmlParser;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.kml.Placemark;
import com.toursims.mobile.model.kml.Point;
import com.toursims.mobile.ui.utils.CustomItemizedOverlay;

import android.graphics.drawable.Drawable;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.SlidingDrawer;
import android.widget.Toast;

public class CourseStepActivity extends MapActivity{
    /** Called when the activity is first created. */
	
	private static LocalizationService serviceLocalization;
	private String proximityIntentAction = new String("CourseStepActivity");
	private List<Placemark> placemarks;
	
	private MapController mapController;
    List<Overlay> mapOverlays;
    Drawable drawable;
    CustomItemizedOverlay itemizedOverlay;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursestep);
		
        Log.d("Bind Service","Bind Service");
       
        placemarks = getPlaceMarks();
        
        MapView mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);
		//mapView.setStreetView(true);
		mapController = mapView.getController();
		mapController.setZoom(13); // Zoom 1 is world view
		mapController.animateTo(new GeoPoint(45759723,4842223));

        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.maps_icon);
        itemizedOverlay = new CustomItemizedOverlay(drawable, this);
        
        for(Placemark placemark: getPlaceMarks()){
        	String[] lL = placemark.getPoint().getCoordinates().split(",");
        	int l = (new Double(Double.parseDouble(lL[1])* 1000000)).intValue();
        	int L = (new Double(Double.parseDouble(lL[0])* 1000000)).intValue();
        	Log.d(getLocalClassName(), String.valueOf(l) + " " + String.valueOf(L));
        	GeoPoint point = new GeoPoint(l,L);
        	OverlayItem overlayItem = new OverlayItem(point, "", "");
        
        	itemizedOverlay.addOverlay(overlayItem);
        }
        mapOverlays.add(itemizedOverlay);
        
        doBindService();
    }
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    protected List<Placemark> getPlaceMarks() {
        Bundle bundle = getIntent().getExtras();
        int course_id = bundle.getInt("COURSE_ID");
        
        CourseBDD datasource = new CourseBDD(this);
    	datasource.open();
    	
    	Course c = new Course();
    	c.setId(course_id);
    	   	
    	List<Placemark> l = datasource.getAllPlacemarks(c);
    	
    	datasource.close();
    	return l;
    }

    private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			serviceLocalization = ((LocalizationService.MyBinder) binder).getService();
			Toast.makeText(CourseStepActivity.this, "Connected", Toast.LENGTH_SHORT).show();
			updateReceiver();
		}

		public void onServiceDisconnected(ComponentName className) {
			serviceLocalization = null;
		}
	};
	
	private void doBindService() {
		bindService(new Intent(this, LocalizationService.class), mConnection,Context.BIND_AUTO_CREATE);
	}
	
	static int currentPoint = 0;
    static BroadcastReceiver receiverLocalization;
    
    public void updateReceiver() {    	
    	Log.d("updateReceiver","Receiver Update");
    	
    	if(serviceLocalization!=null&&!placemarks.isEmpty()){
		    	if(receiverLocalization!=null){
		    		unregisterReceiver(receiverLocalization);
		    					
			    	NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			    	Notification notification = new Notification(R.drawable.ic_launcher,placemarks.get(currentPoint).getName(), System.currentTimeMillis());
			    	notification.flags |= Notification.FLAG_AUTO_CANCEL;
			    	notification.number += 1;
			    	
			    	PendingIntent activity = PendingIntent.getActivity(getBaseContext(), 0, new Intent(getBaseContext(),HomeActivity.class),0);
			    	notification.setLatestEventInfo(getBaseContext(), placemarks.get(currentPoint).getName(),placemarks.get(currentPoint).getName(), activity);
			    	notificationManager.notify(0, notification);
			    	currentPoint += 1;
		    	} 
	    	
		    if(currentPoint<placemarks.size()){
		    	receiverLocalization = new BroadcastReceiver() {			
					@Override
					public void onReceive(Context context, Intent intent) {			    	
				    	updateReceiver();
						Log.d("ReceiverGPS","Proximity Alert");
					}
	    		};
	    	
	    	IntentFilter intentFilter = new IntentFilter(proximityIntentAction);  	
	    	registerReceiver(receiverLocalization, intentFilter);
	    	
	    	serviceLocalization.setProximityAlert(placemarks.get(currentPoint).getPoint().getLongitude(),
	    			placemarks.get(currentPoint).getPoint().getLattitude(), 1, 0,proximityIntentAction);  	
	    	}
	    }
     else {
		Log.d("ReceiverGPS","Service not connected");
     }
    }
}
