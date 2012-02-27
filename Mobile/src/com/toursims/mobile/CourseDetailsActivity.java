package com.toursims.mobile;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
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
        int course_id = bundle.getInt("COURSE_ID");
        intent = new Intent().setClass(this, CourseStepActivity.class);
        intent.putExtra("COURSE_ID", course_id);
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
