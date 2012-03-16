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
import com.toursims.mobile.model.Comment;
import com.toursims.mobile.util.places.EasySSLSocketFactory;

/**
 * Wrapper for the Comment related webservices
 */
public class CommentWrapper {
	
	/**
	 * Android debugging tag
	 */
	private static final String TAG = CommentWrapper.class.toString();
	
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
//	private static final String SQL_NULL = "null";
	
	/**
	 * Our HTTP client, used for making requests
	 */
	private HttpClient httpClient;
	
	/**
	 * Default constructor
	 * Initialize the HTTP client, we use a less secure one
	 */
	public CommentWrapper(Context context) {
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
	 * Launch a SOAP request to the Comment webservice
	 * List all the latest comments linked to the specified course
	 * @param course_id The id of the specified course
	 */
	public List<Comment> GetCourseComments(int course_id) {
		// Return variable
		List<Comment> comments = null;
		
		// Build the SOAP request
		StringBuffer request = new StringBuffer(serverRoot + "/comment.php?");
		request.append("action=" + "get_course_comments");
		request.append("&course_id=" + course_id);

		Log.d(TAG, "Launching a Comment request : " + request);
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
		    comments = jsonResponseParserComment(json);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}
		
		return comments;
	}
	
	/**
	 * Utility for parsing the JSON response from the Comment webservice
	 * @param json The response JSON to parse
	 * @return A list of comments
	 */
	private List<Comment> jsonResponseParserComment(String json) throws JSONException, URISyntaxException {
		// Timestamp formatting
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy -MM -dd HH:mm:ss.S Z");
		
		List<Comment> comments = new ArrayList<Comment>();
		
		// Variables used for reading the JSON response
		JSONArray jsonResults = new JSONArray(json);
		JSONObject jsonResult;
		String jsonCommentId;
		String jsonText;
		String jsonTimestamp;
		String jsonUserId;
		String jsonUserName;
		String jsonUserAvatar;
		
		for (int j = 0; j < jsonResults.length(); j++) {
			jsonResult = jsonResults.getJSONObject(j);
			
			// Get the attributes from the JSON
			jsonCommentId = jsonResult.has("comment_id") ? jsonResult.getString("comment_id") : "0";
			jsonText = jsonResult.has("text") ? jsonResult.getString("text") : "";
			jsonTimestamp = jsonResult.has("timestamp") ? jsonResult.getString("timestamp") : "";
			jsonUserId = jsonResult.has("user_id") ? jsonResult.getString("user_id") : "";
			jsonUserName = jsonResult.has("user_name") ? jsonResult.getString("user_name") : "";
			jsonUserAvatar = jsonResult.has("user_avatar") ? jsonResult.getString("user_avatar") : "";
			
			Log.d(TAG, "comment_id : " + jsonCommentId);
			Log.d(TAG, "text : " + jsonText);
			Log.d(TAG, "timestamp : " + jsonTimestamp);
			Log.d(TAG, "user_id : " + jsonUserId);
			Log.d(TAG, "user_name : " + jsonUserName);
			Log.d(TAG, "user_avatar : " + jsonUserAvatar);
			
			// Construct the Comment object
			try {
				comments.add(new Comment(Integer.parseInt(jsonCommentId), jsonText,
						simpleDateFormat.parse(jsonTimestamp.replace("+", " +").replace("-", " -") + "00"),
						Integer.parseInt(jsonUserId), jsonUserName, jsonUserAvatar));
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				Log.e(TAG, e.toString());
			}
		}
		
		Log.d(TAG, "Nombre de Commentaires : " + comments.size());
		
		return comments;
	}
	
}
