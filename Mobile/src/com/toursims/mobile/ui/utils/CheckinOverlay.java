package com.toursims.mobile.ui.utils;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.toursims.mobile.ProfileActivity;
import com.toursims.mobile.R;
import com.toursims.mobile.TourSims;
import com.toursims.mobile.WriteActivity;
import com.toursims.mobile.controller.UserWrapper;
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
		Button sendMessageButton = (Button) dialog.findViewById(R.id.social_dialog_button_send_message);
		Button addContactButton = (Button) dialog.findViewById(R.id.social_dialog_button_add_contact);
		
		nameTextView.setText(checkin.getName());
		avatarImageView.setImageBitmap(checkin.getAvatarBitmap());
		dataTextView.setText("Latitude : " + checkin.getLatitude() + ", Longitude : " + checkin.getLongitude() + ", Timestamp : " + checkin.getTimestamp());
		
		// Intent initialisations
		profileButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(mContext, ProfileActivity.class);
				intent.putExtra(User.USER_ID_EXTRA, checkin.getUserId());
				intent.putExtra(ProfileActivity.IS_USER_PROFILE, false);
				mContext.startActivity(intent);
			}
		});
		
		sendMessageButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(mContext, WriteActivity.class);
				intent.putExtra(WriteActivity.DEST_USER_ID, checkin.getUserId());
				((Activity)mContext).startActivityForResult(intent, 0);
			}
		});
		
		addContactButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				TourSims tourSims = (TourSims) ((Activity)mContext).getApplication();
				if (tourSims.isUserLoggedIn()) {
					// Add the specified user as a contact for both users (simplification) 
					UserWrapper userWrapper = new UserWrapper(mContext);
					userWrapper.AddContact(tourSims.getUser().getUserId(), checkin.getUserId());
					userWrapper.AddContact(checkin.getUserId(), tourSims.getUser().getUserId());
					
					Toast toast = Toast.makeText(mContext, R.string.overlay_add_contact_ok, Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});
		
		dialog.show();
		
		return true;
	}
	
}
