package com.toursims.mobile;

import java.util.Iterator;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.controller.KmlParser;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.kml.Placemark;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CourseGameActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursegamelist);
      
        // Action on default table row
        final TableRow tbrow = (TableRow) findViewById(R.id.tableRow1);
        final TableLayout tblay = (TableLayout) findViewById(R.id.tableLayout1);
                      
        CourseBDD datasource = new CourseBDD(this);
		datasource.open();	
		
		KmlParser k = KmlParser.getInstance();
		Course c1 = k.parse("http://www.x00b.com/tour.kml");
		c1.setId(1);
		c1.setUrl("http://www.x00b.com/tour.kml");
		
		//datasource.insertCourse(c1);
		//datasource.insertPlacemarks(c1);
		
		
       for (Course course : datasource.getAllCourses()) {
    		TableRow row = new TableRow(this);
        	TextView t = new TextView(this);
        	t.setText(course.getName()+course.getId());
        	CheckBox c = new CheckBox(this);
        	row.addView(t);
        	row.addView(c);
        	tblay.addView(row,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));  
        	
	    }
                   
       
       Course course1 = new Course();
       course1.setId(1);
       
      for (Placemark pl : datasource.getAllPlacemarks(course1)) {
	   		TableRow row = new TableRow(this);
	       	TextView t = new TextView(this);
	       	t.setText(pl.getPoint().getCoordinates());
	       	row.addView(t);
	       	tblay.addView(row,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));  
	    }
       
       
        
       datasource.close();

    
               
        tbrow.setOnClickListener(new OnClickListener() {                      
            public void onClick(View arg0) {       	
              // Create an Intent to launch an Activity for Course details
            	Intent courseDetails = new Intent(getApplicationContext(),CourseDetailsActivity.class);
                startActivity(courseDetails);           	
            }
        });
       

     
     
    }

}
