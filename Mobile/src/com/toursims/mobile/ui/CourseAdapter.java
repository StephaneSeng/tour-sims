package com.toursims.mobile.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.toursims.mobile.R;
import com.toursims.mobile.model.Course;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
	
	public CourseAdapter(Context context,List<Course> courses) {

		inflater = LayoutInflater.from(context);
		this.courses = courses;

	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return courses.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return courses.get(arg0);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
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

		double rating = courses.get(position).getRating();
		float r = (float) rating;
		
		holder.name.setText(courses.get(position).getName());
		holder.description.setText(courses.get(position).getDesc());
		holder.rating.setRating((float) r);
		holder.image.setBackgroundColor(Color.WHITE);
		
		try {
			  Bitmap bitmap = BitmapFactory.decodeStream(
			                 (InputStream)new URL(courses.get(position).getCoverPictureURL()).getContent());
			  holder.image.setImageBitmap(bitmap);
			} catch (MalformedURLException e) {
			  e.printStackTrace();
			} catch (IOException e) {
			  e.printStackTrace();
			}
		
	return convertView;

	}
	
}
