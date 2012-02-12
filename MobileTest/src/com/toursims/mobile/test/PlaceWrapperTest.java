package com.toursims.mobile.test;

import java.util.List;

import junit.framework.TestCase;

import com.toursims.mobile.controller.PlaceWrapper;
import com.toursims.mobile.model.places.Place;

/**
 * JUnit tests for the PlaceWrapper class 
 */
public class PlaceWrapperTest extends TestCase {

	private PlaceWrapper placeWrapper;
	
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
	}

	/**
	 * Standard JUnit method, called after a test is launched
	 */
	public void tearDown() {
	}
	
	/**
	 * Test the SearchPointOfInterestPlaces method
	 */
	public void testSearchPointOfInterestPlaces() {
		List<Place> places = placeWrapper.SearchPointOfInterestPlaces(10.0, 10.0, 10.0);
		assertTrue(places.size() != 0);
	}

}
