package com.toursims.mobile;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.model.City;
import com.toursims.mobile.ui.CityAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CityActivity extends Activity{
	private static List<City> cities;
	private static EditText searchText;
	private CityAdapter adapter;
	private ListView lv;
	private List<City> cities2;
	private boolean firstClick = false;
	private OnItemClickListener listener;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city);
			    
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
		
		cities2 = cities;
			    
		searchText = (EditText) findViewById(R.id.searchText);
		adapter = new CityAdapter(this, cities,getCacheDir().getAbsolutePath());
	    lv = (ListView) findViewById(R.id.lvListe);
	    lv.setTextFilterEnabled(true);
	    lv.setAdapter(adapter);

	    listener = new OnItemClickListener() {
	        
	        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
	    // When clicked, show Course details
	          Intent courseDetails = new Intent(getApplicationContext(),CourseGameActivity.class);
	          courseDetails.putExtra("CITY", cities2.get(position).getName());
	          startActivity(courseDetails);
	        }
	    	
	    	};
	    
	    
	    lv.setOnItemClickListener(listener);
	    
	    searchText.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				filter(s);
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	    	
	    searchText.setOnKeyListener(new OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
			    if (event.getAction()==KeyEvent.ACTION_DOWN&&keyCode==KeyEvent.KEYCODE_ENTER){
			    	closeKeyboard();
			    }
			    return false;
			}
		});
	}
	
	private void filter(CharSequence s) {
		cities2 = new ArrayList<City>();
		for(City c:cities){
			
			String cityName = Normalizer.normalize(c.getName().toLowerCase(), Normalizer.Form.NFD);
			String search = Normalizer.normalize(s, Normalizer.Form.NFD);
			
			if(cityName.contains(search.toLowerCase())){
				cities2.add(c);
			}
		}
		adapter.setCities(cities2);
		lv.setAdapter(adapter);
		listener = new OnItemClickListener() {
	        
	        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
	    // When clicked, show Course details
	          Intent courseDetails = new Intent(getApplicationContext(),CourseGameActivity.class);
	          courseDetails.putExtra("CITY", cities2.get(position).getName());
	          startActivity(courseDetails);
	        }
	    	
	    	};
		
	    lv.setOnItemClickListener(listener);
	}
	
	private void closeKeyboard() {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
	}

}
