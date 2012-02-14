package com.toursims.mobile.controller;

import java.util.ArrayList;
import java.util.List;

import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.kml.Placemark;

public class CourseController {
	private static volatile CourseController instance = new CourseController();
	private static volatile List<Course> courses = new ArrayList<Course>();
	
	
	public static CourseController getInstance() {
			return instance;
	}
		
	private CourseController(){
	}
	
	public List<Course> getCourses() {
		KmlParser kml = KmlParser.getInstance();
		List<Course> c = new ArrayList<Course>();
		courses.add(kml.parse("http://www.x00b.com/tour.kml"));
		return courses;
		
	}
	
	
    static final String[] COURSESURL = new String[] {
    	"http://www.x00b.com/tour.xml"
    };
    
    
	
}
