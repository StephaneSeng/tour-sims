package com.toursims.mobile.ui;

import java.util.List;

import com.toursims.mobile.FlagMapActivity;
import com.toursims.mobile.R;
import com.toursims.mobile.model.kml.Point;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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

			holder.wrapper.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, FlagMapActivity.class);
					intent = items.get(pos).toIntent(intent);
					context.startActivity(intent);
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
