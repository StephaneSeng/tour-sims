package com.toursims.mobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.ui.CourseAdapter;

public class CourseGameActivity extends Activity {
		
	private static List<Course> courses = new ArrayList<Course>();
	
	public final static String ITEM_TITLE = "title";  
	public final static String ITEM_CAPTION = "caption";
	LecteurFlux objLectFlux = new LecteurFlux();
	public Map<String,?> createItem(String title, String caption) {  
		         Map<String,String> item = new HashMap<String,String>();  
		         item.put(ITEM_TITLE, title);  
		         item.put(ITEM_CAPTION, caption);  
		         return item;  
		     } 

	
	public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.coursegame);
    
    List<Map<String,?>> security = new LinkedList<Map<String,?>>();  
    security.add(createItem("titre 1 ", "sous titre du titre1"));  
    security.add(createItem("titre 2", "Sous titre du titre 2"));  
    security.add(createItem("Titre 3", "sous titre du titre 3 un peu lon pour avoir un retour ¨¤ la ligne"));  

    // creation de nom objet de type ListSeparer 
    ListSeparer adapter = new ListSeparer(this);  

        // ajoute d'un objet adapter nom de la cat¨¦gorie Array Test avec deux items first item et item two
    adapter.addSection("Array test", new ArrayAdapter<String>(this, R.layout.coursegame, new String[] { "First item", "item two" }));  

        //ajout d'un autre adapter avec entete plux complex et des items sur deux lignes
    adapter.addSection("Plus complex", new SimpleAdapter(this, security, R.layout.coursegame,  
        new String[] { ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));  



    ListSeparer adapter1 = objLectFlux.liste_course_periode(this);
    ListView list = new ListView(this);  
    list.setAdapter(adapter1);  
    this.setContentView(list); 

    
    // Action on default table row
    ListView lv = (ListView) findViewById(R.id.lvListe);
    TextView title = (TextView) findViewById(R.id.title);
    TextView noCourse = (TextView) findViewById(R.id.noCourse);
    
    Bundle bundle = getIntent().getExtras();
    String city = bundle.getString("CITY");
    title.setText(city);
    
    CourseBDD datasource;
	try {
		datasource = new CourseBDD(this);
		datasource.open();
		courses = datasource.getCoursesWithCity(city);
	    datasource.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
/*	Course c1 = CourseLoader.getInstance().parse("http://www.x00b.com/tour.kml");
	datasource.insertCourse(c1);
	c1.setId(datasource.getCourseIdWithURL("http://www.x00b.com/tour.kml"));
	datasource.insertPlacemarks(c1);
	
	Course c2 = CourseLoader.getInstance().parse("http://www.x00b.com/tour2.kml");
	datasource.insertCourse(c2);
	c2.setId(datasource.getCourseIdWithURL("http://www.x00b.com/tour2.kml"));
	datasource.insertPlacemarks(c2);
	
	Course c3 = CourseLoader.getInstance().parse("http://www.x00b.com/tour3.kml");
	datasource.insertCourse(c3);
	c3.setId(datasource.getCourseIdWithURL("http://www.x00b.com/tour3.kml"));
	datasource.insertPlacemarks(c3);*/
		
				
	if(courses.size()>0) {
	    CourseAdapter adapter = new CourseAdapter(this, courses,getCacheDir().getAbsolutePath());
	    
	    lv.setAdapter(adapter);   
	    lv.setOnItemClickListener(new OnItemClickListener() {
	       
	        public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
	    // When clicked, show Course details
	          Intent courseDetails = new Intent(getApplicationContext(),CourseDetailsActivity.class);
	          courseDetails.putExtra(Course.ID_EXTRA, courses.get(position).getId());
	          courseDetails.putExtra(Course.URL_EXTRA, courses.get(position).getUrl());
	          startActivity(courseDetails);
	          
	          //Put that a course is started
	          SharedPreferences settings = getSharedPreferences(HomeActivity.PREF_FILE, 0);
	          SharedPreferences.Editor editor = settings.edit();
	          editor.putString(Course.PREFERENCES_STARTED_URL, courses.get(position).getUrl());
	          editor.putInt(Course.PREFERENCES_STARTED_ID, courses.get(position).getId());
	          Calendar c = Calendar.getInstance(); 
	          int seconds = c.get(Calendar.SECOND);
	          editor.putInt(Course.PREFERENCES_STARTED_TIME_STARTED, seconds);
	          // Commit the edits!
	          editor.commit();
	    }
		});
	} else {
		noCourse.setText("No courses yet");
	}
}
}