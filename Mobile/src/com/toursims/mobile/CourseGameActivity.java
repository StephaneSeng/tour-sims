package com.toursims.mobile;

import java.util.List;
import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.controller.KmlParser;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.ui.CourseAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ListView;

public class CourseGameActivity extends Activity {

	private static List<Course> courses;
	
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
    
    CourseBDD datasource = new CourseBDD(this);
	datasource.open();
	datasource.truncate();
	
	Course c1 = KmlParser.getInstance().parse("http://www.x00b.com/tour.kml");
	c1.setUrl("http://www.x00b.com/tour.kml");
	datasource.insertCourse(c1);
	c1.setId(datasource.getCourseIdWithURL("http://www.x00b.com/tour.kml"));
	datasource.insertPlacemarks(c1);
	
	Course c2 = KmlParser.getInstance().parse("http://www.x00b.com/tour2.kml");
	c2.setUrl("http://www.x00b.com/tour2.kml");
	datasource.insertCourse(c2);
	c2.setId(datasource.getCourseIdWithURL("http://www.x00b.com/tour2.kml"));
	datasource.insertPlacemarks(c2);
	
	courses = datasource.getCoursesWithCity(city);
				
	if(courses.size()>0) {
	    CourseAdapter adapter = new CourseAdapter(this, courses);
	    
	    datasource.close();
	    lv.setAdapter(adapter);   
	    lv.setOnItemClickListener(new OnItemClickListener() {
	       
	        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
	    // When clicked, show Course details
	          Intent courseDetails = new Intent(getApplicationContext(),CourseDetailsActivity.class);
	          courseDetails.putExtra("COURSE_ID", courses.get(position).getId());
	          startActivity(courseDetails);
	        
	    }
		});
	} else {
		noCourse.setText("No courses yet");
	}
}
}
		
		
		
		
               
        



