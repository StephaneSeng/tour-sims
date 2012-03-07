package com.toursims.mobile.ui.utils;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.toursims.mobile.model.User;

public class UserOverlayItem extends OverlayItem {

	private User user;

	public UserOverlayItem(GeoPoint point, String title, String snippet, User user) {
		super(point, title, snippet);
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
