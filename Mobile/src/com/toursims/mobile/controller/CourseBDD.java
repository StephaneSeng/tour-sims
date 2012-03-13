package com.toursims.mobile.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.toursims.mobile.model.City;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.Trace;
import com.toursims.mobile.model.kml.Placemark;
import com.toursims.mobile.model.kml.Point;
import com.toursims.mobile.util.SQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CourseBDD {
	private static final int VERSION_BDD = 1;
	private static final String BDD_PACKAGE = "sqlite.db";
	private static final String BDD_CUSTOM = "trace.db";
	private static final String PATH_BDD = "/data/data/com.toursims.mobile/databases/";

	private SQLiteHelper packageBaseSQLite;
	private SQLiteHelper customBaseSQLite;
	private SQLiteDatabase packageDB;
	private SQLiteDatabase customDB;

	public CourseBDD(Context context) throws IOException {
		packageBaseSQLite = new SQLiteHelper(context, BDD_PACKAGE, null,
				VERSION_BDD);
		customBaseSQLite = new SQLiteHelper(context, BDD_CUSTOM, null,
				VERSION_BDD);
	}

	public void open() {
		packageDB = packageBaseSQLite.getWritableDatabase();
		customDB = customBaseSQLite.getWritableDatabase();
	}

	public void close() {
		packageDB.close();
		customDB.close();
	}

	public SQLiteDatabase getBDD() {
		return packageDB;
	}

	private String[] allColumnsCity = { SQLiteHelper.COL_ID,
			SQLiteHelper.COL_NAME, SQLiteHelper.CITY_COVERPICTUREURL_COL };

	private String[] allColumnsCourse = { SQLiteHelper.COL_ID,
			SQLiteHelper.COL_NAME, SQLiteHelper.COURSE_CITYID_COL,
			SQLiteHelper.COURSE_TIME_COL, SQLiteHelper.COURSE_DESC_COL,
			SQLiteHelper.COURSE_URL_COL, SQLiteHelper.COURSE_RATING_COL,
			SQLiteHelper.COURSE_PICTURE_COL };

	private String[] allColumnsTrace = { SQLiteHelper.COL_ID,
			SQLiteHelper.COL_NAME, SQLiteHelper.TRACE_MILLIS_COL,
			SQLiteHelper.TRACE_FILE_COL };

	private String[] allColumnsPlacemark = { SQLiteHelper.COL_ID,
			SQLiteHelper.COL_NAME, SQLiteHelper.PLACEMARK_DESCRIPTION_COL,
			SQLiteHelper.PLACEMARK_ADDRESS_COL,
			SQLiteHelper.PLACEMARK_POINT_COL };

	private String[] allColumnsFlag = { SQLiteHelper.COL_ID,
			SQLiteHelper.COL_NAME, SQLiteHelper.FLAG_MILLIS_COL,
			SQLiteHelper.FLAG_LONGITUDE_COL, SQLiteHelper.FLAG_LATITUDE_COL };

	public long insertCourse(Course item) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COURSE_CITYID_COL, item.getCity());
		values.put(SQLiteHelper.COURSE_DESC_COL, item.getDesc());
		values.put(SQLiteHelper.COURSE_PICTURE_COL, item.getCoverPictureURL());
		values.put(SQLiteHelper.COURSE_RATING_COL, item.getRating());
		values.put(SQLiteHelper.COURSE_TIME_COL, item.getLength());
		values.put(SQLiteHelper.COURSE_URL_COL, item.getUrl());
		values.put(SQLiteHelper.COL_NAME, item.getName());
		return packageDB.insert(SQLiteHelper.TABLE_COURSE, null, values);
	}

	public long insertTrace(Trace item) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COL_NAME, item.getName());
		values.put(SQLiteHelper.TRACE_MILLIS_COL, item.getMillis());
		values.put(SQLiteHelper.TRACE_FILE_COL, item.getFile());
		return customDB.insert(SQLiteHelper.TABLE_TRACE, null, values);
	}

	public long insertFlag(Point item) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COL_NAME, item.getName());
		values.put(SQLiteHelper.FLAG_MILLIS_COL, item.getMillis());
		values.put(SQLiteHelper.FLAG_LONGITUDE_COL, item.getLongitude());
		values.put(SQLiteHelper.FLAG_LATITUDE_COL, item.getLatitude());
		return customDB.insert(SQLiteHelper.TABLE_FLAG, null, values);
	}

	private Point cursorToFlag(Cursor c) {
		if (c.getCount() == 0)
			return null;
		Point item = new Point();
		item.setId(c.getInt(SQLiteHelper.COL_ID_NUM));
		item.setName(c.getString(SQLiteHelper.COL_NAME_NUM));
		item.setMillis(c.getLong(SQLiteHelper.FLAG_MILLIS_NUM));
		item.setLongitude(c.getDouble(SQLiteHelper.FLAG_LONGITUDE_NUM));
		item.setLatitude(c.getDouble(SQLiteHelper.FLAG_LATITUDE_NUM));
		return item;
	}

	private Trace cursorToTrace(Cursor c) {
		if (c.getCount() == 0)
			return null;
		Trace item = new Trace();
		item.setId(c.getInt(SQLiteHelper.COL_ID_NUM));
		item.setName(c.getString(SQLiteHelper.COL_NAME_NUM));
		item.setMillis(c.getLong(SQLiteHelper.TRACE_MILLIS_NUM));
		item.setFile(c.getString(SQLiteHelper.TRACE_FILE_NUM));
		return item;
	}

	private Placemark cursorToPlacemark(Cursor c) {
		if (c.getCount() == 0)
			return null;
		Placemark item = new Placemark();
		item.setId(c.getInt(SQLiteHelper.COL_ID_NUM));
		item.setName(c.getString(SQLiteHelper.COL_NAME_NUM));
		item.setAddress(c.getString(SQLiteHelper.PLACEMARK_ADDRESS_NUM));
		item.setDescription(c.getString(SQLiteHelper.PLACEMARK_DESCRIPTION_NUM));
		item.setPoint(c.getString(SQLiteHelper.PLACEMARK_POINT_NUM));
		return item;
	}

	public long insertPlacemark(Placemark item, Course course) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COL_ID, item.getId());
		values.put(SQLiteHelper.PLACEMARK_ADDRESS_COL, item.getAddress());
		values.put(SQLiteHelper.PLACEMARK_DESCRIPTION_COL,
				item.getDescription());
		values.put(SQLiteHelper.PLACEMARK_POINT_COL, item.getPoint()
				.getCoordinates());
		values.put(SQLiteHelper.COL_NAME, item.getName());
		values.put(SQLiteHelper.PLACEMARK_COURSE_ID_COL, course.getId());
		return packageDB.insert(SQLiteHelper.TABLE_PLACEMARK, null, values);
	}

	public void insertPlacemarks(Course course) {
		for (Placemark placemark : course.getPlacemarks()) {
			insertPlacemark(placemark, course);
		}
	}

	public Course getCourseWithId(Integer id) {
		Cursor c = packageDB.query(SQLiteHelper.TABLE_COURSE, allColumnsCourse,
				SQLiteHelper.COL_ID + " LIKE \"" + id + "\"", null, null, null,
				null);
		c.moveToFirst();
		Course course = cursorToCourse(c);
		c.close();
		return course;
	}

	public Course getCourseWithURL(String url) {
		Cursor c = packageDB.query(SQLiteHelper.TABLE_COURSE, allColumnsCourse,
				SQLiteHelper.COURSE_URL_COL + " LIKE \"" + url + "\"", null,
				null, null, null);
		c.moveToFirst();
		Course course = cursorToCourse(c);
		c.close();
		return course;
	}

	public Integer getCourseIdWithURL(String url) {
		Cursor c = packageDB.query(SQLiteHelper.TABLE_COURSE, allColumnsCourse,
				SQLiteHelper.COURSE_URL_COL + " LIKE \"" + url + "\"", null,
				null, null, null);
		c.moveToFirst();
		Course course = cursorToCourse(c);
		c.close();
		return course.getId();
	}

	private Course cursorToCourse(Cursor c) {
		if (c.getCount() == 0)
			return null;
		Course course = new Course();
		course.setId(c.getInt(SQLiteHelper.COL_ID_NUM));
		course.setDesc(c.getString(SQLiteHelper.COURSE_DESC_NUM));
		course.setCity(c.getString(SQLiteHelper.COURSE_CITYID_NUM));
		course.setLength(c.getDouble(SQLiteHelper.COURSE_TIME_NUM));
		course.setCoverPictureURL(c.getString(SQLiteHelper.COURSE_PICTURE_NUM));
		course.setRating(c.getDouble(SQLiteHelper.COURSE_RATING_NUM));
		course.setUrl(c.getString(SQLiteHelper.COURSE_URL_NUM));
		course.setName(c.getString(SQLiteHelper.COL_NAME_NUM));
		return course;
	}

	private City cursorToCity(Cursor c) {
		if (c.getCount() == 0)
			return null;
		City item = new City();
		item.setId(c.getInt(SQLiteHelper.COL_ID_NUM));
		item.setName(c.getString(SQLiteHelper.COL_NAME_NUM));
		item.setCoverPictureURL(c
				.getString(SQLiteHelper.CITY_COVERPICTUREURL_NUM));
		return item;
	}

	public List<Course> getAllCourses() {
		List<Course> courses = new ArrayList<Course>();
		Cursor cursor = packageDB.query(SQLiteHelper.TABLE_COURSE,
				allColumnsCourse, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Course item = cursorToCourse(cursor);
			courses.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return courses;
	}

	public List<Trace> getAllTraces() {
		List<Trace> items = new ArrayList<Trace>();
		Cursor cursor = customDB.query(SQLiteHelper.TABLE_TRACE,
				allColumnsTrace, null, null, null, null, SQLiteHelper.COL_ID
						+ " DESC");
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Trace item = cursorToTrace(cursor);
			items.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return items;
	}

	public List<Point> getAllPoints() {
		List<Point> items = new ArrayList<Point>();
		Cursor cursor = customDB.query(SQLiteHelper.TABLE_FLAG, allColumnsFlag,
				null, null, null, null, SQLiteHelper.COL_ID + " DESC");
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Point item = cursorToFlag(cursor);
			items.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return items;
	}

	public List<City> getAllCities() {
		List<City> list = new ArrayList<City>();
		Cursor cursor = packageDB.query(SQLiteHelper.TABLE_CITY,
				allColumnsCity, null, null, null, null, SQLiteHelper.COL_NAME);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			City item = cursorToCity(cursor);
			list.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}

	public List<Placemark> getAllPlacemarks(Course course) {
		List<Placemark> placemarks = new ArrayList<Placemark>();
		Cursor cursor = packageDB.query(SQLiteHelper.TABLE_PLACEMARK,
				allColumnsPlacemark, SQLiteHelper.PLACEMARK_COURSE_ID_COL
						+ " = " + course.getId(), null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Placemark p = cursorToPlacemark(cursor);
			placemarks.add(p);
			cursor.moveToNext();
		}
		cursor.close();
		return placemarks;
	}

	public void delete(Trace item) {
		customDB.delete(SQLiteHelper.TABLE_TRACE, SQLiteHelper.COL_ID + "="
				+ item.getId(), null);
	}

	public List<Course> getCoursesWithCity(String city) {

		List<Course> courses = new ArrayList<Course>();
		Cursor cursor = packageDB.query(SQLiteHelper.TABLE_COURSE,
				allColumnsCourse, SQLiteHelper.COURSE_CITYID_COL + " LIKE \""
						+ city + "\"", null, null, null, null);
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
		packageDB.execSQL("DELETE FROM " + SQLiteHelper.TABLE_PLACEMARK + ";");
		packageDB.execSQL("DELETE FROM " + SQLiteHelper.TABLE_COURSE + ";");
	}

	public void copyDataBase(Context c) throws IOException {

		Log.d("CourseBDD", "Copy Database");
		InputStream myInput = c.getAssets().open(BDD_PACKAGE);

		// Path to the just created empty db
		String outFileName = PATH_BDD + BDD_PACKAGE;

		File f = new File(PATH_BDD);
		f.mkdirs();

		// Open the empty db as the output stream
		java.io.OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

		f = new File(PATH_BDD + BDD_CUSTOM);
		myInput = c.getAssets().open(BDD_CUSTOM);

		// Path to the just created empty db
		outFileName = PATH_BDD + BDD_CUSTOM;

		myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		buffer = new byte[1024];

		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

}
