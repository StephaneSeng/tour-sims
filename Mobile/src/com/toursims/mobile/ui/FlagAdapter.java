package com.toursims.mobile.ui;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.toursims.mobile.R;
import com.toursims.mobile.model.kml.Point;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FlagAdapter extends BaseAdapter {

	List<Point> items;
	LayoutInflater inflater;
	Context context;

	public FlagAdapter(Context context, List<Point> items) {
		inflater = LayoutInflater.from(context);
		this.items = items;
		this.context = context;
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
		LinearLayout wrapper;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.trace_item, null);
			holder.details = (TextView) convertView.findViewById(R.id.details);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.wrapper = (LinearLayout) convertView
					.findViewById(R.id.wrapper);

			final int pos = position;

			holder.wrapper.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					//Dialog d = new Dialog(context);
					//d.setTitle("Sélectionnez");
					//d.setContentView(R.layout.flag_edit);

					//TextView textDialog = (TextView) d.findViewById(R.id.name);
					//textDialog.setText(items.get(pos).getName());

					//MapView mapView = (MapView) d.findViewById(R.id.map);
					
			//		AlertDialog.Builder d = new AlertDialog.Builder(mapView.getContext());
			//		d.show();
					


					return false;
				}
			});

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (items.get(position).getName() != null) {
			holder.name.setText(items.get(position).getName());
		}

		holder.details.setText(items.get(position).getLongitude() + ":"
				+ items.get(position).getLatitude());

		return convertView;

	}

}
