package com.toursims.mobile.controller;

import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.kml.*;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class KmlParser {
	
	private static volatile KmlParser instance = null;
	
	public static KmlParser getKmlParser() {
		if(instance == null) {
			new KmlParser();
		}
		return KmlParser.instance;
	}
	
	private KmlParser(){
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
		
		course.setCity(kml1.getDocument().getExtendedData().getData().get(0).getValue());
		course.setCoverPictureURL(kml1.getDocument().getExtendedData().getData().get(1).getValue());
		course.setDesc(kml1.getDocument().getExtendedData().getData().get(2).getValue());
		course.setRating(Double.valueOf(kml1.getDocument().getExtendedData().getData().get(3).getValue()));
		course.setLength(Double.valueOf(kml1.getDocument().getExtendedData().getData().get(4).getValue()));
		
		
		return course;
		}
}
