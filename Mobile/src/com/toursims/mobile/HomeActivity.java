package com.toursims.mobile;

import com.toursims.mobile.model.Course;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends Activity {
	
	public static final String PREF_FILE = "PREF_FILE";
	public static final String PREF_CURRENT_COURSE_URL = "PREF_CURRENT_COURSE_URL";
	public static final String PREF_CURRENT_COURSE_TIME_STARTED = "PREF_CURRENT_COURSE_STARTED";
    
	/**
	 * Android debugging tag
	 */
	private static final String TAG = HomeActivity.class.getName(); 
	
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
                Intent courseGameList = new Intent(getApplicationContext(), CityActivity.class);
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
				//Intent Social = new Intent(getApplicationContext(),CityActivity.class);
				
				startActivity(Social);
			}
		});
        
        
        Button btnGoogleLogin = (Button) findViewById(R.id.btnGoogleLogin);
        // Listening to button POI
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				//Starting new Intent
				Intent GoogleLogin = new Intent(getApplicationContext(), LoginActivity.class);
				startActivityForResult(GoogleLogin, 0);
			}
		});
        
        // Start the localization service
        ComponentName localizationComponentName = new ComponentName(LocalizationService.class.getPackage().getName(), LocalizationService.class.getName());
        ComponentName localizationComponentService = startService(new Intent().setComponent(localizationComponentName));
        if (localizationComponentService == null){
                Log.e(TAG, "Could not start service " + localizationComponentName.toString());
        }
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		
		// User connection management
		TourSims tourSims = (TourSims)getApplicationContext();
		TextView nameTextView = (TextView)findViewById(R.id.nameTextView);
		Button btnGoogleLogin = (Button)findViewById(R.id.btnGoogleLogin);
		
		if (tourSims.getUserName().isEmpty()) {
			// The user is not yet connected 
			nameTextView.setText("Welcome, please login with your Google Account...");
			btnGoogleLogin.setVisibility(Button.VISIBLE);
		} else {
			nameTextView.setText("Welcome " + tourSims.getUserName() + " !");
			btnGoogleLogin.setVisibility(Button.INVISIBLE);
		}
	}

    private void resumeCourse(){
    	
    }
    
    private boolean startedCourse() {
    	/*
    	SharedPreferences settings = getSharedPreferences(PREF_FILE, 0);

    	if(settings.contains(PREF_CURRENT_COURSE_URL)){
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);

    		dialog.setTitle();
			dialog.setMessage(placemarks.get(currentPoint).getGreetings());
			dialog.setPositiveButton(R.string.game_play, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
				          Intent gameActivity = new Intent(getApplicationContext(),GameActivity.class);
				          gameActivity.putExtra(Course.COURSE_URL_EXTRA, course.getUrl());
				          gameActivity.putExtra(Course.COURSE_CURRENT_PLACEMARK, currentPoint);
				          startActivity(gameActivity);	
					}
				});
    	}
    	
    	return false;
    }*/
}
 