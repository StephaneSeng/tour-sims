package com.toursims.mobile;

import java.util.ArrayList;
import java.util.List;

import com.toursims.mobile.model.Course;
import com.toursims.mobile.ui.HomeAdapter;
import com.toursims.mobile.ui.HomeItem;
import com.toursims.mobile.ui.ToolBox;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.User;
import com.toursims.mobile.ui.HomeAdapter;
import com.toursims.mobile.ui.HomeItem;

public class HomeActivity extends Activity {
	
	public static final String ALREADY_ASKED_TO_RESUME = "already_asked_to_resume";
		
	/**
	 * Android debugging tag
	 */
//	@SuppressWarnings("unused")
	private static final String TAG = HomeActivity.class.getName(); 
	private static SharedPreferences settings;
	private static List<HomeItem> items = new ArrayList<HomeItem>();
	private static HomeAdapter adapter;
	private static ListView lv;
	
	/**
	 * Application context
	 */
	TourSims tourSims;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
    	settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0); 
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(ALREADY_ASKED_TO_RESUME);
		editor.commit();
        
        // Application context initialisation
        tourSims = (TourSims) getApplication();
        
        //----------------------------------------------------
	    // HOME
	    //----------------------------------------------------
	           
        
                
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
        
        if(settings.contains(CustomPreferences.COURSE_STARTED_URL)){
            items.add(new HomeItem(new OnClickListener() {
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				restartCourse();
    			}
    		}, R.string.home_goon_course, R.drawable.ic_menu_myplaces));
        }
        
	    adapter = new HomeAdapter(this, items,getCacheDir().getAbsolutePath());
	    lv = (ListView) findViewById(R.id.lvListe);
	    lv.setAdapter(adapter);
	    ToolBox.setListViewHeightBasedOnChildren(lv);

        
	    //----------------------------------------------------
	    // SOCIAL
	    //----------------------------------------------------
	    
	    List<HomeItem> items2 = new ArrayList<HomeItem>();
        
	    items2.add(new HomeItem(new OnClickListener() {
			
			public void onClick(View v) {
				social(v);
			}
		}, R.string.home_social_map, R.drawable.ic_menu_globe));
	    
	    items2.add(new HomeItem(new OnClickListener() {
			
			public void onClick(View v) {
				chatClick(v);
			}
		}, R.string.home_social_chat, R.drawable.ic_menu_dialog));
	    
        items2.add(new HomeItem(new OnClickListener() {
			
			public void onClick(View v) {
				contactsClick(v);
			}
			}, R.string.home_social_contacts, R.drawable.ic_menu_allfriends));
        
        items2.add(new HomeItem(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				profileClick(v);
			}
		}, R.string.home_social_profile, R.drawable.ic_menu_user));
        
        items2.add(new HomeItem(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				googleLoginClick(v);
			}
		}, R.string.home_social_login, R.drawable.ic_menu_user));
        
	    HomeAdapter adapter2 = new HomeAdapter(this, items2,getCacheDir().getAbsolutePath());
	    ListView lv2 = (ListView) findViewById(R.id.lvListe2);
	    lv2.setAdapter(adapter2);   	
	    ToolBox.setListViewHeightBasedOnChildren(lv2);

	    
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
//		TextView nameTextView = (TextView)findViewById(R.id.nameTextView);
//		TextView btnGoogleLogin = (TextView)findViewById(R.id.googleLogin);
		
		if (!tourSims.isUserLoggedIn()) {
			// The user is not yet connected 
//			nameTextView.setText("Welcome, please login with your Google Account...");
		} else {
//			nameTextView.setText("Welcome " + tourSims.getUserName() + " !");
			TextView textView = (TextView) findViewById(R.id.home_user_name);
			ImageView imageView = (ImageView) findViewById(R.id.home_avatar);
			textView.setText(tourSims.getUser().getName());
			imageView.setImageBitmap(tourSims.getUser().getAvatarBitmap());
		}
		
        popUpRestart();
	}
    
    private void popUpRestart(){
    	settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0); 
    	
    	if(settings.contains(CustomPreferences.COURSE_STARTED_URL)&&settings.getBoolean(ALREADY_ASKED_TO_RESUME, true)){
    		    		
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			
    		dialog.setTitle(R.string.course_already_started_title);
			dialog.setMessage(R.string.course_already_started_message);
			dialog.setPositiveButton(R.string.course_already_started_go_on, new DialogInterface.OnClickListener() {
					
				public void onClick(DialogInterface dialog, int which) {	
						// TODO Auto-generated method stub
				          restartCourse();
					}
				});
			
			dialog.setNegativeButton(R.string.course_already_started_discard, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					SharedPreferences.Editor editor = settings.edit();
					
					for (String item : CustomPreferences.COURSE_ALL) {
						editor.remove(item);
					}
					
					items.remove(items.size()-1);
					adapter.setItems(items);
					lv.setAdapter(adapter);
				    ToolBox.setListViewHeightBasedOnChildren(lv);
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
    
    private void restartCourse() {
    	Intent activity = new Intent(getApplicationContext(),CourseStepActivity.class);
        activity.putExtra(Course.URL_EXTRA, settings.getString(CustomPreferences.COURSE_STARTED_URL, null));
        activity.putExtra(Course.ID_EXTRA, settings.getInt(CustomPreferences.COURSE_STARTED_ID, 0));
        startActivity(activity);
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
    
    public void chatClick(View v){
		Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
		startActivity(intent);	
    }
    
    public void contactsClick(View v){
		Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
		startActivity(intent);	
    }
    
    public void profileClick(View v){
		Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
		intent.putExtra(User.USER_ID_EXTRA, tourSims.getUser().getUserId());
		startActivity(intent);	
    }
    
    public void googleLoginClick(View v){
		Intent GoogleLogin = new Intent(getApplicationContext(), LoginActivity.class);
		startActivityForResult(GoogleLogin, 0);	
    }
    
    public void social(View v){
    	Intent Social = new Intent(getApplicationContext(),SocialActivity.class);	
		startActivity(Social);
    }
}
 