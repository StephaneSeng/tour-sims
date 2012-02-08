package com.toursims.mobile.model.places;

import java.net.URI;

public class Place {

	private double latitude;
	
	private double longitude;
	
	private URI icon;
	
	private String id;
	
	private String name;
	
	private float rating;
	
	private String reference;
	
	private String types[];
	
	private String vicinity;

	public Place(double latitude, double longitude, URI icon, String id,
			String name, float rating, String reference, String[] types,
			String vicinity) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.icon = icon;
		this.id = id;
		this.name = name;
		this.rating = rating;
		this.reference = reference;
		this.types = types;
		this.vicinity = vicinity;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public URI getIcon() {
		return icon;
	}

	public void setIcon(URI icon) {
		this.icon = icon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	public String getVicinity() {
		return vicinity;
	}

	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}
	
}
