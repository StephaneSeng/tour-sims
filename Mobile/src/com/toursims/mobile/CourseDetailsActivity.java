package com.toursims.mobile;

import com.toursims.mobile.model.Course;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class CourseDetailsActivity extends TabActivity{
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursedetails);
        
        //Get course name send by parent Activity
        //Intent i = getIntent();
        //String courseName = i.getStringExtra("coursename");
 
        //Set title of current screen to course name
        //setTitle(courseName);
               
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Initialize a TabSpec for each tab and add it to the TabHost
        // Tab Map
        Bundle bundle = getIntent().getExtras();
        intent = new Intent().setClass(this, CourseStepActivity.class);

        if(bundle.containsKey(Course.ID_EXTRA)){
	        int course_id = bundle.getInt(Course.ID_EXTRA);
	        intent.putExtra(Course.ID_EXTRA, course_id);
        }
        
        if(bundle.containsKey(Course.URL_EXTRA)){
	        String course_url = bundle.getString(Course.URL_EXTRA);
	        intent.putExtra(Course.URL_EXTRA, course_url);
        }
        if(bundle.containsKey(Course.NEXT_PLACEMARK)){
	        boolean next_placemark = bundle.getBoolean(Course.NEXT_PLACEMARK);
	        intent.putExtra(Course.NEXT_PLACEMARK, next_placemark);
        } 
        spec = tabHost.newTabSpec("map").setIndicator("Map")
                      .setContent(intent);
        tabHost.addTab(spec);

        // Tab Step
        intent = new Intent().setClass(this, CourseMapActivity.class);
        
        spec = tabHost.newTabSpec("step").setIndicator("Step")
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(2);

    }
}
