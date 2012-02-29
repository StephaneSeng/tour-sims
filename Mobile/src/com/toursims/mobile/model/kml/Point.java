package com.toursims.mobile.model.kml;
import org.simpleframework.xml.Element;

public class Point {

	@Element
	private String coordinates;
	private double longitude;
	private double lattitude;

	public Point() {
		super();
	}

	public Point(String coordinates) {
		super();
		this.coordinates = coordinates;
		String[] lL = coordinates.split(",");
    	lattitude = Double.parseDouble(lL[0]);
    	longitude = Double.parseDouble(lL[1]);
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
	
	public double getLattitude(){
		return lattitude;
	}
}