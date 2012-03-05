package com.toursims.mobile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.ListIterator;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.toursims.mobile.LocalizationService.MyBinder;
import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.controller.CourseLoader;
import com.toursims.mobile.controller.PlaceWrapper;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.kml.Placemark;
import com.toursims.mobile.model.kml.Point;
import com.toursims.mobile.model.places.Place;
import com.toursims.mobile.model.places.Road;
import com.toursims.mobile.ui.utils.CustomItemizedOverlay;
import com.toursims.mobile.ui.utils.DirectionPathOverlay;
import com.toursims.mobile.ui.utils.RoadProvider;

import android.R.integer;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.Toast;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;

public class CourseStepActivity extends MapActivity{
    /** Called when the activity is first created. */
	private static final String TAG = LocalizationService.class.getName();
	private static final String PROXIMITY_INTENT = LocalizationService.class.getName()+".PROXIMITY_INTENT";
	private static final String PROXIMITY_RECEIVER = LocalizationService.class.getName()+".PROXIMITY_RECEIVER";
	private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATE = 3000; // in Milliseconds
    private static final long POINT_RADIUS = 25; // in Meters
    private static final long PROX_ALERT_EXPIRATION = -1;    

	private static LocalizationService serviceLocalization;
	private static List<Placemark> placemarks;
	private static Course course;
	private static String type;	
	private MapController mapController;
	private List<Overlay> mapOverlays;
	private Drawable drawable;
	private CustomItemizedOverlay itemizedOverlay;
	private CustomItemizedOverlay itemizedOverlay_currentPoint;
	private List<Road> mRoads;
	private MyLocationOverlay myLocationOverlay;
	private MapView mapView;
    private LocationManager locationManager;
    private static int currentPlacemark = -1;
    private static BroadcastReceiver receiverLocalization;
	private LocalizationService s;


    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle bundle = getIntent().getExtras();       
        setContentView(R.layout.coursestep);
        
        placemarks = getPlaceMarks();
        
        if(bundle.containsKey(Course.NEXT_PLACEMARK)){
        	if(currentPlacemark<placemarks.size()-1)
        	currentPlacemark += 1;
        }
                
        mapView = (MapView) findViewById(R.id.map);
		mapView.setBuiltInZoomControls(true);
		//mapView.setStreetView(true);
		mapController = mapView.getController();
		mapController.setZoom(14); // Zoom 1 is world view
		
		updateMap();

   
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 
                        MINIMUM_TIME_BETWEEN_UPDATE, 
                        MINIMUM_DISTANCECHANGE_FOR_UPDATE,
                        new MyLocationListener()
        );
        
        updatePlacemark();

		myLocationOverlay = new MyLocationOverlay(this, mapView);
		myLocationOverlay.enableMyLocation();
		mapOverlays.add(myLocationOverlay);
		
	
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("TAG","Start Service");
		
		Intent i = new Intent(this, LocalizationService.class);
		i.putExtra(Point.LATITUDE, placemarks.get(currentPlacemark).getPoint().getLatitude());
		i.putExtra(Point.LONGITUDE, placemarks.get(currentPlacemark).getPoint().getLatitude());
		i.putExtra(Placemark.NAME, placemarks.get(currentPlacemark).getName());

		startService(i);
	}
	
    
	@Override
	protected void onResume() {
		super.onResume();
		
		Log.d("TAG","Stop service");
		stopService(new Intent(this, LocalizationService.class));

		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		Bundle b = getIntent().getExtras();
		
		if(b.getBoolean(Course.NEXT_PLACEMARK)){
			currentPlacemark++;
		}
		
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		myLocationOverlay.enableMyLocation();
		mapOverlays.add(myLocationOverlay);
	}
	
	/**
	 * Display the user on the MapView
	 */
	protected void displayUser(Location location) {
		GeoPoint point = new GeoPoint((int)(location.getLatitude() * 1e6), (int)(location.getLongitude() * 1e6));
        OverlayItem overlayItem = new OverlayItem(point, "User Name", "Latitude : " + location.getLatitude() + ", Longitude : " + location.getLongitude());
        
        drawable = this.getResources().getDrawable(R.drawable.androidmarkerred);
        CustomItemizedOverlay itemizedOverlay2 = new CustomItemizedOverlay(drawable, CourseStepActivity.this);
        itemizedOverlay2.addOverlay(overlayItem);
        mapOverlays.add(itemizedOverlay2);
	}
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
		   MapOverlay mapOverlay = new MapOverlay(mRoads.get(msg.what), mapView);
		   mapOverlays.add(mapOverlay);
		   //listOfOverlays.clear();
		   mapView.invalidate();
		   //mapController.setZoom(14);
		   Log.d("mHandler", "route tracee");
		};
	};
	
	
	private InputStream getConnection(String url) {
		  InputStream is = null;
		  try {
		   URLConnection conn = new URL(url).openConnection();
		   is = conn.getInputStream();
		  } catch (MalformedURLException e) {
		   e.printStackTrace();
		  } catch (IOException e) {
		   e.printStackTrace();
		  }
		  return is;
    }
	    
    protected List<Placemark> getPlaceMarks() {
        
    	Bundle bundle = getIntent().getExtras();
    	String course_url = bundle.getString(Course.URL_EXTRA);
    	
    	course = CourseLoader.getInstance().parse(course_url);
    	type = course.getType();
    	
    	return course.getPlacemarks();
    }
    
    
    public void updateMap() {
        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.maps_icon);
        itemizedOverlay = new CustomItemizedOverlay(drawable, this);
        itemizedOverlay_currentPoint = new CustomItemizedOverlay(drawable, this);
        
        mapOverlays.clear();
        /***** load overlays ******/
        
        String[] formerPoint = null;
        int i = 0;
        
        for(Placemark placemark: placemarks){
        	
        	String[] lL = placemark.getPoint().getCoordinates().split(",");
        	int l = (new Double(Double.parseDouble(lL[1])* 1000000)).intValue();
        	int L = (new Double(Double.parseDouble(lL[0])* 1000000)).intValue();
        	Log.d(getLocalClassName(), String.valueOf(l) + " " + String.valueOf(L));
        	GeoPoint point = new GeoPoint(l,L);
        	if (i - currentPlacemark >=-1) {
        		OverlayItem overlayItem = new OverlayItem(point, placemark.getName(),placemark.getDescription());
        	
	        	if(i==currentPlacemark){
	        		Drawable d = this.getResources().getDrawable(R.drawable.maps_icon_current);
	                itemizedOverlay_currentPoint = new CustomItemizedOverlay(d, this);
	                itemizedOverlay_currentPoint.addOverlay(overlayItem);
	                mapOverlays.add(itemizedOverlay_currentPoint);
	        	} else {
	            	itemizedOverlay.addOverlay(overlayItem);
	        	}
        	}
        	i++;
        	
        	
        	/***** load routes *****/
        	if(formerPoint != null) {
        		if (i - currentPlacemark >0){
	        		roadConnectionThread t = new roadConnectionThread() {
		    			   @Override
		    			   public void run() {
		    				   String url = RoadProvider.getUrl(fromLat, fromLon, toLat, toLon);
		    				   InputStream is = getConnection(url);
		    				   if (mRoads == null) {
		    					   mRoads = new ArrayList<Road>();
		    				   }
		    				   mRoads.add(RoadProvider.getRoute(is));
		    				   mHandler.sendEmptyMessage(mRoads.size() - 1);
		    			   }
		    		};
		    		t.setCoord(formerPoint, lL);
	        		t.start();
        		}
        	} else {
        		mapController.animateTo(point);
        	}
        	formerPoint = lL;

        }
        mapOverlays.add(itemizedOverlay);
    	
    }
	    
    public void updatePlacemark() {      	
    	Log.d("updateReceiver","Receiver Update");
    	Log.d("placemark size","placemark size "+placemarks.size());
    	      	
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);       
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            	
    	if(!placemarks.isEmpty()){

			updateMap();
    		if(currentPlacemark==-1){
    			AlertDialog.Builder dialog = new AlertDialog.Builder(this);

				dialog.setTitle(course.getName());
				dialog.setMessage(course.getPresentation());
				dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
							
						public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
									updatePlacemark();
									dialog.dismiss();
							}
						});
				dialog.show();
    			
    			currentPlacemark++;
    		} else if(currentPlacemark<placemarks.size()){
		    	//Present the new objective with its description
		       	Placemark item = placemarks.get(currentPlacemark);
		    	
		       	AlertDialog.Builder dialog = new AlertDialog.Builder(this);

				dialog.setTitle(item.getName());
				dialog.setMessage(item.getDirection());
				dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
							
						public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
									dialog.dismiss();
							}
						});
				dialog.show();
					
		     	receiverLocalization = new BroadcastReceiver() {			
					@Override
					public void onReceive(Context context, Intent intent) {			    	
					   	unregisterReceiver(receiverLocalization);    					
					    Log.d(PROXIMITY_RECEIVER,"Proximity Alert");
						displayNotification();
					}
	    		};
	    		
		    	IntentFilter intentFilter = new IntentFilter(PROXIMITY_INTENT);  	
		    	registerReceiver(receiverLocalization, intentFilter);
	    	
	    		Intent intent = new Intent(PROXIMITY_INTENT);
	            PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
	            
	            String[] lL = placemarks.get(currentPlacemark).getPoint().getCoordinates().split(",");
	        	double l = Double.parseDouble(lL[1]);	        	
	        	double L = Double.parseDouble(lL[0]);
	        		        	
	        	Log.d(TAG,"latl"+l+"latPl"+placemarks.get(currentPlacemark).getPoint().getLatitude());
	            
	            locationManager.addProximityAlert(
	            		placemarks.get(currentPlacemark).getPoint().getLatitude(), // the latitude of the central point of the alert region
	            		placemarks.get(currentPlacemark).getPoint().getLongitude(), // the longitude of the central point of the alert region
	            		POINT_RADIUS, // the radius of the central point of the alert region, in meters
	            		PROX_ALERT_EXPIRATION, // time for this proximity alert, in milliseconds, or -1 to indicate no expiration 
	            		proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
	           );
	            	            
				Log.d(PROXIMITY_INTENT,"Alert Proximity Set for lat :"+placemarks.get(currentPlacemark).getPoint().getLatitude()+", long : "+placemarks.get(currentPlacemark).getPoint().getLongitude());

		    } else {
		    	//End of the course 
		    	SharedPreferences settings = getSharedPreferences(HomeActivity.PREF_FILE, 0);    	
		    	SharedPreferences.Editor editor = settings.edit();
				editor.remove(Course.PREFERENCES_STARTED_URL);
				editor.remove(Course.PREFERENCES_STARTED_TIME_STARTED);
				editor.remove(Course.PREFERENCES_STARTED_ID);		    	
		    	
		    	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
				
				dialog.setTitle(R.string.course_finished_title);
				dialog.setMessage(course.getEnd());
				dialog.setPositiveButton(R.string.course_finished_button_ok, new DialogInterface.OnClickListener() {
							
					public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
							dialog.dismiss();
					}
				});
				dialog.show();
		    }
    	} 
    }


	private void displayNotification() {
		
		//check for class in foreground
		Context context = getBaseContext();
		ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
		String packageName = am.getRunningTasks(1).get(0).topActivity.getPackageName();
		String className = am.getRunningTasks(1).get(0).topActivity.getClassName();
		
		if(!className.equals(CourseDetailsActivity.class.getName())) {
			//send notification in not in foreground
				NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				Notification notification = new Notification(R.drawable.ic_launcher,placemarks.get(currentPlacemark).getName(), System.currentTimeMillis());
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				notification.number += 1;
				
				Intent i = new Intent(getBaseContext(),CourseStepActivity.class);
				i.putExtra(Course.ID_EXTRA, course.getId());
		        i.putExtra(Course.URL_EXTRA, course.getUrl());
				
				PendingIntent activity = PendingIntent.getActivity(getBaseContext(), 0, i,0);
				notification.setLatestEventInfo(getBaseContext(), placemarks.get(currentPlacemark).getName(),placemarks.get(currentPlacemark).getName(), activity);
				notificationManager.notify(0, notification);
		} else {		
				AlertDialog.Builder dialog = new AlertDialog.Builder(this);
				
				if(type.equals(Course.TYPE_GAME)) {
					dialog.setTitle(placemarks.get(currentPlacemark).getName());
					dialog.setMessage(placemarks.get(currentPlacemark).getGreetings());
					dialog.setPositiveButton(R.string.game_play, new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
						          Intent gameActivity = new Intent(getApplicationContext(),GameActivity.class);
						          gameActivity.putExtra(Course.URL_EXTRA, course.getUrl());
						          gameActivity.putExtra(Course.CURRENT_PLACEMARK, currentPlacemark);
						          startActivity(gameActivity);
						          dialog.dismiss();
							}
						});
				} else {
					dialog.setTitle(placemarks.get(currentPlacemark).getName());
					dialog.setMessage(placemarks.get(currentPlacemark).getDescription());
					dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							currentPlacemark++;
					        updatePlacemark();
					        dialog.dismiss();
						}
					});

				}
				
				dialog.show();
		}
	}
	
	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			s = ((LocalizationService.MyBinder) binder).getService();
			Log.d("Connect","Connected Home Activity");
		}

		public void onServiceDisconnected(ComponentName className) {
			s = null;
		}
	};

	void doBindService() {
		Log.d("Connect","do Bind");
		bindService(new Intent(this, LocalizationService.class), mConnection,
				Context.BIND_AUTO_CREATE);
	}
	

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.coursestep, menu);

	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.pause:
	        	return true;
	        case R.id.help:
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void nextPlacemark(View view) {
		currentPlacemark++;
		updatePlacemark();
	}
	public void previousPlacemark(View view){
		currentPlacemark--;
		updatePlacemark();
	}
	public void pauseGame(View view) {
		Log.d("pause", "pause");
	}
  
}

class MapOverlay extends com.google.android.maps.Overlay {
    Road mRoad;
    ArrayList<GeoPoint> mPoints;

    public MapOverlay(Road road, MapView mv) {
            mRoad = road;
            if (road.mRoute.length > 0) {
                    mPoints = new ArrayList<GeoPoint>();
                    for (int i = 0; i < road.mRoute.length; i++) {
                            mPoints.add(new GeoPoint((int) (road.mRoute[i][1] * 1000000),
                                            (int) (road.mRoute[i][0] * 1000000)));
                    }
//                    int moveToLat = (mPoints.get(0).getLatitudeE6() + (mPoints.get(
//                                    mPoints.size() - 1).getLatitudeE6() - mPoints.get(0)
//                                    .getLatitudeE6()) / 2);
//                    int moveToLong = (mPoints.get(0).getLongitudeE6() + (mPoints.get(
//                                    mPoints.size() - 1).getLongitudeE6() - mPoints.get(0)
//                                    .getLongitudeE6()) / 2);
//                    GeoPoint moveTo = new GeoPoint(moveToLat, moveToLong);

//                    MapController mapController = mv.getController();
//                    mapController.animateTo(moveTo);
//                    mapController.setZoom(7);
            }
    }

    @Override
    public boolean draw(Canvas canvas, MapView mv, boolean shadow, long when) {
            super.draw(canvas, mv, shadow);
            drawPath(mv, canvas);
            return true;
    }

    public void drawPath(MapView mv, Canvas canvas) {
            int x1 = -1, y1 = -1, x2 = -1, y2 = -1;
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);
            for (int i = 0; i < mPoints.size(); i++) {
                    android.graphics.Point point = new android.graphics.Point();
                    mv.getProjection().toPixels(mPoints.get(i), point);
                    x2 = point.x;
                    y2 = point.y;
                    if (i > 0) {
                            canvas.drawLine(x1, y1, x2, y2, paint);
                    }
                    x1 = x2;
                    y1 = y2;
            }
    }
        
}


class roadConnectionThread extends Thread {
 	   double fromLat, fromLon; 
 	   double toLat, toLon;
	
 	   public void setCoord(String[] from, String[] to) {
 		   this.fromLat = Double.parseDouble(from[1]);
 		   this.fromLon = Double.parseDouble(from[0]);
 		   this.toLat = Double.parseDouble(to[1]);
 		   this.toLon = Double.parseDouble(to[0]);
 	   }
}


class MyLocationListener implements LocationListener {
    public void onLocationChanged(Location location) {
		Log.d("Location", "New user position : (" + location.getLatitude() + ", " + location.getLongitude() + ")");	
    }
    public void onStatusChanged(String s, int i, Bundle b) {            
    }
    public void onProviderDisabled(String s) {
    }
    public void onProviderEnabled(String s) {            
    }
}
