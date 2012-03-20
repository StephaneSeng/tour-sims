package com.toursims.mobile;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.toursims.mobile.controller.MessageWrapper;
import com.toursims.mobile.controller.UserWrapper;
import com.toursims.mobile.model.User;
import com.toursims.mobile.ui.ToolBox;
import com.toursims.mobile.ui.UserAdapter;

public class WriteActivity extends SherlockActivity {

	public static final String DEST_USER_ID = "DEST_USER_ID";
	public static final String ROOT_MESSAGE_ID = "ROOT_MESSAGE_ID";

	private int destUserId;
	private boolean hasDestUser;
	private User destUser;
	private int rootMessageId;

	/**
	 * Called when the activity is first created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_write);

		new DownloadTask().execute();

		// ActionBarSherlock initialization
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(R.drawable.ic_menu_compose_white);
		actionBar.setTitle(R.string.chat_write_title);
	}

	private class DownloadTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			setSupportProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Shared variables initialization
			if (getIntent().getExtras().containsKey(DEST_USER_ID)) {
				destUserId = getIntent().getExtras().getInt(DEST_USER_ID);
				hasDestUser = true;

				// Retreive the destUser informations
				UserWrapper userWrapper = new UserWrapper(getApplicationContext());
				destUser = userWrapper.GetProfile(destUserId);
			} else {
				destUserId = -1;
				hasDestUser = false;
				destUser = null;
			}
			
			// Also retreive the root_message_id if it exists
			if (getIntent().getExtras().containsKey(ROOT_MESSAGE_ID)) {
				rootMessageId = getIntent().getExtras().getInt(ROOT_MESSAGE_ID);
			} else {
				rootMessageId = -1;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Layout items initialization
			ListView destListView = (ListView) findViewById(R.id.write_listView_dest);

			if (hasDestUser) {
				// Layout initialisation
				List<User> users = new ArrayList<User>();
				users.add(destUser);
				UserAdapter userAdapter = new UserAdapter(getApplicationContext(), users);
				destListView.setAdapter(userAdapter);
				ToolBox.setListViewHeightBasedOnChildren(destListView);
			}

			setSupportProgressBarIndeterminateVisibility(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_write, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		Intent intent;

		// Handle item selection
		switch (item.getItemId()) {
		case android.R.id.home:
			finishActivity(0);
			finish();
			return true;
		case R.id.write_menuItem_send:
			EditText messageEditText = (EditText) findViewById(R.id.write_editText_message);
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			String bestProvider = locationManager.getBestProvider(criteria, true);
			Location lastLocation = null;
			if (bestProvider != null) {
				lastLocation = locationManager.getLastKnownLocation(bestProvider);
			}
			
			User user = null;
			TourSims tourSims = (TourSims) getApplication();
			if (tourSims.isUserLoggedIn()) {
				user = tourSims.getUser();
			}
			
			// Send a message
			MessageWrapper messageWrapper = new MessageWrapper(getApplicationContext());
			if (user != null) {
				if (lastLocation != null) {
					messageWrapper.CreateMessage(messageEditText.getText().toString(), lastLocation.getLatitude(), lastLocation.getLongitude(), rootMessageId, user.getUserId(), destUserId);
				} else {
					messageWrapper.CreateMessage(messageEditText.getText().toString(), 0, 0, rootMessageId, user.getUserId(), destUserId);
				}
			}
			
			finishActivity(0);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
