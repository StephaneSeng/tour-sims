package com.toursims.mobile;

import java.util.Calendar;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;

import com.toursims.mobile.model.Course;

public class CourseDetailsActivity extends TabActivity{
	private int course_id;
	private String course_url;
	private ProgressDialog dialog;
	
	@Override
	protected void onPause() {
		super.onPause();
		if (dialog != null) {
			dialog.dismiss();
		}
	}
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursedetails);
             
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab
        Bundle bundle = getIntent().getExtras();

        // Initialize a TabSpec for each tab and add it to the TabHost
        // Tab Social
        intent = new Intent().setClass(this, CourseSocialActivity.class);
        if(bundle.containsKey(Course.ID_EXTRA)){
	        course_id = bundle.getInt(Course.ID_EXTRA);
	        intent.putExtra(Course.ID_EXTRA, course_id);
        }
        
        spec = tabHost.newTabSpec("social").setIndicator("Informations")
                      .setContent(intent);
        tabHost.addTab(spec);
        
        // Tab Map
//        intent = new Intent().setClass(this, CourseStepActivity.class);
        // TODO: Put something else within this tab
        intent = new Intent().setClass(this, CourseSocialActivity.class);

        if(bundle.containsKey(Course.ID_EXTRA)){
	        course_id = bundle.getInt(Course.ID_EXTRA);
	        intent.putExtra(Course.ID_EXTRA, course_id);
        }        
        if(bundle.containsKey(Course.URL_EXTRA)){
	        course_url = bundle.getString(Course.URL_EXTRA);
	        intent.putExtra(Course.URL_EXTRA, course_url);
        }
        if(bundle.containsKey(Course.NEXT_PLACEMARK)){
	        boolean next_placemark = bundle.getBoolean(Course.NEXT_PLACEMARK);
	        intent.putExtra(Course.NEXT_PLACEMARK, next_placemark);
        }
        spec = tabHost.newTabSpec("map").setIndicator("Carte")
                      .setContent(intent);
        tabHost.addTab(spec);
        
        tabHost.setCurrentTab(0);
        
        // Set the event handlers
        Button startButton = (Button) findViewById(R.id.coursedetails_button_start);
        startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog = ProgressDialog.show(CourseDetailsActivity.this, "", "Loading. Please wait...", true);

				// When clicked, show Course details
				Intent courseStep = new Intent(getApplicationContext(),
						CourseStepActivity.class);
				courseStep.putExtra(Course.ID_EXTRA, course_id);
				courseStep.putExtra(Course.URL_EXTRA, course_url);
				startActivity(courseStep);

				// Put that a course is started
				SharedPreferences settings = getSharedPreferences(
						CustomPreferences.PREF_FILE, 0);
				CustomPreferences.removeCourseStarted(settings);

				SharedPreferences.Editor editor = settings.edit();
				editor.putString(CustomPreferences.COURSE_STARTED_URL,
						course_url);
				editor.putInt(CustomPreferences.COURSE_STARTED_ID, course_id);
				Calendar c = Calendar.getInstance();
				int seconds = c.get(Calendar.SECOND);
				editor.putInt(
						CustomPreferences.COURSE_STARTED_TIME_STARTED,
						seconds);
				editor.commit();
			}
		});
    }
    
    
}
