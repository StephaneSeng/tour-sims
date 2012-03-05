package com.toursims.mobile.model;

import java.util.ArrayList;
import java.util.List;

import com.toursims.mobile.model.kml.Document;
import com.toursims.mobile.model.kml.Placemark;

public class Course {
	
	public static final String URL_EXTRA = "course_url";
	public static final String ID_EXTRA = "course_id";
	public static final String NEXT_PLACEMARK = "course_next_placemark";
	public static final String CURRENT_PLACEMARK = "placemark";
	public static final String PREFERENCES_STARTED_URL = "course_url_for_preferences";
	public static final String PREFERENCES_STARTED_TIME_STARTED = "course_started_time_for_preferences";
	public static final String PREFERENCES_STARTED_ID = "course_id_for_preferences";
	
	private Integer id;
	private String city; 
	private String coverPictureURL;
	private String desc;
	private Double length;
	private Double rating; 
	private String url;
	private String name;
	private String type;
	private String end;
	private String presentation;
	
	public static final String TYPE_COURSE = "course";
	public static final String TYPE_GAME = "game";
			
	private List<Placemark> placemarks = new ArrayList<Placemark>();
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}

	public String getCoverPictureURL() {
		return coverPictureURL;
	}
	
	public void setCoverPictureURL(String coverPictureURL) {
		this.coverPictureURL = coverPictureURL;
	}
	
	public void setDesc(String text) {
		this.desc = text;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public void setLength(Double length) {
		this.length = length;
	}
	
	public Double getLength() {
		return length;
	}
	
	public void setPlacemarks(List<Placemark> placemarks) {
		this.placemarks = placemarks;
	}
	
	public List<Placemark> getPlacemarks() {
		return placemarks;
	}
	
	public Double getRating() {
		return rating;
	}
	
	public void setRating(Double rating) {
		this.rating = rating;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public String getEnd() {
		if(end==null){
			return name;
		}
		return end;
	}
	
	public void setEnd(String end) {
		this.end = end;
	}
	
	public String getPresentation() {
		if(presentation==null){
			return presentation;
		}
		return presentation;
	}
	
	public void setPresentation(String presentation) {
		this.presentation = presentation;
	}
	
	public void copyFromDocument(Document item,String address){
		setName(item.getName());
		try {
			setUrl(address);
			setCity(item.getExtendedData().get(0).getValue());
			setCoverPictureURL(item.getExtendedData().get(1).getValue());
			setDesc(item.getExtendedData().get(2).getValue());
			setRating(Double.valueOf(item.getExtendedData().get(3).getValue()));
			setLength(Double.valueOf(item.getExtendedData().get(4).getValue()));
			setType(item.getExtendedData().get(5).getValue());
		} catch (IndexOutOfBoundsException e) {
			setType(Course.TYPE_COURSE);
		}	
		setPlacemarks(item.getPlacemarks());
		setEnd(item.getEnd());
		setPresentation(item.getPresentation());
	}
}
