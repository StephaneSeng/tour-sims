package com.toursims.mobile.model.kml;

import org.simpleframework.xml.Element;

public class Clue {

	@Element(required=false)
	private String delay;

	@Element(required=false)
	private String value;
	
	public String getDelay() {
		return delay;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setDelay(String delay) {
		this.delay = delay;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
