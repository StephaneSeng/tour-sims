package com.toursims.mobile;

import java.util.ArrayList;
import java.util.List;

import com.toursims.mobile.model.Course;
import com.toursims.mobile.ui.HomeAdapter;
import com.toursims.mobile.ui.HomeItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

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
        
        //----------------------------------------------------
	    // HOME
	    //----------------------------------------------------
	           
        List<HomeItem> items = new ArrayList<HomeItem>();
                
        items.add(new HomeItem(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				allCityActivityClick();
			}
		}, R.string.home_cities_all, R.drawable.ic_menu_compass));
        
        items.add(new HomeItem(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				poiClick();
			}
		}, R.string.home_poi, R.drawable.ic_menu_info_details));
        
	    HomeAdapter adapter = new HomeAdapter(this, items,getCacheDir().getAbsolutePath());
	    ListView lv = (ListView) findViewById(R.id.lvListe);
	    lv.setAdapter(adapter);   	
        
	    //----------------------------------------------------
	    // SOCIAL
	    //----------------------------------------------------
	    
	    List<HomeItem> items2 = new ArrayList<HomeItem>();
        
	    items2.add(new HomeItem(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				social(v);
			}
		}, R.string.home_social_chat, R.drawable.ic_menu_dialog));
	    
        items2.add(new HomeItem(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		}, R.string.home_social_contacts, R.drawable.ic_menu_allfriends));
        
        items2.add(new HomeItem(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				googleLogin(v);
			}
		}, R.string.home_social_profil, R.drawable.ic_menu_user));
        
	    HomeAdapter adapter2 = new HomeAdapter(this, items2,getCacheDir().getAbsolutePath());
	    ListView lv2 = (ListView) findViewById(R.id.lvListe2);
	    lv2.setAdapter(adapter2);   	
        
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
		//TourSims tourSims = (TourSims)getApplicationContext();
		//TextView nameTextView = (TextView)findViewById(R.id.nameTextView);
		//TextView btnGoogleLogin = (TextView)findViewById(R.id.googleLogin);
		/*
		if (tourSims.getUserName().isEmpty()) {
			// The user is not yet connected 
	//		nameTextView.setText("Welcome, please login with your Google Account...");
			btnGoogleLogin.setVisibility(Button.VISIBLE);
		} else {
	//		nameTextView.setText("Welcome " + tourSims.getUserName() + " !");
			btnGoogleLogin.setVisibility(Button.INVISIBLE);
		}
		*/
        restartCourse();
	}
    
    private void restartCourse() {
    	
    	settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0); 
    	    	
    	if(settings.contains(CustomPreferences.COURSE_STARTED_URL)&&settings.getBoolean(ALREADY_ASKED_TO_RESUME, false)){
    		    		
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			
    		dialog.setTitle(R.string.course_already_started_title);
			dialog.setMessage(R.string.course_already_started_message);
			dialog.setPositiveButton(R.string.course_already_started_go_on, new DialogInterface.OnClickListener() {
					
				public void onClick(DialogInterface dialog, int which) {	
						// TODO Auto-generated method stub
				          Intent activity = new Intent(getApplicationContext(),CourseDetailsActivity.class);
				          activity.putExtra(Course.URL_EXTRA, settings.getString(CustomPreferences.COURSE_STARTED_URL, null));
				          activity.putExtra(Course.ID_EXTRA, settings.getInt(CustomPreferences.COURSE_STARTED_ID, 0));
				          startActivity(activity);
					}
				});
			
			dialog.setNegativeButton(R.string.course_already_started_discard, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					SharedPreferences.Editor editor = settings.edit();
					
					for (String item : CustomPreferences.COURSE_ALL) {
						editor.remove(item);
					}
					
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
    
    public void allCityActivityClick(){
    	Intent courseGameList = new Intent(getApplicationContext(), CityActivity.class);
        startActivity(courseGameList);
    }
    
    public void poiClick(){
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
 