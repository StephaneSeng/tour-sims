package com.toursims.mobile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

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

	private final IBinder mBinder = new MyBinder();
	private Timer timer = new Timer();

	private static final String TAG = LocalizationService.class.getName();
	private static final String PROXIMITY_INTENT = TAG + ".PROXIMITY_INTENT";

	private static final long UPDATE_INTERVAL = 10000;
	// private long minUpdateTime = 1 * 1 * 1000; // 30 seconds
	// private long maxUpdateTime = 1 * 5 * 1000; // 1 minute
	// private long minUpdateDistance = 0; // 0 m
	private LocationManager locationManager;
	private Criteria criteria;
	private String bestProvider;
	private LocalizationListener localizationListener;
	// private Location currentLocation;
	// private Location lastLocation;
	private PendingIntent pendingIntentForProximityAlert = null;
	private static final long expiration = 600000;
	private static String fileString = new String();
	private static Location knownLocation;
	private static boolean recording = false;
	private static Course course;
	private static String filename;

	private static final float radius = 100f;

	private UserWrapper userWrapper;

	@Override
	public void onCreate() {
		super.onCreate();

		userWrapper = new UserWrapper(getApplicationContext());

		// c = Calendar.getInstance();

		Log.d(TAG, "The LocalizationService is starting...");

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		bestProvider = locationManager.getBestProvider(criteria, true);
		// bestProvider = LocationManager.GPS_PROVIDER;

		Log.d(TAG, "Best current localization provider : " + bestProvider);

		localizationListener = new LocalizationListener();
		// lastUpdateTime = Calendar.getInstance();

		if (!bestProvider.equals("null")) {
			locationManager.requestLocationUpdates(bestProvider, 0, 0,
					localizationListener);
			// locationManager.requestLocationUpdates(bestProvider,
			// minUpdateTime, minUpdateDistance, localizationListener);
		}

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		knownLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new LocalizationListener());

		localizationTask();
		Log.d(TAG, "The LocalizationService has been started");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Bundle b = intent.getExtras();

		if (b != null) {
			if (b.containsKey(Point.LATITUDE) && b.containsKey(Point.LONGITUDE)
					&& b.containsKey(Placemark.NAME)) {
				setProximityAlert(b.getDouble(Point.LATITUDE),
						b.getDouble(Point.LONGITUDE), 0, 0, PROXIMITY_INTENT);

				BroadcastReceiver r = new BroadcastReceiver() {

					@Override
					public void onReceive(Context context, Intent intent) {
						// TODO Auto-generated method stub
						Log.d(PROXIMITY_INTENT,
								"Proximity Intent received from Service");
					}
				};

				IntentFilter intentFilter = new IntentFilter(PROXIMITY_INTENT);
				registerReceiver(r, intentFilter);

			} else {
				Log.d(TAG, "Missing Arguments");
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	private void localizationTask() {
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// Log.d("TAG","Timer");
			}
		}, 0, UPDATE_INTERVAL);
		Log.d(getClass().getSimpleName(), "Timer started.");
	}

	public void setProximityAlert(double lat, double lon, final long eventID,
			int requestCode, String proximityIntentAction) {
		if (pendingIntentForProximityAlert != null) {
			locationManager
					.removeProximityAlert(pendingIntentForProximityAlert);
		}

		Intent intent = new Intent(proximityIntentAction);
		pendingIntentForProximityAlert = PendingIntent.getBroadcast(
				getApplicationContext(), requestCode, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		locationManager.addProximityAlert(lat, lon, radius, expiration,
				pendingIntentForProximityAlert);

		Log.d("setProximityAlert", "An alert has been set for :" + lat + ","
				+ lon);
	}

	private final class LocalizationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			Log.d(TAG, "New location : " + location);

			if (location != null) {
				// recordLocation(location);
				knownLocation = location;

				if (knownLocation != null) {
					recordLocation(knownLocation);
				}
				// Create a checkin
				TourSims tourSims = (TourSims) getApplicationContext();
				if (tourSims.isUserLoggedIn()) {
					Log.d(TAG, "Create a checkin");
					userWrapper.CreateCheckin(location.getLatitude(), location
							.getLongitude(), tourSims.getUser().getUserId());
				}

				Log.d(TAG, "New user position : (" + location.getLatitude()
						+ ", " + location.getLongitude() + ")");
			}
		}

		public void onProviderDisabled(String provider) {
			Log.d(TAG, "The GPS provider has been disabled");
			bestProvider = locationManager.getBestProvider(criteria, true);
			// bestProvider = LocationManager.GPS_PROVIDER;
			Log.d(TAG, "Best current localization provider : " + bestProvider);
		}

		public void onProviderEnabled(String provider) {
			Log.d(TAG, "The GPS provider has been enabled");
			bestProvider = locationManager.getBestProvider(criteria, true);
			// bestProvider = LocationManager.GPS_PROVIDER;
			Log.d(TAG, "Best current localization provider : " + bestProvider);
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.d(TAG, "The GPS provider status has changed to " + status);
			bestProvider = locationManager.getBestProvider(criteria, true);
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
		super.onDestroy();
		timer.cancel();
		Log.d(TAG, "The LocalizationService has be destroyed");
	}

	public void recordLocation(Location location) {
		if (recording) {
			try {
				fileString += location.getTime() + "," + location.getLatitude()
						+ "," + location.getLongitude() + "\n";
				Placemark p = new Placemark(location.getLongitude(),
						location.getLatitude());
				if (course == null) {
					course = new Course();
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
		SharedPreferences settings = getSharedPreferences(
				CustomPreferences.PREF_FILE, 0);
		Long startedTime = settings.getLong(
				CustomPreferences.RECORDING_RIGHT_NOW, -1);
		try {
			FileOutputStream fos = openFileOutput(filename, Context.MODE_APPEND);
			fos.write(stringFile.getBytes());
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startRecording(String name) {
		try {
			recording = true;
			recordLocation(knownLocation);
			course = new Course();
			Trace item = new Trace();

			SharedPreferences settings = getSharedPreferences(
					CustomPreferences.PREF_FILE, 0);
			Long startedTime = settings.getLong(
					CustomPreferences.RECORDING_RIGHT_NOW, -1);
			filename = "/data/data/com.toursims.mobile/files/trace_"
					+ startedTime.toString() + ".kml";

			item.setFile(filename);
			item.setMillis(startedTime);
			item.setName(name);

			filename = "trace_" + startedTime.toString() + ".kml";
			
			CourseBDD datasource;
			datasource = new CourseBDD(this);
			datasource.open();
			datasource.insertTrace(item);
			datasource.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Location getKnownLocation() {
		return knownLocation;
	}
}