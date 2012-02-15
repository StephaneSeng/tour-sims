package com.toursims.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAuthorizationRequestUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;

public class LoginActivity extends Activity {
	
	/**
	 * Android debugging tag
	 */
	private final static String TAG = LoginActivity.class.toString();
	
	/**
	 * Google API variables
	 */
	private final String GOOGLE_CLIENT_ID = "350066665292.apps.googleusercontent.com";
	private final String GOOGLE_CLIENT_SECRET = "eFCnOdRrQTficUQyF1louck2";
	private final String GOOGLE_SCOPE = "https://www.googleapis.com/auth/plus.me";
	private final String GOOGLE_REDIRECT_URI = "http://localhost";
	
	/**
	 * Called when the activity is first created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.login);
	}

	/**
	 * Called when the activity is resumed
	 */
	@Override
	protected void onResume() {
		super.onResume();

        String googleAuthorizationRequestUrl = new GoogleAuthorizationRequestUrl(GOOGLE_CLIENT_ID, GOOGLE_REDIRECT_URI, GOOGLE_SCOPE).build();
        final WebView webView = (WebView)findViewById(R.id.webView);
        
        // Webview parameters
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				if (url.startsWith(GOOGLE_REDIRECT_URI)) {
					try {
						if (url.indexOf("code=") != -1) {
							// Url is like http://localhost/?code=4/Z5DgC1IxNL-muPsrE2Sjy9zQn2pF
							String code = url.substring(GOOGLE_REDIRECT_URI.length() + 7, url.length());

							AccessTokenResponse accessTokenResponse = new GoogleAuthorizationCodeGrant(
									new NetHttpTransport(),
									new JacksonFactory(),
									GOOGLE_CLIENT_ID,
									GOOGLE_CLIENT_SECRET,
									code,
									GOOGLE_REDIRECT_URI).execute();

							view.setVisibility(View.INVISIBLE);
							
							// Set up the HTTP transport and JSON factory
							HttpTransport httpTransport = new NetHttpTransport();
				        	JsonFactory jsonFactory = new JacksonFactory();
							
				        	// Set up OAuth 2.0 access of protected resources
				        	// using the refresh and access tokens, automatically
				        	// refreshing the access token when it expires
				        	GoogleAccessProtectedResource requestInitializer =
				        	    new GoogleAccessProtectedResource(accessTokenResponse.accessToken, httpTransport,
				        	    jsonFactory, GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET, accessTokenResponse.refreshToken);
				        	
							// Set up the main Google+ class
				        	Plus plus = Plus.builder(httpTransport, jsonFactory)
				        	    .setHttpRequestInitializer(requestInitializer)
				        	    .build();
				
				        	// Make a request to access your profile and display it to console
				        	Person profile = plus.people().get("me").execute();
				        	System.out.println("ID: " + profile.getId());
				        	System.out.println("Name: " + profile.getDisplayName());
				        	System.out.println("Image URL: " + profile.getImage().getUrl());
				        	System.out.println("Profile URL: " + profile.getUrl());
				        	
				        	// Update the user name in the application
				        	TourSims tourSims = (TourSims)getApplicationContext();
				        	tourSims.setUserName(profile.getDisplayName());
				        	
				        	finishActivity(0);
						}
					} catch (Exception e) {
						Log.e(TAG, e.getMessage());
						Log.e(TAG, e.toString());
					}
				}
			}
			
		});

		// send user to authorization page
		webView.loadUrl(googleAuthorizationRequestUrl);
        // GOOGLE API END
		
	}

}
