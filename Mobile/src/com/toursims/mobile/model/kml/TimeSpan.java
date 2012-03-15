package com.toursims.mobile.model.kml;

import org.simpleframework.xml.Element;

public class TimeSpan {

	@Element(required = false)
	private String begin;

	@Element(required = false)
	private String end;

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String toKml() {
		return "<TimeSpan><begin>" + getBegin() + "</begin><end>" + getEnd()
				+ "</end></TimeSpan>";
	}
}
