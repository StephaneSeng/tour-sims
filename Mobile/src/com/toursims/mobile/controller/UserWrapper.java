package com.toursims.mobile.controller;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.net.Uri;
import android.util.Log;

import com.toursims.mobile.R;
import com.toursims.mobile.model.Checkin;
import com.toursims.mobile.model.User;
import com.toursims.mobile.util.places.EasySSLSocketFactory;

/**
 * Wrapper for the User related webservices
 */
public class UserWrapper {

	/**
	 * Android debugging tag
	 */
	private static final String TAG = UserWrapper.class.toString();

	/**
	 * Application server root
	 */
	private String serverRoot;

	/**
	 * Our HTTP client, used for making requests
	 */
	private HttpClient httpClient;

	/**
	 * Default constructor Initialize the HTTP client, we use a less secure one
	 */
	public UserWrapper(Context context) {
		super();

		serverRoot = context.getString(R.string.server_root);

		// Create a HTTP server with minor security
		// Source :
		// http://www.virtualzone.de/2011-02-27/how-to-use-apache-httpclient-with-httpsssl-on-android
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));

		HttpParams params = new BasicHttpParams();
		params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(30));
		params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);

		// Modifications on the HTTP client for solving the
		// "Connection still allocated" problem
		// See:
		// http://iamvijayakumar.blogspot.com/2012/02/invalid-use-of-singleclientconnmanager.html
		this.httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, cm.getSchemeRegistry()), params);
	}

	/**
	 * Launch a SOAP request to the User webservice Authenticate the user with
	 * its SSO informations
	 * 
	 * @param name
	 *            The user's usual name that he has defined with his SSO
	 *            provider
	 * @param avatar
	 *            A link to the user's profile picture defined with his SSO
	 *            provider
	 * @param sso_id
	 *            The SSO provider id that have been chosen by the user
	 * @param sso_name
	 *            The SSO name that the SSO provider has returned
	 */
	public User AuthenticateUser(String name, String avatar, int sso_id, String sso_name) {
		// Return variable
		User user = null;

		// Build the SOAP request
		StringBuffer request = new StringBuffer(serverRoot + "/user.php?");
		request.append("action=" + "authenticate");
		request.append("&name=" + Uri.encode(name));
		request.append("&avatar=" + Uri.encode(avatar));
		request.append("&sso_id=" + sso_id);
		request.append("&sso_name=" + sso_name);

		Log.d(TAG, "Launching a User request : " + request);
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
			user = jsonResponseParserUser(json).get(0);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}

		return user;
	}

	/**
	 * Launch a SOAP request to the User webservice Retreive the shared
	 * informations about the current user
	 * 
	 * @param user_id
	 *            The id of the user to work with
	 */
	public User GetProfile(int user_id) {
		// Return variable
		User user = null;

		// Build the SOAP request
		StringBuffer request = new StringBuffer(serverRoot + "/user.php?");
		request.append("action=" + "get_profile");
		request.append("&user_id=" + user_id);

		Log.d(TAG, "Launching a User request : " + request);
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
			user = jsonResponseParserUser(json).get(0);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}

		return user;
	}

	/**
	 * Launch a SOAP request to the User webservice List all the contacts linked
	 * to the specified user
	 * 
	 * @param user_id
	 *            The id of the user to work with
	 */
	public List<User> GetContacts(int user_id) {
		// Return variable
		List<User> users = null;

		// Build the SOAP request
		StringBuffer request = new StringBuffer(serverRoot + "/user.php?");
		request.append("action=" + "get_contacts");
		request.append("&user_id=" + user_id);

		Log.d(TAG, "Launching a User request : " + request);
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
			users = jsonResponseParserUser(json);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}

		return users;
	}

	/**
	 * Launch a SOAP request to the User webservice Create a checkin for the
	 * current user
	 * 
	 * @param latitude
	 *            The latitude of the specified location
	 * @param longitude
	 *            The longitude of the specified location
	 * @param user_id
	 *            The id of the current user
	 */
	public void CreateCheckin(double latitude, double longitude, int user_id) {
		// Timestamp formatting
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SZ");

		// Build the SOAP request
		StringBuffer request = new StringBuffer(serverRoot + "/user.php?");
		request.append("action=" + "checkin");
		request.append("&latitude=" + latitude);
		request.append("&longitude=" + longitude);
		request.append("&timestamp=" + Uri.encode(simpleDateFormat.format(Calendar.getInstance().getTime())));
		request.append("&user_id=" + user_id);

		Log.d(TAG, "Launching a User request : " + request);
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
	 * Launch a SOAP request to the User webservice Find users around a
	 * specified location
	 * 
	 * @param latitude
	 *            The latitude of the specified location
	 * @param longitude
	 *            The longitude of the specified location
	 * @return A list of relevant checkins
	 */
	public List<Checkin> SearchCheckins(double latitude, double longitude, int user_id) {
		// Return variable
		List<Checkin> checkins = new ArrayList<Checkin>();

		// Build the SOAP request
		StringBuffer request = new StringBuffer(serverRoot + "/user.php?");
		request.append("action=" + "get_nearby_checkins");
		request.append("&latitude=" + latitude);
		request.append("&longitude=" + longitude);
		request.append("&user_id=" + user_id);

		Log.d(TAG, "Launching a User request : " + request);
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
			checkins = jsonResponseParserCheckin(json);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			Log.e(TAG, e.toString());
		}

		return checkins;
	}

	/**
	 * Utility for parsing the JSON response from the User webservice
	 * 
	 * @param json
	 *            The response JSON to parse
	 * @return A list of users
	 */
	private List<User> jsonResponseParserUser(String json) throws JSONException, URISyntaxException {
		List<User> users = new ArrayList<User>();

		// Variables used for reading the JSON response
		JSONArray jsonResults = new JSONArray(json);
		JSONObject jsonResult;
		String jsonUserId;
		String jsonName;
		String jsonAvatar;
		String jsonSharePosition;
		String jsonIsGuide;

		for (int j = 0; j < jsonResults.length(); j++) {
			jsonResult = jsonResults.getJSONObject(j);

			// Get the attributes from the JSON
			jsonUserId = jsonResult.has("user_id") ? jsonResult.getString("user_id") : "0";
			jsonName = jsonResult.has("name") ? jsonResult.getString("name") : "";
			jsonAvatar = jsonResult.has("avatar") ? jsonResult.getString("avatar") : "";
			jsonSharePosition = jsonResult.has("share_position") ? jsonResult.getString("share_position") : "";
			jsonIsGuide = jsonResult.has("is_guide") ? jsonResult.getString("is_guide") : "";

			Log.d(TAG, "user_id : " + jsonUserId);
			Log.d(TAG, "name : " + jsonName);
			Log.d(TAG, "avatar : " + jsonAvatar);
			Log.d(TAG, "share_position : " + jsonSharePosition);
			Log.d(TAG, "is_guide : " + jsonIsGuide);

			// Construct the User object
			// users.add(new User(Integer.parseInt(jsonUserId), jsonName,
			// jsonAvatar, jsonSharePosition.equals(JSON_TRUE),
			// jsonIsGuide.equals(JSON_TRUE)));
			users.add(new User(Integer.parseInt(jsonUserId), jsonName, jsonAvatar));
		}

		Log.d(TAG, "Nombre de Users : " + users.size());

		return users;
	}

	/**
	 * Utility for parsing the JSON response from the User webservice
	 * 
	 * @param json
	 *            The response JSON to parse
	 * @return A list of checkins
	 */
	private List<Checkin> jsonResponseParserCheckin(String json) throws JSONException, URISyntaxException {
		// Timestamp formatting
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy -MM -dd HH:mm:ss.S Z");

		List<Checkin> checkins = new ArrayList<Checkin>();

		// Variables used for reading the JSON response
		JSONArray jsonResults = new JSONArray(json);
		JSONObject jsonResult;
		String jsonUserId;
		String jsonName;
		String jsonAvatar;
		String jsonLatitude;
		String jsonLongitude;
		String jsonTimestamp;

		for (int j = 0; j < jsonResults.length(); j++) {
			jsonResult = jsonResults.getJSONObject(j);

			// Get the attributes from the JSON
			jsonUserId = jsonResult.has("user_id") ? jsonResult.getString("user_id") : "";
			jsonName = jsonResult.has("name") ? jsonResult.getString("name") : "";
			jsonAvatar = jsonResult.has("avatar") ? jsonResult.getString("avatar") : "";
			jsonLatitude = jsonResult.has("latitude") ? jsonResult.getString("latitude") : "0.0";
			jsonLongitude = jsonResult.has("longitude") ? jsonResult.getString("longitude") : "0.0";
			jsonTimestamp = jsonResult.has("timestamp") ? jsonResult.getString("timestamp") : "";

			Log.d(TAG, "user_id : " + jsonUserId);
			Log.d(TAG, "name : " + jsonName);
			Log.d(TAG, "avatar : " + jsonAvatar);
			Log.d(TAG, "latitude : " + jsonLatitude);
			Log.d(TAG, "longitude : " + jsonLongitude);
			Log.d(TAG, "timestamp : " + jsonTimestamp);

			// Construct the Checkin object
			try {
				// We have to append "00" to the timestamp for the compatibility
				// with PostgreSQL
				// We are also adding spaces between the date items for the same
				// reasons
				checkins.add(new Checkin(Integer.parseInt(jsonUserId), jsonName, jsonAvatar, Double.parseDouble(jsonLatitude), Double.parseDouble(jsonLongitude), simpleDateFormat.parse(jsonTimestamp
						.replace("+", " +").replace("-", " -") + "00")));
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				Log.e(TAG, e.toString());
			}
		}

		Log.d(TAG, "Nombre de Checkins : " + checkins.size());

		return checkins;
	}

}
