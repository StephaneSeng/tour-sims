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
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.toursims.mobile.controller.PlaceWrapper;
import com.toursims.mobile.model.places.Place;
import com.toursims.mobile.ui.utils.CustomItemizedOverlay;

public class POIActivity extends SherlockMapActivity {

	/**
	 * Android debugging tag
	 */
	private static final String TAG = POIActivity.class.toString();

	/**
	 * Shared object
	 */
	private MapView mapView;
	private MapController mapController;
	private List<Overlay> mapOverlays;
	private Drawable drawable;
	private CustomItemizedOverlay itemizedOverlay;
	private MyLocationOverlay myLocationOverlay;
	private LocationManager locationManager;
	private Criteria criteria;
	private String bestProvider;

	/**
	 * Called when the activity is first created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poi);

		// Initialize the shared attributes
		mapView = (MapView) findViewById(R.id.poiMapView);
		mapController = mapView.getController();
		mapOverlays = mapView.getOverlays();

		// Set the MapView properties
		mapView.setBuiltInZoomControls(true);
		mapController.setZoom(16); // Zoom 1 is world view

		myLocationOverlay = new MyLocationOverlay(this, mapView);
		myLocationOverlay.enableMyLocation();

		// ActionBarSherlock setup
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(R.drawable.ic_menu_marker_colored);
		actionBar.setTitle(R.string.home_poi);
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
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			bestProvider = locationManager.getBestProvider(criteria, true);
			Location lastLocation = null;
			if (bestProvider != null) {
				lastLocation = locationManager.getLastKnownLocation(bestProvider);
			}

			// Display the map with the user at its center
			if (myLocationOverlay.getMyLocation() != null) {
				mapController.animateTo(new GeoPoint((int) (lastLocation.getLatitude() * 1E6), (int) (lastLocation.getLongitude() * 1E6)));
				mapOverlays.add(myLocationOverlay);
			}

			update(mapView);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mapView.invalidate();
			setSupportProgressBarIndeterminateVisibility(false);
		}
	}

	@Override
	protected void onDestroy() {
		myLocationOverlay.disableMyLocation();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		myLocationOverlay.disableMyLocation();
		super.onPause();
	}

	/**
	 * Method to override with the MapActivity class
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public void update(View view) {
		Location lastLocation = null;
		if (bestProvider != null) {
			lastLocation = locationManager.getLastKnownLocation(bestProvider);
		}

		if (lastLocation != null) {
			Log.d(TAG, "Updating in progress !");

			mapController.animateTo(new GeoPoint((int) (lastLocation.getLatitude() * 1E6), (int) (lastLocation.getLongitude() * 1E6)));

			// Call the SearchPointOfInterestPlaces method
			PlaceWrapper placeWrapper = new PlaceWrapper();
			List<Place> places = placeWrapper.SearchPointOfInterestPlaces(lastLocation.getLatitude(), lastLocation.getLongitude(), 100.0);

			// Display the points of interests
			displayPointOfInterests(places);
		}
	}

	/**
	 * Display the specified points of interests on the MapView
	 */
	protected void displayPointOfInterests(List<Place> places) {
		// Iterate in the list
		Iterator<Place> i = places.iterator();
		Place place;
		GeoPoint point;
		OverlayItem overlayItem;

		while (i.hasNext()) {
			place = i.next();
			point = new GeoPoint((int) (place.getLatitude() * 1e6), (int) (place.getLongitude() * 1e6));
			overlayItem = new OverlayItem(point, place.getName(), "Latitude : " + place.getLatitude() + ", Longitude : " + place.getLongitude());

			drawable = this.getResources().getDrawable(R.drawable.maps_icon);
			itemizedOverlay = new CustomItemizedOverlay(drawable, POIActivity.this);
			itemizedOverlay.addOverlay(overlayItem);
			mapOverlays.add(itemizedOverlay);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_poi, menu);

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
		case R.id.poi_menuItem_refresh:
			new DownloadTask().execute();
			return true;
		case R.id.poi_menuItem_center:
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