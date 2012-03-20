package com.toursims.mobile;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.model.Trace;
import com.toursims.mobile.model.kml.Point;
import com.toursims.mobile.ui.CustomMapActivity;
import com.toursims.mobile.ui.FlagAdapter;
import com.toursims.mobile.ui.ToolBox;
import com.toursims.mobile.ui.TraceAdapter;

public class TracesActivity extends CustomMapActivity {

	private static List<Trace> items = new ArrayList<Trace>();
	private static List<Point> items2 = new ArrayList<Point>();
	private TraceAdapter adapter;
	private FlagAdapter adapter2;
	private ListView lv;
	private ListView lv2;
	private CourseBDD datasource = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.traces);

		try {
			datasource = new CourseBDD(this);
			datasource.open();
			items = datasource.getAllTraces();
			items2 = datasource.getAllPoints();
			datasource.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (datasource != null) {
			datasource.close();
		}

		adapter = new TraceAdapter(this, items);
		lv = (ListView) findViewById(R.id.lvListe);
		lv.setTextFilterEnabled(true);
		lv.setAdapter(adapter);

		adapter2 = new FlagAdapter(this, items2);
		lv2 = (ListView) findViewById(R.id.lvListe2);
		lv2.setTextFilterEnabled(true);
		lv2.setAdapter(adapter2);

		ToolBox.setListViewHeightBasedOnChildren(lv);
		ToolBox.setListViewHeightBasedOnChildren(lv2);

		// ActionBarSherlock setup
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(R.string.home_my_records);
		actionBar.setIcon(R.drawable.ic_menu_mylocation_colored);
	}

	@Override
	protected void onDestroy() {
		if (datasource != null) {
			datasource.close();
		}

		super.onDestroy();
	}

	public void delete(View v) {
		adapter.deleteItems();

		CourseBDD datasource = null;
		try {
			datasource = new CourseBDD(this);
			datasource.open();
			items = datasource.getAllTraces();
		} catch (Exception e) {
		}
		
		if (datasource != null) {
			datasource.close();
		}

		adapter = new TraceAdapter(this, items);
		lv.setTextFilterEnabled(true);
		lv.setAdapter(adapter);

		ToolBox.setListViewHeightBasedOnChildren(lv);
		ToolBox.setListViewHeightBasedOnChildren(lv2);
	}

	public void share(View v) {
		adapter.share();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_traces, menu);

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
		case R.id.traces_menuItem_share:
			share(item.getActionView());
			return true;
		case R.id.traces_menuItem_delete:
			delete(item.getActionView());
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}