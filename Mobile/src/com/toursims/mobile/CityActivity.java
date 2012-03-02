package com.toursims.mobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.model.City;
import com.toursims.mobile.ui.CityAdapter;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CityActivity extends Activity{
	private static List<City> cities;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city);
		
		Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      searchCity(query);
	    }
	    
	    cities = new ArrayList<City>();
	  
	    
	    CourseBDD datasource;
		try {
			datasource = new CourseBDD(this);
			datasource.open();
			cities = datasource.getAllCities();
		    datasource.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			    
	    CityAdapter adapter = new CityAdapter(this, cities,getCacheDir().getAbsolutePath());
	    ListView lv = (ListView) findViewById(R.id.lvListe);
	    lv.setAdapter(adapter);   	
	
	    
	    lv.setOnItemClickListener(new OnItemClickListener() {
	        
	        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
	    // When clicked, show Course details
	          Intent courseDetails = new Intent(getApplicationContext(),CourseGameActivity.class);
	          courseDetails.putExtra("CITY", cities.get(position).getName());
	          startActivity(courseDetails);
	        }
	    	
	    	});
	    }

	private void searchCity(String query) {
		// TODO Auto-generated method stub
		
	}
}
