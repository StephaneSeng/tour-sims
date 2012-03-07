package com.toursims.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.toursims.mobile.controller.UserWrapper;
import com.toursims.mobile.model.User;

public class ProfileActivity extends Activity {

	private User user;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layout_profile);
	    
	    // Initialise the current user
	    UserWrapper userWrapper = new UserWrapper();
	    user = userWrapper.GetProfile(getIntent().getIntExtra(User.USER_ID_EXTRA, 0));
	    
	    // Layout initialisation
	    ImageView avatarImageView = (ImageView) findViewById(R.id.profile_imageView_avatar);
	    TextView textView = (TextView) findViewById(R.id.profile_textView_user_name);
	    
	    avatarImageView.setImageBitmap(user.getAvatarBitmap());
	    textView.setText(user.getName());
	}

}
