package com.toursims.mobile.model;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Object representing an User with some useful informations
 */
public class User {

	/**
	 * Android debugging tag
	 */
	private final static String TAG = User.class.getName();
	
	public final static String USER_ID_EXTRA = "user_id";
	
	private int userId;
	
	private String name;
	
	private String avatar;
	
	private boolean sharePosition;
	
	private boolean isGuide;
	
	// Mobile specifics
	private Bitmap avatarBitmap;

	public User(int userId, String name, String avatar, boolean sharePosition,
			boolean isGuide) {
		super();
		this.userId = userId;
		this.name = name;
		this.avatar = avatar;
		this.sharePosition = sharePosition;
		this.isGuide = isGuide;
		
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

	public boolean isSharePosition() {
		return sharePosition;
	}

	public void setSharePosition(boolean sharePosition) {
		this.sharePosition = sharePosition;
	}

	public boolean isGuide() {
		return isGuide;
	}

	public void setGuide(boolean isGuide) {
		this.isGuide = isGuide;
	}

	public Bitmap getAvatarBitmap() {
		return avatarBitmap;
	}

	public void setAvatarBitmap(Bitmap avatarBitmap) {
		this.avatarBitmap = avatarBitmap;
	}

	public static String getTag() {
		return TAG;
	}

}
