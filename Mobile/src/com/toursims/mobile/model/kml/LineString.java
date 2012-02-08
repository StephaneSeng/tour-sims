package com.toursims.mobile.model.kml;
import org.simpleframework.xml.Element;

public class LineString {

	@Element(required=false)
	private String coordinates;

	public LineString() {
		super();
	}

	public LineString(String coordinates) {
		super();
		this.coordinates = coordinates;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}

}