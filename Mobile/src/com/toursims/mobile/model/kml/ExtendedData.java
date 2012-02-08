package com.toursims.mobile.model.kml;
import java.util.List;
import org.simpleframework.xml.ElementList;

public class ExtendedData {

	@ElementList(entry="Data",inline=true,required=false)
	private List<Data> Data;


	public ExtendedData() {
		super();
	}

	public ExtendedData(List<Data> data) {
		super();
		Data = data;
	}

	public void setData(List<Data> data) {
		Data = data;
	}
	
	public List<Data> getData() {
		return Data;
	}
}