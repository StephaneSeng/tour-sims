package com.toursims.mobile.model.kml;
import org.simpleframework.xml.Attribute;

public class hotSpot {

	@Attribute
	private double x;

	@Attribute
	private double y; 

	@Attribute
	private String xunits;

	@Attribute
	private String yunits;

	public hotSpot() {
		super();
	}

	public hotSpot(double x, double y, String xunits, String yunits) {
		super();
		this.x = x;
		this.y = y;
		this.xunits = xunits;
		this.yunits = yunits;
	}

}