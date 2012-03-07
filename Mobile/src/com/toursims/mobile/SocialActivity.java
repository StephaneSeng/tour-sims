package com.toursims.mobile;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.toursims.mobile.controller.UserWrapper;
import com.toursims.mobile.model.Checkin;
import com.toursims.mobile.ui.utils.CustomItemizedOverlay;


public class SocialActivity extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstantState){
		super.onCreate(savedInstantState);
		setContentView(R.layout.social);
	}


public class SocialActivity extends MapActivity {
	
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
	private CustomItemizedOverlay itemizedOverlay;
	
	/**
	 * Called when the activity is first created
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social);
        
        // Initialize the share attributes
        mapView = (MapView)findViewById(R.id.socialMapView);
        mapController = mapView.getController();
		mapOverlays = mapView.getOverlays();
        
        // Set the MapView properties
        mapView.setBuiltInZoomControls(true);
        mapController.setZoom(13); // Zoom 1 is world view
    }
	
	/**
	 * Called when the activity is resumed
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		// Get the current user position
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		// Try to get the best localization provider
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = locationManager.getBestProvider(criteria, true);
		
		Location lastLocation = locationManager.getLastKnownLocation(bestProvider);
		Log.d(TAG, "Test : " + lastLocation);
		
		// Call the SearchCheckins method
		UserWrapper userWrapper = new UserWrapper();
		TourSims tourSims = (TourSims)getApplicationContext();
		List<Checkin> checkins = userWrapper.SearchCheckins(lastLocation.getLatitude(), lastLocation.getLongitude(), tourSims.getUserId());
		
		// Display the map with the user at its center
		mapController.animateTo(new GeoPoint((int)(lastLocation.getLatitude() * 1e6), (int)(lastLocation.getLongitude() * 1e6)));
		
		// Display the user
		displayUser(lastLocation);
		
		// Display the points of interests
		displayCheckins(checkins);
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
		GeoPoint point = new GeoPoint((int)(location.getLatitude() * 1e6), (int)(location.getLongitude() * 1e6));
        OverlayItem overlayItem = new OverlayItem(point, "User Name", "Latitude : " + location.getLatitude() + ", Longitude : " + location.getLongitude());
        
        drawable = this.getResources().getDrawable(R.drawable.androidmarkerred);
        itemizedOverlay = new CustomItemizedOverlay(drawable, SocialActivity.this);
        itemizedOverlay.addOverlay(overlayItem);
        mapOverlays.add(itemizedOverlay);
	}
	
	/**
	 * Display the specified checkins on the MapView
	 */
	protected void displayCheckins(List<Checkin> checkins) {
		// Iterate in the list
		Iterator<Checkin> i = checkins.iterator();
		Checkin checkin;
		GeoPoint point;
		OverlayItem overlayItem;
		
		while (i.hasNext()) {
			checkin = i.next();
			point = new GeoPoint((int)(checkin.getLatitude() * 1e6), (int)(checkin.getLongitude() * 1e6));
	        overlayItem = new OverlayItem(point, checkin.getName(), "Latitude : " + checkin.getLatitude() + ", Longitude : " + checkin.getLongitude());
	        
	        drawable = this.getResources().getDrawable(R.drawable.androidmarker);
	        itemizedOverlay = new CustomItemizedOverlay(drawable, SocialActivity.this);
	        itemizedOverlay.addOverlay(overlayItem);
	        mapOverlays.add(itemizedOverlay);
		}
	}
	
