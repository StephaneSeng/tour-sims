package com.toursims.mobile.ui.utils;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.toursims.mobile.model.Checkin;

// TODO: Refactorer
public class CheckinOverlayItem extends OverlayItem {

	private Checkin checkin;

	public CheckinOverlayItem(GeoPoint point, Checkin checkin) {
		super(point, "", "");
		this.checkin = checkin;
	}

	public Checkin getCheckin() {
		return checkin;
	}

	public void setCehckin(Checkin checkin) {
		this.checkin = checkin;
	}
	
}
