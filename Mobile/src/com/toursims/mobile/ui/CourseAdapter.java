package com.toursims.mobile.ui;

import java.util.List;

import com.toursims.mobile.R;
import com.toursims.mobile.controller.RatingWrapper;
import com.toursims.mobile.model.Course;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


public class CourseAdapter extends BaseAdapter {
	
	List<Course> courses;
	LayoutInflater inflater;
	String cachePath;
	Context context;
	
	public CourseAdapter(Context context,List<Course> courses,String cachePath) {
		inflater = LayoutInflater.from(context);
		this.courses = courses;
		this.cachePath = cachePath;
		this.context = context;
	}
	
	public int getCount() {
		return courses.size();
	}

	public Object getItem(int arg0) {
		return courses.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}

	
	private class ViewHolder {
		ImageView image;
		TextView name;
		TextView description;
		RatingBar rating;
		}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.coursegame_item, null);
			
			holder.image = (ImageView)convertView.findViewById(R.id.courseImage);
			holder.name = (TextView)convertView.findViewById(R.id.courseName);
			holder.description = (TextView)convertView.findViewById(R.id.courseDetails);
			holder.rating = (RatingBar)convertView.findViewById(R.id.courseRating);	
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

//		double rating = courses.get(position).getRating();
//		float r = (float) rating;
		
		// Plumbing
		RatingWrapper ratingWrapper = new RatingWrapper(context);
		double rating = ratingWrapper.GetCourseRating(courses.get(position).getId());
		float r = (float) rating;
		
		holder.name.setText(courses.get(position).getName());
		
		if(courses.get(position).getAuthor()!=null){
//			holder.description.setText(courses.get(position).getAuthor());
			holder.description.setText(courses.get(position).getDesc());
		}
		
		holder.rating.setRating((float) r);	
		String fileURL = courses.get(position).getCoverPictureURL();
		String fileName = ToolBox.cacheFile(fileURL, cachePath);
			
		if(fileName!=null){
			Bitmap myBitmap = BitmapFactory.decodeFile(fileName);
		    holder.image.setImageBitmap(myBitmap);
		} 
		
	return convertView;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
}
