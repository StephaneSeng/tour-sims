package com.toursims.mobile;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.toursims.mobile.controller.CommentWrapper;
import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.controller.RatingWrapper;
import com.toursims.mobile.model.Comment;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.ui.CommentAdapter;
import com.toursims.mobile.ui.ToolBox;

public class CourseDetailsActivity extends SherlockActivity {
	private int course_id;
	private String course_url;
	private ProgressDialog dialog;
	private Course course;
	private double rating;
	private List<Comment> comments;
	private CommentAdapter commentAdapter;

	@Override
	protected void onPause() {
		super.onPause();
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coursedetails);
		
		new DownloadTask().execute();
		
		// ActionBarSherlock setup
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(R.drawable.ic_dialog_map_colored);
		actionBar.setTitle("");
	}
	
	private class DownloadTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			setSupportProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			CourseBDD datasource = null;
			course = null;
			int courseId = getIntent().getExtras().getInt(Course.ID_EXTRA);
			try {
				datasource = new CourseBDD(getApplicationContext());
				datasource.open();
				course = datasource.getCourseWithId(courseId);
				datasource.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (datasource != null) {
				datasource.close();
			}
			
			// Retreive the current course rating
			RatingWrapper ratingWrapper = new RatingWrapper(getApplicationContext());
			rating = ratingWrapper.GetCourseRating(1);
			
			// Retreive the current course comments
			CommentWrapper commentWrapper = new CommentWrapper(getApplicationContext());
			comments = commentWrapper.GetCourseComments(1);
			commentAdapter = new CommentAdapter(getApplicationContext(), comments);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			ImageView thumbnailImageView = (ImageView) findViewById(R.id.coursesocial_imageView_thumbnail);
			TextView nameTextView = (TextView) findViewById(R.id.coursesocial_textView_name);
			TextView descriptionTextView = (TextView) findViewById(R.id.coursesocial_textView_description);
			ListView commentsListView = (ListView) findViewById(R.id.coursesocial_listView_comments);
			RatingBar ratingBar = (RatingBar) findViewById(R.id.coursesocial_ratingBar_rating);
			
			Bitmap bitmap = BitmapFactory.decodeFile(ToolBox.cacheFile(course.getCoverPictureURL(), getCacheDir().getAbsolutePath()));
			thumbnailImageView.setImageBitmap(bitmap);
			nameTextView.setText(course.getName());
			descriptionTextView.setText(course.getDesc());
			ratingBar.setRating((float) rating);
			commentsListView.setAdapter(commentAdapter);
			ToolBox.setListViewHeightBasedOnChildren(commentsListView);
			
			course_id = course.getId();
			course_url = course.getUrl();
			
			// ActionBarSherlock customization 2
			getSupportActionBar().setTitle(course.getName());
			
			setSupportProgressBarIndeterminateVisibility(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_coursedetails, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		// Handle item selection
		switch (item.getItemId()) {
		case android.R.id.home:
			intent = new Intent(this, CourseGameActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("CITY", course.getCity());
			startActivity(intent);
			return true;
		case R.id.coursedetails_menuItem_refresh:
			new DownloadTask().execute();
			
			return true;
		case R.id.coursedetails_menuItem_start:
			onClickStart(item.getActionView());
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void onClickStart(View view) {
		dialog = ProgressDialog.show(CourseDetailsActivity.this, "", "Loading. Please wait...", true);

		// When clicked, show Course details
		Intent courseStep = new Intent(getApplicationContext(), CourseStepActivity.class);
		courseStep.putExtra(Course.ID_EXTRA, course_id);
		courseStep.putExtra(Course.URL_EXTRA, course_url);
		startActivity(courseStep);

		// Put that a course is started
		SharedPreferences settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);
		CustomPreferences.removeCourseStarted(settings);

		SharedPreferences.Editor editor = settings.edit();
		editor.putString(CustomPreferences.COURSE_STARTED_URL, course_url);
		editor.putInt(CustomPreferences.COURSE_STARTED_ID, course_id);
		Calendar c = Calendar.getInstance();
		int seconds = c.get(Calendar.SECOND);
		editor.putInt(CustomPreferences.COURSE_STARTED_TIME_STARTED, seconds);
		editor.commit();
	}

}