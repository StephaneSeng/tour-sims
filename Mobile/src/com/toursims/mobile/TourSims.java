package com.toursims.mobile;

import android.app.Application;

/**
 * Android application containing variables shared between the activities
 */
public class TourSims extends Application {

	/**
	 * User name
	 */
	private String userName;
	
	/**
	 * User ID
	 */
	private int userId;
	
	/**
	 * State variable
	 * true if the user is logged in, false otherwise
	 */
	private boolean userLoggedIn = false;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean isUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(boolean userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}
	
}
