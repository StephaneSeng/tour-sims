package com.toursims.mobile.model.kml;
import org.simpleframework.xml.Element;

public class GeometryCollection {

	@Element(required=false)
	private LineString LineString;

	public GeometryCollection() {
		super();
	}

	public GeometryCollection(LineString lineString) {
		super();
		LineString = lineString;
	}

	public LineString getLineString() {
		return LineString;
	}

	public void setLineString(LineString lineString) {
		LineString = lineString;
	}
}