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

import android.util.Log;

import com.toursims.mobile.model.City;
import com.toursims.mobile.util.places.EasySSLSocketFactory;

/**
 * Wrapper for the City related webservices
 */
public class CityWrapper {
	
	/**
	 * Android debugging tag
	 */
	private static final String TAG = CityWrapper.class.toString();
	
	/**
	 * Our HTTP client, used for making requests
	 */
	private HttpClient httpClient;
	
	/**
	 * Default constructor
	 * Initialize the HTTP client, we use a less secure one
	 */
	public CityWrapper() {
		super();
		
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
	 * Launch a SOAP request to the City webservice
	 * Find cities around a specified location
	 * @param latitude The latitude of the specified location
	 * @param longitude The longitude of the specified location
	 * @return A list of relevant cities
	 */
	public List<City> SearchCities(double latitude, double longitude) {
		// Return variable
		List<City> cities = new ArrayList<City>();
		
		// Build the SOAP request
		StringBuffer request = new StringBuffer("http://192.168.0.1:80/city.php?");
		request.append("action=" + "get");
		request.append("&latitude=" + latitude);
		request.append("&longitude=" + longitude);

		Log.d(TAG, "Launching a City request : " + request);
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
		    
		    // Construct the list of Places
		    cities = jsonResponseParser(json);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}
		
		return cities;
	}

	/**
	 * Utility for parsing the JSON response from the City webservice
	 * @param json The response JSON to parse
	 * @return A list of cities
	 */
	private List<City> jsonResponseParser(String json) throws JSONException, URISyntaxException {
		List<City> cities = new ArrayList<City>();
		
		// Variables used for reading the JSON response
		JSONArray jsonResults = new JSONArray(json);
		JSONObject jsonResult;
		String jsonName;
		String jsonImage;
		
		for (int j = 0; j < jsonResults.length(); j++) {
			jsonResult = jsonResults.getJSONObject(j);
			
			// Get the attributes from the JSON
			jsonName = jsonResult.has("name") ? jsonResult.getString("name") : "";
			jsonImage = jsonResult.has("image") ? jsonResult.getString("image") : "";
			
			Log.d(TAG, "Name : " + jsonName);
			Log.d(TAG, "Image : " + jsonImage);
			
			// Construct the Place object
			cities.add(new City(jsonName, jsonImage));
		}
		
		Log.d(TAG, "Nombre de Cities : " + cities.size());
		
		return cities;
	}
	
}
