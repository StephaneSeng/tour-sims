package com.toursims.mobile;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import com.toursims.mobile.model.kml.Placemark;
import com.toursims.mobile.model.kml.Point;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocalizationService extends Service {
	
	private final IBinder mBinder = new MyBinder();
	private Timer timer = new Timer();

	private static final String TAG = LocalizationService.class.getName();
	private static final String PROXIMITY_INTENT = TAG+".PROXIMITY_INTENT";

	private static final long UPDATE_INTERVAL = 5000;
	private long minUpdateTime = 1 * 1 * 1000; // 30 seconds
	private long maxUpdateTime = 1 * 5 * 1000; // 1 minute
	private long minUpdateDistance  = 0; // 0 m
	private LocationManager locationManager;
	private Criteria criteria;
	private String bestProvider;
	private LocalizationListener localizationListener;
	private Location lastLocation;
	private Calendar lastUpdateTime;
	private PendingIntent pendingIntentForProximityAlert = null;
	private static final long expiration = 600000;
	
	private static final float radius = 100f;
		
	@Override
	public void onCreate() {
		super.onCreate();
			
		Log.d(TAG, "The LocalizationService is starting...");
		
		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		bestProvider = locationManager.getBestProvider(criteria, false);
		bestProvider = LocationManager.GPS_PROVIDER;
		
		Log.d(TAG, "Best current localization provider : " + bestProvider);
		
		localizationListener = new LocalizationListener();
		lastUpdateTime = Calendar.getInstance();
		
		locationManager.requestLocationUpdates(bestProvider, 0, 0, localizationListener);
		locationManager.requestLocationUpdates(bestProvider, minUpdateTime, minUpdateDistance, localizationListener);
		
		localizationTask();		
		Log.d(TAG, "The LocalizationService has been started");
		

		
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub		
		Bundle b = intent.getExtras();
		
		if(b!=null){
			if(b.containsKey(Point.LATITUDE)&&b.containsKey(Point.LONGITUDE)&&b.containsKey(Placemark.NAME)){
				setProximityAlert(b.getDouble(Point.LATITUDE),
						b.getDouble(Point.LONGITUDE),
						0, 
						0, 
						PROXIMITY_INTENT);
			
			BroadcastReceiver r = new BroadcastReceiver() {
				
				@Override
				public void onReceive(Context context, Intent intent) {
					// TODO Auto-generated method stub
					Log.d(PROXIMITY_INTENT,"Proximity Intent received from Service");
				}
			};
			
	    	IntentFilter intentFilter = new IntentFilter(PROXIMITY_INTENT);  	
	    	registerReceiver(r, intentFilter);
			
			
			} else {
				Log.d(TAG,"Missing Arguments");
			}	
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	private void localizationTask() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				localizationListener.onLocationChanged(locationManager.getLastKnownLocation(bestProvider));
				Intent intent=new Intent();
				intent.setAction("TIMER");
				sendBroadcast(intent);
			}
		}, 0, UPDATE_INTERVAL);
		Log.d(getClass().getSimpleName(), "Timer started.");
	}
		
	public void setProximityAlert(double lat, double lon, final long eventID, int requestCode, String proximityIntentAction)
	{
		if(pendingIntentForProximityAlert!=null){
			locationManager.removeProximityAlert(pendingIntentForProximityAlert);
		}
		
		Intent intent = new Intent(proximityIntentAction);
		pendingIntentForProximityAlert = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		locationManager.addProximityAlert(lat, lon, radius, expiration, pendingIntentForProximityAlert);
		
		Log.d("setProximityAlert","An alert has been set for :"+lat+","+lon);		
	}
	
	private final class LocalizationListener implements LocationListener {
		
		public void onLocationChanged(Location location) {
		//	Log.d(TAG, "The GPS location will be updated");
			Log.d(TAG, "New location : " + location);
			
			if (location != null) {
				// Update the current user location
				lastLocation = location;
				lastUpdateTime = Calendar.getInstance();
				
				Log.d(TAG, "New user position : (" + location.getLatitude() + ", " + location.getLongitude() + ")");	
			}
		}

		public void onProviderDisabled(String provider) {
			Log.d(TAG, "The GPS provider has been disabled");
			bestProvider = locationManager.getBestProvider(criteria, false);
			bestProvider = LocationManager.GPS_PROVIDER;
		}

		public void onProviderEnabled(String provider) {
			Log.d(TAG, "The GPS provider has been enabled");
			bestProvider = locationManager.getBestProvider(criteria, false);
			bestProvider = LocationManager.GPS_PROVIDER;
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.d(TAG, "The GPS provider status has changed to " + status);		
			bestProvider = locationManager.getBestProvider(criteria, false);
			bestProvider = LocationManager.GPS_PROVIDER;
		}
		
	}
	 
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	public class MyBinder extends Binder {
		LocalizationService getService() {
			return LocalizationService.this;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();		
		Log.d(TAG, "The LocalizationService has be destroyed");
	}	
	
}
