package com.toursims.mobile.model.kml;
import org.simpleframework.xml.Element;

public class Point {
	public static final String LATITUDE = "point_latitude";
	public static final String LONGITUDE = "point_longitude";
	

	@Element
	private String coordinates;
	private double longitude;
	private double latitude;

	public Point() {
		super();
	}

	public Point(String coordinates) {
		super();
		this.coordinates = coordinates;
		String[] lL = coordinates.split(",");
    	latitude = Double.parseDouble(lL[1]);
    	longitude = Double.parseDouble(lL[0]);
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
	
	public double getLongitude(){
		return longitude;
	}
	
	public double getLatitude(){
		return latitude;
	}
}