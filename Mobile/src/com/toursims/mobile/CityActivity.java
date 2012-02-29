package com.toursims.mobile;

import java.util.ArrayList;
import java.util.List;

import com.toursims.mobile.model.City;
import com.toursims.mobile.ui.CityAdapter;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
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
	    cities.add(new City("Berlin", "http://cache.virtualtourist.com/13/3742094-Brandenburger_Tor_Berlin.jpg"));
	    cities.add(new City("Hong Kong", "http://icdn.pro/images/fr/h/o/hong-kong-icone-5565-48.png"));
	    cities.add(new City("Lyon", "http://www.lyon-serrurier.fr/images/logo-ville-lyon.png"));
	    cities.add(new City("Paris", "http://lh5.ggpht.com/I7ZXI1UTSQQIje-omgMRi5KAU2f0-wz2OHCbqrcaKohKu2QicRVBCGiX_b-ZuDK4wc0k5p6g_l9nqWX7NTg=s128-c"));
	    cities.add(new City("Sydney", "http://t3.gstatic.com/images?q=tbn:ANd9GcQ_KGX0AytX6zDGhXiHog_320deXd0flkNjjylh7gPOkKFhBSOGCu6ksPMr"));
	    
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
