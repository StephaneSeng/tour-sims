package com.toursims.mobile.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.toursims.mobile.model.City;
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
	private Context context;
	
	public CourseBDD(Context context) throws IOException{
		copyDataBase(context);
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
	
	private String[] allColumnsCity = {SQLiteHelper.COL_ID, 
			SQLiteHelper.COL_NAME,
			SQLiteHelper.COL_CITY_COVERPICTUREURL
	};
	
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
		c.moveToFirst();
		Course course = cursorToCourse(c);
		c.close();
		return course;
	}
	
	public Course getCourseWithURL(String url){
		Cursor c = bdd.query(SQLiteHelper.TABLE_COURSE, allColumnsCourse, SQLiteHelper.COL_COURSE_URL + " LIKE \"" + url +"\"", null, null, null, null);
		c.moveToFirst();
		Course course = cursorToCourse(c);
		c.close();
		return course;
	}
	 
	public Integer getCourseIdWithURL(String url){
		Cursor c = bdd.query(SQLiteHelper.TABLE_COURSE, allColumnsCourse, SQLiteHelper.COL_COURSE_URL + " LIKE \"" + url +"\"", null, null, null, null);
		c.moveToFirst();
		Course course = cursorToCourse(c);
		c.close();
		return course.getId();
	}
	
	
	private Course cursorToCourse(Cursor c){
		if (c.getCount() == 0)
			return null;
		Course course = new Course();
		course.setId(c.getInt(SQLiteHelper.NUM_COL_ID));
		course.setDesc(c.getString(SQLiteHelper.NUM_COL_COURSE_DESC));
		course.setCity(c.getString(SQLiteHelper.NUM_COL_COURSE_CITYID));
		course.setLength(c.getDouble(SQLiteHelper.NUM_COL_COURSE_TIME));
		course.setCoverPictureURL(c.getString(SQLiteHelper.NUM_COL_COURSE_PICTURE));
		course.setRating(c.getDouble(SQLiteHelper.NUM_COL_COURSE_RATING));
		course.setUrl(c.getString(SQLiteHelper.NUM_COL_COURSE_URL));
		course.setName(c.getString(SQLiteHelper.NUM_COL_NAME));
		return course;
	}
	
	private City cursorToCity(Cursor c){
		if (c.getCount() == 0)
			return null;
		City item = new City();
		item.setId(c.getInt(SQLiteHelper.NUM_COL_ID));
		item.setName(c.getString(SQLiteHelper.NUM_COL_NAME));
		item.setCoverPictureURL(c.getString(SQLiteHelper.NUM_COL_CITY_COVERPICTUREURL));
		return item;
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
	
	public List<City> getAllCities() {
		List<City> list = new ArrayList<City>();
		Cursor cursor = bdd.query(SQLiteHelper.TABLE_CITY,
				allColumnsCity, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			City item = cursorToCity(cursor);
			list.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return list;
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
	
	public List<Course> getCoursesWithCity(String city){
		
		List<Course> courses = new ArrayList<Course>();
		Cursor cursor = bdd.query(SQLiteHelper.TABLE_COURSE, allColumnsCourse , SQLiteHelper.COL_COURSE_CITYID + " LIKE \""+city+"\"", null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Course c = cursorToCourse(cursor);
			courses.add(c);
			cursor.moveToNext();
		}
		cursor.close();
		
		return courses;
	}
	
	public void truncate() {
		bdd.execSQL("DELETE FROM " + SQLiteHelper.TABLE_PLACEMARK + ";");
		bdd.execSQL("DELETE FROM " + SQLiteHelper.TABLE_COURSE + ";");
	}
	
	private void copyDataBase(Context c) throws IOException{
		 
    	//Open your local db as the input stream
    	InputStream myInput = c.getAssets().open(NOM_BDD);
 
    	// Path to the just created empty db
    	String outFileName = "/data/data/com.toursims.mobile/databases/" + NOM_BDD;
 
    	//Open the empty db as the output stream
    	java.io.OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
    }
	
}
