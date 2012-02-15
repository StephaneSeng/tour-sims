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
import android.widget.AdapterView.OnItemClickListener;

import android.widget.ListView;

public class CourseGameActivity extends Activity {

	private static List<Course> courses;
	
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.coursegame);
    
    // Action on default table row
    ListView lv = (ListView) findViewById(R.id.lvListe);
       
    CourseBDD datasource = new CourseBDD(this);
	datasource.open();
	datasource.truncate();
	
	Course c1 = KmlParser.getInstance().parse("http://www.x00b.com/tour.kml");
	c1.setUrl("http://www.x00b.com/tour.kml");
	c1.setId(1);
	datasource.insertCourse(c1);
	datasource.insertPlacemarks(c1);
			
	courses = datasource.getAllCourses();   

    CourseAdapter adapter = new CourseAdapter(this, courses);
    
    datasource.close();
    lv.setAdapter(adapter);   
    lv.setOnItemClickListener(new OnItemClickListener() {
        
        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
    // When clicked, show Course details
          Intent courseDetails = new Intent(getApplicationContext(),CourseStepActivity.class);
          courseDetails.putExtra("COURSE_ID", courses.get(position).getId());
          startActivity(courseDetails);
        }
    
});
}
}
		
		
		
		
               
        



