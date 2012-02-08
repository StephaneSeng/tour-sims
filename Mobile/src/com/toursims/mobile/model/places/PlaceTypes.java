package com.toursims.mobile.model.places;

/**
 * Object of the Google Places API types
 */
public enum PlaceTypes {
	
	/**
	 * Enumeration of the Places API types
	 */
	POINT_OF_INTERESTS("point_of_interests");
	
	/**
	 * String value of one item of the enumeration, each one should have a different value
	 */
	String value;
	
	/**
	 * Default constructor
	 * @param value The String value of the item
	 */
	private PlaceTypes(String value) {
		this.value = value;
	}
	
	/**
	 * Used for retrieving the String value of one item of the enumeration
	 */
	public String toString() {
		return value;
	}
	
}
