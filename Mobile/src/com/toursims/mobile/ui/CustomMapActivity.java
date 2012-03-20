package com.toursims.mobile.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockMapActivity;
import com.google.android.maps.MapView;
import com.toursims.mobile.R;

public class CustomMapActivity extends SherlockMapActivity {

	private MapView mapView;
	private Button switchSatellite;

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void displaySatelliteMap(View v) {
		try {
			mapView.setSatellite(mapView.isSatellite() ? false : true);
			switchSatellite.setText(mapView.isSatellite() ? R.string.map_map : R.string.map_sat);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		try {
			mapView = (MapView) findViewById(R.id.map);
			switchSatellite = (Button) findViewById(R.id.switchSatellite);
			switchSatellite.setText(R.string.map_sat);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
