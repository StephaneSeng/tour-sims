package com.toursims.mobile.model.kml;
import org.simpleframework.xml.Element;

public class Point {

	@Element
	private String coordinates;

	public Point() {
		super();
	}

	public Point(String coordinates) {
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