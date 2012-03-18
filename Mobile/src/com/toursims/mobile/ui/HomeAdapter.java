package com.toursims.mobile.ui;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.toursims.mobile.R;

public class HomeAdapter extends BaseAdapter {

	LayoutInflater inflater;
	List<HomeItem> items;
	String cachePath;
	Context context;
	int i;

	public HomeAdapter(Context context, List<HomeItem> items, String cachePath) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.items = items;
		this.cachePath = cachePath;
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
		ImageView image;
		TextView name;
		LinearLayout layout;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		holder = new ViewHolder();
		convertView = inflater.inflate(R.layout.home_item, null);
		holder.image = (ImageView) convertView.findViewById(R.id.image);
		holder.name = (TextView) convertView.findViewById(R.id.text);
		holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);

		if (items.get(position).getFunction() != null)
			holder.layout.setOnClickListener(items.get(position).getFunction());

		if (items.get(position).getC() != null) {
			CustomOnClickListener listener = new CustomOnClickListener();
			listener.setI(position);
			holder.layout.setOnClickListener(listener);
		}

		holder.name.setText(items.get(position).getText());

		if (items.get(position).getPictureURLString() != null) {
			String fileName = ToolBox.cacheFile(items.get(position).getPictureURLString(), cachePath);
			if (fileName != null) {
				Bitmap myBitmap = BitmapFactory.decodeFile(fileName);
				holder.image.setImageBitmap(myBitmap);
			}
		} else {
			holder.image.setImageResource(items.get(position).getPictureURL());
		}
		convertView.setTag(holder);

		return convertView;
	}

	public List<HomeItem> getItems() {
		return items;
	}

	public void setItems(List<HomeItem> items) {
		this.items = items;
	}

	public class CustomOnClickListener implements OnClickListener {
		int i;

		public void onClick(View v) {
			Intent intent = new Intent(context.getApplicationContext(), items.get(i).getC());
			context.startActivity(intent);
		}

		public void setI(int i) {
			this.i = i;
		}
	}

}
