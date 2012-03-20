package com.toursims.mobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.controller.CourseWrapper;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.User;
import com.toursims.mobile.model.kml.Point;
import com.toursims.mobile.ui.HomeAdapter;
import com.toursims.mobile.ui.HomeItem;
import com.toursims.mobile.ui.ToolBox;

public class HomeActivity extends SherlockActivity {

	public static final String ALREADY_ASKED_TO_RESUME = "already_asked_to_resume";

	private static SharedPreferences settings;
	private static SharedPreferences.Editor editor;

	private List<HomeItem> items = new ArrayList<HomeItem>();
	private List<HomeItem> items2 = new ArrayList<HomeItem>();
	private HomeAdapter adapter;
	private HomeAdapter adapter2;
	private ListView lv;
	private ListView lv2;
	private EditText e;
	private TextView warningsTextView;
	private View warningsView;
	private TextView warningSocialTextView;
	private TextView warningNoProviderTextView;
	private TextView warningNoConnectionTextView;
	private LocalizationService s;

	/**
	 * Application context
	 */
	TourSims tourSims;

	/**
	 * Called when the activity is first created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		lv = (ListView) findViewById(R.id.lvListe);
		lv2 = (ListView) findViewById(R.id.lvListe2);
		warningsTextView = (TextView) findViewById(R.id.home_textView_warnings);
		warningsView = (View) findViewById(R.id.home_view_warnings);
		warningSocialTextView = (TextView) findViewById(R.id.home_textView_warning_social);
		warningNoProviderTextView = (TextView) findViewById(R.id.home_textView_warning_no_provider);
		warningNoConnectionTextView = (TextView) findViewById(R.id.home_textView_warning_no_connection);

		settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);
		settings.edit().remove(ALREADY_ASKED_TO_RESUME).commit();

		// Copy only one time the database after each fresh version
		android.content.pm.PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String version = pInfo.versionName;
			String last_version = settings.getString(CustomPreferences.LATEST_VERSION, "-1");
			Log.d("TEST", "version : " + version);
			Log.d("TEST", "last_version : " + last_version);

			if (!version.equals(last_version)) {
				settings.edit().putString(CustomPreferences.LATEST_VERSION, version).commit();
				CourseBDD datasource;
				datasource = new CourseBDD(this);
				datasource.copyDataBase(this);

				CourseWrapper courseWrapper = new CourseWrapper(getApplicationContext());
				courseWrapper.GetCourses();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Application context initialisation
		tourSims = (TourSims) getApplication();

		// Restore the user session if someone was already logged in
		SharedPreferences loginSettings = getSharedPreferences(LoginActivity.LOGIN_PREFERENCES, 0);
		if (loginSettings.getBoolean(LoginActivity.LOGIN_PREFERENCES_LOGGEDIN, false)) {
			User user = new User(loginSettings.getInt(LoginActivity.LOGIN_PREFERENCES_ID, 0), loginSettings.getString(LoginActivity.LOGIN_PREFERENCES_NAME, ""), loginSettings.getString(
					LoginActivity.LOGIN_PREFERENCES_IMAGE, ""));
			tourSims.setUser(user);
			tourSims.setUserLoggedIn(true);
		} else {
			tourSims.setUserLoggedIn(false);
		}

		// HOME LIST
		List<HomeItem> items = new ArrayList<HomeItem>();
		items.add(new HomeItem(R.string.home_cities_all, R.drawable.ic_menu_compass_colored, CityActivity.class));
		items.add(new HomeItem(R.string.home_poi, R.drawable.ic_menu_marker_colored, POIActivity.class));
		items.add(new HomeItem(R.string.home_my_records, R.drawable.ic_menu_mylocation_colored, TracesActivity.class));

		settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);
		if (settings.contains(CustomPreferences.COURSE_STARTED_URL)) {
			Log.d("TEST", "OK");
			Log.d("TEST", "OK : " + settings.getInt(CustomPreferences.COURSE_STARTED_ID, 1));

			CourseBDD datasource = null;
			try {
				datasource = new CourseBDD(this);
				datasource.open();
				Course c = datasource.getCourseWithId(settings.getInt(CustomPreferences.COURSE_STARTED_ID, 1));
				datasource.close();

				if ((c != null) && (c.getCoverPictureURL() != null)) {
					items.add(new HomeItem(R.string.home_goon_course, c.getCoverPictureURL(), new OnClickListener() {
						public void onClick(View v) {
							restartCourse();
						}
					}));
				} else {
					items.add(new HomeItem(R.string.home_goon_course, R.drawable.ic_menu_myplaces, new OnClickListener() {
						public void onClick(View v) {
							restartCourse();
						}
					}));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			if (datasource != null) {
				datasource.close();
			}
		}

		adapter = new HomeAdapter(this, items, getCacheDir().getAbsolutePath());
		lv.setAdapter(adapter);
		ToolBox.setListViewHeightBasedOnChildren(lv);

		// SOCIAL LIST
		items2.add(new HomeItem(R.string.home_social_map, R.drawable.ic_menu_globe_colored, SocialActivity.class));
		items2.add(new HomeItem(R.string.home_social_chat, R.drawable.ic_menu_dialog_colored, ChatActivity.class));
		items2.add(new HomeItem(R.string.home_social_contacts, R.drawable.ic_menu_allfriends_colored, ContactActivity.class));
		items2.add(new HomeItem(R.string.home_social_profile, R.drawable.ic_menu_user_colored, new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
				intent.putExtra(User.USER_ID_EXTRA, tourSims.getUser().getUserId());
				intent.putExtra(ProfileActivity.IS_USER_PROFILE, true);
				startActivity(intent);
			}
		}));

		onResume();

		bindService(new Intent(this, LocalizationService.class), mConnection, Context.BIND_AUTO_CREATE);
		invalidateOptionsMenu();

		// ActionBarSherlock setup
		getSupportActionBar().setHomeButtonEnabled(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		List<HomeItem> items3 = new ArrayList<HomeItem>();
		items3.addAll(items2);

		if (tourSims.isUserLoggedIn()) {
			// The user is already connected
			// Set the user profile avatar
			// items3.get(items3.size() -
			// 1).setPictureURL(tourSims.getUser().getAvatar());
			items3.get(items3.size() - 1).setPictureURL(R.drawable.ic_menu_user_colored);
			lv2.setVisibility(ListView.VISIBLE);
			warningSocialTextView.setVisibility(TextView.GONE);
		} else {
			// The user is not yet connected
			lv2.setVisibility(ListView.GONE);
			warningSocialTextView.setVisibility(TextView.VISIBLE);
		}

		adapter2 = new HomeAdapter(this, items3, getCacheDir().getAbsolutePath());
		lv2.setAdapter(adapter2);
		ToolBox.setListViewHeightBasedOnChildren(lv2);

		// Check the device position providers
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = locationManager.getBestProvider(criteria, true);

		// Display a warning message if an enabled provider cannot be found
		if (bestProvider == null) {
			warningsTextView.setVisibility(TextView.VISIBLE);
			warningsView.setVisibility(View.VISIBLE);
			warningNoProviderTextView.setVisibility(TextView.VISIBLE);
			warningNoConnectionTextView.setVisibility(TextView.GONE);
		} else if ((bestProvider.equals(LocationManager.NETWORK_PROVIDER))
				&& ((connectivityManager.getActiveNetworkInfo() == null) || ((connectivityManager.getActiveNetworkInfo() != null) && (!connectivityManager.getActiveNetworkInfo()
						.isConnectedOrConnecting())))) {
			// Display another warning message if no Internet access is
			// currently available when the network is the current best provider
			warningsTextView.setVisibility(TextView.VISIBLE);
			warningsView.setVisibility(View.VISIBLE);
			warningNoProviderTextView.setVisibility(TextView.GONE);
			warningNoConnectionTextView.setVisibility(TextView.VISIBLE);
		} else {
			warningsTextView.setVisibility(TextView.GONE);
			warningsView.setVisibility(View.GONE);
			warningNoProviderTextView.setVisibility(TextView.GONE);
			warningNoConnectionTextView.setVisibility(TextView.GONE);
		}

		popUpRestart();
		invalidateOptionsMenu();
	}

	private void popUpRestart() {
		settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);

		if (settings.contains(CustomPreferences.COURSE_STARTED_URL) && !settings.getBoolean(ALREADY_ASKED_TO_RESUME, true)) {

			AlertDialog.Builder dialog = ToolBox.getDialog(this);

			dialog.setTitle(R.string.course_already_started_title).setMessage(R.string.course_already_started_message)
					.setPositiveButton(R.string.course_already_started_go_on, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							restartCourse();
						}
					});

			dialog.setNegativeButton(R.string.course_already_started_discard, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					SharedPreferences.Editor editor = settings.edit();

					for (String item : CustomPreferences.COURSE_ALL) {
						editor.remove(item).commit();
					}

					items.remove(items.size() - 1);
					adapter.setItems(items);
					lv.setAdapter(adapter);
					ToolBox.setListViewHeightBasedOnChildren(lv);
					dialog.dismiss();
				}
			});

			dialog.setNeutralButton(R.string.later, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean(ALREADY_ASKED_TO_RESUME, true).commit();
					dialog.dismiss();
				}
			});
			dialog.show();
		}
	}

	private void restartCourse() {
		Intent intent = new Intent(getApplicationContext(), CourseStepActivity.class);

		intent.putExtra(Course.URL_EXTRA, settings.getString(CustomPreferences.COURSE_STARTED_URL, null)).putExtra(Course.ID_EXTRA, settings.getInt(CustomPreferences.COURSE_STARTED_ID, 0));

		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(ALREADY_ASKED_TO_RESUME).commit();

		unbindService(mConnection);
		super.onDestroy();
	}

	public void onClickWarningSocial(View v) {
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		startActivityForResult(intent, 0);
	}

	public void onClickWarningNoProvider(View v) {
		// Recheck
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = locationManager.getBestProvider(criteria, true);
		if (bestProvider == null) {
			// The problem is still there
			Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, 0);
		} else {
			reloadActivity();
		}
	}

	public void onClickWarningNoConnection(View v) {
		// Recheck
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String bestProvider = locationManager.getBestProvider(criteria, true);
		if ((bestProvider != null)
				&& ((bestProvider.equals(LocationManager.NETWORK_PROVIDER)) && ((connectivityManager.getActiveNetworkInfo() == null) || ((connectivityManager.getActiveNetworkInfo() != null) && (!connectivityManager
						.getActiveNetworkInfo().isConnectedOrConnecting()))))) {
			// The problem is still there
			Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
			startActivityForResult(intent, 0);
		} else {
			reloadActivity();
		}

	}

	public void rec(View v) {
		settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);
		editor = settings.edit();

		if (settings.getLong(CustomPreferences.RECORDING_RIGHT_NOW, -1) == -1) {
			final AlertDialog.Builder dialog = ToolBox.getDialog(this);
			dialog.setTitle(R.string.home_record_title);

			e = new EditText(this);
			e.setHint(R.string.home_record_hint);

			dialog.setView(e).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// if (s == null) {
					// bindService(new Intent(this,
					// LocalizationService.class),
					// mConnection, Context.BIND_AUTO_CREATE);
					// }
					Long l = Calendar.getInstance().getTimeInMillis();
					settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);
					editor = settings.edit();
					editor.putLong(CustomPreferences.RECORDING_RIGHT_NOW, l);
					editor.commit();
					Log.d("TAG", "Start recording" + l);
					s.startRecording(e.getText().toString());
					invalidateOptionsMenu();
					dialog.dismiss();
				}
			}).setNegativeButton(R.string.home_record_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
				}
			}).show();
		} else {
			s.stopRecording();
			editor.remove(CustomPreferences.RECORDING_RIGHT_NOW);
			editor.commit();
			Toast.makeText(this, R.string.home_record_stop_recording, Toast.LENGTH_LONG).show();
			invalidateOptionsMenu();
		}
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder binder) {
			s = ((LocalizationService.MyBinder) binder).getService();
		}

		public void onServiceDisconnected(ComponentName className) {
			s = null;
		}
	};

	public void setFlag(View v) {
		// if (s == null) {
		// doBindService();
		// }
		if (s != null) {
			if (s.getKnownLocation() != null) {
				CourseBDD datasource = null;

				Point item = new Point();
				Location l = s.getKnownLocation();

				item.setMillis(l.getTime());
				item.setLongitude(l.getLongitude());
				item.setLatitude(l.getLatitude());

				try {
					datasource = new CourseBDD(this);
					datasource.open();
					datasource.insertFlag(item);
					datasource.close();
					Toast.makeText(this, "Flag OK", Toast.LENGTH_LONG).show();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (datasource != null) {
					datasource.close();
				}
			}
		}
	}

	public void traceMap(View v) {
		Intent intent = new Intent(getApplicationContext(), TraceMapActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.home, menu);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		// Retreive the menu items
		MenuItem loginMenuItem = (MenuItem) menu.findItem(R.id.home_menuItem_login);
		MenuItem setFlagMenuItem = (MenuItem) menu.findItem(R.id.home_menuItem_setFlag);
		MenuItem recMenuItem = (MenuItem) menu.findItem(R.id.home_menuItem_recImage);
		MenuItem preferencesMenuItem = (MenuItem) menu.findItem(R.id.home_menuItem_preferences);
		MenuItem exitMenuItem = (MenuItem) menu.findItem(R.id.home_menuItem_exit);

		// State management
		if (tourSims.isUserLoggedIn()) {
			loginMenuItem.setVisible(false);
			setFlagMenuItem.setVisible(true);
			recMenuItem.setVisible(true);
			preferencesMenuItem.setVisible(true);
			exitMenuItem.setVisible(true);
		} else {
			loginMenuItem.setVisible(true);
			setFlagMenuItem.setVisible(false);
			recMenuItem.setVisible(false);
			preferencesMenuItem.setVisible(false);
			exitMenuItem.setVisible(false);
		}

		// Other icon modifications
		settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);
		if (settings.getLong(CustomPreferences.RECORDING_RIGHT_NOW, -1) == -1) {
			recMenuItem.setIcon(R.drawable.ic_media_play_white);
		} else {
			recMenuItem.setIcon(R.drawable.ic_media_pause_white);
		}

		return true;
	}

	public void reloadActivity() {
		// Reload the current Activity
		// See:
		// http://stackoverflow.com/questions/1397361/how-do-i-restart-an-android-activity
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		// Handle item selection
		switch (item.getItemId()) {
		case R.id.home_menuItem_login:
			onClickWarningSocial(item.getActionView());
			return true;
		case R.id.home_menuItem_setFlag:
			setFlag(item.getActionView());
			return true;
		case R.id.home_menuItem_recImage:
			rec(item.getActionView());
			return true;
		case R.id.home_menuItem_update:
			// Clear the saved course if existing
			settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);
			if (settings.contains(CustomPreferences.COURSE_STARTED_URL)) {
				settings.edit().remove(CustomPreferences.COURSE_STARTED_URL).commit();
			}

			CourseWrapper courseWrapper = new CourseWrapper(getApplicationContext());
			courseWrapper.GetCourses();

			reloadActivity();

			return true;
		case R.id.home_menuItem_preferences:
			intent = new Intent(getApplicationContext(), PrefActivity.class);
			startActivity(intent);
			return true;
		case R.id.home_menuItem_exit:
			// The user is signing out
			tourSims.setUserLoggedIn(false);
			SharedPreferences loginSettings = getSharedPreferences(LoginActivity.LOGIN_PREFERENCES, 0);
			SharedPreferences.Editor editor = loginSettings.edit();
			editor.putBoolean(LoginActivity.LOGIN_PREFERENCES_LOGGEDIN, false);
			editor.commit();

			reloadActivity();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}