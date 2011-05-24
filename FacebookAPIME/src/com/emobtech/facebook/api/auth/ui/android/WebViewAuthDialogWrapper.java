/*
 * WebViewAuthDialogWrapper.java
 * 24/05/2011
 * Facebook API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 * GNU General Public License (GPL) Version 2, June 1991
 */
package com.emobtech.facebook.api.auth.ui.android;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.emobtech.facebook.api.auth.AuthenticationListener;
import com.emobtech.facebook.api.auth.ui.AuthDialogWrapper;

/**
 * <p>
 * This class defines a wrapper for a WebView component, which will hold the 
 * authentication process to Facebook. It is responsible for informing to
 * Facebook all the parameters about the application that is trying to get 
 * access to a given user account.
 * </p>
 * <p>
 * In addition, this class also tracks the process in order to identify when the 
 * authorization is granted or denied, so it can notify the application. This 
 * notification is done throught a listener object that implements 
 * {@link AuthenticationListener}. 
 * </p>
 * <p>
 * Before using this class, the developer must register an app on Facebook
 * Developer website.
 * </p>
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class WebViewAuthDialogWrapper extends AuthDialogWrapper {
	/**
	 * <p>
	 * WebView object.
	 * </p>
	 */
	private WebView webView;
	
	/**
	 * <p>
	 * Create an instance of WebViewAuthDialogWrapper class.
	 * </p>
	 * @param webView WebView to display Facebook's login page.
	 * @param appId App id.
	 * @param appSecret App secret.
	 * @param redirectUri Redirect uri.
	 * @param permissions Array of permissions.
	 * @param authListener Auth listener.
	 */
	public WebViewAuthDialogWrapper(WebView webView, String appId,
		String appSecret, String redirectUri, String[] permissions, 
		AuthenticationListener authListener) {
		super(appId, appSecret, redirectUri, permissions, authListener);
		//
		if (webView == null) {
			throw new IllegalArgumentException("WebView must not be null.");
		}
		//
		this.webView = webView;
		this.webView.setWebViewClient(new WebViewClientAuth());
	}
	
	/**
	 * <p>
	 * Create an instance of WebViewAuthDialogWrapper class.
	 * </p>
	 * @param webView WebView to display Facebook's login page.
	 */
	public WebViewAuthDialogWrapper(WebView webView) {
		this(webView, null, null, null, null, null);
	}

	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthDialogWrapper#loadUrl(java.lang.String)
	 */
	protected void loadUrl(String url) {
		webView.loadUrl(url);
	}
	
	/**
	 * @author ernandes@gmail.com
	 */
	private class WebViewClientAuth extends WebViewClient {
		/**
		 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView, java.lang.String, android.graphics.Bitmap)
		 */
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			trackCodeUrl(url);
		}
	}
}
