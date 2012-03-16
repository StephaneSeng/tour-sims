package com.toursims.mobile.model;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Comment {

	/**
	 * Android debugging tag
	 */
	private final static String TAG = Comment.class.getName();
	
	public final static String COMMENT_ID_EXTRA = "comment_id";
	
	private int commentId;
	
	private String text;
	
	private Date timestamp;
	
	private int userId;
	
	private String userName;
	
	private String userAvatar;
	
	// Mobile specifics
	private Bitmap userAvatarBitmap;

	public Comment(int commentId, String text, Date timestamp, int userId,
			String userName, String userAvatar) {
		super();
		this.commentId = commentId;
		this.text = text;
		this.timestamp = timestamp;
		this.userId = userId;
		this.userName = userName;
		this.userAvatar = userAvatar;
		
		// TODO: Lots of refactoring!
		// Download the avatar and store it as a Bitmap object
		try {
			Log.d(TAG, "Trying to download the avatar image from : " + userAvatar);
			
			URL url = new URL(userAvatar);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.connect();
            InputStream inputStream = httpUrlConnection.getInputStream();
            userAvatarBitmap = BitmapFactory.decodeStream(inputStream);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public Bitmap getUserAvatarBitmap() {
		return userAvatarBitmap;
	}

	public void setUserAvatarBitmap(Bitmap userAvatarBitmap) {
		this.userAvatarBitmap = userAvatarBitmap;
	}

}
