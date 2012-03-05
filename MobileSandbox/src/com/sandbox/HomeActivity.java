package com.sandbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends FragmentActivity {

	/**
	 * Called when the activity is first created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layout_home);
	    
	    // ActionBarSherlock configuration
	    
	    // Event handlers
	    Button chatButton = (Button)findViewById(R.id.button_chat);
	    chatButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent().setClass(getApplicationContext(), ChatActivity.class));
			}
		});
	}

}
