package com.toursims.mobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.ui.CourseAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ListView;

public class CourseGameActivity extends Activity {
		
	private static List<Course> courses = new ArrayList<Course>();
	private ProgressDialog dialog;
	
	@Override
	protected void onPause() {
		super.onPause();
		if(dialog!=null){
			dialog.dismiss();
		}
	}
	
	public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.coursegame);
    
    // Action on default table row
    ListView lv = (ListView) findViewById(R.id.lvListe);
    TextView title = (TextView) findViewById(R.id.title);
    TextView noCourse = (TextView) findViewById(R.id.noCourse);
    
    Bundle bundle = getIntent().getExtras();
    String city = bundle.getString("CITY");
    title.setText(city);
    
    CourseBDD datasource;
	try {
		datasource = new CourseBDD(this);
		datasource.open();
		courses = datasource.getCoursesWithCity(city);
	    datasource.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
//	Course c1 = CourseLoader.getInstance().parse("http://www.x00b.com/tour.kml");
//	datasource.insertCourse(c1);
//	c1.setId(datasource.getCourseIdWithURL("http://www.x00b.com/tour.kml"));
//	datasource.insertPlacemarks(c1);
		
				
	if(courses.size()>0) {
	    CourseAdapter adapter = new CourseAdapter(this, courses,getCacheDir().getAbsolutePath());
	    lv.setAdapter(adapter);   
	    lv.setOnItemClickListener(new OnItemClickListener() {
	       
	        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
	            
	        	dialog = ProgressDialog.show(CourseGameActivity.this, "", 
	                    "Loading. Please wait...", true);
	            
	    // When clicked, show Course details
	          Intent courseDetails = new Intent(getApplicationContext(),CourseStepActivity.class);
	          courseDetails.putExtra(Course.ID_EXTRA, courses.get(position).getId());
	          courseDetails.putExtra(Course.URL_EXTRA, courses.get(position).getUrl());
	          startActivity(courseDetails);
	          
	          //Put that a course is started
	          SharedPreferences settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);
	          CustomPreferences.removeCourseStarted(settings);

	          SharedPreferences.Editor editor = settings.edit();
	          editor.putString(CustomPreferences.COURSE_STARTED_URL, courses.get(position).getUrl());
	          editor.putInt(CustomPreferences.COURSE_STARTED_ID, courses.get(position).getId());
	          Calendar c = Calendar.getInstance(); 
	          int seconds = c.get(Calendar.SECOND);
	          editor.putInt(CustomPreferences.COURSE_STARTED_TIME_STARTED, seconds);
	          editor.commit();
	    }
		});
	} else {
		noCourse.setText("No courses yet");
	}
}
}