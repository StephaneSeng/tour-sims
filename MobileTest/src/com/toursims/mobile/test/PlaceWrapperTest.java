package com.toursims.mobile.test;

import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.test.AndroidTestCase;
import android.util.Log;

import com.toursims.mobile.controller.PlaceWrapper;
import com.toursims.mobile.model.places.Place;

/**
 * JUnit tests for the PlaceWrapper class 
 */
public class PlaceWrapperTest extends AndroidTestCase {

	/**
	 * Android debugging tag
	 */
	private static final String TAG = PlaceWrapperTest.class.getName(); 
	
	/**
	 * The PlaceWrapper object to test
	 */
	private PlaceWrapper placeWrapper;
	
	/**
	 * Shared location of the user
	 */
	private Location lastLocation;
	
	/**
	 * Default constructor, required by JUnit
	 */
	public PlaceWrapperTest() {
		super();
	}
	
	/**
	 * Standard JUnit method, called before a test is launched
	 */
	public void setUp() {
		placeWrapper = new PlaceWrapper(); 
		lastLocation = null;
	}
	
	/**
	 * Test the SearchPointOfInterestPlaces method
	 */
	public void testSearchPointOfInterestPlaces() {
		List<Place> places = placeWrapper.SearchPointOfInterestPlaces(10.0, 10.0, 10.0);
		assertTrue(places.size() != 0);
	}
	
	/**
	 * Test the SearchPointOfInterestPlaces method with specified GPS coordinates
	 */
	public void testSearchNearbyPointOfInterestPlaces() {
		// Get the current user position
		LocationManager locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
		
		// Try to get the best localization provider
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = locationManager.getBestProvider(criteria, false);
		lastLocation = locationManager.getLastKnownLocation(bestProvider);
		Log.d(TAG, "Test : " + lastLocation);
		
		assertTrue(lastLocation != null);
	}

}
