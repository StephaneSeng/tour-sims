package com.toursims.mobile;

import android.app.Application;

/**
 * Android application containing variables shared between the activities
 */
public class TourSims extends Application {

	/**
	 * User name
	 */
	private String userName = "";

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
