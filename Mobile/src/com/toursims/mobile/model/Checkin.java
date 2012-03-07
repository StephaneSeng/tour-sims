package com.toursims.mobile.model;

import java.util.Date;

/**
 * Object representing a Checkin made by a user
 */
public class Checkin {
	
	private int userId;
	
	private String name;
	
	private String avatar;
	
	private double latitude;
	
	private double longitude;
	
	private Date timestamp;

	public Checkin(int userId, String name, String avatar, double latitude,
			double longitude, Date timestamp) {
		super();
		this.userId = userId;
		this.name = name;
		this.avatar = avatar;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
