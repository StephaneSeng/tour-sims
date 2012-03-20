package com.toursims.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.toursims.mobile.controller.CommentWrapper;
import com.toursims.mobile.controller.RatingWrapper;

public class FeedbackActivity extends SherlockActivity {

	public static final String FEEDBACK_COURSE_ID = "FEEDBACK_COURSE_ID";
	public static final String FEEDBACK_COURSE_NAME = "FEEDBACK_COURSE_NAME";
	private int courseId;
	private String courseName;
	private int userId;
	
	/**
	 * Called when the activity is first created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layout_feedback);
	    
	    // Shared variable initialization
	    TourSims tourSims = (TourSims) getApplication();
	    if (tourSims.isUserLoggedIn()) {
	    	userId = tourSims.getUser().getUserId();
	    } else {
	    	userId = -1;
	    }
	    
	    if (getIntent().getExtras().containsKey(FEEDBACK_COURSE_ID)) {
	    	courseId = getIntent().getExtras().getInt(FEEDBACK_COURSE_ID);
	    } else {
	    	courseId = -1;
	    }
	    
	    if (getIntent().getExtras().containsKey(FEEDBACK_COURSE_NAME)) {
	    	courseName = getIntent().getExtras().getString(FEEDBACK_COURSE_NAME);
	    } else {
	    	courseName = "";
	    }
	    
	    // ActionBarSherlock initialization
 		ActionBar actionBar = getSupportActionBar();
 		actionBar.setDisplayHomeAsUpEnabled(true);
 		actionBar.setIcon(R.drawable.ic_menu_compose_white);
 		actionBar.setTitle(R.string.feedback);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_feedback, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		// Handle item selection
		switch (item.getItemId()) {
		case android.R.id.home:
			intent = new Intent(this, HomeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.feedback_menuItem_send:
			EditText messageEditText = (EditText) findViewById(R.id.feedback_editText_message);
			RatingBar ratingBar = (RatingBar) findViewById(R.id.feedback_ratingBar);
			
			// Send a feedback
			RatingWrapper ratingWrapper = new RatingWrapper(getApplicationContext());
			ratingWrapper.CreateCourseRating((double)ratingBar.getRating(), userId, courseName);
			CommentWrapper commentWrapper = new CommentWrapper(getApplicationContext());
			commentWrapper.CreateCourseComments(messageEditText.getText().toString(), userId, courseName);
			
			intent = new Intent(this, HomeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
