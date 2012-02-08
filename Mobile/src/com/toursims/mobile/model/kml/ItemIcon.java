package com.toursims.mobile.model.kml;
import org.simpleframework.xml.Element;

public class ItemIcon {

	@Element
	private String href;

	public ItemIcon() {
		super();
	}

	public ItemIcon(String href) {
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