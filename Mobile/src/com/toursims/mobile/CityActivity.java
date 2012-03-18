package com.toursims.mobile;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.model.City;
import com.toursims.mobile.ui.CityAdapter;

public class CityActivity extends SherlockActivity {
	private static List<City> cities;
	private static EditText searchText;
	private CityAdapter adapter;
	private ListView lv;
	private List<City> cities2;
	private OnItemClickListener listener;

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city);

		new DownloadTask().execute();

		// ActionBarSherlock setup
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(R.drawable.ic_menu_compass_colored);
		actionBar.setTitle(R.string.home_cities_all);
	}

	private class DownloadTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			setSupportProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			cities = new ArrayList<City>();
			CourseBDD datasource = null;
			try {
				datasource = new CourseBDD(getApplicationContext());
				datasource.open();
				cities = datasource.getAllCities();
				datasource.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (datasource != null) {
				datasource.close();
			}

			cities2 = cities;
			adapter = new CityAdapter(getApplicationContext(), cities, getCacheDir().getAbsolutePath());

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Layout initialization
			searchText = (EditText) findViewById(R.id.searchText);
			lv = (ListView) findViewById(R.id.lvListe);

			listener = new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// When clicked, show Course details
					Intent courseDetails = new Intent(getApplicationContext(), CourseGameActivity.class);
					courseDetails.putExtra("CITY", cities2.get(position).getName());
					startActivity(courseDetails);
				}
			};

			lv.setTextFilterEnabled(true);
			lv.setAdapter(adapter);
			lv.setOnItemClickListener(listener);

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

			setSupportProgressBarIndeterminateVisibility(false);
		}
	}

	private void filter(CharSequence s) {
		cities2 = new ArrayList<City>();
		for (City c : cities) {

			String cityName = Normalizer.normalize(c.getName().toLowerCase(), Normalizer.Form.NFD);
			String search = Normalizer.normalize(s, Normalizer.Form.NFD);

			if (cityName.contains(search.toLowerCase())) {
				cities2.add(c);
			}
		}
		adapter.setCities(cities2);
		lv.setAdapter(adapter);
		listener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// When clicked, show Course details
				Intent courseDetails = new Intent(getApplicationContext(), CourseGameActivity.class);
				courseDetails.putExtra("CITY", cities2.get(position).getName());
				startActivity(courseDetails);
			}

		};

		lv.setOnItemClickListener(listener);
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
			intent = new Intent(this, HomeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
