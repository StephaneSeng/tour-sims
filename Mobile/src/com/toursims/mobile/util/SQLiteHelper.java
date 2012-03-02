package com.toursims.mobile.util;

import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SQLiteHelper extends SQLiteOpenHelper{
        
	public static final String COL_ID = "_id";
	public static final int NUM_COL_ID = 0;
	public static final String COL_NAME = "NAME";
	public static final int NUM_COL_NAME = 1;
    
    //Table Course Constants
	public static final String TABLE_COURSE = "COURSES";
	public static final String COL_COURSE_CITYID = "CITYID";
	public static final int NUM_COL_COURSE_CITYID = 2;
	public static final String COL_COURSE_TIME = "TIME";
	public static final int NUM_COL_COURSE_TIME = 3;	
	public static final String COL_COURSE_DESC = "DESC";
	public static final int NUM_COL_COURSE_DESC = 4;
	public static final String COL_COURSE_URL = "URL";
	public static final int NUM_COL_COURSE_URL = 5;
	public static final String COL_COURSE_RATING = "RATING";
	public static final int NUM_COL_COURSE_RATING = 6;
	public static final String COL_COURSE_PICTURE = "PICTURE";
	public static final int NUM_COL_COURSE_PICTURE = 7;
		
	public static final String TABLE_PLACEMARK = "PLACEMARKS";
	public static final String COL_PLACEMARK_DESCRIPTION = "DESCRIPTION";
	public static final int NUM_COL_PLACEMARK_DESCRIPTION = 2;
	public static final String COL_PLACEMARK_ADDRESS = "ADDRESS";
	public static final int NUM_COL_PLACEMARK_ADDRESS = 3;
	public static final String COL_PLACEMARK_POINT = "POINT";
	public static final int NUM_COL_PLACEMARK_POINT = 4;
	public static final String COL_PLACEMARK_COURSE_ID = "COURSE";
	public static final int NUM_COL_PLACEMARK_COURSE_ID = 5;

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
		
	public SQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BDD_COURSE);
		db.execSQL(CREATE_BDD_PLACEMARK);
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("INSERT INTO \"android_metadata\" VALUES ('en_US')");
		db.execSQL("DROP TABLE " + TABLE_COURSE + ";");
		db.execSQL("DROP TABLE " + TABLE_PLACEMARK + ";");
		onCreate(db);
	}
	
}