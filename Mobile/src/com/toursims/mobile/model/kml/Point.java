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
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
	
	public double getLongitude(){
        String[] lL = getCoordinates().split(",");
    	double l = Double.parseDouble(lL[1]);	        	
    	double L = Double.parseDouble(lL[0]);
    	return L;
	}
	
	public double getLatitude(){
        String[] lL = getCoordinates().split(",");
    	double l = Double.parseDouble(lL[1]);	        	
    	return l;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}