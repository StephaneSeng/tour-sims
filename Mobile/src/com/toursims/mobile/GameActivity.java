package com.toursims.mobile;

import java.util.ArrayList;
import java.util.List;

import com.toursims.mobile.controller.CourseLoader;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.model.kml.Answer;
import com.toursims.mobile.model.kml.Placemark;
import com.toursims.mobile.model.kml.Question;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
	private static Question q;
	private static LinearLayout answers;
	private static EditText e;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        
    	title = (TextView)findViewById(R.id.title);
    	buttonNext = (Button)findViewById(R.id.buttonNext);
    	description = (TextView)findViewById(R.id.description);
        
        Bundle bundle = getIntent().getExtras();
        String course_url = bundle.getString(Course.URL_EXTRA); 
    	currentPlacemark = bundle.getInt(Course.CURRENT_PLACEMARK);

    	course = CourseLoader.getInstance().parse(course_url);
    	
    	p = course.getPlacemarks().get(currentPlacemark);   	
    	title.setText(p.getName());
    	description.setText(p.getDescription());
    	
    	Log.d("GameActivity","nb questions"+p.getQuestions().size());
    	
    	if(p.getQuestions() == null) {
 		   buttonNext.setText(R.string.game_next_stage);   
 	   } else {
 		   buttonNext.setText(R.string.game_play);   
 	   }
    	
    	buttonNext.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				buttonValidateClicked();
			}
		});
    	
    }
    
   public void buttonValidateClicked() {
	   if(p.getQuestions() == null) {
		   quitView();
	   } else {		   
		   if(currentQuestion == -1){
			   currentQuestion++;
			   nextQuestion();
		   }  else if(currentQuestion >= 0) {
			   
			   AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			   			   
			   if(checkAnswer()) {
				   currentQuestion++;
				   dialog.setTitle(R.string.game_good_answer);
				   if(currentQuestion == p.getQuestions().size()) {
					   
					   dialog.setPositiveButton(R.string.game_ok, new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								dialogEnd();	
							}
						});
				     
				   } else {
					   
					   dialog.setPositiveButton(R.string.game_ok, new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								nextQuestion();	
							}
						});
				   }		  
			   } else {
				   dialog.setTitle(R.string.game_try_again); 
				   dialog.setPositiveButton(R.string.game_ok, new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
			   }
			      
			   dialog.show();
	   } 
		   
		   
   }
	   
   }
   
public void nextQuestion() {
	q = p.getQuestions().get(currentQuestion);
	
	description.setText(q.getTitle());
    setContentView(R.layout.game_question);
    
	title = (TextView)findViewById(R.id.title);
	buttonNext = (Button)findViewById(R.id.buttonNext);
	description = (TextView)findViewById(R.id.description);
	
	answers = (LinearLayout) findViewById(R.id.answers);
	answers.setOrientation(LinearLayout.VERTICAL);
	
	title.setText(p.getName());
	description.setText(q.getTitle());
	
	if(q.getType().equals(Question.TYPE_MULTIPLE_CHOICE)){
		
		// multiple answers
		if(q.hasMultipleAnswers()) {
			int i = 0;
			for (Answer a : q.getAnswers()) {
				CheckBox item = new CheckBox(this);
				item.setText(a.getValue());
				item.setId(i++);	
				answers.addView(item);
			}
		} else {
		//only one answer	
			RadioGroup r = new RadioGroup(this);
			
			int i = 0;			
			for (Answer a : q.getAnswers()) {
				RadioButton item = new RadioButton(this);
				item.setText(a.getValue());
				item.setId(i++);
				r.addView(item);
			}
			answers.addView(r);
		}
	} else if(q.getType().equals(Question.TYPE_EXACT)){
		e = new EditText(this);
		e.setText(R.string.game_your_answer);
		e.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				e.setText("");
			}
		});
		
		e.setOnKeyListener(new OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
			    if (event.getAction()==KeyEvent.ACTION_DOWN&&keyCode==KeyEvent.KEYCODE_ENTER){
			    	buttonValidateClicked();
			    }
				return false;
			}
		});
		
		e.setSingleLine(true);	
		answers.addView(e);		
	}
	
	buttonNext.setText(R.string.game_answer);
	buttonNext.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			buttonValidateClicked();
		}
	});
		
}
   
public void dialogEnd() {
	AlertDialog.Builder dialog2 = new AlertDialog.Builder(this);
	dialog2.setTitle(R.string.game_next_stage); 
	
	dialog2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			quitView();
		}
	});
	
	dialog2.show();
	
}

public void quitView() {
       Intent courseDetails = new Intent(getApplicationContext(),CourseStepActivity.class);
       courseDetails.putExtra(Course.ID_EXTRA, course.getId());
       courseDetails.putExtra(Course.URL_EXTRA, course.getUrl());
       courseDetails.putExtra(Course.NEXT_PLACEMARK, true);
       startActivity(courseDetails);
       finish();
   }
   
public boolean checkAnswer() {
	if(q.getType().equals(Question.TYPE_MULTIPLE_CHOICE)) {
		if(q.hasMultipleAnswers()){
			List<Integer> ints = new ArrayList<Integer>();
			int size = answers.getChildCount();
			int i = 0;
			
			while(i<size){
				CheckBox c = (CheckBox) answers.getChildAt(i);
				if(c.isChecked()) {
					ints.add(i);
				}
				i++;
			}
			
			return q.checkAnswers(ints);
		} else {
			RadioGroup r = (RadioGroup) answers.getChildAt(0);
			return q.checkAnswer(r.getCheckedRadioButtonId());			
		}
	} else if(q.getType().equals(Question.TYPE_EXACT)) {
		EditText e = (EditText) answers.getChildAt(0);
		Log.d("edit","edit"+e.getText().toString());
		return q.checkAnswer(e.getText().toString());
	}
	return true;
}
   
}
