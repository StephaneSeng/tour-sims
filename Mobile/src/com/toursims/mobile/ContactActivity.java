package com.toursims.mobile;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.toursims.mobile.controller.UserWrapper;
import com.toursims.mobile.model.User;
import com.toursims.mobile.ui.UserAdapter;

public class ContactActivity extends Activity {

	/**
	 * Android debugging tag
	 */
//	private final static String TAG = ContactActivity.class.getName();
	
	/**
	 * Application context
	 */
	private TourSims tourSims;
	
	/**
	 * The current user contacts
	 */
	private List<User> contacts;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layout_contact);
	    
	    // Application context initialisation
	    tourSims = (TourSims) getApplication();
	    
	    // Retreive the current user contacts
	    UserWrapper userWrapper = new UserWrapper();
	    contacts = userWrapper.GetContacts(tourSims.getUser().getUserId());
	    
	    // Layout initialisation
	    ListView contactsListView = (ListView) findViewById(R.id.contact_listView_contacts);
	    UserAdapter userAdapter = new UserAdapter(this, contacts);
	    contactsListView.setAdapter(userAdapter);
	    
	    // Event initialisation
	    contactsListView.setOnItemClickListener(new OnItemClickListener() {
	    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    		Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
	    		intent.putExtra(User.USER_ID_EXTRA, contacts.get(position).getUserId());
	    		startActivity(intent);
	    	}
		});
	}

}
