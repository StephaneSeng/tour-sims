package com.toursims.mobile;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.toursims.mobile.controller.MessageWrapper;
import com.toursims.mobile.model.Message;
import com.toursims.mobile.ui.MessageAdapter;

public class ChatMessageActivity extends SherlockActivity {

	/**
	 * The current user contacts
	 */
	private List<Message> messages;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_chat);

		new DownloadTask().execute();		

		// ActionBarSherlock setup
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(R.drawable.ic_menu_dialog_solo_colored);
		actionBar.setTitle(R.string.home_social_chat_message);
	}
	
	private class DownloadTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			setSupportProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// Retreive the current user contacts
			MessageWrapper messageWrapper = new MessageWrapper(getApplicationContext());
			messages = messageWrapper.GetReplyMessages(getIntent().getIntExtra(Message.ROOT_MESSAGE_ID_EXTRA, 0));

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// Layout initialisation
			ListView messagesListView = (ListView) findViewById(R.id.chat_listView_messages);
			MessageAdapter messageAdapter = new MessageAdapter(getApplicationContext(), messages);
			messagesListView.setAdapter(messageAdapter);

			setSupportProgressBarIndeterminateVisibility(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.menu_chat, menu);

	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		// Handle item selection
		switch (item.getItemId()) {
		case android.R.id.home:
			intent = new Intent(this, ChatActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		case R.id.chat_menuItem_refresh:
			new DownloadTask().execute();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
