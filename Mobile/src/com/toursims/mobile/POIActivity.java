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
import com.toursims.mobile.controller.PlaceWrapper;
import com.toursims.mobile.model.places.Place;
import com.toursims.mobile.ui.utils.CustomItemizedOverlay;

public class POIActivity extends MapActivity {
	
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
	
	/**
	 * Called when the activity is first created
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poi);
        
        // Initialize the share attributes
        mapView = (MapView)findViewById(R.id.poiMapView);
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
		String bestProvider = locationManager.getBestProvider(criteria, false);
		
		bestProvider = LocationManager.GPS_PROVIDER;
		
		Location lastLocation = locationManager.getLastKnownLocation(bestProvider);
		Log.d(TAG, "Test : " + lastLocation);
		
		// Call the SearchPointOfInterestPlaces method
		PlaceWrapper placeWrapper = new PlaceWrapper();
		List<Place> places = placeWrapper.SearchPointOfInterestPlaces(lastLocation.getLatitude(), lastLocation.getLongitude(), 100.0);
		
		// Display the map with the user at its center
		mapController.animateTo(new GeoPoint((int)(lastLocation.getLatitude() * 1e6), (int)(lastLocation.getLongitude() * 1e6)));
		
		// Display the user
		displayUser(lastLocation);
		
		// Display the points of interests
		displayPointOfInterests(places);
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
        itemizedOverlay = new CustomItemizedOverlay(drawable, POIActivity.this);
        itemizedOverlay.addOverlay(overlayItem);
        mapOverlays.add(itemizedOverlay);
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
			point = new GeoPoint((int)(place.getLatitude() * 1e6), (int)(place.getLongitude() * 1e6));
	        overlayItem = new OverlayItem(point, place.getName(), "Latitude : " + place.getLatitude() + ", Longitude : " + place.getLongitude());
	        
	        drawable = this.getResources().getDrawable(R.drawable.androidmarker);
	        itemizedOverlay = new CustomItemizedOverlay(drawable, POIActivity.this);
	        itemizedOverlay.addOverlay(overlayItem);
	        mapOverlays.add(itemizedOverlay);
		}
	}
	
}
