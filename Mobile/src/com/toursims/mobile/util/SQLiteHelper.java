package com.toursims.mobile.util;

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
	public static final String TABLE_COURSE = "COURSE";
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
		
	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_COURSE + " ("
			+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
			+ COL_NAME 			+ " TEXT NOT NULL, "
			+ COL_COURSE_CITYID 			+ " TEXT NOT NULL, "
			+ COL_COURSE_TIME 			+ " TEXT NOT NULL, "
			+ COL_COURSE_DESC 			+ " TEXT  NULL, "
			+ COL_COURSE_URL 			+ " TEXT NOT NULL, "
			+ COL_COURSE_RATING 			+ " TEXT NOT NULL," 
			+ COL_COURSE_PICTURE		   +" TEXT NOT NULL);";
	
		
	public SQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BDD);
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_COURSE + ";");
		onCreate(db);
	}
 
}