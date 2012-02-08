package com.toursims.mobile.model.kml;
import org.simpleframework.xml.Element;

public class Icon {

	@Element
	private String href;

	public Icon() {
		super();
	}

	public Icon(String href) {
		super();
		this.href = href;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

}