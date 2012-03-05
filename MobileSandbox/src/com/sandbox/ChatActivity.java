package com.sandbox;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ChatActivity extends FragmentActivity {

	/**
	 * Called when the activity is first created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layout_chat);
	    
	    // ActionBarSherlock configuration
	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    getSupportActionBar().setTitle("Chat");
	}

}
