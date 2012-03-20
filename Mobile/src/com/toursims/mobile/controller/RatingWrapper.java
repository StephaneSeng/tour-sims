package com.toursims.mobile.controller;

import java.io.InputStream;
import java.net.URISyntaxException;

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
import android.net.Uri;
import android.util.Log;

import com.toursims.mobile.R;
import com.toursims.mobile.util.places.EasySSLSocketFactory;

/**
 * Wrapper for the Rating related webservices
 */
public class RatingWrapper {
	
	/**
	 * Android debugging tag
	 */
	private static final String TAG = RatingWrapper.class.toString();
	
	/**
	 * SQL "null" output
	 */
	private static final String SQL_NULL = "null";
	
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
	public RatingWrapper(Context context) {
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
	 * Launch a SOAP request to the Rating webservice
	 * Retreive the ratings linked to the specified course
	 * @param course_name The name of the specified course
	 */
	public double GetCourseRating(String course_name) {
		// Return variable
		double rating = 0;
		
		// Build the SOAP request
		StringBuffer request = new StringBuffer(serverRoot + "/rating.php?");
		request.append("action=" + "get_course_rating");
		request.append("&course_name=" + Uri.encode(course_name));

		Log.d(TAG, "Launching a Rating request : " + request);
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
		    rating = jsonResponseParserRating(json);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}
		
		return rating;
	}
	
	/**
	 * Launch a SOAP request to the Rating webservice
	 * Create a rating linked to the specified course
	 * @param course_name The name of the specified course
	 */
	public void CreateCourseRating(double rating, int user_id, String course_name) {
		// Build the SOAP request
		StringBuffer request = new StringBuffer(serverRoot + "/rating.php?");
		request.append("action=" + "create_course_rating");
		request.append("&rating=" + rating);
		request.append("&user_id=" + user_id);
		request.append("&course_name=" + Uri.encode(course_name));

		Log.d(TAG, "Launching a Rating request : " + request);
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
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}
	}
	
	/**
	 * Utility for parsing the JSON response from the Rating webservice
	 * @param json The response JSON to parse
	 * @return A list of comments
	 */
	private double jsonResponseParserRating(String json) throws JSONException, URISyntaxException {
		double rating = 0;
		
		// Variables used for reading the JSON response
		JSONArray jsonResults = new JSONArray(json);
		JSONObject jsonResult = jsonResults.getJSONObject(0);
		String jsonRating;
		
		// Get the attributes from the JSON
		jsonRating = jsonResult.has("rating") ? jsonResult.getString("rating") : "0";
		
		Log.d(TAG, "rating : " + jsonRating);
		
		// Construct the Rating object
		try {
			if (jsonRating.contains(SQL_NULL)) {
				rating = 0;
			} else {
				rating = Double.parseDouble(jsonRating);
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}
		
		Log.d(TAG, "Ratings : " + rating);
		
		return rating;
	}
	
}
