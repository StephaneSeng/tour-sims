package com.toursims.mobile;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.toursims.mobile.controller.MessageWrapper;
import com.toursims.mobile.model.Message;
import com.toursims.mobile.ui.MessageAdapter;

public class ChatActivity extends Activity {

	/**
	 * Android debugging tag
	 */
//	private final static String TAG = ChatActivity.class.getName();
	
	/**
	 * Application context
	 */
	private TourSims tourSims;
	
	/**
	 * The current user contacts
	 */
	private List<Message> messages;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.layout_chat);
	    
	    // Application context initialisation
	    tourSims = (TourSims) getApplication();
	    
	    // Retreive the current user contacts
	    MessageWrapper messageWrapper = new MessageWrapper();
	    messages = messageWrapper.GetMessages(tourSims.getUser().getUserId());
	    
	    // Layout initialisation
	    ListView messagesListView = (ListView) findViewById(R.id.chat_listView_messages);
	    MessageAdapter messageAdapter = new MessageAdapter(this, messages);
	    messagesListView.setAdapter(messageAdapter);
	    
	    // Event initialisation
	    messagesListView.setOnItemClickListener(new OnItemClickListener() {
	    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	    		Intent intent = new Intent(getApplicationContext(), ChatMessageActivity.class);
	    		intent.putExtra(Message.ROOT_MESSAGE_ID_EXTRA, messages.get(position).getReplyMessageId());
	    		startActivity(intent);
	    	}
		});
	}

}
