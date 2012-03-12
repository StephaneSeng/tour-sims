package com.toursims.mobile.controller;

import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.kml.*;

import java.io.File;
import java.io.FileInputStream;
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

	private CourseLoader() {
		super();
	}

	public Course parse(String address) {

		Kml kml = new Kml();
		Course course = new Course();
		InputStream is;
		
		try {
			File f = new File(address);
			if (f.exists()) {
				is = new FileInputStream(f);
			} else {
				HttpURLConnection urlConnection = null;
				URL url = new URL(address);
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setDoOutput(true);
				urlConnection.setDoInput(true);
				urlConnection.connect();
				is = urlConnection.getInputStream();
			}
			Serializer serializer = new Persister();
			kml = serializer.read(Kml.class, is, false);
			course.copyFromDocument(kml.getDocument(), address);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return course;
	}
}
