package com.toursims.mobile;

import android.app.Application;

import com.toursims.mobile.model.User;

/**
 * Android application containing variables shared between the activities
 */
public class TourSims extends Application {

	/**
	 * The current logged in user
	 */
	private User user;

	/**
	 * State variable true if the user is logged in, false otherwise
	 */
	private boolean userLoggedIn = false;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(boolean userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}

}
