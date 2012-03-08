package com.toursims.mobile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.toursims.mobile.model.Course;
import com.toursims.mobile.ui.HomeAdapter;
import com.toursims.mobile.ui.HomeItem;
import com.toursims.mobile.ui.ToolBox;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.toursims.mobile.model.User;

public class HomeActivity extends Activity {
	
	public static final String ALREADY_ASKED_TO_RESUME = "already_asked_to_resume";
		
	/**
	 * Android debugging tag
	 */
	@SuppressWarnings("unused")
	private static final String TAG = HomeActivity.class.getName(); 
	private static SharedPreferences settings;
	private static SharedPreferences.Editor editor;

	private static List<HomeItem> items = new ArrayList<HomeItem>();
	private static List<HomeItem> items2 = new ArrayList<HomeItem>();
	
	private static HomeAdapter adapter;
	private static HomeAdapter adapter2;
	private static ListView lv;
	private static ListView lv2;
	private static ImageView recImage;
	
	private LocalizationService s;

	
	/**
	 * Application context
	 */
	TourSims tourSims;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        recImage = (ImageView)findViewById(R.id.recImage);
	    lv = (ListView) findViewById(R.id.lvListe);
	    lv2 = (ListView) findViewById(R.id.lvListe2);
                
    	settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0); 
		settings.edit()
			.remove(ALREADY_ASKED_TO_RESUME)
			.commit();
        
        // Application context initialisation
        tourSims = (TourSims) getApplication();
        
        //----------------------------------------------------
	    // HOME
	    //----------------------------------------------------
	           
        items.add(new HomeItem(R.string.home_cities_all, R.drawable.ic_menu_compass));
        items.add(new HomeItem(R.string.home_poi, R.drawable.ic_menu_info_details));
        items.add(new HomeItem(R.string.home_goon_course, R.drawable.ic_menu_myplaces));
        items.add(new HomeItem(R.string.home_my_records,R.drawable.ic_menu_mylocation));
          
	    adapter = new HomeAdapter(this, items,getCacheDir().getAbsolutePath());
	    lv.setAdapter(adapter);
	    ToolBox.setListViewHeightBasedOnChildren(lv);
	    	    	
	    lv.setOnItemClickListener(new OnItemClickListener() {
	        
	        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
	    		switch (items.get(position).getText()) {
	    		case R.string.home_cities_all:
	    			allCityActivityClick();
	    			break;
	    		case R.string.home_poi:
	    			poiClick();
	    		case R.string.home_goon_course:
	    			restartCourse();
	    		default:
	    			break;
	    		}	    	
	        }
	    });
        
	    //----------------------------------------------------
	    // SOCIAL
	    //----------------------------------------------------
	            
	    items2.add(new HomeItem(R.string.home_social_map, R.drawable.ic_menu_globe));   
	    items2.add(new HomeItem(R.string.home_social_chat, R.drawable.ic_menu_dialog));
        items2.add(new HomeItem(R.string.home_social_contacts, R.drawable.ic_menu_allfriends));  
        items2.add(new HomeItem(R.string.home_social_profile, R.drawable.ic_menu_user));
        items2.add(new HomeItem(R.string.home_social_login, R.drawable.ic_menu_user));
              
	    adapter2 = new HomeAdapter(this, items2,getCacheDir().getAbsolutePath());
	    lv2.setAdapter(adapter2);   	
	    ToolBox.setListViewHeightBasedOnChildren(lv2);
	    
	    lv2.setOnItemClickListener(new OnItemClickListener() {
	        
	        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
	    		switch (items.get(position).getText()) {
	    		case R.string.home_social_map:
	    			social(view);
	    			break;
	    		case R.string.home_social_chat:
	    			chatClick(view);
	    			break;
	    		case R.string.home_social_contacts:
	    			contactsClick(view);
	    			break;
	    		case R.string.home_social_profile:
	    			profileClick(view);
	    		case R.string.home_social_login:
	    			googleLoginClick(view);
	    		default:
	    			break;
	    		}	    	
	        }
	    });

		doBindService();
		recImage();

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
//			TextView textView = (TextView) findViewById(R.id.home_user_name);
//			ImageView imageView = (ImageView) findViewById(R.id.home_avatar);
//			textView.setText(tourSims.getUser().getName());
//			imageView.setImageBitmap(tourSims.getUser().getAvatarBitmap());
		}
		
        popUpRestart();
        recImage();
	}
    
    private void popUpRestart(){
    	settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0); 
    	
    	if(settings.contains(CustomPreferences.COURSE_STARTED_URL)&&!settings.getBoolean(ALREADY_ASKED_TO_RESUME, true)){
    		    		
			AlertDialog.Builder dialog = ToolBox.getDialog(this);
			
    		dialog.setTitle(R.string.course_already_started_title)
				.setMessage(R.string.course_already_started_message)
				.setPositiveButton(R.string.course_already_started_go_on, new DialogInterface.OnClickListener() {
					
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
						editor.remove(item)
							.commit();
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
					editor.putBoolean(ALREADY_ASKED_TO_RESUME, true)
						.commit();
					dialog.dismiss();		
				}
			});
			dialog.show();
    	}   	
    }
    
    private void restartCourse() {
    	Intent activity = new Intent(getApplicationContext(),CourseStepActivity.class);
    	
        activity.putExtra(Course.URL_EXTRA, settings.getString(CustomPreferences.COURSE_STARTED_URL, null))
        	.putExtra(Course.ID_EXTRA, settings.getInt(CustomPreferences.COURSE_STARTED_ID, 0));
        
        startActivity(activity);
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(ALREADY_ASKED_TO_RESUME)
			.commit();
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
    
    public void rec(View v){
    	settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);
		editor = settings.edit();

		if(settings.getLong(CustomPreferences.RECORDING_RIGHT_NOW, -1)==-1){
			
			final AlertDialog.Builder dialog = ToolBox.getDialog(this);
			dialog.setTitle(R.string.home_record_title);

			EditText e = new EditText(this);
			e.setHint(R.string.home_record_hint);
			
			dialog.setView(e)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if(s==null){
							doBindService();
						}
						Long l	= Calendar.getInstance().getTimeInMillis();
				    	settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);
						editor = settings.edit();
						editor.putLong(CustomPreferences.RECORDING_RIGHT_NOW,l);
						editor.commit();
						Log.d("TAG","Start recording"+l);
						s.startRecording();
						recImage();
						dialog.dismiss();
					}
				})
				.setNegativeButton(R.string.home_record_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						}
				})
			.show();
		} else {
			s.stopRecording();
			editor.remove(CustomPreferences.RECORDING_RIGHT_NOW);
			editor.commit();
			Toast.makeText(this, R.string.home_record_stop_recording, Toast.LENGTH_LONG).show();
			recImage();
		}
    }
    
    private void recImage() {
    	settings = getSharedPreferences(CustomPreferences.PREF_FILE, 0);
    	if(settings.getLong(CustomPreferences.RECORDING_RIGHT_NOW, -1)==-1){
    		recImage.setImageResource(R.drawable.ic_media_play);
    	} else {
    		recImage.setImageResource(R.drawable.ic_media_pause);
    	}
    }
    
    private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder binder) {
			s = ((LocalizationService.MyBinder) binder).getService();
		}

		public void onServiceDisconnected(ComponentName className) {
			s = null;
		}
	};

	void doBindService() {
		bindService(new Intent(this, LocalizationService.class), mConnection,
				Context.BIND_AUTO_CREATE);
	}
}