package com.toursims.mobile.controller;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
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

import com.toursims.mobile.model.places.Place;
import com.toursims.mobile.model.places.PlaceTypes;
import com.toursims.mobile.util.places.EasySSLSocketFactory;

/**
 * Wrapper for the Google Places API
 */
public class PlaceWrapper {
	
	/**
	 * Android debugging tag
	 */
	private static final String TAG = PlaceWrapper.class.getName();
	
	/**
	 * Google Maps API key
	 */
	private static final String KEY = "AIzaSyDPcfMkBspS9GCNcDNnyhy9y6nhTM8_k3E";
	
	/**
	 * True if the device has a GPS sensor, false otherwise
	 */
	private static final boolean SENSOR = true;
	
	/**
	 * Our HTTP client, used for making requests
	 */
	private HttpClient httpClient;
	
	/**
	 * Default constructor
	 * Initialize the HTTP client, we use a less secure one
	 */
	public PlaceWrapper() {
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
	 * Launch a SOAP request to the Places API
	 * Find places around a specified location
	 * @param latitude The latitude of the specified location
	 * @param longitude The longitude of the specified location
	 * @param radius The radius of the searched space
	 * @param types Filter the found places by their types
	 * @return A list of relevant places
	 */
	public List<Place> SearchPlaces(double latitude, double longitude, double radius, List<PlaceTypes> types) {
		// Return variable
		List<Place> places = new ArrayList<Place>();
		
		// Build the SOAP request
		StringBuffer request = new StringBuffer("https://maps.googleapis.com/maps/api/place/search/json?");
		request.append("key=" + KEY);
		request.append("&location=" + latitude + "," + longitude);
		request.append("&radius=" + radius);
		request.append("&sensor=" + SENSOR);
		
		// Read the types list
		request.append("&type=");
		Iterator<PlaceTypes> i = types.iterator();
		while (i.hasNext()) {
			request.append(i.next().toString());
			
			// Multiple types case
			if (i.hasNext()) {
				request.append("|");
			}
		}
		
		Log.d(TAG, "Launching a Places request : " + request);
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
		    places = jsonResponseParser(json);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}
		
		return places;
	}

	/**
	 * Utility for parsing the JSON response from the Places API 
	 * @param json The response JSON to parse
	 * @return A list of places
	 */
	private List<Place> jsonResponseParser(String json) throws JSONException, URISyntaxException {
		List<Place> places = new ArrayList<Place>();
		
		// Variables used for reading the JSON response
		JSONObject jsonObject = new JSONObject(json);
		JSONArray jsonResults = jsonObject.getJSONArray("results");
		JSONObject jsonResult;
		JSONObject jsonGeometry;
		JSONObject jsonLocation;
		String jsonLatitude;
		String jsonLongitude;
		String jsonIcon;
		String jsonId;
		String jsonName;
		String jsonRating;
		String jsonReference;
		String jsonVicinity;
		JSONArray jsonTypes;
		List<String> jsonTypesList;
		
		for (int j = 0; j < jsonResults.length(); j++) {
			jsonResult = jsonResults.getJSONObject(j);
			
			// Get the informations about the latitude and the longitude
			jsonGeometry = (JSONObject)jsonResult.get("geometry");
			jsonLocation = (JSONObject)jsonGeometry.get("location");
			jsonLatitude = jsonLocation.has("lat") ? jsonLocation.getString("lat") : "0.0";
			jsonLongitude = jsonLocation.has("lng") ?jsonLocation.getString("lng") : "0.0";
			
			// Get the other attributes from the JSON
			jsonIcon = jsonResult.has("icon") ? jsonResult.getString("icon") : "";
			jsonId = jsonResult.has("id") ? jsonResult.getString("id") : "";
			jsonName = jsonResult.has("name") ? jsonResult.getString("name") : "";
			jsonRating = jsonResult.has("rating") ? jsonResult.getString("rating") : "0.0";
			jsonReference = jsonResult.has("reference") ? jsonResult.getString("reference") : "";
			jsonVicinity = jsonResult.has("vicinity") ? jsonResult.getString("vicinity") : "";
			
			// Construct the "types" array
			jsonTypesList = new ArrayList<String>();
			if (jsonResult.has("types")) {
				jsonTypes = jsonResult.getJSONArray("types"); 
				int len = jsonTypes.length();
				if (jsonTypes != null) { 
				   for (int i=0;i<len;i++){ 
				    jsonTypesList.add(jsonTypes.get(i).toString());
				   } 
				}
			}
			
			Log.d(TAG, "Latitude : " + Double.parseDouble(jsonLatitude));
			Log.d(TAG, "Longitude : " + Double.parseDouble(jsonLongitude));
			Log.d(TAG, "URI : " + new URI(jsonIcon).toString());
			Log.d(TAG, "Id : " + jsonId);
			Log.d(TAG, "Name : " + jsonName);
			Log.d(TAG, "Rating : " + Float.parseFloat(jsonRating));
			Log.d(TAG, "Reference : " + jsonReference);
			Log.d(TAG, "Nombre de Types : " + jsonTypesList.size());
			Log.d(TAG, "Vicinity : " + jsonVicinity);
			
			// Construct the Place object
			places.add(new Place(Double.parseDouble(jsonLatitude), Double.parseDouble(jsonLongitude), new URI(jsonIcon), jsonId, jsonName, Float.parseFloat(jsonRating), jsonReference, jsonTypesList, jsonVicinity));
		}
		
		Log.d(TAG, "Number of places found : " + places.size());
		
		return places;
	}
	
	/**
	 * Launch a SOAP request to the Places API
	 * Find points of interest around a specified location 
	 * @param latitude The latitude of the specified location
	 * @param longitude The longitude of the specified location
	 * @param radius The radius of the searched space
	 * @return A list of relevant places
	 */
	public List<Place> SearchPointOfInterestPlaces(double latitude, double longitude, double radius) {
		List<PlaceTypes> types = new ArrayList<PlaceTypes>();
		types.add(PlaceTypes.POINT_OF_INTERESTS);
		
		return SearchPlaces(latitude, longitude, radius, types);
	}
	
}
