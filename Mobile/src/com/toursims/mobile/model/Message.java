package com.toursims.mobile.model;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Message {

	/**
	 * Android debugging tag
	 */
	private final static String TAG = Message.class.getName();
	
	public final static String MESSAGE_ID_EXTRA = "message_id";
	public final static String ROOT_MESSAGE_ID_EXTRA = "root_message_id";
	
	private int messageId;
	
	private String text;
	
	private double latitude;
	
	private double longitude;
	
	private Date timestamp;
	
	private int replyMessageId;
	
	private int writerId;
	
	private String writerName;
	
	private String writerAvatar;
	
	private int replyMessageCount;
	
	// Mobile specifics
	private Bitmap writerAvatarBitmap;

	public Message(int messageId, String text, double latitude,
			double longitude, Date timestamp, int replyMessageId, int writerId,
			String writerName, String writerAvatar, int replyMessageCount) {
		super();
		this.messageId = messageId;
		this.text = text;
		this.latitude = latitude;
		this.longitude = longitude;
		this.timestamp = timestamp;
		this.replyMessageId = replyMessageId;
		this.writerId = writerId;
		this.writerName = writerName;
		this.writerAvatar = writerAvatar;
		this.replyMessageCount = replyMessageCount;
		
		// TODO: Lots of refactoring!
		// Download the avatar and store it as a Bitmap object
		try {
			Log.d(TAG, "Trying to download the avatar image from : " + writerAvatar);
			
			URL url = new URL(writerAvatar);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.connect();
            InputStream inputStream = httpUrlConnection.getInputStream();
            writerAvatarBitmap = BitmapFactory.decodeStream(inputStream);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

	public int getReplyMessageId() {
		return replyMessageId;
	}

	public void setReplyMessageId(int replyMessageId) {
		this.replyMessageId = replyMessageId;
	}

	public int getWriterId() {
		return writerId;
	}

	public void setWriterId(int writerId) {
		this.writerId = writerId;
	}

	public String getWriterName() {
		return writerName;
	}

	public void setWriterName(String writerName) {
		this.writerName = writerName;
	}

	public String getWriterAvatar() {
		return writerAvatar;
	}

	public void setWriterAvatar(String writerAvatar) {
		this.writerAvatar = writerAvatar;
	}
	
	public int getReplyMessageCount() {
		return replyMessageCount;
	}

	public void setReplyMessageCount(int replyMessageCount) {
		this.replyMessageCount = replyMessageCount;
	}

	public Bitmap getWriterAvatarBitmap() {
		return writerAvatarBitmap;
	}

	public void setWriterAvatarBitmap(Bitmap writerAvatarBitmap) {
		this.writerAvatarBitmap = writerAvatarBitmap;
	}
	
}
