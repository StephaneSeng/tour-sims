package com.toursims.mobile.ui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;

import com.toursims.mobile.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeAdapter extends BaseAdapter{
	LayoutInflater inflater;
	List<HomeItem> items;
	String cachePath;
	
	public HomeAdapter(Context context,List<HomeItem> items, String cachePath) {
		inflater = LayoutInflater.from(context);
		this.items = items;
		this.cachePath = cachePath;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return items.get(arg0);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	private class ViewHolder {
		ImageView image;
		TextView name;
		LinearLayout layout;
		}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		
		holder = new ViewHolder();
		convertView = inflater.inflate(R.layout.home_item, null);
		holder.image = (ImageView)convertView.findViewById(R.id.image);
		holder.name = (TextView)convertView.findViewById(R.id.text);
		holder.layout = (LinearLayout)convertView.findViewById(R.id.layout);
		holder.name.setText(items.get(position).getText());
		holder.image.setImageResource(items.get(position).getPictureURL());
		holder.layout.setOnClickListener(items.get(position).getFunction());
		convertView.setTag(holder);
		
		/*if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.home_item, null);
			holder.image = (ImageView)convertView.findViewById(R.id.image);
			holder.name = (TextView)convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(items.get(position).getText());
		holder.image.setBackgroundColor(Color.WHITE);
		
		try {
			String fileURL = items.get(position).getPictureURL();
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
			} catch (IOException e) {
			  e.printStackTrace();
			}*/
		
	return convertView;

	}
}
