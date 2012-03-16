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
import com.toursims.mobile.model.Comment;

/**
 * A list adapter for displaying Comments objects
 */
public class CommentAdapter extends BaseAdapter {
	
	Context context;
	List<Comment> comments;
	
	public CommentAdapter(Context context, List<Comment> comments) {
		this.context = context;
		this.comments = comments;
	}
	
	public int getCount() {
		return comments.size();
	}

	public Object getItem(int arg0) {
		return comments.get(arg0);
	}

	public long getItemId(int position) {
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		Comment comment = comments.get(position);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = inflater.inflate(R.layout.comment_item, parent, false);
		ImageView avatarImageView = (ImageView) rowView.findViewById(R.id.comment_imageView_avatar);
		TextView textTextView = (TextView) rowView.findViewById(R.id.comment_textView_text);
		TextView metadataTextView = (TextView) rowView.findViewById(R.id.comment_textView_metadata);
		
		textTextView.setText(comment.getText());
		avatarImageView.setImageBitmap(comment.getUserAvatarBitmap());
		metadataTextView.setText("Timestamp : " + comment.getTimestamp());
		
		return rowView;
	}
}
