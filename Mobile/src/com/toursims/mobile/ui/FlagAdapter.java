package com.toursims.mobile.ui;

import java.util.List;

import com.toursims.mobile.R;
import com.toursims.mobile.model.kml.Point;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FlagAdapter extends BaseAdapter {

	List<Point> items;
	LayoutInflater inflater;
	
	public FlagAdapter(Context context,List<Point> items){
		inflater = LayoutInflater.from(context);
		this.items = items;
	}
	
	public int getCount() {
		return items.size();
	}
	
	public Object getItem(int arg0) {
		return items.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}
	
	private class ViewHolder {
		TextView name;
		TextView details;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.trace_item, null);
			holder.details = (TextView)convertView.findViewById(R.id.details);
			holder.name = (TextView)convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if(items.get(position).getName()!=null){
			holder.name.setText(items.get(position).getName());
		}
		
		holder.details.setText(items.get(position).getLongitude()+":"+items.get(position).getLatitude());
		
	return convertView;

	}
}
 