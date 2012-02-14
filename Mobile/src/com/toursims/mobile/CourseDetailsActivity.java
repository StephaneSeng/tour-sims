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
        Intent i = getIntent();
        String courseName = i.getStringExtra("coursename");
 
        //Set title of current screen to course name
        setTitle(courseName);
               
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, MapActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("map").setIndicator("Map")
                      .setContent(intent);
        tabHost.addTab(spec);

        // Tab Step
        intent = new Intent().setClass(this, CourseStepActivity.class);
        spec = tabHost.newTabSpec("step").setIndicator("Step")
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(2);

    }
}
