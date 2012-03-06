package com.toursims.mobile.ui;

import android.content.Intent;
import android.view.View.OnClickListener;

public class HomeItem {

	private OnClickListener function;
	private Intent intent;
	private int text;
	private int pictureURL;
	
	public HomeItem() {
		// TODO Auto-generated constructor stub
	}

	public OnClickListener getFunction() {
		return function;
	}

	public void setFunction(OnClickListener function) {
		this.function = function;
	}

	public int getText() {
		return text;
	}

	public void setText(int text) {
		this.text = text;
	}

	public int getPictureURL() {
		return pictureURL;
	}

	public void setPictureURL(int pictureURL) {
		this.pictureURL = pictureURL;
	}

	public HomeItem(OnClickListener function, int text, int pictureURL) {
		super();
		this.function = function;
		this.text = text;
		this.pictureURL = pictureURL;
	}
	
	public HomeItem(Intent i,int text, int pictureURL) {
		super();
		this.intent = i;
		this.text = text;
		this.pictureURL = pictureURL;
	}	
	
	public Intent getIntent() {
		return intent;
	}
	public void setIntent(Intent intent) {
		this.intent = intent;
	}
}
