package com.toursims.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.controller.KmlParser;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.kml.Placemark;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CourseGameActivity extends Activity {

	
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.coursegame);
    
    // Action on default table row
    final TableRow tbrow = (TableRow) findViewById(R.id.tableRow1);
    final TableLayout tblay = (TableLayout) findViewById(R.id.tableLayout1);
        
    CourseBDD datasource = new CourseBDD(this);
	datasource.open();
	
	Course c1 = KmlParser.getInstance().parse("http://www.x00b.com/tour.kml");
	c1.setId(1);
	c1.setUrl("http://www.x00b.com/tour.kml");

	datasource.insertCourse(c1);
	List<Course> lC2;
	lC2 = datasource.getAllCourses();    
    
   for (Course course : lC2) {
		TableRow row = new TableRow(this);
    	TextView t = new TextView(this);
    	t.setText(course.getCity()+course.getId());
    	CheckBox c = new CheckBox(this);
    	row.addView(t);
    	row.addView(c);
    	tblay.addView(row,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));  
    }
   
         
    tbrow.setOnClickListener(new OnClickListener() {                      
        public void onClick(View arg0) {       	
          // Create an Intent to launch an Activity for Course details
        	Intent courseDetails = new Intent(getApplicationContext(),CourseStepActivity.class);
        	
            startActivity(courseDetails);           	
        }
    });
   
    datasource.close();

   
}
	/*
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursegame);
      
        // Get list of course by parent Activity (hard-coded, get rid of this later)
        Intent intent = getIntent();
        final String[] COURSES = intent.getStringArrayExtra("courses");
      //  final ArrayList<HashMap<String, ?>> data = new ArrayList<HashMap<String, ?>>();
               
      //  ArrayList<RowModel> list=new ArrayList<RowModel>();
        
        CourseBDD datasource = new CourseBDD(this);
		datasource.open();	
		
		KmlParser k = KmlParser.getInstance();
		Course c1 = k.parse("http://www.x00b.com/tour.kml");
		c1.setId(1);
		c1.setUrl("http://www.x00b.com/tour.kml");
		
		TextView t = (TextView)findViewById(R.id.textView1);
		t.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
	    
	                  // When clicked, show Course details
	              	  Intent courseDetails = new Intent(getApplicationContext(),CourseDetailsActivity.class);
	              	  startActivity(courseDetails);

			}
		});
		//datasource.insertCourse(c1);
		//datasource.insertPlacemarks(c1);
		
		/*for (Course course : datasource.getAllCourses()) {
			list.add(new RowModel(course));
	    }
                   
       
		Course course1 = new Course();
		course1.setId(1);
       
		for (Placemark pl : datasource.getAllPlacemarks(course1)) {
	   		TableRow row = new TableRow(this);
	       	TextView t = new TextView(this);
	       	t.setText(pl.getPoint().getCoordinates());
	       	row.addView(t);
	       	//tblay.addView(row,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));  
	    }       
        
		datasource.close();
               
        ListView lv = getListView();
        setListAdapter(new CourseListAdapter(list));
        
        //Set action when an item clicked
        lv.setOnItemClickListener(new OnItemClickListener() {
        
        	public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
            // When clicked, show Course details
        	  Intent courseDetails = new Intent(getApplicationContext(),CourseDetailsActivity.class);
        	  courseDetails.putExtra("coursename", COURSES[position]);
        	  startActivity(courseDetails);
          	}
        });
        
		
     
    }

	public class CourseListAdapter extends ArrayAdapter<RowModel> {
        
        CourseListAdapter(ArrayList<RowModel> list) {
            super(CourseGameActivity.this, R.layout.coursegamelist, R.id.courseName, list);
          }
		public View getView(int position, View convertView, ViewGroup parent) {
		      View row=super.getView(position, convertView, parent);
		      ViewHolder holder=(ViewHolder)row.getTag();
		                          
		      if (holder==null) {   
		        holder=new ViewHolder(row);
		        row.setTag(holder);
		        
		        
		      }

		      RowModel model=(((CourseListAdapter)getListAdapter()).getItem(position));
		      Log.d("Log","Item in list " + model.name);
		      holder.rating.setTag(new Integer(position));
		      holder.rating.setRating((float) model.rating);
	          //LinearLayout parent=(LinearLayout)ratingBar.getParent();
	          TextView label= (TextView) findViewById(R.id.courseName);
	        
	          //label.setText(model.name);
		      holder.name.setText(model.name);
		      holder.details.setText("details");
		      //holder.image.setTag(new Integer(position));
		      holder.image.setImageResource(model.imageId);
		      
		      return row;
		}
		
		class ViewHolder {
			TextView name;
			TextView details;
			ImageView image;
			RatingBar rating;
			ViewHolder(View base) {
				this.rating=(RatingBar)base.findViewById(R.id.courseRating);
				this.name=(TextView)base.findViewById(R.id.courseName);
				this.details=(TextView)base.findViewById(R.id.courseDetails);
				this.image=(ImageView)base.findViewById(R.id.courseImage);
			}
		}
		
	}
	class RowModel {
		String name;
		String details;
		Integer imageId;
		double rating;
		    
		RowModel(Course course) {
			this.imageId = R.drawable.ic_launcher;
			this.name = course.getCity();
			this.details = course.getDesc();
			this.rating = course.getRating();
		}
		    
	}*/
}

