package com.toursims.mobile;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.toursims.mobile.controller.UserWrapper;
import com.toursims.mobile.model.Checkin;
import com.toursims.mobile.model.User;
import com.toursims.mobile.ui.utils.CheckinOverlay;
import com.toursims.mobile.ui.utils.CheckinOverlayItem;
import com.toursims.mobile.ui.utils.UserOverlay;
import com.toursims.mobile.ui.utils.UserOverlayItem;

public class SocialActivity extends SherlockMapActivity {

	/**
	 * Android debugging tag
	 */
	private static final String TAG = SocialActivity.class.toString();

	/**
	 * Shared object
	 */
	private MapView mapView;
	private MapController mapController;
	private List<Overlay> mapOverlays;
	private Drawable drawable;

	/**
	 * Called when the activity is first created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social);

		// Initialize the share attributes
		mapView = (MapView) findViewById(R.id.socialMapView);
		mapController = mapView.getController();
		mapOverlays = mapView.getOverlays();

		// Set the MapView properties
		mapView.setBuiltInZoomControls(true);
		mapController.setZoom(16); // Zoom 1 is world view

		// ActionBarSherlock setup
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(R.drawable.ic_menu_globe_colored);
		actionBar.setTitle(R.string.home_social_map);
	}

	/**
	 * Called when the activity is resumed
	 */
	@Override
	protected void onResume() {
		super.onResume();

		new DownloadTask().execute();
	}

	private class DownloadTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			setSupportProgressBarIndeterminateVisibility(true);

			mapOverlays.clear();
			mapView.refreshDrawableState();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Get the current user position
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			// Try to get the best localization provider
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			String bestProvider = locationManager.getBestProvider(criteria, true);

			Location lastLocation = null;
			if (bestProvider != null) {
				lastLocation = locationManager.getLastKnownLocation(bestProvider);
			}

			if (lastLocation != null) {
				Log.d(TAG, "Test : " + lastLocation);

				// Call the SearchCheckins method
				UserWrapper userWrapper = new UserWrapper(getApplicationContext());
				TourSims tourSims = (TourSims) getApplicationContext();
				List<Checkin> checkins = userWrapper.SearchCheckins(lastLocation.getLatitude(), lastLocation.getLongitude(), tourSims.getUser().getUserId());

				// Display the map with the user at its center
				mapController.animateTo(new GeoPoint((int) (lastLocation.getLatitude() * 1e6), (int) (lastLocation.getLongitude() * 1e6)));

				// Display the user
				displayUser(lastLocation);

				// Display the points of interests
				displayCheckins(checkins);
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mapView.invalidate();
			setSupportProgressBarIndeterminateVisibility(false);
		}
	}

	/**
	 * Method to override with the MapActivity class
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * Display the user on the MapView
	 */
	protected void displayUser(Location location) {
		GeoPoint point = new GeoPoint((int) (location.getLatitude() * 1e6), (int) (location.getLongitude() * 1e6));
		TourSims tourSims = (TourSims) getApplicationContext();
		User user = tourSims.getUser();
		UserOverlayItem userOverlayItem = new UserOverlayItem(point, "", "Latitude : " + location.getLatitude() + ", Longitude : " + location.getLongitude(), user);

		drawable = this.getResources().getDrawable(R.drawable.androidmarkerred);
		UserOverlay userOverlay = new UserOverlay(drawable, SocialActivity.this);
		userOverlay.addOverlay(userOverlayItem);
		mapOverlays.add(userOverlay);
	}

	/**
	 * Display the specified checkins on the MapView
	 */
	protected void displayCheckins(List<Checkin> checkins) {
		// Iterate in the list
		Iterator<Checkin> i = checkins.iterator();
		Checkin checkin;
		GeoPoint point;
		CheckinOverlayItem checkinOverlayItem;

		while (i.hasNext()) {
			checkin = i.next();
			point = new GeoPoint((int) (checkin.getLatitude() * 1e6), (int) (checkin.getLongitude() * 1e6));
			checkinOverlayItem = new CheckinOverlayItem(point, checkin);

			drawable = this.getResources().getDrawable(R.drawable.androidmarker);
			CheckinOverlay checkinOverlay = new CheckinOverlay(drawable, SocialActivity.this);
			checkinOverlay.addOverlay(checkinOverlayItem);
			mapOverlays.add(checkinOverlay);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_social, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		// Handle item selection
		switch (item.getItemId()) {
		case android.R.id.home:
			intent = new Intent(this, HomeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.social_menuItem_refresh:
			new DownloadTask().execute();
			return true;
		case R.id.social_menuItem_center:
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			String bestProvider = locationManager.getBestProvider(criteria, true);
			Location lastLocation = null;
			if (bestProvider != null) {
				lastLocation = locationManager.getLastKnownLocation(bestProvider);
			}
			if (lastLocation != null) {
				mapController.animateTo(new GeoPoint((int) (lastLocation.getLatitude() * 1e6), (int) (lastLocation.getLongitude() * 1e6)));
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
