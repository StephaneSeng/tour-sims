package com.toursims.mobile;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Service used for tracking the user position if they want to share their position
 */
public class LocalizationService extends Service {

	/**
	 * Android debugging tag
	 */
	private static final String TAG = LocalizationService.class.getName();
	
	/**
	 * User preferences variables for this service
	 * The minimum time interval between two localization requests (ms)
	 */
	private long minUpdateTime;
	
	/**
	 * User preferences variables for this service
	 * The maximum time interval between two localization requests (ms)
	 */
	private long maxUpdateTime;
	
	/**
	 * User preferences variables for this service
	 * The minimum distance between two user positions (m)
	 */
	private long minUpdateDistance;
	
	/**
	 * Android LocationManager which enables us to access to the system location services
	 */
	private LocationManager locationManager;
	
	/**
	 * Android criteria for choosing the right position provider
	 */
	private Criteria criteria;
	
	/**
	 * Android current provider for the localization
	 */
	private String bestProvider;
	
	/**
	 * GPS Listener class used for retrieving the user positions
	 */
	private LocalizationListener localizationListener;
	
	/**
	 * Timer used for updating the user current position
	 */
	private Timer timer;

	/**
	 * Last user position
	 */
	private Location lastLocation;
	
	/**
	 * Last user position update time
	 */
	private Calendar lastUpdateTime;
	
	/**
	 * Default event handler for an Android service
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	/**
	 * Initialize the service attributes
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.d(TAG, "The LocalizationService is starting...");
		
		// TODO : Retrieve the user preferences
		minUpdateTime = 1 * 30 * 1000; // 30 seconds
		maxUpdateTime = 1 * 60 * 1000; // 1 minute
		minUpdateDistance = 0; // 0 m
		
		// Get access to the localization service
		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		
		// Try to get the best localization provider
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		bestProvider = locationManager.getBestProvider(criteria, false);
		Log.d(TAG, "Best current localization provider : " + bestProvider);
		
		// Initialization
		localizationListener = new LocalizationListener();
		timer = new Timer();
		lastUpdateTime = Calendar.getInstance();
		
		// Set the timer used for the updates of the user position
		locationManager.requestLocationUpdates(bestProvider, 0, 0, localizationListener);
		locationManager.requestLocationUpdates(bestProvider, minUpdateTime, minUpdateDistance, localizationListener);
		timer.schedule(new LocalizationTimer(), 0, maxUpdateTime);
		
		Log.d(TAG, "The LocalizationService has been started");
	}

	/**
	 * Destruction of the service attributes
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		// Liberation of the instanced variables
		timer.cancel();
		
		Log.d(TAG, "The LocalizationService has be destroyed");
	}
	
	/**
	 * Subclass !
	 * Listen to the GPS sensor events, register the new user positions
	 */
	private final class LocalizationListener implements LocationListener {
		
		/**
		 * Event handler called when the location has changed
		 */
		public void onLocationChanged(Location location) {
			Log.d(TAG, "The GPS location will be updated");
			Log.d(TAG, "New location : " + location);
			
			if (location != null) {
				// Update the current user location
				lastLocation = location;
				lastUpdateTime = Calendar.getInstance();
				
				Log.d(TAG, "New user position : (" + location.getLatitude() + ", " + location.getLongitude() + ")");	
			}
		}

		/**
		 * Event handler called when a position provider is disabled by the user
		 */
		public void onProviderDisabled(String provider) {
			Log.d(TAG, "The GPS provider has been disabled");
			
			// Update the best provider
			bestProvider = locationManager.getBestProvider(criteria, false);
		}

		/**
		 * Event handler called when a position provider is enabled by the user
		 */
		public void onProviderEnabled(String provider) {
			Log.d(TAG, "The GPS provider has been enabled");
			
			// Update the best provider
			bestProvider = locationManager.getBestProvider(criteria, false);
		}

		/**
		 * Event handler called when the GPS provider status has changed without the knowledge of the user
		 */
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.d(TAG, "The GPS provider status has changed to " + status);
			
			// Update the best provider
			bestProvider = locationManager.getBestProvider(criteria, false);
		}
		
	}
	
	/**
	 * Subclass !
	 * Task which will be called to update the user position when it is necessary 
	 */
	private final class LocalizationTimer extends TimerTask {
		
		/**
		 * Default constructor
		 */
		public LocalizationTimer() {
			Log.d(TAG, "A LocalizationTimer has been created");
		}
		
		/**
		 * Core method of the task
		 */
		@Override
		public void run() {
			Calendar now = Calendar.getInstance();
			long timeInterval = now.getTimeInMillis() - lastUpdateTime.getTimeInMillis(); 
			Log.d(TAG, "Current time interval : " + timeInterval + " ms");
			
			// Check if the user position have to be updated
			if (timeInterval >= maxUpdateTime) {
				Log.d(TAG, "The maximum update time threshold has been exceeded");
				
				// The user position is updated
				localizationListener.onLocationChanged(locationManager.getLastKnownLocation(bestProvider));
			}
		}
		
	}
	
}
