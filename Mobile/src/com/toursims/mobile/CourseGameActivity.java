package com.toursims.mobile;

import java.util.Iterator;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.controller.CourseController;
import com.toursims.mobile.model.Course;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
        
        final CourseController cc = CourseController.getInstance();        
        List<Course> lC = cc.getCourses();
        List<Course> lC2;
        
      /*  Course b = new Course();
        b.setCity("ha");
        b.setCoverPictureURL("n");
        b.setDesc("ha");
        b.setLength(4.0);
        b.setName("ha");
        b.setRating(3.2);
        b.setUrl("hu");
        
        CourseBDD datasource = new CourseBDD(this);
		datasource.open();

		datasource.insertCourse(b);*/
        CourseBDD datasource = new CourseBDD(this);
		datasource.open();
		lC2 = datasource.getAllCourses();    
		
        
       for (Course course : lC2) {
    		TableRow row = new TableRow(this);
        	TextView t = new TextView(this);
        	t.setText(course.getCity());
        	CheckBox c = new CheckBox(this);
        	row.addView(t);
        	row.addView(c);
        	tblay.addView(row,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));  
	    }
       
        
        
    
               
        tbrow.setOnClickListener(new OnClickListener() {                      
            public void onClick(View arg0) {       	
              // Create an Intent to launch an Activity for Course details
            	Intent courseDetails = new Intent(getApplicationContext(),CourseDetailsActivity.class);
                startActivity(courseDetails);           	
            }
        });
       
        datasource.close();

       
    }
    

}
