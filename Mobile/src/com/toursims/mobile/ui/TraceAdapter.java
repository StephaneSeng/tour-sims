package com.toursims.mobile.ui;

import java.util.List;

import com.google.api.client.util.DateTime;
import com.toursims.mobile.R;
import com.toursims.mobile.TraceMapActivity;
import com.toursims.mobile.TracesActivity;
import com.toursims.mobile.model.Trace;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TraceAdapter extends BaseAdapter {

	List<Trace> items;
	List<Trace> items_full;
	LayoutInflater inflater;
	Context context;

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
		ImageView imageDelete;
		LinearLayout wrapper;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.trace_item, null);

			holder.details = (TextView) convertView.findViewById(R.id.details);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.imageDelete = (ImageView) convertView
					.findViewById(R.id.imageDelete);
			holder.wrapper = (LinearLayout) convertView
					.findViewById(R.id.wrapper);

			holder.name.setText(items.get(position).getName());
			holder.details
					.setText(new DateTime(items.get(position).getMillis())
							.toStringRfc3339());

			convertView.setTag(holder);

			holder.imageDelete.setOnClickListener(new OnClickListener() {
				private int pos = position;

				@Override
				public void onClick(View v) {
					ToolBox.deleteItem(items_full.get(pos), context);
					int i = 0;

					while (i < items.size()) {
						if (items.get(i).getId() == items_full.get(pos).getId())
							break;
						i++;
					}

					if (i < items.size()) {
						items.remove(i);
					}

					notifyDataSetChanged();
				}
			});

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
}