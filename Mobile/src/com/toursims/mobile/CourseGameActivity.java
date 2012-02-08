package com.toursims.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;

public class CourseGameActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursegamelist);
        
        // Action on default table row
        final TableRow tbrow = (TableRow) findViewById(R.id.tableRow1);
        tbrow.setOnClickListener(new OnClickListener() {                      
            public void onClick(View arg0) {
            	
                // Create an Intent to launch an Activity for Course details
            	Intent courseDetails = new Intent(getApplicationContext(),CourseDetailsActivity.class);
                startActivity(courseDetails);
            }
        });

       
    }
}
