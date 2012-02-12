package com.toursims.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button btnCourseGame = (Button) findViewById(R.id.btnCourseGame);
        
        //Listening to button event
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

