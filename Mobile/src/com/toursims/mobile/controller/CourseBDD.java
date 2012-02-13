package com.toursims.mobile.controller;

import java.util.ArrayList;
import java.util.List;

import com.toursims.mobile.model.Course;
import com.toursims.mobile.util.SQLiteHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CourseBDD {
	private SQLiteHelper maBaseSQLite;
	private SQLiteDatabase bdd;
	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "course.db";
	private String[] allColumns = {maBaseSQLite.COL_ID, 
			maBaseSQLite.COL_NAME,
			maBaseSQLite.COL_COURSE_CITYID,
			maBaseSQLite.COL_COURSE_TIME,
			maBaseSQLite.COL_COURSE_DESC,
			maBaseSQLite.COL_COURSE_URL,
			maBaseSQLite.COL_COURSE_RATING,
			maBaseSQLite.COL_COURSE_PICTURE};
 
 
	public CourseBDD(Context context){
		//On cr�er la BDD et sa table
		maBaseSQLite = new SQLiteHelper(context, NOM_BDD, null, VERSION_BDD);
	}
 
	public void open(){
		//on ouvre la BDD en �criture
		bdd = maBaseSQLite.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'acc�s � la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public long insertCourse(Course course){
	/*	if(getCourseWithUrl(course.getUrl()) != null) {
			return (Long) null;
		}*/
		
		ContentValues values = new ContentValues();
		values.put(maBaseSQLite.COL_COURSE_CITYID, course.getCity());
		values.put(maBaseSQLite.COL_COURSE_DESC, course.getDesc());
		values.put(maBaseSQLite.COL_COURSE_PICTURE, course.getCoverPictureURL());
		values.put(maBaseSQLite.COL_COURSE_RATING, course.getRating());
		values.put(maBaseSQLite.COL_COURSE_TIME, course.getLength());
		values.put(maBaseSQLite.COL_COURSE_URL, course.getUrl());
		values.put(maBaseSQLite.COL_NAME, course.getName());
		return bdd.insert(maBaseSQLite.TABLE_COURSE, null, values);
	}
 
	/*public int updateLivre(int id, Livre livre){
		//La mise � jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
		//il faut simple pr�ciser quelle livre on doit mettre � jour gr�ce � l'ID
		ContentValues values = new ContentValues();
		values.put(COL_ISBN, livre.getIsbn());
		values.put(COL_TITRE, livre.getTitre());
		return bdd.update(TABLE_LIVRES, values, COL_ID + " = " +id, null);
	}*/
 
	/*
	public int removeLivreWithID(int id){
		//Suppression d'un livre de la BDD gr�ce � l'ID
		return bdd.delete(TABLE_LIVRES, COL_ID + " = " +id, null);
	}
    */
	

	
	public Course getCourseWithUrl(String url){
		//R�cup�re dans un Cursor les valeur correspondant � un livre contenu dans la BDD (ici on s�lectionne le livre gr�ce � son titre)
		Cursor c = bdd.query(maBaseSQLite.TABLE_COURSE, allColumns , maBaseSQLite.COL_COURSE_URL + " LIKE \"" + url +"\"", null, null, null, null);
		return cursorToCourse(c);
	}
 
	//Cette m�thode permet de convertir un cursor en un livre
	private Course cursorToCourse(Cursor c){
		//si aucun �l�ment n'a �t� retourn� dans la requ�te, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		//Sinon on se place sur le premier �l�ment
		c.moveToFirst();
		//On cr�� un livre
		Course course = new Course();
		//on lui affecte toutes les infos gr�ce aux infos contenues dans le Cursor
		course.setCity(c.getString(maBaseSQLite.NUM_COL_COURSE_CITYID));
		course.setLength(c.getDouble(maBaseSQLite.NUM_COL_COURSE_TIME));
		course.setCoverPictureURL(c.getString(maBaseSQLite.NUM_COL_COURSE_PICTURE));
		course.setRating(c.getDouble(maBaseSQLite.NUM_COL_COURSE_RATING));
		course.setUrl(c.getString(maBaseSQLite.NUM_COL_COURSE_URL));
		course.setName(c.getString(maBaseSQLite.NUM_COL_NAME));

		//On retourne le livre
		return course;
	}
	
	public List<Course> getAllCourses() {
		List<Course> courses = new ArrayList<Course>();
		Cursor cursor = bdd.query(maBaseSQLite.TABLE_COURSE,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Course course = cursorToCourse(cursor);
			courses.add(course);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return courses;
	}
	
	
}
