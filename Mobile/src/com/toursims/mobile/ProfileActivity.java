package com.toursims.mobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.toursims.mobile.controller.UserWrapper;
import com.toursims.mobile.model.User;

public class ProfileActivity extends SherlockActivity {

	private User user;

	public final static String IS_USER_PROFILE = "IS_USER_PROFILE";

	/**
	 * true if the displayed profile is the user's one, false otherwise
	 */
	private boolean isUserProfile;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_profile);

		new DownloadTask().execute();

		// Check if it is the current user's profile
		isUserProfile = getIntent().getBooleanExtra(IS_USER_PROFILE, true);

		// ActionBarSherlock setup
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("");
		if (isUserProfile) {
			actionBar.setIcon(R.drawable.ic_menu_user_colored);
		} else {
			actionBar.setIcon(R.drawable.ic_menu_user_colored_alt);
		}
	}

	private class DownloadTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			setSupportProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Initialize the current user
			UserWrapper userWrapper = new UserWrapper(getApplicationContext());
			user = userWrapper.GetProfile(getIntent().getIntExtra(User.USER_ID_EXTRA, 0));

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Layout initialisation
			ImageView avatarImageView = (ImageView) findViewById(R.id.profile_imageView_avatar);
			TextView textView = (TextView) findViewById(R.id.profile_textView_user_name);
			avatarImageView.setImageBitmap(user.getAvatarBitmap());
			textView.setText(user.getName());

			// ActionBar setup 2
			ActionBar actionBar = getSupportActionBar();
			actionBar.setTitle(user.getName());

			setSupportProgressBarIndeterminateVisibility(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_profile, menu);

		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		// Retreive the menu items
		MenuItem writeMenuItem = (MenuItem) menu.findItem(R.id.profile_menuItem_write);

		// State management
		if (isUserProfile) {
			writeMenuItem.setVisible(false);
		}

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		// Handle item selection
		switch (item.getItemId()) {
		case android.R.id.home:
			// The Up button behaviour is not the same provided if it is the
			// user profile or another profile
			if (isUserProfile) {
				intent = new Intent(this, HomeActivity.class);
			} else {
				intent = new Intent(this, ContactActivity.class);
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.profile_menuItem_write:
			intent = new Intent(this, WriteActivity.class);
			intent.putExtra(WriteActivity.DEST_USER_ID, user.getUserId());
			startActivityForResult(intent, 0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
