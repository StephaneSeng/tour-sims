package com.toursims.mobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.controller.CourseLoader;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.ui.CourseAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ListView;

public class CourseGameActivity extends Activity {
		
	private static List<Course> courses = new ArrayList<Course>();
	
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
	
/*	Course c1 = CourseLoader.getInstance().parse("http://www.x00b.com/tour.kml");
	datasource.insertCourse(c1);
	c1.setId(datasource.getCourseIdWithURL("http://www.x00b.com/tour.kml"));
	datasource.insertPlacemarks(c1);
	
	Course c2 = CourseLoader.getInstance().parse("http://www.x00b.com/tour2.kml");
	datasource.insertCourse(c2);
	c2.setId(datasource.getCourseIdWithURL("http://www.x00b.com/tour2.kml"));
	datasource.insertPlacemarks(c2);
	
	Course c3 = CourseLoader.getInstance().parse("http://www.x00b.com/tour3.kml");
	datasource.insertCourse(c3);
	c3.setId(datasource.getCourseIdWithURL("http://www.x00b.com/tour3.kml"));
	datasource.insertPlacemarks(c3);*/
		
				
	if(courses.size()>0) {
	    CourseAdapter adapter = new CourseAdapter(this, courses,getCacheDir().getAbsolutePath());
	    
	    lv.setAdapter(adapter);   
	    lv.setOnItemClickListener(new OnItemClickListener() {
	       
	        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
	    // When clicked, show Course details
	          Intent courseDetails = new Intent(getApplicationContext(),CourseDetailsActivity.class);
	          courseDetails.putExtra(Course.COURSE_ID_EXTRA, courses.get(position).getId());
	          courseDetails.putExtra(Course.COURSE_URL_EXTRA, courses.get(position).getUrl());
	          startActivity(courseDetails);
	          
	          //Put that a course is started
	          SharedPreferences settings = getSharedPreferences(HomeActivity.PREF_FILE, 0);
	          SharedPreferences.Editor editor = settings.edit();
	          editor.putString(HomeActivity.PREF_CURRENT_COURSE_URL, courses.get(position).getUrl());
	          Calendar c = Calendar.getInstance(); 
	          int seconds = c.get(Calendar.SECOND);
	          editor.putInt(HomeActivity.PREF_CURRENT_COURSE_STARTED, seconds);
	          // Commit the edits!
	          editor.commit();
	    }
		});
	} else {
		noCourse.setText("No courses yet");
	}
}
}
		
		
		
		
               
        



