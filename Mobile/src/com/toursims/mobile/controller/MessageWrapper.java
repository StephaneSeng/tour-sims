package com.toursims.mobile.controller;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.toursims.mobile.R;
import com.toursims.mobile.model.Message;
import com.toursims.mobile.util.places.EasySSLSocketFactory;

/**
 * Wrapper for the Message related webservices
 */
public class MessageWrapper {
	
	/**
	 * Android debugging tag
	 */
	private static final String TAG = MessageWrapper.class.toString();
	
	/**
	 * Application server root
	 */
	private String serverRoot;
	
	/**
	 * JSON "true" boolean output
	 */
//	private static final String JSON_TRUE = "t";
	
	/**
	 * SQL "null" output
	 */
	private static final String SQL_NULL = "null";
	
	/**
	 * Our HTTP client, used for making requests
	 */
	private HttpClient httpClient;
	
	/**
	 * Default constructor
	 * Initialize the HTTP client, we use a less secure one
	 */
	public MessageWrapper(Context context) {
		super();
		
		serverRoot = context.getString(R.string.server_root);
		
		// Create a HTTP server with minor security
		// Source : http://www.virtualzone.de/2011-02-27/how-to-use-apache-httpclient-with-httpsssl-on-android
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
		
		HttpParams params = new BasicHttpParams();
		params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
		params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		
		ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);
		
		// Modifications on the HTTP client for solving the "Connection still allocated" problem
		// See: http://iamvijayakumar.blogspot.com/2012/02/invalid-use-of-singleclientconnmanager.html
		this.httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, cm.getSchemeRegistry()), params);
	}

	/**
	 * Launch a SOAP request to the Mesage webservice
	 * List all the latest messages linked to the specified user
	 * @param user_id The id of the specified user
	 */
	public List<Message> GetMessages(int user_id) {
		// Return variable
		List<Message> messages = null;
		
		// Build the SOAP request
		StringBuffer request = new StringBuffer(serverRoot + "/message.php?");
		request.append("action=" + "get_messages");
		request.append("&user_id=" + user_id);

		Log.d(TAG, "Launching a Message request : " + request);
		HttpGet httpGet = new HttpGet(request.toString());
		HttpResponse httpResponse;
		
		try {
			httpResponse = httpClient.execute(httpGet);
			
			// JSON reconstruction
			InputStream inputStream = httpResponse.getEntity().getContent();
			byte[] buffer = new byte[1024];
		    int length;
		    StringBuilder builder = new StringBuilder();
		    while ((length = inputStream.read(buffer)) > 0) {
		            builder.append(new String(buffer, 0, length));
		    }
		    String json = builder.toString();
		    
		    Log.d(TAG, "JSON recieved : " + json);
		    
		    // Read the User informations
		    messages = jsonResponseParserMessage(json);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}
		
		return messages;
	}
	
	/**
	 * Launch a SOAP request to the Mesage webservice
	 * Select all the messages from one given thread
	 * @param root_message_id The id of the root message
	 */
	public List<Message> GetReplyMessages(int root_message_id) {
		// Return variable
		List<Message> messages = null;
		
		// Build the SOAP request
		StringBuffer request = new StringBuffer(serverRoot + "/message.php?");
		request.append("action=" + "get_reply_messages");
		request.append("&root_message_id=" + root_message_id);

		Log.d(TAG, "Launching a Message request : " + request);
		HttpGet httpGet = new HttpGet(request.toString());
		HttpResponse httpResponse;
		
		try {
			httpResponse = httpClient.execute(httpGet);
			
			// JSON reconstruction
			InputStream inputStream = httpResponse.getEntity().getContent();
			byte[] buffer = new byte[1024];
		    int length;
		    StringBuilder builder = new StringBuilder();
		    while ((length = inputStream.read(buffer)) > 0) {
		            builder.append(new String(buffer, 0, length));
		    }
		    String json = builder.toString();
		    
		    Log.d(TAG, "JSON recieved : " + json);
		    
		    // Read the User informations
		    messages = jsonResponseParserMessage(json);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}
		
		return messages;
	}
	
	/**
	 * Utility for parsing the JSON response from the Message webservice
	 * @param json The response JSON to parse
	 * @return A list of messages
	 */
	private List<Message> jsonResponseParserMessage(String json) throws JSONException, URISyntaxException {
		// Timestamp formatting
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy -MM -dd HH:mm:ss.S Z");
		
		List<Message> messages = new ArrayList<Message>();
		
		// Variables used for reading the JSON response
		JSONArray jsonResults = new JSONArray(json);
		JSONObject jsonResult;
		String jsonMessageId;
		String jsonText;
		String jsonLatitude;
		String jsonLongitude;
		String jsonTimestamp;
		String jsonReplyMessageId;
		String jsonWriterId;
		String jsonWriterName;
		String jsonWriterAvatar;
		String jsonReplyMessageCount;
		
		for (int j = 0; j < jsonResults.length(); j++) {
			jsonResult = jsonResults.getJSONObject(j);
			
			// Get the attributes from the JSON
			jsonMessageId = jsonResult.has("message_id") ? jsonResult.getString("message_id") : "0";
			jsonText = jsonResult.has("text") ? jsonResult.getString("text") : "";
			jsonLatitude = jsonResult.has("latitude") ? jsonResult.getString("latitude") : "";
			jsonLongitude = jsonResult.has("longitude") ? jsonResult.getString("longitude") : "";
			jsonTimestamp = jsonResult.has("timestamp") ? jsonResult.getString("timestamp") : "";
			jsonReplyMessageId = jsonResult.has("reply_message_id") ? jsonResult.getString("reply_message_id") : "";
			jsonWriterId = jsonResult.has("writer_id") ? jsonResult.getString("writer_id") : "";
			jsonWriterName = jsonResult.has("writer_name") ? jsonResult.getString("writer_name") : "";
			jsonWriterAvatar = jsonResult.has("writer_avatar") ? jsonResult.getString("writer_avatar") : "";
			jsonReplyMessageCount = jsonResult.has("reply_message_count") ? jsonResult.getString("reply_message_count") : "";
			
			Log.d(TAG, "message_id : " + jsonMessageId);
			Log.d(TAG, "text : " + jsonText);
			Log.d(TAG, "latitude : " + jsonLatitude);
			Log.d(TAG, "longitude : " + jsonLongitude);
			Log.d(TAG, "timestamp : " + jsonTimestamp);
			Log.d(TAG, "reply_message_id : " + jsonReplyMessageId);
			Log.d(TAG, "writer_id : " + jsonWriterId);
			Log.d(TAG, "writer_name : " + jsonWriterName);
			Log.d(TAG, "writer_avatar : " + jsonWriterAvatar);
			Log.d(TAG, "reply_message_count : " + jsonReplyMessageCount);
			
			// Construct the User object
			try {
				messages.add(new Message(Integer.parseInt(jsonMessageId), jsonText,
						Double.parseDouble(jsonLatitude), Double.parseDouble(jsonLongitude),
						simpleDateFormat.parse(jsonTimestamp.replace("+", " +").replace("-", " -") + "00"),
						(jsonReplyMessageId.equals(SQL_NULL)) ? 0 :Integer.parseInt(jsonReplyMessageId),
						Integer.parseInt(jsonWriterId), jsonWriterName, jsonWriterAvatar,
						Integer.parseInt(jsonReplyMessageCount)));
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				Log.e(TAG, e.toString());
			}
		}
		
		Log.d(TAG, "Nombre de Messages : " + messages.size());
		
		return messages;
	}
	
}
