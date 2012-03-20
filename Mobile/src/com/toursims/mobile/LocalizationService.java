package com.toursims.mobile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.controller.UserWrapper;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.Trace;
import com.toursims.mobile.model.kml.Placemark;
import com.toursims.mobile.model.kml.Point;

public class LocalizationService extends Service {

	private static final String TAG = LocalizationService.class.getName();
	private static final String PROXIMITY_INTENT = TAG + ".PROXIMITY_INTENT";
	private static final long expiration = -1;
	private static final float radius = 250f;
	private static final long MINIMUM_DISTANCECHANGE_FOR_UPDATE = 1; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATE = 10 * 1000; // in Milliseconds

	private static String fileString = new String();
	private static Location knownLocation;
	private static boolean recording = false;
	private static Course course;
	private static String filename;

	private final IBinder mBinder = new MyBinder();

	private LocationManager locationManager;
	private LocalizationListener gpsLocationListener;
	private LocalizationListener networkLocationListener;
	private Criteria criteria;
	private String bestProvider;
	private PendingIntent pendingIntentForProximityAlert = null;
	private UserWrapper userWrapper;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "The LocalizationService is starting...");

		userWrapper = new UserWrapper(getApplicationContext());
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		// Retrive the currently best provider
		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		bestProvider = locationManager.getBestProvider(criteria, true);
		Log.d(TAG, "Best current localization provider : " + bestProvider);
		
		// Suscribe to the provider events
		gpsLocationListener = new LocalizationListener();
		networkLocationListener = new LocalizationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, gpsLocationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, networkLocationListener);
		
		Log.d(TAG, "The LocalizationService has been started");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			Bundle b = intent.getExtras();
	
			if (b != null) {
				if (b.containsKey(Point.LATITUDE) && b.containsKey(Point.LONGITUDE) && b.containsKey(Placemark.NAME)) {
					setProximityAlert(b.getDouble(Point.LATITUDE), b.getDouble(Point.LONGITUDE), 0, 0, PROXIMITY_INTENT);
	
					BroadcastReceiver r = new BroadcastReceiver() {
						@Override
						public void onReceive(Context context, Intent intent) {
							Log.d(PROXIMITY_INTENT, "Proximity Intent received from Service");
							
							// Debug
							// new CourseStepActivity().displayNotification();
						}
					};
	
					IntentFilter intentFilter = new IntentFilter(PROXIMITY_INTENT);
					registerReceiver(r, intentFilter);
				} else {
					Log.d(TAG, "Missing Arguments");
				}
			}
		}
	
		return super.onStartCommand(intent, flags, startId);
	}

	public void setProximityAlert(double lat, double lon, final long eventID, int requestCode, String proximityIntentAction) {
		if (pendingIntentForProximityAlert != null) {
			locationManager.removeProximityAlert(pendingIntentForProximityAlert);
		}

		Intent intent = new Intent(proximityIntentAction);
		pendingIntentForProximityAlert = PendingIntent.getBroadcast(getApplicationContext(), requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		locationManager.addProximityAlert(lat, lon, radius, expiration, pendingIntentForProximityAlert);

		Log.d("setProximityAlert", "An alert has been set for :" + lat + "," + lon);
	}

	private final class LocalizationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			Log.d(TAG, "New location : " + location);

			if (location != null) {
				knownLocation = location;

				if (knownLocation != null) {
					recordLocation(knownLocation);
				}

				// Create a checkin if possible
				TourSims tourSims = (TourSims) getApplicationContext();
				if (tourSims.isUserLoggedIn()) {
					Log.d(TAG, "Create a checkin");
					userWrapper.CreateCheckin(location.getLatitude(), location.getLongitude(), tourSims.getUser().getUserId());
				}

				Log.d(TAG, "New user position : (" + location.getLatitude() + ", " + location.getLongitude() + ")");
			}
		}

		public void onProviderDisabled(String provider) {
			Log.d(TAG, "A provider has been disabled");
			bestProvider = locationManager.getBestProvider(criteria, true);
			if (bestProvider != null) {
				if (bestProvider.equals(LocationManager.GPS_PROVIDER)) {
					locationManager.requestLocationUpdates(bestProvider, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, gpsLocationListener);
				} else if (bestProvider.equals(LocationManager.NETWORK_PROVIDER)) {
					locationManager.requestLocationUpdates(bestProvider, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, networkLocationListener);
				}
			}
			Log.d(TAG, "Best current localization provider : " + bestProvider);
		}

		public void onProviderEnabled(String provider) {
			Log.d(TAG, "A provider has been enabled");
			bestProvider = locationManager.getBestProvider(criteria, true);
			if (bestProvider != null) {
				if (bestProvider.equals(LocationManager.GPS_PROVIDER)) {
					locationManager.requestLocationUpdates(bestProvider, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, gpsLocationListener);
				} else if (bestProvider.equals(LocationManager.NETWORK_PROVIDER)) {
					locationManager.requestLocationUpdates(bestProvider, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, networkLocationListener);
				}
			}
			Log.d(TAG, "Best current localization provider : " + bestProvider);
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.d(TAG, "A provider status has changed to " + status);
			bestProvider = locationManager.getBestProvider(criteria, true);
			if (bestProvider != null) {
				if (bestProvider.equals(LocationManager.GPS_PROVIDER)) {
					locationManager.requestLocationUpdates(bestProvider, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, gpsLocationListener);
				} else if (bestProvider.equals(LocationManager.NETWORK_PROVIDER)) {
					locationManager.requestLocationUpdates(bestProvider, MINIMUM_TIME_BETWEEN_UPDATE, MINIMUM_DISTANCECHANGE_FOR_UPDATE, networkLocationListener);
				}
			}
			Log.d(TAG, "Best current localization provider : " + bestProvider);
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
		locationManager.removeUpdates(gpsLocationListener);
		locationManager.removeUpdates(networkLocationListener);
		Log.d(TAG, "The LocalizationService has be destroyed");
		
		this.stopSelf();
		super.onDestroy();
	}

	public void recordLocation(Location location) {
		if (recording) {
			try {
				fileString += location.getTime() + "," + location.getLatitude() + "," + location.getLongitude() + "\n";
				Placemark p = new Placemark(location.getLongitude(), location.getLatitude(), Calendar.getInstance().getTime().toLocaleString());
				if (course == null) {
					course = new Course();
				}

				if (course.getPlacemarks().size() > 0) {
					course.getPlacemarks().get(course.getPlacemarks().size() - 1).getTimeSpan().setEnd(Calendar.getInstance().getTime().toLocaleString());
				}

				course.addPlacemark(p);

				if (fileString.length() > 500) {
					writeFile(fileString, ".log");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void stopRecording() {
		recording = false;
		if (course != null) {
			writeFile(course.toKml(), "kml");
		}
	}

	public void writeFile(String stringFile, String fileNameExt) {
		SharedPreferences settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);
		Long startedTime = settings.getLong(CustomPreferences.RECORDING_RIGHT_NOW, -1);

		try {
			FileOutputStream fos = openFileOutput(filename, Context.MODE_APPEND);
			fos.write(stringFile.getBytes());
			fos.close();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}
	}

	public void startRecording(String name) {
		CourseBDD datasource = null;
		try {
			recording = true;
			recordLocation(knownLocation);
			course = new Course();
			Trace item = new Trace();

			SharedPreferences settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);
			Long startedTime = settings.getLong(CustomPreferences.RECORDING_RIGHT_NOW, -1);
			filename = "/data/data/com.toursims.mobile/files/trace_" + startedTime.toString() + ".kml";

			item.setFile(filename);
			item.setMillis(startedTime);
			item.setName(name);

			filename = "trace_" + startedTime.toString() + ".kml";

			datasource = new CourseBDD(this);
			datasource.open();
			datasource.insertTrace(item);
			datasource.close();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}
		
		if (datasource != null) {
			datasource.close();
		}
	}

	public Location getKnownLocation() {
		return knownLocation;
	}

}