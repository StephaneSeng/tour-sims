package com.toursims.mobile.model.places;

public enum PlaceTypes {
	
	POINT_OF_INTERESTS("point_of_interests");
	
	String value;
	
	private PlaceTypes(String value) {
		this.value = value;
	}
	
	public String toString() {
		return value;
	}
	
}
