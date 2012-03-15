package com.toursims.mobile.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.toursims.mobile.R;
import com.toursims.mobile.TraceMapActivity;
import com.toursims.mobile.model.Trace;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TraceAdapter extends BaseAdapter {

	List<Trace> items;
	List<Trace> items_full;
	LayoutInflater inflater;
	Context context;
	Set<Integer> selectedItems = new HashSet<Integer>();

	public TraceAdapter(Context context, List<Trace> items) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.items = items;
		this.items_full = items;
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
		TextView name;
		TextView details;
		LinearLayout wrapper;
		CheckBox checkBox;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.trace_item, null);

			holder.details = (TextView) convertView.findViewById(R.id.details);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.wrapper = (LinearLayout) convertView
					.findViewById(R.id.wrapper);
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBox);

			holder.name.setText(items.get(position).getName());
			holder.details.setText(new Date(items.get(position).getMillis())
					.toLocaleString());
			holder.checkBox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						private int pos = position;

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								selectedItems.add(pos);
							} else {
								selectedItems.remove(pos);
							}
						}
					});

			convertView.setTag(holder);

			holder.wrapper.setOnClickListener(new OnClickListener() {
				private int pos = position;

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, TraceMapActivity.class);
					intent.putExtra("TRACE", items_full.get(pos).getFile());
					context.startActivity(intent);
				}
			});

			Log.d("TAG", items_full.get(position).getFile());
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	public void deleteItems() {
		for (Integer i : selectedItems) {
			ToolBox.deleteItem(items_full.get(i), context);
			File f = new File(items_full.get(i).getFile());
			f.delete();			
		}
	}

}