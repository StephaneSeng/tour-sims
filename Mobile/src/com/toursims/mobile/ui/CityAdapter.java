package com.toursims.mobile.ui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;

import com.toursims.mobile.R;
import com.toursims.mobile.model.City;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CityAdapter extends BaseAdapter {
	
	List<City> cities;
	LayoutInflater inflater;
	String cachePath;
	
	public CityAdapter(Context context,List<City> cities,String cachePath) {
		inflater = LayoutInflater.from(context);
		this.cities = cities;
		this.cachePath = cachePath;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return cities.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return cities.get(arg0);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	private class ViewHolder {
		ImageView image;
		TextView name;
		}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.city_item, null);
			
			holder.image = (ImageView)convertView.findViewById(R.id.cityImage);
			holder.name = (TextView)convertView.findViewById(R.id.cityName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(cities.get(position).getName());
		holder.image.setBackgroundColor(Color.WHITE);
		
		try {
			String fileURL = cities.get(position).getCoverPictureURL();
			String fileName = cachePath + fileURL.replaceAll("[.|/|:]", "_");
			
			File file = new File(fileName);
			
			if(!file.exists()){
				URL url = new URL(fileURL); 				                       
				URLConnection ucon = url.openConnection();
				 
				InputStream is = ucon.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is);
				 
				ByteArrayBuffer baf = new ByteArrayBuffer(50);
				int current = 0;
				while ((current = bis.read()) != -1) {
					baf.append((byte) current);
				}
				 
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(baf.toByteArray());
				fos.close();
			}
				
			if(file.exists()){
			    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			    holder.image.setImageBitmap(myBitmap);
			}
			
			} catch (MalformedURLException e) {
			  e.printStackTrace();
			  holder.image.setImageResource(R.drawable.ic_menu_globe);
			} catch (IOException e) {
			  e.printStackTrace();
			  holder.image.setImageResource(R.drawable.ic_menu_globe);
			}
		
	return convertView;

	}
	
}
