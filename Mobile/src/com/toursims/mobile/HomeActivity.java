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
	
	public static final String PREF_FILE = "toursims_pref_file";
	
	/**
	 * Android debugging tag
	 */
	private static final String TAG = HomeActivity.class.getName(); 
	private static SharedPreferences settings;

	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mùain);
        
        restartCourse();
                
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
      //  ComponentName localizationComponentName = new ComponentName(LocalizationService.class.getPackage().getName(), LocalizationService.class.getName());
      //  ComponentName localizationComponentService = startService(new Intent().setComponent(localizationComponentName));
      //  if (localizationComponentService == null){
      //          Log.e(TAG, "Could not start service " + localizationComponentName.toString());
      //  }
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
		
        restartCourse();
	}
    
    private void restartCourse() {
    	
    	settings = getSharedPreferences(PREF_FILE, 0);    	
    	
    	if(settings.contains(Course.PREFERENCES_STARTED_URL)){
    		    		
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			
    		dialog.setTitle(R.string.course_already_started_title);
			dialog.setMessage(R.string.course_already_started_message);
			dialog.setPositiveButton(R.string.course_already_started_go_on, new DialogInterface.OnClickListener() {
					
				public void onClick(DialogInterface dialog, int which) {	
						// TODO Auto-generated method stub
				          Intent activity = new Intent(getApplicationContext(),CourseDetailsActivity.class);
				          activity.putExtra(Course.URL_EXTRA, settings.getString(Course.PREFERENCES_STARTED_URL, null));
				          activity.putExtra(Course.ID_EXTRA, settings.getInt(Course.PREFERENCES_STARTED_ID, 0));
				          startActivity(activity);
					}
				});
			
			dialog.setNegativeButton(R.string.course_already_started_discard, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					SharedPreferences.Editor editor = settings.edit();
					editor.remove(Course.PREFERENCES_STARTED_URL);
					editor.remove(Course.PREFERENCES_STARTED_TIME_STARTED);
					editor.remove(Course.PREFERENCES_STARTED_ID);
					dialog.dismiss();
				}
			});
			
			dialog.show();
    	} 
    	
    }
}
 