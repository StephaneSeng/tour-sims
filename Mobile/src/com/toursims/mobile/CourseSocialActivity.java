package com.toursims.mobile;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.toursims.mobile.controller.CommentWrapper;
import com.toursims.mobile.controller.CourseBDD;
import com.toursims.mobile.controller.RatingWrapper;
import com.toursims.mobile.model.Comment;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.ui.CommentAdapter;
import com.toursims.mobile.ui.ToolBox;

public class CourseSocialActivity extends Activity{
    private List<Comment> comments;
    private int courseId;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coursesocial);
        
        // Initialize the layout elements
        ImageView thumbnailImageView = (ImageView) findViewById(R.id.coursesocial_imageView_thumbnail);
        TextView nameTextView = (TextView) findViewById(R.id.coursesocial_textView_name);
        TextView descriptionTextView = (TextView) findViewById(R.id.coursesocial_textView_description);
        ListView commentsListView = (ListView) findViewById(R.id.coursesocial_listView_comments);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.coursesocial_ratingBar_rating);
        
        CourseBDD datasource;
        Course course = null;
        courseId = getIntent().getExtras().getInt(Course.ID_EXTRA);
        try {
        	datasource = new CourseBDD(this);
			datasource.open();
			course = datasource.getCourseWithId(courseId);
			datasource.close();
        } catch (IOException e) {
        	e.printStackTrace();
        }
        
        if (course != null) {
        	Bitmap bitmap = BitmapFactory.decodeFile(ToolBox.cacheFile(course.getCoverPictureURL(), getCacheDir().getAbsolutePath()));
        	thumbnailImageView.setImageBitmap(bitmap);
        	nameTextView.setText(course.getName());
        	descriptionTextView.setText(course.getDesc());
        }
        
        // Retreive the current course rating
        //RatingWrapper ratingWrapper = new RatingWrapper(getApplicationContext());
        //double rating = ratingWrapper.GetCourseRating(1);
        //ratingBar.setRating((float)rating);
        
        // Retreive the current course comments
	    //CommentWrapper commentWrapper = new CommentWrapper(getApplicationContext());
	    //comments = commentWrapper.GetCourseComments(1);
	    //CommentAdapter commentAdapter = new CommentAdapter(this, comments);
	    //commentsListView.setAdapter(commentAdapter);
    }
}