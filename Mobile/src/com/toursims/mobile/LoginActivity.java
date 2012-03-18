package com.toursims.mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
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
import com.toursims.mobile.controller.UserWrapper;
import com.toursims.mobile.model.User;

public class LoginActivity extends SherlockActivity {

	/**
	 * Android debugging tag
	 */
	private final static String TAG = LoginActivity.class.toString();

	/**
	 * Preferences key
	 */
	public final static String LOGIN_PREFERENCES = LoginActivity.class.toString();
	public final static String LOGIN_PREFERENCES_ID = "LOGIN_PREFERENCES_ID";
	public final static String LOGIN_PREFERENCES_NAME = "LOGIN_PREFERENCES_NAME";
	public final static String LOGIN_PREFERENCES_IMAGE = "LOGIN_PREFERENCES_IMAGE";
	public final static String LOGIN_PREFERENCES_LOGGEDIN = "LOGIN_PREFERENCES_ISLOGGEDIN";

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
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// Scrollbar workaround
		// See:
		// http://stackoverflow.com/questions/2279978/webview-showing-white-bar-on-right-side
		WebView webView = (WebView) findViewById(R.id.webView);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		// ActionBarSherlock setup
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(R.drawable.ic_menu_login_white);
		actionBar.setTitle(R.string.home_social_login);
	}

	/**
	 * Called when the activity is resumed
	 */
	@Override
	protected void onResume() {
		super.onResume();

		String googleAuthorizationRequestUrl = new GoogleAuthorizationRequestUrl(GOOGLE_CLIENT_ID, GOOGLE_REDIRECT_URI, GOOGLE_SCOPE).setApprovalPrompt("auto").build();
		final WebView webView = (WebView) findViewById(R.id.webView);

		// Webview parameters
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				setSupportProgressBarIndeterminateVisibility(true);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				setSupportProgressBarIndeterminateVisibility(false);

				if (url.startsWith(GOOGLE_REDIRECT_URI)) {
					try {
						if (url.indexOf("code=") != -1) {
							// Url is like :
							// http://localhost/?code=4/Z5DgC1IxNL-muPsrE2Sjy9zQn2pF
							String code = url.substring(GOOGLE_REDIRECT_URI.length() + 7, url.length());

							AccessTokenResponse accessTokenResponse = new GoogleAuthorizationCodeGrant(new NetHttpTransport(), new JacksonFactory(), GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET, code,
									GOOGLE_REDIRECT_URI).execute();

							view.setVisibility(View.INVISIBLE);

							// Set up the HTTP transport and JSON factory
							HttpTransport httpTransport = new NetHttpTransport();
							JsonFactory jsonFactory = new JacksonFactory();

							// Set up OAuth 2.0 access of protected resources
							// using the refresh and access tokens,
							// automatically refreshing the access token when it
							// expires
							GoogleAccessProtectedResource requestInitializer = new GoogleAccessProtectedResource(accessTokenResponse.accessToken, httpTransport, jsonFactory, GOOGLE_CLIENT_ID,
									GOOGLE_CLIENT_SECRET, accessTokenResponse.refreshToken);

							// Set up the main Google+ class
							Plus plus = Plus.builder(httpTransport, jsonFactory).setHttpRequestInitializer(requestInitializer).build();

							// Make a request to access your profile and display
							// it to console
							Person profile = plus.people().get("me").execute();
							System.out.println("ID: " + profile.getId());
							System.out.println("Name: " + profile.getDisplayName());
							System.out.println("Image URL: " + profile.getImage().getUrl());
							System.out.println("Profile URL: " + profile.getUrl());

							// Call our own webservice for the authentication
							UserWrapper userWrapper = new UserWrapper(getApplicationContext());
							User user = userWrapper.AuthenticateUser(profile.getDisplayName(), profile.getImage().getUrl(), 1, profile.getId());

							// Update the user name in the application
							TourSims tourSims = (TourSims) getApplicationContext();
							tourSims.setUser(user);
							tourSims.setUserLoggedIn(true);

							// Save the user information in the Preferences
							SharedPreferences loginSettings = getSharedPreferences(LOGIN_PREFERENCES, 0);
							SharedPreferences.Editor editor = loginSettings.edit();
							editor.putInt(LOGIN_PREFERENCES_ID, user.getUserId());
							editor.putString(LOGIN_PREFERENCES_NAME, user.getName());
							editor.putString(LOGIN_PREFERENCES_IMAGE, user.getAvatar());
							editor.putBoolean(LOGIN_PREFERENCES_LOGGEDIN, true);
							editor.commit();

							// Clean the user session
							android.webkit.CookieManager.getInstance().removeAllCookie();
							finishActivity(0);
							finish();
						}
					} catch (Exception e) {
						// Log.e(TAG, e.getMessage());
						Log.e(TAG, e.toString());
					}
				}
			}

		});

		// send user to authorization page
		webView.loadUrl(googleAuthorizationRequestUrl);
		// GOOGLE API END

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;

		// Handle item selection
		switch (item.getItemId()) {
		case android.R.id.home:
			intent = new Intent(this, HomeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
