package com.tourisms.mobile.model;

import java.util.List;

import com.toursims.mobile.model.kml.Placemark;

public class Course {
	
	private String city; 
	private String coverPictureURL;
	private String text;
	private Double length;
	private Double rating; 
	
	private List<Placemark> placemarks;
	
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
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
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
}
