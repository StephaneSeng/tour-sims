package com.toursims.mobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.Trace;
import com.toursims.mobile.ui.HomeAdapter;
import com.toursims.mobile.ui.HomeItem;
import com.toursims.mobile.ui.ToolBox;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.toursims.mobile.model.User;
import com.toursims.mobile.model.kml.Point;

public class HomeActivity extends Activity {

	public static final String ALREADY_ASKED_TO_RESUME = "already_asked_to_resume";

	/**
	 * Android debugging tag
	 */
	@SuppressWarnings("unused")
	private static final String TAG = HomeActivity.class.getName();
	private static SharedPreferences settings;
	private static SharedPreferences.Editor editor;

	private List<HomeItem> items2 = new ArrayList<HomeItem>();
	private List<HomeItem> items = new ArrayList<HomeItem>();
	private HomeAdapter adapter;
	private HomeAdapter adapter2;
	private ListView lv;
	private ListView lv2;
	private ImageView recImage;
	private EditText e;

	private LocalizationService s;

	/**
	 * Application context
	 */
	TourSims tourSims;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		recImage = (ImageView) findViewById(R.id.recImage);
		lv = (ListView) findViewById(R.id.lvListe);
		lv2 = (ListView) findViewById(R.id.lvListe2);

		settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);
		settings.edit().remove(ALREADY_ASKED_TO_RESUME).commit();

		// Copy only one time the database after each fresh version
		android.content.pm.PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			String version = pInfo.versionName;
			String last_version = settings.getString(
					CustomPreferences.LATEST_VERSION, "-1");
			settings.edit()
					.putString(CustomPreferences.LATEST_VERSION, version);
			CourseBDD datasource;
			datasource = new CourseBDD(this);
			datasource.copyDataBase(this);
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Application context initialisation
		tourSims = (TourSims) getApplication();

		// ----------------------------------------------------
		// HOME
		// ----------------------------------------------------

		List<HomeItem> items = new ArrayList<HomeItem>();

		items.add(new HomeItem(R.string.home_cities_all,
				R.drawable.ic_menu_compass, CityActivity.class));
		items.add(new HomeItem(R.string.home_poi,
				R.drawable.ic_menu_info_details, POIActivity.class));

		settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);
		if (settings.contains(CustomPreferences.COURSE_STARTED_URL)) {
			try {
				CourseBDD datasource = new CourseBDD(this);
				datasource.open();
				Course c = datasource.getCourseWithId(settings.getInt(
						CustomPreferences.COURSE_STARTED_ID, 1));
				datasource.close();

				if (c.getCoverPictureURL() != null) {
					items.add(new HomeItem(R.string.home_goon_course, c
							.getCoverPictureURL(), new OnClickListener() {
						public void onClick(View v) {
							restartCourse();
						}
					}));
				} else {
					items.add(new HomeItem(R.string.home_goon_course,
							R.drawable.ic_menu_myplaces, new OnClickListener() {
								public void onClick(View v) {
									restartCourse();
								}
							}));
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		items.add(new HomeItem(R.string.home_my_records,
				R.drawable.ic_menu_mylocation, TracesActivity.class));

		adapter = new HomeAdapter(this, items, getCacheDir().getAbsolutePath());
		lv.setAdapter(adapter);

		ToolBox.setListViewHeightBasedOnChildren(lv);
		// ----------------------------------------------------
		// SOCIAL
		// ----------------------------------------------------

		items2.add(new HomeItem(R.string.home_social_map,
				R.drawable.ic_menu_globe, SocialActivity.class));
		items2.add(new HomeItem(R.string.home_social_chat,
				R.drawable.ic_menu_dialog, ChatActivity.class));
		items2.add(new HomeItem(R.string.home_social_contacts,
				R.drawable.ic_menu_allfriends, ContactActivity.class));
		items2.add(new HomeItem(R.string.home_social_profile,
				R.drawable.ic_menu_user, new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(getApplicationContext(),
								ProfileActivity.class);
						intent.putExtra(User.USER_ID_EXTRA, tourSims.getUser()
								.getUserId());
						startActivity(intent);
					}
				}));

		items2.add(new HomeItem(R.string.home_social_login,
				R.drawable.ic_menu_user, new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(getApplicationContext(),
								LoginActivity.class);
						startActivityForResult(intent, 0);
					}
				}));

		adapter2 = new HomeAdapter(this, items2, getCacheDir()
				.getAbsolutePath());
		lv2.setAdapter(adapter2);
		ToolBox.setListViewHeightBasedOnChildren(lv2);

		doBindService();
		recImage();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// User connection management
		// TextView nameTextView = (TextView)findViewById(R.id.nameTextView);
		// TextView btnGoogleLogin = (TextView)findViewById(R.id.googleLogin);

		if (tourSims.isUserLoggedIn()) {
			// TODO: Refactoring!
			if (items2.size() == 5) {
				items2.remove(4);
			}
			items2.get(3).setPictureURL(tourSims.getUser().getAvatar());
			adapter2.setItems(items2);
			lv2.setAdapter(adapter2);
			ToolBox.setListViewHeightBasedOnChildren(lv2);
			// nameTextView.setText("Welcome, please login with your Google Account...");
		} else {
			// The user is not yet connected
			// nameTextView.setText("Welcome " + tourSims.getUserName() + " !");
			// TextView textView = (TextView) findViewById(R.id.home_user_name);
			// ImageView imageView = (ImageView) findViewById(R.id.home_avatar);
			// textView.setText(tourSims.getUser().getName());
			// imageView.setImageBitmap(tourSims.getUser().getAvatarBitmap());
		}

		popUpRestart();
		recImage();
	}

	private void popUpRestart() {
		settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);

		if (settings.contains(CustomPreferences.COURSE_STARTED_URL)
				&& !settings.getBoolean(ALREADY_ASKED_TO_RESUME, true)) {

			AlertDialog.Builder dialog = ToolBox.getDialog(this);

			dialog.setTitle(R.string.course_already_started_title)
					.setMessage(R.string.course_already_started_message)
					.setPositiveButton(R.string.course_already_started_go_on,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									restartCourse();
								}
							});

			dialog.setNegativeButton(R.string.course_already_started_discard,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
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

			dialog.setNeutralButton(R.string.later,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							SharedPreferences.Editor editor = settings.edit();
							editor.putBoolean(ALREADY_ASKED_TO_RESUME, true)
									.commit();
							dialog.dismiss();
						}
					});
			dialog.show();
		}
	}

	private void restartCourse() {
		Intent intent = new Intent(getApplicationContext(),
				CourseStepActivity.class);

		intent.putExtra(Course.URL_EXTRA,
				settings.getString(CustomPreferences.COURSE_STARTED_URL, null))
				.putExtra(Course.ID_EXTRA,
						settings.getInt(CustomPreferences.COURSE_STARTED_ID, 0));

		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(ALREADY_ASKED_TO_RESUME).commit();
		super.onDestroy();
	}

	public void rec(View v) {
		settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);
		editor = settings.edit();

		if (settings.getLong(CustomPreferences.RECORDING_RIGHT_NOW, -1) == -1) {
			final AlertDialog.Builder dialog = ToolBox.getDialog(this);
			dialog.setTitle(R.string.home_record_title);

			e = new EditText(this);
			e.setHint(R.string.home_record_hint);

			dialog.setView(e)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									if (s == null) {
										doBindService();
									}
									Long l = Calendar.getInstance()
											.getTimeInMillis();
									settings = getSharedPreferences(
											CustomPreferences.PREF_FILE, 0);
									editor = settings.edit();
									editor.putLong(
											CustomPreferences.RECORDING_RIGHT_NOW,
											l);
									editor.commit();
									Log.d("TAG", "Start recording" + l);
									s.startRecording(e.getText().toString());
									recImage();
									dialog.dismiss();
								}
							})
					.setNegativeButton(R.string.home_record_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
								}
							}).show();
		} else {
			s.stopRecording();
			editor.remove(CustomPreferences.RECORDING_RIGHT_NOW);
			editor.commit();
			Toast.makeText(this, R.string.home_record_stop_recording,
					Toast.LENGTH_LONG).show();
			recImage();
		}
	}

	private void recImage() {
		settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);
		if (settings.getLong(CustomPreferences.RECORDING_RIGHT_NOW, -1) == -1) {
			recImage.setImageResource(R.drawable.ic_media_play);
		} else {
			recImage.setImageResource(R.drawable.ic_media_pause);
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

	void doBindService() {
		bindService(new Intent(this, LocalizationService.class), mConnection,
				Context.BIND_AUTO_CREATE);
	}

	public void setFlag(View v) {
		if (s == null) {
			doBindService();
		}
		if (s != null) {
			if (s.getKnownLocation() != null) {
				CourseBDD datasource;

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
			}
		}
	}

	public void traceMap(View v) {
		Intent intent = new Intent(getApplicationContext(),
				TraceMapActivity.class);
		startActivity(intent);
	}
}