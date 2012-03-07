package com.toursims.mobile.model;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Object representing a Checkin made by a user
 */
public class Checkin {
	
	/**
	 * Android debugging tag
	 */
	private final static String TAG = Checkin.class.getName();
	
	private int userId;
	
	private String name;
	
	private String avatar;
	
	private double latitude;
	
	private double longitude;
	
	private Date timestamp;
	
	// Mobile specifics
	private Bitmap avatarBitmap;

	public Checkin(int userId, String name, String avatar, double latitude,
			double longitude, Date timestamp) {
		super();
		this.userId = userId;
		this.name = name;
		this.avatar = avatar;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
		
		// TODO: Refactorer
		// Download the avatar and store it as a Bitmap object
		try {
			Log.d(TAG, "Trying to download the avatar image from : " + avatar);
			
			URL url = new URL(avatar);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.connect();
            InputStream inputStream = httpUrlConnection.getInputStream();
            avatarBitmap = BitmapFactory.decodeStream(inputStream);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}
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

	public Bitmap getAvatarBitmap() {
		return avatarBitmap;
	}

	public void setAvatarBitmap(Bitmap avatarBitmap) {
		this.avatarBitmap = avatarBitmap;
	}

}
