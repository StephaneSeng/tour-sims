package com.toursims.mobile.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toursims.mobile.R;
import com.toursims.mobile.model.User;

/**
 * A list adapter for displaying User objects (used for the contacts)
 */
public class UserAdapter extends BaseAdapter {
	
	Context context;
	List<User> users;
	
	public UserAdapter(Context context, List<User> users) {
		this.context = context;
		this.users = users;
	}
	
	public int getCount() {
		return users.size();
	}

	public Object getItem(int arg0) {
		return users.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.contact_item, parent, false);
		ImageView avatarImageView = (ImageView) rowView.findViewById(R.id.contact_item_imageView_avatar);
		TextView userNameTextView = (TextView) rowView.findViewById(R.id.contact_item_textView_user_name);
		
		userNameTextView.setText(users.get(position).getName());
		avatarImageView.setImageBitmap(users.get(position).getAvatarBitmap());
		
		return rowView;
	}
}
