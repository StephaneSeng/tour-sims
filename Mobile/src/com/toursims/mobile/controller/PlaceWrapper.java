package com.toursims.mobile.controller;

import java.io.InputStream;
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

import android.util.Log;

import com.toursims.mobile.model.places.Place;
import com.toursims.mobile.model.places.PlaceTypes;
import com.toursims.mobile.util.places.EasySSLSocketFactory;

public class PlaceWrapper {
	
	private static final String TAG = PlaceWrapper.class.toString();
	
	private static final String KEY = "AIzaSyDPcfMkBspS9GCNcDNnyhy9y6nhTM8_k3E";
	
	private static final boolean SENSOR = true;
	
	private HttpClient httpClient;
	
	public PlaceWrapper() {
		super();
		
		// Create a HTTP server with custom security
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

	public List<Place> SearchPlaces(double latitude, double longitude, double radius, List<PlaceTypes> types) {
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
		    
		    // TODO : Return management
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}
		
		return null;
	}
	
	public List<Place> SearchPointOfInterestPlaces(double latitude, double longitude, double radius) {
		List<PlaceTypes> types = new ArrayList<PlaceTypes>();
		types.add(PlaceTypes.POINT_OF_INTERESTS);
		
		return SearchPlaces(latitude, longitude, radius, types);
	}
	
}
