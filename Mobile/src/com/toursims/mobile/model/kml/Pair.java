package com.toursims.mobile.model.kml;
import org.simpleframework.xml.Element;

public class Pair {
	@Element
	private String key;

	@Element
	private Style Style;

	public Pair() {
		super();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Style getStyle() {
		return Style;
	}

	public void setStyle(Style style) {
		this.Style = style;
	}

}