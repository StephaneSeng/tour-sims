package com.toursims.mobile;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.controller.CourseLoader;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.ui.CourseAdapter;

public class CourseGameActivity extends SherlockActivity {

	private static List<Course> courses = new ArrayList<Course>();
	private ProgressDialog dialog;
	private static EditText searchText;
	private List<Course> courses2;
	private CourseAdapter adapter;
	private ListView lv;

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
		setContentView(R.layout.coursegame);

		new DownloadTask().execute();

		// ActionBarSherlock setup
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(R.drawable.ic_menu_compass_colored);
		actionBar.setTitle("");
	}

	private class DownloadTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			setSupportProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			Bundle bundle = getIntent().getExtras();
			String city = bundle.getString("CITY");

			CourseBDD datasource = null;
			try {
				datasource = new CourseBDD(getApplicationContext());
				datasource.open();
				courses = datasource.getCoursesWithCity(city);

				// Course c1 =
				// CourseLoader.getInstance().parse("http://www.x00b.com/tour3.kml");
				// datasource.insertCourse(c1);
				// c1.setId(datasource.getCourseIdWithURL("http://www.x00b.com/tour3.kml"));
				// datasource.insertPlacemarks(c1);

				datasource.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (datasource != null) {
				datasource.close();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Action on default table row
			lv = (ListView) findViewById(R.id.lvListe);
			TextView noCourse = (TextView) findViewById(R.id.noCourse);
			searchText = (EditText) findViewById(R.id.searchText);

			if (courses.size() > 0) {
				adapter = new CourseAdapter(getApplicationContext(), courses, getCacheDir().getAbsolutePath());
				lv.setAdapter(adapter);
				lv.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// Show informations about the selected course
						Intent courseDetails = new Intent(getApplicationContext(), CourseDetailsActivity.class);
						courseDetails.putExtra(Course.ID_EXTRA, courses.get(position).getId());
						courseDetails.putExtra(Course.URL_EXTRA, courses.get(position).getUrl());
						startActivity(courseDetails);
					}
				});
			} else {
				noCourse.setText("No courses yet");
			}

			lv.setTextFilterEnabled(true);

			searchText.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					filter(s);
				}

				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				public void afterTextChanged(Editable s) {
				}
			});

			searchText.setOnKeyListener(new OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
						closeKeyboard();
					}
					return false;
				}
			});

			// ActionBarSherlock initialization 2
			Bundle bundle = getIntent().getExtras();
			String city = bundle.getString("CITY");
			getSupportActionBar().setTitle(city);

			setSupportProgressBarIndeterminateVisibility(false);
		}
	}

	private void filter(CharSequence s) {
		courses2 = new ArrayList<Course>();
		for (Course c : courses) {
			String courseName = Normalizer.normalize(c.getName().toLowerCase(), Normalizer.Form.NFD);
			String search = Normalizer.normalize(s, Normalizer.Form.NFD);

			if (courseName.contains(search.toLowerCase())) {
				courses2.add(c);
			}
		}
		adapter.setCourses(courses2);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Show informations about the selected course
				Intent courseDetails = new Intent(getApplicationContext(), CourseDetailsActivity.class);
				courseDetails.putExtra(Course.ID_EXTRA, courses.get(position).getId());
				courseDetails.putExtra(Course.URL_EXTRA, courses.get(position).getUrl());
				startActivity(courseDetails);
			}
		});
	}

	private void closeKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		// Handle item selection
		switch (item.getItemId()) {
		case android.R.id.home:
			intent = new Intent(this, CityActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}