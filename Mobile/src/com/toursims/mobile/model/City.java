package com.toursims.mobile.model;

public class City {

	private String name; 
	private String coverPictureURL;
	private Integer id;

	public Integer getId() {
		return id;
	}
	
	public City(String name, String coverPictureURL){
		this.name = name;
		this.coverPictureURL = coverPictureURL;
	}
	
	public City() {
		// TODO Auto-generated constructor stub
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

	public String getCoverPictureURL() {
		return coverPictureURL;
	}

	public void setCoverPictureURL(String coverPictureURL) {
		this.coverPictureURL = coverPictureURL;
	}
	










}
