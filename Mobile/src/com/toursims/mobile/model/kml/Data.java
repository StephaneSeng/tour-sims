package com.toursims.mobile.model.kml;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class Data {

	@Attribute
	private String name;

	@Element
	private String value;
	
	public Data() {
		super();
	}

	public Data(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
public String getValue() {
	return value;
}	
}
