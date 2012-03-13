package com.toursims.mobile;

import android.content.SharedPreferences;

public class CustomPreferences {
	public static final String PREF_FILE = "toursims_pref_file";
	
	public static final String COURSE_STARTED_URL = "course_url_for_preferences";
	public static final String COURSE_STARTED_TIME_STARTED = "course_started_time_for_preferences";
	public static final String COURSE_STARTED_ID = "course_id_for_preferences";
	public static final String COURSE_CURRENT_PLACEMARK = "course_current_placemark";
	
	public static final String LATEST_VERSION = "LATEST_VERSION";
	
	public static final String RECORDING_RIGHT_NOW = "recoding_right_now";
	
	public static String[] COURSE_ALL = {COURSE_STARTED_URL,COURSE_STARTED_TIME_STARTED,COURSE_STARTED_ID,COURSE_CURRENT_PLACEMARK};
	
	public static final String VIBRATOR_ENABLED = "vibrator_enabled";
	public static final boolean SOUND_ALERT = true;
	
	public static final int VIBRATOR_LENGTH = 500;
	
	public static void removeCourseStarted(SharedPreferences settings){
    	SharedPreferences.Editor editor = settings.edit();
		
		for (String item : CustomPreferences.COURSE_ALL) {
			editor.remove(item);
		}
		editor.commit();
	}

}
