/*
 * BrowserFieldAuthDialogWrapper.java
 * 24/05/2011
 * Facebook API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 * GNU General Public License (GPL) Version 2, June 1991
 */
package com.emobtech.facebook.api.auth.ui.rim;

import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.browser.field2.BrowserFieldListener;

import org.w3c.dom.Document;

import com.emobtech.facebook.api.auth.AuthenticationListener;
import com.emobtech.facebook.api.auth.ui.AuthDialogWrapper;

/**
 * <p>
 * This class defines a wrapper for a BrowserField component, which will hold
 * the authentication process to Facebook. It is responsible for informing to
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
public final class BrowserFieldAuthDialogWrapper extends AuthDialogWrapper {
	/**
	 * <p>
	 * BrowserField object.
	 * </p>
	 */
	private BrowserField browserField;
	
	/**
	 * <p>
	 * Create an instance of BrowserFieldAuthDialogWrapper class.
	 * </p>
	 * @param browserField BrowserField to display Facebook's login page.
	 * @param appId App id.
	 * @param appSecret App secret.
	 * @param redirectUri Redirect uri.
	 * @param permissions Array of permissions.
	 * @param authListener Auth listener.
	 */
	public BrowserFieldAuthDialogWrapper(BrowserField browserField, 
		String appId, String appSecret,	String redirectUri, 
		String[] permissions, AuthenticationListener authListener) {
		super(appId, appSecret, redirectUri, permissions, authListener);
		//
		if (browserField == null) {
			throw new IllegalArgumentException(
				"BrowserField must not be null.");
		}
		//
		this.browserField = browserField;
		this.browserField.addListener(new BrowserFieldListenerAuth());
	}
	
	/**
	 * <p>
	 * Create an instance of BrowserFieldAuthDialogWrapper class.
	 * </p>
	 * @param browserField BrowserField to display Facebook's login page.
	 */
	public BrowserFieldAuthDialogWrapper(BrowserField browserField) {
		this(browserField, null, null, null, null, null);
	}

	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthDialogWrapper#loadUrl(java.lang.String)
	 */
	protected void loadUrl(String url) {
		browserField.requestContent(url);
	}
	
	/**
	 * @see com.emobtech.facebook.api.auth.ui.AuthDialogWrapper#loadHTML(java.lang.String)
	 */
	protected void loadHTML(String htmlContent) {
		browserField.displayContent(htmlContent, redirectUri);
	}
	
	/**
	 * @author ernandes@gmail.com
	 */
	private class BrowserFieldListenerAuth extends BrowserFieldListener {
		/**
		 * @see net.rim.device.api.browser.field2.BrowserFieldListener#documentLoaded(net.rim.device.api.browser.field2.BrowserField, org.w3c.dom.Document)
		 */
		public void documentLoaded(BrowserField browserField, Document document)
			throws Exception {
			trackCodeUrl(browserField.getDocumentUrl());
		}
	}
}
