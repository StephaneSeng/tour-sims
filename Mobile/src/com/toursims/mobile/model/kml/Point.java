package com.toursims.mobile.model.kml;
import org.simpleframework.xml.Element;

import com.toursims.mobile.model.BasicObject;

public class Point extends BasicObject {
	public static final String LATITUDE = "point_latitude";
	public static final String LONGITUDE = "point_longitude";
	
	@Element
	private String coordinates;
	private double longitude;
	private double latitude;
	private long millis;
	
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
		if(getCoordinates()!=null){
	        String[] lL = getCoordinates().split(",");
	    	longitude = Double.parseDouble(lL[0]);
		} 			
		return longitude;
	}
	
	public double getLatitude(){
		if(getCoordinates()!=null){
			String[] lL = getCoordinates().split(",");
			latitude = Double.parseDouble(lL[1]);
		}
    	return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public long getMillis() {
		return millis;
	}
	
	public void setMillis(long millis) {
		this.millis = millis;
	}
}