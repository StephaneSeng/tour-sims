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
import com.toursims.mobile.model.Message;

/**
 * A list adapter for displaying Messages objects
 */
public class MessageAdapter extends BaseAdapter {
	
	Context context;
	List<Message> messages;
	
	public MessageAdapter(Context context, List<Message> messages) {
		this.context = context;
		this.messages = messages;
	}
	
	public int getCount() {
		return messages.size();
	}

	public Object getItem(int arg0) {
		return messages.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		Message message = messages.get(position);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.message_item, parent, false);
		ImageView avatarImageView = (ImageView) rowView.findViewById(R.id.message_imageView_avatar);
		TextView textTextView = (TextView) rowView.findViewById(R.id.message_textView_text);
		TextView metadataTextView = (TextView) rowView.findViewById(R.id.message_textView_metadata);
		
		textTextView.setText(message.getText());
		avatarImageView.setImageBitmap(message.getWriterAvatarBitmap());
		metadataTextView.setText("Latitude : " + message.getLatitude() + ", Longitude : " + message.getLongitude() + ", Timestamp : " + message.getTimestamp() + ", Message count : " + message.getReplyMessageCount());
		
		return rowView;
	}
}
