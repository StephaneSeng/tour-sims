package com.toursims.mobile.controller;

import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.kml.*;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class CourseLoader {
	
	private static CourseLoader instance = new CourseLoader();
		
	public static CourseLoader getInstance() {
		return CourseLoader.instance;
	}
	
	private CourseLoader(){
		super();
	}
	
	public Course parse(String address){

		Kml kml1 = new Kml(); 		  
	   	HttpURLConnection urlConnection= null;
	   	Course course = new Course();
	   	
		try {
			URL url = new URL(address);
			urlConnection=(HttpURLConnection)url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.connect();
			InputStream is = urlConnection.getInputStream();
			Serializer serializer = new Persister();
			kml1 = serializer.read(Kml.class, is, false);
			course.copyFromDocument(kml1.getDocument(), address);
		}  catch (Exception e) {
		   		e.printStackTrace();
		}
		   
		return course;
		}
}
