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
        
        //Listening to button Course
        btnCourseGame.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
                //Starting a new Intent
                Intent courseGameList = new Intent(getApplicationContext(), CourseGameActivity.class);
                courseGameList.putExtra("courses", COURSES);
                startActivity(courseGameList);
            }
        });
        
        Button btnPOI = (Button) findViewById(R.id.btnPOI);
        // Listening to button POI
        btnPOI.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				//Starting new Intent
				Intent POI = new Intent(getApplicationContext(),POIActivity.class);
				startActivity(POI);
			}
		});

        Button btnSocial = (Button) findViewById(R.id.btnSocial);
        // Listening to button POI
        btnSocial.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				//Starting new Intent
				Intent Social = new Intent(getApplicationContext(),SocialActivity.class);
				startActivity(Social);
			}
		});
}
    
    static final String[] COURSES = new String[] {
    	"LaDoua", "INSA", "Lyon1", "IUT-Feyssine"
    };
}

