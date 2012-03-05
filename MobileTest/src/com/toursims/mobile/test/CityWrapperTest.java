package com.toursims.mobile.test;

import java.util.List;

import android.location.Location;
import android.test.AndroidTestCase;

import com.toursims.mobile.controller.CityWrapper;
import com.toursims.mobile.model.City;

/**
 * JUnit tests for the CityWrapper class 
 */
public class CityWrapperTest extends AndroidTestCase {

	/**
	 * Android debugging tag
	 */
	private static final String TAG = CityWrapperTest.class.getName(); 
	
	/**
	 * The PlaceWrapper object to test
	 */
	private CityWrapper cityWrapper;
	
	/**
	 * Shared location of the user
	 */
	private Location lastLocation;
	
	/**
	 * Default constructor, required by JUnit
	 */
	public CityWrapperTest() {
		super();
	}
	
	/**
	 * Standard JUnit method, called before a test is launched
	 */
	public void setUp() {
		cityWrapper = new CityWrapper(); 
		lastLocation = null;
	}
	
	/**
	 * Test the SearchPointOfInterestPlaces method
	 */
	public void testSearchCities() {
		List<City> cities = cityWrapper.SearchCities(10.0, 10.0);
		assertTrue(cities.size() != 0);
	}

}
