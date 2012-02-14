package com.toursims.mobile.controller;

import java.util.ArrayList;
import java.util.List;

import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.kml.Placemark;
import com.toursims.mobile.util.SQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CourseBDD {
	private SQLiteHelper maBaseSQLite;
	private SQLiteDatabase bdd;
	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "sqlite.db";
	public CourseBDD(Context context){
		maBaseSQLite = new SQLiteHelper(context, NOM_BDD, null, VERSION_BDD);
	}
 
	public void open(){
		bdd = maBaseSQLite.getWritableDatabase();
	}
 
	public void close(){
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
	
	private String[] allColumnsCourse = {SQLiteHelper.COL_ID, 
			SQLiteHelper.COL_NAME,
			SQLiteHelper.COL_COURSE_CITYID,
			SQLiteHelper.COL_COURSE_TIME,
			SQLiteHelper.COL_COURSE_DESC,
			SQLiteHelper.COL_COURSE_URL,
			SQLiteHelper.COL_COURSE_RATING,
			SQLiteHelper.COL_COURSE_PICTURE};
  
	public long insertCourse(Course course){		
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COL_COURSE_CITYID, course.getCity());
		values.put(SQLiteHelper.COL_COURSE_DESC, course.getDesc());
		values.put(SQLiteHelper.COL_COURSE_PICTURE, course.getCoverPictureURL());
		values.put(SQLiteHelper.COL_COURSE_RATING, course.getRating());
		values.put(SQLiteHelper.COL_COURSE_TIME, course.getLength());
		values.put(SQLiteHelper.COL_COURSE_URL, course.getUrl());
		values.put(SQLiteHelper.COL_NAME, course.getName());
		return bdd.insert(SQLiteHelper.TABLE_COURSE, null, values);
	}
	
	private String[] allColumnsPlacemark = {SQLiteHelper.COL_ID,
			SQLiteHelper.COL_NAME,
			SQLiteHelper.COL_PLACEMARK_DESCRIPTION,
			SQLiteHelper.COL_PLACEMARK_ADDRESS,
			SQLiteHelper.COL_PLACEMARK_POINT
	};
	
	private Placemark cursorToPlacemark(Cursor c){
		if (c.getCount() == 0)
			return null;
		Placemark placemark = new Placemark();
		placemark.setName(c.getString(SQLiteHelper.NUM_COL_NAME));
		placemark.setId(c.getInt(SQLiteHelper.NUM_COL_ID));
		placemark.setAddress(c.getString(SQLiteHelper.NUM_COL_PLACEMARK_ADDRESS));
		placemark.setDescription(c.getString(SQLiteHelper.NUM_COL_PLACEMARK_DESCRIPTION));
		placemark.setPoint(c.getString(SQLiteHelper.NUM_COL_PLACEMARK_POINT));				
		return placemark;
	}
	
	public long insertPlacemark(Placemark placemark,Course course){
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COL_ID, placemark.getId());
		values.put(SQLiteHelper.COL_PLACEMARK_ADDRESS, placemark.getAddress());
		values.put(SQLiteHelper.COL_PLACEMARK_DESCRIPTION, placemark.getDescription());
		values.put(SQLiteHelper.COL_PLACEMARK_POINT, placemark.getPoint().getCoordinates());
		values.put(SQLiteHelper.COL_NAME, placemark.getName());
		values.put(SQLiteHelper.COL_PLACEMARK_COURSE_ID,course.getId());
		return bdd.insert(SQLiteHelper.TABLE_PLACEMARK, null, values);
	}
	
	public void insertPlacemarks(Course course){
		for(Placemark placemark : course.getPlacemarks()){
			insertPlacemark(placemark, course);
		}
	}
	
	
	public Course getCourseWithId(Integer id){
		Cursor c = bdd.query(SQLiteHelper.TABLE_COURSE, allColumnsCourse, SQLiteHelper.COL_ID + " LIKE \"" + id +"\"", null, null, null, null);
		Course course = cursorToCourse(c);
		c.close();
		return course;
	}
	
	public Course getCourseWithURL(String url){
		Cursor c = bdd.query(SQLiteHelper.TABLE_COURSE, allColumnsCourse, SQLiteHelper.COL_COURSE_URL + " LIKE \"" + url +"\"", null, null, null, null);
		Course course = cursorToCourse(c);
		c.close();
		return course;
	}
	 
	private Course cursorToCourse(Cursor c){
		if (c.getCount() == 0)
			return null;
		Course course = new Course();
		course.setId(c.getInt(SQLiteHelper.NUM_COL_ID));
		course.setCity(c.getString(SQLiteHelper.NUM_COL_COURSE_CITYID));
		course.setLength(c.getDouble(SQLiteHelper.NUM_COL_COURSE_TIME));
		course.setCoverPictureURL(c.getString(SQLiteHelper.NUM_COL_COURSE_PICTURE));
		course.setRating(c.getDouble(SQLiteHelper.NUM_COL_COURSE_RATING));
		course.setUrl(c.getString(SQLiteHelper.NUM_COL_COURSE_URL));
		course.setName(c.getString(SQLiteHelper.NUM_COL_NAME));
		return course;
	}
	
	public List<Course> getAllCourses() {
		List<Course> courses = new ArrayList<Course>();
		Cursor cursor = bdd.query(SQLiteHelper.TABLE_COURSE,
				allColumnsCourse, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Course course = cursorToCourse(cursor);
			courses.add(course);
			cursor.moveToNext();
		}
		cursor.close();
		return courses;
	}
	
	public List<Placemark> getAllPlacemarks(Course course){
		List<Placemark> placemarks = new ArrayList<Placemark>();
		Cursor cursor = bdd.query(SQLiteHelper.TABLE_PLACEMARK, allColumnsPlacemark , SQLiteHelper.COL_PLACEMARK_COURSE_ID + " = "+course.getId(), null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Placemark p = cursorToPlacemark(cursor);
			placemarks.add(p);
			cursor.moveToNext();
		}
		cursor.close();
		return placemarks;
	}

	public void truncate() {
		bdd.execSQL("TRUNCATE TABLE " + SQLiteHelper.TABLE_PLACEMARK + ";");
		bdd.execSQL("TRUNCATE TABLE " + SQLiteHelper.TABLE_COURSE + ";");

	}
	
	
}
