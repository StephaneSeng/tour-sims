package com.toursims.mobile;

import com.toursims.mobile.model.Course;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends Activity {
	
	public static final String ALREADY_ASKED_TO_RESUME = "already_asked_to_resume";
		
	/**
	 * Android debugging tag
	 */
	@SuppressWarnings("unused")
	private static final String TAG = HomeActivity.class.getName(); 
	private static SharedPreferences settings;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
                
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
		TextView btnGoogleLogin = (TextView)findViewById(R.id.googleLogin);
		
		if (tourSims.getUserName().isEmpty()) {
			// The user is not yet connected 
	//		nameTextView.setText("Welcome, please login with your Google Account...");
			btnGoogleLogin.setVisibility(Button.VISIBLE);
		} else {
	//		nameTextView.setText("Welcome " + tourSims.getUserName() + " !");
			btnGoogleLogin.setVisibility(Button.INVISIBLE);
		}
		
        restartCourse();
	}
    
    private void restartCourse() {
    	
    	settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0); 
    	    	
    	if(settings.contains(CustomPreferences.COURSE_PREFERENCES_STARTED_URL)&&settings.getBoolean(ALREADY_ASKED_TO_RESUME, false)){
    		    		
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			
    		dialog.setTitle(R.string.course_already_started_title);
			dialog.setMessage(R.string.course_already_started_message);
			dialog.setPositiveButton(R.string.course_already_started_go_on, new DialogInterface.OnClickListener() {
					
				public void onClick(DialogInterface dialog, int which) {	
						// TODO Auto-generated method stub
				          Intent activity = new Intent(getApplicationContext(),CourseDetailsActivity.class);
				          activity.putExtra(Course.URL_EXTRA, settings.getString(CustomPreferences.COURSE_PREFERENCES_STARTED_URL, null));
				          activity.putExtra(Course.ID_EXTRA, settings.getInt(CustomPreferences.COURSE_PREFERENCES_STARTED_ID, 0));
				          startActivity(activity);
					}
				});
			
			dialog.setNegativeButton(R.string.course_already_started_discard, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					SharedPreferences.Editor editor = settings.edit();
					editor.remove(CustomPreferences.COURSE_PREFERENCES_STARTED_URL);
					editor.remove(CustomPreferences.COURSE_PREFERENCES_STARTED_TIME_STARTED);
					editor.remove(CustomPreferences.COURSE_PREFERENCES_STARTED_ID);
					dialog.dismiss();
				}
			});
			
			dialog.setNeutralButton(R.string.later, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean(ALREADY_ASKED_TO_RESUME, true);
					editor.commit();
					dialog.dismiss();		
				}
			});
			dialog.show();
    	}   	
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(ALREADY_ASKED_TO_RESUME);
		editor.commit();
    	super.onDestroy();
    }
    
    public void allCityActivityClick(View v){
    	Intent courseGameList = new Intent(getApplicationContext(), CityActivity.class);
        startActivity(courseGameList);
    }
    
    public void poiClick(View v){
    	Intent POI = new Intent(getApplicationContext(),POIActivity.class);
		startActivity(POI);
    }
    
    public void googleLogin(View v){
		Intent GoogleLogin = new Intent(getApplicationContext(), LoginActivity.class);
		startActivityForResult(GoogleLogin, 0);	
    }
    
    public void social(View v){
    	Intent Social = new Intent(getApplicationContext(),SocialActivity.class);	
		startActivity(Social);
    }
    
}
 