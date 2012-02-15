package com.toursims.mobile;

import java.util.ArrayList;
import java.util.List;

import com.toursims.mobile.model.City;
import com.toursims.mobile.ui.CityAdapter;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

public class CityActivity extends Activity{
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city);
		
		Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      searchCity(query);
	    }
	    
	    List<City> cities = new ArrayList<City>();
	    cities.add(new City("Lyon", ""));
	    cities.add(new City("Paris", ""));

	    CityAdapter adapter = new CityAdapter(this, cities);
	    ListView lv = (ListView) findViewById(R.id.lvListe);
	    lv.setAdapter(adapter);   
	    
	}

	private void searchCity(String query) {
		// TODO Auto-generated method stub
		
	}
}
