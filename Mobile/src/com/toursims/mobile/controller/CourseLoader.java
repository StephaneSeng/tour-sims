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
	   	
		try {
			URL url = null;
			
			   	try {
			   		url = new URL(address);
			   	} catch (Exception e) {
			   		e.printStackTrace();
		   }
			   	
		   try {
			   urlConnection=(HttpURLConnection)url.openConnection();
		   } catch (Exception e) {
		   	e.printStackTrace();
		   }
		   
		   urlConnection.setRequestMethod("GET");
		   urlConnection.setDoOutput(true);
		   urlConnection.setDoInput(true);
		   
		   try {
			   urlConnection.connect();
		   } catch (Exception e) {
		   		e.printStackTrace();
		   }
		   
		   InputStream is = urlConnection.getInputStream();
		   Serializer serializer = new Persister();

	   		
		   try {
		   		kml1 = serializer.read(Kml.class, is, false);
		   } catch (Exception e) {
		   		e.printStackTrace();
		   }
		   
		   
			} catch (Exception e) {
		   		e.printStackTrace();
			} 
		
		Course course = new Course();
		
		course.setUrl(address);
		course.setName(kml1.getDocument().getName());
		
		try {
			course.setUrl(address);
			course.setCity(kml1.getDocument().getExtendedData().get(0).getValue());
			course.setCoverPictureURL(kml1.getDocument().getExtendedData().get(1).getValue());
			course.setDesc(kml1.getDocument().getExtendedData().get(2).getValue());
			course.setRating(Double.valueOf(kml1.getDocument().getExtendedData().get(3).getValue()));
			course.setLength(Double.valueOf(kml1.getDocument().getExtendedData().get(4).getValue()));
			course.setType(kml1.getDocument().getExtendedData().get(5).getValue());
		} catch (IndexOutOfBoundsException e) {
			course.setType(Course.TYPE_COURSE);
		}
			
		course.setPlacemarks(kml1.getDocument().getPlacemarks());
		
		return course;
		}
}
