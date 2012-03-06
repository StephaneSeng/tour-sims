package com.toursims.mobile.model;

/**
 * Object representing an User with some useful informations
 */
public class User {

	private int userId;
	
	private String name;
	
	private String avatar;
	
	private boolean sharePosition;
	
	private boolean isGuide;

	public User(int userId, String name, String avatar, boolean sharePosition,
			boolean isGuide) {
		super();
		this.userId = userId;
		this.name = name;
		this.avatar = avatar;
		this.sharePosition = sharePosition;
		this.isGuide = isGuide;
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

}
