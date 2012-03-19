package com.toursims.mobile.controller;

import java.io.InputStream;
import java.net.URISyntaxException;
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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.toursims.mobile.R;
import com.toursims.mobile.model.City;
import com.toursims.mobile.model.Course;
import com.toursims.mobile.util.places.EasySSLSocketFactory;

/**
 * Wrapper for the Course related webservices
 */
public class CourseWrapper {
	
	/**
	 * Android debugging tag
	 */
	private static final String TAG = CourseWrapper.class.toString();
	
	/**
	 * Application server root
	 */
	private String serverRoot;
	
	/**
	 * Our HTTP client, used for making requests
	 */
	private HttpClient httpClient;
	
	private Context context;
	
	/**
	 * Default constructor
	 * Initialize the HTTP client, we use a less secure one
	 */
	public CourseWrapper(Context context) {
		super();
		
		this.context = context;
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
		this.httpClient = new DefaultHttpClient(cm, params);
	}

	/**
	 * Launch a SOAP request to the Course webservice
	 * Find all the courses that have been put on our server
	 * @return A list of courses
	 */
	public void GetCourses() {
		// Return variable
//		List<Course> courses = new ArrayList<Course>();
		
		// Build the SOAP request
		StringBuffer request = new StringBuffer(serverRoot + "/course.php?");
		request.append("action=" + "get_courses");

		Log.d(TAG, "Launching a Course request : " + request);
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
		    
		    // Construct the list of Cities
		    jsonResponseParser(json);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}
		
//		return courses;
	}
	
	/**
	 * Launch a SOAP request to the Course webservice
	 * Find all the courses that have been put on our server from one city
	 * @return A list of courses from one city
	 */
	public void GetCourses(int city_id) {
		// Return variable
//		List<Course> courses = new ArrayList<Course>();
		
		// Build the SOAP request
		StringBuffer request = new StringBuffer(serverRoot + "/course.php?");
		request.append("action=" + "get_city_courses");
		request.append("&city_id=" + city_id);

		Log.d(TAG, "Launching a Course request : " + request);
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
		    
		    // Construct the list of Cities
		    jsonResponseParser(json);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}
		
//		return courses;
	}
	
	/**
	 * Utility for parsing the JSON response from the Course webservice
	 * @param json The response JSON to parse
	 * @return A list of courses
	 */
	private void jsonResponseParser(String json) throws JSONException, URISyntaxException {
//		List<Course> courses = new ArrayList<Course>();
		
		// Variables used for reading the JSON response
		JSONArray jsonResults = new JSONArray(json);
		JSONObject jsonResult;
		String jsonFile;
		Course course;
		CourseBDD datasource = null;
		
		try {
			datasource = new CourseBDD(context);
			datasource.open();
			datasource.truncate();
		
			for (int j = 0; j < jsonResults.length(); j++) {
				jsonResult = jsonResults.getJSONObject(j);
				
				// Get the attributes from the JSON
				jsonFile = jsonResult.has("file") ? jsonResult.getString("file") : "";
				
				Log.d(TAG, "file : " + jsonFile);
				
				// Construct the Course object
				course = CourseLoader.getInstance().parse(jsonFile);
				course.setUrl(jsonFile);
				datasource.insertCourse(course);
				course.setId(datasource.getCourseIdWithURL(jsonFile));
				datasource.insertPlacemarks(course);
				
				Log.d(TAG, "file : id = " + datasource.getCourseIdWithURL(jsonFile));
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (datasource != null) {
			datasource.close();
		}
		
//		Log.d(TAG, "Nombre de Cities : " + courses.size());
		
//		return courses;
	}
	
}
