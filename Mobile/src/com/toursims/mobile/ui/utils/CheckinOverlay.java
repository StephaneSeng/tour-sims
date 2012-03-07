package com.toursims.mobile.ui.utils;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.toursims.mobile.ProfileActivity;
import com.toursims.mobile.R;
import com.toursims.mobile.model.Checkin;
import com.toursims.mobile.model.User;

public class CheckinOverlay extends ItemizedOverlay<CheckinOverlayItem> {
	private ArrayList<CheckinOverlayItem> mOverlays = new ArrayList<CheckinOverlayItem>(); 
	private Context mContext;
	
	public CheckinOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}
	
	public void addOverlay(CheckinOverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected CheckinOverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		CheckinOverlayItem item = mOverlays.get(index);
		
		final Checkin checkin = item.getCheckin();
		
		Dialog dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.social_dialog);
		
		// Retreive the layout components
		TextView nameTextView = (TextView) dialog.findViewById(R.id.social_dialog_textView_name);
		ImageView avatarImageView = (ImageView) dialog.findViewById(R.id.social_dialog_imageView_avatar);
		TextView dataTextView = (TextView) dialog.findViewById(R.id.social_dialog_textView_data);
		Button profileButton = (Button) dialog.findViewById(R.id.social_dialog_button_view_profile);
//		Button sendMessageButton = (Button) dialog.findViewById(R.id.social_dialog_button_send_message);
		
		nameTextView.setText(checkin.getName());
		avatarImageView.setImageBitmap(checkin.getAvatarBitmap());
		dataTextView.setText("Latitude : " + checkin.getLatitude() + ", Longitude : " + checkin.getLongitude() + ", Timestamp : " + checkin.getTimestamp());
		
		// Intent initialisations
		profileButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				Intent intent = new Intent(mContext, ProfileActivity.class);
				intent.putExtra(User.USER_ID_EXTRA, checkin.getUserId());
				mContext.startActivity(intent);
			}
		});
		
		dialog.show();
		
		return true;
	}
	
}
