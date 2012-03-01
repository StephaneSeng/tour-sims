package com.toursims.mobile;

import com.toursims.mobile.controller.CourseLoader;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.kml.Placemark;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GameActivity extends Activity{
    /** Called when the activity is first created. */
	private static Course course;
	private static Integer currentPlacemark;
	private static Integer currentQuestion = -1;
	private static TextView title;
	private static TextView description;
	private static Button buttonNext;
	private static Placemark p;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        
    	title = (TextView)findViewById(R.id.title);
    	buttonNext = (Button)findViewById(R.id.buttonNext);
    	description = (TextView)findViewById(R.id.description);
        
        Bundle bundle = getIntent().getExtras();
        String course_url = bundle.getString(Course.COURSE_URL_EXTRA); 
    	currentPlacemark = bundle.getInt(Course.COURSE_CURRENT_PLACEMARK);
    	   	
    	course = CourseLoader.getInstance().parse(course_url);
    	
    	p = course.getPlacemarks().get(currentPlacemark);   	
    	title.setText(p.getName());
    	description.setText(p.getDescription());
    	
    	
    	if(p.getQuestions() == null) {
 		   buttonNext.setText("Etape Suivante");   
 	   } else {
 		   buttonNext.setText("Jouer");   
 	   }
    	
    	buttonNext.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				buttonClicked();
			}
		});
    	
    }
    
   public void buttonClicked() {
	   if(p.getQuestions() == null) {
		   quitView();
	   } else {		   
		   currentQuestion += 1;
		   if(currentQuestion == 0){
			   nextQuestion();
		   }  else if(currentQuestion > 0) {
			   AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			   dialog.setTitle("Bien répondu"); 

			   if(currentQuestion == p.getQuestions().size()) {
				   
				   dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							dialogEnd();	
						}
					});
			     
			   } else {
				   
				   dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							nextQuestion();		
						}
					});
			   }		   
			   dialog.show();
	   } 
		   
		   
   }
	   
   }
   
public void nextQuestion() {
	description.setText(p.getQuestions().get(currentQuestion).getTitle());
	buttonNext.setText("Répondez");
}
   
public void dialogEnd() {
	AlertDialog.Builder dialog2 = new AlertDialog.Builder(this);
	dialog2.setTitle("Vous pouvez passer à l'étape suivante"); 
	
	dialog2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			quitView();
		}
	});
	
	dialog2.show();
	
}

   public void quitView() {
       Intent courseDetails = new Intent(getApplicationContext(),CourseDetailsActivity.class);
       courseDetails.putExtra(Course.COURSE_ID_EXTRA, course.getId());
       courseDetails.putExtra(Course.COURSE_URL_EXTRA, course.getUrl());
       courseDetails.putExtra(Course.COURSE_NEXT_PLACEMARK, true);
       startActivity(courseDetails);
       finish();
   }
   
}
