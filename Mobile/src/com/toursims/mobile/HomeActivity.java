package com.toursims.mobile;


import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.model.Course;

import android.app.ListActivity;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class HomeActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button btnCourseGame = (Button) findViewById(R.id.btnCourseGame);
        Button btn1 = (Button) findViewById(R.id.button1);       
             
        //Listening to button event
       btn1.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent courseGameList = new Intent(getApplicationContext(), CourseDetailsActivity.class);
                startActivity(courseGameList);
            }
        });
        
       btnCourseGame.setOnClickListener(new View.OnClickListener() {
    	   
           public void onClick(View arg0) {
               //Starting a new Intent
               Intent courseGameList = new Intent(getApplicationContext(), CourseGameActivity.class);
               startActivity(courseGameList);
           }
       }); 
    }
       
    static final String[] COURSES = new String[] {
    	"LaDoua", "INSA", "Lyon1", "IUT-Feyssine"
    };
}

