package com.toursims.mobile;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.toursims.mobile.controller.MessageWrapper;
import com.toursims.mobile.model.Message;
import com.toursims.mobile.ui.MessageAdapter;

public class ChatMessageActivity extends Activity {

	/**
	 * Android debugging tag
	 */
//	private final static String TAG = ChatActivity.class.getName();
	
	/**
	 * Application context
	 */
//	private TourSims tourSims;
	
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
//	    tourSims = (TourSims) getApplication();
	    
	    // Retreive the current user contacts
	    MessageWrapper messageWrapper = new MessageWrapper();
	    messages = messageWrapper.GetReplyMessages(getIntent().getIntExtra(Message.ROOT_MESSAGE_ID_EXTRA, 0));
	    
	    // Layout initialisation
	    ListView messagesListView = (ListView) findViewById(R.id.chat_listView_messages);
	    MessageAdapter messageAdapter = new MessageAdapter(this, messages);
	    messagesListView.setAdapter(messageAdapter);
	}

}
