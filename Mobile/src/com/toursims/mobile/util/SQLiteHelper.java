package com.toursims.mobile.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SQLiteHelper extends SQLiteOpenHelper{
        
	public static final String COL_ID = "_id";
	public static final int COL_ID_NUM = 0;
	public static final String COL_NAME = "NAME";
	public static final int COL_NAME_NUM = 1;
    
    //Table Course Constants
	public static final String TABLE_COURSE = "COURSES";
	public static final String COURSE_CITYID_COL = "CITYID";
	public static final int COURSE_CITYID_NUM = 2;
	public static final String COURSE_TIME_COL = "TIME";
	public static final int COURSE_TIME_NUM = 3;	
	public static final String COURSE_DESC_COL = "DESC";
	public static final int COURSE_DESC_NUM = 4;
	public static final String COURSE_URL_COL = "URL";
	public static final int COURSE_URL_NUM = 5;
	public static final String COURSE_RATING_COL = "RATING";
	public static final int COURSE_RATING_NUM = 6;
	public static final String COURSE_PICTURE_COL = "PICTURE";
	public static final int COURSE_PICTURE_NUM = 7;
		
	public static final String TABLE_PLACEMARK = "PLACEMARKS";
	public static final String PLACEMARK_DESCRIPTION_COL = "DESCRIPTION";
	public static final int PLACEMARK_DESCRIPTION_NUM = 2;
	public static final String PLACEMARK_ADDRESS_COL = "ADDRESS";
	public static final int PLACEMARK_ADDRESS_NUM = 3;
	public static final String PLACEMARK_POINT_COL = "POINT";
	public static final int PLACEMARK_POINT_NUM = 4;
	public static final String PLACEMARK_COURSE_ID_COL = "COURSE";
	public static final int PLACEMARK_COURSE_ID_NUM = 5;

	public static final String TABLE_CITY = "CITIES";
	public static final String CITY_COVERPICTUREURL_COL = "COVERPICTUREURL";
	public static final int CITY_COVERPICTUREURL_NUM = 2;
	
	public static final String TABLE_TRACE = "TRACES";
	public static final String TRACE_MILLIS_COL= "MILLIS";
	public static final int TRACE_MILLIS_NUM = 2;
	
	public static final String TABLE_FLAG = "FLAG";
	public static final String FLAG_MILLIS_COL = "MILLIS";
	public static final int FLAG_MILLIS_NUM = 2;
	public static final String FLAG_LONGITUDE_COL = "LONGITUDE";
	public static final int FLAG_LONGITUDE_NUM = 3;
	public static final String FLAG_LATITUDE_COL = "LATITUDE";
	public static final int FLAG_LATITUDE_NUM = 4;
	/*
	private static final String CREATE_BDD_COURSE = "CREATE TABLE " + TABLE_COURSE + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COL_NAME 			+ " TEXT, "
			+ COL_COURSE_CITYID 			+ " TEXT, "
			+ COL_COURSE_TIME 			+ " TEXT, "
			+ COL_COURSE_DESC 			+ " TEXT, "
			+ COL_COURSE_URL 			+ " TEXT, "
			+ COL_COURSE_RATING 			+ " TEXT," 
			+ COL_COURSE_PICTURE		   +" TEXT);";
	
	private static final String CREATE_BDD_PLACEMARK = "CREATE TABLE " + TABLE_PLACEMARK + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COL_NAME 			+ " TEXT, "
			+ COL_PLACEMARK_DESCRIPTION 			+ " TEXT, "
			+ COL_PLACEMARK_ADDRESS 			+ " TEXT, "
			+ COL_PLACEMARK_POINT			+ " TEXT, "
			+ COL_PLACEMARK_COURSE_ID + " TEXT);"
		;
	*/
	public SQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
	//	db.execSQL(CREATE_BDD_COURSE);
	//	db.execSQL(CREATE_BDD_PLACEMARK);
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	//	db.execSQL("INSERT INTO \"android_metadata\" VALUES ('en_US')");
	//	db.execSQL("DROP TABLE " + TABLE_COURSE + ";");
	//	db.execSQL("DROP TABLE " + TABLE_PLACEMARK + ";");
	//	onCreate(db);
	}
	
}