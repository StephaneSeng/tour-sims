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
import com.toursims.mobile.model.User;

public class UserOverlay extends ItemizedOverlay<UserOverlayItem> {
	private ArrayList<UserOverlayItem> mOverlays = new ArrayList<UserOverlayItem>(); 
	private Context mContext;
	
	public UserOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}
	
	public void addOverlay(UserOverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected UserOverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		UserOverlayItem item = mOverlays.get(index);
		
		final User user = item.getUser();
		
		Dialog dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.social_dialog);
		
		// Retreive the layout components
		TextView nameTextView = (TextView) dialog.findViewById(R.id.social_dialog_textView_name);
		ImageView avatarImageView = (ImageView) dialog.findViewById(R.id.social_dialog_imageView_avatar);
		TextView dataTextView = (TextView) dialog.findViewById(R.id.social_dialog_textView_data);
		Button profileButton = (Button) dialog.findViewById(R.id.social_dialog_button_view_profile);
		Button sendMessageButton = (Button) dialog.findViewById(R.id.social_dialog_button_send_message);
		
		nameTextView.setText(user.getName());
		avatarImageView.setImageBitmap(user.getAvatarBitmap());
		dataTextView.setText(item.getSnippet());
		
		sendMessageButton.setVisibility(Button.GONE);
		
		// Intent initialisations
		profileButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				Intent intent = new Intent(mContext, ProfileActivity.class);
				intent.putExtra(User.USER_ID_EXTRA, user.getUserId());
				mContext.startActivity(intent);
			}
		});
		
		dialog.show();
		
		return true;
	}
	
}
